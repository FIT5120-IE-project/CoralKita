<template>
  <div class="education-container">
    <!-- 头部 -->
    <div class="education-header">
      <h1>Education & Responsible Tourism Hub</h1>
      <p>Learn about coral reefs and become a responsible traveler</p>
    </div>

    <!-- 主要内容区域 -->
    <div class="education-content">
      <!-- 未登录状态 -->
      <div v-if="!isAuthenticated" class="login-section">
        <div class="login-card">
          <h2>Welcome to CoralKita Education</h2>
          <p>Please login or register to access our interactive quizzes and learning materials</p>
          
          <!-- 登录表单 -->
          <div class="login-form">
            <div class="form-group">
              <label for="username">用户名</label>
              <input 
                type="text" 
                id="username" 
                v-model="loginForm.name" 
                placeholder="请输入用户名"
                @keyup.enter="handleLogin"
              >
            </div>
            
            <div class="form-group">
              <label for="password">密码</label>
              <input 
                type="password" 
                id="password" 
                v-model="loginForm.password" 
                placeholder="请输入密码"
                @keyup.enter="handleLogin"
              >
            </div>
            
            <div class="form-actions">
              <button 
                class="btn-login" 
                @click="handleLogin"
                :disabled="loading"
              >
                {{ loading ? '登录中...' : '登录' }}
              </button>
              
              <div class="form-links">
                <span @click="showRegister = true" class="link">还没有账号？立即注册</span>
              </div>
            </div>
          </div>
          
          <!-- 注册表单 -->
          <div v-if="showRegister" class="register-form">
            <div class="form-header">
              <h3>用户注册</h3>
              <span @click="showRegister = false" class="close-btn">&times;</span>
            </div>
            
            <div class="form-group">
              <label for="reg-username">用户名</label>
              <input 
                type="text" 
                id="reg-username" 
                v-model="registerForm.name" 
                placeholder="请输入用户名"
              >
            </div>
            
            <div class="form-group">
              <label for="reg-password">密码</label>
              <input 
                type="password" 
                id="reg-password" 
                v-model="registerForm.password" 
                placeholder="请输入密码"
              >
            </div>
            
            <div class="form-group">
              <label for="reg-confirm-password">确认密码</label>
              <input 
                type="password" 
                id="reg-confirm-password" 
                v-model="registerForm.confirmPassword" 
                placeholder="请再次输入密码"
              >
            </div>
            
            <div class="form-actions">
              <button 
                class="btn-register" 
                @click="handleRegister"
                :disabled="registerLoading"
              >
                {{ registerLoading ? '注册中...' : '注册' }}
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 已登录状态 -->
      <div v-else class="authenticated-content">
        <!-- 用户信息卡片 -->
        <div class="user-info-card">
          <div class="user-avatar">
            <img src="@/assets/icon.png" alt="User Avatar" />
          </div>
          <div class="user-details">
            <h3>欢迎回来, {{ currentUser.name }}!</h3>
            <div class="user-stats">
              <span class="stat-item">
                <i class="stat-icon">🏆</i>
                等级: {{ currentUser.level }}
              </span>
              <span class="stat-item">
                <i class="stat-icon">⭐</i>
                积分: {{ currentUser.points }}
              </span>
              <span class="stat-item">
                <i class="stat-icon">📚</i>
                经验: {{ currentUser.experience }}
              </span>
            </div>
          </div>
          <button class="btn-logout" @click="handleLogout">退出登录</button>
        </div>

        <!-- 功能区域 -->
        <div class="features-grid">
          <!-- 珊瑚礁知识测验 -->
          <div class="feature-card quiz-card">
            <div class="feature-icon">🧠</div>
            <h3>珊瑚礁知识测验</h3>
            <p>测试你对珊瑚礁的了解程度，学习海洋生态知识</p>
            <button class="btn-feature" @click="startQuiz">开始测验</button>
          </div>

          <!-- 负责任旅游指南 -->
          <div class="feature-card guide-card">
            <div class="feature-icon">🌊</div>
            <h3>负责任旅游指南</h3>
            <p>学习如何在访问珊瑚礁时最小化对环境的影响</p>
            <button class="btn-feature" @click="viewGuide">查看指南</button>
          </div>

          <!-- 学习进度 -->
          <div class="feature-card progress-card">
            <div class="feature-icon">📊</div>
            <h3>学习进度</h3>
            <p>跟踪你的学习进度和获得的成就</p>
            <button class="btn-feature" @click="viewProgress">查看进度</button>
          </div>

          <!-- 奖励系统 -->
          <div class="feature-card rewards-card">
            <div class="feature-icon">🎁</div>
            <h3>奖励系统</h3>
            <p>完成学习任务获得积分和徽章</p>
            <button class="btn-feature" @click="viewRewards">查看奖励</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 错误提示 -->
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import { userLogin, userRegister } from '@/api/user'

export default {
  name: 'Education',
  data() {
    return {
      loginForm: {
        name: '',
        password: ''
      },
      registerForm: {
        name: '',
        password: '',
        confirmPassword: ''
      },
      showRegister: false,
      loading: false,
      registerLoading: false,
      errorMessage: ''
    }
  },
  computed: {
    ...mapGetters(['isAuthenticated', 'currentUser'])
  },
  methods: {
    ...mapActions(['login', 'logout']),
    
    async handleLogin() {
      if (!this.loginForm.name || !this.loginForm.password) {
        this.errorMessage = '请输入用户名和密码'
        return
      }
      
      this.loading = true
      this.errorMessage = ''
      
      try {
        console.log('发送登录请求:', this.loginForm)
        const response = await userLogin(this.loginForm)
        console.log('登录响应:', response)
        
        if (response.code === 1) {
          // 登录成功，使用Vuex保存用户信息和token
          this.$store.dispatch('login', {
            user: response.data,
            token: response.data.token
          })
          
          // 显示成功消息
          alert('登录成功！欢迎来到CoralKita教育中心')
        } else {
          this.errorMessage = response.msg || '登录失败'
        }
      } catch (error) {
        console.error('登录错误:', error)
        this.errorMessage = '登录失败，请检查网络连接'
      } finally {
        this.loading = false
      }
    },
    
    async handleRegister() {
      if (!this.registerForm.name || !this.registerForm.password || !this.registerForm.confirmPassword) {
        this.errorMessage = '请填写完整的注册信息'
        return
      }
      
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        this.errorMessage = '两次输入的密码不一致'
        return
      }
      
      this.registerLoading = true
      this.errorMessage = ''
      
      try {
        const response = await userRegister({
          name: this.registerForm.name,
          password: this.registerForm.password
        })
        
        if (response.code === 1) {
          // 注册成功，显示成功消息并切换到登录表单
          alert('注册成功！请登录')
          this.showRegister = false
          this.registerForm = {
            name: '',
            password: '',
            confirmPassword: ''
          }
        } else {
          this.errorMessage = response.msg || '注册失败'
        }
      } catch (error) {
        console.error('注册错误:', error)
        this.errorMessage = '注册失败，请检查网络连接'
      } finally {
        this.registerLoading = false
      }
    },
    
    async handleLogout() {
      try {
        await this.logout();
        alert('已成功退出登录');
      } catch (error) {
        console.error('退出登录失败:', error);
      }
    },
    
    startQuiz() {
      alert('珊瑚礁知识测验功能即将上线！');
    },
    
    viewGuide() {
      alert('负责任旅游指南功能即将上线！');
    },
    
    viewProgress() {
      alert('学习进度功能即将上线！');
    },
    
    viewRewards() {
      alert('奖励系统功能即将上线！');
    }
  }
}
</script>

<style scoped>
.education-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.education-header {
  text-align: center;
  color: white;
  margin-bottom: 40px;
}

.education-header h1 {
  font-size: 2.5rem;
  margin-bottom: 10px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.7);
}

.education-header p {
  font-size: 1.2rem;
  opacity: 0.9;
}

.education-content {
  max-width: 1200px;
  margin: 0 auto;
}

/* 登录区域样式 */
.login-section {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}

.login-card {
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  padding: 40px;
  width: 100%;
  max-width: 500px;
  position: relative;
}

.login-card h2 {
  text-align: center;
  color: #333;
  margin-bottom: 20px;
}

.login-card p {
  text-align: center;
  color: #666;
  margin-bottom: 30px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 16px;
  transition: border-color 0.3s ease;
}

.form-group input:focus {
  outline: none;
  border-color: #667eea;
}

.form-actions {
  margin-top: 30px;
}

.btn-login, .btn-register {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.btn-login:hover, .btn-register:hover {
  transform: translateY(-2px);
}

.btn-login:disabled, .btn-register:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
}

.form-links {
  text-align: center;
  margin-top: 20px;
}

.link {
  color: #667eea;
  cursor: pointer;
  text-decoration: underline;
  font-size: 14px;
}

.link:hover {
  color: #764ba2;
}

.register-form {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: white;
  border-radius: 20px;
  padding: 40px;
  z-index: 10;
}

.form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.form-header h3 {
  color: #333;
  font-size: 18px;
}

.close-btn {
  font-size: 24px;
  color: #999;
  cursor: pointer;
  padding: 5px;
}

.close-btn:hover {
  color: #333;
}

/* 已认证用户内容样式 */
.authenticated-content {
  color: white;
}

.user-info-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 30px;
  margin-bottom: 40px;
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-avatar img {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 3px solid rgba(255, 255, 255, 0.3);
}

.user-details h3 {
  margin-bottom: 15px;
  font-size: 1.5rem;
}

.user-stats {
  display: flex;
  gap: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
  background: rgba(255, 255, 255, 0.2);
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 0.9rem;
}

.stat-icon {
  font-size: 1.2rem;
}

.btn-logout {
  margin-left: auto;
  padding: 10px 20px;
  background: rgba(255, 107, 107, 0.8);
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.btn-logout:hover {
  background: rgba(255, 107, 107, 1);
}

/* 功能网格 */
.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 30px;
  margin-top: 40px;
}

.feature-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 30px;
  text-align: center;
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 20px;
}

.feature-card h3 {
  margin-bottom: 15px;
  font-size: 1.3rem;
}

.feature-card p {
  margin-bottom: 25px;
  opacity: 0.9;
  line-height: 1.6;
}

.btn-feature {
  padding: 12px 24px;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.8);
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.btn-feature:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

/* 错误提示 */
.error-message {
  position: fixed;
  top: 20px;
  right: 20px;
  background: #ff4757;
  color: white;
  padding: 12px 20px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  animation: slideIn 0.3s ease;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .education-header h1 {
    font-size: 2rem;
  }
  
  .user-info-card {
    flex-direction: column;
    text-align: center;
  }
  
  .user-stats {
    justify-content: center;
    flex-wrap: wrap;
  }
  
  .btn-logout {
    margin-left: 0;
    margin-top: 20px;
  }
  
  .features-grid {
    grid-template-columns: 1fr;
  }
}
</style>
