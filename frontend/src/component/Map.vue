<template>
  <div class="map-container">
    <!-- Top Navigation -->
    <div class="top-nav">
      <div class="nav-left">
        <!-- Left side logo -->
        <img src="@/assets/icon.png" alt="logo" class="nav-logo" @click="goToHome" />
        <h1 class="logo" @click="goToHome">CoralKita</h1>
      </div>
      <div class="nav-right">
        <div class="nav-items">
          <span class="nav-item active">Map</span>
          <span class="nav-item" @click="goToTrends">Trends</span>
          <span class="nav-item" @click="goToEducation">Education</span>
          <span class="nav-item" @click="goToGovernment">Government</span>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
      <div class="coming-soon-card">
        <div class="icon-container">
          <div class="map-icon">🗺️</div>
        </div>
        <h1>Interactive Map</h1>
        <h2>Coming Soon</h2>
        <p>We're working on an amazing interactive map feature that will help you explore coral reef locations around Malaysia and track their health status in real-time.</p>
        
        <div class="features-preview">
          <h3>What's Coming:</h3>
          <ul>
            <li>📍 Real-time coral reef locations</li>
            <li>🌊 Interactive marine data visualization</li>
            <li>📊 Health status indicators</li>
            <li>🔍 Detailed location information</li>
            <li>📱 Mobile-friendly navigation</li>
          </ul>
        </div>

        <button class="back-button" @click="goBack">
          ← Back to Previous Page
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Map',
      mounted() {
      // Set global refresh detection timestamp for verification system
      localStorage.setItem('lastPageRefresh', Date.now().toString());
    },
  methods: {
    goToHome() {
      window.location.href = '/';
    },

    goToTrends() {
      localStorage.setItem('hasNavigatedToEducation', 'true');
      this.$router.push('/trends');
    },

    goToEducation() {
      localStorage.setItem('hasNavigatedToEducation', 'true');
      this.$router.push('/education');
    },

    goToGovernment() {
      localStorage.setItem('hasNavigatedToEducation', 'true');
      this.$router.push('/government');
    },

    goBack() {
      // Go back to previous page
      if (window.history.length > 1) {
        this.$router.go(-1);
      } else {
        // Fallback to home page
        this.goToHome();
      }
    }
  }
}
</script>

<style scoped>
.map-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow-x: hidden;
}

.map-container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('@/assets/bg_login5.jpg') center/cover;
  opacity: 0.3;
  z-index: 1;
}

/* Top Navigation */
.top-nav {
  background: linear-gradient(90deg, #1A1D25 0%, #01A2EB 100%);
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

.nav-left {
  display: flex;
  align-items: center;
  gap: 10px;
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

.nav-logo {
  height: 46px;
  cursor: pointer;
}

/* Navigation Items */
.nav-items {
  display: flex;
  gap: 32px;
  align-items: center;
}

.nav-item {
  padding: 8px 16px;
  color: rgba(255, 255, 255, 0.7);
  border-bottom: 2px solid transparent;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  font-size: 1rem;
}

.nav-item:hover {
  color: white;
  border-bottom-color: rgba(255, 255, 255, 0.5);
}

.nav-item.active {
  color: white;
  border-bottom-color: #63b3ed;
}

/* Main Content */
.main-content {
  position: relative;
  z-index: 2;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 70px);
  padding: 40px 20px;
}

.coming-soon-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 25px;
  padding: 50px 40px;
  text-align: center;
  max-width: 600px;
  width: 100%;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  animation: slideUp 0.6s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.icon-container {
  margin-bottom: 30px;
}

.map-icon {
  font-size: 4rem;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-10px);
  }
  60% {
    transform: translateY(-5px);
  }
}

.coming-soon-card h1 {
  color: #1A1D25;
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 10px;
  background: linear-gradient(135deg, #01A2EB, #1A1D25);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.coming-soon-card h2 {
  color: #01A2EB;
  font-size: 1.8rem;
  font-weight: 600;
  margin-bottom: 20px;
}

.coming-soon-card p {
  color: #555;
  font-size: 1.1rem;
  line-height: 1.6;
  margin-bottom: 30px;
}

.features-preview {
  text-align: left;
  margin: 30px 0;
  padding: 25px;
  background: rgba(1, 162, 235, 0.1);
  border-radius: 15px;
  border-left: 4px solid #01A2EB;
}

.features-preview h3 {
  color: #1A1D25;
  font-size: 1.3rem;
  font-weight: 600;
  margin-bottom: 15px;
  text-align: center;
}

.features-preview ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.features-preview li {
  color: #555;
  font-size: 1rem;
  margin-bottom: 12px;
  padding-left: 10px;
  position: relative;
}

.back-button {
  background: linear-gradient(135deg, #01A2EB, #1A1D25);
  color: white;
  border: none;
  padding: 14px 28px;
  border-radius: 25px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(1, 162, 235, 0.3);
  margin-top: 20px;
}

.back-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(1, 162, 235, 0.4);
}

/* Responsive Design */
@media (max-width: 768px) {
  .nav-items {
    gap: 20px;
  }
  
  .coming-soon-card {
    padding: 40px 30px;
    margin: 20px;
  }
  
  .coming-soon-card h1 {
    font-size: 2rem;
  }
  
  .coming-soon-card h2 {
    font-size: 1.5rem;
  }
  
  .features-preview {
    padding: 20px;
  }
}

@media (max-width: 480px) {
  .nav-items {
    gap: 15px;
  }
  
  .nav-item {
    font-size: 0.9rem;
    padding: 6px 12px;
  }
  
  .coming-soon-card {
    padding: 30px 20px;
  }
  
  .map-icon {
    font-size: 3rem;
  }
  
  .coming-soon-card h1 {
    font-size: 1.8rem;
  }
}
</style>
