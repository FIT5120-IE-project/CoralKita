<template>
  <div class="trends-container bg-container">
    <!-- 背景装饰元素 -->
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

    <!-- 顶部导航 -->
    <div class="top-nav">
      <div class="nav-left">
        <!-- 左侧图标 -->
        <img src="@/assets/icon.png" alt="logo" class="nav-logo" @click="goToHome" />
        <h1 class="logo" @click="goToHome">CoralKita</h1>
      </div>
      <div class="nav-right">
        <div class="nav-items">
          <span class="nav-item" @click="goToMap">Map</span>
          <span class="nav-item active">Trends</span>
          <span class="nav-item" @click="goToEducation">Education</span>
          <span class="nav-item" @click="goToGovernment">Government</span>
        </div>
      </div>
    </div>

    <!-- 主标题 -->
    <div class="page-title">
      <h2>Coral Data Visualization & Trends</h2>
    </div>

    <!-- 加载指示器 -->
    <div v-if="isLoading" class="loading-indicator">
      <div class="loading-spinner"></div>
      <p>{{ loadingMessage }}</p>
    </div>

    <!-- 控制面板 -->
    <div class="control-panel">
      <div class="control-item">
        <div :class="['combined-search-container', { 'dropdown-active': showDropdown }]">
          <input 
          v-model="searchInput" 
          @input="onSearchInput"
            @focus="handleInputFocus"
            @blur="handleInputBlur"
            @keydown="handleKeyDown"
            placeholder="Search or choose an island..." 
            class="combined-search-input"
            autocomplete="off"
          />
          <button 
            @click="toggleDropdown" 
            class="dropdown-toggle-btn"
            type="button"
          >
            <span :class="['dropdown-arrow', { 'rotated': showDropdown }]">▼</span>
          </button>
          
          <!-- 下拉选项列表 -->
          <div v-if="showDropdown" class="dropdown-list" ref="dropdownList">
            <div 
              v-for="(island, index) in filteredIslands" 
              :key="island"
              :class="['dropdown-item', { 
                'highlighted': index === highlightedIndex,
                'selected': island === selectedIsland 
              }]"
              @mousedown="selectIslandFromDropdown(island)"
              @mouseover="highlightedIndex = index"
            >
              <span class="island-icon">🏝️</span>
              <span class="island-name">{{ island }}</span>
              <span v-if="island === selectedIsland" class="selected-badge">✓</span>
            </div>
            <div v-if="filteredIslands.length === 0" class="no-results">
              <span class="no-results-icon">🔍</span>
              <span>No islands found</span>
            </div>
          </div>
        </div>
      </div>

      <div class="control-item">
        <button @click="openCompareModal" class="control-button">
        Compare Islands
      </button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-layout">
      <!-- 左侧马来西亚地图区域 -->
      <div class="left-panel">
        <div class="map-display">
          <div class="map-header">
            <h3>Malaysia Coral Reef Locations</h3>
            <div v-if="selectedIsland" class="location-info">
              <span class="island-name">{{ selectedIsland }}</span>
              <div v-if="selectedCoordinates" class="coordinates">
                <span class="coord-label">Coordinates:</span>
                <span class="coord-values">{{ selectedCoordinates.lat.toFixed(4) }}°N, {{ selectedCoordinates.lng.toFixed(4) }}°E</span>
          </div>
              </div>
            </div>
          
          <div class="map-container" ref="mapContainer" id="leaflet-map">
            <!-- Leaflet地图将在这里渲染 -->
          </div>
          
          <!-- 地图图例 -->
          <div class="map-legend">
            <div class="legend-item">
              <div class="legend-marker has-data"></div>
              <span>Islands with Data</span>
          </div>
            <div class="legend-item">
              <div class="legend-marker no-data"></div>
              <span>Islands without Data</span>
            </div>
            <div class="legend-item">
              <div class="legend-marker selected"></div>
              <span>Selected Island</span>
            </div>
          </div>
          </div>
        </div>

      <!-- 右侧图表区域 -->
      <div class="right-panel">
        <div class="chart-wrapper">
          <canvas ref="trendChart"></canvas>
          <div v-if="trendData.length === 0" class="chart-placeholder">
            <p>Select an island to view trend data</p>
          </div>
          
          <!-- 总结按钮 -->
          <div v-if="trendData.length > 0" class="summary-button-container">
            <button @click="showSummaryModal = true" class="summary-button">
              📊 View Data Summary
            </button>
          </div>
        </div>
      </div>
    </div>

        <!-- 底部白化对比图 -->
    <div class="bleaching-gallery">
          <div class="bleaching-item">
            <img src="../assets/bleach 20%.jpg" alt="20% Bleaching" class="bleaching-image" />
        <div class="bleaching-label">20%</div>
        <div class="bleaching-desc">Mild bleaching, most corals remain healthy and active.</div>
          </div>

          <div class="bleaching-item">
            <img src="../assets/Bleached 40%.jpg" alt="40% Bleaching" class="bleaching-image" />
        <div class="bleaching-label">40%</div>
        <div class="bleaching-desc">Moderate bleaching, some corals lose symbiotic algae and face increasing stress.</div>
          </div>

          <div class="bleaching-item">
            <img src="../assets/bleach 60%.jpg" alt="60% Bleaching" class="bleaching-image" />
        <div class="bleaching-label">60%</div>
        <div class="bleaching-desc">Severe bleaching, most corals are close to dying and the reef ecosystem is heavily impacted.</div>
          </div>
        </div>

    <!-- 岛屿对比弹窗 -->
    <div v-if="showCompareModal" class="modal-overlay" @click="closeCompareModal">
      <div class="compare-modal" @click.stop>
        <div class="modal-header">
          <h3>Compare two or more Island</h3>
          <button @click="closeCompareModal" class="close-btn">Close</button>
      </div>

        <div class="modal-content">
          <!-- 岛屿选择区域 -->
          <div class="choose-islands-section">
            <h4>Choose Islands</h4>
                        <div class="islands-grid">
              <div 
                v-for="(column, columnIndex) in compareColumns" 
                :key="columnIndex"
                class="island-column"
              >
                                <div class="column-header">
                  <h5>Select Island {{ columnIndex + 1 }}</h5>
          </div>
                <div class="island-dropdown">
                  <select 
                    :value="selectedCompareIslands[columnIndex] || ''"
                    @change="onCompareIslandSelect(columnIndex, $event.target.value)"
                    class="island-select"
                  >
                    <option value="">Choose Island</option>
                    <option 
                      v-for="island in availableIslands" 
                      :key="island"
                      :value="island"
                      :disabled="selectedCompareIslands.includes(island) && selectedCompareIslands[columnIndex] !== island"
                    >
                      {{ island }}
                    </option>
                  </select>
                </div>
                <div v-if="selectedCompareIslands[columnIndex]" class="selected-island-display">
                  <div class="island-tag">
                    <span>{{ selectedCompareIslands[columnIndex] }}</span>
                    <button @click="removeCompareIsland(columnIndex)" class="remove-btn">×</button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 属性筛选区域 -->
          <div class="attribute-filter-section">
            <label for="attribute-select">Select Attribute:</label>
            <select v-model="selectedCompareAttribute" class="attribute-select">
              <option value="lcc">Live Coral Cover (LCC)</option>
              <option value="ot">Other (OT)</option>
              <option value="as">Available Substrate (AS)</option>
              <option value="sd">Sand (SD)</option>
              <option value="di">Disturbance Indicator (DI)</option>
              <option value="pi">Pollution Indicator (PI)</option>
            </select>
          </div>

          <!-- 对比图表区域 -->
          <div class="compare-chart-container">
            <canvas id="compareChart" ref="compareChart" v-show="selectedCompareIslands.filter(island => island !== '').length > 0"></canvas>
            <div v-if="selectedCompareIslands.filter(island => island !== '').length === 0" class="no-comparison-data">
              Please select at least one island to compare
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 总结模态框 -->
    <div v-if="showSummaryModal" class="summary-modal-overlay" @click="showSummaryModal = false">
      <div class="summary-modal" @click.stop>
        <div class="summary-modal-header">
          <h3>Data Summary</h3>
          <button @click="showSummaryModal = false" class="close-summary-btn">×</button>
        </div>
        <div class="summary-modal-content">
                       <div class="summary-grid">
               <div class="summary-item" v-for="(dataset, index) in chartData.datasets" :key="index">
                 <div class="color-indicator" :style="{ backgroundColor: dataset.borderColor }"></div>
                 <div class="summary-info">
                   <div class="metric-name">{{ dataset.label }}</div>
                                   <div class="average-value" :style="{ color: getValueColor(dataset.label, getAverageValue(dataset.data) / 100) }">
                  Average: {{ getAverageValue(dataset.data) }}%
                </div>
                 </div>
               </div>
             </div>
             
             <div class="color-legend">
               <h4>Color Legend:</h4>
               <div class="legend-grid">
                 <div class="legend-item">
                   <span class="legend-color green"></span>
                   <span class="legend-text"><strong>Green:</strong> Safe/Good</span>
                 </div>
                 <div class="legend-item">
                   <span class="legend-color yellow"></span>
                   <span class="legend-text"><strong>Yellow:</strong> Moderate/Warning</span>
                 </div>
                 <div class="legend-item">
                   <span class="legend-color red"></span>
                   <span class="legend-text"><strong>Red:</strong> Dangerous/Poor</span>
                 </div>
                 <div class="legend-item">
                   <span class="legend-color gray"></span>
                   <span class="legend-text"><strong>Gray:</strong> Neutral</span>
                 </div>
               </div>
             </div>
             
             <div class="metrics-explanation">
               <h4>Metrics Explanation:</h4>
               <div class="explanation-grid">
                 <div class="explanation-item">
                   <strong>LCC:</strong> More live coral is better.
                 </div>
                 <div class="explanation-item">
                   <strong>AS:</strong> Less sand and rubble is better.
                 </div>
                 <div class="explanation-item">
                   <strong>SD:</strong> A moderate amount of soft coral is best.
                 </div>
                 <div class="explanation-item">
                   <strong>DI:</strong> Fewer signs of damage are better.
                 </div>
                 <div class="explanation-item">
                   <strong>PI:</strong> Less pollution is better.
                 </div>
                 <div class="explanation-item">
                   <strong>OT:</strong> Neutral category, not good or bad on its own.
                 </div>
               </div>
             </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { Chart, registerables } from 'chart.js';
// 移除API_BASE_URL导入，使用相对路径
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

Chart.register(...registerables);

// 修复Leaflet默认图标问题
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
      availableIslands: [],
      selectedIsland: '',
      searchInput: '',
      trendData: [],
      chart: null,
      currentIslandIndex: 0,
      // Leaflet地图相关数据
      map: null,
      islandLocations: [],
      selectedCoordinates: null,
      markerLayers: [],
      selectedMarker: null,
      // 合并搜索框相关数据
      showDropdown: false,
      highlightedIndex: -1,
      isActivelySearching: false, // 标记用户是否正在主动搜索
      // 对比功能相关数据
      showCompareModal: false,
      selectedCompareIslands: ['', '', '', ''], // 4个下拉框的选择，用数组索引对应列
      // 总结模态框
      showSummaryModal: false,
      selectedCompareAttribute: 'lcc', // 默认选择Live Coral Cover
      
      // 轮播图相关数据
      currentSlide: 0,
      coralIndicators: [
        {
          title: 'Live Coral Cover (LCC)',
          average: 'avg. 51.9%',
          image: require('@/assets/LCC.png')
        },
        {
          title: 'Available Substrate (AS)',
          average: 'avg. 24%',
          image: require('@/assets/AS.png')
        },
        {
          title: 'Soft Coral (SD)',
          average: 'avg. 9.4%',
          image: require('@/assets/SD.png')
        },
        {
          title: 'Disturbance Index (DI)',
          average: 'avg. 8.8%',
          image: require('@/assets/DI.png')
        },
        {
          title: 'Pollution Index (PI)',
          average: 'avg. 3.5%',
          image: require('@/assets/PI.png')
        },
        {
          title: 'Other Categories (OT)',
          average: 'avg. 2.5%',
          image: require('@/assets/LCC.png') // 使用LCC作为默认
        }
      ],
      compareColumns: [1, 2, 3, 4], // 4列布局
      compareChart: null,
      compareData: {},
      // 图表数据
      chartData: {
        datasets: []
      },
      // 加载状态
      isLoading: false,
      loadingMessage: 'Loading...'
    };
  },
  
  computed: {
    // 过滤后的岛屿列表
    filteredIslands() {
      // 如果用户没有主动搜索（即只是已选择状态），显示所有岛屿
      if (!this.isActivelySearching) {
        return this.availableIslands;
      }
      
      // 用户正在主动搜索，根据输入内容过滤
      if (!this.searchInput.trim()) {
        return this.availableIslands;
      }
      
      return this.availableIslands.filter(island => 
        island.toLowerCase().includes(this.searchInput.toLowerCase())
      );
    }
  },
  
  methods: {
    // 轮播图控制方法
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
    console.log('组件挂载完成，开始初始化...');
    
    // 并行执行初始化和数据加载
    await Promise.all([
      this.$nextTick(() => {
        this.setupChart();
        this.initializeLeafletMap();
      }),
      this.loadAvailableIslands()
    ]);
    
    // 添加窗口事件监听器
    window.addEventListener('resize', this.updateDropdownPosition);
    window.addEventListener('scroll', this.updateDropdownPosition);
    

  },
  
  methods: {
    async loadAvailableIslands() {
      this.isLoading = true;
      this.loadingMessage = 'Loading islands...';
      
      try {
        console.log('正在从后端加载岛屿列表...');
        const response = await axios.get('/api/trend/islands');
        console.log('岛屿列表API响应:', response);
        
        if (response.data.code === 1) {
          this.availableIslands = response.data.data;
          console.log('成功从后端加载岛屿列表:', this.availableIslands);
          // 立即加载岛屿坐标数据
          await this.loadIslandCoordinates();
        } else {
          console.error('后端返回错误:', response.data.msg);
          this.availableIslands = [];
        }
      } catch (error) {
        console.error('加载岛屿列表失败:', error);
        console.error('错误详情:', error.response || error.message);
        // 如果接口调用失败，显示错误信息
        this.availableIslands = [];
        alert('无法连接到后端服务，请检查后端是否正在运行');
      } finally {
        this.isLoading = false;
      }
    },
    
    async loadTrendData(island) {
      if (!island) return;
      
      console.log('正在加载岛屿趋势数据:', island);
      const url = `/api/trend/query?island=${encodeURIComponent(island)}`;
      console.log('趋势数据API地址:', url);
      
      try {
        const response = await axios.get(url);
        console.log('趋势数据API响应:', response);
        
        if (response.data.code === 1) {
          this.trendData = response.data.data;
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
    
    onIslandChange() {
      console.log('岛屿选择已更改为:', this.selectedIsland);
      if (this.selectedIsland) {
        // 通过地图选择函数来处理，确保地图同步
        this.selectIslandFromMap(this.selectedIsland);
      }
    },
    
    onSearchInput() {
      console.log('用户输入搜索:', this.searchInput);
      this.isActivelySearching = true; // 标记为主动搜索状态
      this.highlightedIndex = 0; // 重置高亮索引
      this.showDropdown = true;
      
      // 更新下拉框位置
      this.$nextTick(() => {
        this.updateDropdownPosition();
      });
    },
    
    // 切换下拉菜单显示状态
    toggleDropdown() {
      console.log('切换下拉框状态，当前状态:', this.showDropdown);
      this.showDropdown = !this.showDropdown;
      
      if (this.showDropdown) {
        this.highlightedIndex = 0;
        // 重置为非搜索状态，显示所有岛屿
        this.isActivelySearching = false;
        
        // 获取输入框的引用并让它获得焦点
        const input = this.$el.querySelector('.combined-search-input');
        if (input) {
          input.focus();
          // 如果有内容，选中全部文本
          if (this.searchInput) {
            input.select();
          }
        }
        
        this.$nextTick(() => {
          this.updateDropdownPosition();
        });
      }
    },
    
    // 从下拉菜单选择岛屿
    selectIslandFromDropdown(island) {
      console.log('从下拉菜单选择岛屿:', island);
      this.searchInput = island;
      this.isActivelySearching = false; // 选择完成，不再是搜索状态
      this.showDropdown = false;
      this.selectIslandFromMap(island);
    },
    
    // 处理输入框获得焦点
    handleInputFocus(event) {
      console.log('输入框获得焦点，显示下拉框');
      this.showDropdown = true;
      this.highlightedIndex = 0;
      
      // 重置为非搜索状态，显示所有岛屿
      this.isActivelySearching = false;
      
      // 如果输入框有内容，选中全部文本，方便用户重新输入
      if (this.searchInput && event.target) {
        this.$nextTick(() => {
          event.target.select();
        });
      }
      
      this.$nextTick(() => {
        this.updateDropdownPosition();
      });
    },

    // 处理输入框失去焦点
    handleInputBlur() {
      // 延迟隐藏下拉菜单，以便点击事件能正常触发
      setTimeout(() => {
        this.showDropdown = false;
      }, 150);
    },
    
    // 处理键盘事件
    handleKeyDown(event) {
      if (!this.showDropdown || this.filteredIslands.length === 0) return;
      
      switch (event.key) {
        case 'ArrowDown':
          event.preventDefault();
          this.highlightedIndex = Math.min(
            this.highlightedIndex + 1, 
            this.filteredIslands.length - 1
          );
          this.scrollToHighlighted();
          break;
          
        case 'ArrowUp':
          event.preventDefault();
          this.highlightedIndex = Math.max(this.highlightedIndex - 1, 0);
          this.scrollToHighlighted();
          break;
          
        case 'Enter':
          event.preventDefault();
          if (this.highlightedIndex >= 0 && this.filteredIslands[this.highlightedIndex]) {
            this.selectIslandFromDropdown(this.filteredIslands[this.highlightedIndex]);
          }
          break;
          
        case 'Escape':
          this.showDropdown = false;
          break;
      }
    },
    
    // 滚动到高亮项
    scrollToHighlighted() {
      this.$nextTick(() => {
        const dropdown = this.$refs.dropdownList;
        const highlighted = dropdown?.querySelector('.dropdown-item.highlighted');
        if (dropdown && highlighted) {
          highlighted.scrollIntoView({ block: 'nearest' });
        }
      });
    },
    
    // 更新下拉框位置
    updateDropdownPosition() {
      const dropdown = this.$refs.dropdownList;
      const searchContainer = dropdown?.parentElement;
      const inputElement = searchContainer?.querySelector('.combined-search-input');
      
      if (dropdown && inputElement) {
        const rect = inputElement.getBoundingClientRect();
        
        // 设置下拉框位置和尺寸
        dropdown.style.left = rect.left + 'px';
        dropdown.style.top = (rect.bottom + 4) + 'px';
        dropdown.style.width = rect.width + 'px';
        
        // 根据可用空间调整高度
        const windowHeight = window.innerHeight;
        const spaceBelow = windowHeight - rect.bottom - 4;
        const maxHeight = Math.min(300, spaceBelow - 20);
        
        if (maxHeight > 100) {
          dropdown.style.maxHeight = maxHeight + 'px';
        } else {
          // 如果下方空间不足，显示在上方
          const spaceAbove = rect.top - 4;
          const maxHeightAbove = Math.min(300, spaceAbove - 20);
          dropdown.style.top = (rect.top - maxHeightAbove - 4) + 'px';
          dropdown.style.maxHeight = maxHeightAbove + 'px';
        }
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
        
        console.log('Leaflet地图初始化完成');
      });
    },
    
    // 添加地图图层
    addMapLayers() {
      // 使用多个地图源以获得最佳效果
      const osmLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors',
        maxZoom: 19
      });
      
      const cartoLayer = L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png', {
        attribution: '© CARTO © OpenStreetMap contributors',
        maxZoom: 19
      });
      
      const satelliteLayer = L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
        attribution: '© Esri © DigitalGlobe © GeoEye © Earthstar Geographics © CNES/Airbus DS © USDA © USGS © AeroGRID © IGN © IGP',
        maxZoom: 19
      });
      
      // 默认使用CartoDB Voyager图层（更美观）
      cartoLayer.addTo(this.map);
      
      // 添加图层控制
      const baseLayers = {
        "CartoDB Voyager": cartoLayer,
        "OpenStreetMap": osmLayer,
        "Satellite": satelliteLayer
      };
      
      L.control.layers(baseLayers).addTo(this.map);
    },
    
    // 加载岛屿坐标数据
    async loadIslandCoordinates() {
      try {
        console.log('开始加载岛屿坐标数据...');
        const allCoordinates = {};
        
        for (const island of this.availableIslands) {
          try {
            const response = await axios.get(`/api/trend/bleach?island=${encodeURIComponent(island)}`);
            if (response.data.code === 1 && response.data.data && response.data.data.length > 0) {
              const coralData = response.data.data[0]; // 取第一条数据获取坐标
              if (coralData.islandLat && coralData.islandLng) {
                allCoordinates[island] = {
                  lat: coralData.islandLat,
                  lng: coralData.islandLng,
                  hasData: true
                };
              }
            }
          } catch (error) {
            console.warn(`无法获取岛屿 ${island} 的坐标数据:`, error);
          }
        }
        
        // 存储岛屿位置数据
        this.islandLocations = Object.entries(allCoordinates).map(([island, coords]) => ({
          island,
          lat: coords.lat,
          lng: coords.lng,
          hasData: coords.hasData
        }));
        
        console.log('加载的岛屿坐标:', this.islandLocations);
        
        // 在地图上添加标记
        this.addIslandMarkers();
        
        // 如果已经有选中的岛屿，立即定位
        if (this.selectedIsland) {
          console.log('检测到预选岛屿:', this.selectedIsland);
          setTimeout(() => {
            this.selectIslandFromMap(this.selectedIsland);
          }, 500);
        }
        
      } catch (error) {
        console.error('加载岛屿坐标失败:', error);
      }
    },
    
    // 在地图上添加岛屿标记
    addIslandMarkers() {
      if (!this.map || this.islandLocations.length === 0) return;
      
      // 清除现有标记
      this.markerLayers.forEach(marker => this.map.removeLayer(marker));
      this.markerLayers = [];
      
      // 创建自定义图标
      const createCustomIcon = (hasData, isSelected = false) => {
        const color = isSelected ? '#dc2626' : (hasData ? '#16a34a' : '#6b7280');
        const size = isSelected ? [35, 45] : [25, 35];
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
              border: 4px solid ${borderColor};
              box-shadow: 0 4px 15px ${shadowColor};
              transform: rotate(-45deg);
              display: flex;
              align-items: center;
              justify-content: center;
              position: relative;
              ${animation}
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
        const marker = L.marker(
          [location.lat, location.lng],
          { 
            icon: createCustomIcon(location.hasData),
            title: location.island
          }
        );
        
        // 创建弹出窗口内容
        const isCurrentlySelected = location.island === this.selectedIsland;
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
              ${location.hasData ? '✅ Data Available' : '❌ No Data'}
            </div>
            ${isCurrentlySelected ? 
              '<div style="background: #fee2e2; color: #dc2626; padding: 4px 8px; border-radius: 8px; font-size: 12px; font-weight: bold; margin-bottom: 8px;">🎯 Currently Selected</div>' : 
              '<button onclick="window.selectIslandFromMap(\'' + location.island + '\')" style="background: #3b82f6; color: white; border: none; padding: 6px 12px; border-radius: 6px; cursor: pointer; font-size: 12px; font-weight: bold;">Select Island</button>'
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
      
      // 全局函数，供弹出窗口按钮调用
      window.selectIslandFromMap = (island) => {
        this.selectIslandFromMap(island);
      };
    },
    
    // 从地图选择岛屿
    selectIslandFromMap(island) {
      console.log('从地图选择岛屿:', island);
      this.selectedIsland = island;
      this.searchInput = island;
      
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
    },
    
    // 更新标记样式
    updateMarkerStyles(selectedIsland) {
      this.markerLayers.forEach((marker, index) => {
        const location = this.islandLocations[index];
        const isSelected = location.island === selectedIsland;
        
        const createCustomIcon = (hasData, isSelected = false) => {
          const color = isSelected ? '#dc2626' : (hasData ? '#16a34a' : '#6b7280');
          const size = isSelected ? [35, 45] : [25, 35];
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
                border: 4px solid ${borderColor};
                box-shadow: 0 4px 15px ${shadowColor};
                transform: rotate(-45deg);
                display: flex;
                align-items: center;
                justify-content: center;
                position: relative;
                ${animation}
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
        
        marker.setIcon(createCustomIcon(location.hasData, isSelected));
      });
    },
    
    // 打开选中岛屿的弹窗
    openSelectedIslandPopup(selectedIsland) {
      if (!this.map) return;
      
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
    goToMap() {
      console.log('导航到Map页面');
      this.$router.push('/map');
    },

    goToEducation() {
      console.log('导航到Education页面');
      // 设置导航标记，表示这是路由导航而不是页面刷新
      localStorage.setItem('hasNavigatedToEducation', 'true');
      this.$router.push('/education');
    },

    goToGovernment() {
      console.log('导航到Government页面');
      this.$router.push('/government');
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
        const response = await axios.post('/api/trend/compare', {
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
          const response = await axios.get(`/api/trend/query?island=${encodeURIComponent(island)}`);
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
              }
            },
            scales: {
              x: {
                title: { display: true, text: 'Time' }
              },
              y: {
                title: { display: true, text: 'Value' },
                beginAtZero: true
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
              const dateStr = `${item.date[0]}-${String(item.date[1]).padStart(2, '0')}`;
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
          const item = data.find(d => `${d.date[0]}-${String(d.date[1]).padStart(2, '0')}` === date);
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
        return `${year}-${month.toString().padStart(2, '0')}`;
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
    
    // 清理全局函数
    if (window.selectIslandFromMap) {
      delete window.selectIslandFromMap;
    }
    
    // 清理事件监听器
    window.removeEventListener('resize', this.updateDropdownPosition);
    window.removeEventListener('scroll', this.updateDropdownPosition);
    

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
}

.bg-container {
  background-image: url('@/assets/bg_trend.jpg'); 
  background-size: cover;       
  background-position: center;  
  background-repeat: no-repeat; 
  min-height: 100vh;            
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

/* 波浪图案 */
.wave-pattern {
  position: absolute;
  width: 100px;
  height: 2px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 2px;
}

.wave-1 {
  top: 30%;
  left: 80px;
  transform: rotate(45deg);
}

.wave-2 {
  top: 65%;
  left: 120px;
  transform: rotate(-30deg);
}

.wave-3 {
  top: 35%;
  right: 80px;
  transform: rotate(-45deg);
}

.wave-4 {
  top: 75%;
  right: 120px;
  transform: rotate(30deg);
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
}

.nav-item {
  padding: 8px 16px;
  color: rgba(255, 255, 255, 0.7);
  font-weight: 400;
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
  padding: 18px 0;
  position: relative;
  z-index: 5;
}

.page-title h2 {
  margin: 0;
  font-size: 1.8rem;
  color: #ffffffff;
  font-weight: 600;
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
  border-color: #3b82f6;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.15);
}

.combined-search-input:focus {
  border-color: #3b82f6;
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
  color: #3b82f6;
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

/* 主要布局 */
.main-layout {
  display: grid;
  grid-template-columns: 520px 1fr;
  gap: 0;
  max-width: 1200px;
  margin: 12px auto;
  background: rgba(255, 255, 255, 0.69);
  backdrop-filter: blur(10px);
  border-radius: 8px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  overflow: visible; /* 允许下拉框显示 */
  position: relative;
  z-index: 5;
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
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  backdrop-filter: blur(5px);
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
  flex-direction: column;
  gap: 4px;
}

.coord-label {
  font-size: 12px;
  color: #718096;
  font-weight: 500;
}

.coord-values {
  font-size: 14px;
  color: #4a5568;
  font-weight: 600;
  font-family: 'Courier New', monospace;
}

/* Leaflet地图容器 */
.map-container {
  position: relative;
  flex: 1;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  border: 2px solid #e2e8f0;
  min-height: 400px;
}

#leaflet-map {
  width: 100%;
  height: 100%;
  min-height: 400px;
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

/* 地图图例 */
.map-legend {
  margin-top: 12px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
  font-size: 12px;
  color: #4a5568;
}

.legend-item:last-child {
  margin-bottom: 0;
}

.legend-marker {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  border: 2px solid;
}

.legend-marker.has-data {
  background: #48bb78;
  border-color: #38a169;
}

.legend-marker.no-data {
  background: #a0a0a0;
  border-color: #707070;
}

.legend-marker.selected {
  background: #ff6b6b;
  border-color: #fff;
  box-shadow: 0 0 0 1px #ff6b6b;
}

/* 右侧面板 */
.right-panel {
  padding: 24px;
}



.chart-wrapper {
  position: relative;
  height: 320px;
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

/* 底部白化对比图 */
.bleaching-gallery {
  display: flex;
  justify-content: center;
  gap: 30px;
  padding: 32px 24px;
  background: rgba(45, 55, 72, 0.3);
  backdrop-filter: blur(10px);
  margin-top: 20px;
  position: relative;
  z-index: 5;
}

.bleaching-item {
  position: relative;  
  display: inline-block;
  text-align: center;
}

/* 图片样式 */
.bleaching-image {
  width: 150px;
  height: 100px;
  object-fit: cover;
  border-radius: 6px;
  display: block;
}

/* 图片悬停放大 */
.bleaching-item:hover .bleaching-image {
  transform: scale(1.1);
  box-shadow: 0 12px 28px rgba(0,0,0,0.28);
}

/* 百分比标签：在图片底部 */
.bleaching-label {
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  color: #fff;
  font-size: 20px;
  font-weight: 700;
  text-shadow: 0 0 6px rgba(0, 0, 0, 0.6);
  pointer-events: none;
}

/* 英文描述：悬停显示 */
.bleaching-desc {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 6px 8px;
  background: rgba(0, 0, 0, 0.55);
  color: #fff;
  font-size: 12px;
  font-weight: 500;
  opacity: 0;
  transform: translateY(20%);
  transition: opacity 0.5s ease, transform 0.5s ease;
  border-radius: 6px;
}

/* 悬停时显示描述 */
.bleaching-item:hover .bleaching-desc {
  opacity: 1;
  transform: translateY(0);
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .main-layout {
    grid-template-columns: 1fr;
    margin: 16px;
    max-width: calc(100% - 32px);
  }
  
  .left-panel {
    border-right: none;
    border-bottom: 1px solid #e2e8f0;
  }
  
  .carousel-image-container {
    height: 200px;
  }
  
  .chart-wrapper {
    height: 250px;
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
  
  .bleaching-gallery {
    flex-wrap: wrap;
    gap: 16px;
    padding: 24px 16px;
  }
  
  .bleaching-image {
    width: 100px;
    height: 70px;
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
    height: 200px;
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
  padding: 8px 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.summary-button:hover {
  background: #5a6fd8;
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
</style>

