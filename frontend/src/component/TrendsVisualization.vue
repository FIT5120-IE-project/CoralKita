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
        <select 
          v-model="selectedIsland" 
          @change="onIslandChange"
          class="control-select"
        >
            <option value="">Choose Area</option>
          <option 
            v-for="island in availableIslands" 
            :key="island" 
            :value="island"
          >
            {{ island }}
          </option>
          </select>
        </div>

      <div class="control-item">
          <input 
          v-model="searchInput" 
          @input="onSearchInput"
            placeholder="Type..." 
          class="control-input"
          />
      </div>

      <div class="control-item">
        <button @click="openCompareModal" class="control-button">
        Compare Islands
      </button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-layout">
      <!-- 左侧轮播图区域 -->
      <div class="left-panel">
        <div class="carousel-display">
          <div class="carousel-image-container">
            <img 
              v-if="carouselImages.length > 0" 
              :src="carouselImages[currentImageIndex]" 
              :alt="`Carousel Image ${currentImageIndex + 1}`" 
              class="carousel-image" 
            />
            <div v-else class="image-placeholder">
              <div class="placeholder-content">
                <div class="placeholder-icon">🖼️</div>
                <p>图片轮播区域</p>
                <small>请添加图片到轮播列表</small>
          </div>
              </div>
            </div>
          <div class="carousel-navigation">
            <button class="nav-arrow" @click="previousImage" :disabled="carouselImages.length === 0">❮</button>
          <div class="carousel-indicators">
            <span 
                v-for="(image, index) in carouselImages" 
              :key="index"
                :class="['dot', { active: currentImageIndex === index }]"
                @click="goToImage(index)"
            ></span>
              <!-- 如果没有图片，显示占位指示器 -->
              <span v-if="carouselImages.length === 0" class="dot placeholder-dot"></span>
              <span v-if="carouselImages.length === 0" class="dot placeholder-dot"></span>
              <span v-if="carouselImages.length === 0" class="dot placeholder-dot"></span>
          </div>
            <button class="nav-arrow" @click="nextImage" :disabled="carouselImages.length === 0">❯</button>
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
  </div>
</template>

<script>
import axios from 'axios';
import { Chart, registerables } from 'chart.js';
Chart.register(...registerables);

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
      // 轮播图相关数据
      carouselImages: [
        // 这里可以添加图片路径，目前为空数组
        // '../assets/coral1.jpg',
        // '../assets/coral2.jpg',
        // '../assets/coral3.jpg',
      ],
      currentImageIndex: 0,
      // 对比功能相关数据
      showCompareModal: false,
      selectedCompareIslands: ['', '', '', ''], // 4个下拉框的选择，用数组索引对应列
      selectedCompareAttribute: 'lcc', // 默认选择Live Coral Cover
      compareColumns: [1, 2, 3, 4], // 4列布局
      compareChart: null,
      compareData: {},
      // 加载状态
      isLoading: false,
      loadingMessage: 'Loading...'
    };
  },
  
  async mounted() {
    console.log('组件挂载完成，开始初始化...');
    
    // 并行执行初始化和数据加载
    await Promise.all([
      this.$nextTick(() => {
        this.setupChart();
      }),
      this.loadAvailableIslands()
    ]);
  },
  
  methods: {
    async loadAvailableIslands() {
      this.isLoading = true;
      this.loadingMessage = 'Loading islands...';
      
      try {
        console.log('正在从后端加载岛屿列表...');
        const response = await axios.get('http://localhost:8080/trend/islands');
        console.log('岛屿列表API响应:', response);
        
        if (response.data.code === 1) {
          this.availableIslands = response.data.data;
          console.log('成功从后端加载岛屿列表:', this.availableIslands);
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
             const url = `http://localhost:8080/trend/query?island=${encodeURIComponent(island)}`;
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
        this.loadTrendData(this.selectedIsland);
      }
    },
    
    onSearchInput() {
      if (this.searchInput.trim()) {
        const matchingIsland = this.availableIslands.find(island => 
          island.toLowerCase().includes(this.searchInput.toLowerCase())
        );
        if (matchingIsland) {
          this.selectedIsland = matchingIsland;
          this.loadTrendData(matchingIsland);
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

    // 轮播图独立方法
    previousImage() {
      if (this.carouselImages.length === 0) return;
      this.currentImageIndex = this.currentImageIndex > 0 
        ? this.currentImageIndex - 1 
        : this.carouselImages.length - 1;
    },
    
    nextImage() {
      if (this.carouselImages.length === 0) return;
      this.currentImageIndex = this.currentImageIndex < this.carouselImages.length - 1 
        ? this.currentImageIndex + 1 
        : 0;
    },
    
    goToImage(index) {
      if (this.carouselImages.length === 0) return;
      this.currentImageIndex = index;
    },

    // 添加图片到轮播列表的方法（供后续使用）
    addCarouselImage(imagePath) {
      this.carouselImages.push(imagePath);
      console.log('添加图片到轮播:', imagePath);
    },

    // 移除轮播图片的方法
    removeCarouselImage(index) {
      if (index >= 0 && index < this.carouselImages.length) {
        this.carouselImages.splice(index, 1);
        // 调整当前索引
        if (this.currentImageIndex >= this.carouselImages.length) {
          this.currentImageIndex = Math.max(0, this.carouselImages.length - 1);
        }
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
        const response = await axios.post('http://localhost:8080/trend/compare', {
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
          const response = await axios.get(`http://localhost:8080/trend/query?island=${encodeURIComponent(island)}`);
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
        lcc: { label: 'Live Coral Cover (LCC)' },
        ot: { label: 'Other (OT)' },
        as: { label: 'Available Substrate (AS)' },
        sd: { label: 'Sand (SD)' },
        di: { label: 'Disturbance Indicator (DI)' },
        pi: { label: 'Pollution Indicator (PI)' }
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
              plugins: {
                legend: {
                  position: 'right'
                },
                title: {
                  display: true,
                  text: 'Coral Reef Trend Data Visualization'
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
          label: 'Live Coral Cover (LCC)',
          data: sortedData.map(item => item.lcc || 0),
          borderColor: 'rgb(75, 192, 192)',
          backgroundColor: 'rgba(75, 192, 192, 0.2)',
          tension: 0.1,
          fill: false
        },
        {
          label: 'Other (OT)',
          data: sortedData.map(item => item.ot || 0),
          borderColor: 'rgb(255, 99, 132)',
          backgroundColor: 'rgba(255, 99, 132, 0.2)',
          tension: 0.1,
          fill: false
        },
        {
          label: 'Pollution Indicator (PI)',
          data: sortedData.map(item => item.pi || 0),
          borderColor: 'rgb(54, 162, 235)',
          backgroundColor: 'rgba(54, 162, 235, 0.2)',
          tension: 0.1,
          fill: false
        },
        {
          label: 'Sand (SD)',
          data: sortedData.map(item => item.sd || 0),
          borderColor: 'rgb(255, 205, 86)',
          backgroundColor: 'rgba(255, 205, 86, 0.2)',
          tension: 0.1,
          fill: false
        },
        {
          label: 'Disturbance Indicator (DI)',
          data: sortedData.map(item => item.di || 0),
          borderColor: 'rgb(153, 102, 255)',
          backgroundColor: 'rgba(153, 102, 255, 0.2)',
          tension: 0.1,
          fill: false
        },
        {
          label: 'Available Substrate (AS)',
          data: sortedData.map(item => item.as || 0),
          borderColor: 'rgb(255, 159, 64)',
          backgroundColor: 'rgba(255, 159, 64, 0.2)',
          tension: 0.1,
          fill: false
        }
      ];
      
      this.chart.data.labels = labels;
      this.chart.data.datasets = datasets;
      this.chart.update();
      
      console.log('图表更新完成');
    }
  },

  watch: {
    selectedCompareAttribute: {
      handler: 'onAttributeFilterChange'
    }
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
  z-index: 5;
}

.control-item {
  display: flex;
  align-items: center;
}

.control-select,
.control-input {
  padding: 12px 30px;
  border: 1px solid #cbd5e0;
  border-radius: 10px;
  font-size: 14px;
  min-width: 150px;
  background: rgba(255, 255, 255, 0.84);
}

.control-select:hover,
.control-input:hover {
  box-shadow: 0 2px 6px rgba(255, 255, 255, 1);
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
  overflow: hidden;
  position: relative;
  z-index: 5;
}

/* 左侧面板 */
.left-panel {
  background: rgba(255, 255, 255, 0.2);
  padding: 24px;
  border-right: 1px solid rgba(226, 232, 240, 0.3);
}



.carousel-display {
  text-align: center;
}

.carousel-image-container {
  position: relative;
  width: 100%;
  height: 280px;
  margin-bottom: 16px;
  border-radius: 8px;
  overflow: hidden;
  background: #f7fafc;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f7fafc 0%, #edf2f7 100%);
  border: 2px dashed #cbd5e0;
}

.placeholder-content {
  text-align: center;
  color: #718096;
}

.placeholder-icon {
  font-size: 3rem;
  margin-bottom: 12px;
  opacity: 0.6;
}

.placeholder-content p {
  margin: 8px 0 4px 0;
  font-size: 16px;
  font-weight: 500;
}

.placeholder-content small {
  font-size: 12px;
  opacity: 0.8;
}

.carousel-navigation {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
}

.nav-arrow {
  background: #e2e8f0;
  border: none;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.nav-arrow:hover:not(:disabled) {
  background: #cbd5e0;
  transform: scale(1.05);
}

.nav-arrow:disabled {
  background: #f7fafc;
  color: #cbd5e0;
  cursor: not-allowed;
}

.carousel-indicators {
  display: flex;
  gap: 6px;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #cbd5e0;
  cursor: pointer;
  transition: all 0.2s;
}

.dot:hover {
  transform: scale(1.2);
}

.dot.active {
  background: #4299e1;
  transform: scale(1.3);
}

.dot.placeholder-dot {
  background: #e2e8f0;
  cursor: default;
}

.dot.placeholder-dot:hover {
  transform: none;
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
  margin-top: auto;
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
  
  .control-select,
  .control-input {
    min-width: 200px;
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
</style>

