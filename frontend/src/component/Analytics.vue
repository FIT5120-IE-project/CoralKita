<template>
  <div class="page-container">
    <!-- Background decoration elements -->
    <div class="background-decorations">
      <div class="decoration-left">
        <div class="bubble bubble-1"></div>
        <div class="bubble bubble-2"></div>
        <div class="wave-pattern wave-1"></div>
      </div>
      <div class="decoration-right">
        <div class="bubble bubble-3"></div>
        <div class="bubble bubble-4"></div>
        <div class="wave-pattern wave-2"></div>
      </div>
    </div>

    <!-- Top navigation -->
    <div class="top-nav">
      <div class="nav-left">
        <h1 class="logo" @click="goToHome">CoralKita</h1>
      </div>
      <div class="nav-center">
        <span class="nav-item active">Data Analysis</span>
      </div>
      <div class="nav-right">
        <div class="nav-icons">
          <div class="nav-icon" @click="goToTrends" title="Trend Analysis">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
              <path d="M16 6l2.29 2.29-4.88 4.88-4-4L2 16.59 3.41 18l6-6 4 4 6.3-6.29L22 12V6z"/>
            </svg>
          </div>
          <div class="nav-icon" @click="goToDataManagement" title="Data Management">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
            </svg>
          </div>
          <div class="nav-icon" @click="goToReports" title="Report Generation">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
              <path d="M14 2H6c-1.1 0-1.99.9-1.99 2L4 20c0 1.1.89 2 2 2h8c1.1 0 2-.9 2-2V8l-6-6zm2 16H8v-2h8v2zm0-4H8v-2h8v2zm-3-5V3.5L18.5 9H13z"/>
            </svg>
          </div>
          <div class="nav-icon" @click="goToSettings" title="Settings">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
              <path d="M19.14,12.94c0.04-0.3,0.06-0.61,0.06-0.94c0-0.32-0.02-0.64-0.07-0.94l2.03-1.58c0.18-0.14,0.23-0.41,0.12-0.61 l-1.92-3.32c-0.12-0.22-0.37-0.29-0.59-0.22l-2.39,0.96c-0.5-0.38-1.03-0.7-1.62-0.94L14.4,2.81c-0.04-0.24-0.24-0.41-0.48-0.41 h-3.84c-0.24,0-0.43,0.17-0.47,0.41L9.25,5.35C8.66,5.59,8.12,5.92,7.63,6.29L5.24,5.33c-0.22-0.08-0.47,0-0.59,0.22L2.74,8.87 C2.62,9.08,2.66,9.34,2.86,9.48l2.03,1.58C4.84,11.36,4.82,11.69,4.82,12s0.02,0.64,0.07,0.94l-2.03,1.58 c-0.18,0.14-0.23,0.41-0.12,0.61l1.92,3.32c0.12,0.22,0.37,0.29,0.59,0.22l2.39-0.96c0.5,0.38,1.03,0.7,1.62,0.94l0.36,2.54 c0.05,0.24,0.24,0.41,0.48,0.41h3.84c0.24,0,0.44-0.17,0.47-0.41l0.36-2.54c0.59-0.24,1.13-0.56,1.62-0.94l2.39,0.96 c0.22,0.08,0.47,0,0.59-0.22l1.92-3.32c0.12-0.22,0.07-0.47-0.12-0.61L19.14,12.94z M12,15.6c-1.98,0-3.6-1.62-3.6-3.6 s1.62-3.6,3.6-3.6s3.6,1.62,3.6,3.6S13.98,15.6,12,15.6z"/>
            </svg>
          </div>
          <div class="nav-icon" @click="goToProfile" title="Personal Center">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 3c1.66 0 3 1.34 3 3s-1.34 3-3 3-3-1.34-3-3 1.34-3 3-3zm0 14.2c-2.5 0-4.71-1.28-6-3.22.03-1.99 4-3.08 6-3.08 1.99 0 5.97 1.09 6 3.08-1.29 1.94-3.5 3.22-6 3.22z"/>
            </svg>
          </div>
        </div>
      </div>
    </div>

    <!-- Main content area -->
    <div class="main-content">
      <div class="content-card">
        <div class="empty-state">
          <div class="empty-icon">
            <svg width="80" height="80" viewBox="0 0 24 24" fill="currentColor">
              <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zM9 17H7v-7h2v7zm4 0h-2V7h2v10zm4 0h-2v-4h2v4z"/>
            </svg>
          </div>
          <h2>Data Analysis</h2>
          <p>Advanced data analysis features are under development...</p>
          <p class="description">This will provide deep data analysis, statistical modeling, and predictive analysis features</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Analytics',
  methods: {
    goToHome() { window.location.href = '/'; },
    goToTrends() { window.location.href = '/trends'; },
    goToDataManagement() { window.location.href = '/data-management'; },
    goToReports() { window.location.href = '/reports'; },
    goToSettings() { window.location.href = '/settings'; },
    goToProfile() { window.location.href = '/profile'; }
  }
};
</script>

<style scoped>
/* Reuse the same styles */
.page-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #333;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  position: relative;
  overflow-x: hidden;
}

.background-decorations {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  z-index: 0;
}

.decoration-left, .decoration-right {
  position: absolute;
  top: 0;
  bottom: 0;
  width: 200px;
}

.decoration-left { left: 0; }
.decoration-right { right: 0; }

.bubble {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.bubble-1 { width: 60px; height: 60px; top: 20%; left: 20px; animation-delay: 0s; }
.bubble-2 { width: 40px; height: 40px; top: 60%; left: 50px; animation-delay: 2s; }
.bubble-3 { width: 50px; height: 50px; top: 30%; right: 30px; animation-delay: 1s; }
.bubble-4 { width: 35px; height: 35px; top: 70%; right: 60px; animation-delay: 3s; }

.wave-pattern {
  position: absolute;
  width: 100px;
  height: 2px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 2px;
}

.wave-1 { top: 40%; left: 80px; transform: rotate(45deg); }
.wave-2 { top: 50%; right: 80px; transform: rotate(-45deg); }

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-20px); }
}

.top-nav {
  background: rgba(74, 85, 104, 0.95);
  backdrop-filter: blur(10px);
  color: white;
  padding: 12px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  z-index: 10;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.nav-left .logo {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 8px 12px;
  border-radius: 8px;
}

.nav-left .logo:hover {
  background: rgba(255, 255, 255, 0.1);
  transform: scale(1.05);
  color: #63b3ed;
}

.nav-center .nav-item {
  padding: 8px 16px;
  border-bottom: 2px solid transparent;
  font-weight: 500;
}

.nav-item.active { border-bottom-color: #63b3ed; }

.nav-icons {
  display: flex;
  gap: 8px;
  align-items: center;
}

.nav-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: rgba(255, 255, 255, 0.8);
  position: relative;
}

.nav-icon:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #63b3ed;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.nav-icon::after {
  content: attr(title);
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  white-space: nowrap;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.3s ease;
  margin-top: 5px;
  z-index: 1000;
}

.nav-icon:hover::after { opacity: 1; }

.main-content {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 80px);
  padding: 40px 20px;
  position: relative;
  z-index: 5;
}

.content-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 60px 40px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  text-align: center;
  max-width: 500px;
  width: 100%;
}

.empty-state { color: #4a5568; }
.empty-icon { color: #667eea; margin-bottom: 20px; }
.empty-state h2 { font-size: 2rem; margin: 20px 0 16px 0; color: #2d3748; }
.empty-state p { font-size: 1.1rem; margin: 12px 0; color: #718096; }
.description { font-size: 1rem !important; opacity: 0.8; }

@media (max-width: 768px) {
  .content-card { padding: 40px 20px; margin: 20px; }
  .empty-state h2 { font-size: 1.5rem; }
  .nav-icons { gap: 4px; }
  .nav-icon { width: 36px; height: 36px; }
}
</style>
