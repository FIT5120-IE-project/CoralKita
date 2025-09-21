<template>
  <div class="trends-container bg-container">
    <!-- 背景图片加载占位符 -->
    <div class="bg-placeholder" v-if="!backgroundLoaded">
      <div class="progress-container">
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: loadingProgress + '%' }"></div>
        </div>
        <p class="loading-text">{{ loadingText }}</p>
      </div>
    </div>
    
    <!-- Canvas background animation -->
    <canvas ref="backgroundCanvas" class="background-canvas"></canvas>
    
    <!-- Background decoration elements -->
    <div class="background-decorations">
      <div class="decoration-left">
        <div class="bubble bubble-1"></div>
        <div class="bubble bubble-2"></div>
        <div class="bubble bubble-3"></div>
        <div class="wave-pattern wave-1"></div>
        <div class="wave-pattern wave-2"></div>
      </div>
      <div class="decoration-right">
        <div class="bubble bubble-4"></div>
        <div class="bubble bubble-5"></div>
        <div class="bubble bubble-6"></div>
        <div class="wave-pattern wave-3"></div>
        <div class="wave-pattern wave-4"></div>
      </div>
    </div>

    <!-- Top navigation -->
    <div class="top-nav">
      <div class="nav-left">
        <!-- Left side icon -->
        <img :src="imageUrls.appIcon" alt="logo" class="nav-logo" @click="goToHome" />
        <h1 class="logo" @click="goToHome">CoralKita</h1>
      </div>
      <div class="nav-right">
        <div class="nav-items">
          <div class="nav-item-wrapper">
            <span class="nav-item map-rec-item active">
              <span class="nav-text-line">Map &</span>
              <span class="nav-text-line">Recommendation</span>
            </span>
          </div>
          <div class="nav-item-dropdown" @mouseenter="showTravelDropdown = true" @mouseleave="showTravelDropdown = false">
            <span class="nav-item">Island</span>
            <div class="dropdown-menu" v-show="showTravelDropdown">
              <div 
                v-for="island in recommendedIslands" 
                :key="island.name"
                class="dropdown-item"
                @click="goToIslandDetail(island.name)"
              >
                <span>{{ island.name }}</span>
              </div>
            </div>
          </div>
          <span class="nav-item" @click="goToEducation">Education</span>
          <span class="nav-item" @click="goToAITools">AI Classification</span>
        </div>
      </div>
    </div>

    <!-- Main title -->
    <div class="page-title">
      <h2>Interactive Heat Map</h2>

    </div>

    <!-- Loading indicator removed -->

    <!-- New Layout: Heat Map on top, controls and comparison below -->
    <div class="new-layout">
      <!-- Main heat map area -->
      <div class="heat-map-section">
        <!-- Left: Select Panel -->
        <div class="select-panel">
          <h3>Select Panel</br></br></h3>
          
          <!-- Location Type Selection -->
          <div class="control-group">
            <label>Filter by State/Island</label>
            <div class="combobox-container">
              <select 
                v-model="selectedLocationType"
                @change="onLocationTypeChange"
                class="location-type-select"
              >
                <option value="">Select type...</option>
                <option value="state">State</option>
                <option value="island">Island</option>
              </select>
              <div class="select-arrow">
                <svg width="12" height="12" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M7 10l5 5 5-5z"/>
                </svg>
              </div>
            </div>
          </div>
          
          <!-- Dynamic Selection Combobox -->
          <div class="control-group">
            <label>{{ selectedLocationType === 'state' ? 'Select State' : selectedLocationType === 'island' ? 'Select Island' : 'Select Location' }}</label>
            <div class="combobox-container">
              <input 
                type="text" 
                v-model="dynamicInputValue"
                @input="onDynamicInputChange"
                @focus="onDynamicInputFocus"
                @blur="onDynamicInputBlur"
                @keydown="onDynamicKeyDown"
                :placeholder="selectedLocationType === 'state' ? 'Type or select a state...' : selectedLocationType === 'island' ? 'Type or select an island...' : 'Select location type first...'"
                class="island-input"
                :disabled="!selectedLocationType"
                ref="dynamicInput"
              />
              <button 
                type="button"
                @click="toggleDynamicDropdown"
                class="dropdown-toggle-btn"
                :disabled="!selectedLocationType"
              >
                <svg width="12" height="12" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M7 10l5 5 5-5z"/>
                </svg>
              </button>
              <div 
                v-show="showDynamicDropdown && selectedLocationType" 
                class="dropdown-list"
              >
                <div 
                  v-if="filteredDynamicOptions.length === 0"
                  class="dropdown-item no-results"
                >
                  {{ selectedLocationType === 'state' ? 'No states found' : 'No islands found' }}
                </div>
                <div 
                  v-for="option in filteredDynamicOptions" 
                  :key="option"
                  @click="selectFromDynamicCombobox(option)"
                  @mousedown.prevent
                  class="dropdown-item"
                >
                  {{ option }}
                </div>
              </div>
            </div>
        </div>
      </div>

        <!-- Center: Heat Map -->
        <div class="heat-map-container">
          <div class="map-header">
            <div v-if="selectedIsland" class="location-info">
              <span class="island-name">{{ selectedIsland }}</span>
              <div v-if="selectedCoordinates" class="coordinates">
                <span class="coord-label">Coordinates:</span>
                <span class="coord-values">{{ selectedCoordinates.lat.toFixed(4) }}°N, {{ selectedCoordinates.lng.toFixed(4) }}°E</span>
          </div>
              </div>
            <div class="map-instructions" v-if="!selectedIsland">
              <p>
                🎯 Use the left panel to filter by state or island.<br>🗺 Click the markers on the map to explore island details.<br>⭐ The right panel shows recommended islands, which you can click to view more information.
              </p>
              </div>
            </div>
          
          <div class="map-container" ref="mapContainer" id="leaflet-map">
            <!-- Leaflet heat map will be rendered here -->
            
            <!-- Loading indicators -->
            <div v-if="isLoadingCoordinates" class="loading-overlay">
              <div class="loading-spinner"></div>
              <div class="loading-text">Loading Malaysia's recent coral reef data...</div>
            </div>
            
            <div v-if="isLoadingCoralData" class="loading-overlay">
              <div class="loading-spinner"></div>
              <div class="loading-text">Loading Malaysia's recent coral reef data...</div>
            </div>
          </div>
          </div>
          
        <!-- Right: Recommended Islands -->
        <div class="recommendations-panel">
          <h3>Recommended Islands List</h3>
          <p class="recommendation-subtitle">The list is based on Live Coral Cover and the overall health status.</br></p>
          
          <div class="recommended-islands">
            <div 
              v-for="(island, index) in recommendedIslands" 
              :key="island.name"
              class="recommendation-item"
              @click="selectRecommendedIsland(island)"
            >
              <div class="recommendation-rank">
                <img v-if="index === 0" :src="imageUrls.crown1" alt="1st Place Crown" class="crown-icon" />
                <img v-else-if="index === 1" :src="imageUrls.crown2" alt="2nd Place Crown" class="crown-icon" />
                <img v-else-if="index === 2" :src="imageUrls.crown3" alt="3rd Place Crown" class="crown-icon" />
                <img v-else-if="index === 3" :src="imageUrls.crown4" alt="4th Place Crown" class="crown-icon" />
                <img v-else-if="index === 4" :src="imageUrls.crown5" alt="5th Place Crown" class="crown-icon" />
                <span v-else class="rank-number">{{ index + 1 }}</span>
              </div>
              <div class="recommendation-content">
                <h4>{{ island.name }}</h4>
                <div class="location-state">
                  {{ getIslandState(island.name) }}
          </div>
        </div>
              <div class="recommendation-arrow">➤</div>
      </div>
          </div>
          
          <div class="location-note">
            <p>Click to enter the island page</p>
        </div>
      </div>
    </div>

      <!-- New Layout: Left Panel + Visualization Area -->
    <div class="comparison-section">
    
    <div class="comparison-bg-wrapper">
      <h2 class="comparison-title">Island Comparison Visualization</h2>
      <div class="comparison-layout-container">
        <!-- Left: Island Selection Panel -->
        <div class="external-island-selection-panel">
        <h3>Comparison Panel</h3>
        <p>Select two islands and an attribute to compare coral reef health trends</p>
            <div class="island-selector">
              <label>🏝️Select Island:</label>
              <select v-model="compareIsland1" @change="updateComparisonChart" class="island-select">
                <option value="">Choose Island</option>
                <option 
                  v-for="island in availableIslands" 
                  :key="island"
                  :value="island"
                  :disabled="island === compareIsland2"
                >
                  {{ island }}
                </option>
              </select>
          </div>
            
            <div class="island-selector">
              <label>⚖️Compare Island:</label>
              <select v-model="compareIsland2" @change="updateComparisonChart" class="island-select">
                    <option value="">Choose Island</option>
                    <option 
                      v-for="island in availableIslands" 
                      :key="island"
                      :value="island"
                  :disabled="island === compareIsland1"
                    >
                      {{ island }}
                    </option>
                  </select>
          </div>
        <!-- 把 Select Attribute 放到这里 -->
          <div class="island-selector">
            <label>📊Select Attribute:</label>
            <div class="indicator-dropdown" @click.stop>
              <div class="selected" @click="toggleIndicatorDropdown">
                {{ selectedIndicatorLabel }}
              </div>
              <div class="options" v-show="showIndicatorDropdown">
                <div 
                  v-for="option in indicatorOptions" 
                  :key="option.value"
                  class="option"
                  @click="selectIndicator(option)"
                  @mouseenter="showTooltip($event, option.tooltip)"
                  @mouseleave="hideTooltip"
                >
                  {{ option.label }}
                </div>
              </div>
            </div>
          </div>
          <div class="external-island-selection-panel-img">
          <img :src="imageUrls.coral" alt="Info" class="info-icon" loading="lazy" @mouseenter="showTooltip($event, 'Select an attribute to compare coral reef health trends between the two islands.')" @mouseleave="hideTooltip" />
        </div>
        </div>

        <!-- Right: Visualization Container -->
        <div class="visualization-container">
          <!-- Top: Attribute Selector -->
          

          <!-- Chart Area -->
          <div class="chart-area">
        <div class="horizon-chart-container">
          <canvas ref="horizonChart" class="horizon-chart"></canvas>
              <div v-if="!compareIsland1 && !compareIsland2" class="chart-placeholder">
                <p>Select at least one island to view data</p>
              </div>
            </div>
              </div>
            </div>
            <div class="info-panel">
            <h3>Additional Infomation</h3>
              <div class="info-bubble">
                Want to understand what each indicator reveals about coral reef conditions?
              </div>

              <div class="info-bubble">
                Please scroll down to see the Coral Reef Health Indicators Analysis.
              </div>
              

              <img :src="imageUrls.hand" alt="Info" class="hand-icon" loading="lazy" @mouseleave="hideTooltip" />
          </div>
          </div>
          </div>
        </div>

        <!-- All Indicators Section -->
<div class="all-indicators-section">
  <div class="section-header">
    <h2 class="section-title">Coral Reef Health Indicators Analysis</h2>
    <p class="section-subtitle">Each indicator (e.g., live coral cover, disturbance, pollution) represents a proportion of the reef condition, and together they add up to 100%, showing how the reef area is distributed across these factors.</p>
  </div>

  <div class="indicator-grid">

    <!-- LCC -->
    <div class="indicator-panel">
      <div class="panel-header">
        <h3 class="panel-title">LCC (Live Coral Cover)</h3>
      </div>
      <div class="analysis-content">
        <p><span class="good">&gt;50%:</span> Healthy condition, indicating large live coral coverage area and stable ecosystem.</p>
        <p><span class="moderate">30–50%:</span> Moderate level, still has recovery potential but sensitive to external pressure.</p>
        <p><span class="poor">&lt;30%:</span> Severely degraded, may be replaced by algae, sand or rubble, declining ecosystem services.</p>
      </div>
      <div class="indicator-images">
        <div class="image-card">
          <img :src="imageUrls.lcc20" alt="LCC Poor Condition" loading="lazy" />
          <div class="percentage-overlay">&lt;30%</div>
        </div>
        <div class="image-card">
          <img :src="imageUrls.lcc100" alt="LCC Good Condition" loading="lazy" />
          <div class="percentage-overlay">&gt;50%</div>
        </div>
      </div>
    </div>

    <!-- AS -->
    <div class="indicator-panel">
      <div class="panel-header">
        <h3 class="panel-title">AS (Available Substrate)</h3>
      </div>
      <div class="analysis-content">
        <p><span class="good">&lt;10%:</span> Ideal state, most substrate covered by corals.</p>
        <p><span class="moderate">10–25%:</span> Warning zone, indicating recent coral death or fragmentation.</p>
        <p><span class="critical">&gt;40%:</span> Severely degraded area, indicating massive coral death, easily occupied by algae.</p>
      </div>
      <div class="indicator-images">
        <div class="image-card">
          <img :src="imageUrls.as80" alt="AS Poor Condition" loading="lazy" />
          <div class="percentage-overlay">&gt;40%</div>
        </div>
        <div class="image-card">
          <img :src="imageUrls.as10" alt="AS Good Condition" loading="lazy" />
          <div class="percentage-overlay">&lt;10%</div>
        </div>
      </div>
    </div>

    <!-- SD -->
    <div class="indicator-panel">
      <div class="panel-header">
        <h3 class="panel-title">SD (Sand/Soft Coral Coverage)</h3>
      </div>
      <div class="analysis-content">
        <p><span class="good">5–15%:</span> Appropriate amount, optimal state maintaining ecological balance.</p>
        <p><span class="poor">&gt;20%:</span> Excessive sand may bury hard corals and hinder larvae settlement.</p>
        <p><span class="poor">&lt;5%:</span> Too low, may indicate ecosystem imbalance, lacking necessary soft corals.</p>
      </div>
      <div class="indicator-images">
        <div class="image-card">
          <img :src="imageUrls.sd5" alt="SD Poor Condition" loading="lazy" />
          <div class="percentage-overlay">&lt;5%</div>
        </div>
        <div class="image-card">
          <img :src="imageUrls.sd50" alt="SD Good Condition" loading="lazy" />
          <div class="percentage-overlay">5–15%</div>
        </div>
      </div>
    </div>

    <!-- DI -->
    <div class="indicator-panel">
      <div class="panel-header">
        <h3 class="panel-title">DI (Disturbance Indicator)</h3>
      </div>
      <div class="analysis-content">
        <p><span class="good">&lt;5%:</span> Ideal state, almost no obvious damage.</p>
        <p><span class="moderate">5–10%:</span> Warning level, indicating recent typhoon, fishing or anchor damage.</p>
        <p><span class="poor">&gt;10%:</span> Obvious damage, ecosystem in vulnerable or degraded state.</p>
      </div>
      <div class="indicator-images">
        <div class="image-card">
          <img :src="imageUrls.di30" alt="DI Poor Condition" loading="lazy" />
          <div class="percentage-overlay">&gt;10%</div>
        </div>
        <div class="image-card">
          <img :src="imageUrls.di5" alt="DI Good Condition" loading="lazy" />
          <div class="percentage-overlay">&lt;5%</div>
        </div>
      </div>
    </div>

    <!-- PI -->
    <div class="indicator-panel">
      <div class="panel-header">
        <h3 class="panel-title">PI (Pollution Indicator)</h3>
      </div>
      <div class="analysis-content">
        <p><span class="good">&lt;2%:</span> Ideal, minimal external pollution impact.</p>
        <p><span class="moderate">2–5%:</span> Moderate level, indicating some pollution sources, needs attention.</p>
        <p><span class="poor">&gt;5%:</span> High pollution, may cause coral bleaching, increased disease and mortality.</p>
      </div>
      <div class="indicator-images">
        <div class="image-card">
          <img :src="imageUrls.pi50" alt="PI Poor Condition" loading="lazy" />
          <div class="percentage-overlay">&gt;5%</div>
        </div>
        <div class="image-card">
          <img :src="imageUrls.pi1" alt="PI Good Condition" loading="lazy" />
          <div class="percentage-overlay">&lt;2%</div>
        </div>
      </div>
    </div>

  </div>


</div>

             
        <!-- Tooltip -->
        <div class="tooltip" :style="tooltipStyle">
          {{ tooltipContent }}
        </div>

             
    </div>
    <footer class="main-footer">
      <div class="footer-content">
        © 2025 CoralKita
        <span class="footer-links">
          <a href="mailto:coralkita.service@gmail.com">Contact Us</a>
        </span>
      </div>
    </footer>
  </div>
</template>

<script>
import axios from 'axios';
import { Chart, registerables } from 'chart.js';
import { API_BASE_URL } from '@/config/config';
import ossService from '@/utils/ossService.js';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
// Import leaflet.markercluster plugin for clustering
import 'leaflet.markercluster';
import 'leaflet.markercluster/dist/MarkerCluster.css';
import 'leaflet.markercluster/dist/MarkerCluster.Default.css';

Chart.register(...registerables);

// Fix Leaflet default icon issue
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
  iconUrl: require('leaflet/dist/images/marker-icon.png'),
  shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
});

export default {
  name: 'TrendsVisualization',
  data() {
    return {
      backgroundLoaded: false, // 背景图片加载状态
      loadingProgress: 0, // 加载进度
      loadingText: 'Loading map data...', // 加载文本
      backgroundImageUrl: null, // 背景图片URL
      availableIslands: [],
      selectedIsland: '',
      // Combobox related data
      islandInputValue: '',
      showDropdown: false,
      filteredIslands: [],
      // State combobox related data
      stateInputValue: '',
      showStateDropdown: false,
      filteredStates: [],
      // New dynamic location selection data
      selectedLocationType: '', // 'state' or 'island'
      dynamicInputValue: '',
      showDynamicDropdown: false,
      filteredDynamicOptions: [],
      previousValue: null, // 用于存储之前的值
      currentLanguage: 'en',
      trendData: [],
      chart: null,
      currentIslandIndex: 0,
      // Leaflet map related data
      map: null,
      islandLocations: [],
      selectedCoordinates: null,
      markerLayers: [],
      selectedMarker: null,
      markerClusterGroup: null, // Marker cluster group
      // UI state data
      // New layout data
      selectedState: '',
      availableStates: [],
      stateIslandsData: [], // 存储州和岛屿的映射数据
      recommendedIslands: [],
      allIslandsLCCData: {}, // 存储所有岛屿的LCC数据
      allIslandsCoralData: {}, // 存储所有岛屿的珊瑚数据（bleach数据）
      // 加载状态
      isLoadingCoordinates: false,
      isLoadingCoralData: false,
      // Comparison function related data (simplified to 2 islands)
      compareIsland1: '',
      compareIsland2: '',
      selectedCompareAttribute: 'lcc', // Default selection Live Coral Cover
      
      // Indicator selector data
      selectedIndicator: 'lcc',
      // OSS图片URL
      imageUrls: {
        appIcon: '',
        crown1: '',
        crown2: '',
        crown3: '',
        crown4: '',
        crown5: '',
        coral: '',
        hand: '',
        lcc20: '',
        lcc100: '',
        as80: '',
        as10: '',
        sd5: '',
        sd50: '',
        di30: '',
        di5: '',
        pi50: '',
        pi1: ''
      },
  indicatorOptions: [
    { 
      value: 'lcc', 
      label: 'Live Coral Cover (LCC)', 
      tooltip: 'The percentage of the reef surface covered by living hard corals, serving as a key indicator of reef health and ecological resilience.' 
    },
    { 
      value: 'as', 
      label: 'Available Substrate (AS)', 
      tooltip: 'The proportion of reef area consisting of bare substrate (e.g., rock, rubble) available for new coral larvae settlement, reflecting the potential for reef recovery.' 
    },
    { 
      value: 'sd', 
      label: 'Sand (SD)', 
      tooltip: 'The percentage of the reef area covered by sand, which can indicate habitat stability but excessive sand coverage may limit coral regrowth.' 
    },
    { 
      value: 'di', 
      label: 'Disturbance Indicator (DI)', 
      tooltip: 'The presence of physical or biological damage such as coral breakage, disease, or bleaching, serving as a signal of recent stress events or ecosystem disturbance.' 
    },
    { 
      value: 'pi', 
      label: 'Pollution Indicator (PI)', 
      tooltip: 'The occurrence of pollution-related signs including debris, sedimentation, or algal overgrowth, which may reflect anthropogenic impacts and degraded water quality.' 
    }
  ],
      showIndicatorDropdown: false,
      showTravelDropdown: false,
      tooltipContent: '',
      tooltipStyle: { display: 'none' },
      horizonChart: null,
      compareData: {},
      // Chart data
      chartData: {
        datasets: []
      },
      // Loading state
      isLoading: false,
      loadingMessage: 'Loading...',
      // Canvas animation
      backgroundCanvas: null,
      backgroundCtx: null,
      particles: [],
      animationId: null
    };
  },
  
  computed: {
    // 获取当前选中指标的标签
    selectedIndicatorLabel() {
      const option = this.indicatorOptions.find(opt => opt.value === this.selectedIndicator);
      return option ? option.label : 'Choose Indicator';
    },
    
    // 获取低指标图片
    lowIndicatorImage() {
      const indicator = this.selectedCompareAttribute.toLowerCase();
      
      const imageMap = {
        'lcc': '/assets/LCC_20pct.png',
        'as': '/assets/AS_80pct.png',
        'sd': '/assets/SD_5pct.png',
        'di': '/assets/DI_30pct.png',
        'pi': '/assets/PI_50pct.png'
      };
      
      return imageMap[indicator] || imageMap['lcc'];
    },
    
    // 获取高指标图片
    highIndicatorImage() {
      const indicator = this.selectedCompareAttribute.toLowerCase();
      
      const imageMap = {
        'lcc': '/assets/LCC_100pct.png',
        'as': '/assets/AS_10pct.png',
        'sd': '/assets/SD_50pct.png',
        'di': '/assets/DI_5pct.png',
        'pi': '/assets/PI_1pct.png'
      };
      
      return imageMap[indicator] || imageMap['lcc'];
    },
  },
  
  methods: {
    // Carousel control methods
    nextSlide() {
      if (this.currentSlide < this.coralIndicators.length - 1) {
        this.currentSlide++;
      }
    },
    
    prevSlide() {
      if (this.currentSlide > 0) {
        this.currentSlide--;
      }
    },
    
    goToSlide(index) {
      this.currentSlide = index;
    }
  },
  
  async mounted() {
    console.log('Component mounted, starting initialization...');
    
    // 立即开始预加载背景图片
    this.preloadBackgroundImages();
    
    // 加载背景图片
    this.loadBackgroundImage();
    
    // 同时开始预加载图片到缓存
    this.preloadImageToCache();
    
    // Set global refresh detection timestamp for verification system
    localStorage.setItem('lastPageRefresh', Date.now().toString());
    
    // Load OSS images first
    await this.loadOssImages();
    
    // Execute initialization and data loading in sequence
    await this.$nextTick(() => {
      this.initializeLeafletMap();
      this.setupHorizonChart();
      this.initializeCanvasBackground();
    });
    
    // Load islands first
    await this.loadAvailableIslands();
    
    // Load states data
    await this.loadAvailableStates();
    
    // Load all islands coral data in batch
    await this.loadAllIslandsCoralData();
    
    // Then load coordinates
    await this.loadIslandCoordinates();
    
    // Finally load recommendations and update heat map
    await this.loadRecommendedIslands();
    
    // Test API connection
    this.testAPIConnection();
    
    // Debug specific island
    this.debugSpecificIsland('Tengah');
    
    // 添加全局函数供弹出窗口使用
    window.selectIslandFromPopup = (islandName) => {
      console.log('全局函数被调用，岛屿名称:', islandName);
      this.selectIslandFromPopup(islandName);
    };
    
    // 添加测试函数到全局
    window.testNavigation = () => {
      console.log('测试导航功能');
      this.$router.push('/education').then(() => {
        console.log('测试跳转成功');
      }).catch(err => {
        console.error('测试跳转失败:', err);
      });
    };
    
    // 添加窗口事件监听器
    window.addEventListener('resize', this.resizeCanvas);
    document.addEventListener('click', this.handleOutsideClick);
    

  },
  
  methods: {
    /**
     * 加载背景图片
     */
    async loadBackgroundImage() {
      try {
        this.backgroundImageUrl = await ossService.getFileUrl('bg_trend.webp')
        // 设置CSS变量
        document.documentElement.style.setProperty('--bg-image', `url(${this.backgroundImageUrl})`)
      } catch (error) {
        console.warn('加载背景图片失败，使用默认图片:', error)
        this.backgroundImageUrl = null
      }
    },

    /**
     * 预加载背景图片和预连接OSS服务
     */
    preloadBackgroundImages() {
      // 预连接OSS服务
      const preconnectLink = document.createElement('link');
      preconnectLink.rel = 'preconnect';
      preconnectLink.href = 'https://coralkita.oss-ap-southeast-3.aliyuncs.com';
      preconnectLink.crossOrigin = 'anonymous';
      
      // 添加到head中
      document.head.appendChild(preconnectLink);
      
      // 创建高优先级预加载链接元素
      const preloadLink = document.createElement('link');
      preloadLink.rel = 'preload';
      preloadLink.as = 'image';
      preloadLink.href = this.backgroundImageUrl;
      preloadLink.fetchPriority = 'high'; // 高优先级
      
      // 添加到head中
      document.head.appendChild(preloadLink);
      
      // 预加载图片到浏览器缓存
      this.preloadImageToCache();
      
      console.log('OSS preconnect and high-priority background image preload links added');
    },

    /**
     * 预加载图片到浏览器缓存
     */
    preloadImageToCache() {
      const img = new Image();
      img.src = this.backgroundImageUrl;
      
      // 模拟加载进度
      this.simulateLoadingProgress();
      
      // 设置超时机制，防止一直加载
      const timeout = setTimeout(() => {
        console.warn('Background image loading timeout');
        this.loadingProgress = 100;
        this.loadingText = 'Using backup data...';
        setTimeout(() => {
          this.backgroundLoaded = true;
        }, 500);
      }, 10000); // 10秒超时
      
      img.onload = () => {
        clearTimeout(timeout);
        console.log('Background image preloaded to cache');
        this.loadingProgress = 100;
        this.loadingText = 'Map data loaded successfully!';
        setTimeout(() => {
          this.backgroundLoaded = true;
        }, 500);
      };
      img.onerror = () => {
        clearTimeout(timeout);
        console.warn('Failed to preload background image');
        this.loadingProgress = 100;
        this.loadingText = 'Using backup data...';
        setTimeout(() => {
          this.backgroundLoaded = true; // 即使失败也隐藏占位符
        }, 500);
      };
    },

    /**
     * 模拟加载进度
     */
    simulateLoadingProgress() {
      const progressSteps = [
        { progress: 20, text: 'Connecting to map server...' },
        { progress: 40, text: 'Loading map data...' },
        { progress: 60, text: 'Locating island information...' },
        { progress: 80, text: 'Generating interactive map...' },
        { progress: 95, text: 'Almost ready...' }
      ];

      let currentStep = 0;
      const updateProgress = () => {
        if (currentStep < progressSteps.length) {
          const step = progressSteps[currentStep];
          this.loadingProgress = step.progress;
          this.loadingText = step.text;
          currentStep++;
          setTimeout(updateProgress, 800);
        }
      };

      updateProgress();
    },

    /**
     * 加载所有OSS图片URL
     */
    async loadOssImages() {
      try {
        console.log('开始加载OSS图片...')
        
        // 并行加载所有图片URL
        const [
          appIcon,
          crown1,
          crown2,
          crown3,
          crown4,
          crown5,
          coral,
          hand,
          lcc20,
          lcc100,
          as80,
          as10,
          sd5,
          sd50,
          di30,
          di5,
          pi50,
          pi1
        ] = await Promise.all([
          ossService.getSignedUrl('assets/icon.png'),
          ossService.getSignedUrl('assets/icons/icon_crown1.png'),
          ossService.getSignedUrl('assets/icons/icon_crown2.png'),
          ossService.getSignedUrl('assets/icons/icon_crown3.png'),
          ossService.getSignedUrl('assets/icons/icon_crown4.png'),
          ossService.getSignedUrl('assets/icons/icon_crown5.png'),
          ossService.getSignedUrl('assets/icons/coral.png'),
          ossService.getSignedUrl('assets/icons/hand.png'),
          ossService.getSignedUrl('assets/LCC_20pct.png'),
          ossService.getSignedUrl('assets/LCC_100pct.png'),
          ossService.getSignedUrl('assets/AS_80pct.png'),
          ossService.getSignedUrl('assets/AS_10pct.png'),
          ossService.getSignedUrl('assets/SD_5pct.png'),
          ossService.getSignedUrl('assets/SD_50pct.png'),
          ossService.getSignedUrl('assets/DI_30pct.png'),
          ossService.getSignedUrl('assets/DI_5pct.png'),
          ossService.getSignedUrl('assets/PI_50pct.png'),
          ossService.getSignedUrl('assets/PI_1pct.png')
        ])

        // 更新图片URL
        this.imageUrls = {
          appIcon,
          crown1,
          crown2,
          crown3,
          crown4,
          crown5,
          coral,
          hand,
          lcc20,
          lcc100,
          as80,
          as10,
          sd5,
          sd50,
          di30,
          di5,
          pi50,
          pi1
        }

        console.log('OSS图片加载完成:', this.imageUrls)
      } catch (error) {
        console.error('加载OSS图片失败:', error)
        // 如果OSS加载失败，使用本地图片作为备用
        this.loadFallbackImages()
      }
    },

    /**
     * 加载备用本地图片 - 已移除，完全使用CDN
     */
    loadFallbackImages() {
      console.log('CDN加载失败，但不使用本地图片备用')
      // 不再使用require()，完全依赖CDN
      // 如果CDN失败，图片将显示为空或加载失败状态
    },

    async loadAvailableIslands() {
      this.isLoading = true;
      this.loadingMessage = 'Loading islands...';
      
      try {
        console.log('正在从后端加载岛屿列表...');
        const response = await axios.get(`${API_BASE_URL}/trend/islands`);
        console.log('岛屿列表API响应:', response);
        
        if (response.data.code === 1) {
          this.availableIslands = response.data.data;
          this.filteredIslands = [...this.availableIslands]; // 初始化过滤列表
          console.log('成功从后端加载岛屿列表:', this.availableIslands);
          console.log('初始化过滤列表:', this.filteredIslands);
        } else {
          console.error('后端返回错误:', response.data.msg);
          this.availableIslands = [];
          this.filteredIslands = [];
        }
      } catch (error) {
        console.error('加载岛屿列表失败:', error);
        console.error('错误详情:', error.response || error.message);
        // 如果接口调用失败，显示错误信息
        this.availableIslands = [];
        this.filteredIslands = [];
        alert('无法连接到后端服务，请检查后端是否正在运行');
      } finally {
        this.isLoading = false;
      }
    },

    async loadAvailableStates() {
      console.log('开始加载可用州列表...');
      this.isLoading = true;
      this.loadingMessage = 'Loading available states...';
      
      try {
        const response = await axios.get(`${API_BASE_URL}/trend/metadata/latest`);
        console.log('州数据API响应:', response);
        
        if (response.data.code === 1) {
          this.stateIslandsData = response.data.data || [];
          // 提取所有唯一的州名
          const statesSet = new Set();
          this.stateIslandsData.forEach(item => {
            if (item.state) {
              statesSet.add(item.state);
            }
          });
          this.availableStates = Array.from(statesSet).sort();
          this.filteredStates = [...this.availableStates]; // 初始化过滤列表
          console.log('成功加载州列表:', this.availableStates);
          console.log('初始化过滤州列表:', this.filteredStates);
          console.log('州和岛屿数据总数:', this.stateIslandsData.length);
          console.log('州和岛屿数据示例:', this.stateIslandsData.slice(0, 3));
        } else {
          console.error('加载州列表失败:', response.data.msg);
          this.availableStates = [];
          this.stateIslandsData = [];
        }
      } catch (error) {
        console.error('加载州列表出错:', error);
        this.availableStates = [];
        this.filteredStates = [];
        this.stateIslandsData = [];
      } finally {
        this.isLoading = false;
      }
    },

    // 新的位置类型选择相关方法
    onLocationTypeChange() {
      console.log('位置类型改变为:', this.selectedLocationType);
      
      // 清空动态输入框的值
      this.dynamicInputValue = '';
      this.showDynamicDropdown = false;
      
      // 根据选择的类型更新可用选项
      this.updateDynamicOptions();
      
      // 清空之前的选择
      this.selectedIsland = '';
      this.selectedState = '';
      this.islandInputValue = '';
      this.stateInputValue = '';
    },

    updateDynamicOptions() {
      if (this.selectedLocationType === 'state') {
        this.filteredDynamicOptions = [...this.availableStates];
      } else if (this.selectedLocationType === 'island') {
        this.filteredDynamicOptions = [...this.availableIslands];
      } else {
        this.filteredDynamicOptions = [];
      }
      console.log('更新动态选项:', this.filteredDynamicOptions);
    },

    onDynamicInputChange() {
      console.log('动态输入框内容改变:', this.dynamicInputValue);
      this.filterDynamicOptions();
    },

    onDynamicInputFocus() {
      console.log('动态输入框获得焦点');
      this.showDynamicDropdown = true;
      // 当焦点进入时，如果已有选择，清空输入框让用户重新选择
      if (this.dynamicInputValue) {
        // 暂时清空输入框，显示所有选项
        const currentValue = this.dynamicInputValue;
        this.dynamicInputValue = '';
        this.showAllDynamicOptions();
        // 如果用户没有输入任何内容就失去焦点，恢复之前的值
        this.previousValue = currentValue;
      } else {
        this.showAllDynamicOptions();
      }
    },

    onDynamicInputBlur() {
      console.log('动态输入框失去焦点');
      setTimeout(() => {
        this.showDynamicDropdown = false;
        // 如果用户没有选择任何选项且输入框为空，恢复之前的值
        if (!this.dynamicInputValue && this.previousValue) {
          this.dynamicInputValue = this.previousValue;
          this.previousValue = null;
        }
      }, 200);
    },

    onDynamicKeyDown(event) {
      if (event.key === 'Escape') {
        this.showDynamicDropdown = false;
        this.$refs.dynamicInput.blur();
      }
    },

    toggleDynamicDropdown() {
      if (!this.selectedLocationType) return;
      
      this.showDynamicDropdown = !this.showDynamicDropdown;
      if (this.showDynamicDropdown) {
        this.$nextTick(() => {
          if (this.$refs.dynamicInput) {
            this.$refs.dynamicInput.focus();
          }
        });
        // 显示所有选项而不是过滤的选项
        this.showAllDynamicOptions();
      }
    },

    showAllDynamicOptions() {
      if (!this.selectedLocationType) {
        this.filteredDynamicOptions = [];
        return;
      }
      
      const sourceOptions = this.selectedLocationType === 'state' ? this.availableStates : this.availableIslands;
      this.filteredDynamicOptions = [...sourceOptions];
      console.log('显示所有动态选项:', this.filteredDynamicOptions);
    },

    filterDynamicOptions() {
      if (!this.selectedLocationType) {
        this.filteredDynamicOptions = [];
        return;
      }

      const searchTerm = this.dynamicInputValue.toLowerCase();
      const sourceOptions = this.selectedLocationType === 'state' ? this.availableStates : this.availableIslands;
      
      if (searchTerm === '') {
        this.filteredDynamicOptions = [...sourceOptions];
      } else {
        this.filteredDynamicOptions = sourceOptions.filter(option =>
          option.toLowerCase().includes(searchTerm)
        );
      }
      console.log('过滤后的动态选项:', this.filteredDynamicOptions);
    },

    selectFromDynamicCombobox(option) {
      console.log('从动态下拉框选择:', option, '类型:', this.selectedLocationType);
      
      this.dynamicInputValue = option;
      this.showDynamicDropdown = false;
      this.previousValue = null; // 清除之前的值，因为用户已经选择了新值
      
      if (this.selectedLocationType === 'state') {
        this.selectedState = option;
        this.stateInputValue = option;
        // 调用原有的州选择逻辑
        this.filterIslandsByState(option);
      } else if (this.selectedLocationType === 'island') {
        this.selectedIsland = option;
        this.islandInputValue = option;
        // 调用原有的岛屿选择逻辑
        this.onIslandChange();
      }
      
      // 选择后失去焦点，这样下次点击时会重新显示所有选项
      if (this.$refs.dynamicInput) {
        this.$refs.dynamicInput.blur();
      }
    },
    
    async loadTrendData(island) {
      if (!island) return;
      
      console.log('正在加载岛屿趋势数据:', island);
             const url = `${API_BASE_URL}/trend/query?island=${encodeURIComponent(island)}`;
      console.log('趋势数据API地址:', url);
      
      try {
        const response = await axios.get(url);
        console.log('趋势数据API响应:', response);
        
        if (response.data.code === 1) {
          // 处理日期格式，将数组格式转换为Date对象
          this.trendData = response.data.data.map(item => ({
            ...item,
            date: Array.isArray(item.date) ? new Date(item.date[0], item.date[1] - 1, item.date[2]) : item.date
          }));
          this.updateChart();
          console.log('成功加载趋势数据:', this.trendData);
        } else {
          console.error('加载趋势数据失败:', response.data.msg);
          this.trendData = [];
        }
      } catch (error) {
        console.error('加载趋势数据出错:', error);
        console.error('错误详情:', error.response || error.message);
        // 清空数据并显示错误
        this.trendData = [];
        alert(`无法加载岛屿 "${island}" 的数据，请检查后端服务是否正常运行`);
      }
    },

    // 批量加载所有岛屿的珊瑚数据（bleach数据）
    async loadAllIslandsCoralData() {
      this.isLoadingCoralData = true;
      try {
        console.log('开始批量加载所有岛屿的珊瑚数据...');
        const response = await axios.get(`${API_BASE_URL}/trend/bleach/all`);
        console.log('批量珊瑚数据响应:', response.data);
        
        if (response.data.code === 1) {
          this.allIslandsCoralData = response.data.data;
          console.log('成功加载所有岛屿的珊瑚数据:', Object.keys(this.allIslandsCoralData).length, '个岛屿');
        } else {
          console.error('批量加载珊瑚数据失败:', response.data.msg);
        }
      } catch (error) {
        console.error('批量加载珊瑚数据出错:', error);
      } finally {
        this.isLoadingCoralData = false;
      }
    },
    
    onIslandChange() {
      console.log('岛屿选择已更改为:', this.selectedIsland);
      if (this.selectedIsland) {
        // 自动更新州选择
        this.updateStateSelectionFromIsland(this.selectedIsland);
        
        // 通过地图选择函数来处理，确保地图同步
        this.selectIslandFromMap(this.selectedIsland);
        } else {
        // 如果取消选择岛屿，重置地图视图
        if (this.map) {
          this.map.setView([4.2105, 103.7751], 6); // 回到马来西亚中心视图
        }
        this.selectedCoordinates = null;
        
        // 清空输入框
        this.islandInputValue = '';
        this.filterIslands(); // 重新过滤以显示所有岛屿
        
        // 清空州选择
        this.selectedState = '';
        this.stateInputValue = '';
      }
    },

    // 根据选择的岛屿自动更新州选择
    updateStateSelectionFromIsland(islandName) {
      console.log('根据岛屿自动更新州选择:', islandName);
      
      // 获取岛屿对应的州
      const islandState = this.getIslandState(islandName);
      
      if (islandState && islandState !== 'Malaysia') {
        // 检查该州是否在可用州列表中
        if (this.availableStates.includes(islandState)) {
          console.log(`自动选择州: ${islandState}`);
          this.selectedState = islandState;
          this.stateInputValue = islandState; // 同时更新输入框的值
        } else {
          console.warn(`州 ${islandState} 不在可用州列表中:`, this.availableStates);
        }
      } else {
        console.log('未找到岛屿对应的州，保持当前州选择');
      }
    },

    // State combobox methods
    onStateInputFocus() {
      console.log('州输入框获得焦点');
      this.showStateDropdown = true;
      // 获得焦点时显示所有州，不进行过滤
      this.filteredStates = [...this.availableStates];
      console.log('焦点获得，显示所有州数量:', this.filteredStates.length);
    },

    onStateInputBlur() {
      console.log('州输入框失去焦点');
      // 延迟隐藏下拉框，给点击事件时间执行
      setTimeout(() => {
        this.showStateDropdown = false;
      }, 200);
    },

    onStateInputChange() {
      console.log('州输入变化:', this.stateInputValue);
      console.log('当前可用州数量:', this.availableStates.length);
      this.filterStates();
      this.showStateDropdown = true;
      console.log('输入变化后过滤结果数量:', this.filteredStates.length);
    },

    filterStates() {
      const input = this.stateInputValue.toLowerCase();
      console.log('过滤州输入:', input);
      console.log('可用州数量:', this.availableStates.length);
      
      if (input === '') {
        this.filteredStates = [...this.availableStates];
      } else {
        this.filteredStates = this.availableStates.filter(state => 
          state.toLowerCase().includes(input)
        );
      }
      console.log('过滤后的州数量:', this.filteredStates.length);
      console.log('过滤后的州:', this.filteredStates);
    },

    selectStateFromCombobox(state) {
      console.log('从组合框选择州:', state);
      
      // 更新输入框和选中状态
      this.stateInputValue = state;
      this.selectedState = state;
      
      // 立即隐藏下拉框
      this.showStateDropdown = false;
      
      // 移除输入框焦点
      if (this.$refs.stateInput) {
        this.$refs.stateInput.blur();
      }
      
      // 触发州选择变化
      this.onStateChange();
    },

    toggleStateDropdown() {
      console.log('切换州下拉框状态:', this.showStateDropdown, '->', !this.showStateDropdown);
      this.showStateDropdown = !this.showStateDropdown;
      if (this.showStateDropdown) {
        // 打开下拉框时，显示所有州（忽略当前输入框内容）
        this.filteredStates = [...this.availableStates];
        console.log('显示州下拉框，显示所有州数量:', this.filteredStates.length);
        console.log('可用州总数:', this.availableStates.length);
        
        // 确保输入框获得焦点
        this.$nextTick(() => {
          if (this.$refs.stateInput) {
            this.$refs.stateInput.focus();
          }
        });
      }
    },

    onStateKeyDown(event) {
      if (event.key === 'Enter') {
        // 如果有过滤结果，选择第一个
        if (this.filteredStates.length > 0) {
          this.selectStateFromCombobox(this.filteredStates[0]);
        }
      } else if (event.key === 'Escape') {
        this.showStateDropdown = false;
      }
    },
    
    // Combobox methods
    onInputFocus() {
      console.log('输入框获得焦点');
      this.showDropdown = true;
      // 获得焦点时显示所有岛屿，不进行过滤
      this.filteredIslands = [...this.availableIslands];
      console.log('焦点获得，显示所有岛屿数量:', this.filteredIslands.length);
    },

    onInputBlur() {
      console.log('输入框失去焦点');
      // 延迟隐藏下拉框，给点击事件时间执行
      setTimeout(() => {
        this.showDropdown = false;
      }, 200);
    },

    onInputChange() {
      console.log('输入变化:', this.islandInputValue);
      console.log('当前可用岛屿数量:', this.availableIslands.length);
      this.filterIslands();
      this.showDropdown = true;
      console.log('输入变化后过滤结果数量:', this.filteredIslands.length);
    },

    filterIslands() {
      const input = this.islandInputValue.toLowerCase();
      console.log('过滤输入:', input);
      console.log('可用岛屿数量:', this.availableIslands.length);
      
      if (input === '') {
        this.filteredIslands = [...this.availableIslands];
      } else {
        this.filteredIslands = this.availableIslands.filter(island => 
          island.toLowerCase().includes(input)
        );
      }
      console.log('过滤后的岛屿数量:', this.filteredIslands.length);
      console.log('过滤后的岛屿:', this.filteredIslands);
    },

    selectIslandFromCombobox(island) {
      console.log('从组合框选择岛屿:', island);
      
      // 更新输入框和选中状态
      this.islandInputValue = island;
      this.selectedIsland = island;
      
      // 立即隐藏下拉框
      this.showDropdown = false;
      
      // 移除输入框焦点
      if (this.$refs.islandInput) {
        this.$refs.islandInput.blur();
      }
      
      // 触发岛屿选择变化
      this.onIslandChange();
    },

    toggleDropdown() {
      console.log('切换下拉框状态:', this.showDropdown, '->', !this.showDropdown);
      this.showDropdown = !this.showDropdown;
      if (this.showDropdown) {
        // 打开下拉框时，显示所有岛屿（忽略当前输入框内容）
        this.filteredIslands = [...this.availableIslands];
        console.log('显示下拉框，显示所有岛屿数量:', this.filteredIslands.length);
        console.log('可用岛屿总数:', this.availableIslands.length);
        
        // 确保输入框获得焦点
        this.$nextTick(() => {
          if (this.$refs.islandInput) {
            this.$refs.islandInput.focus();
          }
        });
      }
    },


    onKeyDown(event) {
      if (event.key === 'Enter') {
        // 如果有过滤结果，选择第一个
        if (this.filteredIslands.length > 0) {
          this.selectIslandFromCombobox(this.filteredIslands[0]);
        }
      } else if (event.key === 'Escape') {
        this.showDropdown = false;
      }
    },

    
    

    
    selectIsland(island) {
      this.selectedIsland = island;
      this.currentIslandIndex = this.availableIslands.indexOf(island);
      this.loadTrendData(island);
    },
    
    previousIsland() {
      if (this.currentIslandIndex > 0) {
        this.currentIslandIndex--;
        this.selectIsland(this.availableIslands[this.currentIslandIndex]);
      }
    },
    
    nextIsland() {
      if (this.currentIslandIndex < this.availableIslands.length - 1) {
        this.currentIslandIndex++;
        this.selectIsland(this.availableIslands[this.currentIslandIndex]);
      }
    },

    // 初始化Leaflet地图
    initializeLeafletMap() {
      this.$nextTick(() => {
        if (this.map) {
          this.map.remove(); // 清理现有地图
        }
        
        // 创建地图实例，聚焦马来西亚
        this.map = L.map('leaflet-map', {
          center: [4.2105, 103.7751], // 马来西亚中心点
          zoom: 6,
          minZoom: 5,
          maxZoom: 12,
          zoomControl: true,
          attributionControl: true
        });
        
        // 添加美观的地图图层
        this.addMapLayers();
        
        // 添加健康程度图例
        this.addHealthLegend();
        
        console.log('Leaflet地图初始化完成');
      });
    },
    
    // 添加地图图层
    addMapLayers() {
      // 只使用一个基础地图图层
      const cartoLayer = L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png', {
        attribution: '© CARTO © OpenStreetMap contributors',
        maxZoom: 19
      });
      
      // 添加到地图
      cartoLayer.addTo(this.map);
    },

    // 添加健康程度图例
    addHealthLegend() {
      if (!this.map) return;
      
      // 创建图例控件
      const legend = L.control({ position: 'bottomleft' });
      
        legend.onAdd = () => {
          const div = L.DomUtil.create('div', 'map-legend');
          div.innerHTML = `
            <h4>Live Coral Cover</h4>
            <p>Average LCC (%)</p>
            <div><span style="background:#00b050"></span> Good</div>
            <div><span style="background:#ffc000"></span> Fair</div>
            <div><span style="background:#ff0000"></span> Poor</div>
          `;
        
        // 防止图例被拖拽
        L.DomEvent.disableClickPropagation(div);
        L.DomEvent.disableScrollPropagation(div);
        
        return div;
      };
      
      legend.addTo(this.map);
    },
    
    // 加载岛屿坐标数据
    async loadIslandCoordinates() {
      if (this.availableIslands.length === 0) {
        console.log('没有可用岛屿，跳过坐标加载');
        return;
      }
      
      this.isLoadingCoordinates = true;
      try {
        console.log('开始批量加载岛屿坐标数据，岛屿列表:', this.availableIslands);
        
        // 优先使用批量坐标API
        try {
          console.log('使用批量坐标API获取所有岛屿坐标...');
          const response = await axios.post(`${API_BASE_URL}/trend/bleach/coordinates`, this.availableIslands);
          console.log('批量坐标API响应:', response.data);
          
          if (response.data.code === 1) {
            const coordinatesMap = response.data.data;
            console.log('成功获取批量坐标数据:', Object.keys(coordinatesMap).length, '个岛屿');
            
            // 转换为数组格式
            this.islandLocations = Object.entries(coordinatesMap).map(([island, coords]) => ({
              island,
              lat: coords.lat,
              lng: coords.lng,
              hasData: coords.hasData
            }));
            
            console.log('批量加载的岛屿坐标:', this.islandLocations);
            
            // 在地图上添加标记
            if (this.islandLocations.length > 0) {
              this.updateClusterMap();
            } else {
              console.error('批量API没有返回任何坐标数据');
            }
            
            // 如果已经有选中的岛屿，立即定位
            if (this.selectedIsland) {
              console.log('检测到预选岛屿:', this.selectedIsland);
              setTimeout(() => {
                this.selectIslandFromMap(this.selectedIsland);
              }, 1000);
            }
            
            return; // 成功获取批量数据，直接返回
          } else {
            console.error('批量坐标API返回错误:', response.data.msg);
          }
        } catch (batchError) {
          console.error('批量坐标API请求失败:', batchError);
        }
        
        // 如果批量API失败，回退到使用缓存的批量珊瑚数据
        let allCoordinates = {};
        if (this.allIslandsCoralData && Object.keys(this.allIslandsCoralData).length > 0) {
          console.log('批量API失败，使用缓存的批量珊瑚数据获取坐标');
          for (const island of this.availableIslands) {
            if (this.allIslandsCoralData[island] && this.allIslandsCoralData[island].length > 0) {
              const coralData = this.allIslandsCoralData[island][0]; // 取第一条数据获取坐标
              console.log(`岛屿 ${island} 的坐标数据:`, coralData);
              
              if (coralData.islandLat && coralData.islandLng) {
                allCoordinates[island] = {
                  lat: coralData.islandLat,
                  lng: coralData.islandLng,
                  hasData: true
                };
                console.log(`岛屿 ${island} 坐标: ${coralData.islandLat}, ${coralData.islandLng}`);
              } else {
                console.warn(`岛屿 ${island} 没有坐标数据`);
              }
            } else {
              console.warn(`岛屿 ${island} 没有珊瑚数据`);
            }
          }
        } else {
          // 如果缓存数据也没有，回退到逐个请求
          console.log('没有批量数据，回退到逐个请求');
          for (const island of this.availableIslands) {
            try {
              console.log(`正在获取岛屿 ${island} 的坐标...`);
              const response = await axios.get(`${API_BASE_URL}/trend/bleach?island=${encodeURIComponent(island)}`);
              console.log(`岛屿 ${island} 的响应:`, response.data);
              
              if (response.data.code === 1 && response.data.data && response.data.data.length > 0) {
                const coralData = response.data.data[0]; // 取第一条数据获取坐标
                console.log(`岛屿 ${island} 的坐标数据:`, coralData);
                
                if (coralData.islandLat && coralData.islandLng) {
                  allCoordinates[island] = {
                    lat: coralData.islandLat,
                    lng: coralData.islandLng,
                    hasData: true
                  };
                  console.log(`岛屿 ${island} 坐标: ${coralData.islandLat}, ${coralData.islandLng}`);
                } else {
                  console.warn(`岛屿 ${island} 没有坐标数据`);
                }
              } else {
                console.warn(`岛屿 ${island} API返回错误:`, response.data.msg);
              }
            } catch (error) {
              console.error(`获取岛屿 ${island} 坐标失败:`, error);
            }
          }
        }
        
        // 转换为数组格式
        this.islandLocations = Object.entries(allCoordinates).map(([island, coords]) => ({
          island,
          lat: coords.lat,
          lng: coords.lng,
          hasData: coords.hasData
        }));
        
        console.log('最终加载的岛屿坐标:', this.islandLocations);
        
        // 在地图上添加标记
        if (this.islandLocations.length > 0) {
          this.updateClusterMap();
        } else {
          console.error('没有加载到任何岛屿坐标数据');
        }
        
        // 如果已经有选中的岛屿，立即定位
        if (this.selectedIsland) {
          console.log('检测到预选岛屿:', this.selectedIsland);
          setTimeout(() => {
            this.selectIslandFromMap(this.selectedIsland);
          }, 1000);
        }
        
      } catch (error) {
        console.error('加载岛屿坐标失败:', error);
      } finally {
        this.isLoadingCoordinates = false;
      }
    },
    
    // 在地图上添加岛屿标记
    addIslandMarkers() {
      if (!this.map) {
        console.error('地图未初始化');
        return;
      }
      
      if (this.islandLocations.length === 0) {
        console.error('没有岛屿位置数据');
        return;
      }
      
      console.log('开始添加岛屿标记，数据:', this.islandLocations);
      
      // 清除现有标记
      this.markerLayers.forEach(marker => this.map.removeLayer(marker));
      this.markerLayers = [];
      
      // 创建自定义图标
      const createCustomIcon = (hasData, isSelected = false, lccValue = 0) => {
        // 根据LCC值确定颜色（与图例保持一致）
        let color = '#6b7280'; // 默认灰色
        if (lccValue >= 0.7) color = '#0066cc'; // 蓝色 - Excellent
        else if (lccValue >= 0.5) color = '#00b050'; // 绿色 - Good
        else if (lccValue >= 0.3) color = '#ffc000'; // 橙色 - Fair
        else if (lccValue > 0) color = '#ff0000'; // 红色 - Poor
        
        if (isSelected) color = '#dc2626'; // 选中时显示红色
        
        const size = isSelected ? [35, 45] : [28, 38];
        const borderColor = isSelected ? '#fef2f2' : '#ffffff';
        const shadowColor = isSelected ? 'rgba(220, 38, 38, 0.5)' : 'rgba(0,0,0,0.3)';
        const animation = isSelected ? 'animation: bounce 2s infinite;' : '';
        
        return L.divIcon({
          className: 'custom-coral-marker',
          html: `
            <div style="
              background: ${color};
              width: ${size[0]}px;
              height: ${size[1]}px;
              border-radius: 50% 50% 50% 0;
              border: 3px solid ${borderColor};
              box-shadow: 0 4px 15px ${shadowColor};
              transform: rotate(-45deg);
              display: flex;
              align-items: center;
              justify-content: center;
              position: relative;
              ${animation}
              z-index: 1000;
            ">
              <div style="
                color: white;
                font-size: ${isSelected ? '14px' : '12px'};
                font-weight: bold;
                transform: rotate(45deg);
                text-shadow: 1px 1px 2px rgba(0,0,0,0.7);
              ">🪸</div>
            </div>
          `,
          iconSize: size,
          iconAnchor: [size[0]/2, size[1]],
          popupAnchor: [0, -size[1]]
        });
      };
      
      // 为每个岛屿添加标记
      this.islandLocations.forEach(location => {
        // 从完整的LCC数据中获取该岛屿的LCC值
        const islandLCCData = this.allIslandsLCCData[location.island];
        let lccValue = 0;
        if (islandLCCData && islandLCCData.lcc) {
          lccValue = parseFloat(islandLCCData.lcc) || 0;
        }
        console.log(`标记 ${location.island} 的LCC值:`, lccValue, '完整LCC数据:', islandLCCData);
        
        const marker = L.marker(
          [location.lat, location.lng],
          { 
            icon: createCustomIcon(location.hasData, location.island === this.selectedIsland, lccValue),
            title: location.island,
            zIndexOffset: 1000 // 确保标记在热力图之上
          }
        );
        
        // 创建弹出窗口内容
        const isCurrentlySelected = location.island === this.selectedIsland;
        const lccPercentage = lccValue ? (lccValue * 100).toFixed(1) : 'N/A';
        const popupContent = `
          <div style="text-align: center; font-family: Arial, sans-serif;">
            <h4 style="margin: 0 0 8px 0; color: ${isCurrentlySelected ? '#dc2626' : '#2563eb'}; font-size: 16px;">
              ${isCurrentlySelected ? '🔴 ' : ''}${location.island}
            </h4>
            <p style="margin: 0 0 8px 0; font-size: 14px; color: #64748b;">
              📍 ${location.lat.toFixed(4)}°N, ${location.lng.toFixed(4)}°E
            </p>
            <div style="
              padding: 4px 8px; 
              border-radius: 12px; 
              font-size: 12px; 
              font-weight: bold;
              background: ${location.hasData ? '#dcfce7' : '#f1f5f9'};
              color: ${location.hasData ? '#166534' : '#475569'};
              margin-bottom: 8px;
            ">
              ${location.hasData ? `✅ LCC: ${lccPercentage}%` : '❌ No Data'}
            </div>
            ${isCurrentlySelected ? 
              '<div style="background: #fee2e2; color: #dc2626; padding: 4px 8px; border-radius: 8px; font-size: 12px; font-weight: bold; margin-bottom: 8px;">🎯 Currently Selected</div>' : 
              this.getSelectButtonHtml(location.island)
            }
          </div>
        `;
        
        marker.bindPopup(popupContent, {
          maxWidth: 250,
          className: 'custom-popup'
        });
        
        // 点击事件
        marker.on('click', () => {
          this.selectIslandFromMap(location.island);
        });
        
        marker.addTo(this.map);
        this.markerLayers.push(marker);
      });
      
      // 全局函数已在mounted中定义，这里不需要重复定义
    },
    
    // 从地图选择岛屿
    selectIslandFromMap(island) {
      console.log('从地图选择岛屿:', island);
      this.selectedIsland = island;
      
      // 更新选中岛屿的坐标
      const location = this.islandLocations.find(loc => loc.island === island);
      if (location && this.map) {
        this.selectedCoordinates = {
          lat: location.lat,
          lng: location.lng
        };
        
        console.log(`定位到岛屿 ${island}:`, location.lat, location.lng);
        
        // 地图飞到选中位置并放大
        this.map.flyTo([location.lat, location.lng], 10, {
          duration: 2,
          easeLinearity: 0.3
        });
        
        // 延迟更新标记样式，确保地图动画开始后再更新
        setTimeout(() => {
          this.updateMarkerStyles(island);
        }, 100);
        
        // 打开选中岛屿的弹窗
        setTimeout(() => {
          this.openSelectedIslandPopup(island);
        }, 2000);
      } else if (!this.map) {
        console.warn('地图尚未初始化');
        // 如果地图还没初始化，保存选择状态，稍后处理
        this.selectedCoordinates = location ? {
          lat: location.lat,
          lng: location.lng
        } : null;
      }
      
      // 加载趋势数据
      this.loadTrendData(island);
      
      // 更新标记样式
      this.updateMarkerStyles(island);
    },
    
    // 更新标记样式（聚类地图版本）
    updateMarkerStyles(selectedIsland) {
      // 在聚类地图中，我们需要重新创建整个聚类组来更新标记样式
      if (this.markerClusterGroup && this.map) {
        // 重新创建聚类地图以反映选中状态
        this.updateClusterMap();
      }
    },
    
    // 打开选中岛屿的弹窗
    openSelectedIslandPopup(selectedIsland) {
      if (!this.map || !this.markerClusterGroup) return;
      
      // 在聚类组中查找对应的标记
      this.markerClusterGroup.eachLayer(layer => {
        if (layer.options && layer.options.islandName === selectedIsland) {
          // 如果标记在聚类中，先展开聚类
          this.markerClusterGroup.zoomToShowLayer(layer, () => {
            layer.openPopup();
          });
          return;
        }
      });
      
      // 如果在聚类组中没找到，尝试在markerLayers中查找
      const markerIndex = this.islandLocations.findIndex(loc => loc.island === selectedIsland);
      if (markerIndex >= 0 && this.markerLayers[markerIndex]) {
        const marker = this.markerLayers[markerIndex];
        marker.openPopup();
      }
    },
    
    compareIslands() {
      // 实现岛屿比较功能
      console.log('岛屿比较功能待实现');
    },

    // 返回首页
    goToHome() {
      // 可以使用Vue Router导航到首页
      // this.$router.push('/');
      // 或者直接跳转到根路径
      window.location.href = '/';
    },

    // 导航方法

    goToEducation() {
      console.log('导航到Education页面');
      // 主页面导航，不设置标记，应该显示验证
      this.$router.push('/education').catch(err => {
        // Ignore navigation duplicated error
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToGovernment() {
      console.log('导航到Government页面');
      this.$router.push('/government').catch(err => {
        // Ignore navigation duplicated error
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToAITools() {
      console.log('Navigate to AI Tools page');
      this.$router.push('/ai-tools').catch(err => {
        // Ignore navigation duplicated error
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    toggleLanguage() {
      this.currentLanguage = this.currentLanguage === 'en' ? 'zh' : 'en'
    },

    goToIslandDetail(islandName) {
      console.log('导航到岛屿详情页面:', islandName);
      // 立即关闭下拉菜单，但不阻止事件
      this.showTravelDropdown = false;
      
      // 使用nextTick确保状态更新完成
      this.$nextTick(() => {
        this.$router.push(`/travel/${encodeURIComponent(islandName)}`).catch(err => {
          // Ignore navigation duplicated error
          if (err.name !== 'NavigationDuplicated') {
            console.error('Navigation error:', err);
          }
        });
      });
    },

    // 对比功能方法
    openCompareModal() {
      console.log('打开对比弹窗');
      console.log('当前可用岛屿列表:', this.availableIslands);
      console.log('岛屿总数:', this.availableIslands.length);
      
      this.showCompareModal = true;
      this.selectedCompareIslands = ['', '', '', '']; // 重置为4个空选择
      this.selectedCompareAttribute = 'lcc'; // 默认选择Live Coral Cover
      
      // 等待DOM更新后初始化对比图表canvas
      this.$nextTick(() => {
        this.setupCompareChart();
      });
    },

    closeCompareModal() {
      console.log('关闭对比弹窗');
      this.showCompareModal = false;
      if (this.compareChart) {
        this.compareChart.destroy();
        this.compareChart = null;
      }
    },

    // 处理下拉框选择岛屿
    async onCompareIslandSelect(columnIndex, islandName) {
      console.log(`列 ${columnIndex + 1} 选择岛屿:`, islandName);
      
      // 更新选择的岛屿
      this.$set(this.selectedCompareIslands, columnIndex, islandName);
      
      // 获取实际选中的岛屿列表（过滤空值）
      const selectedIslands = this.selectedCompareIslands.filter(island => island !== '');
      console.log('当前选中的岛屿:', selectedIslands);
      
      if (selectedIslands.length > 0) {
        await this.loadCompareData();
        this.$nextTick(() => {
          this.updateCompareChart();
        });
      } else {
        // 清空图表
        if (this.compareChart) {
          this.compareChart.data.labels = [];
          this.compareChart.data.datasets = [];
          this.compareChart.options.plugins.title.text = 'Please select islands to compare';
          this.compareChart.update();
        }
      }
    },

    // 移除选中的岛屿
    async removeCompareIsland(columnIndex) {
      console.log(`移除列 ${columnIndex + 1} 的岛屿`);
      this.$set(this.selectedCompareIslands, columnIndex, '');
      
      // 获取实际选中的岛屿列表（过滤空值）
      const selectedIslands = this.selectedCompareIslands.filter(island => island !== '');
      console.log('移除后选中的岛屿:', selectedIslands);
      
      if (selectedIslands.length > 0) {
        await this.loadCompareData();
        this.$nextTick(() => {
          this.updateCompareChart();
        });
      } else {
        // 清空图表
        if (this.compareChart) {
          this.compareChart.data.labels = [];
          this.compareChart.data.datasets = [];
          this.compareChart.options.plugins.title.text = 'Please select islands to compare';
          this.compareChart.update();
        }
      }
    },

    // 监听岛屿选择变化（保留用于属性筛选变化）
    async onCompareIslandsChange() {
      const selectedIslands = this.selectedCompareIslands.filter(island => island !== '');
      console.log('选中的对比岛屿:', selectedIslands);
      
      if (selectedIslands.length > 0) {
        await this.loadCompareData();
        this.$nextTick(() => {
          this.updateCompareChart();
        });
      } else {
        // 清空图表
        if (this.compareChart) {
          this.compareChart.data.labels = [];
          this.compareChart.data.datasets = [];
          this.compareChart.options.plugins.title.text = 'Please select islands to compare';
          this.compareChart.update();
        }
      }
    },

    // 监听属性筛选变化
    onAttributeFilterChange() {
      console.log('选中的对比属性:', this.selectedCompareAttribute);
      const selectedIslands = this.selectedCompareIslands.filter(island => island !== '');
      if (selectedIslands.length > 0) {
        this.updateCompareChart();
      }
    },

    // 加载对比数据
    async loadCompareData() {
      console.log('加载对比数据...');
      this.compareData = {};
      
      // 获取实际选中的岛屿列表（过滤空值）
      const selectedIslands = this.selectedCompareIslands.filter(island => island !== '');
      
      if (selectedIslands.length === 0) {
        console.log('没有选中的岛屿');
        return;
      }
      
      try {
        // 使用后端的批量对比接口
        const response = await axios.post(`${API_BASE_URL}/trend/compare`, {
          islands: selectedIslands
        });
        
        console.log('对比数据响应:', response.data);
        
        if (response.data && response.data.code === 1 && response.data.data) {
          // 后端返回的是 TrendCompareVO 列表，按指标分组
          const compareResult = response.data.data;
          console.log('原始对比数据:', compareResult);
          
          // 初始化岛屿数据结构
          selectedIslands.forEach(island => {
            this.compareData[island] = {};
          });
          
          // 处理每个指标的数据
          compareResult.forEach(indicatorData => {
            const indicator = indicatorData.indicator.toLowerCase(); // 转换为小写匹配前端
            const islandData = indicatorData.islandData;
            const islandDates = indicatorData.islandDates;
            
            // 为每个岛屿处理该指标的数据
            Object.keys(islandData).forEach(island => {
              if (selectedIslands.includes(island)) {
                const values = islandData[island];
                const dates = islandDates[island];
                
                // 为每个时间点创建数据项
                values.forEach((value, index) => {
                  const dateStr = dates[index];
                  
                  // 如果该时间点的数据项不存在，创建它
                  if (!this.compareData[island][dateStr]) {
                    // 解析日期字符串 (格式: YYYY-MM-DD)
                    const dateParts = dateStr.split('-');
                    this.compareData[island][dateStr] = {
                      date: [parseInt(dateParts[0]), parseInt(dateParts[1]), parseInt(dateParts[2])],
                      lcc: 0, ot: 0, pi: 0, sd: 0, di: 0, as: 0
                    };
                  }
                  
                  // 设置该指标的值
                  this.compareData[island][dateStr][indicator] = value || 0;
                });
              }
            });
          });
          
          // 转换为数组格式
          Object.keys(this.compareData).forEach(island => {
            const dateData = this.compareData[island];
            this.compareData[island] = Object.values(dateData);
          });
          
          console.log('转换后的对比数据:', this.compareData);
        } else {
          console.warn('对比数据格式不正确:', response.data);
          // 如果批量接口失败，回退到单独获取
          await this.loadCompareDataFallback();
        }
      } catch (error) {
        console.error('批量加载对比数据失败:', error);
        // 如果批量接口失败，回退到单独获取
        await this.loadCompareDataFallback();
      }
    },

    // 回退方法：单独获取每个岛屿的数据
    async loadCompareDataFallback() {
      console.log('使用回退方法加载对比数据...');
      this.compareData = {};
      
      // 获取实际选中的岛屿列表（过滤空值）
      const selectedIslands = this.selectedCompareIslands.filter(island => island !== '');
      
      for (const island of selectedIslands) {
        try {
          const response = await axios.get(`${API_BASE_URL}/trend/query?island=${encodeURIComponent(island)}`);
          console.log(`${island} 数据响应:`, response.data);
          
          if (response.data && response.data.code === 1 && response.data.data) {
            this.compareData[island] = response.data.data;
          } else {
            console.warn(`${island} 数据格式不正确:`, response.data);
          }
        } catch (error) {
          console.error(`加载 ${island} 数据失败:`, error);
        }
      }
      
      console.log('回退方法获取的所有对比数据:', this.compareData);
    },

    // 初始化对比图表canvas
    setupCompareChart() {
      console.log('初始化对比图表canvas...');
      
      if (!this.$refs.compareChart) {
        console.warn('对比图表canvas引用未找到');
        return;
      }

      // 销毁现有图表
      if (this.compareChart) {
        this.compareChart.destroy();
        this.compareChart = null;
      }

      try {
        // 创建空的图表实例
        this.compareChart = new Chart(this.$refs.compareChart, {
          type: 'line',
          data: {
            labels: [],
            datasets: []
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
              title: {
                display: true,
                text: 'Please select islands to compare',
                font: { size: 16, weight: 'bold' }
              },
              legend: {
                display: true,
                position: 'top'
              },
              tooltip: {
                enabled: true,
                mode: 'index',
                intersect: false,
                backgroundColor: 'rgba(0, 0, 0, 0.9)',
                titleColor: '#fff',
                bodyColor: '#fff',
                borderColor: '#fff',
                borderWidth: 1,
                cornerRadius: 8,
                padding: 12,
                titleFont: {
                  size: 14,
                  weight: 'bold'
                },
                bodyFont: {
                  size: 13
                },
                callbacks: {
                  title: function(context) {
                    const label = context[0].label;
                    return `Time: ${label}`;
                  },
                  label: function(context) {
                    const value = (context.parsed.y * 100).toFixed(1);
                    return `${context.dataset.label}: ${value}%`;
                  }
                }
              }
            },
            scales: {
              x: {
                title: { display: true, text: 'Time' }
              },
              y: {
                beginAtZero: true,
                max: 1,
                title: {
                  display: true,
                  text: 'Percentage Content'
                },
                ticks: {
                  stepSize: 0.1,
                  callback: function(value) {
                    return (value * 100) + '%';
                  }
                }
              }
            }
          }
        });
        
        console.log('对比图表canvas初始化完成');
      } catch (error) {
        console.error('初始化对比图表失败:', error);
      }
    },

    // 更新对比图表
    updateCompareChart() {
      console.log('更新对比图表...');
      
      if (!this.compareChart) {
        console.warn('对比图表实例未初始化');
        this.setupCompareChart();
        return;
      }

      // 检查是否有数据
      if (!this.compareData || Object.keys(this.compareData).length === 0) {
        console.warn('没有对比数据可显示');
        return;
      }

      // 准备图表数据
      const datasets = [];
      const labels = [];
      
      // 获取所有时间点
      const allDates = new Set();
      Object.values(this.compareData).forEach(data => {
        if (Array.isArray(data)) {
          data.forEach(item => {
            if (item && item.date && Array.isArray(item.date)) {
              const dateStr = `${item.date[0]}`;
              allDates.add(dateStr);
            }
          });
        }
      });
      
      const sortedDates = Array.from(allDates).sort();
      console.log('所有时间点:', sortedDates);
      
      // 颜色配置
      const colors = [
        'rgba(99, 179, 237, 0.8)',
        'rgba(255, 99, 132, 0.8)', 
        'rgba(75, 192, 192, 0.8)',
        'rgba(255, 206, 86, 0.8)'
      ];

              // 属性配置
        const attributeConfig = {
          lcc: { label: 'LCC', description: 'Live Coral Cover - 活珊瑚覆盖率，反映珊瑚礁的健康状况和生态完整性' },
          as: { label: 'AS', description: 'Available Substrate - 可用基质，为珊瑚生长提供基础支撑的硬质表面' },
          sd: { label: 'SD', description: 'Sand - 沙质底质，影响珊瑚幼虫附着和生态系统稳定性' },
          di: { label: 'DI', description: 'Disturbance Indicator - 干扰指标，衡量人类活动对珊瑚礁的负面影响程度' },
          pi: { label: 'PI', description: 'Pollution Indicator - 污染指标，反映水质污染对珊瑚礁的威胁水平' },
          ot: { label: 'OT', description: 'Other - 其他成分，包括藻类、海绵等非珊瑚生物群落' }
        };

      // 显示单个属性，每个岛屿一条线
      Object.keys(this.compareData).forEach((island, index) => {
        const data = this.compareData[island];
        const values = sortedDates.map(date => {
          const item = data.find(d => `${d.date[0]}` === date);
          return item ? item[this.selectedCompareAttribute] : null;
        });

        datasets.push({
          label: island, // 只显示岛屿名称
          data: values,
          borderColor: colors[index % colors.length],
          backgroundColor: colors[index % colors.length].replace('0.8', '0.2'),
          fill: false,
          tension: 0.4
        });
      });

      // 更新图表数据
      this.compareChart.data.labels = sortedDates;
      this.compareChart.data.datasets = datasets;
      
      // 更新图表标题
      this.compareChart.options.plugins.title.text = `Island Comparison - ${attributeConfig[this.selectedCompareAttribute]?.label || ''}`;
      
      // 刷新图表
      this.compareChart.update();

      console.log('对比图表更新完成');
    },
    


    getIslandDescription(island) {
      // 基于岛屿名称生成描述，或者可以从后端获取
      return `${island} - 拥有丰富海洋生物多样性的美丽岛屿，珊瑚礁生态系统独特。`;
    },
    
    setupChart() {
      this.$nextTick(() => {
        const ctx = this.$refs.trendChart;
        console.log('设置图表，Canvas元素:', ctx);
        
        if (ctx) {
          // 如果已经有图表，先销毁
          if (this.chart) {
            this.chart.destroy();
            console.log('销毁旧图表');
          }
          
          console.log('创建新图表...');
          this.chart = new Chart(ctx, {
            type: 'line',
            data: {
              labels: [],
              datasets: []
            },
            options: {
              responsive: true,
              maintainAspectRatio: false,
              scales: {
                y: {
                  beginAtZero: true,
                  max: 1,
                  title: {
                    display: true,
                    text: 'Percentage Content'
                  },
                  ticks: {
                    stepSize: 0.1,
                    callback: function(value) {
                      return (value * 100) + '%';
                    }
                  }
                },
                x: {
                  title: {
                    display: true,
                    text: 'Time'
                  }
                }
              },
              interaction: {
                mode: 'nearest',
                axis: 'x',
                intersect: false
              },
              plugins: {
                legend: {
                  position: 'right',
                  onHover: function(e, legendItem, legend) {
                    const descriptions = {
                      'LCC': 'Live Coral Cover - Percentage of living coral coverage, reflects the health and ecological integrity of coral reefs',
                      'AS': 'Available Substrate - Hard surfaces that provide foundation support for coral growth',
                      'SD': 'Sand - Sandy substrate that affects coral larva attachment and ecosystem stability',
                      'DI': 'Disturbance Indicator - Measures the negative impact of human activities on coral reefs',
                      'PI': 'Pollution Indicator - Reflects the threat level of water pollution to coral reefs',
                      'OT': 'Other - Other components including algae, sponges and other non-coral biological communities'
                    };
                    
                    const label = legendItem.text;
                    if (descriptions[label]) {
                      // 创建或更新悬浮提示框
                      let tooltip = document.getElementById('legend-tooltip');
                      if (!tooltip) {
                        tooltip = document.createElement('div');
                        tooltip.id = 'legend-tooltip';
                        tooltip.style.cssText = `
                          position: fixed;
                          background: rgba(0, 0, 0, 0.9);
                          color: white;
                          padding: 16px;
                          border-radius: 10px;
                          font-size: 14px;
                          max-width: 350px;
                          z-index: 1000;
                          pointer-events: none;
                          box-shadow: 0 6px 20px rgba(0, 0, 0, 0.4);
                        `;
                        document.body.appendChild(tooltip);
                      }
                      
                      tooltip.innerHTML = `
                        <div style="font-weight: bold; margin-bottom: 10px; font-size: 16px;">${label}</div>
                        <div style="font-size: 13px; line-height: 1.5;">${descriptions[label]}</div>
                      `;
                      
                      // 右面中间显示
                      tooltip.style.right = '-10px';
                      tooltip.style.top = '50%';
                      tooltip.style.transform = 'translateY(-50%)';
                      tooltip.style.display = 'block';
                    }
                  },
                  onLeave: function(e, legendItem, legend) {
                    // 隐藏悬浮提示框
                    const tooltip = document.getElementById('legend-tooltip');
                    if (tooltip) {
                      tooltip.style.display = 'none';
                    }
                  }
                },
                title: {
                  display: true,
                  text: 'Coral Reef Trend Data Visualization'
                },
                tooltip: {
                  enabled: true,
                  mode: 'index',
                  intersect: false,
                  backgroundColor: 'rgba(0, 0, 0, 0.9)',
                  titleColor: '#fff',
                  bodyColor: '#fff',
                  borderColor: '#fff',
                  borderWidth: 1,
                  cornerRadius: 8,
                  padding: 12,
                  titleFont: {
                    size: 14,
                    weight: 'bold'
                  },
                  bodyFont: {
                    size: 13
                  },
                  callbacks: {
                    title: function(context) {
                      const label = context[0].label;
                      return `Time: ${label}`;
                    },
                    label: function(context) {
                      const value = (context.parsed.y * 100).toFixed(1);
                      return `${context.dataset.label}: ${value}%`;
                    }
                  }
                }
              }
            }
          });
          console.log('图表初始化完成');
        }
      });
    },
    
    updateChart() {
      if (!this.chart || this.trendData.length === 0) {
        console.log('图表更新失败: 图表未初始化或数据为空');
        return;
      }
      
      console.log('开始更新图表，数据:', this.trendData);
      
      // 准备数据 - 按时间排序（从旧到新）
      const sortedData = [...this.trendData].sort((a, b) => {
        const dateA = new Date(a.date[0], a.date[1] - 1, a.date[2]);
        const dateB = new Date(b.date[0], b.date[1] - 1, b.date[2]);
        return dateA - dateB;
      });
      
      const labels = sortedData.map(item => {
        const [year, month, day] = item.date;
        return `${year}`;
      });
      
              const datasets = [
          {
            label: 'LCC',
            data: sortedData.map(item => item.lcc || 0),
            borderColor: 'rgb(75, 192, 192)',
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            tension: 0.1,
            fill: false
          },
          {
            label: 'AS',
            data: sortedData.map(item => item.as || 0),
            borderColor: 'rgb(255, 159, 64)',
            backgroundColor: 'rgba(255, 159, 64, 0.2)',
            tension: 0.1,
            fill: false
          },
          {
            label: 'SD',
            data: sortedData.map(item => item.sd || 0),
            borderColor: 'rgb(255, 205, 86)',
            backgroundColor: 'rgba(255, 205, 86, 0.2)',
            tension: 0.1,
            fill: false
          },
          {
            label: 'DI',
            data: sortedData.map(item => item.di || 0),
            borderColor: 'rgb(153, 102, 255)',
            backgroundColor: 'rgba(153, 102, 255, 0.2)',
            tension: 0.1,
            fill: false
          },
          {
            label: 'PI',
            data: sortedData.map(item => item.pi || 0),
            borderColor: 'rgb(54, 162, 235)',
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            tension: 0.1,
            fill: false
          },
          {
            label: 'OT',
            data: sortedData.map(item => item.ot || 0),
            borderColor: 'rgb(255, 99, 132)',
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            tension: 0.1,
            fill: false
          }
        ];
      
      this.chart.data.labels = labels;
      this.chart.data.datasets = datasets;
      this.chartData.datasets = datasets;
      this.chart.update();
      
      console.log('图表更新完成');
    },
    
    // 计算平均值（数据是0-1的小数，需要转换为百分比）
    getAverageValue(data) {
      if (!data || data.length === 0) return 0;
      const sum = data.reduce((acc, val) => acc + val, 0);
      const average = sum / data.length;
      return (average * 100).toFixed(1);
    },
    
    // 根据指标类型和值获取颜色
    getValueColor(metricName, value) {
      const percentage = value * 100;
      
      switch (metricName) {
        case 'LCC': // 高=好
          if (percentage >= 50) return '#28a745'; // 绿
          if (percentage >= 25) return '#ffc107'; // 黄
          return '#dc3545'; // 红
          
        case 'AS': // 低=好
          if (percentage < 20) return '#28a745'; // 绿
          if (percentage <= 40) return '#ffc107'; // 黄
          return '#dc3545'; // 红
          
        case 'SD': // 适中最好
          if (percentage >= 5 && percentage <= 20) return '#28a745'; // 绿
          if ((percentage >= 20 && percentage <= 30) || (percentage >= 1 && percentage < 5)) return '#ffc107'; // 黄
          return '#dc3545'; // 红
          
        case 'DI': // 低=好
          if (percentage < 5) return '#28a745'; // 绿
          if (percentage <= 10) return '#ffc107'; // 黄
          return '#dc3545'; // 红
          
        case 'PI': // 低=好
          if (percentage < 3) return '#28a745'; // 绿
          if (percentage <= 8) return '#ffc107'; // 黄
          return '#dc3545'; // 红
          
        case 'OT': // 中性类别
          return '#6c757d'; // 灰色
          
        default:
          return '#333'; // 默认黑色
      }
    },

    // 新增方法：初始化热力图
    initializeLeafletHeatMap() {
      this.$nextTick(() => {
        if (this.map) {
          this.map.remove(); // 清理现有地图
        }
        
        // 创建地图实例，聚焦马来西亚
        this.map = L.map('leaflet-map', {
          center: [4.2105, 103.7751], // 马来西亚中心点
          zoom: 6,
          minZoom: 5,
          maxZoom: 12,
          zoomControl: true,
          attributionControl: true
        });
        
        // 添加地图图层
        this.addMapLayers();
        
        // 添加健康程度图例
        this.addHealthLegend();
        
        console.log('Leaflet热力图初始化完成');
      });
    },

    // 加载推荐岛屿列表（基于LCC指标）
    async loadRecommendedIslands() {
      if (this.availableIslands.length === 0) {
        console.log('没有可用岛屿，跳过推荐列表加载');
        return;
      }
      
      this.isLoading = true;
      this.loadingMessage = 'Loading recommended islands...';
      
      try {
        console.log('正在加载推荐岛屿列表...');
        
        // 使用批量API获取所有岛屿的趋势数据
        console.log('使用批量API获取所有岛屿趋势数据...');
        const response = await axios.get(`${API_BASE_URL}/trend/query/all`);
        console.log('批量趋势数据响应:', response.data);
        
        if (response.data.code === 1) {
          const allIslandsTrendData = response.data.data;
          console.log('成功获取所有岛屿趋势数据:', Object.keys(allIslandsTrendData).length, '个岛屿');
          
          // 处理批量数据，提取LCC信息
          const allIslandsData = {};
          Object.entries(allIslandsTrendData).forEach(([island, trendData]) => {
            if (trendData && trendData.length > 0) {
              // 只获取最新一年的LCC数据（数据已按日期倒序排列）
              const latestData = trendData[0]; // 第一条数据就是最新的
              const latestLCC = parseFloat(latestData.lcc) || 0;
              
              console.log(`${island} 最新数据项:`, latestData);
              console.log(`${island} 最新LCC值:`, latestLCC);
              
              if (latestLCC > 0) {
                allIslandsData[island] = {
                  name: island,
                  lcc: latestLCC,
                  dataCount: trendData.length,
                  latestDate: latestData.date
                };
                console.log(`${island} 最新LCC:`, latestLCC);
              } else {
                console.warn(`${island} 最新LCC数据无效:`, latestLCC);
              }
            } else {
              console.warn(`岛屿 ${island} 没有有效的趋势数据`);
            }
          });
          
          // 保存所有岛屿的LCC数据
          this.allIslandsLCCData = allIslandsData;
          console.log('所有岛屿LCC数据:', this.allIslandsLCCData);
        } else {
          console.error('批量加载趋势数据失败:', response.data.msg);
          this.recommendedIslands = [];
          return;
        }
        
        // 按LCC值排序，取前5个作为推荐
        this.recommendedIslands = Object.values(this.allIslandsLCCData)
          .filter(island => island.lcc > 0) // 只包含有数据的岛屿
          .sort((a, b) => b.lcc - a.lcc)
          .slice(0, 5);
        
        console.log('推荐岛屿列表:', this.recommendedIslands);
        
        // 更新热力图
        this.updateClusterMap();
        
      } catch (error) {
        console.error('加载推荐岛屿失败:', error);
        this.recommendedIslands = [];
      } finally {
        this.isLoading = false;
      }
    },

    // 更新聚类地图
    updateClusterMap() {
      if (!this.map || this.islandLocations.length === 0) return;
      
      // 移除现有聚类图层
      if (this.markerClusterGroup) {
        this.map.removeLayer(this.markerClusterGroup);
      }
      
      // 清除现有标记
      this.markerLayers.forEach(marker => this.map.removeLayer(marker));
      this.markerLayers = [];
      
      // 创建聚类组
      this.markerClusterGroup = L.markerClusterGroup({
        maxClusterRadius: 80,
        spiderfyOnMaxZoom: true,
        showCoverageOnHover: false,
        zoomToBoundsOnClick: true,
        disableClusteringAtZoom: null, // 确保在所有缩放级别都能聚类
        removeOutsideVisibleBounds: false, // 保持所有标记以便聚类
        animate: true, // 启用聚类动画
        animateAddingMarkers: true, // 启用添加标记的动画
        iconCreateFunction: (cluster) => {
          const childCount = cluster.getChildCount();
          
          // 计算聚类内岛屿的平均健康程度
          let totalLCC = 0;
          let validLCCCount = 0;
          
          cluster.getAllChildMarkers().forEach(marker => {
            const islandName = marker.options.islandName;
            if (islandName && this.allIslandsLCCData[islandName]) {
              const lccValue = parseFloat(this.allIslandsLCCData[islandName].lcc);
              if (!isNaN(lccValue)) {
                totalLCC += lccValue;
                validLCCCount++;
              }
            }
          });
          
          // 计算平均LCC值
          const averageLCC = validLCCCount > 0 ? totalLCC / validLCCCount : 0;
          
          // 根据平均LCC值确定颜色（与图例保持一致）
          let clusterColor = '#6B7280'; // 默认灰色
          let className = 'marker-cluster-small';
          
          if (averageLCC >= 0.7) {
            clusterColor = '#0066cc'; // 蓝色 - Excellent
          } else if (averageLCC >= 0.5) {
            clusterColor = '#00b050'; // 绿色 - Good
          } else if (averageLCC >= 0.3) {
            clusterColor = '#ffc000'; // 橙色 - Fair
          } else if (averageLCC > 0) {
            clusterColor = '#ff0000'; // 红色 - Poor
          }
          
          // 根据数量确定大小
          if (childCount > 10) {
            className = 'marker-cluster-large';
          } else if (childCount > 5) {
            className = 'marker-cluster-medium';
          }
          
          return L.divIcon({
            html: `<div style="background-color: ${clusterColor};"><span>${childCount}</span></div>`,
            className: 'marker-cluster ' + className,
            iconSize: new L.Point(40, 40)
          });
        }
      });
      
      // 为每个岛屿创建标记
      this.islandLocations.forEach(location => {
        // 获取该岛屿的LCC数据
        const islandLCCData = this.allIslandsLCCData[location.island];
        let lccValue = 'N/A';
        let markerColor = '#6B7280'; // 默认灰色
        
        if (islandLCCData && islandLCCData.lcc !== undefined) {
          lccValue = (islandLCCData.lcc * 100).toFixed(1) + '%';
          // 根据LCC值设置颜色（与图例保持一致）
          if (islandLCCData.lcc >= 0.7) {
            markerColor = '#0066cc'; // 蓝色 - Excellent
          } else if (islandLCCData.lcc >= 0.5) {
            markerColor = '#00b050'; // 绿色 - Good
          } else if (islandLCCData.lcc >= 0.3) {
            markerColor = '#ffc000'; // 橙色 - Fair
          } else {
            markerColor = '#ff0000'; // 红色 - Poor
          }
        }
        
        // 检查是否为选中的岛屿
        const isSelected = location.island === this.selectedIsland;
        const markerSize = isSelected ? 40 : 30;
        const fontSize = isSelected ? '14px' : '12px';
        const borderWidth = isSelected ? '3px' : '2px';
        const borderColor = isSelected ? '#fef2f2' : 'white';
        const boxShadow = isSelected ? 
          '0 4px 15px rgba(220, 38, 38, 0.5), 0 0 0 3px rgba(220, 38, 38, 0.3)' : 
          '0 2px 8px rgba(0,0,0,0.3)';
        const animation = isSelected ? 'animation: pulse 2s infinite;' : '';
        
        // 创建自定义数字标记
        const marker = L.marker([location.lat, location.lng], {
          islandName: location.island, // 添加岛屿名称到选项中
          icon: L.divIcon({
            className: 'custom-number-marker',
            html: `
              <div style="
                background-color: ${markerColor};
                color: white;
                width: ${markerSize}px;
                height: ${markerSize}px;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-weight: bold;
                font-size: ${fontSize};
                border: ${borderWidth} solid ${borderColor};
                box-shadow: ${boxShadow};
                cursor: pointer;
                transition: transform 0.2s ease;
                ${animation}
              ">
              </div>
            `,
            iconSize: [markerSize, markerSize],
            iconAnchor: [markerSize/2, markerSize/2]
          })
        });
        
        // 添加弹出窗口
        const islandState = this.getIslandState(location.island);
        const popupContent = `
          <div class="custom-popup-content">
            <h3 style="margin: 0 0 10px 0; color: #1f2937; font-size: 16px; font-weight: 600;">
              ${location.island}
            </h3>
            <div style="margin-bottom: 8px; color: #4b5563;">
              <span style="font-weight: 500;">🏛️ State:</span>
              ${islandState}
            </div>
            ${this.getSelectButtonHtml(location.island)}
          </div>
        `;
        
        
        marker.bindPopup(popupContent, {
          maxWidth: 250,
          className: 'custom-popup'
        });
        
        // 添加到聚类组
        this.markerClusterGroup.addLayer(marker);
        this.markerLayers.push(marker);
      });
      
      // 添加聚类组到地图
      this.map.addLayer(this.markerClusterGroup);
      
      console.log('聚类地图更新完成，岛屿数量:', this.islandLocations.length);
    },

    // 创建自定义热力效果
    createCustomHeatEffect(heatData) {
      // 创建连续的热力区域，不显示数字
      const groupedData = {};
      
      // 将数据按岛屿分组
      this.islandLocations.forEach(location => {
        const islandLCCData = this.allIslandsLCCData[location.island];
        let intensity = 0.3;
        
        if (islandLCCData && islandLCCData.lcc) {
          intensity = Math.max(0.3, Math.min(islandLCCData.lcc * 2.0, 1.0));
        }
        
        // 为每个岛屿创建热力区域
        let color = '#dc2626'; // 默认红色
        if (intensity >= 0.8) color = '#16a34a'; // 深绿色
        else if (intensity >= 0.6) color = '#22c55e'; // 绿色
        else if (intensity >= 0.4) color = '#84cc16'; // 浅绿色
        else if (intensity >= 0.3) color = '#fbbf24'; // 黄色
        else if (intensity >= 0.2) color = '#f97316'; // 橙色
        
        // 创建多个重叠的圆圈来模拟热力效果
        for (let i = 0; i < 3; i++) {
          const radius = 25 + (i * 15) + (intensity * 20);
          const opacity = (0.4 - i * 0.1) * intensity;
          
          const circle = L.circle([location.lat, location.lng], {
            radius: radius * 1000, // 转换为米
            fillColor: color,
            color: color,
            weight: 0,
            opacity: 0,
            fillOpacity: opacity
          }).addTo(this.map);
          
          this.markerLayers.push(circle);
        }
      });
    },

    // 根据LCC值获取颜色（与图例保持一致）
    getLCCColor(lccValue) {
      if (lccValue >= 70) return '#0066cc'; // 蓝色 - Excellent
      if (lccValue >= 50) return '#00b050'; // 绿色 - Good
      if (lccValue >= 30) return '#ffc000'; // 橙色 - Fair
      return '#ff0000'; // 红色 - Poor
    },

     
     // 获取低指标百分比标签
     getLowIndicatorPercentage() {
       const indicator = this.selectedCompareAttribute.toLowerCase();
       const percentages = {
         'lcc': '<30%',
         'as': '>40%',
         'sd': '<5%',
         'di': '>10%',
         'pi': '>5%'
       };
       return percentages[indicator] || '<30%';
     },
     
     // 获取高指标百分比标签
     getHighIndicatorPercentage() {
       const indicator = this.selectedCompareAttribute.toLowerCase();
       const percentages = {
         'lcc': '>50%',
         'as': '<10%',
         'sd': '5-15%',
         'di': '<5%',
         'pi': '<2%'
       };
       return percentages[indicator] || '>50%';
     },
     
     // 获取指标分析文本
     getIndicatorAnalysis() {
       const indicator = this.selectedCompareAttribute.toLowerCase();
       const analyses = {
         'lcc': `
           <div class="analysis-item">
             <strong>LCC (Live Coral Cover)</strong><br><br>
             <span class="good">&gt;50%:</span> Healthy condition, indicating large live coral coverage area and stable ecosystem.<br><br>
             <span class="moderate">30–50%:</span> Moderate level, still has recovery potential but sensitive to external pressure.<br><br>
             <span class="poor">&lt;30%:</span> Severely degraded, may be replaced by algae, sand or rubble, declining ecosystem services.
           </div>
         `,
         'as': `
           <div class="analysis-item">
             <strong>AS (Available Substrate)</strong><br><br>
             <span class="good">&lt;10%:</span> Ideal state, most substrate covered by corals.<br><br>
             <span class="moderate">10–25%:</span> Warning zone, indicating recent coral death or fragmentation.<br><br>
             <span class="critical">&gt;40%:</span> Severely degraded area, indicating massive coral death, easily occupied by algae.
           </div>
         `,
         'sd': `
           <div class="analysis-item">
             <strong>SD (Sand/Soft Coral Coverage)</strong><br><br>
             <span class="good">5–15%:</span> Appropriate amount, optimal state maintaining ecological balance.<br><br>
             <span class="poor">&gt;20%:</span> Excessive sand may bury hard corals and hinder larvae settlement.<br><br>
             <span class="poor">&lt;5%:</span> Too low, may indicate ecosystem imbalance, lacking necessary soft corals.
           </div>
         `,
         'di': `
           <div class="analysis-item">
             <strong>DI (Disturbance Indicator)</strong><br><br>
             <span class="good">&lt;5%:</span> Ideal state, almost no obvious damage.<br><br>
             <span class="moderate">5–10%:</span> Warning level, indicating recent typhoon, fishing or anchor damage.<br><br>
             <span class="poor">&gt;10%:</span> Obvious damage, ecosystem in vulnerable or degraded state.
           </div>
         `,
         'pi': `
           <div class="analysis-item">
             <strong>PI (Pollution Indicator)</strong><br><br>
             <span class="good">&lt;2%:</span> Ideal, minimal external pollution impact.<br><br>
             <span class="moderate">2–5%:</span> Moderate level, indicating some pollution sources, needs attention.<br><br>
             <span class="poor">&gt;5%:</span> High pollution, may cause coral bleaching, increased disease and mortality.
           </div>
         `
       };
       return analyses[indicator] || analyses['lcc'];
     },
     
     // 根据岛屿名称获取所属州
     getIslandState(islandName) {
      // 检查数据是否已加载
      if (!this.stateIslandsData || this.stateIslandsData.length === 0) {
        console.warn('州岛屿数据尚未加载，使用默认值');
        return 'Malaysia';
      }
      
      try {
        console.log(`正在查找岛屿 "${islandName}" 的州信息...`);
        console.log('可用的州岛屿数据数量:', this.stateIslandsData.length);
        
        // 从 stateIslandsData 中查找岛屿对应的州
        const islandData = this.stateIslandsData.find(item => {
          return item && item.island && item.island.toLowerCase() === islandName.toLowerCase();
        });
        
        if (islandData && islandData.state) {
          console.log(`精确匹配找到岛屿 ${islandName} 的州: ${islandData.state}`);
          return islandData.state;
        }
        
        // 如果没有找到精确匹配，尝试模糊匹配
        console.log(`精确匹配失败，尝试模糊匹配 "${islandName}"...`);
        const fuzzyMatch = this.stateIslandsData.find(item => {
          if (!item || !item.island) return false;
          const itemIsland = item.island.toLowerCase();
          const searchIsland = islandName.toLowerCase();
          const match = itemIsland.includes(searchIsland) || searchIsland.includes(itemIsland);
          if (match) {
            console.log(`模糊匹配候选: "${item.island}" -> "${item.state}"`);
          }
          return match;
        });
        
        if (fuzzyMatch && fuzzyMatch.state) {
          console.log(`通过模糊匹配找到岛屿 ${islandName} 的州: ${fuzzyMatch.state}`);
          return fuzzyMatch.state;
        }
        
        // 显示一些可能相关的岛屿数据用于调试
        const similarIslands = this.stateIslandsData.filter(item => {
          if (!item || !item.island) return false;
          return item.island.toLowerCase().includes('singa') || item.island.toLowerCase().includes('p ');
        });
        console.log('包含"singa"或"p "的岛屿:', similarIslands);
        
        console.warn(`未找到岛屿 ${islandName} 对应的州，使用默认值`);
        return 'Malaysia';
      } catch (error) {
        console.error(`获取岛屿 ${islandName} 州信息时出错:`, error);
        return 'Malaysia';
      }
    },

    // 州选择变化处理
    async onStateChange() {
      console.log('选择州:', this.selectedState);
      if (this.selectedState) {
        // 根据州筛选显示相关岛屿
        await this.filterIslandsByState(this.selectedState);
      } else {
        // 如果取消选择州，显示所有岛屿
        this.updateClusterMap();
      }
    },

    // 根据州筛选岛屿
    async filterIslandsByState(state) {
      console.log('根据州筛选岛屿:', state);
      console.log('完整的州岛屿数据:', this.stateIslandsData);
      
      // 从stateIslandsData中筛选出该州的所有岛屿
      const stateIslands = this.stateIslandsData.filter(item => item.state === state);
      const islandNames = stateIslands.map(item => item.island);
      
      console.log(`州 ${state} 相关岛屿:`, islandNames);
      console.log(`州 ${state} 岛屿详细数据:`, stateIslands);
      
      if (islandNames.length > 0) {
        // 计算该州所有岛屿的中心坐标
        await this.focusOnStateIslands(stateIslands);
        
        // 更新地图只显示该州的岛屿
        await this.updateStateClusterMap(stateIslands);
      } else {
        // 如果没找到相关岛屿，显示提示
        console.log(`没有找到州 ${state} 相关的岛屿`);
        console.log('所有可用的州:', [...new Set(this.stateIslandsData.map(item => item.state))]);
        alert(`没有找到州 ${state} 的岛屿数据`);
      }
    },

    // 聚焦到州的岛屿区域
    async focusOnStateIslands(stateIslands) {
      console.log('聚焦到州岛屿:', stateIslands);
      if (!this.map || stateIslands.length === 0) {
        console.error('地图未初始化或州岛屿数据为空:', {map: !!this.map, stateIslandsLength: stateIslands.length});
        return;
      }
      
      try {
        // 获取所有岛屿的坐标
        const islandNames = stateIslands.map(item => item.island);
        const response = await axios.post(`${API_BASE_URL}/trend/bleach/coordinates`, islandNames);
        
        if (response.data.code === 1) {
          const coordinatesData = response.data.data;
          const validCoordinates = [];
          
          // 收集有效的坐标
          for (const [islandName, data] of Object.entries(coordinatesData)) {
            if (data.hasData && data.lat && data.lng) {
              validCoordinates.push([data.lat, data.lng]);
            }
          }
          
          if (validCoordinates.length > 0) {
            // 计算边界并聚焦地图
            const group = new L.featureGroup(validCoordinates.map(coord => L.marker(coord)));
            this.map.fitBounds(group.getBounds(), {padding: [20, 20]});
            console.log(`聚焦到州的${validCoordinates.length}个岛屿`);
          }
        }
      } catch (error) {
        console.error('获取州岛屿坐标失败:', error);
      }
    },

    // 更新州的集群地图
    async updateStateClusterMap(stateIslands) {
      console.log('更新州集群地图:', stateIslands);
      if (!this.map) {
        console.error('地图未初始化');
        return;
      }
      
      // 重新创建集群组以避免状态问题
      if (this.markerClusterGroup) {
        this.map.removeLayer(this.markerClusterGroup);
        this.markerClusterGroup = null;
      }
      
      // 创建新的集群组（与updateClusterMap完全一致的配置）
      this.markerClusterGroup = L.markerClusterGroup({
        maxClusterRadius: 80,
        spiderfyOnMaxZoom: true,
        showCoverageOnHover: false,
        zoomToBoundsOnClick: true,
        disableClusteringAtZoom: null, // 确保在所有缩放级别都能聚类
        removeOutsideVisibleBounds: false, // 保持所有标记以便聚类
        animate: true, // 启用聚类动画
        animateAddingMarkers: true, // 启用添加标记的动画
        iconCreateFunction: (cluster) => {
          const childCount = cluster.getChildCount();
          
          // 计算聚类内岛屿的平均健康程度
          let totalLCC = 0;
          let validLCCCount = 0;
          
          cluster.getAllChildMarkers().forEach(marker => {
            const islandName = marker.options.islandName;
            if (islandName && this.allIslandsLCCData[islandName]) {
              const lccValue = parseFloat(this.allIslandsLCCData[islandName].lcc);
              if (!isNaN(lccValue)) {
                totalLCC += lccValue;
                validLCCCount++;
              }
            }
          });
          
          // 计算平均LCC值
          const averageLCC = validLCCCount > 0 ? totalLCC / validLCCCount : 0;
          
          // 根据平均LCC值确定颜色（与图例保持一致）
          let clusterColor = '#6B7280'; // 默认灰色
          let className = 'marker-cluster-small';
          
          if (averageLCC >= 0.7) {
            clusterColor = '#0066cc'; // 蓝色 - Excellent
          } else if (averageLCC >= 0.5) {
            clusterColor = '#00b050'; // 绿色 - Good
          } else if (averageLCC >= 0.3) {
            clusterColor = '#ffc000'; // 橙色 - Fair
          } else if (averageLCC > 0) {
            clusterColor = '#ff0000'; // 红色 - Poor
          }
          
          // 根据数量确定大小
          if (childCount > 10) {
            className = 'marker-cluster-large';
          } else if (childCount > 5) {
            className = 'marker-cluster-medium';
          }
          
          return L.divIcon({
            html: `<div style="background-color: ${clusterColor};"><span>${childCount}</span></div>`,
            className: 'marker-cluster ' + className,
            iconSize: new L.Point(40, 40)
          });
        }
      });
      
      console.log('创建新的集群组');
      
      // 收集所有有效的标记
      const validMarkers = [];
      
      // 为该州的岛屿添加标记 - 使用Promise.all等待所有异步操作
      const markerPromises = stateIslands.map(async (islandData) => {
        console.log(`正在处理岛屿: ${islandData.island}`);
        try {
          const response = await axios.post(`${API_BASE_URL}/trend/bleach/coordinates`, [islandData.island]);
          console.log(`岛屿 ${islandData.island} 坐标响应:`, response.data);
          
          if (response.data.code === 1) {
            const coordinatesData = response.data.data;
            const data = coordinatesData[islandData.island];
            console.log(`岛屿 ${islandData.island} 坐标数据:`, data);
            
            if (data && data.hasData && data.lat && data.lng) {
              // 验证坐标数据
              const lat = parseFloat(data.lat);
              const lng = parseFloat(data.lng);
              if (isNaN(lat) || isNaN(lng)) {
                console.warn(`岛屿 ${islandData.island} 坐标数据格式错误:`, {lat: data.lat, lng: data.lng});
                return null;
              }
              
              // 解析LCC值
              const lccValue = parseFloat(islandData.lcc.replace('%', '')) || 0;
              
              // 创建圆形自定义图标（与updateClusterMap保持一致）
              const customIcon = L.divIcon({
                html: `
                  <div style="
                    background-color: ${this.getLCCColor(lccValue)};
                    width: 40px;
                    height: 40px;
                    border-radius: 50%;
                    border: 3px solid white;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
                    font-weight: bold;
                    font-size: 14px;
                    color: white;
                    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
                  ">
                  </div>
                `,
                className: 'custom-div-icon',
                iconSize: [40, 40],
                iconAnchor: [20, 20]
              });
              
              // 创建标记，确保所有必要的属性都存在
              const marker = L.marker([lat, lng], {
                icon: customIcon,
                islandName: islandData.island,
                title: islandData.island
              });
              
              // 确保标记正确初始化
              if (!marker || !marker._latlng) {
                console.error(`标记创建失败: ${islandData.island}`);
                return null;
              }
              
              // 添加弹窗（与updateClusterMap保持一致的样式）
              const popupContent = `
                <div class="custom-popup-content">
                  <h3 style="margin: 0 0 10px 0; color: #1f2937; font-size: 16px; font-weight: 600;">
                    ${islandData.island}
                  </h3>
                  <div style="margin-bottom: 8px; color: #4b5563;">
                    <span style="font-weight: 500;">🏛️ State:</span><br>
                    ${islandData.state}
                  </div>
                  ${this.getSelectButtonHtml(islandData.island)}
                </div>
              `;
              
              marker.bindPopup(popupContent, {
                maxWidth: 250,
                className: 'custom-popup'
              });
              
              console.log(`成功创建岛屿标记: ${islandData.island} at [${lat}, ${lng}]`);
              return marker;
            } else {
              console.warn(`岛屿 ${islandData.island} 坐标数据无效:`, data);
            }
          } else {
            console.warn(`岛屿 ${islandData.island} 坐标API响应失败:`, response.data);
          }
        } catch (error) {
          console.error(`获取岛屿 ${islandData.island} 坐标失败:`, error);
        }
        return null;
      });
      
      // 等待所有标记创建完成
      const markers = await Promise.all(markerPromises);
      const validMarkersArray = markers.filter(marker => marker !== null);
      
      // 批量添加有效标记到集群组
      if (validMarkersArray.length > 0) {
        // 先将集群组添加到地图
        this.map.addLayer(this.markerClusterGroup);
        
        // 然后批量添加标记
        validMarkersArray.forEach(marker => {
          try {
            this.markerClusterGroup.addLayer(marker);
            validMarkers.push(marker);
          } catch (error) {
            console.error('添加标记到集群组失败:', error, marker);
          }
        });
        
        // 强制刷新聚类
        this.markerClusterGroup.refreshClusters();
        
        console.log(`州集群地图更新完成，成功显示${validMarkers.length}/${stateIslands.length}个岛屿`);
        console.log('集群组状态:', {
          hasLayers: this.markerClusterGroup.getLayers().length > 0,
          layerCount: this.markerClusterGroup.getLayers().length,
          onMap: this.map.hasLayer(this.markerClusterGroup)
        });
      } else {
        console.warn('没有有效的标记可以显示');
      }
    },

    // 更新地区热力图
    updateRegionalHeatMap(filteredIslands) {
      if (!this.map) return;
      
      // 移除现有热力图层
      if (this.heatLayer) {
        this.map.removeLayer(this.heatLayer);
      }
      
      // 清除现有标记
      this.markerLayers.forEach(marker => this.map.removeLayer(marker));
      this.markerLayers = [];
      
      // 只显示筛选出的岛屿
      const filteredLocations = this.islandLocations.filter(location => 
        filteredIslands.includes(location.island)
      );
      
      // 准备热力图数据
      const heatData = [];
      filteredLocations.forEach(location => {
        const recommendedIsland = this.recommendedIslands.find(r => r.name === location.island);
        let intensity = 0.1;
        
        if (recommendedIsland && recommendedIsland.lcc) {
          intensity = Math.min(recommendedIsland.lcc, 1.0);
        }
        
        heatData.push([location.lat, location.lng, intensity]);
      });
      
      // 创建热力图或自定义热力效果
      if (window.L && window.L.heatLayer) {
        this.heatLayer = L.heatLayer(heatData, {
          radius: 60,
          blur: 40,
          maxZoom: 17,
          max: 1.0,
          gradient: {
            0.0: '#dc2626',
            0.2: '#f97316',
            0.4: '#fbbf24',
            0.6: '#84cc16',
            0.8: '#22c55e',
            1.0: '#16a34a'
          }
        }).addTo(this.map);
      } else {
        this.createCustomHeatEffect(heatData);
      }
      
      // 添加岛屿标记（在热力图之上）
      filteredLocations.forEach(location => {
        const marker = L.marker([location.lat, location.lng], {
          title: location.island
        });
        
        marker.bindPopup(`
          <div style="text-align: center; font-family: Arial, sans-serif;">
            <h4 style="margin: 0 0 8px 0; color: #2563eb; font-size: 16px;">
              ${location.island}
            </h4>
            <p style="margin: 0 0 8px 0; font-size: 14px; color: #64748b;">
              📍 ${location.lat.toFixed(4)}°N, ${location.lng.toFixed(4)}°E
            </p>
            ${this.getSelectButtonHtml(location.island)}
          </div>
        `);
        
        marker.on('click', () => {
          this.selectIslandFromMap(location.island);
        });
        
        marker.addTo(this.map);
        this.markerLayers.push(marker);
      });
      
      // 调整地图视角以包含所有筛选的岛屿
      if (filteredLocations.length > 0) {
        const group = new L.featureGroup();
        filteredLocations.forEach(location => {
          L.marker([location.lat, location.lng]).addTo(group);
        });
        this.map.fitBounds(group.getBounds().pad(0.1));
        
        // 清除临时标记组
        this.map.removeLayer(group);
      }
    },

    // 选择推荐岛屿（排名前5的岛屿）
    selectRecommendedIsland(island) {
      console.log('选择推荐岛屿:', island.name);
      // 跳转到岛屿详情页面
      this.$router.push(`/travel/${encodeURIComponent(island.name)}`).catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    // 判断是否应该显示选择按钮
    shouldShowSelectButton(islandName) {
      // 检查岛屿的LCC数据
      const islandLCCData = this.allIslandsLCCData[islandName];
      
      // 如果是Poor指标（< 30%），显示按钮（跳转到Education）
      if (islandLCCData && islandLCCData.lcc !== undefined) {
        const lccValue = parseFloat(islandLCCData.lcc);
        if (lccValue < 0.3) {
          return true; // Poor状态，跳转到Education
        }
      }
      
      // 检查是否为推荐岛屿（排名前5）
      const isRecommended = this.recommendedIslands.some(island => island.name === islandName);
      if (isRecommended) {
        return true; // 推荐岛屿，跳转到详情页面
      }
      
      return false; // 其他情况不显示按钮
    },

    // 获取选择按钮的HTML
    getSelectButtonHtml(islandName) {
      if (this.shouldShowSelectButton(islandName)) {
        // 检查是否为红色岛屿（Poor状态，LCC < 30%）
        const islandLCCData = this.allIslandsLCCData[islandName];
        let buttonText = 'Select Island'; // 默认文本
        
        if (islandLCCData && islandLCCData.lcc !== undefined) {
          const lccValue = parseFloat(islandLCCData.lcc);
          if (lccValue < 0.3) {
            buttonText = 'Travel Responsibly'; // 红色岛屿显示此文本
          }
        }
        
        return `<button onclick="window.selectIslandFromPopup('${islandName}')" style="background: #3b82f6; color: white; border: none; padding: 6px 12px; border-radius: 6px; cursor: pointer; font-size: 12px; font-weight: bold;">${buttonText}</button>`;
      } else {
        return `<div style="color: #6b7280; font-size: 12px; font-style: italic; margin-top: 8px;">No detailed page available</div>`;
      }
    },

    // 从地图弹窗选择岛屿（简化版本）
    selectIslandFromPopup(islandName) {
      console.log('=== 点击岛屿调试信息 ===');
      console.log('点击岛屿:', islandName);
      console.log('当前路由:', this.$route.path);
      console.log('所有岛屿LCC数据:', this.allIslandsLCCData);
      console.log('推荐岛屿列表:', this.recommendedIslands);
      
      // 自动更新州选择
      this.updateStateSelectionFromIsland(islandName);
      
      // 检查岛屿的LCC数据
      const islandLCCData = this.allIslandsLCCData[islandName];
      console.log(`${islandName} 的LCC数据:`, islandLCCData);
      
      if (islandLCCData && islandLCCData.lcc !== undefined) {
        const lccValue = parseFloat(islandLCCData.lcc);
        console.log(`${islandName} LCC值: ${lccValue}`);
        
        // 如果是Poor指标（< 30%），直接跳转到Education界面
        if (lccValue < 0.3) {
          console.log(`${islandName} 是Poor指标岛屿，跳转到Education界面`);
          console.log('准备跳转到 /education');
          
          this.$router.push('/education').then(() => {
            console.log('成功跳转到Education界面');
          }).catch(err => {
            console.error('跳转到Education界面失败:', err);
            if (err.name !== 'NavigationDuplicated') {
              console.error('Navigation error:', err);
            }
          });
          return;
        } else {
          console.log(`${islandName} LCC值 ${lccValue} >= 0.3，不是Poor状态`);
        }
      } else {
        console.log(`${islandName} 没有LCC数据或数据无效`);
      }
      
      // 检查是否为推荐岛屿（排名前5）
      const isRecommended = this.recommendedIslands.some(island => island.name === islandName);
      console.log(`${islandName} 是否为推荐岛屿:`, isRecommended);
      
      if (isRecommended) {
        console.log(`${islandName} 是推荐岛屿，跳转到详情页面`);
        const targetPath = `/travel/${encodeURIComponent(islandName)}`;
        console.log('准备跳转到:', targetPath);
        
        this.$router.push(targetPath).then(() => {
          console.log('成功跳转到岛屿详情页面');
        }).catch(err => {
          console.error('跳转到岛屿详情页面失败:', err);
          if (err.name !== 'NavigationDuplicated') {
            console.error('Navigation error:', err);
          }
        });
      } else {
        console.log(`${islandName} 不是推荐岛屿，只在地图上定位`);
        this.selectIslandFromMap(islandName);
      }
      
      console.log('=== 调试信息结束 ===');
    },


    // 设置Horizon图表
    setupHorizonChart() {
      this.$nextTick(() => {
        const ctx = this.$refs.horizonChart;
        console.log('设置Horizon图表，Canvas元素:', ctx);
        
        if (ctx) {
          // 如果已经有图表，先销毁
          if (this.horizonChart) {
            this.horizonChart.destroy();
            console.log('销毁旧Horizon图表');
          }
          
          console.log('创建新Horizon图表...');
          this.horizonChart = new Chart(ctx, {
            type: 'line',
            data: {
              labels: [],
              datasets: []
            },
            options: {
              responsive: true,
              maintainAspectRatio: false,
              scales: {
                y: {
                  beginAtZero: true,
                  max: 1,
                  title: {
                    display: true,
                    text: 'Percentage Content'
                  },
                  ticks: {
                    stepSize: 0.1,
                    callback: function(value) {
                      return (value * 100) + '%';
                    }
                  }
                },
                x: {
                  title: {
                    display: true,
                    text: 'Time'
                  }
                }
              },
              plugins: {
                title: {
                  display: true,
                  text: 'Island Comparison - Horizon Chart'
                },
                legend: {
                  position: 'top'
                },
                tooltip: {
                  enabled: true,
                  mode: 'index',
                  intersect: false,
                  backgroundColor: 'rgba(0, 0, 0, 0.9)',
                  titleColor: '#fff',
                  bodyColor: '#fff',
                  borderColor: '#fff',
                  borderWidth: 1,
                  cornerRadius: 8,
                  padding: 12,
                  callbacks: {
                    title: function(context) {
                      const label = context[0].label;
                      return `Time: ${label}`;
                    },
                    label: function(context) {
                      const value = (context.parsed.y * 100).toFixed(1);
                      return `${context.dataset.label}: ${value}%`;
                    }
                  }
                }
              },
              elements: {
                line: {
                  tension: 0.4,
                  fill: true // 启用填充
                },
                point: {
                  radius: 4,
                  hoverRadius: 6
                }
              }
            }
          });
          console.log('Horizon图表初始化完成');
        }
      });
    },

    // 更新比较图表
    async updateComparisonChart() {
      if (!this.compareIsland1 && !this.compareIsland2) {
        console.log('需要至少选择一个岛屿');
        return;
      }
      
      console.log('更新比较图表:', this.compareIsland1, 'vs', this.compareIsland2);
      
      try {
        // 准备API请求
        const requests = [];
        const islands = [];
        
        if (this.compareIsland1) {
          requests.push(axios.get(`${API_BASE_URL}/trend/query?island=${encodeURIComponent(this.compareIsland1)}`));
          islands.push(this.compareIsland1);
        }
        
        if (this.compareIsland2) {
          requests.push(axios.get(`${API_BASE_URL}/trend/query?island=${encodeURIComponent(this.compareIsland2)}`));
          islands.push(this.compareIsland2);
        }
        
        // 加载岛屿数据
        const responses = await Promise.all(requests);
        const islandDataArray = responses.map((response, index) => ({
          island: islands[index],
          data: response.data.code === 1 ? response.data.data : []
        }));
        
        console.log('获取到的岛屿数据:', islandDataArray);
        console.log('当前选中的属性:', this.selectedCompareAttribute);
        
        // 检查是否有有效数据
        const validData = islandDataArray.filter(item => item.data.length > 0);
        if (validData.length === 0) {
          console.warn('没有岛屿有数据');
          return;
        }
        
        // 准备图表数据
        const allDates = new Set();
        validData.forEach(islandItem => {
          islandItem.data.forEach(item => {
            console.log('处理数据项:', item);
            if (item && item.date) {
              if (Array.isArray(item.date)) {
            allDates.add(`${item.date[0]}`);
                console.log('添加日期 (数组格式):', item.date[0]);
              } else {
                // 如果日期不是数组，直接使用
                allDates.add(`${item.date}`);
                console.log('添加日期 (字符串格式):', item.date);
              }
            }
          });
        });
        
        const sortedDates = Array.from(allDates).sort();
        console.log('所有排序后的日期:', sortedDates);
        
        // 创建数据集 - 动态创建，支持1-2个岛屿
        const datasets = [];
        const colors = [
          { border: 'rgba(99, 179, 237, 0.8)', background: 'rgba(99, 179, 237, 0.2)' },
          { border: 'rgba(255, 99, 132, 0.8)', background: 'rgba(255, 99, 132, 0.2)' }
        ];
        
        validData.forEach((islandItem, index) => {
          const dataset = {
            label: islandItem.island,
            data: sortedDates.map(date => {
              const item = islandItem.data.find(d => {
                const itemDate = Array.isArray(d.date) ? `${d.date[0]}` : `${d.date}`;
                return itemDate === date;
              });
              const value = item ? item[this.selectedCompareAttribute] : null;
              console.log(`${islandItem.island} - 日期 ${date}: ${value}`);
              return value;
            }),
            borderColor: colors[index].border,
            backgroundColor: colors[index].background,
            fill: index === 0 ? (validData.length > 1 ? '+1' : 'origin') : 'origin',
            tension: 0.4
          };
          datasets.push(dataset);
        });
        
        // 更新图表
        if (this.horizonChart) {
          console.log('更新图表数据:');
          console.log('- 标签:', sortedDates);
          console.log('- 数据集:', datasets);
          console.log('- 数据集数量:', datasets.length);
          
          this.horizonChart.data.labels = sortedDates;
          this.horizonChart.data.datasets = datasets;
          this.horizonChart.update();
          
          console.log('图表更新调用完成');
        } else {
          console.error('horizonChart 实例不存在！');
        }
        
        console.log('比较图表更新完成');
        
      } catch (error) {
        console.error('更新比较图表失败:', error);
      }
    },

    // Canvas背景动画方法
    initializeCanvasBackground() {
      this.$nextTick(() => {
        const canvas = this.$refs.backgroundCanvas;
        if (!canvas) return;
        
        this.backgroundCanvas = canvas;
        this.backgroundCtx = canvas.getContext('2d');
        
        this.resizeCanvas();
        this.createParticles();
        this.animateBackground();
        
        console.log('Canvas背景动画初始化完成');
      });
    },

    resizeCanvas() {
      if (!this.backgroundCanvas) return;
      
      this.backgroundCanvas.width = window.innerWidth;
      this.backgroundCanvas.height = window.innerHeight;
    },

    createParticles() {
      this.particles = [];
      const particleCount = 50;
      
      for (let i = 0; i < particleCount; i++) {
        this.particles.push({
          x: Math.random() * this.backgroundCanvas.width,
          y: Math.random() * this.backgroundCanvas.height,
          radius: Math.random() * 3 + 1,
          speedX: (Math.random() - 0.5) * 0.5,
          speedY: (Math.random() - 0.5) * 0.5,
          opacity: Math.random() * 0.5 + 0.1,
          color: `hsla(${Math.random() * 60 + 180}, 70%, 60%, ${Math.random() * 0.3 + 0.1})`
        });
      }
    },

    animateBackground() {
      if (!this.backgroundCtx || !this.backgroundCanvas) return;
      
      // 清空画布
      this.backgroundCtx.clearRect(0, 0, this.backgroundCanvas.width, this.backgroundCanvas.height);
      
      // 绘制渐变背景
      const gradient = this.backgroundCtx.createLinearGradient(0, 0, 0, this.backgroundCanvas.height);
      gradient.addColorStop(0, 'rgba(102, 126, 234, 0.1)');
      gradient.addColorStop(0.5, 'rgba(118, 75, 162, 0.05)');
      gradient.addColorStop(1, 'rgba(59, 130, 246, 0.1)');
      
      this.backgroundCtx.fillStyle = gradient;
      this.backgroundCtx.fillRect(0, 0, this.backgroundCanvas.width, this.backgroundCanvas.height);
      
      // 更新和绘制粒子
      this.particles.forEach(particle => {
        // 更新位置
        particle.x += particle.speedX;
        particle.y += particle.speedY;
        
        // 边界检测
        if (particle.x < 0 || particle.x > this.backgroundCanvas.width) {
          particle.speedX *= -1;
        }
        if (particle.y < 0 || particle.y > this.backgroundCanvas.height) {
          particle.speedY *= -1;
        }
        
        // 绘制粒子
        this.backgroundCtx.beginPath();
        this.backgroundCtx.arc(particle.x, particle.y, particle.radius, 0, Math.PI * 2);
        this.backgroundCtx.fillStyle = particle.color;
        this.backgroundCtx.fill();
      });
      
      // 绘制连接线
      this.drawConnections();
      
      // 继续动画
      this.animationId = requestAnimationFrame(() => this.animateBackground());
    },

    drawConnections() {
      if (!this.backgroundCtx) return;
      
      for (let i = 0; i < this.particles.length; i++) {
        for (let j = i + 1; j < this.particles.length; j++) {
          const particle1 = this.particles[i];
          const particle2 = this.particles[j];
          
          const distance = Math.sqrt(
            Math.pow(particle1.x - particle2.x, 2) + 
            Math.pow(particle1.y - particle2.y, 2)
          );
          
          if (distance < 100) {
            const opacity = (100 - distance) / 100 * 0.2;
            this.backgroundCtx.beginPath();
            this.backgroundCtx.moveTo(particle1.x, particle1.y);
            this.backgroundCtx.lineTo(particle2.x, particle2.y);
            this.backgroundCtx.strokeStyle = `rgba(59, 130, 246, ${opacity})`;
            this.backgroundCtx.lineWidth = 1;
            this.backgroundCtx.stroke();
          }
        }
      }
    },

    // 测试API连接
    async testAPIConnection() {
      console.log('=== API连接测试开始 ===');
      console.log('API_BASE_URL:', API_BASE_URL);
      
      // 测试岛屿列表API
      try {
        console.log('测试岛屿列表API...');
        const islandsResponse = await axios.get(`${API_BASE_URL}/trend/islands`);
        console.log('岛屿列表API响应:', islandsResponse.data);
      } catch (error) {
        console.error('岛屿列表API测试失败:', error);
      }
      
      // 测试具体岛屿的数据API（使用确认有数据的岛屿）
      const testIslands = ['Payar']; // 从您提供的API响应中确认有数据
      
      for (const island of testIslands) {
        try {
          console.log(`测试岛屿 ${island} 的bleach API...`);
          const bleachResponse = await axios.get(`${API_BASE_URL}/trend/bleach?island=${encodeURIComponent(island)}`);
          console.log(`岛屿 ${island} bleach API响应:`, bleachResponse.data);
          
          console.log(`测试岛屿 ${island} 的query API...`);
          const queryResponse = await axios.get(`${API_BASE_URL}/trend/query?island=${encodeURIComponent(island)}`);
          console.log(`岛屿 ${island} query API响应:`, queryResponse.data);
        } catch (error) {
          console.error(`岛屿 ${island} API测试失败:`, error);
        }
      }
      
      console.log('=== API连接测试结束 ===');
    },


    // 调试特定岛屿的完整数据流程
    async debugSpecificIsland(islandName) {
      console.log(`=== 调试岛屿 ${islandName} 开始 ===`);
      
      try {
        // 1. 检查岛屿是否在可用列表中
        console.log('1. 可用岛屿列表:', this.availableIslands);
        console.log(`${islandName} 是否在列表中:`, this.availableIslands.includes(islandName));
        
        // 2. 检查坐标数据
        const locationData = this.islandLocations.find(loc => loc.island === islandName);
        console.log(`2. ${islandName} 坐标数据:`, locationData);
        
        // 3. 检查推荐岛屿数据
        const recommendedData = this.recommendedIslands.find(r => r.name === islandName);
        console.log(`3. ${islandName} 推荐数据:`, recommendedData);
        
        // 4. 手动调用API获取趋势数据
        console.log(`4. 手动调用 ${islandName} 趋势数据API...`);
        const trendResponse = await axios.get(`${API_BASE_URL}/trend/query?island=${encodeURIComponent(islandName)}`);
        console.log(`${islandName} 趋势数据完整响应:`, trendResponse);
        console.log(`${islandName} 趋势数据内容:`, trendResponse.data);
        
        if (trendResponse.data.code === 1 && trendResponse.data.data) {
          const trendData = trendResponse.data.data;
          console.log(`${islandName} 趋势数据项数:`, trendData.length);
          
          trendData.forEach((item, index) => {
            console.log(`${islandName} 数据项 ${index + 1}:`, item);
            console.log(`  - 日期:`, item.date);
            console.log(`  - LCC:`, item.lcc);
            console.log(`  - LCC类型:`, typeof item.lcc);
          });
          
          // 获取最新一年的LCC（第一条数据，因为按日期倒序排列）
          const latestData = trendData[0];
          const latestLCC = parseFloat(latestData.lcc) || 0;
          
          console.log(`${islandName} 最新数据项:`, latestData);
          console.log(`${islandName} 最新LCC值:`, latestLCC);
          console.log(`${islandName} 最新LCC百分比:`, (latestLCC * 100).toFixed(1) + '%');
          
          if (latestLCC <= 0) {
            console.error(`${islandName} 最新LCC数据无效:`, latestLCC);
          }
        }
        
        // 5. 手动调用bleach API获取坐标数据
        console.log(`5. 手动调用 ${islandName} bleach数据API...`);
        const bleachResponse = await axios.get(`${API_BASE_URL}/trend/bleach?island=${encodeURIComponent(islandName)}`);
        console.log(`${islandName} bleach数据完整响应:`, bleachResponse);
        
        if (bleachResponse.data.code === 1 && bleachResponse.data.data) {
          const bleachData = bleachResponse.data.data;
          console.log(`${islandName} bleach数据项数:`, bleachData.length);
          
          if (bleachData.length > 0) {
            const firstItem = bleachData[0];
            console.log(`${islandName} bleach第一项:`, firstItem);
            console.log(`  - 坐标: ${firstItem.islandLat}, ${firstItem.islandLng}`);
          }
        }
        
      } catch (error) {
        console.error(`调试岛屿 ${islandName} 时出错:`, error);
      }
      
      console.log(`=== 调试岛屿 ${islandName} 结束 ===`);
    },

    // Indicator selector methods
    toggleIndicatorDropdown() {
      this.showIndicatorDropdown = !this.showIndicatorDropdown;
      if (!this.showIndicatorDropdown) {
        this.hideTooltip();
      }
    },

    selectIndicator(option) {
      this.selectedIndicator = option.value;
      this.selectedCompareAttribute = option.value;
      this.showIndicatorDropdown = false;
      this.hideTooltip();
      this.updateComparisonChart();
    },

    showTooltip(event, tooltip) {
      this.tooltipContent = tooltip;
      this.tooltipStyle = {
        display: 'block',
        left: (event.target.getBoundingClientRect().right + 14) + 'px',
        top: event.target.getBoundingClientRect().top + 'px'
      };
    },

    hideTooltip() {
      this.tooltipStyle = { display: 'none' };
    },

    // Handle clicks outside dropdown
    handleOutsideClick(event) {
      if (!event.target.closest('.indicator-dropdown')) {
        this.showIndicatorDropdown = false;
        this.hideTooltip();
      }
    }
  },

  watch: {
    selectedCompareAttribute: {
      handler: 'onAttributeFilterChange'
    }
  },
  
  beforeDestroy() {
    // 清理Leaflet地图实例
    if (this.map) {
      this.map.remove();
      this.map = null;
    }
    
    // 清理图表实例
    if (this.horizonChart) {
      this.horizonChart.destroy();
      this.horizonChart = null;
    }
    
    // 清理Canvas动画
    if (this.animationId) {
      cancelAnimationFrame(this.animationId);
      this.animationId = null;
    }
    
    // 移除事件监听器
    window.removeEventListener('resize', this.resizeCanvas);
    document.removeEventListener('click', this.handleOutsideClick);
    
    // 清理全局函数
    if (window.selectIslandFromPopup) {
      delete window.selectIslandFromPopup;
    }
    
    // 清理事件监听器
    window.removeEventListener('resize', this.resizeCanvas);
    

  }
};
</script>

<style scoped>
.trends-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #333;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  position: relative;
  overflow-x: hidden;
  overflow-y: visible; /* 确保下拉框可以显示 */
  display: flex;
  flex-direction: column;
  position: relative;            /* 正常文档流 */
  min-height: 100vh;
  overflow-y: auto;              /* 页面能滚 */
}

/* Canvas背景动画 */
.background-canvas {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  pointer-events: none;
}

.bg-container {
  position: relative;
  z-index: 0;
}

.bg-container::before {
  content: "";
  position: fixed;
  inset: 0;
  background: url('@/assets-webp/bg_trend.webp') no-repeat center center;
  background-size: cover;
  z-index: -1;
}

/* 海洋主题背景加载占位符样式 */
.bg-placeholder {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #0f4c75 0%, #3282b8 25%, #0f4c75 50%, #1e3a8a 75%, #0f4c75 100%);
  background-size: 400% 400%;
  animation: oceanWave 8s ease-in-out infinite;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  color: white;
  font-size: 18px;
  overflow: hidden;
}

.bg-placeholder::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="waves" x="0" y="0" width="100" height="100" patternUnits="userSpaceOnUse"><path d="M0,50 Q25,30 50,50 T100,50 L100,100 L0,100 Z" fill="rgba(255,255,255,0.1)"/></pattern></defs><rect width="100" height="100" fill="url(%23waves)"/></svg>');
  animation: waveMotion 6s ease-in-out infinite;
}



/* 海洋主题进度条样式 */
.progress-container {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1;
  width: 300px;
  text-align: center;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  overflow: hidden;
  margin-bottom: 15px;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.3);
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #00d4ff 0%, #00a8cc 50%, #0077be 100%);
  border-radius: 10px;
  transition: width 0.8s ease-in-out;
  position: relative;
  overflow: hidden;
}

.progress-fill::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.4), transparent);
  animation: shimmer 2s infinite;
}

.loading-text {
  color: white;
  font-size: 16px;
  font-weight: 500;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
  margin: 0;
  animation: textGlow 2s ease-in-out infinite alternate;
}

@keyframes shimmer {
  0% { left: -100%; }
  100% { left: 100%; }
}

@keyframes textGlow {
  0% { text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5); }
  100% { text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5), 0 0 10px rgba(0, 212, 255, 0.3); }
}

@keyframes oceanWave {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}

@keyframes waveMotion {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-10px) rotate(1deg); }
}

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-15px) rotate(5deg); }
}


/* 背景装饰元素 */
.background-decorations {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  z-index: 0;
}

.decoration-left,
.decoration-right {
  position: absolute;
  top: 0;
  bottom: 0;
  width: 200px;
}

.decoration-left {
  left: 0;
}

.decoration-right {
  right: 0;
}

/* 气泡装饰 */
.bubble {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.bubble-1 {
  width: 60px;
  height: 60px;
  top: 20%;
  left: 20px;
  animation-delay: 0s;
}

.bubble-2 {
  width: 40px;
  height: 40px;
  top: 50%;
  left: 50px;
  animation-delay: 2s;
}

.bubble-3 {
  width: 80px;
  height: 80px;
  top: 70%;
  left: 10px;
  animation-delay: 4s;
}

.bubble-4 {
  width: 50px;
  height: 50px;
  top: 25%;
  right: 30px;
  animation-delay: 1s;
}

.bubble-5 {
  width: 70px;
  height: 70px;
  top: 60%;
  right: 20px;
  animation-delay: 3s;
}

.bubble-6 {
  width: 35px;
  height: 35px;
  top: 80%;
  right: 60px;
  animation-delay: 5s;
}



@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-20px);
  }
}

/* 顶部导航 */
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

/* 导航项 */
.nav-items {
  display: flex;
  gap: 32px;
  align-items: center;
  flex-wrap: wrap;
}

.nav-item-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.map-rec-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  line-height: 1.1;
}

.nav-text-line {
  display: block;
}

.nav-item {
  padding: 8px 16px;
  color: rgba(255, 255, 255, 0.7);
  font-weight: 600;
  cursor: pointer;
  transition: color 0.3s ease;
  border-bottom: 2px solid transparent;
}

.nav-item:hover {
  color: rgba(255, 255, 255, 0.9);
}

.nav-item.active {
  color: white;
  border-bottom-color: #63b3ed;
}

/* Island 下拉菜单样式 */
.nav-item-dropdown {
  position: relative;
}

.dropdown-menu {
  position: absolute;
  top: calc(100% + 8px);
  left: 50%;
  transform: translateX(-50%);
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(25px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15), 0 4px 16px rgba(0, 0, 0, 0.1);
  min-width: 120px;
  max-width: 140px;
  z-index: 1000;
  overflow: hidden;
  animation: dropdownSlideIn 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 移除小箭头，使用图二样式 */

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
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.dropdown-item:last-child {
  border-bottom: none;
}

/* Language Toggle */
.language-toggle {
  margin-left: 16px;
}

.lang-btn {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: white;
  padding: 6px 12px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  font-size: 0.9rem;
  min-width: 40px;
}

.lang-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.5);
  transform: scale(1.05);
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


/* 对比弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.compare-modal {
  background: white;
  border-radius: 16px;
  width: 90%;
  max-width: 1200px;
  height: 80%;
  max-height: 800px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  display: flex;
  flex-direction: column;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #e5e7eb;
  background: #f9fafb;
}

.modal-header h3 {
  margin: 0;
  color: #374151;
  font-size: 24px;
  font-weight: 600;
}

.close-btn {
  background: #e5e7eb;
  color: #6b7280;
  border: none;
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.2s ease;
}

.close-btn:hover {
  background: #d1d5db;
  color: #374151;
}

.modal-content {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 岛屿选择区域 */
.choose-islands-section h4 {
  margin: 0 0 16px 0;
  color: #374151;
  font-size: 18px;
  font-weight: 600;
}

.islands-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  background: #f9fafb;
  padding: 20px;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
}

.island-column {
  background: white;
  border-radius: 8px;
  padding: 16px;
  border: 1px solid #e5e7eb;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.column-header h5 {
  margin: 0;
  color: #374151;
  font-size: 14px;
  font-weight: 600;
  text-align: center;
}

.island-dropdown {
  width: 100%;
}

.island-select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  background: white;
  color: #374151;
  font-size: 14px;
  cursor: pointer;
}

.island-select:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.island-select option:disabled {
  color: #9ca3af;
  background-color: #f3f4f6;
}

.selected-island-display {
  margin-top: 8px;
}

.island-tag {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #e0f2fe;
  border: 1px solid #0891b2;
  border-radius: 6px;
  padding: 6px 10px;
  font-size: 13px;
  color: #0c4a6e;
}

.island-tag span {
  font-weight: 500;
}

.remove-btn {
  background: none;
  border: none;
  color: #dc2626;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  padding: 0;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background-color 0.2s;
}

.remove-btn:hover {
  background-color: rgba(220, 38, 38, 0.1);
}

/* 属性筛选区域 */
.attribute-filter-section {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #f9fafb;
  padding: 16px;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
}

.attribute-filter-section label {
  color: #374151;
  font-weight: 600;
  white-space: nowrap;
}

.attribute-select {
  padding: 8px 12px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  background: white;
  color: #374151;
  font-size: 14px;
  min-width: 200px;
}

.attribute-select:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

/* 对比图表区域 */
.compare-chart-container {
  flex: 1;
  position: relative;
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 20px;
  min-height: 400px;
}

.compare-chart-container canvas {
  width: 100% !important;
  height: 100% !important;
}

.no-comparison-data {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  color: #6b7280;
  font-size: 16px;
  font-weight: 500;
}

/* 页面标题 */
.page-title {
  text-align: center;
  padding: 18px 0 40px 0;
  position: relative;
  z-index: 5;

}

.page-title h2 {
  margin: 0;
  font-size: 2rem;
  color: #ffffffff;
  font-weight: 600;
  text-shadow: 2px 2px 6px rgba(0, 0, 0, 0.66); 
}



/* 加载指示器 */
.loading-indicator {
  text-align: center;
  padding: 20px;
  position: relative;
  z-index: 5;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid rgba(255, 255, 255, 0.3);
  border-top: 4px solid #63b3ed;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 10px;
}

.loading-indicator p {
  color: white;
  margin: 0;
  font-size: 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 控制面板 */
.control-panel {
  padding: 12px 12px;
  display: flex;
  gap: 20px;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  position: relative;
  z-index: 1000; /* 提高层级确保下拉框能正常显示 */
}

.control-item {
  display: flex;
  align-items: center;
}

/* 合并搜索框样式 */
.combined-search-container {
  position: relative;
  display: flex;
  align-items: center;
  min-width: 280px;
  z-index: 1000; /* 确保搜索框容器有足够的层级 */
}

.combined-search-input {
  flex: 1;
  padding: 12px 45px 12px 16px;
  border: 1px solid #cbd5e0;
  border-radius: 10px;
  font-size: 14px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(5px);
  transition: all 0.3s ease;
  outline: none;
  color: #374151;
}

.combined-search-input:hover {
  border-color: #4f7ac0ff;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.15);
}

.combined-search-input:focus {
  border-color: #455e86ff;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
  background: white;
}

/* 当下拉框显示时，输入框的底部边框样式 */
.combined-search-container:has(.dropdown-list) .combined-search-input,
.combined-search-input:focus-within {
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
}

/* 提供向后兼容的样式（对于不支持:has的浏览器） */
.dropdown-active .combined-search-input {
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
  border-bottom-color: transparent;
}

.dropdown-toggle-btn {
  position: absolute;
  right: 2px;
  top: 50%;
  transform: translateY(-50%);
  width: 36px;
  height: 36px;
  border: none;
  background: rgba(59, 130, 246, 0.1);
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.dropdown-toggle-btn:hover {
  background: rgba(59, 130, 246, 0.2);
}

.dropdown-arrow {
  font-size: 12px;
  color: #395e9aff;
  transition: transform 0.3s ease;
}

.dropdown-arrow.rotated {
  transform: rotate(180deg);
}

/* 下拉列表样式 */
.dropdown-list {
  position: fixed; /* 使用fixed定位完全避免被容器clip */
  background: rgba(255, 255, 255, 0.98);
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  box-shadow: 0 12px 35px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(15px);
  z-index: 10000; /* 最高层级确保不被遮挡 */
  max-height: 300px;
  overflow-y: auto;
  /* 增强覆盖效果 */
  border-top-left-radius: 0;
  border-top-right-radius: 0;
  border-top: none;
}

.dropdown-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  cursor: pointer;
  transition: all 0.2s ease;
  border-bottom: 1px solid #f3f4f6;
}

.dropdown-item:last-child {
  border-bottom: none;
}

.dropdown-item:hover,
.dropdown-item.highlighted {
  background: rgba(59, 130, 246, 0.05);
  color: #1d4ed8;
}

.dropdown-item.selected {
  background: rgba(34, 197, 94, 0.1);
  color: #15803d;
  font-weight: 600;
}

.island-icon {
  margin-right: 8px;
  font-size: 16px;
}

.island-name {
  flex: 1;
  font-size: 14px;
  font-weight: 500;
}

.selected-badge {
  color: #16a34a;
  font-weight: bold;
  font-size: 16px;
}

.no-results {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  color: #6b7280;
  font-style: italic;
}

.no-results-icon {
  margin-right: 8px;
  font-size: 18px;
}

/* 滚动条样式 */
.dropdown-list::-webkit-scrollbar {
  width: 6px;
}

.dropdown-list::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 3px;
}

.dropdown-list::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}

.dropdown-list::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

.control-button {
  background: #357490ff;
  color: white;
  border: none;
  padding: 12px 16px;
  border-radius: 10px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: background-color 0.2s;
}

.control-button:hover {
  background: #214057ff;
  box-shadow: 0 4px 10px rgba(0,0,0,0.2);
  transform: translateY(-1px);
}

.control-button:active {
  transform: translateY(0) scale(0.98);
}

/* 新布局样式 */
.new-layout {
  display: flex;
  flex-direction: column;
  align-items: center; 
  gap: 25px;
  width: 100vw;
  margin: 0;
  padding: 0 1vw;
  position: relative;
  z-index: 5;
}

/* 地区选择样式已移到左侧面板 */

.state-select,
.region-select,
.island-select-dropdown {
  padding: 10px 15px;
  border: 2px solid #e5e7eb;
  border-radius: 8px;
  background: white;
  color: #374151;
  font-size: 14px;
  width: 100%;
  cursor: pointer;
  transition: all 0.3s ease;
}

.state-select:focus,
.region-select:focus,
.island-select-dropdown:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.island-select-dropdown option {
  padding: 8px;
  color: #374151;
}

.island-select-dropdown option:hover {
  background-color: #f3f4f6;
}

/* Combobox styles */
.combobox-container {
  position: relative;
  width: 100%;
}

.location-type-select {
  padding: 10px 40px 10px 15px;
  border: 2px solid #e5e7eb;
  border-radius: 8px;
  background: white;
  color: #374151;
  font-size: 14px;
  width: 100%;
  box-sizing: border-box;
  transition: all 0.3s ease;
  cursor: pointer;
  /* 隐藏默认的下拉箭头 */
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
}

.combobox-container {
  position: relative;
  width: 100%;
}

.location-type-select:focus {
  outline: none;
  border-color: #3b82f6;
  border-radius: 8px;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.location-type-select {
  padding: 12px 40px 12px 16px;   /* 调整内边距更舒适 */
  border: 1.5px solid #d1d5db;    /* 边框更轻巧 */
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.9); /* 半透明玻璃效果 */
  backdrop-filter: blur(6px);
  color: #374151;
  font-size: 14px;
  width: 100%;
  box-sizing: border-box;
  transition: all 0.3s ease;
  cursor: pointer;

  /* 隐藏默认箭头 */
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;

  /* 添加阴影让元素更立体 */
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

/* 悬停时 */
.location-type-select:hover {
  border-color: #3b82f6;
  box-shadow: 0 4px 10px rgba(59, 130, 246, 0.15);
  background: rgba(255, 255, 255, 1);
}

/* 聚焦时 */
.location-type-select:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.2);
  background: #fff;
}

/* 自定义箭头图标 */
.select-arrow {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #3b82f6;       /* 主题蓝色箭头 */
  pointer-events: none;
  font-size: 14px;
  transition: transform 0.2s ease;
}

/* 下拉时箭头旋转效果 */
.location-type-select:focus + .select-arrow {
  transform: translateY(-50%) rotate(180deg);
}


.island-input {
  padding: 10px 40px 10px 15px;
  border: 2px solid #e5e7eb;
  border-radius: 8px;
  background: white;
  color: #374151;
  font-size: 14px;
  width: 100%;
  box-sizing: border-box;
  transition: all 0.3s ease;
}

.island-input:disabled {
  background-color: #f3f4f6;
  color: #9ca3af;
  cursor: not-allowed;
}

.island-input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.dropdown-toggle-btn {
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #6b7280;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.dropdown-toggle-btn:disabled {
  color: #d1d5db;
  cursor: not-allowed;
}

.dropdown-toggle-btn:hover {
  background: #f3f4f6;
  color: #374151;
}

.dropdown-list {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 2px solid #e5e7eb;
  border-top: none;
  border-radius: 0 0 8px 8px;
  max-height: 200px;
  overflow-y: auto;
  z-index: 1000;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.dropdown-item {
  padding: 10px 15px;
  cursor: pointer;
  color: #374151;
  transition: background-color 0.2s ease;
  border-bottom: 1px solid #f3f4f6;
}

.dropdown-item:last-child {
  border-bottom: none;
}

.dropdown-item:hover {
  background-color: #f3f4f6;
}

.dropdown-item:active {
  background-color: #e5e7eb;
}

.dropdown-item.no-results {
  color: #9ca3af;
  font-style: italic;
  cursor: default;
}

.dropdown-item.no-results:hover {
  background-color: white;
}

/* 热力图区域 */
.heat-map-section {
  display: flex;
  justify-content: center;   /* 整体居中 */
  align-items: flex-start;   /* 顶部对齐 */
  gap: 30px;                 /* 左右间距 */
  margin: 0 auto;
  max-width: 1800px;         /* 限制总宽度，避免过宽 */
  align-items: stretch;
}

/* 选择面板 */
.select-panel {
  background: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(5px);
  border-radius: 12px;
  padding: 20px;                         /* 缩小 padding，更紧凑 */
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  width: 240px;                          /* 收窄宽度 */
  display: flex;
  flex-direction: column;
  gap: 20px;                             /* 控件之间间距 */
}

/* 面板标题 */
.select-panel h3 {
  margin: 18px;
  font-size: 22px;
  font-weight: 600;
  color: #ffffffff;
  text-align: center;
  padding-bottom: 8px;
  border-bottom: 2px solid rgba(255, 255, 255, 0.6);
}

/* 每组控件包裹卡片化 */
.control-group {
  background: rgba(255, 255, 255, 0.6);  
  border: 0px solid #ffffffff;
  border-radius: 10px;
  padding: 20px;
  transition: all 0.3s ease;
}

.control-group:hover {
  background: rgba(255, 255, 255, 0.97);
  border-color: #ffffffff;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.15);
}

/* 标签样式 */
.control-group label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
  font-size: 13px;
  color: #000000ff;
}

/* 热力图容器 */
.heat-map-container {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  max-width: 800px;
  width: 100%;
  margin: 0 0;
  margin-left: auto;
  margin-right: auto;
  flex: 1;                   /* 中间自适应 */
  display: flex;
  justify-content: center;
  align-items: center;
  min-width: 800px;          /* 最小宽度，避免太窄 */
  max-width: 1200px;          /* 最大宽度，保证不会无限拉伸 */
  height: 600px;
}

.map-instructions {

  margin-bottom: 10px;
}

.map-instructions p {
  color: #4b5058ff;

  margin: 0;
  font-style: italic;

}

/* 推荐面板 */
.recommendations-panel {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  padding: 12px 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  width: 240px;
  max-width: 240px;
  margin: 0 auto;
}

.recommendations-panel h3 {
  margin: 0 0 10px 0;
  color: #ffffffff;
  font-size: 22px;
  font-weight: 600;
  text-align: center;
  
}

.recommendation-subtitle {
  color: #ffffffff;
  font-size: 12px;
  text-align: center;
  margin: 0 0px 20px 0;
  font-style: italic;

}

.recommended-islands {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.recommendation-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.592);
  border: 2px solid #ffffff;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}




.recommendation-item:hover {
  background: rgba(255, 255, 255, 0.9);
  border-color: #ffffffff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.recommendation-rank {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  flex-shrink: 0;
}

.crown-icon {
  width: 32px;
  height: 32px;
  object-fit: contain;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

.rank-number {
  width: 24px;
  height: 24px;
  background: #3b82f6;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 12px;
}

.recommendation-content {
  flex: 1;
}

.recommendation-content h4 {
  margin: 0 0 4px 0;
  font-size: 14px;
  font-weight: 600;
  color: #374151;
}

.health-status {
  font-size: 12px;
  font-weight: 600;
  padding: 2px 6px;
  border-radius: 4px;
}

.health-status.healthy {
  background: #dcfce7;
  color: #166534;
}

.health-status.moderate {
  background: #fef3c7;
  color: #92400e;
}

.health-status.stressed {
  background: #fee2e2;
  color: #dc2626;
}

.location-state {
  font-size: 11px;
  font-weight: 500;
  color: #41598aff;
  padding: 2px 8px;
  background: rgba(59, 130, 246, 0.1);
  border-radius: 12px;
  display: inline-block;
  border: 1px solid rgba(59, 130, 246, 0.2);
}

.recommendation-arrow {
  color: #446ba8;
  font-weight: bold;
  flex-shrink: 0;
}

.location-note {
  text-align: center;
  margin-top: 15px;
}

.location-note p {
  color: #ffffffff;
  font-size: 12px;
  margin: 0;
  font-style: italic;
}



/* New Layout Container */
.comparison-layout-container {
  display: flex;
  justify-content: center;
  align-items: stretch;
  gap: 20px;
  margin: 30px auto 0;
  max-width: 1400px;
  width: 100%;
}

/* External Island Selection Panel - Left Side */
.external-island-selection-panel,.info-panel {
  flex: 1 1 240px; /* Fixed width */
  background: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(5px);
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

  display: flex;
  flex-direction: column;
  gap: 20px;

  min-height: 500px;
    max-width: 300px;     /* 最大宽度限制 */
  min-width: 180px;     /* 最小宽度限制 */
}

.external-island-selection-panel h3{
  margin: 0 0 10px 0;
  color: #374151;
  font-size: 18px;
  font-weight: 600;
  text-align: center;
}

.info-panel h3 {
  margin: 0 0 10px 0;
  color: #374151;
  font-size: 18px;
  font-weight: 600;
  text-align: center;
}



/* Visualization Container - Right Side (Narrower) */
.visualization-container {
  flex: 0 0 800px; /* Fixed width instead of flex: 1 to make it narrower */
  max-width: 800px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(5px);
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

  display: flex;
  flex-direction: column;
  gap: 20px;
  justify-content: center;
}

/* Attribute Selector Area */
.attribute-selector-area {
  display: flex;
  justify-content: center;
  padding-bottom: 10px;
  border-bottom: 1px solid #e5e7eb;
}

/* Chart Area */
.chart-area {
  flex: 1;
}

.chart-area .horizon-chart-container {
  background: rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  padding: 25px;
  min-height: 450px;
}

.islands-comparison h3 {
  margin: 0 0 20px 0;
  color: #374151;
  font-size: 20px;
  font-weight: 600;
  text-align: center;
}

.comparison-controls {
  display: flex;
  gap: 40px;
  margin-bottom: 30px;
  align-items: flex-start;
}

/* Indicator Selector Styles */
.indicator-selector {
  flex: 0 0 280px;
}

.indicator-selector label {
  display: block;
  margin-bottom: 8px;
  color: #374151;
  font-weight: 500;
  font-size: 14px;
}

.indicator-dropdown {
  position: relative;
  width: 100%;
}

.indicator-dropdown .selected {
  border: 1px solid #ccc;
  padding: 12px 16px;
  border-radius: 12px;
  cursor: pointer;
  background: white;
  color: #374151;
  font-size: 14px;
  transition: all 0.3s ease;
}

.indicator-dropdown .selected:hover {
  border-color: #3b82f6;
}

.indicator-dropdown .options {
  position: absolute;
  width: 100%;
  border: 1px solid #ccc;
  background: white;
  max-height: 200px;
  overflow-y: auto;
  z-index: 1000;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.indicator-dropdown .option {
  padding: 12px 16px;
  cursor: pointer;
  color: #374151;
  font-size: 14px;
  transition: background-color 0.2s ease;
}

.indicator-dropdown .option:hover {
  background: #f0f0f0;
}

/* Legacy Island Selection Styles - Removed for new layout */

.island-selector {
  width: 100%;
}

.island-selector label {
  display: block;
  margin-bottom: 8px;
  color: #374151;
  font-weight: 500;
  font-size: 14px;
}

.island-select {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  background: white;
  color: #374151;
  font-size: 14px;
  transition: all 0.3s ease;
}

.island-select:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

/* Tooltip Styles */
.tooltip {
  position: fixed;
  background: rgba(78, 99, 126, 0.9);
  color: #fff;
  padding: 8px 12px;
  border-radius: 6px;
  font-size: 12px;
  max-width: 250px;
  white-space: normal;
  line-height: 1.4;
  z-index: 2000;
  pointer-events: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.island-select:focus,
.attribute-select:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.island-select option:disabled {
  color: #9ca3af;
  background-color: #f3f4f6;
}

/* Comparison Section Layout */
.comparison-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 100%;
}

/* Attribute Selector Only - Top Section */
.attribute-selector-only {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

/* Chart and Selection Container */
.chart-and-selection {
  display: flex;
  gap: 20px;
  align-items: stretch;
}

/* Horizon图表容器 - Narrower */
.horizon-chart-container {
  position: relative;
  background: #f9fafb;
  border-radius: 12px;
  padding: 25px;
  min-height: 500px;
  border: 2px solid #e5e7eb;
  flex: 1.2; /* Takes less space, making it narrower */
}

.horizon-chart {
  width: 100% !important;
  height: 450px !important;
}

/* Island Selection Panel - Right Side */
.island-selection-panel {
  flex: 1; /* Takes 1/3 of available space */
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border: 2px solid #e5e7eb;
  display: flex;
  flex-direction: column;
  gap: 20px;
  min-width: 250px;
}

.island-selection-panel h3 {
  margin: 0 0 10px 0;
  color: #374151;
  font-size: 18px;
  font-weight: 600;
  text-align: center;
}

.horizon-chart-container .chart-placeholder {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  color: #6b7280;
  font-size: 16px;
  font-weight: 500;
}

/* 左侧面板 */
.left-panel {
  background: rgba(255, 255, 255, 0.2);
  padding: 24px;
  border-right: 1px solid rgba(226, 232, 240, 0.3);
}



/* 地图显示区域 */
.map-display {
  text-align: center;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.map-header {
  margin-bottom: 16px;
  padding: 16px;
  background: rgba(255, 255, 255, 0.35);
  border-radius: 12px;
  backdrop-filter: blur(8px);
  box-shadow: 0 2px 12px rgba(59, 130, 246, 0.08);
  width: 100%;
  max-width: 900px;    /* 保持宽度，不会自适应变窄 */
  min-width: 500px;    /* 可选：防止过窄 */
  margin-left: auto;
  margin-right: auto;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.map-header h3 {
  margin: 0 0 12px 0;
  color: #333;
  font-size: 18px;
  font-weight: 600;
}

.location-info {
  text-align: left;
}

.island-name {
  font-size: 16px;
  font-weight: 600;
  color: #2d3748;
  display: block;
  margin-bottom: 8px;
}

.coordinates {
  display: flex;
  flex-direction: row;
  align-items: baseline;
  gap: 4px;
  white-space: nowrap;
}

.coord-label {
  font-size: 12px;
  color: #718096;
  font-weight: 500;
  white-space: nowrap;
}

.coord-values {
  font-size: 14px;
  color: #4a5568;
  font-weight: 600;
  font-family: 'Courier New', monospace;
  white-space: nowrap;
}

/* Leaflet地图容器 */
.map-container {
  position: relative;
  flex: 1;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

/* 加载指示器样式 */
.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(5px);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  border-radius: 12px;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #e5e7eb;
  border-top: 4px solid #01A2EB;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 12px;
}

.loading-text {
  color: #374151;
  font-size: 14px;
  font-weight: 500;
  text-align: center;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

#leaflet-map {
  width: 100%;
  height: 100%;
  min-height: 0;
  border-radius: 10px;
}

/* 自定义Leaflet控件样式 */
.leaflet-control-layers {
  background: rgba(255, 255, 255, 0.95) !important;
  backdrop-filter: blur(10px) !important;
  border-radius: 8px !important;
  border: 1px solid rgba(0, 0, 0, 0.1) !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15) !important;
}

.leaflet-control-zoom {
  border: none !important;
  border-radius: 8px !important;
  overflow: hidden !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15) !important;
}

.leaflet-control-zoom a {
  background: rgba(255, 255, 255, 0.95) !important;
  backdrop-filter: blur(10px) !important;
  border: none !important;
  color: #374151 !important;
  font-weight: bold !important;
  transition: all 0.3s ease !important;
}

.leaflet-control-zoom a:hover {
  background: rgba(59, 130, 246, 0.9) !important;
  color: white !important;
  transform: scale(1.05) !important;
}

/* 自定义弹出窗口样式 */
.leaflet-popup-content-wrapper {
  background: white !important;
  border-radius: 12px !important;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2) !important;
  border: 1px solid #e2e8f0 !important;
}

.leaflet-popup-tip {
  background: white !important;
  border: 1px solid #e2e8f0 !important;
}

.custom-popup .leaflet-popup-content {
  margin: 16px !important;
  min-width: 200px !important;
}

/* 自定义标记动画 */
.custom-coral-marker {
  transition: all 0.3s ease;
}

.custom-coral-marker:hover {
  transform: scale(1.1) !important;
  z-index: 1000 !important;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0) rotate(-45deg);
  }
  40% {
    transform: translateY(-10px) rotate(-45deg);
  }
  60% {
    transform: translateY(-5px) rotate(-45deg);
  }
}

/* 地图加载状态 */
.leaflet-container {
  background: linear-gradient(135deg, #e0f2fe 0%, #b3e5fc 100%) !important;
}

.leaflet-tile-loaded {
  transition: opacity 0.3s ease !important;
}

/* 地图归属信息样式优化 */
.leaflet-control-attribution {
  background: rgba(255, 255, 255, 0.8) !important;
  backdrop-filter: blur(5px) !important;
  border-radius: 4px !important;
  font-size: 10px !important;
  color: #6b7280 !important;
}

/* 聚类地图样式 - 完全移除所有默认颜色和光晕效果 */
.marker-cluster,
.marker-cluster-small,
.marker-cluster-medium,
.marker-cluster-large {
  background-color: transparent !important;
  border: none !important;
  box-shadow: none !important;
}

.marker-cluster div,
.marker-cluster-small div,
.marker-cluster-medium div,
.marker-cluster-large div {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid white !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3) !important;
  background-color: transparent !important;
}

.marker-cluster-medium div {
  width: 40px;
  height: 40px;
}

.marker-cluster-large div {
  width: 50px;
  height: 50px;
}

/* 覆盖Leaflet聚类库的所有默认样式 */
.leaflet-marker-icon.marker-cluster,
.leaflet-marker-icon.marker-cluster-small,
.leaflet-marker-icon.marker-cluster-medium,
.leaflet-marker-icon.marker-cluster-large {
  background: transparent !important;
  border: none !important;
  box-shadow: none !important;
}

/* 强制覆盖外部CSS文件的所有默认样式 */
.marker-cluster,
.marker-cluster-small,
.marker-cluster-medium,
.marker-cluster-large,
div.marker-cluster,
div.marker-cluster-small,
div.marker-cluster-medium,
div.marker-cluster-large {
  background-color: transparent !important;
  background: transparent !important;
  border: none !important;
  box-shadow: none !important;
  -webkit-box-shadow: none !important;
  -moz-box-shadow: none !important;
}

/* 覆盖所有可能的伪元素 */
.marker-cluster::before,
.marker-cluster::after,
.marker-cluster-small::before,
.marker-cluster-small::after,
.marker-cluster-medium::before,
.marker-cluster-medium::after,
.marker-cluster-large::before,
.marker-cluster-large::after {
  display: none !important;
}

.marker-cluster div span {
  color: white;
  font-weight: bold;
  font-size: 12px;
}

/* 自定义数字标记样式 */
.custom-number-marker {
  background: transparent !important;
  border: none !important;
}

.custom-number-marker:hover div {
  transform: scale(1.1) !important;
}

/* 选中岛屿的脉冲动画 */
@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.05);
    opacity: 0.8;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

/* 自定义集群图标样式 */
.custom-cluster-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 3px solid white;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease;
}

.custom-cluster-icon:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
}

/* 地图图例样式 - 使用:deep()穿透scoped样式 */
:deep(.map-legend) {
  position: absolute !important;
  bottom: 20px !important;   /* 距离底部 */
  left: 20px !important;     /* 距离左边 */
  background: white !important;
  padding: 8px 12px !important;
  border-radius: 6px !important;
  font-family: Arial, sans-serif !important;
  font-size: 12px !important;
  color: #333 !important;
  box-shadow: 0 2px 8px rgba(0,0,0,0.2) !important;
  line-height: 1.4 !important;
  z-index: 9999 !important;
  border: 1px solid rgba(0,0,0,0.1) !important;
  width: 130px !important;   /* 增加宽度以容纳横向布局 */
}

:deep(.map-legend h4) {
  margin: 0 0 4px 0 !important;
  font-size: 13px !important;
  font-weight: bold !important;
  color: #333 !important;
  text-align: center !important;  /* 标题居中 */
}

:deep(.map-legend p) {
  margin: 0 0 6px 0 !important;
  font-size: 10px !important;
  color: #666 !important;
  text-align: center !important;  /* 副标题居中 */
}

:deep(.map-legend div) {
  display: flex !important;
  align-items: center !important;
  margin-bottom: 3px !important;
  font-size: 11px !important;
  color: #333 !important;
  white-space: nowrap !important;  /* 防止文字换行 */
}

:deep(.map-legend span) {
  display: inline-block !important;
  width: 10px !important;
  height: 10px !important;
  margin-right: 6px !important;
  border-radius: 50% !important;
  flex-shrink: 0 !important;
}

.custom-cluster-icon .lcc-value {
  color: white;
  font-weight: bold;
  font-size: 14px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
}

/* 确保自定义图标悬停效果 */
.custom-div-icon div:hover {
  transform: scale(1.1);
  transition: transform 0.3s ease;
}

.custom-div-icon {
  background: transparent !important;
  border: none !important;
}

/* 统一弹窗样式 */
.island-popup, .custom-popup-content {
  padding: 16px;
  min-width: 200px;
}

.island-popup h3, .custom-popup-content h3 {
  margin: 0 0 12px 0;
  color: #1f2937;
  font-size: 16px;
  font-weight: 600;
  white-space: nowrap;
}

.island-popup p, .custom-popup-content div {
  margin: 8px 0;
  color: #4b5563;
  font-size: 14px;
  white-space: nowrap;
}

.island-popup .select-btn, .custom-popup-content button {
  background: #5c8ac9ff;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 10px;
  cursor: pointer;
  font-size: 12px;
  font-weight: 500;
  transition: background 0.2s ease;
  margin-top: 8px;
}

.island-popup .select-btn:hover, .custom-popup-content button:hover {
  background: #4285c8ff;
}

/* 确保弹窗内容样式一致 */
.custom-popup .leaflet-popup-content {
  margin: 12px !important;
  line-height: 1.4 !important;
}

.custom-popup .leaflet-popup-content-wrapper {
  background: white !important;
  border-radius: 12px !important;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2) !important;
  border: 1px solid #e2e8f0 !important;
}



/* 右侧面板 */
.right-panel {
  padding: 24px;
}



.chart-wrapper {
  position: relative;
  height: 500px; /* 增加高度以匹配地图 */
  background: #f7fafc;
  border-radius: 8px;
  padding: 16px;
  padding-bottom: 30px;
}

.chart-wrapper canvas {
  width: 100% !important;
  height: 100% !important;
}

.chart-placeholder {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #718096;
  font-size: 16px;
}

/* 所有指标区域 - 半透明玻璃效果 */
.all-indicators-section {
  margin-top: 24px;
  padding: 24px;
}

/* 区域标题 */
.section-header {
  text-align: center;
  margin-bottom: 32px;
}

.section-title {
  color: #fff;
  font-size: 24px;
  font-weight: 700;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

/* 指标行 */
.indicator-row {
  display: flex;
  gap: 20px;
  margin-bottom: 24px;
}

.indicator-row:last-child {
  margin-bottom: 0;
}

/* 指标容器 - 三等分 */
.indicator-container {
  flex: 1;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 20px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  min-height: 320px;
}

.indicator-container:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
  background: rgba(255, 255, 255, 0.08);
}

/* 容器头部 */
.container-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.container-title {
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.indicator-badge {
  background: rgba(255, 255, 255, 0.3);
  color: #fff;
  padding: 4px 8px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 600;
  text-shadow: none;
}

/* 图片包装器 */
.image-wrapper {
  position: relative;  
  text-align: center;
}

.indicator-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
  transition: transform 0.3s ease;
}

.indicator-image:hover {
  transform: scale(1.03);
}

/* 百分比覆盖层 */
.percentage-overlay {
  position: absolute;
  bottom: 8px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.8);
  color: #fff;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 16px;
  font-weight: 700;
  text-shadow: none;
}

/* 分析容器特殊样式 */
.analysis-container {
  display: flex;
  flex-direction: column;
}

.analysis-content {
  color: rgba(255, 255, 255, 0.9);
  font-size: 13px;
  line-height: 1.5;
  flex: 1;
  overflow-y: auto;
  max-height: 240px;
}

.analysis-content::-webkit-scrollbar {
  width: 4px;
}

.analysis-content::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 2px;
}

.analysis-content::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.3);
  border-radius: 2px;
}

.analysis-item {
  margin-bottom: 10px;
}

.analysis-item strong {
  color: #fff;
  font-size: 14px;
  display: block;
  margin-bottom: 8px;
}

.analysis-content .good,
.analysis-content .moderate,
.analysis-content .poor,
.analysis-content .critical {
  display: inline-block;
  padding: 2px 6px;       /* 内边距 */
  border-radius: 6px;     /* 圆角矩形 */
  font-weight: 600;
  color: #000;            /* 黑色文字 */
  margin-right: 6px;      /* 和后面文字留点间距 */
}

.analysis-content .good {
  background-color: #4dc87a8c; /* 绿色背景 */
}

.analysis-content .moderate {
  background-color: #fcd34d7a; /* 橙黄色背景 */
}

.analysis-content .poor {
  background-color: #ff939381; /* 浅红色背景 */
}

.analysis-content .critical {
  background-color: #f871718c; /* 深红背景 */
  font-weight: 700;
}

/* 特殊容器颜色 */
.poor-container {
  border-left: 3px solid #fc8181;
}

.good-container {
  border-left: 3px solid #68d391;
}

.analysis-container {
  border-left: 3px solid #63b3ed;
}

/* 响应式设计 */
@media (max-width: 1400px) {
  .new-layout {
    max-width: 98vw;
    padding: 0 1vw;
  }
  
  .heat-map-section {
    grid-template-columns: 220px 1fr 220px;
    gap: 40px;
    min-height: 600px;
    justify-content: center;
  }
}

@media (max-width: 1200px) {
  .new-layout {
    max-width: 100vw;
    padding: 0 15px;
  }
  
  .heat-map-section {
    grid-template-columns: 160px 1fr 200px;
    gap: 20px;
    min-height: 600px;
  }
}

@media (max-width: 1024px) {
  .new-layout {
    padding: 0 10px;
  }
  
  .heat-map-section {
    grid-template-columns: 1fr;
    gap: 20px;
    min-height: 500px;
  }
  
  .select-panel,
  .recommendations-panel {
    order: 2;
  }
  
  .heat-map-container {
    order: 1;
  }
  
  .comparison-selectors {
    grid-template-columns: 1fr;
    gap: 15px;
  }
  
  .map-container {
    min-height: 450px;
  }
  
  #leaflet-map {
    min-height: 420px;
  }

  .decoration-left,
  .decoration-right {
    width: 150px;
  }
}

@media (max-width: 768px) {
  .trends-container {
    padding-bottom: 0;
  }
  
  .control-panel {
    flex-direction: column;
    gap: 12px;
    padding: 20px 16px;
  }
  
  .combined-search-container {
    min-width: 200px;
  }
  
  .combined-search-input {
    font-size: 16px; /* 防止iOS自动缩放 */
  }
  
  .main-layout {
    margin: 12px;
    max-width: calc(100% - 24px);
  }
  
   .all-indicators-section {
     padding: 20px 16px;
   }
   
   .section-title {
     font-size: 20px;
   }
   
   .indicator-row {
     flex-direction: column;
    gap: 16px;
   }
   
   .indicator-container {
     padding: 16px;
   }
   
   .indicator-image {
     height: 140px;
   }
   
   .analysis-content {
     max-height: 160px;
  }

  .decoration-left,
  .decoration-right {
    width: 100px;
  }

  .bubble {
    opacity: 0.6;
  }
}

@media (max-width: 480px) {
  .main-layout {
    margin: 8px;
    max-width: calc(100% - 16px);
  }

  .carousel-image-container {
    height: 180px;
  }

  .chart-wrapper {
    height: 300px;
  }

  .decoration-left,
  .decoration-right {
    display: none;
  }
}

/* 总结按钮样式 */
.summary-button-container {
  position: absolute;
  bottom: 10px;
  left: 10px;
  z-index: 10;
}

.summary-button {
  padding:8px 16px;
  background: linear-gradient(135deg,囗#4882a1ff,囗#32507bff);
  color:white;
  border: none;
  border-radius:20px;
  font-size: 14px;
  font-weight:600;
  cursor: pointer;
  transition:all 0.3s ease;
  box-shadow:0 4px15pxrgba(102,126,234,0.4);
}

.summary-button:hover {
  background:linear-gradient(135deg,#335e75ff,囗#223653ff);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
}

/* 总结模态框样式 */
.summary-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.summary-modal {
  background: white;
  border-radius: 15px;
  width: 90%;
  max-width: 600px;
  max-height: 80vh;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
  position: relative;
  z-index: 1001;
}

.summary-modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.summary-modal-header h3 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
}

.close-summary-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: white;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background 0.3s ease;
}

.close-summary-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.summary-modal-content {
  padding: 20px;
  max-height: 60vh;
  overflow-y: auto;
}

.summary-labels h4 {
  margin: 0 0 15px 0;
  color: #333;
  font-size: 18px;
  font-weight: 600;
  text-align: center;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 15px;
}

.summary-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.summary-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.color-indicator {
  width: 20px;
  height: 20px;
  border-radius: 4px;
  flex-shrink: 0;
}

.summary-info {
  flex: 1;
  min-width: 0;
}

.metric-name {
  font-weight: 600;
  color: #333;
  font-size: 14px;
  margin-bottom: 4px;
}

.average-value {
  color: #666;
  font-size: 16px;
  font-weight: 700;
}

.data-details {
  margin-top: 4px;
}

.data-details small {
  color: #999;
  font-size: 11px;
  font-family: monospace;
}

.status-indicator {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-left: 8px;
  vertical-align: middle;
}

.metrics-explanation {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.metrics-explanation h4 {
  margin: 0 0 15px 0;
  color: #333;
  font-size: 16px;
  font-weight: 600;
}

.explanation-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 8px;
}

.explanation-item {
  font-size: 14px;
  color: #555;
  line-height: 1.4;
}

.explanation-item strong {
  color: #333;
  font-weight: 600;
}

.color-legend {
  margin-top: 25px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.color-legend h4 {
  margin: 0 0 15px 0;
  color: #333;
  font-size: 16px;
  font-weight: 600;
}

.legend-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 10px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  color: #555;
}

.legend-color {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  flex-shrink: 0;
}

.legend-color.green {
  background-color: #28a745;
}

.legend-color.yellow {
  background-color: #ffc107;
}

.legend-color.red {
  background-color: #dc3545;
}

.legend-color.gray {
  background-color: #6c757d;
}

.legend-text strong {
  color: #333;
  font-weight: 600;
}

@media (max-width: 768px) {
  .summary-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;
  }
  
  .summary-item {
    padding: 10px;
  }
  
  .color-indicator {
    width: 16px;
    height: 16px;
  }
  
  .metric-name {
    font-size: 13px;
  }
  
  .average-value {
    font-size: 14px;
  }
}

.comparison-bg-wrapper {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(5px);
  border-radius: 12px;
  box-shadow: 0 8px 40px rgba(59,130,246,0.10);
  padding: 20px 20px;
  margin: 20px auto 0 auto;
  max-width: 1350px;
  width: 95vw;

}

.comparison-title {
  text-align: center;
  font-size: 24px;
  font-weight: 700;
  color: #ffffffff;
  text-shadow: 2px 2px 6px rgba(0, 0, 0, 0.5);
}
.main-footer {
  width: 100%;
  background: rgba(26, 43, 66, 1);
  color: #e0e7ef;
  text-align: center;
  padding: 18px 0 12px 0;
  font-size: 15px;
  font-weight: 400;
  letter-spacing: 0.02em;
  margin-top: 40px;

  box-shadow: 0 -2px 12px rgba(59,130,246,0.08);
  z-index: 10;
}

.footer-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
}

.footer-links {
  margin-top: 4px;
  font-size: 14px;
}

.footer-links a {
  color: #a5d8ff;
  text-decoration: none;
  margin: 0 6px;
  transition: color 0.2s;
}

.footer-links a:hover {
  color: #fff;
  text-decoration: underline;
}

.info-bubble {
  background: #ffffffcc; /* 半透明白色背景 */
  border-radius: 12px;
  padding: 14px 18px;
  font-size: 15px;
  line-height: 1.6;
  color: #1f2937;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  position: relative;
  margin: 15px 0;
  max-width: 90%;
}

/* 左边小三角 */
.info-bubble::before {
  content: "";
  position: absolute;
  top: 15px; /* 调整三角上下位置 */
  left: -10px;
  border-width: 10px 10px 10px 0;
  border-style: solid;
  border-color: transparent #ffffffcc transparent transparent;
}

.external-island-selection-panel-img {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100px; 
  height: 100px;
  margin-left: 30px;
}

/* 真正的 icon 图片 */
.info-icon {
  width: 100px;   /* 设置图标大小 */
  height: 100px;
  opacity: 0.65;
  object-fit: contain; 
  transition: transform 0.2s ease, opacity 0.2s ease;
}

.hand-icon {
  width: 100px;
  height: 100px;
  margin-left: 8px;
  vertical-align: middle;
  opacity: 0.8;
  margin-left: 40px;
  transition: transform 0.2s ease, opacity 0.2s ease;
}

.indicator-panel {
  background: rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(12px);
  border-radius: 20px;
  padding: 20px 30px;
  margin-bottom: 30px;
  color: #000000ff;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.panel-title {
  font-size: 20px;
  font-weight: 600;
}

.indicator-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 一行三个 */
  gap: 20px;
}

.indicator-panel {
  display: flex;                /* 🔹 变成 flex 容器 */
  flex-direction: column;       /* 🔹 垂直排列：header → text → image */
  justify-content: flex-start;  /* 默认从上往下排 */
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 15px 20px;
  color: #000000ff;
  font-size: 13px;
  transition: all 0.3s ease;
}
.indicator-panel:hover {
  background: rgba(255, 255, 255, 0.9);
  transform: translateY(-5px); /* 整个 panel 轻微浮起 */
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.4);
}


.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.panel-title {
  font-size: 18px;
  font-weight: 600;
}

.analysis-content p {
  margin: 4px 0;
  font-size: 14px;
  line-height: 1.3;
  color: #000000ff;
  flex: 0 0 auto;
}

.indicator-images {
  margin-top: auto;    /* 🔹 自动推到 panel 底部 */
  display: flex;
  gap: 14px;
}


.image-card {
  flex: 1;
  position: relative;
  border-radius: 8px;
  overflow: hidden;
}

.image-card img {
  width: 100%;
  height: 160px;       /* 统一高度 */
  object-fit: cover;
  border-radius: 8px;
  display: block;
  transition: transform 0.3s ease;  /* 动画 */
}

/* Hover 放大效果 */
.image-card:hover img {
  transform: scale(1.1);  /* 放大 10% */
}



.percentage-overlay {
  position: absolute;
  bottom: 10px;
  right: 10px;
  display: inline-block;        /* 🔹 只包裹文字 */
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  font-weight: bold;
  font-size: 13px;
  padding: 2px 6px;             /* 🔹 缩小内边距 */
  border-radius: 6px;
  white-space: nowrap;          /* 🔹 防止换行 */
  min-width: unset;             /* 🔹 禁止自动拉宽 */
  max-width: max-content;       /* 🔹 宽度等于文字 */
}

/* LCC指标 - 第一个图片是poor状态(<30%)，第二个图片是good状态(>50%) */
.indicator-panel:nth-child(1) .image-card:nth-child(1) .percentage-overlay {
  background-color: #ff939381; /* 浅红色背景 - 对应poor */
  color: #000;
}

.indicator-panel:nth-child(1) .image-card:nth-child(2) .percentage-overlay {
  background-color: #4dc87a8c; /* 绿色背景 - 对应good */
  color: #000;
}

/* AS指标 - 第一个图片是critical状态(>40%)，第二个图片是good状态(<10%) */
.indicator-panel:nth-child(2) .image-card:nth-child(1) .percentage-overlay {
  background-color: #f871718c; /* 深红背景 - 对应critical */
  color: #000;
}

.indicator-panel:nth-child(2) .image-card:nth-child(2) .percentage-overlay {
  background-color: #4dc87a8c; /* 绿色背景 - 对应good */
  color: #000;
}

/* SD指标 - 第一个图片是poor状态(<5%)，第二个图片是good状态(5-15%) */
.indicator-panel:nth-child(3) .image-card:nth-child(1) .percentage-overlay {
  background-color: #ff939381; /* 浅红色背景 - 对应poor */
  color: #000;
}

.indicator-panel:nth-child(3) .image-card:nth-child(2) .percentage-overlay {
  background-color: #4dc87a8c; /* 绿色背景 - 对应good */
  color: #000;
}

/* DI指标 - 第一个图片是poor状态(>10%)，第二个图片是good状态(<5%) */
.indicator-panel:nth-child(4) .image-card:nth-child(1) .percentage-overlay {
  background-color: #ff939381; /* 浅红色背景 - 对应poor */
  color: #000;
}

.indicator-panel:nth-child(4) .image-card:nth-child(2) .percentage-overlay {
  background-color: #4dc87a8c; /* 绿色背景 - 对应good */
  color: #000;
}

/* PI指标 - 第一个图片是poor状态(>10%)，第二个图片是good状态(<5%) */
.indicator-panel:nth-child(5) .image-card:nth-child(1) .percentage-overlay {
  background-color: #ff939381; /* 浅红色背景 - 对应poor */
  color: #000;
}

.indicator-panel:nth-child(5) .image-card:nth-child(2) .percentage-overlay {
  background-color: #4dc87a8c; /* 绿色背景 - 对应good */
  color: #000;
}

.section-subtitle {

  color: #ffffffff;
  text-shadow: 1px 1px 4px rgba(0, 0, 0, 0.4);
  font-size: 16px;

}



</style>

