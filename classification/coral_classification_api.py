#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
珊瑚分类API服务
提供RESTful API接口，用于接收图片并返回珊瑚健康状态预测结果
"""

import os
import logging
import numpy as np
import tensorflow as tf
from flask import Flask, request, jsonify
from werkzeug.utils import secure_filename
import json
from datetime import datetime
import cv2
from PIL import Image, ImageEnhance

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('coral_classification.log', encoding='utf-8'),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)

# 创建Flask应用
app = Flask(__name__)
app.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024  # 限制上传文件大小为16MB

# 配置
UPLOAD_FOLDER = 'uploads'
ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg', 'gif', 'bmp'}
MODEL_PATH = 'mode/model.h5'

# 确保上传目录存在
os.makedirs(UPLOAD_FOLDER, exist_ok=True)

# 全局变量存储模型和类别名称
model = None
class_names = ['bleached_corals', 'healthy_corals']  # 根据训练数据确定的类别

def allowed_file(filename):
    """检查文件扩展名是否允许"""
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

def calculate_image_quality(image_path):
    """计算图片质量指标"""
    try:
        # 使用OpenCV读取图片
        img = cv2.imread(image_path)
        if img is None:
            return None, "无法读取图片文件"
        
        # 转换为灰度图
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        
        # 计算拉普拉斯方差（模糊度检测）
        laplacian_var = cv2.Laplacian(gray, cv2.CV_64F).var()
        
        # 计算图片亮度
        brightness = np.mean(gray)
        
        # 计算对比度
        contrast = gray.std()
        
        # 计算图片清晰度（基于边缘检测）
        edges = cv2.Canny(gray, 50, 150)
        edge_density = np.sum(edges > 0) / (edges.shape[0] * edges.shape[1])
        
        return {
            'laplacian_var': float(laplacian_var),
            'brightness': float(brightness),
            'contrast': float(contrast),
            'edge_density': float(edge_density)
        }, None
    except Exception as e:
        return None, f"图片质量检测失败: {str(e)}"

def detect_coral_presence(image_path):
    """检测图片中是否包含珊瑚特征"""
    try:
        # 使用OpenCV读取图片
        img = cv2.imread(image_path)
        if img is None:
            return False, "无法读取图片文件"
        
        # 转换为HSV色彩空间
        hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
        
        # 定义更广泛的珊瑚颜色范围 - 珊瑚可以是任何颜色！
        coral_ranges = [
            # 红色到橙色
            (np.array([0, 30, 30]), np.array([10, 255, 255])),
            (np.array([170, 30, 30]), np.array([180, 255, 255])),
            # 黄色到橙色
            (np.array([10, 30, 30]), np.array([25, 255, 255])),
            # 绿色（某些珊瑚）
            (np.array([40, 30, 30]), np.array([80, 255, 255])),
            # 蓝色到青色
            (np.array([100, 30, 30]), np.array([130, 255, 255])),
            # 紫色到粉色
            (np.array([130, 30, 30]), np.array([170, 255, 255])),
            # 白色到浅色（白化珊瑚）
            (np.array([0, 0, 180]), np.array([180, 30, 255])),
            # 棕色到米色
            (np.array([10, 30, 30]), np.array([25, 255, 200])),
            (np.array([0, 0, 100]), np.array([20, 100, 255]))
        ]
        
        # 计算所有颜色范围的掩码
        all_masks = []
        for lower, upper in coral_ranges:
            mask = cv2.inRange(hsv, lower, upper)
            all_masks.append(mask)
        
        # 合并所有掩码
        total_mask = np.zeros_like(all_masks[0])
        for mask in all_masks:
            total_mask = cv2.bitwise_or(total_mask, mask)
        
        # 计算珊瑚相关像素的比例
        coral_pixels = np.sum(total_mask > 0)
        total_pixels = img.shape[0] * img.shape[1]
        coral_ratio = coral_pixels / total_pixels
        
        # 更宽松的阈值，因为珊瑚颜色多样
        has_coral_by_color = coral_ratio > 0.01  # 1%的阈值，更宽松
        
        # 额外的检测方法：检测纹理和形状特征
        has_coral_by_texture = detect_coral_texture(img)
        
        # 如果颜色检测或纹理检测通过，就认为有珊瑚
        has_coral = has_coral_by_color and has_coral_by_texture
        
        return has_coral, coral_ratio
    except Exception as e:
        return False, f"珊瑚检测失败: {str(e)}"

def detect_coral_texture(img):
    """通过纹理特征检测珊瑚"""
    try:
        # 转换为灰度图
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        
        # 使用边缘检测
        edges = cv2.Canny(gray, 50, 150)
        
        # 计算边缘密度
        edge_density = np.sum(edges > 0) / (edges.shape[0] * edges.shape[1])
        
        # 使用形态学操作检测圆形和分支结构
        kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (5, 5))
        closed = cv2.morphologyEx(edges, cv2.MORPH_CLOSE, kernel)
        
        # 查找轮廓
        contours, _ = cv2.findContours(closed, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        
        # 分析轮廓特征
        coral_like_features = 0
        for contour in contours:
            if cv2.contourArea(contour) > 100:  # 过滤小轮廓
                # 计算轮廓的圆形度
                perimeter = cv2.arcLength(contour, True)
                if perimeter > 0:
                    circularity = 4 * np.pi * cv2.contourArea(contour) / (perimeter * perimeter)
                    # 珊瑚通常有中等圆形度（不是完全圆形，也不是完全不规则）
                    if 0.3 < circularity < 0.8:
                        coral_like_features += 1
        
        # 如果有足够的珊瑚样特征，认为检测到珊瑚
        return coral_like_features > 2 or edge_density > 0.02
        
    except Exception as e:
        return False

def detect_coral_fallback(image_path):
    """更宽松的珊瑚检测方法"""
    try:
        img = cv2.imread(image_path)
        if img is None:
            return False
        
        # 转换为灰度图
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        
        # 使用更宽松的边缘检测
        edges = cv2.Canny(gray, 30, 100)
        edge_density = np.sum(edges > 0) / (edges.shape[0] * edges.shape[1])
        
        # 检测是否有足够的纹理特征
        if edge_density > 0.01:  # 1%的边缘密度
            return True
        
        # 检测图片中是否有足够的颜色变化
        color_std = np.std(gray)
        if color_std > 20:  # 有足够的颜色变化
            return True
        
        # 检测是否有圆形或椭圆形结构
        contours, _ = cv2.findContours(edges, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        for contour in contours:
            if cv2.contourArea(contour) > 50:  # 更小的面积阈值
                perimeter = cv2.arcLength(contour, True)
                if perimeter > 0:
                    circularity = 4 * np.pi * cv2.contourArea(contour) / (perimeter * perimeter)
                    if circularity > 0.2:  # 更宽松的圆形度
                        return True
        
        return False
        
    except Exception as e:
        return False

def validate_image_quality(image_path):
    """验证图片质量，返回质量问题和建议"""
    issues = []
    suggestions = []
    
    # 检测图片质量
    quality_metrics, error = calculate_image_quality(image_path)
    if error:
        return False, [error], ["请检查图片文件是否损坏"]
    
    # 检查模糊度（降低阈值）
    if quality_metrics['laplacian_var'] < 50:
            issues.append("Image is too blurry")
            suggestions.append("Please upload a clearer image")
    
    # 检查亮度（放宽范围）
    if quality_metrics['brightness'] < 20:
            issues.append("Image is too dark")
            suggestions.append("Please ensure the image has sufficient brightness")
    elif quality_metrics['brightness'] > 220:
            issues.append("Image is too bright")
            suggestions.append("Please adjust the image brightness")
    
    # 检查对比度（降低阈值）
    if quality_metrics['contrast'] < 20:
            issues.append("Insufficient image contrast")
            suggestions.append("Please upload an image with higher contrast")
    
    # 检查边缘密度（清晰度，降低阈值）
    if quality_metrics['edge_density'] < 0.005:
            issues.append("Image lacks detail")
            suggestions.append("Please upload an image with more details")
    
    # 检测珊瑚存在
    has_coral, coral_ratio = detect_coral_presence(image_path)
    if not has_coral:
        # 如果颜色检测失败，尝试更宽松的检测
        has_coral_fallback = detect_coral_fallback(image_path)
        if not has_coral_fallback:
            issues.append("No coral features detected")
            suggestions.append("Please ensure the image contains coral and re-upload")
    
    # 如果图片太小，可能影响检测
    img = cv2.imread(image_path)
    if img is not None:
        height, width = img.shape[:2]
        if height < 100 or width < 100:
            issues.append("Image size too small")
            suggestions.append("Please upload a higher resolution image")
    
    return len(issues) == 0, issues, suggestions

def load_model():
    """加载训练好的模型"""
    global model
    try:
        if os.path.exists(MODEL_PATH):
            model = tf.keras.models.load_model(MODEL_PATH)
            logger.info(f"模型加载成功: {MODEL_PATH}")
            return True
        else:
            logger.error(f"模型文件不存在: {MODEL_PATH}")
            return False
    except Exception as e:
        logger.error(f"模型加载失败: {str(e)}")
        return False

def preprocess_image(image_path, target_size=(180, 180)):
    """预处理图片"""
    try:
        # 使用TensorFlow加载和预处理图片
        img = tf.keras.utils.load_img(image_path, target_size=target_size)
        img_array = tf.keras.utils.img_to_array(img)
        img_array = tf.expand_dims(img_array, 0)  # 创建批次维度
        return img_array
    except Exception as e:
        logger.error(f"图片预处理失败: {str(e)}")
        raise

def predict_coral_health(image_array):
    """使用模型进行预测"""
    try:
        # 进行预测
        predictions = model.predict(image_array, verbose=0)
        result_probs = predictions[0]  # 获取第一个（也是唯一一个）预测结果
        
        # 计算每个类别的概率
        probabilities = {}
        for i, class_name in enumerate(class_names):
            probabilities[class_name] = float(result_probs[i])
        
        # 找到概率最高的类别
        max_index = np.argmax(result_probs)
        predicted_class = class_names[max_index]
        confidence = float(result_probs[max_index])
        
        return {
            'predicted_class': predicted_class,
            'confidence': confidence,
            'probabilities': probabilities
        }
    except Exception as e:
        logger.error(f"预测失败: {str(e)}")
        raise

@app.route('/health', methods=['GET'])
def health_check():
    """健康检查接口"""
    return jsonify({
        'status': 'healthy',
        'timestamp': datetime.now().isoformat(),
        'model_loaded': model is not None
    })

@app.route('/predict', methods=['POST'])
def predict():
    """珊瑚分类预测接口"""
    try:
        # 检查是否有文件上传
        if 'image' not in request.files:
            return jsonify({
                'success': False,
                'error': 'No image file uploaded',
                'message': 'Please upload an image file using multipart/form-data format with field name "image"'
            }), 400
        
        file = request.files['image']
        
        # 检查文件是否为空
        if file.filename == '':
            return jsonify({
                'success': False,
                'error': 'No file selected',
                'message': 'Please select an image file'
            }), 400
        
        # 检查文件类型
        if not allowed_file(file.filename):
            return jsonify({
                'success': False,
                'error': 'Unsupported file type',
                'message': f'Supported file types: {", ".join(ALLOWED_EXTENSIONS)}'
            }), 400
        
        # 检查模型是否已加载
        if model is None:
            return jsonify({
                'success': False,
                'error': 'Model not loaded',
                'message': 'Please check if the model file exists'
            }), 500
        
        # 保存上传的文件
        filename = secure_filename(file.filename)
        timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
        filename = f"{timestamp}_{filename}"
        filepath = os.path.join(UPLOAD_FOLDER, filename)
        file.save(filepath)
        
        logger.info(f"收到图片上传: {filename}")
        
        try:
            # 验证图片质量
            is_valid, quality_issues, suggestions = validate_image_quality(filepath)
            
            if not is_valid:
                logger.warning(f"图片质量验证失败: {filename} - 问题: {quality_issues}")
                return jsonify({
                    'success': False,
                    'error': 'Image quality does not meet requirements',
                    'data': {
                        'issues': quality_issues,
                        'suggestions': suggestions,
                        'timestamp': datetime.now().isoformat()
                    },
                    'message': 'Please re-upload the image according to the suggestions'
                }), 400
            
            # 预处理图片
            image_array = preprocess_image(filepath)
            
            # 进行预测
            prediction_result = predict_coral_health(image_array)
            
            # 构建响应
            response = {
                'success': True,
                'data': {
                    'predicted_class': prediction_result['predicted_class'],
                    'confidence': prediction_result['confidence'],
                    'probabilities': prediction_result['probabilities'],
                    'class_names': class_names,
                    'timestamp': datetime.now().isoformat()
                },
                'message': 'Prediction successful'
            }
            
            logger.info(f"预测完成: {filename} -> {prediction_result['predicted_class']} (置信度: {prediction_result['confidence']:.4f})")
            
            return jsonify(response)
            
        finally:
            # 清理上传的文件
            try:
                if os.path.exists(filepath):
                    os.remove(filepath)
                    logger.info(f"已清理临时文件: {filename}")
            except Exception as e:
                logger.warning(f"清理临时文件失败: {str(e)}")
    
    except Exception as e:
        logger.error(f"预测接口错误: {str(e)}")
        return jsonify({
            'success': False,
            'error': 'Internal server error',
            'message': str(e)
        }), 500

@app.route('/model/info', methods=['GET'])
def model_info():
    """获取模型信息"""
    if model is None:
        return jsonify({
            'success': False,
            'error': 'Model not loaded'
        }), 500
    
    return jsonify({
        'success': True,
        'data': {
            'model_path': MODEL_PATH,
            'class_names': class_names,
            'num_classes': len(class_names),
            'input_shape': model.input_shape,
            'model_loaded': True
        }
    })

@app.route('/validate-image', methods=['POST'])
def validate_image():
    """图片质量验证接口"""
    try:
        # 检查是否有文件上传
        if 'image' not in request.files:
            return jsonify({
                'success': False,
                'error': 'No image file uploaded',
                'message': 'Please upload an image file using multipart/form-data format with field name "image"'
            }), 400
        
        file = request.files['image']
        
        # 检查文件是否为空
        if file.filename == '':
            return jsonify({
                'success': False,
                'error': 'No file selected',
                'message': 'Please select an image file'
            }), 400
        
        # 检查文件类型
        if not allowed_file(file.filename):
            return jsonify({
                'success': False,
                'error': 'Unsupported file type',
                'message': f'Supported file types: {", ".join(ALLOWED_EXTENSIONS)}'
            }), 400
        
        # 保存上传的文件
        filename = secure_filename(file.filename)
        timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
        filename = f"{timestamp}_{filename}"
        filepath = os.path.join(UPLOAD_FOLDER, filename)
        file.save(filepath)
        
        logger.info(f"收到图片质量验证请求: {filename}")
        
        try:
            # 验证图片质量
            is_valid, quality_issues, suggestions = validate_image_quality(filepath)
            
            # 获取详细的质量指标
            quality_metrics, error = calculate_image_quality(filepath)
            coral_detected, coral_ratio = detect_coral_presence(filepath)
            
            response = {
                'success': True,
                'data': {
                    'is_valid': is_valid,
                    'issues': quality_issues,
                    'suggestions': suggestions,
                    'quality_metrics': quality_metrics if quality_metrics else None,
                    'coral_detected': coral_detected,
                    'coral_ratio': float(coral_ratio) if isinstance(coral_ratio, (int, float)) else None,
                    'timestamp': datetime.now().isoformat()
                },
                'message': 'Image quality validation completed'
            }
            
            logger.info(f"Image quality validation completed: {filename} - 有效: {is_valid}")
            
            return jsonify(response)
            
        finally:
            # 清理上传的文件
            try:
                if os.path.exists(filepath):
                    os.remove(filepath)
                    logger.info(f"已清理临时文件: {filename}")
            except Exception as e:
                logger.warning(f"清理临时文件失败: {str(e)}")
    
    except Exception as e:
        logger.error(f"图片质量验证接口错误: {str(e)}")
        return jsonify({
            'success': False,
            'error': 'Internal server error',
            'message': str(e)
        }), 500

@app.errorhandler(413)
def too_large(e):
    """File too large错误处理"""
    return jsonify({
        'success': False,
        'error': 'File too large',
        'message': 'Uploaded file size cannot exceed 16MB'
    }), 413

@app.errorhandler(404)
def not_found(e):
    """404错误处理"""
    return jsonify({
        'success': False,
        'error': 'API endpoint does not exist',
        'message': 'Please check if the requested URL is correct'
    }), 404

@app.errorhandler(500)
def internal_error(e):
    """500错误处理"""
    return jsonify({
        'success': False,
        'error': 'Internal server error',
        'message': 'Please contact the administrator'
    }), 500

if __name__ == '__main__':
    # 启动时加载模型
    logger.info("Starting coral classification API service...")
    
    if load_model():
        logger.info("Model loaded successfully, starting service...")
        app.run(
            host='0.0.0.0',
            port=5000,
            debug=False,
            threaded=True
        )
    else:
        logger.error("Model loading failed, service cannot start")
        exit(1)
