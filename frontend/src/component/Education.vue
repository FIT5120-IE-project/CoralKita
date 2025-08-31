<template>
  <div class="education-container">
    <!-- 头部 -->
    <div class="education-header">
      <h1>Education & Responsible Tourism Hub</h1>
      <p>Learn about coral reefs and become a responsible traveler</p>
    </div>

    <!-- 主要内容区域 -->
    <div class="education-content">
      <!-- 学习界面 -->
      <div class="learning-interface">
        <!-- 用户信息卡片 -->
        <div class="user-info-card">
          <div class="user-avatar">
            <img src="@/assets/icon.png" alt="User Avatar" />
          </div>
          <div class="user-details">
            <!-- 已登录用户显示欢迎信息 -->
            <div v-if="isAuthenticated">
              <h3>欢迎回来, {{ currentUser.name }}!</h3>
              <div class="user-stats">
                <span class="stat-item">
                  <i class="stat-icon">🏆</i>
                  等级: {{ currentUser.level || 1 }}
                </span>
                <span class="stat-item">
                  <i class="stat-icon">⭐</i>
                  积分: {{ currentUser.points || 0 }}
                </span>
                <span class="stat-item">
                  <i class="stat-icon">📚</i>
                  经验: {{ currentUser.experience || 0 }}
                </span>
              </div>
            </div>
            <!-- 未登录用户显示欢迎信息 -->
            <div v-else>
              <h3>欢迎来到CoralKita教育中心</h3>
              <p>请登录或注册以开始您的学习之旅</p>
            </div>
          </div>
          <div class="user-actions">
            <!-- 已登录用户显示退出登录按钮 -->
            <button v-if="isAuthenticated" class="btn-logout" @click="handleLogout">退出登录</button>
            <!-- 未登录用户显示登录/注册按钮 -->
            <button v-else class="btn-auth" @click="showAuthForms = true">登录/注册</button>
          </div>
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

          <!-- 奖励系统 -->
          <div class="feature-card rewards-card">
            <div class="feature-icon">🎁</div>
            <h3>奖励系统</h3>
            <p>完成学习任务获得积分和徽章</p>
          </div>

          <!-- 学习进度 -->
          <div class="feature-card progress-card">
            <div class="feature-icon">📊</div>
            <h3>学习进度</h3>
            <p>跟踪你的学习进度和获得的成就</p>
            <button class="btn-feature" @click="viewProgress">查看进度</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 错误提示 -->
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
    
    <!-- 独立的登录/注册界面 -->
    <div v-if="!isAuthenticated && showAuthForms" class="auth-overlay">
      <div class="auth-modal">
        <!-- 关闭按钮 -->
        <div class="auth-header">
          <h2>用户认证</h2>
          <button class="close-btn" @click="showAuthForms = false">×</button>
        </div>
        
        <!-- 登录表单 -->
        <div v-if="!showRegister" class="auth-form">
          <h3>用户登录</h3>
          <p>欢迎来到CoralKita教育中心，请登录以开始您的学习之旅</p>
          
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
              class="btn-submit" 
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
        <div v-if="showRegister" class="auth-form">
          <h3>用户注册</h3>
          <p>创建新账号，开始您的珊瑚礁学习之旅</p>
          
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
              class="btn-submit" 
              @click="handleRegister"
              :disabled="registerLoading"
            >
              {{ registerLoading ? '注册中...' : '注册' }}
            </button>
            
            <div class="form-links">
              <span @click="showRegister = false" class="link">已有账号？立即登录</span>
            </div>
          </div>
        </div>
      </div>
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
      showAuthForms: false,
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
          
          // 清空登录表单并隐藏认证表单
          this.loginForm = { name: '', password: '' }
          this.showAuthForms = false
          
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

/* 未登录状态样式 */
.login-section {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 60vh;
}

/* 登录表单容器 */
.login-form-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
}

.login-form-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 40px;
  text-align: center;
  color: white;
  max-width: 500px;
  width: 100%;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.login-form-card h2 {
  font-size: 2rem;
  margin-bottom: 15px;
  color: white;
}

.login-form-card p {
  font-size: 1.1rem;
  margin-bottom: 30px;
  opacity: 0.9;
  line-height: 1.6;
}

/* 注册表单容器 */
.register-form-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
}

.register-form-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 40px;
  text-align: center;
  color: white;
  max-width: 500px;
  width: 100%;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.register-form-card h2 {
  font-size: 2rem;
  margin-bottom: 15px;
  color: white;
}

.register-form-card p {
  font-size: 1.1rem;
  margin-bottom: 30px;
  opacity: 0.9;
  line-height: 1.6;
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

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: white;
  font-weight: 500;
  text-align: left;
}

.form-group input {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  font-size: 16px;
  background: rgba(255, 255, 255, 0.1);
  color: white;
  transition: all 0.3s ease;
}

.form-group input::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

.form-group input:focus {
  outline: none;
  border-color: rgba(255, 255, 255, 0.8);
  background: rgba(255, 255, 255, 0.2);
}

.form-actions {
  margin-top: 30px;
}

.btn-submit {
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

.btn-submit:hover {
  transform: translateY(-2px);
}

.btn-submit:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
}

.form-links {
  text-align: center;
  margin-top: 20px;
}

.link {
  color: rgba(255, 255, 255, 0.9);
  cursor: pointer;
  text-decoration: underline;
  font-size: 14px;
  transition: color 0.3s ease;
}

.link:hover {
  color: white;
}

/* 学习界面样式 */
.learning-interface {
  color: white;
}

/* 独立登录/注册界面样式 */
.auth-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(5px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

.auth-modal {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 25px;
  padding: 0;
  max-width: 500px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: slideUp 0.3s ease;
}

.auth-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 25px 30px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.auth-header h2 {
  color: white;
  font-size: 1.8rem;
  margin: 0;
  font-weight: 600;
}

.close-btn {
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.8);
  font-size: 28px;
  cursor: pointer;
  padding: 5px;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.close-btn:hover {
  background: rgba(255, 255, 255, 0.1);
  color: white;
  transform: rotate(90deg);
}

.auth-form {
  padding: 30px;
  color: white;
}

.auth-form h3 {
  font-size: 1.6rem;
  margin-bottom: 15px;
  color: white;
  text-align: center;
}

.auth-form p {
  font-size: 1rem;
  margin-bottom: 30px;
  opacity: 0.9;
  line-height: 1.6;
  text-align: center;
}

.auth-form .form-group {
  margin-bottom: 20px;
}

.auth-form .form-group label {
  display: block;
  margin-bottom: 8px;
  color: white;
  font-weight: 500;
  font-size: 0.95rem;
}

.auth-form .form-group input {
  width: 100%;
  padding: 15px 18px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 12px;
  font-size: 16px;
  background: rgba(255, 255, 255, 0.1);
  color: white;
  transition: all 0.3s ease;
  box-sizing: border-box;
}

.auth-form .form-group input::placeholder {
  color: rgba(255, 255, 255, 0.6);
}

.auth-form .form-group input:focus {
  outline: none;
  border-color: rgba(255, 255, 255, 0.8);
  background: rgba(255, 255, 255, 0.15);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
}

.auth-form .form-actions {
  margin-top: 30px;
}

.auth-form .btn-submit {
  width: 100%;
  padding: 16px;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 20px;
  box-shadow: 0 4px 15px rgba(79, 172, 254, 0.3);
}

.auth-form .btn-submit:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(79, 172, 254, 0.4);
}

.auth-form .btn-submit:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.auth-form .form-links {
  text-align: center;
}

.auth-form .link {
  color: rgba(255, 255, 255, 0.9);
  cursor: pointer;
  text-decoration: underline;
  font-size: 14px;
  transition: color 0.3s ease;
}

.auth-form .link:hover {
  color: white;
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px) scale(0.9);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

/* 登录/注册按钮 */
.btn-auth {
  padding: 10px 20px;
  background: rgba(102, 126, 234, 0.8);
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.btn-auth:hover {
  background: rgba(102, 126, 234, 1);
  transform: translateY(-2px);
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

.user-actions {
  margin-left: auto;
  display: flex;
  gap: 15px;
  align-items: center;
}

.btn-login-action {
  padding: 10px 20px;
  background: rgba(102, 126, 234, 0.8);
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.btn-login-action:hover {
  background: rgba(102, 126, 234, 1);
  transform: translateY(-2px);
}

.btn-logout {
  padding: 10px 20px;
  background: rgba(255, 107, 107, 0.8);
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-logout:hover {
  background: rgba(255, 107, 107, 1);
  transform: translateY(-2px);
}

/* 内联登录表单样式 */
.login-form-inline {
  display: flex;
  gap: 10px;
  align-items: center;
}

.form-group-inline {
  display: flex;
  align-items: center;
}

.form-group-inline input {
  padding: 8px 12px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 15px;
  background: rgba(255, 255, 255, 0.1);
  color: white;
  font-size: 14px;
  width: 120px;
  transition: all 0.3s ease;
}

.form-group-inline input::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

.form-group-inline input:focus {
  outline: none;
  border-color: rgba(255, 255, 255, 0.8);
  background: rgba(255, 255, 255, 0.2);
}

.btn-login-inline {
  padding: 8px 16px;
  background: rgba(102, 126, 234, 0.8);
  color: white;
  border: none;
  border-radius: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  font-size: 14px;
}

.btn-login-inline:hover {
  background: rgba(102, 126, 234, 1);
  transform: translateY(-2px);
}

.btn-login-inline:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
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
  
  .login-form-card h2,
  .register-form-card h2 {
    font-size: 1.5rem;
  }
  
  .user-info-card {
    flex-direction: column;
    text-align: center;
  }
  
  .user-stats {
    justify-content: center;
    flex-wrap: wrap;
  }
  
  .user-actions {
    margin-left: 0;
    margin-top: 20px;
    justify-content: center;
  }
  
  .auth-modal {
    width: 95%;
    margin: 20px;
  }
  
  .auth-header {
    padding: 20px 25px 15px;
  }
  
  .auth-header h2 {
    font-size: 1.5rem;
  }
  
  .auth-form {
    padding: 25px;
  }
  
  .auth-form h3 {
    font-size: 1.4rem;
  }
  
  .auth-form .form-group input {
    padding: 12px 15px;
  }
  
  .features-grid {
    grid-template-columns: 1fr;
  }
}
</style>
