# CoralKita 登录功能使用说明

## 功能概述

本项目已实现完整的用户登录、注册和退出功能，支持以下特性：

- 用户注册和登录
- JWT Token 认证
- 路由守卫保护
- 用户状态管理
- 响应式界面设计

## 已实现的功能

### 1. 用户认证接口
- **登录**: `POST /user/login`
- **注册**: `POST /user/register`  
- **退出**: `POST /user/logout`

### 2. 前端组件
- `Login.vue` - 登录/注册界面
- 导航栏集成 - 显示用户状态和退出按钮
- 路由守卫 - 保护需要登录的页面

### 3. 状态管理
- Vuex store 管理用户状态
- 本地存储保存登录信息
- 自动恢复登录状态

## 使用方法

### 1. 启动项目
```bash
# 启动后端服务
cd backend
mvn spring-boot:run

# 启动前端服务
cd frontend
npm run serve
```

### 2. 访问登录页面
- 打开浏览器访问: `http://localhost:8080/login`
- 或者点击主页导航栏的"登录"按钮

### 3. 用户注册
- 在登录页面点击"还没有账号？立即注册"
- 填写用户名和密码
- 点击"注册"按钮

### 4. 用户登录
- 输入用户名和密码
- 点击"登录"按钮
- 登录成功后自动跳转到主页

### 5. 查看用户信息
- 登录后导航栏会显示：
  - 欢迎信息
  - 用户积分
  - 用户等级
  - 退出按钮

### 6. 退出登录
- 点击导航栏的"退出"按钮
- 系统会清除用户信息并跳转到登录页

## 技术实现

### 1. 后端接口
- Spring Boot + MyBatis
- JWT Token 认证
- MySQL 数据库存储

### 2. 前端技术
- Vue.js 2.x
- Vue Router 路由管理
- Vuex 状态管理
- Axios HTTP 客户端

### 3. 安全特性
- 密码加密存储
- Token 过期处理
- 路由访问控制
- 自动重定向

## 数据库表结构

### user 表
```sql
CREATE TABLE user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  points INT DEFAULT 0,
  badges VARCHAR(255) DEFAULT '',
  level VARCHAR(50) DEFAULT 'Novice',
  experience INT DEFAULT 0
);
```

## 配置说明

### 1. 后端配置
- 数据库连接配置: `application-dev.yml`
- JWT 密钥配置: `JwtProperties.java`
- 阿里云 OSS 配置: `application-dev.yml`

### 2. 前端配置
- API 基础地址: `src/api/user.js`
- 路由配置: `src/router/index.js`
- 状态管理: `src/vuex/index.js`

## 注意事项

1. **首次使用**: 需要先注册用户账号
2. **数据库**: 确保 MySQL 服务已启动
3. **端口配置**: 默认后端端口 8080，前端端口 8081
4. **跨域问题**: 后端已配置 CORS 支持
5. **Token 过期**: 系统会自动处理并重定向到登录页

## 下一步开发

基于当前的登录功能，可以继续开发：

1. **教育功能** (US 4.1): 珊瑚礁知识测验
2. **旅游指南** (US 4.2): 负责任旅游提示
3. **奖励系统** (US 4.3): 积分和徽章系统
4. **用户资料**: 个人信息管理
5. **权限管理**: 不同用户角色

## 故障排除

### 常见问题

1. **登录失败**: 检查用户名密码是否正确
2. **注册失败**: 检查用户名是否已存在
3. **页面空白**: 检查浏览器控制台错误信息
4. **接口错误**: 检查后端服务是否正常启动

### 调试方法

1. 查看浏览器控制台日志
2. 检查网络请求状态
3. 查看后端服务日志
4. 验证数据库连接

## 联系支持

如有问题或需要技术支持，请查看项目文档或联系开发团队。
