# 答案验证调试指南

## 🐛 问题描述
用户反馈所有测验答案都显示为错误，即使选择了正确答案。

## 🔍 可能的原因

### 1. 数据库字段映射问题
- **数据库字段**: `correct_answer` (下划线命名)
- **Java实体字段**: `correctAnswer` (驼峰命名)
- **MyBatis配置**: 已开启 `map-underscore-to-camel-case: true`

### 2. 数据类型不匹配
- 前端选择的选项: 字符串 'A', 'B', 'C', 'D'
- 数据库存储的答案: 字符串 'A', 'B', 'C', 'D'
- 可能存在类型转换或编码问题

### 3. 字符串比较问题
- 可能存在前后空格
- 可能存在字符编码问题
- 可能存在大小写问题

## 🔧 修复措施

### 1. 显式字段映射
在 `QuizMapper.java` 中使用显式字段映射：
```sql
SELECT id, question, option_a as optionA, option_b as optionB, 
       option_c as optionC, option_d as optionD, 
       correct_answer as correctAnswer, 
       source_title as sourceTitle, ...
FROM quiz_questions WHERE source_title = #{sourceTitle} 
ORDER BY RAND() LIMIT 5
```

### 2. 增强答案比较逻辑
在前端添加更严格的比较逻辑：
```javascript
// 确保比较时都是字符串类型，并且去除空格
const selectedAnswer = String(this.selectedOption).trim()
const correctAnswer = String(currentQ.correctAnswer).trim()
this.isAnswerCorrect = selectedAnswer === correctAnswer
```

### 3. 添加调试日志
- **后端**: 在QuizController中打印题目和正确答案
- **前端**: 在答案验证时打印比较的值和类型

## 🧪 测试步骤

### 1. 检查后端数据
1. 启动后端服务
2. 查看控制台日志，确认题目数据正确加载
3. 验证 `correctAnswer` 字段是否有值

### 2. 检查前端数据接收
1. 打开浏览器开发者工具
2. 进入测验页面
3. 点击"开始随机测验"
4. 查看控制台输出的题目数据

### 3. 检查答案比较
1. 选择一个答案
2. 点击"提交答案"
3. 查看控制台输出的比较过程
4. 验证比较结果是否正确

## 📋 调试检查清单

- [ ] 后端日志显示正确的题目数据
- [ ] 前端接收到完整的题目数据，包括 `correctAnswer` 字段
- [ ] 选项ID ('A', 'B', 'C', 'D') 与正确答案匹配
- [ ] 字符串比较逻辑正确执行
- [ ] 没有类型转换错误
- [ ] 没有编码或空格问题

## 🎯 预期结果

修复后，用户选择正确答案时应该：
1. 控制台显示正确的比较过程
2. 界面显示"回答正确！"
3. 得分正确增加
4. 统计数据正确更新

## 📝 测试数据示例

根据数据库截图，测试用例：
- 题目: "What is a coral reef primarily made of?"
- 选项A: "Rocks"
- 选项B: "Living coral polyps" ✓ (正确答案)
- 选项C: "Seaweed"
- 选项D: "Sand"
- 正确答案: "B"

用户选择选项B时应该显示正确。
