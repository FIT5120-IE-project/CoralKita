# 配置文件说明

## 服务器地址配置

为了避免硬编码服务器地址，所有后端API地址都通过配置文件统一管理。

### 主要配置文件

#### 1. `src/config/config.js` - 核心配置文件
```javascript
// 环境配置
const config = {
  // 开发环境
  development: {
    baseURL: 'http://localhost:8080',
    frontendPort: 3001
  },
  // 生产环境
  production: {
    baseURL: 'https://your-production-domain.com', // 生产环境地址
    frontendPort: 80
  }
};
```

#### 2. `webpack.config.js` - Webpack配置
```javascript
// 后端服务器配置
const BACKEND_URL = 'http://localhost:8080';
const FRONTEND_PORT = 3001;
```

### 如何修改服务器地址

#### 方法1：修改配置文件（推荐）
1. 编辑 `src/config/config.js` 文件
2. 修改对应环境的 `baseURL` 值
3. 重新启动前端服务

#### 方法2：修改Webpack配置
1. 编辑 `webpack.config.js` 文件
2. 修改 `BACKEND_URL` 常量
3. 重新启动前端服务

### 已配置的文件列表

✅ **已配置为使用变量的文件：**
- `src/api/user.js` - 用户API
- `src/component/TrendsVisualization.vue` - 趋势可视化组件
- `webpack.config.js` - Webpack开发服务器配置
- `test.html` - API测试页面
- `debug-api.html` - API调试页面

### 环境变量支持

如果需要使用环境变量，可以在 `src/config/config.js` 中添加：

```javascript
// 支持环境变量
const config = {
  development: {
    baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080',
    frontendPort: process.env.VUE_APP_FRONTEND_PORT || 3001
  },
  production: {
    baseURL: process.env.VUE_APP_API_BASE_URL || 'https://your-production-domain.com',
    frontendPort: process.env.VUE_APP_FRONTEND_PORT || 80
  }
};
```

### 部署到云端

当需要部署到云端时：

1. **修改生产环境配置**：
   ```javascript
   production: {
     baseURL: 'https://your-actual-domain.com',
     frontendPort: 80
   }
   ```

2. **或者使用环境变量**：
   ```bash
   export VUE_APP_API_BASE_URL=https://your-actual-domain.com
   ```

3. **重新构建前端**：
   ```bash
   npm run build
   ```

### 注意事项

- 修改配置后需要重新启动前端服务
- 确保后端服务地址正确且可访问
- 生产环境建议使用HTTPS协议
- 建议使用环境变量来管理不同环境的配置
