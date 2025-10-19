<template>
  <div class="island-detail-container" :key="islandName" :style="{ backgroundImage: backgroundImageUrl ? `url(${backgroundImageUrl})` : 'url(@/assets/island.webp)' }">
    <!-- Background image loading placeholder -->
    <div class="bg-placeholder" v-if="!backgroundLoaded">
      <div class="progress-container">
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: loadingProgress + '%' }"></div>
        </div>
        <p class="loading-text">{{ $t('islandDetail.loading') }}</p>
      </div>
    </div>
    
    <!-- Top Navigation -->
    <div class="top-nav">
      <div class="nav-left">
        <!-- Left side logo -->
        <img :src="appIconUrl || '@/assets/icon.png'" alt="logo" class="nav-logo" @click="goToHome" />
        <h1 class="logo" @click="goToHome">{{ $t('nav.logo') }}</h1>
      </div>
      <div class="nav-right">
        <div class="nav-items">
          <span class="nav-item" @click="goToMap">{{ $t('nav.mapRecommendation.line1') }}</span>
          <div class="nav-item-dropdown" @mouseenter="showTravelDropdown = true" @mouseleave="showTravelDropdown = false">
            <span class="nav-item active">{{ $t('nav.topIsland') }}</span>
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

    <!-- Main Content -->
    <div class="main-content">

      <!-- Content Sections -->
      <div class="content-sections" v-if="islandName === 'P Singa'">
        <div class="section-container">
          
          
          <!-- {{ $t('islandDetail.description') }} Section -->
          <div class="description-section">
            <h3 class="description-title">{{ $t('islandDetail.pSinga.title') }}</h3>
            <div class="description-content">
              <h4>{{ $t('islandDetail.description') }}</h4>
              <p>{{ $t('islandDetail.pSinga.description') }}</p>
            </div>
          </div>
          
          <!-- Carousel Section -->
          <div class="carousel-section">
            <div class="carousel" id="carousel">
              <div class="card" v-for="(image, index) in carouselImages" :key="index">
                <img :src="image" :alt="`P Singa Image ${index + 1}`">
                <div class="card-source">
                  © naturallylangkawi.my
                </div>
              </div>
            </div>
          </div>
          
          <!-- {{ $t('islandDetail.travelInfo') }} Section -->
          <div class="travel-info-section">
            <div class="travel-info-section">
  <div class="info-card">
    <h3 class="info-title">{{ $t('islandDetail.travelInfo') }}</h3>
    <div class="info-content">
      <p><strong>{{ $t('islandDetail.arrive') }}:</strong> {{ $t('islandDetail.pSinga.arrive') }}</p>
      
      <p class="reminder"><strong>{{ $t('islandDetail.reminder') }}:</strong> {{ $t('islandDetail.pSinga.reminder') }}</p>
      
      <p><strong>{{ $t('islandDetail.snorkelingDiving') }}:</strong> {{ $t('islandDetail.pSinga.snorkeling') }}</p>
      
      <p><strong>{{ $t('islandDetail.accommodation') }}:</strong> {{ $t('islandDetail.pSinga.accommodation') }}</p>
    </div>
  </div>
</div>

            
            <!-- Hotel Recommendation Section -->
            <div class="hotel-recommendation-section">
              <h3 class="hotel-section-title">{{ $t('islandDetail.hotelRecommendation') }}</h3>
              <div class="hotel-row">
                <div class="hotel-card">
                  <div class="hotel-img">
                    <img :src="pSingaImages.hotels.datai" :alt="$t('islandDetail.pSinga.dataiResort')">
                  </div>
                  <div class="hotel-body">
                    <div class="hotel-name">{{ $t('islandDetail.pSinga.dataiResort') }}</div>
                    <div class="hotel-desc">{{ $t('islandDetail.pSinga.dataiDesc') }}</div>
                  </div>
                  <a class="hotel-book" href="https://www.thedatai.com/" target="_blank">{{ $t('islandDetail.pSinga.booking') }}</a>
                </div>

                <div class="hotel-card">
                  <div class="hotel-img">
                    <img :src="pSingaImages.hotels.danna" :alt="$t('islandDetail.pSinga.dannaResort')">
                  </div>
                  <div class="hotel-body">
                    <div class="hotel-name">{{ $t('islandDetail.pSinga.dannaResort') }}</div>
                    <div class="hotel-desc">{{ $t('islandDetail.pSinga.dannaDesc') }}</div>
                  </div>
                  <a class="hotel-book" href="https://www.thedanna.com/" target="_blank">{{ $t('islandDetail.pSinga.booking') }}</a>
                </div>

                <div class="hotel-card">
                  <div class="hotel-img">
                    <img :src="pSingaImages.hotels.frangipani" :alt="$t('islandDetail.pSinga.frangipaniResort')">
                  </div>
                  <div class="hotel-body">
                    <div class="hotel-name">{{ $t('islandDetail.pSinga.frangipaniResort') }}</div>
                    <div class="hotel-desc">{{ $t('islandDetail.pSinga.frangipaniDesc') }}</div>
                  </div>
                  <a class="hotel-book" href="https://www.frangipanilangkawi.com/" target="_blank">{{ $t('islandDetail.pSinga.booking') }}</a>
                </div>

                <div class="hotel-card">
                  <div class="hotel-img">
                    <img :src="pSingaImages.hotels.ambong" :alt="$t('islandDetail.pSinga.ambongResort')">
                  </div>
                  <div class="hotel-body">
                    <div class="hotel-name">{{ $t('islandDetail.pSinga.ambongResort') }}</div>
                    <div class="hotel-desc">{{ $t('islandDetail.pSinga.ambongDesc') }}</div>
                  </div>
                  <a class="hotel-book" href="https://www.ambong-ambong.com/" target="_blank">{{ $t('islandDetail.pSinga.booking') }}</a>
                </div>
              </div>
            </div>
            
            <!-- Operators & Tours Section -->
            <div class="operators-tours-section">
              <div class="tours-header">
                <h3 class="tours-title">{{ $t('islandDetail.pSinga.operatorsTours') }}</h3>
                <p class="tours-subtitle">{{ $t('islandDetail.pSinga.operatorsDesc') }}</p>
                <p class="tours-subtitle">{{ $t('islandDetail.pSinga.operatorsDesc2') }}</p>
              </div>
              
              <!-- Tours Duel Panels -->
              <div class="tours-duel-section">
                <input class="duel-input" type="radio" name="pick" id="pick-left" checked>
                <input class="duel-input" type="radio" name="pick" id="pick-right">
                
                <div class="duel-wrap">
                  <!-- Left: PADI Dive Center -->
                    <label class="panel left" for="pick-left" :title="$t('islandDetail.chooseDiving')">
                    <img :src="pSingaImages.activities.tour1" :alt="$t('islandDetail.pSinga.padiDiving')">
                    <div class="meta">
                      <div>
                        <h3 class="title">{{ $t('islandDetail.pSinga.padiDiving') }}</h3>
                        <ul class="points">
                          <li>{{ $t('islandDetail.pSinga.padiDesc1') }}</li>
                          <li>{{ $t('islandDetail.pSinga.padiDesc2') }}</li>
                        </ul>
                      </div>
                      <a class="cta" href="https://www.padi.com/dive-center/malaysia/langkawi-scuba/" target="_blank" rel="noopener">{{ $t('islandDetail.pSinga.checkTour') }}</a>
                    </div>
                  </label>

                  <div class="vs"><span>VS</span></div>

                  <!-- Right: Jet Ski Tour -->
                  <label class="panel right" for="pick-right" :title="$t('islandDetail.chooseSnorkeling')">
                    <img :src="pSingaImages.activities.tour2" :alt="$t('islandDetail.pSinga.unescoJetSki')">
                    <div class="meta">
                      <div>
                        <h3 class="title">{{ $t('islandDetail.pSinga.unescoJetSki') }}</h3>
                        <ul class="points">
                          <li>{{ $t('islandDetail.pSinga.jetSkiDesc1') }}</li>
                          <li>{{ $t('islandDetail.pSinga.jetSkiDesc2') }}</li>
                        </ul>
                      </div>
                      <a class="cta" href="https://www.getyourguide.com/en-gb/langkawi-l903/langkawi-premium-unesco-geopark-jet-ski-tour-of-8-islands-t24907/" target="_blank" rel="noopener">{{ $t('islandDetail.pSinga.checkTour') }}</a>
                    </div>
                  </label>
                </div>
              </div>
            </div>
            
            <!-- Tips & Conservation Section -->
            <div class="tips-conservation-section">
              <!-- Tips Section -->
              <div class="info-card">
                <h3 class="info-title">{{ $t('islandDetail.pSinga.tips') }}</h3>
                <div class="info-content">
                  <p>{{ $t('islandDetail.pSinga.tipsContent') }}</p>
                  <p>{{ $t('islandDetail.pSinga.tipsContent2') }}</p>
                </div>
              </div>
              
              <!-- Conservation Section -->
              <div class="info-card">
                <h3 class="info-title">{{ $t('islandDetail.pSinga.conservation') }}</h3>
                <div class="info-content">
                  <p>{{ $t('islandDetail.pSinga.conservationContent') }}</p>
                </div>
              </div>
            </div>
            
            <!-- Action Buttons Section -->
            <div class="action-buttons-section">
              <button class="action-btn reef-policy-btn" @click="goToGovernment">
                <span class="btn-icon">🏛️</span>
                <span class="btn-text">{{ $t('islandDetail.actionButtons.reefPolicies') }}</span>
                <span class="btn-arrow">→</span>
              </button>
              <button class="action-btn education-btn" @click="goToEducation">
                <span class="btn-icon">📚</span>
                <span class="btn-text">{{ $t('islandDetail.actionButtons.reefConservation') }}</span>
                <span class="btn-arrow">→</span>
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Mertang Content Sections -->
      <div class="content-sections" v-else-if="islandName === 'Mertang'">
        <div class="section-container">
          
          <!-- {{ $t('islandDetail.description') }} Section -->
          <div class="description-section">
            <h3 class="description-title">{{ $t('islandDetail.mertang.title') }}</h3>
            <div class="description-content">
              <h4>{{ $t('islandDetail.description') }}</h4>
              <p>{{ $t('islandDetail.mertang.description') }}</p>
            </div>
          </div>

          <!-- Carousel Section -->
          <div class="carousel-section">
            <div class="carousel" id="carousel">
              <div class="card" v-for="(image, index) in mertangCarouselImages" :key="index">
                <img :src="image" :alt="`Mertang Image ${index + 1}`">
                <div class="card-source">
                  © {{ mertangCarouselSources[index].text }}
                </div>
              </div>
            </div>
          </div>
          
          <!-- {{ $t('islandDetail.travelInfo') }} Section -->
          <div class="travel-info-section">
            <!-- Arrive Section -->
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.arrive') }}</h3>
              <div class="info-content">
                <p>{{ $t('islandDetail.mertang.arrive') }}</p>
                <p class="reminder"><strong>{{ $t('islandDetail.reminder') }}:</strong> {{ $t('islandDetail.mertang.reminder') }}</p>
              </div>
            </div>
            
            <!-- Snorkeling/Diving Section -->
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.snorkelingDiving') }}</h3>
              <div class="info-content">
                <p>{{ $t('islandDetail.mertang.snorkeling') }}</p>
              </div>
            </div>
            
            <!-- {{ $t('islandDetail.accommodation') }} Section -->
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.accommodation') }}</h3>
              <div class="info-content">
                <p><strong>{{ $t('islandDetail.mertang.accommodation') }}</strong></p>
                <p>{{ $t('islandDetail.mertang.accommodationDesc') }}</p>
              </div>
            </div>
            
            <!-- Hotel Recommendation Section -->
            <div class="hotel-recommendation-section">
              <h3 class="hotel-section-title">{{ $t('islandDetail.hotelRecommendation') }}</h3>
              <div class="hotel-row">
                <div class="hotel-card">
                  <div class="hotel-img">
                    <img :src="'http://static.coralkita.site/assets/Mertang/rawaislandresort.png'" alt="Rawa Island Resort">
                  </div>
                <div class="hotel-body">
                  <div class="hotel-name">{{ $t('islandDetail.mertang.rawaResort') }}</div>
                  <div class="hotel-desc">{{ $t('islandDetail.mertang.rawaDesc') }}</div>
                </div>
                  <a class="hotel-book" href="https://rawaislandresort.com/" target="_blank">Booking</a>
                </div>

                <div class="hotel-card">
                  <div class="hotel-img">
                    <img :src="'http://static.coralkita.site/assets/Mertang/orcanation.png'" alt="Orca Nation Resort">
                  </div>
                <div class="hotel-body">
                  <div class="hotel-name">{{ $t('islandDetail.mertang.orcaResort') }}</div>
                  <div class="hotel-desc">{{ $t('islandDetail.mertang.orcaResortDesc') }}</div>
                </div>
                  <a class="hotel-book" href="https://www.alangsrawa.com/" target="_blank">Booking</a>
                </div>
              </div>
            </div>
            
            <!-- Tour Activities Section -->
            <div class="tour-activities-section">
              <h3 class="tour-section-title">{{ $t('islandDetail.tourActivities') }}</h3>
              
              <!-- Hero Diving Card -->
              <div class="hero-tour-card">
                <img :src="'http://static.coralkita.site/assets/Mertang/diving.jpg'" alt="Diving">
                <div class="hero-tour-overlay">
                  <h2 class="hero-tour-title">{{ $t('islandDetail.mertang.orcaDiving') }}</h2>
                  <p class="hero-tour-desc">{{ $t('islandDetail.mertang.orcaDesc') }}</p>
                  <a href="https://orcanation.org/diving/" target="_blank" class="hero-tour-btn">{{ $t('islandDetail.exploreDiving') }}</a>
                </div>
              </div>

              <!-- Sub Activities Grid -->
              <div class="sub-activities-grid">
                <div class="sub-activity-card">
                  <img :src="'http://static.coralkita.site/assets/Mertang/rawaislandresort.png'" alt="Rawa Island Resort">
                  <div class="sub-activity-overlay">
                    <h3 class="sub-activity-title">{{ $t('islandDetail.mertang.rawaResort') }}</h3>
                    <a href="https://rawaislandresort.com/" target="_blank" class="sub-activity-btn">{{ $t('islandDetail.mertang.book') }}</a>
                  </div>
                </div>
                <div class="sub-activity-card">
                  <img :src="'http://static.coralkita.site/assets/Mertang/orcanation.png'" alt="Orca Nation">
                  <div class="sub-activity-overlay">
                    <h3 class="sub-activity-title">{{ $t('islandDetail.mertang.orcaDiving') }}</h3>
                    <a href="https://www.alangsrawa.com/" target="_blank" class="sub-activity-btn">{{ $t('islandDetail.mertang.book') }}</a>
                  </div>
                </div>
                <div class="sub-activity-card">
                  <img :src="'http://static.coralkita.site/assets/Mertang/vacation_279.png'" alt="Island Hopping">
                  <div class="sub-activity-overlay">
                    <h3 class="sub-activity-title">{{ $t('islandDetail.mertang.islandHopping') }}</h3>
                    <a href="https://ami.travel/en/vacations/279" target="_blank" class="sub-activity-btn">{{ $t('islandDetail.mertang.book') }}</a>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- Tips & Conservation Section -->
            <div class="tips-conservation-section">
            <!-- Tips Section -->
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.mertang.tips') }}</h3>
              <div class="info-content">
                <p>{{ $t('islandDetail.mertang.tipsContent') }}</p>
              </div>
            </div>
            
            <!-- Conservation Section -->
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.mertang.conservation') }}</h3>
              <div class="info-content">
                <p>{{ $t('islandDetail.mertang.conservationContent') }}</p>
              </div>
            </div>
            </div>
            
            <!-- Action Buttons Section -->
            <div class="action-buttons-section">
              <button class="action-btn reef-policy-btn" @click="goToGovernment">
                <span class="btn-icon">🏛️</span>
                <span class="btn-text">{{ $t('islandDetail.actionButtons.reefPolicies') }}</span>
                <span class="btn-arrow">→</span>
              </button>
              <button class="action-btn education-btn" @click="goToEducation">
                <span class="btn-icon">📚</span>
                <span class="btn-text">{{ $t('islandDetail.actionButtons.reefConservation') }}</span>
                <span class="btn-arrow">→</span>
              </button>
            </div>
          </div>
          
        </div>
      </div>
      
      <!-- Sipadan Content Sections -->
      <div class="content-sections" v-else-if="islandName === 'Sipadan'">
        <div class="section-container">
          
          <!-- {{ $t('islandDetail.description') }} Section -->
          <div class="description-section">
            <h3 class="description-title">{{ $t('islandDetail.sipadan.title') }}</h3>
            <div class="description-content">
              <h4>{{ $t('islandDetail.description') }}</h4>
              <p>{{ $t('islandDetail.sipadan.description') }}</p>
            </div>
          </div>

          <!-- Carousel Section -->
          <div class="carousel-section">
            <div class="carousel" id="carousel">
              <div class="card" v-for="(image, index) in sipadanCarouselImages" :key="index">
                <img :src="image" :alt="`Sipadan Image ${index + 1}`">
                <div class="card-source">
                  © {{ sipadanCarouselSources[index].text }}
                </div>
              </div>
            </div>
          </div>
          
          <!-- {{ $t('islandDetail.travelInfo') }} Section -->
          <div class="travel-info-section">
            <!-- Arrive Section -->
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.arrive') }}</h3>
              <div class="info-content">
                <p>{{ $t('islandDetail.sipadan.arrive') }}</p>
                <p class="reminder"><strong>{{ $t('islandDetail.reminder') }}:</strong> {{ $t('islandDetail.sipadan.reminder') }}</p>
              </div>
            </div>
            
            <!-- Snorkeling/Diving Section -->
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.snorkelingDiving') }}</h3>
              <div class="info-content">
                <p>{{ $t('islandDetail.sipadan.snorkeling') }}</p>
                <p><strong>{{ $t('islandDetail.sipadan.iconicSites') }}</strong></p>
                <p><strong>{{ $t('islandDetail.sipadan.marineLife') }}</strong></p>
                <p><strong>{{ $t('islandDetail.sipadan.snorkelingNote') }}</strong></p>
              </div>
            </div>
            
            <!-- {{ $t('islandDetail.accommodation') }} Section -->
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.accommodation') }}</h3>
              <div class="info-content">
                <p>{{ $t('islandDetail.sipadan.accommodation') }}</p>
              </div>
            </div>
          </div>
          
          <!-- Hotel Recommendation Section -->
          <div class="hotel-recommendation-section">
            <h3 class="hotel-section-title">{{ $t('islandDetail.hotelRecommendation') }}</h3>
            <div class="hotel-row">
              <div class="hotel-card">
                <div class="hotel-img">
                  <img :src="'http://static.coralkita.site/assets/Sipadan/Kapalai_Dive.png'" alt="Sipadan Kapalai Dive Resort">
                </div>
                <div class="hotel-body">
                  <div class="hotel-name">{{ $t('islandDetail.sipadan.kapalaiResort') }}</div>
                  <div class="hotel-desc">{{ $t('islandDetail.sipadan.kapalaiDesc') }}</div>
                </div>
                <a class="hotel-book" href="https://dive-malaysia.com/sipadan-kapalai-dive-resort/" target="_blank">{{ $t('islandDetail.sipadan.booking') }}</a>
              </div>
              <div class="hotel-card">
                <div class="hotel-img">
                  <img :src="'http://static.coralkita.site/assets/Sipadan/Mabul_Beach.png'" alt="Mabul Beach Resort">
                </div>
                <div class="hotel-body">
                  <div class="hotel-name">{{ $t('islandDetail.sipadan.mabulResort') }}</div>
                  <div class="hotel-desc">{{ $t('islandDetail.sipadan.mabulDesc') }}</div>
                </div>
                <a class="hotel-book" href="https://www.scuba-junkie.com/mabul-beach-resort/" target="_blank">{{ $t('islandDetail.sipadan.booking') }}</a>
              </div>
              <div class="hotel-card">
                <div class="hotel-img">
                  <img :src="'http://static.coralkita.site/assets/Sipadan/Seaventures_Dive.png'" alt="Seaventures Dive Rig">
                </div>
                <div class="hotel-body">
                  <div class="hotel-name">{{ $t('islandDetail.sipadan.seaventuresResort') }}</div>
                  <div class="hotel-desc">{{ $t('islandDetail.sipadan.seaventuresDesc') }}</div>
                </div>
                <a class="hotel-book" href="https://seaventuresdive.com/" target="_blank">{{ $t('islandDetail.sipadan.booking') }}</a>
              </div>
            </div>
          </div>
          
          <!-- Tips & Conservation Section -->
          <div class="tips-conservation-section">
            <!-- Tips Section -->
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.sipadan.tips') }}</h3>
              <div class="info-content">
                <p>{{ $t('islandDetail.sipadan.tipsContent') }}</p>
                <p>{{ $t('islandDetail.sipadan.tipsContent2') }}</p>
                <p>{{ $t('islandDetail.sipadan.tipsContent3') }}</p>
              </div>
            </div>
            
            <!-- Conservation Section -->
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.sipadan.conservation') }}</h3>
              <div class="info-content">
                <p>{{ $t('islandDetail.sipadan.conservationContent') }}</p>
                <p>{{ $t('islandDetail.sipadan.conservationContent2') }}</p>
              </div>
            </div>
            
            <!-- Action Buttons Section -->
            <div class="action-buttons-section">
              <button class="action-btn reef-policy-btn" @click="goToGovernment">
                <span class="btn-icon">🏛️</span>
                <span class="btn-text">{{ $t('islandDetail.actionButtons.reefPolicies') }}</span>
                <span class="btn-arrow">→</span>
              </button>
              <button class="action-btn education-btn" @click="goToEducation">
                <span class="btn-icon">📚</span>
                <span class="btn-text">{{ $t('islandDetail.actionButtons.reefConservation') }}</span>
                <span class="btn-arrow">→</span>
              </button>
            </div>
          </div>
          
        </div>
      </div>
      
      <!-- Pulau Lima Content -->
      <div class="content-sections" v-else-if="islandName === 'Pulau Lima'">
        <div class="section-container">
          <!-- {{ $t('islandDetail.description') }} Section -->
          <div class="description-section">
            <h3 class="description-title">{{ $t('islandDetail.pulauLima.title') }}</h3>
            <div class="description-content">
              <h4>{{ $t('islandDetail.description') }}</h4>
              <p>{{ $t('islandDetail.pulauLima.description') }}</p>
            </div>
          </div>

          <!-- Carousel Section -->
          <div class="carousel-section">
            <div class="carousel" id="carousel">
              <div class="card" v-for="(image, index) in pulauLimaCarouselImages" :key="index">
                <img :src="image" :alt="`Pulau Lima Image ${index + 1}`">
                <div class="card-source">
                  © {{ pulauLimaCarouselSources[index].text }}
                </div>
              </div>
            </div>
          </div>
          
          <!-- {{ $t('islandDetail.travelInfo') }} Section -->
          <div class="travel-info-section">
            <!-- Arrive Section -->
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.arrive') }}</h3>
              <div class="info-content">
                <p>{{ $t('islandDetail.pulauLima.arrive') }}</p>
                <p class="reminder"><strong>{{ $t('islandDetail.reminder') }}:</strong> {{ $t('islandDetail.pulauLima.reminder') }}</p>
              </div>
            </div>
            
            <!-- Snorkeling/Diving Section -->
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.snorkelingDiving') }}</h3>
              <div class="info-content">
                <p>{{ $t('islandDetail.pulauLima.snorkeling') }}</p>
              </div>
            </div>
            
            <!-- {{ $t('islandDetail.marineParkSnorkeling') }} Card -->
            <div class="hero-tour-card">
                <img :src="'http://static.coralkita.site/assets/Pulau Lima/dive.png'" :alt="$t('islandDetail.marineParkSnorkeling')">
              <div class="hero-tour-overlay">
                <h2 class="hero-tour-title">{{ $t('islandDetail.marineParkSnorkeling') }}</h2>
                <p class="hero-tour-desc">{{ $t('islandDetail.pulauLima.marineParkDesc') }}</p>
                <a href="https://www.siburesort.com/dive-base/snorkel-trips-to-tropical-islands-within-the-marine-park.html" target="_blank" class="hero-tour-btn">{{ $t('islandDetail.exploreSnorkeling') }}</a>
              </div>
            </div>
            
          </div>
          
          <!-- {{ $t('islandDetail.accommodation') }} Section -->
          <div class="travel-info-section">
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.accommodation') }}</h3>
              <div class="info-content">
                <p><strong>{{ $t('islandDetail.pulauLima.accommodation') }}</strong></p>
                <p>{{ $t('islandDetail.pulauLima.accommodationDesc') }}</p>
              </div>
            </div>
          </div>
          
          <!-- Hotel Recommendation Section -->
          <div class="hotel-recommendation-section">
            <h3 class="hotel-section-title">{{ $t('islandDetail.hotelRecommendation') }}</h3>
            <div class="hotel-row">
              <div class="hotel-card">
                <div class="hotel-img">
                  <img :src="'http://static.coralkita.site/assets/Pulau Lima/sibu.png'" :alt="$t('islandDetail.pulauLima.seaGypsyResort')">
                </div>
                <div class="hotel-body">
                  <div class="hotel-name">{{ $t('islandDetail.pulauLima.seaGypsyResort') }}</div>
                  <div class="hotel-desc">{{ $t('islandDetail.pulauLima.seaGypsyDesc') }}</div>
                </div>
                <a class="hotel-book" href="https://www.siburesort.com/dive-base/snorkel-trips-to-tropical-islands-within-the-marine-park.html" target="_blank">{{ $t('islandDetail.pSinga.booking') }}</a>
              </div>
              <div class="hotel-card">
                <div class="hotel-img">
                  <img :src="'http://static.coralkita.site/assets/Pulau Lima/rimbaresort.png'" :alt="$t('islandDetail.pulauLima.rimbaResort')">
                </div>
                <div class="hotel-body">
                  <div class="hotel-name">{{ $t('islandDetail.pulauLima.rimbaResort') }}</div>
                  <div class="hotel-desc">{{ $t('islandDetail.pulauLima.rimbaDesc') }}</div>
                </div>
                <a class="hotel-book" href="https://rimbaresort.com/" target="_blank">{{ $t('islandDetail.pSinga.booking') }}</a>
              </div>
            </div>
          </div>
          
          <!-- Arc Gallery Activities Section -->
          <div class="arc-activities-section">
            <h3 class="arc-section-title">{{ $t('islandDetail.tourActivities') }}</h3>
            <div class="arc">
              <div class="orbit">
                <div class="ph">
                  <img :src="'http://static.coralkita.site/assets/Pulau Lima/act1.png'" :alt="$t('islandDetail.pulauLima.myVillaRecreation')">
                  <div class="ph-overlay">
                    <h4 class="ph-title">{{ $t('islandDetail.pulauLima.myVillaRecreation') }}</h4>
                    <p class="ph-desc">{{ $t('islandDetail.pulauLima.myVillaDesc') }}</p>
                  </div>
                  <a class="cta" href="https://myvillasibu.com/Recreation-Facilities.html" target="_blank">{{ $t('islandDetail.pulauLima.book') }}</a>
                </div>
                <div class="ph">
                  <img :src="'http://static.coralkita.site/assets/Pulau Lima/act2.png'" :alt="$t('islandDetail.pulauLima.islandHopping')">
                  <div class="ph-overlay">
                    <h4 class="ph-title">{{ $t('islandDetail.pulauLima.islandHopping') }}</h4>
                    <p class="ph-desc">{{ $t('islandDetail.pulauLima.islandHoppingDesc') }}</p>
                  </div>
                  <a class="cta" href="https://causewaylinkholidays.com/product-detail/?id=2145-Mersing-Island-Hopping-1-Day-Trip" target="_blank">{{ $t('islandDetail.pulauLima.book') }}</a>
                </div>
                <div class="ph">
                  <img :src="'http://static.coralkita.site/assets/Pulau Lima/act3.png'" :alt="$t('islandDetail.pulauLima.marineParkTours')">
                  <div class="ph-overlay">
                    <h4 class="ph-title">{{ $t('islandDetail.pulauLima.marineParkTours') }}</h4>
                    <p class="ph-desc">{{ $t('islandDetail.pulauLima.marineParkToursDesc') }}</p>
                  </div>
                  <a class="cta" href="https://www.tripadvisor.com/Attraction_Review-g298279-d26267273-Reviews-VReal_Mersing_Island_Hopping-Mersing_Mersing_District_Johor.html" target="_blank">{{ $t('islandDetail.pulauLima.book') }}</a>
                </div>
              </div>
            </div>
          </div>
          
          <!-- Tips Section -->
          <div class="tips-conservation-section">
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.pulauLima.tips') }}</h3>
              <div class="info-content">
                <p>{{ $t('islandDetail.pulauLima.tipsContent') }}</p>
              </div>
            </div>
          </div>
          
          <!-- Action Buttons Section -->
          <div class="action-buttons-section">
            <button class="action-btn reef-policy-btn" @click="goToGovernment">
              <span class="btn-icon">🏛️</span>
              <span class="btn-text">{{ $t('islandDetail.actionButtons.reefPolicies') }}</span>
              <span class="btn-arrow">→</span>
            </button>
            <button class="action-btn education-btn" @click="goToEducation">
              <span class="btn-icon">📚</span>
              <span class="btn-text">{{ $t('islandDetail.actionButtons.reefConservation') }}</span>
              <span class="btn-arrow">→</span>
            </button>
          </div>
        </div>
      </div>
      
      <!-- Pulau Seri Buat Content -->
      <div class="content-sections" v-else-if="islandName === 'Seri Buat'">
        <div class="section-container">
          <!-- {{ $t('islandDetail.description') }} Section -->
          <div class="description-section">
            <h3 class="description-title">{{ $t('islandDetail.seriBuat.title') }}</h3>
            <div class="description-content">
              <h4>{{ $t('islandDetail.description') }}</h4>
              <p>{{ $t('islandDetail.seriBuat.description') }}</p>
            </div>
          </div>

          <!-- Carousel Section -->
          <div class="carousel-section">
            <div class="carousel" id="carousel">
              <div class="card" v-for="(image, index) in seriBuatCarouselImages" :key="index">
                <img :src="image" :alt="`Seri Buat Image ${index + 1}`">
                <div class="card-source">
                  © {{ seriBuatCarouselSources[index].text }}
                </div>
              </div>
            </div>
          </div>
          
          <!-- {{ $t('islandDetail.travelInfo') }} Section -->
          <div class="travel-info-section">
            <!-- Arrive Section -->
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.arrive') }}</h3>
              <div class="info-content">
                <p>{{ $t('islandDetail.seriBuat.arrive') }}</p>
                <p class="reminder"><strong>{{ $t('islandDetail.reminder') }}:</strong> {{ $t('islandDetail.seriBuat.reminder') }}</p>
              </div>
            </div>
            
            <!-- Snorkeling/Diving Section -->
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.snorkelingDiving') }}</h3>
              <div class="info-content">
                <p>{{ $t('islandDetail.seriBuat.snorkeling') }}</p>
                <p v-for="(item, index) in $t('islandDetail.seriBuat.snorkelingList')" :key="index">• {{ item }}</p>
              </div>
            </div>
            
            <!-- Hiking & Adventure Section -->
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.seriBuat.hikingAdventure') }}</h3>
              <div class="info-content">
                <p>{{ $t('islandDetail.seriBuat.hikingContent') }}</p>
              </div>
            </div>
          </div>
          
          <!-- Recommended Resorts & Operators Section -->
          <div class="polaroid-operators-section">
            <h3 class="polaroid-section-title">{{ $t('islandDetail.seriBuat.recommendedResorts') }}</h3>
            <div class="polaroids">
              <div class="pol">
                <div class="ph">
                  <img :src="'http://static.coralkita.site/assets/Seri Buat/operator1.png'" alt="Rawa Island Resort">
                  <div class="ph-overlay">
                    <p class="ph-desc">{{ $t('islandDetail.seriBuat.rawaDesc') }}</p>
                  </div>
                </div>
                <div class="title">{{ $t('islandDetail.seriBuat.rawaResort') }}</div>
                <a class="btn" href="https://rawaislandresort.com/" target="_blank">{{ $t('islandDetail.seriBuat.explore') }}</a>
              </div>
              
              <div class="pol">
                <div class="ph">
                  <img :src="'http://static.coralkita.site/assets/Seri Buat/operator2.png'" alt="1Step1Footprint">
                  <div class="ph-overlay">
                    <p class="ph-desc">{{ $t('islandDetail.seriBuat.stepFootprintDesc') }}</p>
                  </div>
                </div>
                <div class="title">{{ $t('islandDetail.seriBuat.stepFootprint') }}</div>
                <a class="btn" href="https://1step1footprint.com/johor-island-hopping-day-trip-package/" target="_blank">{{ $t('islandDetail.seriBuat.explore') }}</a>
              </div>
              
              <div class="pol">
                <div class="ph">
                  <img :src="'http://static.coralkita.site/assets/Seri Buat/operator3.png'" alt="AMI Travel">
                  <div class="ph-overlay">
                    <p class="ph-desc">{{ $t('islandDetail.seriBuat.amiTravelDesc') }}</p>
                  </div>
                </div>
                <div class="title">{{ $t('islandDetail.seriBuat.amiTravel') }}</div>
                <a class="btn" href="https://ami.travel/ms/pakej-percutian/965" target="_blank">{{ $t('islandDetail.seriBuat.explore') }}</a>
              </div>
            </div>
          </div>
          <!-- Tips Section -->
          <div class="tips-conservation-section">
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.seriBuat.tips') }}</h3>
              <div class="info-content">
                <p>{{ $t('islandDetail.seriBuat.tipsContent') }}</p>
                <p>{{ $t('islandDetail.seriBuat.tipsContent2') }}</p>
                <p>{{ $t('islandDetail.seriBuat.tipsContent3') }}</p>
                <p>{{ $t('islandDetail.seriBuat.tipsContent4') }}</p>
                <p>{{ $t('islandDetail.seriBuat.tipsContent5') }}</p>
                <p>{{ $t('islandDetail.seriBuat.tipsContent6') }}</p>
              </div>
            </div>
            <div class="info-card">
              <h3 class="info-title">{{ $t('islandDetail.seriBuat.conservation') }}</h3>
              <div class="info-content">
                <p>{{ $t('islandDetail.seriBuat.conservationContent') }}</p>
              </div>
            </div>
          </div>
          
          <!-- Action Buttons Section -->
          <div class="action-buttons-section">
            <button class="action-btn reef-policy-btn" @click="goToGovernment">
              <span class="btn-icon">🏛️</span>
              <span class="btn-text">{{ $t('islandDetail.actionButtons.reefPolicies') }}</span>
              <span class="btn-arrow">→</span>
            </button>
            <button class="action-btn education-btn" @click="goToEducation">
              <span class="btn-icon">📚</span>
              <span class="btn-text">{{ $t('islandDetail.actionButtons.reefConservation') }}</span>
              <span class="btn-arrow">→</span>
            </button>
          </div>
        </div>
      </div>
      
      <!-- Default Empty Content for other islands -->
      <div class="content-sections" v-else>
        <div class="empty-content">
          <div class="empty-icon">🏝️</div>
          <h2>{{ islandName }}</h2>
          <p>Content will be added by the administrator</p>
        </div>
      </div>
    </div>

    <!-- Footer -->
    <div class="page-footer">
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
    </div>
  </div>
</template>

<script>
import ossService from '../utils/ossService.js'

import LanguageSwitcher from './LanguageSwitcher.vue'

export default {
  name: 'IslandDetail',
  components: {
    LanguageSwitcher
  },
  props: {
    islandName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      backgroundLoaded: false, // Background image loading status
      loadingProgress: 0, // Loading progress
      loadingText: 'Loading island information...', // Loading text
      islandName: '',
      // Travel dropdown related
      showTravelDropdown: false,
      showEducationDropdown: false,
      currentLanguage: 'en',
      travelIslands: ['Mertang', 'P Singa', 'Sipadan', 'Pulau Lima', 'Seri Buat'],
      // OSS image URLs
      appIconUrl: null,
      backgroundImageUrl: null,
      ossImageUrls: {},
      // P Singa page specific OSS images
      pSingaImages: {
        carousel: [],
        hotels: {},
        activities: {}
      },
      // Carousel related
      carouselImages: [
        'http://static.coralkita.site/assets/P Singa/pic1.png',
        'http://static.coralkita.site/assets/P Singa/pic2.png',
        'http://static.coralkita.site/assets/P Singa/pic3.png',
        'http://static.coralkita.site/assets/P Singa/pic4.png',
        'http://static.coralkita.site/assets/P Singa/pic5.png'
      ],
      // Mertang carousel images
      mertangCarouselImages: [
        'http://static.coralkita.site/assets/Mertang/pic1.png',
        'http://static.coralkita.site/assets/Mertang/pic2.png',
        'http://static.coralkita.site/assets/Mertang/pic3.png',
        'http://static.coralkita.site/assets/Mertang/pic4.png',
        'http://static.coralkita.site/assets/Mertang/pic5.png'
      ],
      // Mertang carousel sources
      mertangCarouselSources: [
        { url: 'https://malaysiatravelgate.com.my/st_tour/mersing-island-5-islands/', text: 'malaysiatravelgate.com.my' },
        { url: 'https://malaysiatravelgate.com.my/st_tour/mersing-island-5-islands/', text: 'malaysiatravelgate.com.my' },
        { url: 'https://malaysiatravelgate.com.my/st_tour/mersing-island-5-islands/', text: 'malaysiatravelgate.com.my' },
        { url: 'https://nmuhamadnasir.blogspot.com/2017/04/teluk-sari-beach-resort-contact-013.html', text: 'nmuhamadnasir.blogspot.com' },
        { url: 'https://nmuhamadnasir.blogspot.com/2017/04/teluk-sari-beach-resort-contact-013.html', text: 'nmuhamadnasir.blogspot.com' }
      ],
      // Sipadan carousel images
      sipadanCarouselImages: [
        'http://static.coralkita.site/assets/Sipadan/pic1.png',
        'http://static.coralkita.site/assets/Sipadan/pic2.png',
        'http://static.coralkita.site/assets/Sipadan/pic3.png',
        'http://static.coralkita.site/assets/Sipadan/pic4.png',
        'http://static.coralkita.site/assets/Sipadan/pic5.png'
      ],
      // Sipadan carousel sources
      sipadanCarouselSources: [
        { url: 'https://www.designerjourneys.com/blog/sipadan-diving-complete-guide/', text: 'designerjourneys.com' },
        { url: 'https://www.designerjourneys.com/blog/sipadan-diving-complete-guide/', text: 'designerjourneys.com' },
        { url: 'https://www.scuba-junkie.com/complete-guide-dive-sipadan-island/', text: 'scuba-junkie.com' },
        { url: 'https://dive-malaysia.com/sipadan-kapalai-dive-resort/', text: 'dive-malaysia.com' },
        { url: 'https://blog.padi.com/everything-you-need-to-know-about-diving-sipadan/', text: 'blog.padi.com' }
      ],
      // Pulau Lima carousel images
      pulauLimaCarouselImages: [
        'http://static.coralkita.site/assets/Pulau Lima/pic1.png',
        'http://static.coralkita.site/assets/Pulau Lima/pic2.png',
        'http://static.coralkita.site/assets/Pulau Lima/pic3.png',
        'http://static.coralkita.site/assets/Pulau Lima/pic4.png',
        'http://static.coralkita.site/assets/Pulau Lima/pic5.png'
      ],
      // Pulau Lima carousel sources
      pulauLimaCarouselSources: [
        { url: 'https://beaches-searcher.com/en/beach/458201183/pulau-lima-besar', text: 'beaches-searcher.com' },
        { url: 'https://www.facebook.com/AmazingTerengganu/posts/pulau-lima-redang-oktober-bulan-akhir-sebelum-penutupan-monsun-bermula-jika-anda/2156293351180935/', text: 'facebook.com' },
        { url: 'https://www.flickr.com/photos/51361303@N06/4924899161', text: 'flickr.com' },
        { url: 'https://musafiriam.wordpress.com/tag/pulau-lima/', text: 'musafiriam.wordpress.com' },
        { url: 'https://www.sinaran.id/traveling/100115611976/spill-biaya-eksplore-ke-pulau-lima-banten-surganya-ujung-kulon-yang-eksotis', text: 'sinaran.id' }
      ],
      // Seri Buat carousel images
      seriBuatCarouselImages: [
        'http://static.coralkita.site/assets/Seri Buat/pic1.png',
        'http://static.coralkita.site/assets/Seri Buat/pic2.png',
        'http://static.coralkita.site/assets/Seri Buat/pic3.png',
        'http://static.coralkita.site/assets/Seri Buat/pic4.png',
        'http://static.coralkita.site/assets/Seri Buat/pic5.png'
      ],
      // Seri Buat carousel sources
      seriBuatCarouselSources: [
        { url: 'https://www.facebook.com/photo/?fbid=980541993478215&set=br.AbrY6KWU7FbUIDE4VvDMsPod-RiT5AgSY0JrbpvuxTH2Q-4uRM7QStBuy2_pG-ha4Ki0mi8uMfTbEe_hnvdiYTBfFaQ8e4SgKkgeU-CqIsdzbYE7tZtCR7dSJLghBQl8xWZ7EeZvvP37aT1V7emLzXIG', text: 'facebook.com' },
        { url: 'https://www.instagram.com/p/CNwDHjoJBur/', text: 'instagram.com' },
        { url: 'https://www.facebook.com/photo/?fbid=10159323664036150&set=br.AbrY6KWU7FbUIDE4VvDMsPod-RiT5AgSY0JrbpvuxTH2Q-4uRM7QStBuy2_pG-ha4Ki0mi8uMfTbEe_hnvdiYTBfFaQ8e4SgKkgeU-CqIsdzbYE7tZtCR7dSJLghBQl8xWZ7EeZvvP37aT1V7emLzXIG', text: 'facebook.com' },
        { url: 'https://www.facebook.com/photo/?fbid=10159323662991150&set=br.AbrY6KWU7FbUIDE4VvDMsPod-RiT5AgSY0JrbpvuxTH2Q-4uRM7QStBuy2_pG-ha4Ki0mi8uMfTbEe_hnvdiYTBfFaQ8e4SgKkgeU-CqIsdzbYE7tZtCR7dSJLghBQl8xWZ7EeZvvP37aT1V7emLzXIG', text: 'facebook.com' },
        { url: 'https://www.yusufmadi.com/theuntouchedisland-pulauseribuat', text: 'yusufmadi.com' }
      ],
      currentSlide: 2 // Default center third image
    }
  },
  created() {
    // Get island name from props or route parameter as fallback
    this.islandName = this.islandName || this.$route.params.islandName || 'Unknown Island';
  },
  watch: {
    // Listen to route parameter changes
    '$route'(to, from) {
      if (to.params.islandName !== from.params.islandName) {
        console.log('Island switch:', from.params.islandName, '->', to.params.islandName);
        this.updateIslandName(to.params.islandName);
      }
    }
  },
  methods: {
    /**
     * Preload background image
     */
    preloadBackgroundImage() {
      // Create high priority preload link element
      const preloadLink = document.createElement('link');
      preloadLink.rel = 'preload';
      preloadLink.as = 'image';
      preloadLink.href = 'http://static.coralkita.site/assets/island.webp';
      preloadLink.fetchPriority = 'high'; // High priority
      
      // Add to head
      document.head.appendChild(preloadLink);
      
      // Preload images to browser cache
      const img = new Image();
      img.src = 'http://static.coralkita.site/assets/island.webp';
      img.onload = () => {
        console.log('IslandDetail background image preloaded to cache');
        this.backgroundLoaded = true;
      };
      img.onerror = () => {
        console.warn('Failed to preload IslandDetail background image');
        this.backgroundLoaded = true; // Hide placeholder even if failed
      };
      
      console.log('IslandDetail background image preload started');
    },

    // Update island name and force component update
    async updateIslandName(newIslandName) {
      console.log('Update island name:', this.islandName, '->', newIslandName);
      this.islandName = newIslandName || 'Unknown Island';
      
      // Reload OSS images for new island
      await this.loadIslandCarouselImages();
      
      // If P Singa page, load specific images
      if (this.islandName === 'P Singa') {
        await this.loadPSingaImages();
      }
      
      // Force update component to ensure all content refreshes
      this.$forceUpdate();
    },
    
    // Travel navigation methods
    goToIslandDetail(islandName) {
      console.log('Navigate to island detail page:', islandName);
      this.showTravelDropdown = false;
      
      // If current island, no need to switch
      if (islandName === this.islandName) {
        console.log('Already on current island page, no need to switch');
        return;
      }
      
      this.$nextTick(() => {
        const targetPath = `/travel/${encodeURIComponent(islandName)}`;
        console.log('Preparing to navigate to:', targetPath);
        
        this.$router.push(targetPath).then(() => {
          console.log('Navigation successful');
        }).catch(err => {
          if (err.name !== 'NavigationDuplicated') {
            console.error('Navigation error:', err);
          } else {
            console.log('Navigation duplicated, but this is normal');
          }
        });
      });
    },
    
    goToHome() {
      this.$router.push('/').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },
    
    goToMap() {
      this.$router.push('/map').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },
    
    goToEducation() {
      this.$router.push('/education').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },
    
    goToTravelChecklist() {
      this.$router.push('/travel-checklist').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },
    
    goToGovernment() {
      this.$router.push('/government').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    goToAITools() {
      console.log('Navigate to AI Tools page');
      this.$router.push('/ai-tools').catch(err => {
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
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
    },

    toggleLanguage() {
      this.currentLanguage = this.currentLanguage === 'en' ? 'zh' : 'en'
    },

    // Carousel methods
    initCarousel() {
      if (this.islandName === 'P Singa' || this.islandName === 'Mertang' || this.islandName === 'Sipadan' || this.islandName === 'Pulau Lima' || this.islandName === 'Seri Buat') {
        this.$nextTick(() => {
          this.layoutCarousel();
          this.bindCarouselEvents();
        });
      }
    },

    layoutCarousel() {
      const cards = document.querySelectorAll('.card');
      cards.forEach((card, index) => {
        card.className = 'card';
        if (index === this.currentSlide) card.classList.add('center');
        else if (index === this.currentSlide - 1) card.classList.add('l1');
        else if (index === this.currentSlide - 2) card.classList.add('l2');
        else if (index < this.currentSlide - 2) card.classList.add('off-left');
        else if (index === this.currentSlide + 1) card.classList.add('r1');
        else if (index === this.currentSlide + 2) card.classList.add('r2');
        else if (index > this.currentSlide + 2) card.classList.add('off-right');
      });
    },

    bindCarouselEvents() {
      const cards = document.querySelectorAll('.card');
      cards.forEach((card, index) => {
        card.addEventListener('click', () => {
          this.currentSlide = index;
          this.layoutCarousel();
        });
      });
    },

    // Load OSS image URLs
    async loadOssImages() {
      try {
        // Load basic images in parallel
        const [appIconUrl, backgroundImageUrl] = await Promise.all([
          ossService.getAppIconUrl(),
          ossService.getIslandDetailBackgroundUrl()
        ])
        
        this.appIconUrl = appIconUrl
        this.backgroundImageUrl = backgroundImageUrl

        // Load current island carousel images
        await this.loadIslandCarouselImages()
        
        // If P Singa page, load specific images
        if (this.islandName === 'P Singa') {
          await this.loadPSingaImages()
        }
        
        console.log('Island detail page OSS images loaded successfully')
      } catch (error) {
        console.error('加载OSS图片失败:', error)
        // If OSS loading fails, use local images as fallback
        this.loadFallbackImages()
      }
    },

    // Load island carousel images
    async loadIslandCarouselImages() {
      if (!this.islandName) return

      try {
        const carouselPromises = []
        for (let i = 1; i <= 5; i++) {
          carouselPromises.push(
            ossService.getIslandCarouselImageUrl(this.islandName, i)
          )
        }
        
        const carouselUrls = await Promise.all(carouselPromises)
        
        // Update carousel images array
        if (this.islandName === 'P Singa') {
          this.carouselImages = carouselUrls
        } else if (this.islandName === 'Mertang') {
          this.mertangCarouselImages = carouselUrls
        } else if (this.islandName === 'Sipadan') {
          this.sipadanCarouselImages = carouselUrls
        } else if (this.islandName === 'Pulau Lima') {
          this.pulauLimaCarouselImages = carouselUrls
        } else if (this.islandName === 'Seri Buat') {
          this.seriBuatCarouselImages = carouselUrls
        }
      } catch (error) {
        console.error('加载岛屿轮播图片失败:', error)
      }
    },

    // Load fallback local images
    loadFallbackImages() {
      this.appIconUrl = 'http://static.coralkita.site/assets/icon.png'
      this.backgroundImageUrl = 'http://static.coralkita.site/assets/island.webp'
    },

    // Load P Singa page specific images
    async loadPSingaImages() {
      if (this.islandName !== 'P Singa') return

      try {
        const pSingaImages = await ossService.getPSingaImages()
        this.pSingaImages = pSingaImages
        
        // Update carousel images
        this.carouselImages = pSingaImages.carousel
        
        console.log('P Singa页面OSS图片加载完成')
      } catch (error) {
        console.error('加载P Singa页面图片失败:', error)
        // If OSS loading fails, use local images as fallback
        this.loadPSingaFallbackImages()
      }
    },

    // Load P Singa page fallback images
    loadPSingaFallbackImages() {
      this.pSingaImages = {
        carousel: [
          'http://static.coralkita.site/assets/P Singa/pic1.png',
          'http://static.coralkita.site/assets/P Singa/pic2.png',
          'http://static.coralkita.site/assets/P Singa/pic3.png',
          'http://static.coralkita.site/assets/P Singa/pic4.png',
          'http://static.coralkita.site/assets/P Singa/pic5.png'
        ],
        hotels: {
          datai: 'http://static.coralkita.site/assets/P Singa/Datai Langkawi.jpg',
          danna: 'http://static.coralkita.site/assets/P Singa/The Danna Langkawi.jpg',
          frangipani: 'http://static.coralkita.site/assets/P Singa/The Frangipani Langkawi Resort & Spa.jpg',
          ambong: 'http://static.coralkita.site/assets/P Singa/Ambong ambong.png'
        },
        activities: {
          tour1: 'http://static.coralkita.site/assets/P Singa/tour 1.png',
          tour2: 'http://static.coralkita.site/assets/P Singa/tour 2.png'
        }
      }
    }
  },
  
  async mounted() {
    // Immediately start preloading background image
    this.preloadBackgroundImage();
    
    this.initCarousel();
    // Load OSS images
    await this.loadOssImages();
  },
  
  updated() {
    this.initCarousel();
  }
}
</script>

<style scoped>
.island-detail-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  position: relative;
  background: none;       
  z-index: 0;               
}

.island-detail-container::before {
  content: "";
  position: fixed;  
  inset: 0;
  background:
    linear-gradient(rgba(33, 54, 78, 0.15), rgba(33, 54, 78, 0.15)),
    url('@/assets/island.webp') center/cover no-repeat;
  pointer-events: none;
  z-index: 0;  
}

/* Ensure real content covers above background */
.island-detail-container > * {
  position: relative;
  z-index: 1;
}

/* Placeholder layer: hidden by default (show when needed) */
.bg-placeholder {
  display: none;
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


/* Top Navigation Styles */
.top-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 24px;
  background: linear-gradient(90deg, rgba(26, 29, 37, 0.95) 0%, rgba(1, 162, 235, 0.95) 100%);
  backdrop-filter: blur(15px);
  color: white;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.3);
}

.nav-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.nav-logo {
  width: 40px;
  height: 40px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.nav-logo:hover {
  transform: scale(1.1);
}

.logo {
  color: white;
  font-size: 24px;
  font-weight: 700;
  margin: 0;
  cursor: pointer;
  transition: color 0.3s ease;
}

.logo:hover {
  color: #63b3ed;
}

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

/* Hero Section */
.hero-section {
  position: relative;
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.hero-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, #1e40af 0%, #3b82f6 50%, #06b6d4 100%);
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, rgba(30, 64, 175, 0.8), rgba(59, 130, 246, 0.6));
}

.hero-content {
  position: relative;
  text-align: center;
  color: white;
  z-index: 2;
}

.island-title {
  font-size: 48px;
  font-weight: 700;
  margin: 0 0 16px 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.island-subtitle {
  font-size: 20px;
  margin: 0 0 24px 0;
  opacity: 0.9;
}

.breadcrumb {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-size: 14px;
}

.breadcrumb-link {
  cursor: pointer;
  transition: color 0.3s ease;
}

.breadcrumb-link:hover {
  color: #63b3ed;
}

.breadcrumb-separator {
  opacity: 0.6;
}

.breadcrumb-current {
  font-weight: 600;
}

/* Content Sections */
.content-sections {
  padding: 140px 0 60px 0;
}

.content-section {
  margin-bottom: 80px;
}

.section-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 32px;
}

.section-title {
  font-size: 32px;
  font-weight: 700;
  color: white;
  margin-bottom: 40px;
  text-align: center;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

/* {{ $t('islandDetail.description') }} Section */
.description-section {
  margin-bottom: 40px;
  margin-top: 20px;
}

.description-title {
  color: white;
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 60px;
  text-align: center;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.6);
}

.description-content {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 30px;
  border: 10px solid rgba(255, 255, 255, 0.2);
  text-align: left;
}

.description-content h4 {
  color: white;
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 15px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

.description-content p {
  color: rgba(255, 255, 255, 0.9);
  font-size: 16px;
  line-height: 1.6;
  margin: 0;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
}

/* Carousel Section */
.carousel-section {
  margin-bottom: 50px;
}

.carousel {
  position: relative;
  height: 300px;
  width: 100%;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  user-select: none;
  perspective: 1200px;
}

.card {
  position: absolute;
  width: 400px;
  height: 220px;
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.48);
  border: 10px solid rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(8px) saturate(1.2);
  border-radius: 20px;
  transition: transform 0.45s ease, filter 0.45s, opacity 0.45s;
  cursor: pointer;

}

.card img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-source {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.8));
  color: white;
  font-size: 12px;
  padding: 20px 15px 10px;
  text-align: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.card:hover .card-source {
  opacity: 1;
}

.card-source a {
  color: #63b3ed;
  text-decoration: none;
  transition: color 0.3s ease;
}

.card-source a:hover {
  color: #93c5fd;
  text-decoration: underline;
}

/* {{ $t('islandDetail.travelInfo') }} Section */
.travel-info-section {
  margin-top: 50px;
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.info-card {
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(15px);
  border-radius: 20px;
  padding: 30px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.info-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.3);
}

.info-title {
  color: white;
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 20px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  border-bottom: 2px solid rgba(255, 255, 255, 0.3);
  padding-bottom: 10px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

.info-content {
  color: rgba(255, 255, 255, 0.9);
  font-size: 16px;
  line-height: 1.7;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

.info-content p {
  margin-bottom: 15px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
}

.info-content p:last-child {
  margin-bottom: 0;
}

.reminder {
  background: rgba(255, 193, 7, 0.1);
  border-left: 4px solid #ffc107;
  padding: 15px 20px;
  border-radius: 8px;
  margin-top: 20px;
  font-style: italic;
}

.reminder strong {
  color: #ffc107;
  font-weight: 600;
}

/* Hotel Recommendation Section */
.hotel-recommendation-section {
  margin-top: 50px;
}

.hotel-section-title {
  text-align: center;
  margin-bottom: 30px;
  color: white;
  font-size: 28px;
  font-weight: 600;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.hotel-row {
  display: flex;
  gap: 28px;
  justify-content: center;
  flex-wrap: nowrap;
  overflow-x: auto;
  padding: 10px 4px;
}

.hotel-card {
  width: 300px;
  background: #ffffffb4;
  backdrop-filter: blur(8px);
  border: 10px solid rgba(198, 198, 198, 0.2);
  border-radius: 18px;
  box-shadow: 0 10px 24px rgba(0, 0, 0, 0.14);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.hotel-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 16px 36px rgba(0, 0, 0, 0.2);
}

.hotel-img {
  aspect-ratio: 16/10;
  background: #eef2ff;
  overflow: hidden;
}

.hotel-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.hotel-body {
  padding: 14px 16px 8px;
  color: #111827;
}

.hotel-name {
  font-weight: 700;
  font-size: 16px;
  margin-bottom: 6px;
}

.hotel-desc {
  font-size: 14px;
  color: #374151;
  min-height: 38px;
}

.hotel-book {
  align-self: center;
  margin: 12px 0 16px;
  padding: 8px 18px;
  border-radius: 999px;
  background: #e9f6ffff;

  color: #3575adff;
  font-weight: 700;
  text-decoration: none;
  transition: filter 0.3s ease, transform 0.3s ease;
}

.hotel-book:hover {
  transform: translateY(-1px);
  background: rgba(255, 255, 255, 1);
  color: #3870a1ff;
  
}

/* Operators & Tours Section */
.operators-tours-section {
  margin-top: 50px;
}

.tours-header {
  text-align: center;
  margin-bottom: 30px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

.tours-title {
  color: white;
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 15px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

.tours-subtitle {
  color: rgba(255, 255, 255, 0.8);
  font-size: 16px;
  margin: 8px 0;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

/* Tours Duel Panels */
.tours-duel-section {
  --radius: 22px;
  --blur: 2px;
}

.duel-input {
  position: absolute;
  opacity: 0;
  pointer-events: none;
}

.duel-wrap {
  display: flex;
  gap: 16px;
  padding: 18px;
  height: min(72vh, 760px);
  max-width: 1200px;
  margin: 0 auto 34px;
}

.panel {
  position: relative;
  flex: 1;
  min-width: 280px;
  border-radius: var(--radius);
  overflow: hidden;
  cursor: pointer;
  transition: flex 0.35s cubic-bezier(0.2, 0.7, 0.2, 1), transform 0.35s, box-shadow 0.35s, filter 0.35s;
  box-shadow: 0 30px 80px rgba(0, 0, 0, 0.45);
  background: #111;
}

.panel:hover {
  transform: translateY(-4px);
}

.panel::after {
  content: "";
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(0, 0, 0, 0.05) 0%, rgba(0, 0, 0, 0.25) 50%, rgba(2, 6, 23, 0.75) 100%);
  pointer-events: none;
}

.panel img {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.45s ease;
}

.panel:hover img {
  transform: scale(1.06);
}

.meta {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 18px 20px;
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 14px;
  align-items: end;
  z-index: 2;
}

.title {
  margin: 0 0 6px;
  font-weight: 900;
  font-size: clamp(18px, 2.2vw, 28px);
  color: white;
}

.points {
  margin: 0;
  padding-left: 18px;
  color: #e5e7eb;
  font-size: clamp(13px, 1.6vw, 15px);
}

.points li {
  margin-bottom: 4px;
}

.cta {
  text-decoration: none;
  background: #e8f1fb;
  color: #0f3a67;
  font-weight: 800;
  padding: 10px 18px;
  border-radius: 999px;
  border: 1px solid #9ec0df;
  box-shadow: 0 10px 24px rgba(0, 0, 0, 0.25);
  white-space: nowrap;
  transition: transform 0.15s ease, filter 0.15s ease;
}

.cta:hover {
  transform: translateY(-2px);
  filter: brightness(0.96);
}

.vs {
  position: relative;
  align-self: center;
  margin: 0 4px;
  z-index: 3;
}

.vs span {
  display: inline-block;
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid rgba(255, 255, 255, 0.25);
  padding: 10px 14px;
  border-radius: 999px;
  backdrop-filter: blur(6px);
  font-weight: 800;
  letter-spacing: 0.6px;
  color: white;
}

/* Selected/Hover effects */
/* Selected/Hover effects */
#pick-left:checked ~ .duel-wrap .left {
  flex: 1.35;
  filter: none; /* Keep clear after click */
}

#pick-left:checked ~ .duel-wrap .right {
  flex: 0.65;
  filter: blur(var(--blur)) saturate(0.7) brightness(0.85);
}

#pick-right:checked ~ .duel-wrap .right {
  flex: 1.35;
  filter: none; /* Keep clear after click */
}

#pick-right:checked ~ .duel-wrap .left {
  flex: 0.65;
  filter: blur(var(--blur)) saturate(0.5) brightness(0.85);
}

/* Default both sides are blurred */
.duel-wrap .panel {
  filter: blur(var(--blur)) saturate(0.5) brightness(0.85);
  transition: all 0.6s ease;
}

/* Hover preview effects */
.duel-wrap .panel:hover {
  filter: none;
  flex: 1.35;
}

/* Non-hover panels remain blurred */
.duel-wrap .panel:not(:hover) {
  filter: blur(var(--blur)) saturate(0.5) brightness(0.85);
}


/* Tour Activities Section */
.tour-activities-section {
  margin-top: 50px;
}

.tour-section-title {
  text-align: center;
  margin-bottom: 30px;
  color: white;
  font-size: 28px;
  font-weight: 600;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

/* Hero Tour Card */
.hero-tour-card {
  position: relative;
  width: 60%;
  max-width: 100%;
  height: 260px;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.4);
  margin: 0 auto 30px;
  min-width: 0;
}

.hero-tour-card img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.5s ease;
}

.hero-tour-card:hover img {
  transform: scale(1.05);
}

.hero-tour-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.1));
  padding: 20px;
}

.hero-tour-title {
  font-size: 28px;
  font-weight: 800;
  margin: 0 0 8px;
  color: white;
}

.hero-tour-desc {
  font-size: 16px;
  margin: 0 0 12px;
  opacity: 0.9;
  color: white;
}

.hero-tour-btn {
  display: inline-block;
  background: #e8f1fb;
  color: #0f3a67;
  font-weight: bold;
  text-decoration: none;
  padding: 10px 18px;
  border-radius: 999px;
  transition: all 0.2s ease;
}

.hero-tour-btn:hover {
  transform: translateY(-2px);
  filter: brightness(0.95);
}

/* Sub Activities Grid */
.sub-activities-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  min-width: 0;
}

.sub-activity-card {
  position: relative;
  border-radius: 16px;
  overflow: hidden;
  height: 200px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.35);
  transition: transform 0.3s ease;
  min-width: 0;
}

.sub-activity-card:hover {
  transform: translateY(-6px);
}

.sub-activity-card img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.sub-activity-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.65), rgba(0, 0, 0, 0.15));
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  padding: 16px;
}

.sub-activity-title {
  margin: 0 0 6px;
  font-weight: 700;
  font-size: 16px;
  color: white;
}

.sub-activity-btn {
  align-self: flex-start;
  padding: 6px 14px;
  border-radius: 999px;
  background: #fefefe;
  color: #1a365d;
  font-size: 13px;
  text-decoration: none;
  font-weight: 600;
  transition: all 0.2s ease;
}

.sub-activity-btn:hover {
  background: #dce7f9;
}

/* Tips & Conservation Section */
.tips-conservation-section {
  margin-top: 50px;
  display: flex;
  flex-direction: column;
  gap: 30px;
}

/* Responsive Design for Travel Info */
@media (max-width: 768px) {
  .travel-info-section {
    margin-top: 40px;
    gap: 25px;
  }
  
  .info-card {
    padding: 25px;
  }
  
  .info-title {
    font-size: 20px;
  }
  
  .info-content {
    font-size: 15px;
  }
}

@media (max-width: 480px) {
  .travel-info-section {
    margin-top: 30px;
    gap: 20px;
  }
  
  .info-card {
    padding: 20px;
  }
  
  .info-title {
    font-size: 18px;
  }
  
  .info-content {
    font-size: 14px;
  }
  
  .reminder {
    padding: 12px 15px;
  }
  
  /* Hotel cards responsive */
  .hotel-row {
    gap: 20px;
    padding: 10px 0;
  }
  
  .hotel-card {
    width: 280px;
  }
  
  .hotel-section-title {
    font-size: 24px;
  }
}

@media (max-width: 480px) {
  .hotel-row {
    gap: 15px;
    padding: 10px 0;
  }
  
  .hotel-card {
    width: 250px;
  }
  
  .hotel-section-title {
    font-size: 22px;
  }
  
  .hotel-name {
    font-size: 15px;
  }
  
  .hotel-desc {
    font-size: 13px;
  }
  
  /* Tours responsive */
  .duel-wrap {
    flex-direction: column;
    height: auto;
  }
  
  .vs {
    display: none;
  }
  
  .tours-title {
    font-size: 24px;
  }
  
  .tours-subtitle {
    font-size: 15px;
  }
  
  /* Tips & Conservation responsive */
  .tips-conservation-section {
    margin-top: 40px;
    gap: 25px;
  }
  
  /* Tour Activities responsive */
  .sub-activities-grid {
    grid-template-columns: 1fr;
  }
  
  .hero-tour-card {
    height: 220px;
    width: 80%;
  }
}

/* Carousel positioning */
.card.center {
  transform: translateX(0) scale(1);
  z-index: 50;
}

.card.l1 {
  transform: translateX(-200px) scale(0.75);
  z-index: 40;
}

.card.l2 {
  transform: translateX(-400px) scale(0.65);
  z-index: 30;
  opacity: 0.7;
}

.card.r1 {
  transform: translateX(200px) scale(0.75);
  z-index: 40;
}

.card.r2 {
  transform: translateX(400px) scale(0.65);
  z-index: 30;
  opacity: 0.7;
}

.card.off-left {
  transform: translateX(-600px) scale(0.55);
  opacity: 0;
  z-index: 10;
}

.card.off-right {
  transform: translateX(600px) scale(0.55);
  opacity: 0;
  z-index: 10;
}


/* Empty Content */
.empty-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  text-align: center;
  color: white;
}

.empty-icon {
  font-size: 80px;
  margin-bottom: 24px;
  opacity: 0.8;
}

.empty-content h2 {
  font-size: 36px;
  font-weight: 600;
  margin: 0 0 16px 0;
  color: #63b3ed;
}

.empty-content p {
  font-size: 18px;
  opacity: 0.8;
  margin: 0;
}

/* Footer */
.page-footer {
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

/* Responsive Design */
@media (max-width: 768px) {
  .nav-items {
    gap: 16px;
  }
  
  .nav-item {
    padding: 6px 12px;
    font-size: 14px;
  }
  
  .island-title {
    font-size: 36px;
  }
  
  .island-subtitle {
    font-size: 18px;
  }
  
  .section-title {
    font-size: 28px;
  }
  
  .health-stats {
    flex-direction: column;
    gap: 24px;
  }
  
  .overview-grid,
  .travel-info-grid {
    grid-template-columns: 1fr;
  }
  
  .gallery-grid {
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  }
}

/* Arc Gallery Activities Styles */
.arc-activities-section {
  margin: 50px 0;
  text-align: center;
}

.arc-section-title {
  font-size: 32px;
  font-weight: 700;
  color: #ffffff;
  margin-bottom: 40px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.arc {
  position: relative;
  height: 300px;
  margin: 40px auto;
  width: min(100%, 1100px);
}

.arc .orbit {
  position: absolute;
  inset: 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.arc .ph {
  position: absolute;
  width: 320px;
  height: 200px;
  border-radius: 22px;
  overflow: hidden;
  transform-origin: center bottom;
  transition: transform .3s ease, box-shadow .3s;
  box-shadow: 0 16px 40px rgba(0,0,0,.35);
}

.arc .ph:nth-child(1) {
  transform: rotate(-12deg) translateY(12px);
  left: 6%;
}

.arc .ph:nth-child(2) {
  transform: rotate(0deg);
  left: calc(50% - 160px);
}

.arc .ph:nth-child(3) {
  transform: rotate(12deg) translateY(12px);
  right: 6%;
}

.arc .ph:hover {
  transform: translateY(-6px) scale(1.03);
  box-shadow: 0 24px 60px rgba(0,0,0,.45);
}

.arc img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.arc .ph-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, rgba(0,0,0,0.1), rgba(0,0,0,0.7));
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  padding: 16px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.arc .ph:hover .ph-overlay {
  opacity: 1;
}

.arc .ph-title {
  color: #ffffff;
  font-size: 18px;
  font-weight: 700;
  margin: 0 0 8px 0;
  text-shadow: 0 2px 4px rgba(0,0,0,0.5);
}

.arc .ph-desc {
  color: #ffffff;
  font-size: 14px;
  margin: 0 0 12px 0;
  line-height: 1.4;
  text-shadow: 0 1px 2px rgba(0,0,0,0.5);
}

.arc .cta {
  position: absolute;
  left: 14px;
  bottom: 14px;
  background: #e8f1fb;
  color: #0f3a67;
  padding: 8px 14px;
  border-radius: 999px;
  font-weight: 800;
  text-decoration: none;
  transition: filter 0.2s ease;
  z-index: 10;
}

.arc .cta:hover { 
  filter: brightness(.95); 
}

/* Responsive Arc Gallery */
@media (max-width: 900px) {
  .arc { 
    height: auto; 
  }
  
  .arc .orbit {
    position: relative;
    flex-direction: column;
    gap: 16px;
  }
  
  .arc .ph {
    position: relative;
    left: auto; 
    right: auto;
    transform: none;
    margin: 0 auto;
  }
}

@media (max-width: 768px) {
  .arc-section-title {
    font-size: 28px;
    margin-bottom: 30px;
  }
  
  .arc .ph {
    width: 280px;
    height: 180px;
  }
}

@media (max-width: 480px) {
  .arc-section-title {
    font-size: 24px;
    margin-bottom: 20px;
  }
  
  .arc .ph {
    width: 250px;
    height: 160px;
  }
  
  .arc .ph-title {
    font-size: 16px;
  }
  
  .arc .ph-desc {
    font-size: 12px;
  }
  
  .arc .cta {
    padding: 6px 12px;
    font-size: 14px;
  }
}

/* Polaroid Operators Gallery Styles */
.polaroid-operators-section {
  margin: 50px 0;
  text-align: center;
}

.polaroid-section-title {
  font-size: 32px;
  font-weight: 700;
  color: #ffffff;
  margin-bottom: 40px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.polaroids {
  display: flex;
  justify-content: center;
  gap: 26px;
  flex-wrap: wrap;
}

.pol {
  width: 260px;
  background: #fff;
  color: #111;
  border-radius: 12px;
  padding: 10px 10px 16px;
  box-shadow: 0 18px 40px rgba(0,0,0,.35);
  transform: rotate(-4deg);
  transition: transform .25s ease, box-shadow .25s ease;
}

.pol:nth-child(2) {
  transform: rotate(2deg);
}

.pol:nth-child(3) {
  transform: rotate(-1.5deg);
}

.pol:hover {
  transform: translateY(-6px) rotate(0);
  box-shadow: 0 26px 70px rgba(0,0,0,.45);
}

.ph {
  position: relative;
  height: 200px;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 10px;
  background: #eef2ff;
}

.ph img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.ph-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, rgba(0,0,0,0.1), rgba(0,0,0,0.7));
  display: flex;
  align-items: flex-end;
  padding: 12px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.pol:hover .ph-overlay {
  opacity: 1;
}

.ph-desc {
  color: #ffffff;
  font-size: 13px;
  margin: 0;
  line-height: 1.3;
  text-shadow: 0 1px 2px rgba(0,0,0,0.5);
}

.title {
  font-weight: 900;
  margin: 0 0 6px;
  font-size: 18px;
}

.btn {
  display: inline-block;
  margin-top: 6px;
  background: #0f3a67;
  color: #fff;
  text-decoration: none;
  padding: 8px 14px;
  border-radius: 10px;
  font-weight: 800;
  box-shadow: 0 8px 18px rgba(0,0,0,.25);
  transition: transform .15s ease, filter .15s ease;
}

.btn:hover {
  transform: translateY(-2px);
  filter: brightness(.96);
}

/* Responsive Polaroid Gallery */
@media (max-width: 768px) {
  .polaroid-section-title {
    font-size: 28px;
    margin-bottom: 30px;
  }
  
  .pol {
    width: 240px;
  }
  
  .ph {
    height: 180px;
  }
}

@media (max-width: 640px) {
  .pol {
    width: 88%;
  }
  
  .polaroids {
    gap: 20px;
  }
  
  .ph-desc {
    font-size: 12px;
  }
  
  .title {
    font-size: 16px;
  }
  
  .btn {
    padding: 6px 12px;
    font-size: 14px;
  }
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

/* Action Buttons Section Styles */
.action-buttons-section {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin: 40px 0;
  padding: 30px 0;
  flex-wrap: wrap;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 12px;
  border: none;
  padding: 18px 40px;
  border-radius: 50px;
  font-size: 1.2rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.4s ease;
  position: relative;
  overflow: hidden;
  min-width: 280px;
}

.reef-policy-btn {
  background: linear-gradient(135deg, #01A2EB 0%, #0077BE 100%);
  color: white;
  box-shadow: 0 8px 25px rgba(1, 162, 235, 0.35);
}

.education-btn {
  background: linear-gradient(135deg, #10B981 0%, #059669 100%);
  color: white;
  box-shadow: 0 8px 25px rgba(16, 185, 129, 0.35);
}

.action-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.6s ease;
}

.action-btn:hover::before {
  left: 100%;
}

.reef-policy-btn:hover {
  transform: translateY(-3px) scale(1.05);
  box-shadow: 0 12px 35px rgba(1, 162, 235, 0.5);
  background: linear-gradient(135deg, #0077BE 0%, #005F9E 100%);
}

.education-btn:hover {
  transform: translateY(-3px) scale(1.05);
  box-shadow: 0 12px 35px rgba(16, 185, 129, 0.5);
  background: linear-gradient(135deg, #059669 0%, #047857 100%);
}

.action-btn:active {
  transform: translateY(-1px) scale(1.02);
}

.reef-policy-btn:active {
  box-shadow: 0 6px 20px rgba(1, 162, 235, 0.4);
}

.education-btn:active {
  box-shadow: 0 6px 20px rgba(16, 185, 129, 0.4);
}

.action-btn .btn-icon {
  font-size: 1.5rem;
  animation: float 2s ease-in-out infinite;
}

.action-btn .btn-text {
  font-size: 1.1rem;
  letter-spacing: 0.5px;
}

.action-btn .btn-arrow {
  font-size: 1.4rem;
  transition: transform 0.3s ease;
}

.action-btn:hover .btn-arrow {
  transform: translateX(5px);
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-3px); }
}

/* Responsive adjustments for action buttons */
@media (max-width: 768px) {
  .action-buttons-section {
    flex-direction: column;
    align-items: center;
    gap: 15px;
  }

  .action-btn {
    padding: 15px 30px;
    font-size: 1rem;
    min-width: 250px;
  }

  .action-btn .btn-icon {
    font-size: 1.3rem;
  }

  .action-btn .btn-text {
    font-size: 1rem;
  }

  .action-btn .btn-arrow {
    font-size: 1.2rem;
  }
}
</style>
