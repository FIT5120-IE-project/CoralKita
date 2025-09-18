<template>
  <div class="government-container">
    <!-- Top Navigation -->
    <div class="top-nav">
      <div class="nav-left">
        <!-- Left side logo -->
        <img src="@/assets/icon.png" alt="logo" class="nav-logo" @click="goToHome" />
        <h1 class="logo" @click="goToHome">CoralKita</h1>
      </div>
      <div class="nav-right">
        <div class="nav-items">
          <div class="nav-item-dropdown" @mouseenter="showTravelDropdown = true" @mouseleave="showTravelDropdown = false">
            <span class="nav-item">Island</span>
            <div class="dropdown-menu" v-show="showTravelDropdown">
              <div 
                v-for="island in travelIslands" 
                :key="island"
                class="dropdown-item"
                @click="goToIslandDetail(island)"
              >
                <span>{{ island }}</span>
              </div>
            </div>
          </div>
          <span class="nav-item" @click="goToMap">Map</span>
          <span class="nav-item" @click="goToEducation">Education</span>
          <span class="nav-item active">Government</span>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
      <!-- Mode Toggle -->
      <div class="mode-toggle">
        <button 
          class="toggle-btn" 
          :class="{ active: showMaze }" 
          @click="showMaze = true"
        >
          🎮 Coral Maze Adventure
        </button>
        <button 
          class="toggle-btn" 
          :class="{ active: !showMaze }" 
          @click="showMaze = false"
        >
          🏛️ Government Portal
        </button>
      </div>

      <!-- Maze Game Content -->
      <div v-if="showMaze" class="maze-content">
        <div class="maze-container">
          <img src="@/assets/game/mazesmall.png" alt="Coral Maze" class="maze-image" />
          
          <!-- Pixi.js画布容器 -->
          <div id="coin-canvas" class="pixi-canvas"></div>
          
          <!-- 金币道具 -->
          <CoinAnimation 
            v-for="(coin, index) in coins" 
            :key="index"
            :x="coin.x" 
            :y="coin.y"
            :isCollecting="coin.isCollecting"
            :coinImage="coinImage"
            @click="collectCoin(index)"
          />
          
          <div class="game-overlay">
            <div class="game-info">
              <div class="score">Score: {{ score }}</div>
              <div class="lives">Lives: {{ lives }}</div>
              <div class="level">Level: {{ level }}</div>
              <div class="coins">Coins: {{ collectedCoins }}</div>
            </div>
            <div class="game-controls">
              <button @click="pauseGame" class="control-btn">{{ isPaused ? 'Resume' : 'Pause' }}</button>
              <button @click="restartGame" class="control-btn">Restart</button>
              <button @click="exitFullscreen" class="control-btn">Exit</button>
            </div>
          </div>
        </div>
        
        <div class="game-instructions">
          <h3>Controls:</h3>
          <p>← → ↑ ↓ Arrow Keys or WASD: Move Player | Space: Action | R: Restart</p>
          <p>Navigate through the coral maze and collect treasures while avoiding dangers!</p>
          <p>Walk near the spinning coins to collect them automatically!</p>
        </div>
      </div>

      <!-- Government Portal Content -->
      <div v-else class="coming-soon-card">
        <div class="icon-container">
          <div class="government-icon">🏛️</div>
        </div>
        <h1>Government Portal</h1>
        <h2>Coming Soon</h2>
        <p>We're developing a comprehensive government portal to provide official resources, policies, and collaborative tools for coral reef conservation efforts.</p>
        
        <div class="features-preview">
          <h3>What's Coming:</h3>
          <ul>
            <li>📋 Government policies and regulations</li>
            <li>📊 Official coral reef status reports</li>
            <li>🤝 Collaboration tools for researchers</li>
            <li>📞 Contact information for relevant agencies</li>
            <li>📄 Conservation project applications</li>
            <li>🎯 Funding opportunities and grants</li>
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
import CoinAnimation from './CoinAnimation.vue'
import * as PIXI from 'pixi.js'

export default {
  name: 'Government',
  components: {
    CoinAnimation
  },
  data() {
    return {
      // Travel dropdown related
      showTravelDropdown: false,
      travelIslands: ['Mertang', 'P Singa', 'Sipadan', 'Pulau Lima', 'Seri Buat'],
      
      showMaze: true, // 默认显示迷宫游戏
      score: 0,
      lives: 3,
      level: 1,
      isPaused: false,
      collectedCoins: 0,
      coinImage: '',
      coinApp: null, // Pixi.js应用实例
      coinSprites: [], // 金币精灵数组
      coinTexture: null, // 金币纹理
      playerSprite: null, // 玩家精灵
      playerTexture: null, // 玩家纹理
      playerTextures: [], // 玩家纹理数组
      playerDirection: 'down', // 玩家方向: up, down, left, right
              playerPosition: { x: 400, y: 300 }, // 玩家位置 - 在完整图片范围内
      keys: {}, // 键盘状态
      coins: [
        { x: 230, y: 120, isCollecting: false },
        { x: 70, y: 110, isCollecting: false },
        { x: 600, y: 400, isCollecting: false },
        { x: 600, y: 100, isCollecting: false },
        { x: 80, y: 350, isCollecting: false },
        { x: 180, y: 500, isCollecting: false },
        { x: 320, y: 400, isCollecting: false }
      ]
    }
  },
  mounted() {
    // Set global refresh detection timestamp for verification system
    localStorage.setItem('lastPageRefresh', Date.now().toString());
    
    // 初始化Pixi.js金币动画
    this.$nextTick(() => {
      this.initCoinAnimation()
    })
  },
  beforeDestroy() {
    // 清理键盘事件
    document.removeEventListener('keydown', this.handleKeyDown)
    document.removeEventListener('keyup', this.handleKeyUp)
    
    if (this.coinApp) {
      this.coinApp.destroy(true)
    }
  },
  methods: {
    // Travel navigation methods
    goToIslandDetail(islandName) {
      console.log('导航到岛屿详情页面:', islandName);
      this.showTravelDropdown = false;
      
      this.$nextTick(() => {
        this.$router.push(`/travel/${encodeURIComponent(islandName)}`).catch(err => {
          if (err.name !== 'NavigationDuplicated') {
            console.error('Navigation error:', err);
          }
        });
      });
    },
    
    initCoinAnimation() {
      console.log('Initializing coin animation...')
      
      // 创建Pixi.js应用 - 使用完整图片尺寸
      this.coinApp = new PIXI.Application({
        width: 800,
        height: 600,
        transparent: true,
        antialias: true
      })
      
      // 将画布添加到DOM
      const canvasContainer = document.getElementById('coin-canvas')
      console.log('Canvas container found:', canvasContainer)
      
      if (canvasContainer) {
        canvasContainer.appendChild(this.coinApp.view)
        console.log('Canvas appended to container')
        console.log('Canvas element:', this.coinApp.view)
        console.log('Canvas size:', this.coinApp.view.width, 'x', this.coinApp.view.height)
      } else {
        console.error('Canvas container not found!')
      }
      
      // 添加一个简单的测试图形
      const testGraphics = new PIXI.Graphics()
      testGraphics.beginFill(0xFF0000)
      testGraphics.drawRect(100, 100, 50, 50)
      testGraphics.endFill()
      this.coinApp.stage.addChild(testGraphics)
      console.log('Test rectangle added to stage')
      
      // 加载金币纹理
      this.loadCoinTexture()
      
      // 加载玩家纹理
      this.loadPlayerTexture()
      
      // 设置键盘输入
      this.setupKeyboardInput()
    },
    
    loadCoinTexture() {
      try {
        const coinImagePath = require('@/assets/game/ltems.png')
        console.log('Loading coin texture:', coinImagePath)
        
        this.coinTexture = PIXI.Texture.from(coinImagePath)
        
        this.coinTexture.on('loaded', () => {
          console.log('Coin texture loaded successfully')
          this.createCoinSprites()
        })
        
        this.coinTexture.on('error', (error) => {
          console.error('Failed to load coin texture:', error)
        })
        
      } catch (error) {
        console.error('Failed to require coin image:', error)
        // 尝试直接路径
        this.coinTexture = PIXI.Texture.from('/src/assets/game/ltems.png')
        this.coinTexture.on('loaded', () => {
          this.createCoinSprites()
        })
      }
    },
    
    createCoinSprites() {
      console.log('Creating coin sprites...')
      
      // 清空之前的精灵
      this.coinSprites.forEach(sprite => {
        if (sprite.parent) {
          sprite.parent.removeChild(sprite)
        }
      })
      this.coinSprites = []
      
      // 创建金币帧纹理数组 (10帧，每帧32x32)
      const coinFrames = []
      for (let i = 0; i < 10; i++) {
        const frame = new PIXI.Rectangle(i * 32, 0, 32, 32)
        const texture = new PIXI.Texture(this.coinTexture.baseTexture, frame)
        coinFrames.push(texture)
      }
      
      // 为每个金币位置创建动画精灵
      this.coins.forEach((coin, index) => {
        const animatedSprite = new PIXI.AnimatedSprite(coinFrames)
        animatedSprite.x = coin.x
        animatedSprite.y = coin.y
        animatedSprite.anchor.set(0.5)
        animatedSprite.animationSpeed = 0.2
        animatedSprite.play()
        
        // 添加点击事件
        animatedSprite.interactive = true
        animatedSprite.buttonMode = true
        animatedSprite.on('pointerdown', () => {
          this.collectCoin(index)
        })
        
        this.coinApp.stage.addChild(animatedSprite)
        this.coinSprites.push(animatedSprite)
      })
      
      console.log(`Created ${this.coinSprites.length} coin sprites`)
    },
    
    loadPlayerTexture() {
      try {
        const playerImagePath = require('@/assets/game/man.png')
        console.log('Loading player texture:', playerImagePath)
        
        this.playerTexture = PIXI.Texture.from(playerImagePath)
        
        // 检查纹理是否已经加载
        if (this.playerTexture.baseTexture.valid) {
          console.log('Player texture already loaded, size:', this.playerTexture.width, 'x', this.playerTexture.height)
          this.createPlayerSprite()
        } else {
          this.playerTexture.on('loaded', () => {
            console.log('Player texture loaded successfully, size:', this.playerTexture.width, 'x', this.playerTexture.height)
            this.createPlayerSprite()
          })
          
          this.playerTexture.on('error', (error) => {
            console.error('Failed to load player texture:', error)
          })
        }
        
      } catch (error) {
        console.error('Failed to require player image:', error)
        // 尝试直接路径
        this.playerTexture = PIXI.Texture.from('/src/assets/game/man.png')
        
        if (this.playerTexture.baseTexture.valid) {
          console.log('Player texture loaded via direct path, size:', this.playerTexture.width, 'x', this.playerTexture.height)
          this.createPlayerSprite()
        } else {
          this.playerTexture.on('loaded', () => {
            console.log('Player texture loaded via direct path')
            this.createPlayerSprite()
          })
          this.playerTexture.on('error', (error) => {
            console.error('Failed to load player texture via direct path:', error)
          })
        }
      }
    },
    
    createPlayerSprite() {
      console.log('Creating player sprite...')
      console.log('Player texture size:', this.playerTexture.width, 'x', this.playerTexture.height)
      console.log('Player texture valid:', this.playerTexture.baseTexture.valid)
      image.png      
      // 创建玩家精灵 (man.png是48x12，包含6个状态，每个8x12)
      // 状态顺序: 左1, 左2, 下1, 下2, 右1, 右2
      const playerFrames = []
      for (let i = 0; i < 6; i++) {
        const frame = new PIXI.Rectangle(i * 8, 0, 8, 12)
        const texture = new PIXI.Texture(this.playerTexture.baseTexture, frame)
        playerFrames.push(texture)
        console.log(`Frame ${i}:`, frame, 'texture valid:', texture.baseTexture.valid)
      }
      
      this.playerSprite = new PIXI.AnimatedSprite(playerFrames)
      this.playerSprite.x = this.playerPosition.x
      this.playerSprite.y = this.playerPosition.y
      this.playerSprite.anchor.set(0.5)
      this.playerSprite.animationSpeed = 0.1
      this.playerSprite.visible = true
      this.playerSprite.alpha = 1
      this.playerSprite.scale.set(2) // 放大2倍，让角色更明显
      
      // 存储纹理数组供方向切换使用
      this.playerTextures = playerFrames
      
      // 设置初始方向为向下
      this.setPlayerDirection('down')
      
      this.coinApp.stage.addChild(this.playerSprite)
      
      // 添加一个测试矩形来确保Pixi.js工作正常
      const testRect = new PIXI.Graphics()
      testRect.beginFill(0xFF0000)
      testRect.drawRect(50, 50, 20, 20)
      testRect.endFill()
      this.coinApp.stage.addChild(testRect)
      console.log('Test rectangle added')
      
      console.log('Player sprite created at position:', this.playerPosition)
      console.log('Player sprite visible:', this.playerSprite.visible)
      console.log('Player sprite alpha:', this.playerSprite.alpha)
      console.log('Player sprite scale:', this.playerSprite.scale.x, this.playerSprite.scale.y)
      console.log('Canvas size:', this.coinApp.screen.width, 'x', this.coinApp.screen.height)
      console.log('Stage children count:', this.coinApp.stage.children.length)
      console.log('Player sprite in stage:', this.coinApp.stage.children.includes(this.playerSprite))
      console.log('Player sprite world position:', this.playerSprite.getGlobalPosition())
      console.log('Player sprite renderable:', this.playerSprite.renderable)
      console.log('Player sprite parent:', this.playerSprite.parent)
      console.log('Player sprite stage:', this.playerSprite.stage)
    },
    
    setPlayerDirection(direction) {
      this.playerDirection = direction
      
      if (!this.playerSprite || !this.playerTextures) return
      
      // 重置缩放
      this.playerSprite.scale.x = 1
      
      // 根据方向设置对应的帧
      // 状态顺序: 左1(0), 左2(1), 下1(2), 下2(3), 右1(4), 右2(5)
      switch (direction) {
        case 'left':
          // 左转使用左1帧
          this.playerSprite.texture = this.playerTextures[0]
          break
        case 'down':
          // 下转使用下1帧
          this.playerSprite.texture = this.playerTextures[2]
          break
        case 'up':
          // 上转使用下1帧的镜像（因为没有上转帧）
          this.playerSprite.texture = this.playerTextures[2]
          this.playerSprite.scale.y = -1 // 垂直翻转
          break
        case 'right':
          // 右转使用右1帧
          this.playerSprite.texture = this.playerTextures[4]
          break
      }
      
      console.log('Player direction set to:', direction)
    },
    
    setupKeyboardInput() {
      // 键盘输入处理
      this.handleKeyDown = (event) => {
        this.keys[event.code] = true
        event.preventDefault()
      }
      
      this.handleKeyUp = (event) => {
        this.keys[event.code] = false
        event.preventDefault()
      }
      
      document.addEventListener('keydown', this.handleKeyDown)
      document.addEventListener('keyup', this.handleKeyUp)
      
      // 开始游戏循环
      this.coinApp.ticker.add(this.gameLoop)
    },
    
    gameLoop() {
      if (this.isPaused || !this.playerSprite) return
      
      const speed = 3
      let moved = false
      
      // 处理移动
      if (this.keys['ArrowLeft'] || this.keys['KeyA']) {
        this.playerPosition.x -= speed
        this.setPlayerDirection('left')
        this.animatePlayer('left')
        moved = true
      }
      if (this.keys['ArrowRight'] || this.keys['KeyD']) {
        this.playerPosition.x += speed
        this.setPlayerDirection('right')
        this.animatePlayer('right')
        moved = true
      }
      if (this.keys['ArrowUp'] || this.keys['KeyW']) {
        this.playerPosition.y -= speed
        this.setPlayerDirection('up')
        this.animatePlayer('up')
        moved = true
      }
      if (this.keys['ArrowDown'] || this.keys['KeyS']) {
        this.playerPosition.y += speed
        this.setPlayerDirection('down')
        this.animatePlayer('down')
        moved = true
      }
      
      // 更新玩家位置
      this.playerSprite.x = this.playerPosition.x
      this.playerSprite.y = this.playerPosition.y
      
      // 边界检查
      this.playerPosition.x = Math.max(16, Math.min(784, this.playerPosition.x))
      this.playerPosition.y = Math.max(16, Math.min(584, this.playerPosition.y))
      
      // 检查金币收集
      this.checkCoinCollision()
    },
    
    animatePlayer(direction) {
      if (!this.playerSprite || !this.playerTextures) return
      
      // 重置缩放
      this.playerSprite.scale.x = 1
      this.playerSprite.scale.y = 1
      
      // 根据方向设置对应的帧和动画
      switch (direction) {
        case 'left':
          // 左转在左1和左2之间切换
          this.playerSprite.texture = this.playerTextures[Math.floor(Date.now() / 200) % 2] // 0或1
          break
        case 'down':
          // 下转在下1和下2之间切换
          this.playerSprite.texture = this.playerTextures[2 + (Math.floor(Date.now() / 200) % 2)] // 2或3
          break
        case 'up':
          // 上转使用下1和下2的镜像
          this.playerSprite.texture = this.playerTextures[2 + (Math.floor(Date.now() / 200) % 2)] // 2或3
          this.playerSprite.scale.y = -1 // 垂直翻转
          break
        case 'right':
          // 右转在右1和右2之间切换
          this.playerSprite.texture = this.playerTextures[4 + (Math.floor(Date.now() / 200) % 2)] // 4或5
          break
      }
    },
    
    checkCoinCollision() {
      if (!this.playerSprite) return
      
      this.coins.forEach((coin, index) => {
        if (coin.isCollecting) return
        
        const distance = Math.sqrt(
          Math.pow(this.playerPosition.x - coin.x, 2) + 
          Math.pow(this.playerPosition.y - coin.y, 2)
        )
        
        if (distance < 30) { // 碰撞检测距离
          this.collectCoin(index)
        }
      })
    },
    
    loadCoinImage() {
      try {
        this.coinImage = require('@/assets/game/ltems.png')
        console.log('Coin image loaded successfully:', this.coinImage)
      } catch (error) {
        console.error('Failed to load coin image:', error)
        // 尝试其他路径
        this.coinImage = '/src/assets/game/ltems.png'
      }
    },
    
    collectCoin(index) {
      if (this.coins[index].isCollecting) return
      
      // 开始收集动画
      this.coins[index].isCollecting = true
      this.collectedCoins++
      this.score += 10
      
      // 播放收集音效（如果有的话）
      this.playCollectSound()
      
      // 移除Pixi.js精灵
      if (this.coinSprites[index]) {
        const sprite = this.coinSprites[index]
        
        // 收集动画：缩放和淡出
        const collectTween = new PIXI.Ticker()
        let scale = 1
        let alpha = 1
        
        const animate = () => {
          scale += 0.1
          alpha -= 0.05
          sprite.scale.set(scale)
          sprite.alpha = alpha
          
          if (alpha <= 0) {
            collectTween.stop()
            if (sprite.parent) {
              sprite.parent.removeChild(sprite)
            }
            this.coins.splice(index, 1)
            this.coinSprites.splice(index, 1)
          }
        }
        
        collectTween.add(animate)
        collectTween.start()
      }
    },
    
    playCollectSound() {
      // 这里可以添加音效播放逻辑
      console.log('Coin collected! +10 points')
    },
    
    pauseGame() {
      this.isPaused = !this.isPaused
    },
    
    restartGame() {
      this.score = 0
      this.lives = 3
      this.level = 1
      this.isPaused = false
      this.collectedCoins = 0
      
      // 重新生成金币
      this.coins = [
        { x: 230, y: 120, isCollecting: false },
        { x: 70, y: 110, isCollecting: false },
        { x: 600, y: 400, isCollecting: false },
        { x: 600, y: 100, isCollecting: false },
        { x: 80, y: 350, isCollecting: false },
        { x: 180, y: 500, isCollecting: false },
        { x: 320, y: 400, isCollecting: false }
      ]
      
      // 重置玩家位置
      this.playerPosition = { x: 100, y: 100 }
      if (this.playerSprite) {
        this.playerSprite.x = this.playerPosition.x
        this.playerSprite.y = this.playerPosition.y
        this.setPlayerDirection('down')
      }
      
      // 重新创建金币精灵
      if (this.coinTexture) {
        this.createCoinSprites()
      }
    },
    
    exitFullscreen() {
      this.showMaze = false
    },
    goToHome() {
      window.location.href = '/';
    },

    goToMap() {
      this.$router.push('/map').catch(err => {
        // Ignore navigation duplicated error
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },


    goToEducation() {
      // 主页面导航，不设置标记，应该显示验证
      this.$router.push('/education').catch(err => {
        // Ignore navigation duplicated error
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
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
.government-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow-x: hidden;
}

.government-container::before {
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

/* Island 下拉菜单样式 */
.nav-item-dropdown {
  position: relative;
  order: -1; /* 确保Island在最前面 */
}

.dropdown-menu {
  position: absolute;
  top: calc(100% + 8px); /* 在Island正下方，留8px间距 */
  left: 50%;
  transform: translateX(-50%); /* 居中对齐 */
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(25px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15), 0 4px 16px rgba(0, 0, 0, 0.1);
  min-width: 220px;
  z-index: 1000;
  overflow: hidden;
  animation: dropdownSlideIn 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 添加小箭头指向Island */
.dropdown-menu::before {
  content: '';
  position: absolute;
  top: -6px;
  left: 50%;
  transform: translateX(-50%);
  width: 12px;
  height: 12px;
  background: rgba(255, 255, 255, 0.98);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-bottom: none;
  border-right: none;
  transform: translateX(-50%) rotate(45deg);
}

@keyframes dropdownSlideIn {
  from {
    opacity: 0;
    transform: translateX(-50%) translateY(-8px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateX(-50%) translateY(0) scale(1);
  }
}

.dropdown-item {
  display: flex;
  align-items: center;
  padding: 14px 20px;
  color: #1f2937;
  cursor: pointer;
  transition: all 0.2s ease;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  font-weight: 500;
}

.dropdown-item:last-child {
  border-bottom: none;
}

.dropdown-item:hover {
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.08), rgba(16, 185, 129, 0.08));
  color: #1e40af;
  transform: translateX(2px);
}

.dropdown-item:first-child:hover {
  border-radius: 16px 16px 0 0;
}

.dropdown-item:last-child:hover {
  border-radius: 0 0 16px 16px;
}

.dropdown-item:first-child:last-child:hover {
  border-radius: 16px;
}

/* Main Content */
.main-content {
  position: relative;
  z-index: 2;
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: calc(100vh - 70px);
  padding: 40px 20px;
}

/* Mode Toggle */
.mode-toggle {
  display: flex;
  gap: 10px;
  margin-bottom: 30px;
  background: rgba(255, 255, 255, 0.1);
  padding: 5px;
  border-radius: 25px;
  backdrop-filter: blur(10px);
}

.toggle-btn {
  background: transparent;
  color: rgba(255, 255, 255, 0.7);
  border: none;
  padding: 12px 24px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.toggle-btn:hover {
  color: white;
  background: rgba(255, 255, 255, 0.1);
}

.toggle-btn.active {
  background: linear-gradient(135deg, #01A2EB, #1A1D25);
  color: white;
  box-shadow: 0 4px 15px rgba(1, 162, 235, 0.3);
}

/* Maze Game Content */
.maze-content {
  width: 100%;
  max-width: 800px;
  background: #1a1a1a;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}

.maze-container {
  position: relative;
  width: 100%;
  height: 600px;
  overflow: hidden;
}

.maze-image {
  width: 200%;
  height: 100%;
  object-fit: cover;
  object-position: right center;
  display: block;
  /* 只显示右半边的3D迷宫，通过调整图片宽度和位置实现 */
  transform: translateX(-50%);
}

.pixi-canvas {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: auto; /* 允许鼠标交互 */
  z-index: 20; /* 确保在所有其他元素之上 */
}

.game-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  background: linear-gradient(135deg, rgba(44, 62, 80, 0.9), rgba(52, 73, 94, 0.9));
  color: white;
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
  backdrop-filter: blur(5px);
}

.game-info {
  display: flex;
  gap: 20px;
  align-items: center;
  flex-wrap: wrap;
}

.score, .lives, .level, .coins {
  font-size: 16px;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.coins {
  color: #FFD700;
}

.game-controls {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.control-btn {
  background: linear-gradient(135deg, #3498db, #2980b9);
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.control-btn:hover {
  background: linear-gradient(135deg, #2980b9, #1f4e79);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
}

.game-instructions {
  background: #2c3e50;
  color: white;
  padding: 15px 20px;
  font-size: 14px;
  line-height: 1.5;
}

.game-instructions h3 {
  margin: 0 0 10px 0;
  color: #3498db;
}

.game-instructions p {
  margin: 5px 0;
}

.coming-soon-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 25px;
  padding: 50px 40px;
  text-align: center;
  max-width: 650px;
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

.government-icon {
  font-size: 4rem;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
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
  
  .mode-toggle {
    flex-direction: column;
    width: 100%;
    max-width: 300px;
  }
  
  .toggle-btn {
    text-align: center;
  }
  
  .maze-header {
    flex-direction: column;
    align-items: stretch;
  }
  
  .game-info {
    justify-content: center;
  }
  
  .game-controls {
    justify-content: center;
  }
  
  .maze-canvas {
    height: 400px;
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
  
  .government-icon {
    font-size: 3rem;
  }
  
  .coming-soon-card h1 {
    font-size: 1.8rem;
  }
}
</style>
