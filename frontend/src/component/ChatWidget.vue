<template>
  <div class="chat-widget" style="display: none;">
    <!-- 聊天按钮 -->
    <div 
      v-if="!isOpen" 
      class="chat-button" 
      @click="toggleChat"
      title="CoralKita Assistant"
    >
      <svg width="32" height="32" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M20 2H4C2.9 2 2 2.9 2 4V22L6 18H20C21.1 18 22 17.1 22 16V4C22 2.9 21.1 2 20 2ZM20 16H5.17L4 17.17V4H20V16Z" fill="currentColor"/>
        <path d="M7 9H17V11H7V9ZM7 12H15V14H7V12Z" fill="currentColor"/>
      </svg>
    </div>

    <!-- 聊天窗口 -->
    <div v-if="isOpen" class="chat-window">
      <!-- 聊天头部 -->
      <div class="chat-header">
        <div class="chat-title">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M20 2H4C2.9 2 2 2.9 2 4V22L6 18H20C21.1 18 22 17.1 22 16V4C22 2.9 21.1 2 20 2ZM20 16H5.17L4 17.17V4H20V16Z" fill="currentColor"/>
          </svg>
          <span>CoralKita Assistant</span>
        </div>
        <button class="close-button" @click="toggleChat" title="Close">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M19 6.41L17.59 5L12 10.59L6.41 5L5 6.41L10.59 12L5 17.59L6.41 19L12 13.41L17.59 19L19 17.59L13.41 12L19 6.41Z" fill="currentColor"/>
          </svg>
        </button>
      </div>

      <!-- 聊天消息区域 -->
      <div class="chat-messages" ref="messagesContainer">
        <div v-if="messages.length === 0" class="welcome-message">
          <p>👋 Hello! I'm your CoralKita Assistant</p>
          <p>You can consult me about issues related to Malaysia's coral reefs from 2007 to 2024. For example, you may ask, "What is the condition of Malaysia's coral reefs in 2024?" or "How has the condition of coral reefs changed in recent years?"</p>
          <p><strong>⚠️ Please note that I may make mistakes, so for important information, kindly consult experts for confirmation.</strong></p>
        </div>
        
        <div 
          v-for="(message, index) in messages" 
          :key="index" 
          :class="['message', message.type]"
        >
          <!-- 机器人消息布局 -->
          <div v-if="message.type === 'bot'" class="message-content">
            <div class="bot-avatar">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 2C13.1 2 14 2.9 14 4C14 5.1 13.1 6 12 6C10.9 6 10 5.1 10 4C10 2.9 10.9 2 12 2ZM21 9V7L15 1H5C3.89 1 3 1.89 3 3V21C3 22.11 3.89 23 5 23H19C20.11 23 21 22.11 21 21V9M19 9H14V4H19V9Z" fill="currentColor"/>
              </svg>
            </div>
            <div class="message-text" v-html="formatMessageText(message.text)"></div>
          </div>
          <div v-if="message.type === 'bot'" class="message-time">{{ formatTime(message.timestamp) }}</div>

          <!-- 用户消息布局 -->
          <div v-if="message.type === 'user'" class="user-message-container">
            <div class="user-message-bubble-group">
              <div class="message-text">{{ message.text }}</div>
              <div class="message-time">{{ formatTime(message.timestamp) }}</div>
            </div>
            <div class="user-avatar">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 12C14.21 12 16 10.21 16 8C16 5.79 14.21 4 12 4C9.79 4 8 5.79 8 8C8 10.21 9.79 12 12 12ZM12 14C9.33 14 4 15.34 4 18V20H20V18C20 15.34 14.67 14 12 14Z" fill="currentColor"/>
              </svg>
            </div>
          </div>
        </div>

        <!-- 加载指示器 -->
        <div v-if="isLoading" class="message bot">
          <div class="message-content">
            <div class="bot-avatar">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 2C13.1 2 14 2.9 14 4C14 5.1 13.1 6 12 6C10.9 6 10 5.1 10 4C10 2.9 10.9 2 12 2ZM21 9V7L15 1H5C3.89 1 3 1.89 3 3V21C3 22.11 3.89 23 5 23H19C20.11 23 21 22.11 21 21V9M19 9H14V4H19V9Z" fill="currentColor"/>
              </svg>
            </div>
            <div class="message-text">
              <div class="typing-indicator">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 输入区域 -->
      <div class="chat-input">
        <div class="input-container">
          <input 
            v-model="currentMessage" 
            @keyup.enter="sendMessage"
            @keyup.esc="toggleChat"
            placeholder="Ask your question..."
            :disabled="isLoading"
            ref="messageInput"
          />
          <button 
            @click="sendMessage" 
            :disabled="!currentMessage.trim() || isLoading"
            class="send-button"
            title="Send message"
          >
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M2.01 21L23 12L2.01 3L2 10L17 12L2 14L2.01 21Z" fill="currentColor"/>
            </svg>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { API_BASE_URL } from '../config/config.js'

export default {
  name: 'ChatWidget',
  data() {
    return {
      isOpen: false,
      currentMessage: '',
      messages: [],
      isLoading: false,
      hasShownWelcome: false
    }
  },
  mounted() {
    // 页面加载后立即显示欢迎提醒 - 已禁用
    // this.showWelcomeNotification()
  },
  beforeDestroy() {
    // 组件销毁时重置状态，确保下次进入页面时能再次显示
    this.hasShownWelcome = false
  },
  methods: {
    showWelcomeNotification() {
      // 每次进入页面都显示欢迎提醒
      if (!this.hasShownWelcome) {
        this.hasShownWelcome = true
        
        // 创建欢迎提醒元素
        const notification = document.createElement('div')
        notification.className = 'welcome-notification'
        notification.innerHTML = `
          <div class="notification-content">
            <div class="notification-avatar">🌊</div>
            <div class="notification-text">
              <div class="notification-title">Hi! I'm your CoralKita little helper</div>
              <div class="notification-message">You can ask me about Malaysia's coral reefs from 2007 to 2024! 🌊🐠</div>
            </div>
            <button class="notification-close" onclick="this.parentElement.parentElement.remove()">×</button>
          </div>
        `
        
        // 添加样式
        const style = document.createElement('style')
        style.textContent = `
          .welcome-notification {
            position: fixed;
            bottom: 100px;
            right: 20px;
            width: 320px;
            background: linear-gradient(135deg, #4682B4 0%, #20B2AA 100%);
            border-radius: 12px;
            box-shadow: 0 8px 32px rgba(70, 130, 180, 0.3);
            z-index: 1001;
            animation: slideInUp 0.5s ease-out;
            color: white;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
          }
          
          .notification-content {
            padding: 16px;
            display: flex;
            align-items: flex-start;
            gap: 12px;
            position: relative;
          }
          
          .notification-avatar {
            font-size: 24px;
            flex-shrink: 0;
          }
          
          .notification-text {
            flex: 1;
          }
          
          .notification-title {
            font-weight: 600;
            font-size: 14px;
            margin-bottom: 4px;
          }
          
          .notification-message {
            font-size: 13px;
            line-height: 1.4;
            opacity: 0.9;
          }
          
          .notification-close {
            position: absolute;
            top: 8px;
            right: 8px;
            background: none;
            border: none;
            color: white;
            font-size: 18px;
            cursor: pointer;
            width: 24px;
            height: 24px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            transition: background-color 0.2s;
          }
          
          .notification-close:hover {
            background-color: rgba(255, 255, 255, 0.2);
          }
          
          @keyframes slideInUp {
            from {
              transform: translateY(100%);
              opacity: 0;
            }
            to {
              transform: translateY(0);
              opacity: 1;
            }
          }
          
          @media (max-width: 480px) {
            .welcome-notification {
              width: calc(100vw - 40px);
              right: 20px;
              left: 20px;
            }
          }
        `
        
        document.head.appendChild(style)
        document.body.appendChild(notification)
        
        // 5秒后自动消失
        setTimeout(() => {
          if (notification.parentElement) {
            notification.style.animation = 'slideOutDown 0.3s ease-in'
            setTimeout(() => {
              if (notification.parentElement) {
                notification.remove()
              }
            }, 300)
          }
        }, 5000)
        
        // 添加滑出动画
        const slideOutStyle = document.createElement('style')
        slideOutStyle.textContent = `
          @keyframes slideOutDown {
            from {
              transform: translateY(0);
              opacity: 1;
            }
            to {
              transform: translateY(100%);
              opacity: 0;
            }
          }
        `
        document.head.appendChild(slideOutStyle)
      }
    },
    
    toggleChat() {
      this.isOpen = !this.isOpen
      if (this.isOpen) {
        this.$nextTick(() => {
          this.$refs.messageInput?.focus()
          this.scrollToBottom()
        })
      }
    },
    

    async sendMessage() {
      if (!this.currentMessage.trim() || this.isLoading) return
      
      const userMessage = {
        type: 'user',
        text: this.currentMessage,
        timestamp: new Date()
      }
      
      this.messages.push(userMessage)
      const question = this.currentMessage
      this.currentMessage = ''
      this.isLoading = true
      
      this.scrollToBottom()
      
      try {
        const response = await fetch(`${API_BASE_URL}/rag/quick-query?question=${encodeURIComponent(question)}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json'
          }
        })
        
        const data = await response.json()
        
        if (data.code === 1) {
          const botMessage = {
            type: 'bot',
            text: data.data,
            timestamp: new Date()
          }
          this.messages.push(botMessage)
        } else {
          const errorMessage = {
            type: 'bot',
            text: 'Sorry, I cannot answer your question at the moment. Please try again later.',
            timestamp: new Date()
          }
          this.messages.push(errorMessage)
        }
      } catch (error) {
        console.error('发送消息失败:', error)
        const errorMessage = {
          type: 'bot',
          text: 'Network connection failed. Please check your connection and try again.',
          timestamp: new Date()
        }
        this.messages.push(errorMessage)
      } finally {
        this.isLoading = false
        this.scrollToBottom()
      }
    },
    
    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$refs.messagesContainer
        if (container) {
          container.scrollTop = container.scrollHeight
        }
      })
    },
    
    formatTime(timestamp) {
      return timestamp.toLocaleTimeString('zh-CN', { 
        hour: '2-digit', 
        minute: '2-digit' 
      })
    },
    
    formatMessageText(text) {
      if (!text) return ''
      
      // 处理 **文本** 格式，转换为换行加粗
      let formattedText = text.replace(/\*\*(.*?)\*\*/g, '<br><strong>$1</strong>')
      
      // 处理 * 文本 格式（单个*后面跟空格和内容），转换为斜体
      formattedText = formattedText.replace(/\*\s+([^*\n]+)/g, '<em>$1</em>')
      
      // 处理 *文本* 格式（成对的*），转换为斜体
      formattedText = formattedText.replace(/\*([^*\n]+)\*/g, '<em>$1</em>')
      
      // 将 "Main source files referenced:" 加粗
      formattedText = formattedText.replace(/Main source files referenced:/g, '<strong>Main source files referenced:</strong>')
      
      // 将换行符转换为HTML换行
      formattedText = formattedText.replace(/\n/g, '<br>')
      
      return formattedText
    }
  }
}
</script>

<style scoped>
.chat-widget {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.chat-button {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #4682B4 0%, #20B2AA 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 20px rgba(70, 130, 180, 0.4);
  transition: all 0.3s ease;
  color: white;
}

.chat-button:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 25px rgba(70, 130, 180, 0.6);
}

.chat-window {
  width: 350px;
  height: 500px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border: 1px solid #e1e5e9;
}

.chat-header {
  background: linear-gradient(135deg, #4682B4 0%, #20B2AA 100%);
  color: white;
  padding: 16px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.chat-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  font-size: 16px;
}

.close-button {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.close-button:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

.chat-messages {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  background: linear-gradient(180deg, #B0E0E6 0%, #E0F6FF 100%);
}

.welcome-message {
  text-align: center;
  color: #4682B4;
  font-size: 14px;
  line-height: 1.5;
}

.welcome-message p {
  margin: 8px 0;
}

.message {
  margin-bottom: 16px;
}

.message.user {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.user-message-container {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  justify-content: flex-end;
}

.user-message-bubble-group {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.message-content {
  display: flex;
  align-items: flex-start;
  gap: 8px;
}

.user-avatar, .bot-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.user-avatar {
  background: #4682B4;
  color: white;
}

.bot-avatar {
  background: #20B2AA;
  color: white;
}

.message-text {
  background: white;
  padding: 12px 16px;
  border-radius: 18px;
  max-width: 250px;
  word-wrap: break-word;
  line-height: 1.4;
  font-size: 14px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.message.user .message-text {
  background: #4682B4;
  color: white;
}

.message.bot .message-text {
  background: #B0E0E6;
  color: #4682B4;
  border: 1px solid #87CEEB;
}

.message-time {
  font-size: 11px;
  color: #4682B4;
  margin-top: 4px;
}

.user-message-bubble-group .message-time {
  text-align: right;
  margin-top: 4px;
  margin-right: 0;
  width: fit-content;
}

.message.bot .message-time {
  text-align: left;
  margin-left: 40px;
}

.typing-indicator {
  display: flex;
  gap: 4px;
  align-items: center;
}

.typing-indicator span {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #4682B4;
  animation: typing 1.4s infinite ease-in-out;
}

.typing-indicator span:nth-child(1) {
  animation-delay: -0.32s;
}

.typing-indicator span:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes typing {
  0%, 80%, 100% {
    transform: scale(0.8);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

.chat-input {
  padding: 16px;
  background: white;
  border-top: 1px solid #e1e5e9;
}

.input-container {
  display: flex;
  gap: 8px;
  align-items: center;
}

.input-container input {
  flex: 1;
  padding: 12px 16px;
  border: 1px solid #e1e5e9;
  border-radius: 24px;
  outline: none;
  font-size: 14px;
  transition: border-color 0.2s;
}

.input-container input:focus {
  border-color: #4682B4;
}

.input-container input:disabled {
  background: #f8f9fa;
  color: #6c757d;
}

.send-button {
  width: 40px;
  height: 40px;
  background: #4682B4;
  border: none;
  border-radius: 50%;
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.send-button:hover:not(:disabled) {
  background: #3A6B9A;
  transform: scale(1.05);
}

.send-button:disabled {
  background: #6c757d;
  cursor: not-allowed;
  transform: none;
}


/* 响应式设计 */
@media (max-width: 480px) {
  .chat-window {
    width: calc(100vw - 40px);
    height: calc(100vh - 40px);
    bottom: 20px;
    right: 20px;
  }
  
  .chat-widget {
    bottom: 10px;
    right: 10px;
  }
}
</style>
