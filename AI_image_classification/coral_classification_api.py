#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Coral Classification API Service
Provides RESTful API endpoints for receiving images and returning coral health status predictions
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

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('coral_classification.log', encoding='utf-8'),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)

# Create Flask application
app = Flask(__name__)
app.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024  # Limit upload file size to 16MB

# Configuration
UPLOAD_FOLDER = 'uploads'
ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg', 'gif', 'bmp'}
MODEL_PATH = 'mode/model.h5'

# Ensure upload directory exists
os.makedirs(UPLOAD_FOLDER, exist_ok=True)

# Global variables to store model and class names
model = None
class_names = ['bleached_corals', 'healthy_corals']  # Class names determined by training data

def allowed_file(filename):
    """Check if file extension is allowed"""
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

def calculate_image_quality(image_path):
    """Calculate image quality metrics"""
    try:
        # Read image using OpenCV
        img = cv2.imread(image_path)
        if img is None:
            return None, "Cannot read image file"
        
        # Convert to grayscale
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        
        # Calculate Laplacian variance (blur detection)
        laplacian_var = cv2.Laplacian(gray, cv2.CV_64F).var()
        
        # Calculate image brightness
        brightness = np.mean(gray)
        
        # Calculate contrast
        contrast = gray.std()
        
        # Calculate image sharpness (based on edge detection)
        edges = cv2.Canny(gray, 50, 150)
        edge_density = np.sum(edges > 0) / (edges.shape[0] * edges.shape[1])
        
        return {
            'laplacian_var': float(laplacian_var),
            'brightness': float(brightness),
            'contrast': float(contrast),
            'edge_density': float(edge_density)
        }, None
    except Exception as e:
        return None, f"Image quality detection failed: {str(e)}"

def detect_coral_presence(image_path):
    """Detect if image contains coral features"""
    try:
        # Read image using OpenCV
        img = cv2.imread(image_path)
        if img is None:
            return False, "Cannot read image file"
        
        # Convert to HSV color space
        hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
        
        # Define broader coral color ranges - corals can be any color!
        coral_ranges = [
            # Red to orange
            (np.array([0, 30, 30]), np.array([10, 255, 255])),
            (np.array([170, 30, 30]), np.array([180, 255, 255])),
            # Yellow to orange
            (np.array([10, 30, 30]), np.array([25, 255, 255])),
            # Green (some corals)
            (np.array([40, 30, 30]), np.array([80, 255, 255])),
            # Blue to cyan
            (np.array([100, 30, 30]), np.array([130, 255, 255])),
            # Purple to pink
            (np.array([130, 30, 30]), np.array([170, 255, 255])),
            # White to light colors (bleached corals)
            (np.array([0, 0, 180]), np.array([180, 30, 255])),
            # Brown to beige
            (np.array([10, 30, 30]), np.array([25, 255, 200])),
            (np.array([0, 0, 100]), np.array([20, 100, 255]))
        ]
        
        # Calculate masks for all color ranges
        all_masks = []
        for lower, upper in coral_ranges:
            mask = cv2.inRange(hsv, lower, upper)
            all_masks.append(mask)
        
        # Merge all masks
        total_mask = np.zeros_like(all_masks[0])
        for mask in all_masks:
            total_mask = cv2.bitwise_or(total_mask, mask)
        
        # Calculate ratio of coral-related pixels
        coral_pixels = np.sum(total_mask > 0)
        total_pixels = img.shape[0] * img.shape[1]
        coral_ratio = coral_pixels / total_pixels
        
        # More lenient threshold due to diverse coral colors
        has_coral_by_color = coral_ratio > 0.01  # 1% threshold, more lenient
        
        # Additional detection method: detect texture and shape features
        has_coral_by_texture = detect_coral_texture(img)
        
        # If either color detection or texture detection passes, consider coral present
        has_coral = has_coral_by_color and has_coral_by_texture
        
        return has_coral, coral_ratio
    except Exception as e:
        return False, f"Coral detection failed: {str(e)}"

def detect_coral_texture(img):
    """Detect coral by texture features"""
    try:
        # Convert to grayscale
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        
        # Use edge detection
        edges = cv2.Canny(gray, 50, 150)
        
        # Calculate edge density
        edge_density = np.sum(edges > 0) / (edges.shape[0] * edges.shape[1])
        
        # Use morphological operations to detect circular and branching structures
        kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (5, 5))
        closed = cv2.morphologyEx(edges, cv2.MORPH_CLOSE, kernel)
        
        # Find contours
        contours, _ = cv2.findContours(closed, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        
        # Analyze contour features
        coral_like_features = 0
        for contour in contours:
            if cv2.contourArea(contour) > 100:  # Filter small contours
                # Calculate contour circularity
                perimeter = cv2.arcLength(contour, True)
                if perimeter > 0:
                    circularity = 4 * np.pi * cv2.contourArea(contour) / (perimeter * perimeter)
                    # Corals typically have moderate circularity (not perfectly circular, not completely irregular)
                    if 0.3 < circularity < 0.8:
                        coral_like_features += 1
        
        # If sufficient coral-like features, consider coral detected
        return coral_like_features > 2 or edge_density > 0.02
        
    except Exception as e:
        return False

def detect_coral_fallback(image_path):
    """More lenient coral detection method"""
    try:
        img = cv2.imread(image_path)
        if img is None:
            return False
        
        # Convert to grayscale
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        
        # Use more lenient edge detection
        edges = cv2.Canny(gray, 30, 100)
        edge_density = np.sum(edges > 0) / (edges.shape[0] * edges.shape[1])
        
        # Check if there are sufficient texture features
        if edge_density > 0.01:  # 1% edge density
            return True
        
        # Check if there is sufficient color variation in image
        color_std = np.std(gray)
        if color_std > 20:  # Sufficient color variation
            return True
        
        # Detect circular or elliptical structures
        contours, _ = cv2.findContours(edges, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        for contour in contours:
            if cv2.contourArea(contour) > 50:  # Smaller area threshold
                perimeter = cv2.arcLength(contour, True)
                if perimeter > 0:
                    circularity = 4 * np.pi * cv2.contourArea(contour) / (perimeter * perimeter)
                    if circularity > 0.2:  # More lenient circularity
                        return True
        
        return False
        
    except Exception as e:
        return False

def validate_image_quality(image_path):
    """Validate image quality, return quality issues and suggestions"""
    issues = []
    suggestions = []
    
    # Detect image quality
    quality_metrics, error = calculate_image_quality(image_path)
    if error:
        return False, [error], ["Please check if image file is corrupted"]
    
    # Check blur (lower threshold)
    if quality_metrics['laplacian_var'] < 50:
            issues.append("Image is too blurry")
            suggestions.append("Please upload a clearer image")
    
    # Check brightness (wider range)
    if quality_metrics['brightness'] < 20:
            issues.append("Image is too dark")
            suggestions.append("Please ensure the image has sufficient brightness")
    elif quality_metrics['brightness'] > 220:
            issues.append("Image is too bright")
            suggestions.append("Please adjust the image brightness")
    
    # Check contrast (lower threshold)
    if quality_metrics['contrast'] < 20:
            issues.append("Insufficient image contrast")
            suggestions.append("Please upload an image with higher contrast")
    
    # Check edge density (sharpness, lower threshold)
    if quality_metrics['edge_density'] < 0.005:
            issues.append("Image lacks detail")
            suggestions.append("Please upload an image with more details")
    
    # Detect coral presence
    has_coral, coral_ratio = detect_coral_presence(image_path)
    if not has_coral:
        # If color detection fails, try more lenient detection
        has_coral_fallback = detect_coral_fallback(image_path)
        if not has_coral_fallback:
            issues.append("No coral features detected")
            suggestions.append("Please ensure the image contains coral and re-upload")
    
    # If image is too small, may affect detection
    img = cv2.imread(image_path)
    if img is not None:
        height, width = img.shape[:2]
        if height < 100 or width < 100:
            issues.append("Image size too small")
            suggestions.append("Please upload a higher resolution image")
    
    return len(issues) == 0, issues, suggestions

def load_model():
    """Load trained model"""
    global model
    try:
        if os.path.exists(MODEL_PATH):
            model = tf.keras.models.load_model(MODEL_PATH)
            logger.info(f"Model loaded successfully: {MODEL_PATH}")
            return True
        else:
            logger.error(f"Model file not found: {MODEL_PATH}")
            return False
    except Exception as e:
        logger.error(f"Model loading failed: {str(e)}")
        return False

def preprocess_image(image_path, target_size=(180, 180)):
    """Preprocess image"""
    try:
        # Load and preprocess image using TensorFlow
        img = tf.keras.utils.load_img(image_path, target_size=target_size)
        img_array = tf.keras.utils.img_to_array(img)
        img_array = tf.expand_dims(img_array, 0)  # Create batch dimension
        return img_array
    except Exception as e:
        logger.error(f"Image preprocessing failed: {str(e)}")
        raise

def predict_coral_health(image_array):
    """Use model for prediction"""
    try:
        # Make prediction
        predictions = model.predict(image_array, verbose=0)
        result_probs = predictions[0]  # Get first (and only) prediction result
        
        # Calculate probability for each class
        probabilities = {}
        for i, class_name in enumerate(class_names):
            probabilities[class_name] = float(result_probs[i])
        
        # Find class with highest probability
        max_index = np.argmax(result_probs)
        predicted_class = class_names[max_index]
        confidence = float(result_probs[max_index])
        
        return {
            'predicted_class': predicted_class,
            'confidence': confidence,
            'probabilities': probabilities
        }
    except Exception as e:
        logger.error(f"Prediction failed: {str(e)}")
        raise

@app.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({
        'status': 'healthy',
        'timestamp': datetime.now().isoformat(),
        'model_loaded': model is not None
    })

@app.route('/predict', methods=['POST'])
def predict():
    """Coral classification prediction endpoint"""
    try:
        # Check if file is uploaded
        if 'image' not in request.files:
            return jsonify({
                'success': False,
                'error': 'No image file uploaded',
                'message': 'Please upload an image file using multipart/form-data format with field name "image"'
            }), 400
        
        file = request.files['image']
        
        # Check if file is empty
        if file.filename == '':
            return jsonify({
                'success': False,
                'error': 'No file selected',
                'message': 'Please select an image file'
            }), 400
        
        # Check file type
        if not allowed_file(file.filename):
            return jsonify({
                'success': False,
                'error': 'Unsupported file type',
                'message': f'Supported file types: {", ".join(ALLOWED_EXTENSIONS)}'
            }), 400
        
        # Check if model is loaded
        if model is None:
            return jsonify({
                'success': False,
                'error': 'Model not loaded',
                'message': 'Please check if the model file exists'
            }), 500
        
        # Save uploaded file
        filename = secure_filename(file.filename)
        timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
        filename = f"{timestamp}_{filename}"
        filepath = os.path.join(UPLOAD_FOLDER, filename)
        file.save(filepath)
        
        logger.info(f"Received image upload: {filename}")
        
        try:
            # Validate image quality
            is_valid, quality_issues, suggestions = validate_image_quality(filepath)
            
            if not is_valid:
                logger.warning(f"Image quality validation failed: {filename} - Issues: {quality_issues}")
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
            
            # Preprocess image
            image_array = preprocess_image(filepath)
            
            # Make prediction
            prediction_result = predict_coral_health(image_array)
            
            # Build response
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
            
            logger.info(f"Prediction completed: {filename} -> {prediction_result['predicted_class']} (Confidence: {prediction_result['confidence']:.4f})")
            
            return jsonify(response)
            
        finally:
            # Clean up uploaded file
            try:
                if os.path.exists(filepath):
                    os.remove(filepath)
                    logger.info(f"Cleaned up temporary file: {filename}")
            except Exception as e:
                logger.warning(f"Failed to clean up temporary file: {str(e)}")
    
    except Exception as e:
        logger.error(f"Prediction endpoint error: {str(e)}")
        return jsonify({
            'success': False,
            'error': 'Internal server error',
            'message': str(e)
        }), 500

@app.route('/model/info', methods=['GET'])
def model_info():
    """Get model information"""
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
    """Image quality validation endpoint"""
    try:
        # Check if file is uploaded
        if 'image' not in request.files:
            return jsonify({
                'success': False,
                'error': 'No image file uploaded',
                'message': 'Please upload an image file using multipart/form-data format with field name "image"'
            }), 400
        
        file = request.files['image']
        
        # Check if file is empty
        if file.filename == '':
            return jsonify({
                'success': False,
                'error': 'No file selected',
                'message': 'Please select an image file'
            }), 400
        
        # Check file type
        if not allowed_file(file.filename):
            return jsonify({
                'success': False,
                'error': 'Unsupported file type',
                'message': f'Supported file types: {", ".join(ALLOWED_EXTENSIONS)}'
            }), 400
        
        # Save uploaded file
        filename = secure_filename(file.filename)
        timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
        filename = f"{timestamp}_{filename}"
        filepath = os.path.join(UPLOAD_FOLDER, filename)
        file.save(filepath)
        
        logger.info(f"Received image quality validation request: {filename}")
        
        try:
            # Validate image quality
            is_valid, quality_issues, suggestions = validate_image_quality(filepath)
            
            # Get detailed quality metrics
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
            
            logger.info(f"Image quality validation completed: {filename} - Valid: {is_valid}")
            
            return jsonify(response)
            
        finally:
            # Clean up uploaded file
            try:
                if os.path.exists(filepath):
                    os.remove(filepath)
                    logger.info(f"Cleaned up temporary file: {filename}")
            except Exception as e:
                logger.warning(f"Failed to clean up temporary file: {str(e)}")
    
    except Exception as e:
        logger.error(f"Image quality validation endpoint error: {str(e)}")
        return jsonify({
            'success': False,
            'error': 'Internal server error',
            'message': str(e)
        }), 500

@app.errorhandler(413)
def too_large(e):
    """File too large error handler"""
    return jsonify({
        'success': False,
        'error': 'File too large',
        'message': 'Uploaded file size cannot exceed 16MB'
    }), 413

@app.errorhandler(404)
def not_found(e):
    """404 error handler"""
    return jsonify({
        'success': False,
        'error': 'API endpoint does not exist',
        'message': 'Please check if the requested URL is correct'
    }), 404

@app.errorhandler(500)
def internal_error(e):
    """500 error handler"""
    return jsonify({
        'success': False,
        'error': 'Internal server error',
        'message': 'Please contact the administrator'
    }), 500

if __name__ == '__main__':
    # Load model on startup
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
