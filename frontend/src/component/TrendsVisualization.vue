<template>
  <div class="trends-container bg-container">
    <!-- Background image loading placeholder -->
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
        <h1 class="logo" @click="goToHome">{{ $t('nav.logo') }}</h1>
      </div>
      <div class="nav-right">
        <div class="nav-items">
          <span class="nav-item active">{{ $t('nav.mapRecommendation.line1') }}</span>
          <div class="nav-item-dropdown" @mouseenter="showTravelDropdown = true" @mouseleave="showTravelDropdown = false">
            <span class="nav-item">{{ $t('nav.topIsland') }}</span>
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
          <div class="nav-item-dropdown" @mouseenter="showEducationDropdown = true" @mouseleave="showEducationDropdown = false">
            <span class="nav-item" @click="goToEducation">{{ $t('nav.education') }}</span>
            <div class="dropdown-menu" v-show="showEducationDropdown">
              <div class="dropdown-item" @click="goToEducation">
                <span>{{ $t('education.dropdown.tourismHub') }}</span>
              </div>
              <div class="dropdown-item" @click="goToTravelChecklist">
                <span>{{ $t('education.dropdown.tourismChecklist') }}</span>
              </div>
            </div>
          </div>
          <span class="nav-item" @click="goToAITools">{{ $t('nav.aiClassification') }}</span>
          <span class="nav-item" @click="goToGovernment">{{ $t('nav.government') }}</span>
          <span class="nav-item" @click="goToFAQ">{{ $t('nav.faq') }}</span>
          <LanguageSwitcher />
        </div>
      </div>
    </div>

    <!-- Main title -->
    <div class="page-title">
      <h2>{{ $t('map.title') }}</h2>
      <p class="data-source">{{ $t('map.dataSource') }}</p>
    </div>

    <!-- Loading indicator removed -->

    <!-- New Layout: Heat Map on top, controls and comparison below -->
    <div class="new-layout">
      <!-- Main heat map area -->
      <div class="heat-map-section">
        <!-- Left: Select Panel -->
        <div class="select-panel">
          <h3>{{ $t('map.selectPanel.title') }}</br></br></h3>
          
          <!-- Location Type Selection -->
          <div class="control-group">
            <label>{{ $t('map.selectPanel.filterByLocation') }}</label>
            <div class="combobox-container">
              <select 
                v-model="selectedLocationType"
                @change="onLocationTypeChange"
                class="location-type-select"
              >
                <option value="">{{ $t('map.selectPanel.selectType') }}</option>
                <option value="state">{{ $t('map.selectPanel.state') }}</option>
                <option value="island">{{ $t('map.selectPanel.island') }}</option>
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
            <label>{{ selectedLocationType === 'state' ? $t('map.selectPanel.selectState') : selectedLocationType === 'island' ? $t('map.selectPanel.selectIsland') : $t('map.selectPanel.selectLocation') }}</label>
            <div class="combobox-container">
              <input 
                type="text" 
                v-model="dynamicInputValue"
                @input="onDynamicInputChange"
                @focus="onDynamicInputFocus"
                @blur="onDynamicInputBlur"
                @keydown="onDynamicKeyDown"
                :placeholder="selectedLocationType === 'state' ? $t('map.selectPanel.typeState') : selectedLocationType === 'island' ? $t('map.selectPanel.typeIsland') : $t('map.selectPanel.selectFirst')"
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
                  {{ selectedLocationType === 'state' ? $t('map.selectPanel.noStates') : $t('map.selectPanel.noIslands') }}
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
                <span class="coord-label">{{ $t('map.coordinates') }}:</span>
                <span class="coord-values">{{ selectedCoordinates.lat.toFixed(4) }}°N, {{ selectedCoordinates.lng.toFixed(4) }}°E</span>
          </div>
              </div>
            <div class="map-instructions" v-if="!selectedIsland">
              <p>
                🎯 {{ $t('map.mapInstructions.useLeftPanel') }}<br>🗺 {{ $t('map.mapInstructions.clickMarkers') }}<br>⭐ {{ $t('map.mapInstructions.rightPanel') }}
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
          <h3>{{ $t('map.recommendedIslands.title') }}</h3>
          <p class="recommendation-subtitle">{{ $t('map.recommendedIslands.subtitle') }}</br></p>
          
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
            <p>{{ $t('map.recommendedIslands.clickToEnter') }}</p>
        </div>
      </div>
    </div>

      <!-- New Layout: Left Panel + Visualization Area -->
    <div class="comparison-section">
    
    <div class="comparison-bg-wrapper">
      <h2 class="comparison-title">{{ $t('map.comparison.title') }}</h2>
      <div class="comparison-layout-container">
        <!-- Left: Island Selection Panel -->
        <div class="external-island-selection-panel">
        <h3>{{ $t('map.comparison.panel.title') }}</h3>
        <p>{{ $t('map.comparison.panel.description') }}</p>
            <div class="island-selector">
              <label>{{ $t('map.comparison.panel.selectIsland') }}</label>
              <select v-model="compareIsland1" @change="updateComparisonChart" class="island-select">
                <option value="">{{ $t('map.comparison.panel.chooseIsland') }}</option>
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
              <label>{{ $t('map.comparison.panel.compareIsland') }}</label>
              <select v-model="compareIsland2" @change="updateComparisonChart" class="island-select">
                    <option value="">{{ $t('map.comparison.panel.chooseIsland') }}</option>
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
        <!-- Place Select Attribute here -->
          <div class="island-selector">
            <label>{{ $t('map.comparison.panel.selectAttribute') }}</label>
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
                <p>{{ $t('map.comparison.panel.placeholder') }}</p>
              </div>
            </div>
              </div>
            </div>
            <div class="info-panel">
            <h3>{{ $t('map.comparison.additionalInfo.title') }}</h3>
              <div class="info-bubble">
                {{ $t('map.comparison.additionalInfo.question') }}
              </div>

              <div class="info-bubble">
                {{ $t('map.comparison.additionalInfo.instruction') }}
              </div>
              

              <img :src="imageUrls.hand" alt="Info" class="hand-icon" loading="lazy" @mouseleave="hideTooltip" />
          </div>
          </div>
          </div>
        </div>

        <!-- All Indicators Section -->
<div class="all-indicators-section">
  <div class="section-header">
    <h2 class="section-title">{{ $t('map.indicators.title') }}</h2>
  </div>

  <div class="indicator-grid">

    <!-- LCC -->
    <div class="indicator-panel">
      <div class="panel-header">
        <h3 class="panel-title">{{ $t('map.indicators.lcc.title') }}</h3>
      </div>
      <div class="analysis-content">
        <p><span class="good">&gt;50%:</span> {{ $t('map.indicators.lcc.good') }}</p>
        <p><span class="moderate">30–50%:</span> {{ $t('map.indicators.lcc.moderate') }}</p>
        <p><span class="poor">&lt;30%:</span> {{ $t('map.indicators.lcc.poor') }}</p>
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

    <!-- Overview -->
    <div class="indicator-panel overview-panel">
      <div class="panel-header">
        <h3 class="panel-title">{{ $t('map.indicators.overview.title') }}</h3>
      </div>
      <div class="overview-content">
        <p class="overview-description">{{ $t('map.indicators.overview.description') }}</p>
      </div>
    </div>

    <!-- SD -->
    <div class="indicator-panel">
      <div class="panel-header">
        <h3 class="panel-title">{{ $t('map.indicators.sd.title') }}</h3>
      </div>
      <div class="analysis-content">
        <p><span class="good">5–15%:</span> {{ $t('map.indicators.sd.good') }}</p>
        <p><span class="poor">&gt;20%:</span> {{ $t('map.indicators.sd.poorHigh') }}</p>
        <p><span class="poor">&lt;5%:</span> {{ $t('map.indicators.sd.poorLow') }}</p>
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
        <h3 class="panel-title">{{ $t('map.indicators.di.title') }}</h3>
      </div>
      <div class="analysis-content">
        <p><span class="good">&lt;5%:</span> {{ $t('map.indicators.di.good') }}</p>
        <p><span class="moderate">5–10%:</span> {{ $t('map.indicators.di.moderate') }}</p>
        <p><span class="poor">&gt;10%:</span> {{ $t('map.indicators.di.poor') }}</p>
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
        <h3 class="panel-title">{{ $t('map.indicators.pi.title') }}</h3>
      </div>
      <div class="analysis-content">
        <p><span class="good">&lt;2%:</span> {{ $t('map.indicators.pi.good') }}</p>
        <p><span class="moderate">2–5%:</span> {{ $t('map.indicators.pi.moderate') }}</p>
        <p><span class="poor">&gt;5%:</span> {{ $t('map.indicators.pi.poor') }}</p>
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

    <!-- AS -->
    <div class="indicator-panel">
      <div class="panel-header">
        <h3 class="panel-title">{{ $t('map.indicators.as.title') }}</h3>
      </div>
      <div class="analysis-content">
        <p><span class="good">&lt;10%:</span> {{ $t('map.indicators.as.good') }}</p>
        <p><span class="moderate">10–25%:</span> {{ $t('map.indicators.as.moderate') }}</p>
        <p><span class="critical">&gt;40%:</span> {{ $t('map.indicators.as.critical') }}</p>
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

  </div>


</div>

             
        <!-- Tooltip -->
        <div class="tooltip" :style="tooltipStyle">
          {{ tooltipContent }}
        </div>

             
    </div>
    <footer class="main-footer">
      <div class="footer-content">
        {{ $t('footer.copyright') }}
        <span class="footer-links">
          <div class="contact-info">
            <div class="contact-item">
              <span class="contact-icon">🌐</span>
              <span class="contact-text">{{ $t('footer.website') }}</span>
            </div>
            <div class="contact-item">
              <span class="contact-icon">✉️</span>
              <span class="contact-text">{{ $t('footer.email') }}</span>
            </div>
          </div>
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

import LanguageSwitcher from './LanguageSwitcher.vue'

export default {
  name: 'TrendsVisualization',
  components: {
    LanguageSwitcher
  },
  data() {
    return {
      backgroundLoaded: false, // Background image loading status
      loadingProgress: 0, // Loading progress
      loadingText: 'Loading map data...', // Loading text
      backgroundImageUrl: null, // Background image URL
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
      previousValue: null, // Used to store previous value
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
      stateIslandsData: [], // Store state and island mapping data
      recommendedIslands: [],
      allIslandsLCCData: {}, // Store all islands LCC data
      allIslandsCoralData: {}, // Store all islands coral data (bleach data)
      // Loading status
      isLoadingCoordinates: false,
      isLoadingCoralData: false,
      // Comparison function related data (simplified to 2 islands)
      compareIsland1: '',
      compareIsland2: '',
      selectedCompareAttribute: 'lcc', // Default selection Live Coral Cover
      
      // Indicator selector data
      selectedIndicator: 'lcc',
      // OSS image URLs
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
      showEducationDropdown: false,
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
    // Get current selected indicator label
    selectedIndicatorLabel() {
      const option = this.indicatorOptions.find(opt => opt.value === this.selectedIndicator);
      return option ? option.label : 'Choose Indicator';
    },
    
    // Get low indicator image
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
    
    // Get high indicator image
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
    
    // Immediately start preloading background image
    this.preloadBackgroundImages();
    
    // Load background image
    this.loadBackgroundImage();
    
    // Also start preloading images to cache
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
    
    // Add global function for popup window use
    window.selectIslandFromPopup = (islandName) => {
      console.log('Global function called, island name:', islandName);
      this.selectIslandFromPopup(islandName);
    };
    
    // Add test function to global
    window.testNavigation = () => {
      console.log('Test navigation functionality');
      this.$router.push('/education').then(() => {
        console.log('Test navigation successful');
      }).catch(err => {
        console.error('Test navigation failed:', err);
      });
    };
    
    // Add window event listeners
    window.addEventListener('resize', this.resizeCanvas);
    document.addEventListener('click', this.handleOutsideClick);
    

  },
  
  methods: {
    /**
     * Load background image
     */
    async loadBackgroundImage() {
      try {
        this.backgroundImageUrl = await ossService.getFileUrl('bg_trend.webp')
        // Set CSS variable
        document.documentElement.style.setProperty('--bg-image', `url(${this.backgroundImageUrl})`)
      } catch (error) {
        console.warn('Failed to load background image, using default image:', error)
        this.backgroundImageUrl = null
      }
    },

    /**
     * Preload background image and preconnect to OSS service
     */
    preloadBackgroundImages() {
      // Preconnect to OSS service
      const preconnectLink = document.createElement('link');
      preconnectLink.rel = 'preconnect';
      preconnectLink.href = 'https://coralkita.oss-ap-southeast-3.aliyuncs.com';
      preconnectLink.crossOrigin = 'anonymous';
      
      // Add to head
      document.head.appendChild(preconnectLink);
      
      // Create high priority preload link element
      const preloadLink = document.createElement('link');
      preloadLink.rel = 'preload';
      preloadLink.as = 'image';
      preloadLink.href = this.backgroundImageUrl;
      preloadLink.fetchPriority = 'high'; // High priority
      
      // Add to head
      document.head.appendChild(preloadLink);
      
      // Preload images to browser cache
      this.preloadImageToCache();
      
      console.log('OSS preconnect and high-priority background image preload links added');
    },

    /**
     * Preload images to browser cache
     */
    preloadImageToCache() {
      const img = new Image();
      img.src = this.backgroundImageUrl;
      
      // Simulate loading progress
      this.simulateLoadingProgress();
      
      // Set timeout mechanism to prevent infinite loading
      const timeout = setTimeout(() => {
        console.warn('Background image loading timeout');
        this.loadingProgress = 100;
        this.loadingText = 'Using backup data...';
        setTimeout(() => {
          this.backgroundLoaded = true;
        }, 500);
      }, 10000); // 10 second timeout
      
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
          this.backgroundLoaded = true; // Hide placeholder even if failed
        }, 500);
      };
    },

    /**
     * Simulate loading progress
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
     * Load all OSS image URLs
     */
    async loadOssImages() {
      try {
        console.log('Starting to load OSS images...')
        
        // Load all image URLs in parallel
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

        // Update image URLs
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

        console.log('OSS images loaded successfully:', this.imageUrls)
        console.log('LCC20 image URL:', this.imageUrls.lcc20)
        console.log('LCC100 image URL:', this.imageUrls.lcc100)
      } catch (error) {
        console.error('Failed to load OSS images:', error)
        // If OSS loading fails, use local images as fallback
        this.loadFallbackImages()
      }
    },

    /**
     * Load fallback local images
     */
    loadFallbackImages() {
      console.log('Using local images as fallback...')
      this.imageUrls = {
        appIcon: require('@/assets/icon.png'),
        crown1: require('@/assets/icons/icon_crown1.png'),
        crown2: require('@/assets/icons/icon_crown2.png'),
        crown3: require('@/assets/icons/icon_crown3.png'),
        crown4: require('@/assets/icons/icon_crown4.png'),
        crown5: require('@/assets/icons/icon_crown5.png'),
        coral: require('@/assets/icons/coral.png'),
        hand: require('@/assets/icons/hand.png'),
        lcc20: require('@/assets/LCC_20pct.png'),
        lcc100: require('@/assets/LCC_100pct.png'),
        as80: require('@/assets/AS_80pct.png'),
        as10: require('@/assets/AS_10pct.png'),
        sd5: require('@/assets/SD_5pct.png'),
        sd50: require('@/assets/SD_50pct.png'),
        di30: require('@/assets/DI_30pct.png'),
        di5: require('@/assets/DI_5pct.png'),
        pi50: require('@/assets/PI_50pct.png'),
        pi1: require('@/assets/PI_1pct.png')
      }
      console.log('Fallback images loaded successfully:', this.imageUrls)
      console.log('Fallback LCC20 image URL:', this.imageUrls.lcc20)
      console.log('Fallback LCC100 image URL:', this.imageUrls.lcc100)
    },

    async loadAvailableIslands() {
      this.isLoading = true;
      this.loadingMessage = 'Loading islands...';
      
      try {
        console.log('Loading island list from backend...');
        const response = await axios.get(`${API_BASE_URL}/trend/islands`);
        console.log('Island list API response:', response);
        
        if (response.data.code === 1) {
          this.availableIslands = response.data.data;
          this.filteredIslands = [...this.availableIslands]; // Initialize filtered list
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
        // If API call fails, show error message
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
          // Extract all unique state names
          const statesSet = new Set();
          this.stateIslandsData.forEach(item => {
            if (item.state) {
              statesSet.add(item.state);
            }
          });
          this.availableStates = Array.from(statesSet).sort();
          this.filteredStates = [...this.availableStates]; // Initialize filtered list
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

    // New location type selection related methods
    onLocationTypeChange() {
      console.log('位置类型改变为:', this.selectedLocationType);
      
      // Clear dynamic input field values
      this.dynamicInputValue = '';
      this.showDynamicDropdown = false;
      
      // Update available options based on selected type
      this.updateDynamicOptions();
      
      // Clear previous selection
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
      // When focus enters, if there is already a selection, clear input field to let user reselect
      if (this.dynamicInputValue) {
        // Temporarily clear input field to show all options
        const currentValue = this.dynamicInputValue;
        this.dynamicInputValue = '';
        this.showAllDynamicOptions();
        // If user loses focus without entering anything, restore previous value
        this.previousValue = currentValue;
      } else {
        this.showAllDynamicOptions();
      }
    },

    onDynamicInputBlur() {
      console.log('动态输入框失去焦点');
      setTimeout(() => {
        this.showDynamicDropdown = false;
        // If user has not selected any option and input field is empty, restore previous value
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
        // Show all options instead of filtered options
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
      this.previousValue = null; // Clear previous value as user has selected new value
      
      if (this.selectedLocationType === 'state') {
        this.selectedState = option;
        this.stateInputValue = option;
        // Call original state selection logic
        this.filterIslandsByState(option);
      } else if (this.selectedLocationType === 'island') {
        this.selectedIsland = option;
        this.islandInputValue = option;
        // Call original island selection logic
        this.onIslandChange();
      }
      
      // Lose focus after selection so next click will show all options again
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
          // Handle date format, convert array format to Date object
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
        // Clear data and show error
        this.trendData = [];
        alert(`无法加载岛屿 "${island}" 的数据，请检查后端服务是否正常运行`);
      }
    },

    // Batch load coral data (bleach data) for all islands
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
        // Automatically update state selection
        this.updateStateSelectionFromIsland(this.selectedIsland);
        
        // Handle through map selection function to ensure map synchronization
        this.selectIslandFromMap(this.selectedIsland);
        } else {
        // If island selection is cancelled, reset map view
        if (this.map) {
          this.map.setView([4.2105, 103.7751], 6); // Return to Malaysia center view
        }
        this.selectedCoordinates = null;
        
        // Clear input field
        this.islandInputValue = '';
        this.filterIslands(); // Re-filter to show all islands
        
        // Clear state selection
        this.selectedState = '';
        this.stateInputValue = '';
      }
    },

    // Automatically update state selection based on selected island
    updateStateSelectionFromIsland(islandName) {
      console.log('根据岛屿自动更新州选择:', islandName);
      
      // Get state corresponding to island
      const islandState = this.getIslandState(islandName);
      
      if (islandState && islandState !== 'Malaysia') {
        // Check if the state is in the available states list
        if (this.availableStates.includes(islandState)) {
          console.log(`自动选择州: ${islandState}`);
          this.selectedState = islandState;
          this.stateInputValue = islandState; // Also update input field value
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
      // When focused, show all states without filtering
      this.filteredStates = [...this.availableStates];
      console.log('焦点获得，显示所有州数量:', this.filteredStates.length);
    },

    onStateInputBlur() {
      console.log('州输入框失去焦点');
      // Delay hiding dropdown to give click event time to execute
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
      
      // Update input field and selected state
      this.stateInputValue = state;
      this.selectedState = state;
      
      // Immediately hide dropdown
      this.showStateDropdown = false;
      
      // Remove input field focus
      if (this.$refs.stateInput) {
        this.$refs.stateInput.blur();
      }
      
      // Trigger state selection change
      this.onStateChange();
    },

    toggleStateDropdown() {
      console.log('切换州下拉框状态:', this.showStateDropdown, '->', !this.showStateDropdown);
      this.showStateDropdown = !this.showStateDropdown;
      if (this.showStateDropdown) {
        // When opening dropdown, show all states (ignore current input content)
        this.filteredStates = [...this.availableStates];
        console.log('显示州下拉框，显示所有州数量:', this.filteredStates.length);
        console.log('可用州总数:', this.availableStates.length);
        
        // Ensure input field gets focus
        this.$nextTick(() => {
          if (this.$refs.stateInput) {
            this.$refs.stateInput.focus();
          }
        });
      }
    },

    onStateKeyDown(event) {
      if (event.key === 'Enter') {
        // If there are filtered results, select the first one
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
      // When focused, show all islands without filtering
      this.filteredIslands = [...this.availableIslands];
      console.log('焦点获得，显示所有岛屿数量:', this.filteredIslands.length);
    },

    onInputBlur() {
      console.log('输入框失去焦点');
      // Delay hiding dropdown to give click event time to execute
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
      
      // Update input field and selected state
      this.islandInputValue = island;
      this.selectedIsland = island;
      
      // Immediately hide dropdown
      this.showDropdown = false;
      
      // Remove input field focus
      if (this.$refs.islandInput) {
        this.$refs.islandInput.blur();
      }
      
      // Trigger island selection change
      this.onIslandChange();
    },

    toggleDropdown() {
      console.log('切换下拉框状态:', this.showDropdown, '->', !this.showDropdown);
      this.showDropdown = !this.showDropdown;
      if (this.showDropdown) {
        // When opening dropdown, show all islands (ignore current input content)
        this.filteredIslands = [...this.availableIslands];
        console.log('显示下拉框，显示所有岛屿数量:', this.filteredIslands.length);
        console.log('可用岛屿总数:', this.availableIslands.length);
        
        // Ensure input field gets focus
        this.$nextTick(() => {
          if (this.$refs.islandInput) {
            this.$refs.islandInput.focus();
          }
        });
      }
    },


    onKeyDown(event) {
      if (event.key === 'Enter') {
        // If there are filtered results, select the first one
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

    // Initialize Leaflet map
    initializeLeafletMap() {
      this.$nextTick(() => {
        if (this.map) {
          this.map.remove(); // Clean up existing map
        }
        
        // Create map instance, focus on Malaysia
        this.map = L.map('leaflet-map', {
          center: [4.2105, 103.7751], // Malaysia center point
          zoom: 6,
          minZoom: 5,
          maxZoom: 12,
          zoomControl: true,
          attributionControl: true
        });
        
        // Add beautiful map layers
        this.addMapLayers();
        
        // Add health level legend
        this.addHealthLegend();
        
        console.log('Leaflet地图初始化完成');
      });
    },
    
    // Add map layers
    addMapLayers() {
      // Only use one base map layer
      const cartoLayer = L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png', {
        attribution: '© CARTO © OpenStreetMap contributors',
        maxZoom: 19
      });
      
      // Add to map
      cartoLayer.addTo(this.map);
    },

    // Add health level legend
    addHealthLegend() {
      if (!this.map) return;
      
      // Create legend control
      const legend = L.control({ position: 'bottomleft' });
      
        legend.onAdd = () => {
          const div = L.DomUtil.create('div', 'map-legend');
          div.innerHTML = `
            <h4>{{ $t('map.legend.liveCoralCover') }}</h4>
            <p>{{ $t('map.legend.averageLCC') }}</p>
            <div><span style="background:#00b050"></span> {{ $t('map.legend.good') }}</div>
            <div><span style="background:#ffc000"></span> {{ $t('map.legend.fair') }}</div>
            <div><span style="background:#ff0000"></span> {{ $t('map.legend.poor') }}</div>
          `;
        
        // Prevent legend from being dragged
        L.DomEvent.disableClickPropagation(div);
        L.DomEvent.disableScrollPropagation(div);
        
        return div;
      };
      
      legend.addTo(this.map);
    },
    
    // Load island coordinate data
    async loadIslandCoordinates() {
      if (this.availableIslands.length === 0) {
        console.log('没有可用岛屿，跳过坐标加载');
        return;
      }
      
      this.isLoadingCoordinates = true;
      try {
        console.log('开始批量加载岛屿坐标数据，岛屿列表:', this.availableIslands);
        
        // Prefer batch coordinate API
        try {
          console.log('使用批量坐标API获取所有岛屿坐标...');
          const response = await axios.post(`${API_BASE_URL}/trend/bleach/coordinates`, this.availableIslands);
          console.log('批量坐标API响应:', response.data);
          
          if (response.data.code === 1) {
            const coordinatesMap = response.data.data;
            console.log('成功获取批量坐标数据:', Object.keys(coordinatesMap).length, '个岛屿');
            
            // Convert to array format
            this.islandLocations = Object.entries(coordinatesMap).map(([island, coords]) => ({
              island,
              lat: coords.lat,
              lng: coords.lng,
              hasData: coords.hasData
            }));
            
            console.log('批量加载的岛屿坐标:', this.islandLocations);
            
            // Add markers on map
            if (this.islandLocations.length > 0) {
              this.updateClusterMap();
            } else {
              console.error('批量API没有返回任何坐标数据');
            }
            
            // If there is already a selected island, immediately position
            if (this.selectedIsland) {
              console.log('检测到预选岛屿:', this.selectedIsland);
              setTimeout(() => {
                this.selectIslandFromMap(this.selectedIsland);
              }, 1000);
            }
            
            return; // Successfully got batch data, return directly
          } else {
            console.error('批量坐标API返回错误:', response.data.msg);
          }
        } catch (batchError) {
          console.error('批量坐标API请求失败:', batchError);
        }
        
        // If batch API fails, fallback to using cached batch coral data
        let allCoordinates = {};
        if (this.allIslandsCoralData && Object.keys(this.allIslandsCoralData).length > 0) {
          console.log('批量API失败，使用缓存的批量珊瑚数据获取坐标');
          for (const island of this.availableIslands) {
            if (this.allIslandsCoralData[island] && this.allIslandsCoralData[island].length > 0) {
              const coralData = this.allIslandsCoralData[island][0]; // Take first data to get coordinates
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
          // If cached data is also unavailable, fallback to individual requests
          console.log('没有批量数据，回退到逐个请求');
          for (const island of this.availableIslands) {
            try {
              console.log(`正在获取岛屿 ${island} 的坐标...`);
              const response = await axios.get(`${API_BASE_URL}/trend/bleach?island=${encodeURIComponent(island)}`);
              console.log(`岛屿 ${island} 的响应:`, response.data);
              
              if (response.data.code === 1 && response.data.data && response.data.data.length > 0) {
                const coralData = response.data.data[0]; // Take first data to get coordinates
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
        
        // Convert to array format
        this.islandLocations = Object.entries(allCoordinates).map(([island, coords]) => ({
          island,
          lat: coords.lat,
          lng: coords.lng,
          hasData: coords.hasData
        }));
        
        console.log('最终加载的岛屿坐标:', this.islandLocations);
        
        // Add markers on map
        if (this.islandLocations.length > 0) {
          this.updateClusterMap();
        } else {
          console.error('没有加载到任何岛屿坐标数据');
        }
        
        // If there is already a selected island, immediately position
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
    
    // Add island markers on map
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
      
      // Clear existing markers
      this.markerLayers.forEach(marker => this.map.removeLayer(marker));
      this.markerLayers = [];
      
      // Create custom icon
      const createCustomIcon = (hasData, isSelected = false, lccValue = 0) => {
        // Determine color based on LCC value (consistent with legend)
        let color = '#6b7280'; // Default gray
        if (lccValue >= 0.7) color = '#0066cc'; // Blue - Excellent
        else if (lccValue >= 0.5) color = '#00b050'; // Green - Good
        else if (lccValue >= 0.3) color = '#ffc000'; // Orange - Fair
        else if (lccValue > 0) color = '#ff0000'; // Red - Poor
        
        if (isSelected) color = '#dc2626'; // Show red when selected
        
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
      
      // Add markers for each island
      this.islandLocations.forEach(location => {
        // Get LCC value for this island from complete LCC data
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
            zIndexOffset: 1000 // Ensure markers are above heatmap
          }
        );
        
        // Create popup content
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
              ${location.hasData ? `✅ ${this.$t('map.popup.lcc')} ${lccPercentage}%` : `❌ ${this.$t('map.popup.noData')}`}
            </div>
            ${isCurrentlySelected ? 
              `<div style="background: #fee2e2; color: #dc2626; padding: 4px 8px; border-radius: 8px; font-size: 12px; font-weight: bold; margin-bottom: 8px;">🎯 ${this.$t('map.popup.currentlySelected')}</div>` : 
              this.getSelectButtonHtml(location.island)
            }
          </div>
        `;
        
        marker.bindPopup(popupContent, {
          maxWidth: 250,
          className: 'custom-popup'
        });
        
        // Click event
        marker.on('click', () => {
          this.selectIslandFromMap(location.island);
        });
        
        marker.addTo(this.map);
        this.markerLayers.push(marker);
      });
      
      // Global function already defined in mounted, no need to redefine here
    },
    
    // Select island from map
    selectIslandFromMap(island) {
      console.log('从地图选择岛屿:', island);
      this.selectedIsland = island;
      
      // Update selected island coordinates
      const location = this.islandLocations.find(loc => loc.island === island);
      if (location && this.map) {
        this.selectedCoordinates = {
          lat: location.lat,
          lng: location.lng
        };
        
        console.log(`定位到岛屿 ${island}:`, location.lat, location.lng);
        
        // Map flies to selected position and zooms in
        this.map.flyTo([location.lat, location.lng], 10, {
          duration: 2,
          easeLinearity: 0.3
        });
        
        // Delay updating marker style to ensure map animation starts before updating
        setTimeout(() => {
          this.updateMarkerStyles(island);
        }, 100);
        
        // Open popup for selected island
        setTimeout(() => {
          this.openSelectedIslandPopup(island);
        }, 2000);
      } else if (!this.map) {
        console.warn('地图尚未初始化');
        // If map not initialized yet, save selection state for later processing
        this.selectedCoordinates = location ? {
          lat: location.lat,
          lng: location.lng
        } : null;
      }
      
      // Load trend data
      this.loadTrendData(island);
      
      // Update marker style
      this.updateMarkerStyles(island);
    },
    
    // Update marker style (clustered map version)
    updateMarkerStyles(selectedIsland) {
      // In clustered map, we need to recreate the entire cluster group to update marker style
      if (this.markerClusterGroup && this.map) {
        // Recreate clustered map to reflect selected state
        this.updateClusterMap();
      }
    },
    
    // Open popup for selected island
    openSelectedIslandPopup(selectedIsland) {
      if (!this.map || !this.markerClusterGroup) return;
      
      // Find corresponding marker in cluster group
      this.markerClusterGroup.eachLayer(layer => {
        if (layer.options && layer.options.islandName === selectedIsland) {
          // If marker is in cluster, expand cluster first
          this.markerClusterGroup.zoomToShowLayer(layer, () => {
            layer.openPopup();
          });
          return;
        }
      });
      
      // If not found in cluster group, try searching in markerLayers
      const markerIndex = this.islandLocations.findIndex(loc => loc.island === selectedIsland);
      if (markerIndex >= 0 && this.markerLayers[markerIndex]) {
        const marker = this.markerLayers[markerIndex];
        marker.openPopup();
      }
    },
    
    compareIslands() {
      // Implement island comparison functionality
      console.log('岛屿比较功能待实现');
    },

    // Return to homepage
    goToHome() {
      // Can use Vue Router to navigate to homepage
      // this.$router.push('/');
      // Or directly jump to root path
      window.location.href = '/';
    },

    // Navigation methods

    goToEducation() {
      console.log('导航到Education页面');
      // Main page navigation, no flag set, should show verification
      this.$router.push('/education').catch(err => {
        // Ignore navigation duplicated error
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },
    
    goToTravelChecklist() {
      console.log('Navigate to Travel Checklist page');
      this.$router.push('/travel-checklist').catch(err => {
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

    goToActionHub() {
      console.log('Navigate to Action Hub page');
      this.$router.push('/action-hub').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToPolicies() {
      console.log('Navigate to Policies page');
      this.$router.push('/policies').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToFAQ() {
      console.log('Navigate to FAQ page');
      this.$router.push('/faq').catch(err => {
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
      // Immediately close dropdown but do not prevent event
      this.showTravelDropdown = false;
      
      // Use nextTick to ensure state update is complete
      this.$nextTick(() => {
        this.$router.push(`/travel/${encodeURIComponent(islandName)}`).catch(err => {
          // Ignore navigation duplicated error
          if (err.name !== 'NavigationDuplicated') {
            console.error('Navigation error:', err);
          }
        });
      });
    },

    // Comparison functionality methods
    openCompareModal() {
      console.log('打开对比弹窗');
      console.log('当前可用岛屿列表:', this.availableIslands);
      console.log('岛屿总数:', this.availableIslands.length);
      
      this.showCompareModal = true;
      this.selectedCompareIslands = ['', '', '', '']; // Reset to 4 empty selections
      this.selectedCompareAttribute = 'lcc'; // Default select Live Coral Cover
      
      // Wait for DOM update then initialize comparison chart canvas
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

    // Handle dropdown selection of islands
    async onCompareIslandSelect(columnIndex, islandName) {
      console.log(`列 ${columnIndex + 1} 选择岛屿:`, islandName);
      
      // Update selected islands
      this.$set(this.selectedCompareIslands, columnIndex, islandName);
      
      // Get actually selected island list (filter empty values)
      const selectedIslands = this.selectedCompareIslands.filter(island => island !== '');
      console.log('当前选中的岛屿:', selectedIslands);
      
      if (selectedIslands.length > 0) {
        await this.loadCompareData();
        this.$nextTick(() => {
          this.updateCompareChart();
        });
      } else {
        // Clear chart
        if (this.compareChart) {
          this.compareChart.data.labels = [];
          this.compareChart.data.datasets = [];
          this.compareChart.options.plugins.title.text = 'Please select islands to compare';
          this.compareChart.update();
        }
      }
    },

    // Remove selected island
    async removeCompareIsland(columnIndex) {
      console.log(`移除列 ${columnIndex + 1} 的岛屿`);
      this.$set(this.selectedCompareIslands, columnIndex, '');
      
      // Get actually selected island list (filter empty values)
      const selectedIslands = this.selectedCompareIslands.filter(island => island !== '');
      console.log('移除后选中的岛屿:', selectedIslands);
      
      if (selectedIslands.length > 0) {
        await this.loadCompareData();
        this.$nextTick(() => {
          this.updateCompareChart();
        });
      } else {
        // Clear chart
        if (this.compareChart) {
          this.compareChart.data.labels = [];
          this.compareChart.data.datasets = [];
          this.compareChart.options.plugins.title.text = 'Please select islands to compare';
          this.compareChart.update();
        }
      }
    },

    // Listen to island selection changes (kept for attribute filter changes)
    async onCompareIslandsChange() {
      const selectedIslands = this.selectedCompareIslands.filter(island => island !== '');
      console.log('选中的对比岛屿:', selectedIslands);
      
      if (selectedIslands.length > 0) {
        await this.loadCompareData();
        this.$nextTick(() => {
          this.updateCompareChart();
        });
      } else {
        // Clear chart
        if (this.compareChart) {
          this.compareChart.data.labels = [];
          this.compareChart.data.datasets = [];
          this.compareChart.options.plugins.title.text = 'Please select islands to compare';
          this.compareChart.update();
        }
      }
    },

    // Listen to attribute filter changes
    onAttributeFilterChange() {
      console.log('选中的对比属性:', this.selectedCompareAttribute);
      const selectedIslands = this.selectedCompareIslands.filter(island => island !== '');
      if (selectedIslands.length > 0) {
        this.updateCompareChart();
      }
    },

    // Load comparison data
    async loadCompareData() {
      console.log('加载对比数据...');
      this.compareData = {};
      
      // Get actually selected island list (filter empty values)
      const selectedIslands = this.selectedCompareIslands.filter(island => island !== '');
      
      if (selectedIslands.length === 0) {
        console.log('没有选中的岛屿');
        return;
      }
      
      try {
        // Use backend batch comparison interface
        const response = await axios.post(`${API_BASE_URL}/trend/compare`, {
          islands: selectedIslands
        });
        
        console.log('对比数据响应:', response.data);
        
        if (response.data && response.data.code === 1 && response.data.data) {
          // Backend returns TrendCompareVO list, grouped by indicator
          const compareResult = response.data.data;
          console.log('原始对比数据:', compareResult);
          
          // Initialize island data structure
          selectedIslands.forEach(island => {
            this.compareData[island] = {};
          });
          
          // Process data for each indicator
          compareResult.forEach(indicatorData => {
            const indicator = indicatorData.indicator.toLowerCase(); // Convert to lowercase to match frontend
            const islandData = indicatorData.islandData;
            const islandDates = indicatorData.islandDates;
            
            // Process data for this indicator for each island
            Object.keys(islandData).forEach(island => {
              if (selectedIslands.includes(island)) {
                const values = islandData[island];
                const dates = islandDates[island];
                
                // Create data item for each time point
                values.forEach((value, index) => {
                  const dateStr = dates[index];
                  
                  // If data item for this time point does not exist, create it
                  if (!this.compareData[island][dateStr]) {
                    // Parse date string (format: YYYY-MM-DD)
                    const dateParts = dateStr.split('-');
                    this.compareData[island][dateStr] = {
                      date: [parseInt(dateParts[0]), parseInt(dateParts[1]), parseInt(dateParts[2])],
                      lcc: 0, ot: 0, pi: 0, sd: 0, di: 0, as: 0
                    };
                  }
                  
                  // Set value for this indicator
                  this.compareData[island][dateStr][indicator] = value || 0;
                });
              }
            });
          });
          
          // Convert to array format
          Object.keys(this.compareData).forEach(island => {
            const dateData = this.compareData[island];
            this.compareData[island] = Object.values(dateData);
          });
          
          console.log('转换后的对比数据:', this.compareData);
        } else {
          console.warn('对比数据格式不正确:', response.data);
          // If batch interface fails, fallback to individual requests
          await this.loadCompareDataFallback();
        }
      } catch (error) {
        console.error('批量加载对比数据失败:', error);
        // If batch interface fails, fallback to individual requests
        await this.loadCompareDataFallback();
      }
    },

    // Fallback method: individually get data for each island
    async loadCompareDataFallback() {
      console.log('使用回退方法加载对比数据...');
      this.compareData = {};
      
      // Get actually selected island list (filter empty values)
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

    // Initialize comparison chart canvas
    setupCompareChart() {
      console.log('初始化对比图表canvas...');
      
      if (!this.$refs.compareChart) {
        console.warn('对比图表canvas引用未找到');
        return;
      }

      // Destroy existing chart
      if (this.compareChart) {
        this.compareChart.destroy();
        this.compareChart = null;
      }

      try {
        // Create empty chart instance
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

    // Update comparison chart
    updateCompareChart() {
      console.log('更新对比图表...');
      
      if (!this.compareChart) {
        console.warn('对比图表实例未初始化');
        this.setupCompareChart();
        return;
      }

      // Check if there is data
      if (!this.compareData || Object.keys(this.compareData).length === 0) {
        console.warn('没有对比数据可显示');
        return;
      }

      // Prepare chart data
      const datasets = [];
      const labels = [];
      
      // Get all time points
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
      
      // Color configuration
      const colors = [
        'rgba(99, 179, 237, 0.8)',
        'rgba(255, 99, 132, 0.8)', 
        'rgba(75, 192, 192, 0.8)',
        'rgba(255, 206, 86, 0.8)'
      ];

              // Property configuration
        const attributeConfig = {
          lcc: { label: 'LCC', description: 'Live Coral Cover - 活珊瑚覆盖率，反映珊瑚礁的健康状况和生态完整性' },
          as: { label: 'AS', description: 'Available Substrate - 可用基质，为珊瑚生长提供基础支撑的硬质表面' },
          sd: { label: 'SD', description: 'Sand - 沙质底质，影响珊瑚幼虫附着和生态系统稳定性' },
          di: { label: 'DI', description: 'Disturbance Indicator - 干扰指标，衡量人类活动对珊瑚礁的负面影响程度' },
          pi: { label: 'PI', description: 'Pollution Indicator - 污染指标，反映水质污染对珊瑚礁的威胁水平' },
          ot: { label: 'OT', description: 'Other - 其他成分，包括藻类、海绵等非珊瑚生物群落' }
        };

      // Show single attribute, one line per island
      Object.keys(this.compareData).forEach((island, index) => {
        const data = this.compareData[island];
        const values = sortedDates.map(date => {
          const item = data.find(d => `${d.date[0]}` === date);
          return item ? item[this.selectedCompareAttribute] : null;
        });

        datasets.push({
          label: island, // Only show island name
          data: values,
          borderColor: colors[index % colors.length],
          backgroundColor: colors[index % colors.length].replace('0.8', '0.2'),
          fill: false,
          tension: 0.4
        });
      });

      // Update chart data
      this.compareChart.data.labels = sortedDates;
      this.compareChart.data.datasets = datasets;
      
      // Update chart title
      this.compareChart.options.plugins.title.text = `Island Comparison - ${attributeConfig[this.selectedCompareAttribute]?.label || ''}`;
      
      // Refresh chart
      this.compareChart.update();

      console.log('对比图表更新完成');
    },
    


    getIslandDescription(island) {
      // Generate description based on island names, or can get from backend
      return `${island} - 拥有丰富海洋生物多样性的美丽岛屿，珊瑚礁生态系统独特。`;
    },
    
    setupChart() {
      this.$nextTick(() => {
        const ctx = this.$refs.trendChart;
        console.log('设置图表，Canvas元素:', ctx);
        
        if (ctx) {
          // If chart already exists, destroy it first
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
                      // Create or update hover tooltip
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
                      
                      // Display on right middle
                      tooltip.style.right = '-10px';
                      tooltip.style.top = '50%';
                      tooltip.style.transform = 'translateY(-50%)';
                      tooltip.style.display = 'block';
                    }
                  },
                  onLeave: function(e, legendItem, legend) {
                    // Hide hover tooltip
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
      
      // Prepare data - sort by time (old to new)
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
    
    // Calculate average value (data is 0-1 decimal, need to convert to percentage)
    getAverageValue(data) {
      if (!data || data.length === 0) return 0;
      const sum = data.reduce((acc, val) => acc + val, 0);
      const average = sum / data.length;
      return (average * 100).toFixed(1);
    },
    
    // Get color based on indicator type and value
    getValueColor(metricName, value) {
      const percentage = value * 100;
      
      switch (metricName) {
        case 'LCC': // High = Good
          if (percentage >= 50) return '#28a745'; // Green
          if (percentage >= 25) return '#ffc107'; // Yellow
          return '#dc3545'; // Red
          
        case 'AS': // Low = Good
          if (percentage < 20) return '#28a745'; // Green
          if (percentage <= 40) return '#ffc107'; // Yellow
          return '#dc3545'; // Red
          
        case 'SD': // Moderate is best
          if (percentage >= 5 && percentage <= 20) return '#28a745'; // Green
          if ((percentage >= 20 && percentage <= 30) || (percentage >= 1 && percentage < 5)) return '#ffc107'; // Yellow
          return '#dc3545'; // Red
          
        case 'DI': // Low = Good
          if (percentage < 5) return '#28a745'; // Green
          if (percentage <= 10) return '#ffc107'; // Yellow
          return '#dc3545'; // Red
          
        case 'PI': // Low = Good
          if (percentage < 3) return '#28a745'; // Green
          if (percentage <= 8) return '#ffc107'; // Yellow
          return '#dc3545'; // Red
          
        case 'OT': // Neutral category
          return '#6c757d'; // Gray
          
        default:
          return '#333'; // Default black
      }
    },

    // New method: initialize heatmap
    initializeLeafletHeatMap() {
      this.$nextTick(() => {
        if (this.map) {
          this.map.remove(); // Clean up existing map
        }
        
        // Create map instance, focus on Malaysia
        this.map = L.map('leaflet-map', {
          center: [4.2105, 103.7751], // Malaysia center point
          zoom: 6,
          minZoom: 5,
          maxZoom: 12,
          zoomControl: true,
          attributionControl: true
        });
        
        // Add map layers
        this.addMapLayers();
        
        // Add health level legend
        this.addHealthLegend();
        
        console.log('Leaflet热力图初始化完成');
      });
    },

    // Load recommended island list (based on LCC indicator)
    async loadRecommendedIslands() {
      if (this.availableIslands.length === 0) {
        console.log('没有可用岛屿，跳过推荐列表加载');
        return;
      }
      
      this.isLoading = true;
      this.loadingMessage = 'Loading recommended islands...';
      
      try {
        console.log('正在加载推荐岛屿列表...');
        
        // Use batch API to get trend data for all islands
        console.log('使用批量API获取所有岛屿趋势数据...');
        const response = await axios.get(`${API_BASE_URL}/trend/query/all`);
        console.log('批量趋势数据响应:', response.data);
        
        if (response.data.code === 1) {
          const allIslandsTrendData = response.data.data;
          console.log('成功获取所有岛屿趋势数据:', Object.keys(allIslandsTrendData).length, '个岛屿');
          
          // Process batch data, extract LCC information
          const allIslandsData = {};
          Object.entries(allIslandsTrendData).forEach(([island, trendData]) => {
            if (trendData && trendData.length > 0) {
              // Only get latest year LCC data (data already sorted by date descending)
              const latestData = trendData[0]; // First data is the latest
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
          
          // Save LCC data for all islands
          this.allIslandsLCCData = allIslandsData;
          console.log('所有岛屿LCC数据:', this.allIslandsLCCData);
        } else {
          console.error('批量加载趋势数据失败:', response.data.msg);
          this.recommendedIslands = [];
          return;
        }
        
        // Sort by LCC value, take top 5 as recommendations
        this.recommendedIslands = Object.values(this.allIslandsLCCData)
          .filter(island => island.lcc > 0) // Only include islands with data
          .sort((a, b) => b.lcc - a.lcc)
          .slice(0, 5);
        
        console.log('推荐岛屿列表:', this.recommendedIslands);
        
        // Update heatmap
        this.updateClusterMap();
        
      } catch (error) {
        console.error('加载推荐岛屿失败:', error);
        this.recommendedIslands = [];
      } finally {
        this.isLoading = false;
      }
    },

    // Update clustered map
    updateClusterMap() {
      if (!this.map || this.islandLocations.length === 0) return;
      
      // Remove existing cluster layer
      if (this.markerClusterGroup) {
        this.map.removeLayer(this.markerClusterGroup);
      }
      
      // Clear existing markers
      this.markerLayers.forEach(marker => this.map.removeLayer(marker));
      this.markerLayers = [];
      
      // Create cluster group
      this.markerClusterGroup = L.markerClusterGroup({
        maxClusterRadius: 80,
        spiderfyOnMaxZoom: true,
        showCoverageOnHover: false,
        zoomToBoundsOnClick: true,
        disableClusteringAtZoom: null, // Ensure clustering at all zoom levels
        removeOutsideVisibleBounds: false, // Keep all markers for clustering
        animate: true, // Enable cluster animation
        animateAddingMarkers: true, // Enable marker addition animation
        iconCreateFunction: (cluster) => {
          const childCount = cluster.getChildCount();
          
          // Calculate average health level of islands in cluster
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
          
          // Calculate average LCC value
          const averageLCC = validLCCCount > 0 ? totalLCC / validLCCCount : 0;
          
          // Determine color based on average LCC value (consistent with legend)
          let clusterColor = '#6B7280'; // Default gray
          let className = 'marker-cluster-small';
          
          if (averageLCC >= 0.7) {
            clusterColor = '#0066cc'; // Blue - Excellent
          } else if (averageLCC >= 0.5) {
            clusterColor = '#00b050'; // Green - Good
          } else if (averageLCC >= 0.3) {
            clusterColor = '#ffc000'; // Orange - Fair
          } else if (averageLCC > 0) {
            clusterColor = '#ff0000'; // Red - Poor
          }
          
          // Determine size based on count
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
      
      // Create markers for each island
      this.islandLocations.forEach(location => {
        // Get LCC data for this island
        const islandLCCData = this.allIslandsLCCData[location.island];
        let lccValue = 'N/A';
        let markerColor = '#6B7280'; // Default gray
        
        if (islandLCCData && islandLCCData.lcc !== undefined) {
          lccValue = (islandLCCData.lcc * 100).toFixed(1) + '%';
          // Set color based on LCC value (consistent with legend)
          if (islandLCCData.lcc >= 0.7) {
            markerColor = '#0066cc'; // Blue - Excellent
          } else if (islandLCCData.lcc >= 0.5) {
            markerColor = '#00b050'; // Green - Good
          } else if (islandLCCData.lcc >= 0.3) {
            markerColor = '#ffc000'; // Orange - Fair
          } else {
            markerColor = '#ff0000'; // Red - Poor
          }
        }
        
        // Check if it is the selected island
        const isSelected = location.island === this.selectedIsland;
        const markerSize = isSelected ? 40 : 30;
        const fontSize = isSelected ? '14px' : '12px';
        const borderWidth = isSelected ? '3px' : '2px';
        const borderColor = isSelected ? '#fef2f2' : 'white';
        const boxShadow = isSelected ? 
          '0 4px 15px rgba(220, 38, 38, 0.5), 0 0 0 3px rgba(220, 38, 38, 0.3)' : 
          '0 2px 8px rgba(0,0,0,0.3)';
        const animation = isSelected ? 'animation: pulse 2s infinite;' : '';
        
        // Create custom number marker
        const marker = L.marker([location.lat, location.lng], {
          islandName: location.island, // Add island name to options
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
        
        // Add popup window
        const islandState = this.getIslandState(location.island);
        const popupContent = `
          <div class="custom-popup-content">
            <h3 style="margin: 0 0 10px 0; color: #1f2937; font-size: 16px; font-weight: 600;">
              ${location.island}
            </h3>
            <div style="margin-bottom: 8px; color: #4b5563;">
              <span style="font-weight: 500;">🏛️ ${this.$t('map.popup.state')}</span>
              ${islandState}
            </div>
            ${this.getSelectButtonHtml(location.island)}
          </div>
        `;
        
        
        marker.bindPopup(popupContent, {
          maxWidth: 250,
          className: 'custom-popup'
        });
        
        // Add to cluster group
        this.markerClusterGroup.addLayer(marker);
        this.markerLayers.push(marker);
      });
      
      // Add cluster group to map
      this.map.addLayer(this.markerClusterGroup);
      
      console.log('聚类地图更新完成，岛屿数量:', this.islandLocations.length);
    },

    // Create custom heat effect
    createCustomHeatEffect(heatData) {
      // Create continuous heat areas without showing numbers
      const groupedData = {};
      
      // Group data by island
      this.islandLocations.forEach(location => {
        const islandLCCData = this.allIslandsLCCData[location.island];
        let intensity = 0.3;
        
        if (islandLCCData && islandLCCData.lcc) {
          intensity = Math.max(0.3, Math.min(islandLCCData.lcc * 2.0, 1.0));
        }
        
        // Create heat area for each island
        let color = '#dc2626'; // Default red
        if (intensity >= 0.8) color = '#16a34a'; // Dark green
        else if (intensity >= 0.6) color = '#22c55e'; // Green
        else if (intensity >= 0.4) color = '#84cc16'; // Light green
        else if (intensity >= 0.3) color = '#fbbf24'; // Yellow
        else if (intensity >= 0.2) color = '#f97316'; // Orange
        
        // Create multiple overlapping circles to simulate heat effect
        for (let i = 0; i < 3; i++) {
          const radius = 25 + (i * 15) + (intensity * 20);
          const opacity = (0.4 - i * 0.1) * intensity;
          
          const circle = L.circle([location.lat, location.lng], {
            radius: radius * 1000, // Convert to meters
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

    // Get color based on LCC value (consistent with legend)
    getLCCColor(lccValue) {
      if (lccValue >= 70) return '#0066cc'; // Blue - Excellent
      if (lccValue >= 50) return '#00b050'; // Green - Good
      if (lccValue >= 30) return '#ffc000'; // Orange - Fair
      return '#ff0000'; // Red - Poor
    },

     
     // Get low indicator percentage label
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
     
     // Get high indicator percentage label
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
     
     // Get indicator analysis text
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
     
     // Get state based on island name
     getIslandState(islandName) {
      // Check if data is loaded
      if (!this.stateIslandsData || this.stateIslandsData.length === 0) {
        console.warn('州岛屿数据尚未加载，使用默认值');
        return 'Malaysia';
      }
      
      try {
        console.log(`正在查找岛屿 "${islandName}" 的州信息...`);
        console.log('可用的州岛屿数据数量:', this.stateIslandsData.length);
        
        // Find state corresponding to island from stateIslandsData
        const islandData = this.stateIslandsData.find(item => {
          return item && item.island && item.island.toLowerCase() === islandName.toLowerCase();
        });
        
        if (islandData && islandData.state) {
          console.log(`精确匹配找到岛屿 ${islandName} 的州: ${islandData.state}`);
          return islandData.state;
        }
        
        // If no exact match found, try fuzzy matching
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
        
        // Show some potentially related island data for debugging
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

    // State selection change handling
    async onStateChange() {
      console.log('选择州:', this.selectedState);
      if (this.selectedState) {
        // Filter and display related islands based on state
        await this.filterIslandsByState(this.selectedState);
      } else {
        // If state selection is cancelled, show all islands
        this.updateClusterMap();
      }
    },

    // Filter islands by state
    async filterIslandsByState(state) {
      console.log('根据州筛选岛屿:', state);
      console.log('完整的州岛屿数据:', this.stateIslandsData);
      
      // Filter all islands of this state from stateIslandsData
      const stateIslands = this.stateIslandsData.filter(item => item.state === state);
      const islandNames = stateIslands.map(item => item.island);
      
      console.log(`州 ${state} 相关岛屿:`, islandNames);
      console.log(`州 ${state} 岛屿详细数据:`, stateIslands);
      
      if (islandNames.length > 0) {
        // Calculate center coordinates of all islands in this state
        await this.focusOnStateIslands(stateIslands);
        
        // Update map to only show islands of this state
        await this.updateStateClusterMap(stateIslands);
      } else {
        // If no related islands found, show prompt
        console.log(`没有找到州 ${state} 相关的岛屿`);
        console.log('所有可用的州:', [...new Set(this.stateIslandsData.map(item => item.state))]);
        alert(`没有找到州 ${state} 的岛屿数据`);
      }
    },

    // Focus on state island area
    async focusOnStateIslands(stateIslands) {
      console.log('聚焦到州岛屿:', stateIslands);
      if (!this.map || stateIslands.length === 0) {
        console.error('地图未初始化或州岛屿数据为空:', {map: !!this.map, stateIslandsLength: stateIslands.length});
        return;
      }
      
      try {
        // Get coordinates of all islands
        const islandNames = stateIslands.map(item => item.island);
        const response = await axios.post(`${API_BASE_URL}/trend/bleach/coordinates`, islandNames);
        
        if (response.data.code === 1) {
          const coordinatesData = response.data.data;
          const validCoordinates = [];
          
          // Collect valid coordinates
          for (const [islandName, data] of Object.entries(coordinatesData)) {
            if (data.hasData && data.lat && data.lng) {
              validCoordinates.push([data.lat, data.lng]);
            }
          }
          
          if (validCoordinates.length > 0) {
            // Calculate bounds and focus map
            const group = new L.featureGroup(validCoordinates.map(coord => L.marker(coord)));
            this.map.fitBounds(group.getBounds(), {padding: [20, 20]});
            console.log(`聚焦到州的${validCoordinates.length}个岛屿`);
          }
        }
      } catch (error) {
        console.error('获取州岛屿坐标失败:', error);
      }
    },

    // Update state cluster map
    async updateStateClusterMap(stateIslands) {
      console.log('更新州集群地图:', stateIslands);
      if (!this.map) {
        console.error('地图未初始化');
        return;
      }
      
      // Recreate cluster group to avoid state issues
      if (this.markerClusterGroup) {
        this.map.removeLayer(this.markerClusterGroup);
        this.markerClusterGroup = null;
      }
      
      // Create new cluster group (consistent configuration with updateClusterMap)
      this.markerClusterGroup = L.markerClusterGroup({
        maxClusterRadius: 80,
        spiderfyOnMaxZoom: true,
        showCoverageOnHover: false,
        zoomToBoundsOnClick: true,
        disableClusteringAtZoom: null, // Ensure clustering at all zoom levels
        removeOutsideVisibleBounds: false, // Keep all markers for clustering
        animate: true, // Enable cluster animation
        animateAddingMarkers: true, // Enable marker addition animation
        iconCreateFunction: (cluster) => {
          const childCount = cluster.getChildCount();
          
          // Calculate average health level of islands in cluster
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
          
          // Calculate average LCC value
          const averageLCC = validLCCCount > 0 ? totalLCC / validLCCCount : 0;
          
          // Determine color based on average LCC value (consistent with legend)
          let clusterColor = '#6B7280'; // Default gray
          let className = 'marker-cluster-small';
          
          if (averageLCC >= 0.7) {
            clusterColor = '#0066cc'; // Blue - Excellent
          } else if (averageLCC >= 0.5) {
            clusterColor = '#00b050'; // Green - Good
          } else if (averageLCC >= 0.3) {
            clusterColor = '#ffc000'; // Orange - Fair
          } else if (averageLCC > 0) {
            clusterColor = '#ff0000'; // Red - Poor
          }
          
          // Determine size based on count
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
      
      // Collect all valid markers
      const validMarkers = [];
      
      // Add markers for islands in this state - use Promise.all to wait for all async operations
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
              // Validate coordinate data
              const lat = parseFloat(data.lat);
              const lng = parseFloat(data.lng);
              if (isNaN(lat) || isNaN(lng)) {
                console.warn(`岛屿 ${islandData.island} 坐标数据格式错误:`, {lat: data.lat, lng: data.lng});
                return null;
              }
              
              // Parse LCC value
              const lccValue = parseFloat(islandData.lcc.replace('%', '')) || 0;
              
              // Create circular custom icon (consistent with updateClusterMap)
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
              
              // Create marker, ensure all necessary properties exist
              const marker = L.marker([lat, lng], {
                icon: customIcon,
                islandName: islandData.island,
                title: islandData.island
              });
              
              // Ensure marker is properly initialized
              if (!marker || !marker._latlng) {
                console.error(`标记创建失败: ${islandData.island}`);
                return null;
              }
              
              // Add popup (consistent style with updateClusterMap)
              const popupContent = `
                <div class="custom-popup-content">
                  <h3 style="margin: 0 0 10px 0; color: #1f2937; font-size: 16px; font-weight: 600;">
                    ${islandData.island}
                  </h3>
                  <div style="margin-bottom: 8px; color: #4b5563;">
                    <span style="font-weight: 500;">🏛️ ${this.$t('map.popup.state')}</span><br>
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
      
      // Wait for all markers to be created
      const markers = await Promise.all(markerPromises);
      const validMarkersArray = markers.filter(marker => marker !== null);
      
      // Batch add valid markers to cluster group
      if (validMarkersArray.length > 0) {
        // First add cluster group to map
        this.map.addLayer(this.markerClusterGroup);
        
        // Then batch add markers
        validMarkersArray.forEach(marker => {
          try {
            this.markerClusterGroup.addLayer(marker);
            validMarkers.push(marker);
          } catch (error) {
            console.error('添加标记到集群组失败:', error, marker);
          }
        });
        
        // Force refresh clustering
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

    // Update regional heatmap
    updateRegionalHeatMap(filteredIslands) {
      if (!this.map) return;
      
      // Remove existing heat layer
      if (this.heatLayer) {
        this.map.removeLayer(this.heatLayer);
      }
      
      // Clear existing markers
      this.markerLayers.forEach(marker => this.map.removeLayer(marker));
      this.markerLayers = [];
      
      // Only show filtered islands
      const filteredLocations = this.islandLocations.filter(location => 
        filteredIslands.includes(location.island)
      );
      
      // Prepare heatmap data
      const heatData = [];
      filteredLocations.forEach(location => {
        const recommendedIsland = this.recommendedIslands.find(r => r.name === location.island);
        let intensity = 0.1;
        
        if (recommendedIsland && recommendedIsland.lcc) {
          intensity = Math.min(recommendedIsland.lcc, 1.0);
        }
        
        heatData.push([location.lat, location.lng, intensity]);
      });
      
      // Create heatmap or custom heat effect
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
      
      // Add island markers (above heatmap)
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
      
      // Adjust map view to include all filtered islands
      if (filteredLocations.length > 0) {
        const group = new L.featureGroup();
        filteredLocations.forEach(location => {
          L.marker([location.lat, location.lng]).addTo(group);
        });
        this.map.fitBounds(group.getBounds().pad(0.1));
        
        // Clear temporary marker group
        this.map.removeLayer(group);
      }
    },

    // Select recommended island (top 5 ranked islands)
    selectRecommendedIsland(island) {
      console.log('选择推荐岛屿:', island.name);
      // Navigate to island detail page
      this.$router.push(`/travel/${encodeURIComponent(island.name)}`).catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    // Determine whether to show selection button
    shouldShowSelectButton(islandName) {
      // All islands show button
      return true;
    },

    // Get selection button HTML
    getSelectButtonHtml(islandName) {
      if (this.shouldShowSelectButton(islandName)) {
        // Check if it is a recommended island (top 5)
        const isRecommended = this.recommendedIslands.some(island => island.name === islandName);
        
        // Check island health status
        const islandLCCData = this.allIslandsLCCData[islandName];
        let isPoor = false;
        let isFair = false;
        let isGood = false;
        if (islandLCCData && islandLCCData.lcc !== undefined) {
          const lccValue = parseFloat(islandLCCData.lcc);
          isPoor = lccValue < 0.3; // Poor状态（红色）
          isFair = lccValue >= 0.3 && lccValue < 0.5; // Fair状态（黄色）
          isGood = lccValue >= 0.5; // Good状态（绿色）
        }
        
        // Set button text based on island type
        let buttonText = this.$t('map.popup.learnMore'); // Default text (other islands)
        if (isRecommended) {
          buttonText = this.$t('map.popup.selectIsland'); // Recommended island (top 5)
        } else if (isPoor || isFair) {
          buttonText = this.$t('map.popup.travelResponsibly'); // Poor状态（红色）和Fair状态（黄色）岛屿
        } else if (isGood) {
          buttonText = this.$t('map.popup.learnBeforeTravel'); // Good状态（绿色）岛屿，但排除推荐岛屿
        }
        
        return `<button onclick="window.selectIslandFromPopup('${islandName}')" style="background: #3b82f6; color: white; border: none; padding: 6px 12px; border-radius: 6px; cursor: pointer; font-size: 12px; font-weight: bold;">${buttonText}</button>`;
      } else {
        return `<div style="color: #6b7280; font-size: 12px; font-style: italic; margin-top: 8px;">${this.$t('map.popup.noDetailedPage')}</div>`;
      }
    },

    // Select island from map popup (simplified version)
    selectIslandFromPopup(islandName) {
      console.log('=== 点击岛屿调试信息 ===');
      console.log('点击岛屿:', islandName);
      console.log('当前路由:', this.$route.path);
      console.log('所有岛屿LCC数据:', this.allIslandsLCCData);
      console.log('推荐岛屿列表:', this.recommendedIslands);
      
      // Automatically update state selection
      this.updateStateSelectionFromIsland(islandName);
      
      // Check island LCC data
      const islandLCCData = this.allIslandsLCCData[islandName];
      console.log(`${islandName} 的LCC数据:`, islandLCCData);
      
      if (islandLCCData && islandLCCData.lcc !== undefined) {
        const lccValue = parseFloat(islandLCCData.lcc);
        console.log(`${islandName} LCC值: ${lccValue}`);
        
        // If Poor indicator (< 30%), directly jump to Education interface
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
      
      // Check if it is a recommended island (top 5)
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
        // Other islands (neither recommended nor Poor status), jump to Education interface
        console.log(`${islandName} 是其他岛屿，跳转到Education界面`);
        console.log('准备跳转到 /education');
        
        this.$router.push('/education').then(() => {
          console.log('成功跳转到Education界面');
        }).catch(err => {
          console.error('跳转到Education界面失败:', err);
          if (err.name !== 'NavigationDuplicated') {
            console.error('Navigation error:', err);
          }
        });
      }
      
      console.log('=== 调试信息结束 ===');
    },


    // Setup Horizon chart
    setupHorizonChart() {
      this.$nextTick(() => {
        const ctx = this.$refs.horizonChart;
        console.log('设置Horizon图表，Canvas元素:', ctx);
        
        if (ctx) {
          // If chart already exists, destroy it first
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
                  fill: true // Enable fill
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

    // Update comparison chart
    async updateComparisonChart() {
      if (!this.compareIsland1 && !this.compareIsland2) {
        console.log('需要至少选择一个岛屿');
        return;
      }
      
      console.log('更新比较图表:', this.compareIsland1, 'vs', this.compareIsland2);
      
      try {
        // Prepare API request
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
        
        // Load island data
        const responses = await Promise.all(requests);
        const islandDataArray = responses.map((response, index) => ({
          island: islands[index],
          data: response.data.code === 1 ? response.data.data : []
        }));
        
        console.log('获取到的岛屿数据:', islandDataArray);
        console.log('当前选中的属性:', this.selectedCompareAttribute);
        
        // Check if there is valid data
        const validData = islandDataArray.filter(item => item.data.length > 0);
        if (validData.length === 0) {
          console.warn('没有岛屿有数据');
          return;
        }
        
        // Prepare chart data
        const allDates = new Set();
        validData.forEach(islandItem => {
          islandItem.data.forEach(item => {
            console.log('处理数据项:', item);
            if (item && item.date) {
              if (Array.isArray(item.date)) {
            allDates.add(`${item.date[0]}`);
                console.log('添加日期 (数组格式):', item.date[0]);
              } else {
                // If date is not an array, use directly
                allDates.add(`${item.date}`);
                console.log('添加日期 (字符串格式):', item.date);
              }
            }
          });
        });
        
        const sortedDates = Array.from(allDates).sort();
        console.log('所有排序后的日期:', sortedDates);
        
        // Create dataset - dynamically created, supports 1-2 islands
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
        
        // Update chart
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
      
      // Clear canvas
      this.backgroundCtx.clearRect(0, 0, this.backgroundCanvas.width, this.backgroundCanvas.height);
      
      // Draw gradient background
      const gradient = this.backgroundCtx.createLinearGradient(0, 0, 0, this.backgroundCanvas.height);
      gradient.addColorStop(0, 'rgba(102, 126, 234, 0.1)');
      gradient.addColorStop(0.5, 'rgba(118, 75, 162, 0.05)');
      gradient.addColorStop(1, 'rgba(59, 130, 246, 0.1)');
      
      this.backgroundCtx.fillStyle = gradient;
      this.backgroundCtx.fillRect(0, 0, this.backgroundCanvas.width, this.backgroundCanvas.height);
      
      // Update and draw particles
      this.particles.forEach(particle => {
        // Update position
        particle.x += particle.speedX;
        particle.y += particle.speedY;
        
        // Boundary detection
        if (particle.x < 0 || particle.x > this.backgroundCanvas.width) {
          particle.speedX *= -1;
        }
        if (particle.y < 0 || particle.y > this.backgroundCanvas.height) {
          particle.speedY *= -1;
        }
        
        // Draw particles
        this.backgroundCtx.beginPath();
        this.backgroundCtx.arc(particle.x, particle.y, particle.radius, 0, Math.PI * 2);
        this.backgroundCtx.fillStyle = particle.color;
        this.backgroundCtx.fill();
      });
      
      // Draw connection lines
      this.drawConnections();
      
      // Continue animation
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

    // Test API connection
    async testAPIConnection() {
      console.log('=== API连接测试开始 ===');
      console.log('API_BASE_URL:', API_BASE_URL);
      
      // Test island list API
      try {
        console.log('测试岛屿列表API...');
        const islandsResponse = await axios.get(`${API_BASE_URL}/trend/islands`);
        console.log('岛屿列表API响应:', islandsResponse.data);
      } catch (error) {
        console.error('岛屿列表API测试失败:', error);
      }
      
      // Test specific island data API (using confirmed data islands)
      const testIslands = ['Payar']; // Confirmed data from provided API response
      
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


    // Debug complete data flow for specific island
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
          
          // Get latest year LCC (first data, as sorted by date descending)
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
    // Clean up Leaflet map instance
    if (this.map) {
      this.map.remove();
      this.map = null;
    }
    
    // Clean up chart instance
    if (this.horizonChart) {
      this.horizonChart.destroy();
      this.horizonChart = null;
    }
    
    // Clean up Canvas animation
    if (this.animationId) {
      cancelAnimationFrame(this.animationId);
      this.animationId = null;
    }
    
    // Remove event listeners
    window.removeEventListener('resize', this.resizeCanvas);
    document.removeEventListener('click', this.handleOutsideClick);
    
    // Clean up global functions
    if (window.selectIslandFromPopup) {
      delete window.selectIslandFromPopup;
    }
    
    // Clean up event listeners
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
  overflow-y: visible; /* Ensure dropdown can be displayed */
  display: flex;
  flex-direction: column;
  position: relative;            /* Normal document flow */
  min-height: 100vh;
  overflow-y: auto;              /* Page can scroll */
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
  background: url('@/assets/bg_trend.webp') no-repeat center center;
  background-size: cover;
  z-index: -1;
}

/* Ocean theme background loading placeholder styles */
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



/* Ocean theme progress bar styles */
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


/* Background decoration elements */
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

/* Bubble decoration */
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

/* Top navigation */
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

/* Navigation items */
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

/* Map & Recommendation 下拉菜单样式 */
.nav-item-dropdown {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.nav-item-dropdown .nav-item-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.nav-item-dropdown::before {
  content: '';
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  height: 12px;
  background: transparent;
}

.dropdown-menu {
  position: absolute;
  top: calc(100% + 4px);
  left: 50%;
  transform: translateX(-50%);
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(25px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15), 0 4px 16px rgba(0, 0, 0, 0.1);
  min-width: 160px;
  z-index: 1000;
  overflow: hidden;
  animation: dropdownSlideIn 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* Remove small arrow, use figure 2 style */

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


/* Comparison modal styles */
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

/* Island selection area */
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

/* Attribute filter area */
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

/* Comparison chart area */
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

/* Page title */
.page-title {
  text-align: center;
  padding: 18px 0 40px 0;
  position: relative;
  z-index: 5;
  align-self: center;    /* Center the title */
  width: 100%;           /* Full width */
}

.page-title h2 {
  margin: 0;
  font-size: 2rem;
  color: #ffffffff;
  font-weight: 600;
  text-shadow: 2px 2px 6px rgba(0, 0, 0, 0.66); 
}

.data-source {
  margin: 8px 0 0 0;
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.8);
  font-style: italic;
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.5);
}



/* Loading indicator */
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

/* Control panel */
.control-panel {
  padding: 12px 12px;
  display: flex;
  gap: 20px;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  position: relative;
  z-index: 1000; /* Increase z-index to ensure dropdown displays properly */
}

.control-item {
  display: flex;
  align-items: center;
}

/* Combined search box styles */
.combined-search-container {
  position: relative;
  display: flex;
  align-items: center;
  min-width: 280px;
  z-index: 1000; /* Ensure search box container has sufficient z-index */
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

/* Input box bottom border style when dropdown is displayed */
.combined-search-container:has(.dropdown-list) .combined-search-input,
.combined-search-input:focus-within {
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
}

/* Provide backward compatible styles (for browsers that do not support :has) */
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

/* Dropdown list styles */
.dropdown-list {
  position: fixed; /* Use fixed positioning to completely avoid container clipping */
  background: rgba(255, 255, 255, 0.98);
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  box-shadow: 0 12px 35px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(15px);
  z-index: 10000; /* Highest z-index to ensure not being blocked */
  max-height: 300px;
  overflow-y: auto;
  /* Enhanced overlay effect */
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

/* Scrollbar styles */
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

/* New layout styles */
.new-layout {
  display: flex;
  flex-direction: column;
  align-items: flex-start;  /* Change to flex-start to prevent child elements from centering */
  gap: 25px;
  width: 100%;              /* Change to 100% to follow parent container */
  margin: 0;
  padding: 0;               /* Remove padding to avoid affecting scroll */
  position: relative;
  z-index: 5;
}

/* Region selection styles moved to left panel */

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
  /* Hide default dropdown arrow */
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
  padding: 12px 40px 12px 16px;   /* Adjust padding for more comfort */
  border: 1.5px solid #d1d5db;    /* Lighter border */
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.9); /* Semi-transparent glass effect */
  backdrop-filter: blur(6px);
  color: #374151;
  font-size: 14px;
  width: 100%;
  box-sizing: border-box;
  transition: all 0.3s ease;
  cursor: pointer;

  /* Hide default arrow */
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;

  /* Add shadow to make elements more three-dimensional */
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

/* On hover */
.location-type-select:hover {
  border-color: #3b82f6;
  box-shadow: 0 4px 10px rgba(59, 130, 246, 0.15);
  background: rgba(255, 255, 255, 1);
}

/* On focus */
.location-type-select:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.2);
  background: #fff;
}

/* Custom arrow icon */
.select-arrow {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #3b82f6;       /* Theme blue arrow */
  pointer-events: none;
  font-size: 14px;
  transition: transform 0.2s ease;
}

/* Arrow rotation effect when dropdown is open */
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

/* Heatmap area */
.heat-map-section {
  display: flex;
  justify-content: flex-start;  /* Start from left to prevent panel moving to right */
  align-items: flex-start;      /* Top alignment */
  gap: 30px;                    /* Left and right spacing */
  margin: 0 auto;               /* Reference comparison-layout-container */
  max-width: 1800px;            /* Set reasonable maximum width */
  width: 100%;                  /* Use 100% width */
  align-self: center;           /* Center overall but content starts from left */
  overflow-x: auto;             /* Allow horizontal scrolling */
  overflow-y: visible;          /* Ensure dropdown menu is visible */
  padding: 0 20px;              /* Add padding */
  box-sizing: border-box;       /* Include padding in width */
}

/* Selection panel */
.select-panel {
  background: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(5px);
  border-radius: 12px;
  padding: 20px;                         /* Reduce padding for more compact */
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  width: 240px;                          /* Narrow width */
  min-height: 600px;                     /* Set minimum height, consistent with map container */
  max-height: 600px;                     /* Set maximum height */
  overflow-y: auto;                      /* Show scrollbar when content overflows */
  flex-shrink: 0;                        /* Prevent compression */
  order: 1;                              /* Ensure on the leftmost */
  display: flex;
  flex-direction: column;
  gap: 20px;                             /* Spacing between controls */
}

/* Selection panel scrollbar styles */
.select-panel::-webkit-scrollbar {
  width: 6px;
}

.select-panel::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.3);
  border-radius: 3px;
}

.select-panel::-webkit-scrollbar-thumb {
  background: rgba(99, 179, 237, 0.5);
  border-radius: 3px;
}

.select-panel::-webkit-scrollbar-thumb:hover {
  background: rgba(99, 179, 237, 0.8);
}

/* Panel title */
.select-panel h3 {
  margin: 18px;
  font-size: 22px;
  font-weight: 600;
  color: #ffffffff;
  text-align: center;
  padding-bottom: 8px;
  border-bottom: 2px solid rgba(255, 255, 255, 0.6);
}

/* Cardify each group of controls */
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

/* Label styles */
.control-group label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
  font-size: 13px;
  color: #000000ff;
}

/* Heatmap container */
.heat-map-container {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  max-width: 1200px;          /* Maximum width to prevent infinite stretching */
  width: 100%;
  margin: 0 auto;
  flex: 1;                   /* Middle adaptive */
  order: 2;                  /* Ensure in the middle */
  justify-content: center;
  align-items: center;
  min-width: 600px;          /* Reduce minimum width to support small screens */
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

/* Recommendation panel */
.recommendations-panel {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  padding: 12px 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  width: 240px;
  max-width: 240px;
  min-height: 600px;         /* Set minimum height, consistent with map container */
  max-height: 600px;         /* Set maximum height */
  overflow-y: auto;          /* Show scrollbar when content overflows */
  flex-shrink: 0;            /* Prevent compression */
  order: 3;                  /* Ensure on the rightmost */
  margin: 0 auto;
}

/* Recommendation panel scrollbar styles */
.recommendations-panel::-webkit-scrollbar {
  width: 6px;
}

.recommendations-panel::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.3);
  border-radius: 3px;
}

.recommendations-panel::-webkit-scrollbar-thumb {
  background: rgba(99, 179, 237, 0.5);
  border-radius: 3px;
}

.recommendations-panel::-webkit-scrollbar-thumb:hover {
  background: rgba(99, 179, 237, 0.8);
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
  justify-content: flex-start;  /* Change to left alignment to ensure left panel is visible */
  align-items: stretch;
  gap: 20px;
  margin: 30px auto 0;
  max-width: 1400px;
  width: 100%;
  overflow-x: auto;          /* Allow horizontal scrolling */
  min-width: 0;             /* Allow flex items to shrink */
  padding: 0 20px;          /* Add left and right padding */
  box-sizing: border-box;   /* Include padding in width */
}

/* External Island Selection Panel - Left Side */
.external-island-selection-panel,.info-panel {
  flex: 0 0 280px;      /* Fixed width, no shrink no grow */
  background: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(5px);
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

  display: flex;
  flex-direction: column;
  gap: 20px;

  min-height: 500px;
  width: 280px;         /* Ensure fixed width */
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
  flex: 1 1 auto;           /* Auto fill remaining space */
  min-width: 500px;         /* Reduce minimum width to adapt to small screens */
  max-width: 900px;         /* Increase maximum width */
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
  align-self: center;        /* Let this section center itself */
  overflow-x: auto;          /* Allow horizontal scrolling */
  padding: 0 20px;          /* Add padding */
  box-sizing: border-box;   /* Include padding in width */
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

/* Left panel */
.left-panel {
  background: rgba(255, 255, 255, 0.2);
  padding: 24px;
  border-right: 1px solid rgba(226, 232, 240, 0.3);
}



/* Map display area */
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
  max-width: 900px;    /* Maintain width, will not adapt to become narrower */
  min-width: 500px;    /* Optional: prevent too narrow */
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

/* Loading indicator styles */
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

/* Custom Leaflet control styles */
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

/* Custom popup styles */
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

/* Custom marker animation */
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

/* Map loading state */
.leaflet-container {
  background: linear-gradient(135deg, #e0f2fe 0%, #b3e5fc 100%) !important;
}

.leaflet-tile-loaded {
  transition: opacity 0.3s ease !important;
}

/* Map attribution info style optimization */
.leaflet-control-attribution {
  background: rgba(255, 255, 255, 0.8) !important;
  backdrop-filter: blur(5px) !important;
  border-radius: 4px !important;
  font-size: 10px !important;
  color: #6b7280 !important;
}

/* Clustered map styles - completely remove all default colors and glow effects */
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

/* Override all default styles of Leaflet clustering library */
.leaflet-marker-icon.marker-cluster,
.leaflet-marker-icon.marker-cluster-small,
.leaflet-marker-icon.marker-cluster-medium,
.leaflet-marker-icon.marker-cluster-large {
  background: transparent !important;
  border: none !important;
  box-shadow: none !important;
}

/* Force override all default styles of external CSS files */
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

/* Override all possible pseudo-elements */
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

/* Custom number marker styles */
.custom-number-marker {
  background: transparent !important;
  border: none !important;
}

.custom-number-marker:hover div {
  transform: scale(1.1) !important;
}

/* Pulse animation for selected island */
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

/* Custom cluster icon styles */
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

/* Map legend styles - use :deep() to penetrate scoped styles */
:deep(.map-legend) {
  position: absolute !important;
  bottom: 20px !important;   /* Distance from bottom */
  left: 20px !important;     /* Distance from left */
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
  width: 130px !important;   /* Increase width to accommodate horizontal layout */
}

:deep(.map-legend h4) {
  margin: 0 0 4px 0 !important;
  font-size: 13px !important;
  font-weight: bold !important;
  color: #333 !important;
  text-align: center !important;  /* Center title */
}

:deep(.map-legend p) {
  margin: 0 0 6px 0 !important;
  font-size: 10px !important;
  color: #666 !important;
  text-align: center !important;  /* Center subtitle */
}

:deep(.map-legend div) {
  display: flex !important;
  align-items: center !important;
  margin-bottom: 3px !important;
  font-size: 11px !important;
  color: #333 !important;
  white-space: nowrap !important;  /* Prevent text wrapping */
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

/* Ensure custom icon hover effects */
.custom-div-icon div:hover {
  transform: scale(1.1);
  transition: transform 0.3s ease;
}

.custom-div-icon {
  background: transparent !important;
  border: none !important;
}

/* Unified popup styles */
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

/* Ensure popup content styles are consistent */
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



/* Right panel */
.right-panel {
  padding: 24px;
}



.chart-wrapper {
  position: relative;
  height: 500px; /* Increase height to match map */
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

/* All indicator areas - semi-transparent glass effect */
.all-indicators-section {
  margin-top: 24px;
  padding: 24px;
  align-self: center;    /* Center indicator area */
  width: 100%;           /* Full width */
}

/* Area title */
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

/* Indicator row */
.indicator-row {
  display: flex;
  gap: 20px;
  margin-bottom: 24px;
}

.indicator-row:last-child {
  margin-bottom: 0;
}

/* Indicator container - three equal parts */
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

/* Container header */
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

/* Image wrapper */
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

/* Percentage overlay */
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

/* Analysis container special styles */
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
  padding: 2px 6px;       /* Padding */
  border-radius: 6px;     /* Rounded rectangle */
  font-weight: 600;
  color: #000;            /* Black text */
  margin-right: 6px;      /* Leave some spacing with following text */
}

.analysis-content .good {
  background-color: #4dc87a8c; /* Green background */
}

.analysis-content .moderate {
  background-color: #fcd34d7a; /* Orange-yellow background */
}

.analysis-content .poor {
  background-color: #ff939381; /* Light red background */
}

.analysis-content .critical {
  background-color: #f871718c; /* Dark red background */
  font-weight: 700;
}

/* Special container colors */
.poor-container {
  border-left: 3px solid #fc8181;
}

.good-container {
  border-left: 3px solid #68d391;
}

.analysis-container {
  border-left: 3px solid #63b3ed;
}

/* Responsive design */
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
  
  .comparison-layout-container {
    justify-content: flex-start;  /* Ensure layout starts from left */
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
  
  .comparison-layout-container {
    flex-wrap: nowrap;  /* No wrap, maintain horizontal layout */
    justify-content: flex-start;
  }
  
  .external-island-selection-panel {
    flex: 0 0 260px;  /* Slightly smaller */
    width: 260px;
  }
  
  .visualization-container {
    min-width: 450px;
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
  
  /* Remove order modification, keep left panel always on the left */
  
  .comparison-layout-container {
    flex-wrap: nowrap;
    overflow-x: auto;  /* Enable horizontal scrolling on small screens */
    justify-content: flex-start;
  }
  
  .external-island-selection-panel {
    flex: 0 0 240px;
    width: 240px;
  }
  
  .visualization-container {
    min-width: 400px;
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
    font-size: 16px; /* Prevent iOS auto zoom */
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

/* Summary button styles */
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

/* Summary modal styles */
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
  padding: 20px;
  margin: 20px auto 0 auto;
  max-width: 1350px;
  width: 95vw;
  overflow-x: auto;          /* Allow horizontal scrolling */
  box-sizing: border-box;   /* Include padding in width */
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

/* Contact info styles */
.contact-info {
  display: flex;
  flex-direction: row;
  gap: 20px;
  align-items: center;
  justify-content: center;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #fff;
  font-size: 14px;
}

.contact-icon {
  font-size: 16px;
  opacity: 0.8;
}

.contact-text {
  user-select: all;
  cursor: text;
}

.info-bubble {
  background: #ffffffcc; /* Semi-transparent white background */
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

/* Left small triangle */
.info-bubble::before {
  content: "";
  position: absolute;
  top: 15px; /* Adjust triangle vertical position */
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

/* Real icon image */
.info-icon {
  width: 100px;   /* Set icon size */
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
  grid-template-columns: repeat(3, 1fr); /* Three per row */
  gap: 20px;
}

.indicator-panel {
  display: flex;                /* Become flex container */
  flex-direction: column;       /* Vertical arrangement: header → text → image */
  justify-content: flex-start;  /* Default top to bottom arrangement */
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
  transform: translateY(-5px); /* Slight floating of entire panel */
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
  margin-top: auto;    /* Auto push to panel bottom */
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
  height: 160px;       /* Uniform height */
  object-fit: cover;
  border-radius: 8px;
  display: block;
  transition: transform 0.3s ease;  /* Animation */
}

/* Hover zoom effect */
.image-card:hover img {
  transform: scale(1.1);  /* Scale up 10% */
}



.percentage-overlay {
  position: absolute;
  bottom: 10px;
  right: 10px;
  display: inline-block;        /* Only wrap text */
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  font-weight: bold;
  font-size: 13px;
  padding: 2px 6px;             /* Reduce padding */
  border-radius: 6px;
  white-space: nowrap;          /* Prevent line break */
  min-width: unset;             /* Disable auto width expansion */
  max-width: max-content;       /* Width equals text */
}

/* LCC indicator - first image is poor state (<30%), second image is good state (>50%) */
.indicator-panel:nth-child(1) .image-card:nth-child(1) .percentage-overlay {
  background-color: #ff939381; /* Light red background - corresponds to poor */
  color: #000;
}

.indicator-panel:nth-child(1) .image-card:nth-child(2) .percentage-overlay {
  background-color: #4dc87a8c; /* Green background - corresponds to good */
  color: #000;
}

/* AS indicator - first image is critical state (>40%), second image is good state (<10%) */
.indicator-panel:nth-child(6) .image-card:nth-child(1) .percentage-overlay {
  background-color: #f871718c; /* Dark red background - corresponds to critical */
  color: #000;
}

.indicator-panel:nth-child(6) .image-card:nth-child(2) .percentage-overlay {
  background-color: #4dc87a8c; /* Green background - corresponds to good */
  color: #000;
}

/* SD indicator - first image is poor state (<5%), second image is good state (5-15%) */
.indicator-panel:nth-child(3) .image-card:nth-child(1) .percentage-overlay {
  background-color: #ff939381; /* Light red background - corresponds to poor */
  color: #000;
}

.indicator-panel:nth-child(3) .image-card:nth-child(2) .percentage-overlay {
  background-color: #4dc87a8c; /* Green background - corresponds to good */
  color: #000;
}

/* DI indicator - first image is poor state (>10%), second image is good state (<5%) */
.indicator-panel:nth-child(4) .image-card:nth-child(1) .percentage-overlay {
  background-color: #ff939381; /* Light red background - corresponds to poor */
  color: #000;
}

.indicator-panel:nth-child(4) .image-card:nth-child(2) .percentage-overlay {
  background-color: #4dc87a8c; /* Green background - corresponds to good */
  color: #000;
}

/* PI indicator - first image is poor state (>10%), second image is good state (<5%) */
.indicator-panel:nth-child(5) .image-card:nth-child(1) .percentage-overlay {
  background-color: #ff939381; /* Light red background - corresponds to poor */
  color: #000;
}

.indicator-panel:nth-child(5) .image-card:nth-child(2) .percentage-overlay {
  background-color: #4dc87a8c; /* Green background - corresponds to good */
  color: #000;
}

/* Overview panel styles */
.overview-panel {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(240, 248, 255, 0.9));
  backdrop-filter: blur(15px);
  border-radius: 20px;
  padding: 25px 30px;
  color: #000000ff;
  font-size: 14px;
  transition: all 0.4s ease;
  border: 2px solid rgba(59, 130, 246, 0.3);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.15), 0 4px 8px rgba(59, 130, 246, 0.1);
  position: relative;
  overflow: hidden;
}

.overview-panel::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #00d4ff, #0099cc, #0066aa);
  border-radius: 20px 20px 0 0;
}

.overview-panel .panel-header {
  text-align: center;
  margin-bottom: 15px;
}

.overview-panel .panel-title {
  text-align: center !important;
  display: block;
  width: 100%;
  font-size: 28px;
  font-weight: 600;
  color: #1e40af;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 8px;
  position: relative;
}

.overview-panel:hover {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.98), rgba(240, 248, 255, 0.95));
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2), 0 8px 16px rgba(59, 130, 246, 0.2);
  border-color: rgba(59, 130, 246, 0.5);
}

.overview-panel:hover::before {
  background: linear-gradient(90deg, #00d4ff, #0099cc, #0066aa, #004499);
  height: 6px;
}

.overview-content {
  margin-top: 10px;
}

.overview-description {
  line-height: 1.8;
  margin: 0;
  font-size: 20px;
  color: #1f2937;
  text-align: left;
  font-weight: 500;
  background: linear-gradient(135deg, #1f2937, #374151);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.section-subtitle {

  color: #ffffffff;
  text-shadow: 1px 1px 4px rgba(0, 0, 0, 0.4);
  font-size: 16px;

}



</style>

