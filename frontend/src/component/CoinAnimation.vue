<template>
  <div class="coin-container" :style="{ left: x + 'px', top: y + 'px' }">
    <div class="coin" :class="{ 'collecting': isCollecting }">
      <img 
        v-if="coinImage" 
        :src="coinImage" 
        class="coin-sprite" 
        alt="Coin"
        @error="onImageError"
        @load="onImageLoad"
      />
      <div v-else class="coin-fallback">💰</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CoinAnimation',
  props: {
    x: {
      type: Number,
      default: 0
    },
    y: {
      type: Number,
      default: 0
    },
    isCollecting: {
      type: Boolean,
      default: false
    },
    coinImage: {
      type: String,
      default: ''
    }
  },
  mounted() {
    console.log('Coin image loaded:', this.coinImage)
  },
  methods: {
    onImageLoad() {
      console.log('Coin image loaded successfully')
    },
    onImageError() {
      console.error('Failed to load coin image:', this.coinImage)
    }
  }
}
</script>

<style scoped>
.coin-container {
  position: absolute;
  width: 32px;
  height: 32px;
  z-index: 10;
}

.coin {
  width: 32px;
  height: 32px;
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
}

.coin:hover {
  transform: scale(1.1);
}

.coin.collecting {
  animation: collect 0.5s ease-out forwards;
}

.coin-sprite {
  width: 100%;
  height: 100%;
  object-fit: none;
  object-position: 0 0;
  animation: coinSpin 1s linear infinite;
  image-rendering: pixelated;
}

.coin-fallback {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  animation: coinSpin 1s linear infinite;
}

@keyframes coinSpin {
  0% { transform: rotateY(0deg); }
  10% { transform: rotateY(36deg); }
  20% { transform: rotateY(72deg); }
  30% { transform: rotateY(108deg); }
  40% { transform: rotateY(144deg); }
  50% { transform: rotateY(180deg); }
  60% { transform: rotateY(216deg); }
  70% { transform: rotateY(252deg); }
  80% { transform: rotateY(288deg); }
  90% { transform: rotateY(324deg); }
  100% { transform: rotateY(360deg); }
}

@keyframes collect {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.5);
    opacity: 0.8;
  }
  100% {
    transform: scale(0);
    opacity: 0;
  }
}
</style>
