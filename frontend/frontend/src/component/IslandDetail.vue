<template>
  <div class="island-detail-container" :key="islandName">
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
            <span class="nav-item active">Island</span>
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
          <span class="nav-item" @click="goToGovernment">Government</span>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">

      <!-- Content Sections -->
      <div class="content-sections" v-if="islandName === 'P Singa'">
        <div class="section-container">
          
          
          <!-- Description Section -->
          <div class="description-section">
            <h3 class="description-title">Pulau Singa, Langkawi</h3>
            <div class="description-content">
              <h4>Description</h4>
              <p>Pulau Singa, part of the Langkawi archipelago in Kedah, is a small island within the Langkawi Marine Park. Known for its clear waters, shallow reefs, and peaceful sandy stretches, it is often included in island-hopping itineraries from Langkawi. The island is uninhabited and does not have accommodation or permanent facilities, making it primarily a day-trip and snorkeling stop.</p>
            </div>
          </div>
          
          <!-- Carousel Section -->
          <div class="carousel-section">
            <div class="carousel" id="carousel">
              <div class="card" v-for="(image, index) in carouselImages" :key="index">
                <img :src="image" :alt="`P Singa Image ${index + 1}`">
                <div class="card-source">
                  Source: <a href="https://naturallylangkawi.my/portfolio/pulau-singa-besar/" target="_blank">naturallylangkawi.my</a>
                </div>
              </div>
            </div>
          </div>
          
          <!-- Travel Information Section -->
          <div class="travel-info-section">
            <div class="travel-info-section">
  <div class="info-card">
    <h3 class="info-title">Travel Information</h3>
    <div class="info-content">
      <p><strong>Arrive:</strong> Pulau Singa is reached by speedboat from Langkawi (20–40 min from Kuah Jetty or Pantai Cenang), often combined with Dayang Bunting and Beras Basah in island-hopping tours.</p>
      
      <p class="reminder"><strong>Reminder:</strong> No jetty exists, so landings depend on weather; some trips may be snorkeling-only.</p>
      
      <p><strong>Snorkeling & Diving:</strong> Shallow coral gardens suit beginners and families, with colorful fish (clownfish, damselfish, wrasses) and occasional turtles. Diving is limited but available via Langkawi Marine Park packages.</p>
      
      <p><strong>Accommodation:</strong> No stays on Pulau Singa; visitors stay in Langkawi, where hotels and eco-lodges are available. Operators arrange day trips.</p>
    </div>
  </div>
</div>

            
            <!-- Hotel Recommendation Section -->
            <div class="hotel-recommendation-section">
              <h3 class="hotel-section-title">Hotel / Resort Recommendation</h3>
              <div class="hotel-row">
                <div class="hotel-card">
                  <div class="hotel-img">
                    <img :src="require('@/assets/P Singa/Datai Langkawi.jpg')" alt="The Datai Langkawi">
                  </div>
                  <div class="hotel-body">
                    <div class="hotel-name">The Datai Langkawi</div>
                    <div class="hotel-desc">Luxury rainforest resort with stunning Andaman Sea views</div>
                  </div>
                  <a class="hotel-book" href="https://www.thedatai.com/" target="_blank">Booking</a>
                </div>

                <div class="hotel-card">
                  <div class="hotel-img">
                    <img :src="require('@/assets/P Singa/The Danna Langkawi.jpg')" alt="The Danna Langkawi">
                  </div>
                  <div class="hotel-body">
                    <div class="hotel-name">The Danna Langkawi</div>
                    <div class="hotel-desc">Colonial-style luxury resort with private beach access</div>
                  </div>
                  <a class="hotel-book" href="https://www.thedanna.com/" target="_blank">Booking</a>
                </div>

                <div class="hotel-card">
                  <div class="hotel-img">
                    <img :src="require('@/assets/P Singa/The Frangipani Langkawi Resort & Spa.jpg')" alt="The Frangipani Langkawi Resort & Spa">
                  </div>
                  <div class="hotel-body">
                    <div class="hotel-name">The Frangipani Langkawi</div>
                    <div class="hotel-desc">Award-winning eco-friendly resort with beachfront location</div>
                  </div>
                  <a class="hotel-book" href="https://www.frangipanilangkawi.com/" target="_blank">Booking</a>
                </div>

                <div class="hotel-card">
                  <div class="hotel-img">
                    <img :src="require('@/assets/P Singa/Ambong ambong.png')" alt="Ambong Ambong">
                  </div>
                  <div class="hotel-body">
                    <div class="hotel-name">Ambong Ambong</div>
                    <div class="hotel-desc">Sustainable luxury villas with private pools and sea views</div>
                  </div>
                  <a class="hotel-book" href="https://www.ambong-ambong.com/" target="_blank">Booking</a>
                </div>
              </div>
            </div>
            
            <!-- Operators & Tours Section -->
            <div class="operators-tours-section">
              <div class="tours-header">
                <h3 class="tours-title">Operators & Tours</h3>
                <p class="tours-subtitle">Island-hopping tours from Pantai Cenang and Kuah Jetty (multiple local operators)</p>
                <p class="tours-subtitle">Custom eco-tours available with licensed Langkawi Marine Park guides.</p>
              </div>
              
              <!-- Tours Duel Panels -->
              <div class="tours-duel-section">
                <input class="duel-input" type="radio" name="pick" id="pick-left" checked>
                <input class="duel-input" type="radio" name="pick" id="pick-right">
                
                <div class="duel-wrap">
                  <!-- Left: PADI Dive Center -->
                  <label class="panel left" for="pick-left" title="Choose Diving">
                    <img :src="require('@/assets/P Singa/tour 1.png')" alt="PADI Langkawi Scuba">
                    <div class="meta">
                      <div>
                        <h3 class="title">PADI Langkawi Scuba – Dive Center</h3>
                        <ul class="points">
                          <li>Boat dives around Langkawi Marine Park</li>
                          <li>Beginner friendly & certification options</li>
                        </ul>
                      </div>
                      <a class="cta" href="https://www.padi.com/dive-center/malaysia/langkawi-scuba/" target="_blank" rel="noopener">Check Tour</a>
                    </div>
                  </label>

                  <div class="vs"><span>VS</span></div>

                  <!-- Right: Jet Ski Tour -->
                  <label class="panel right" for="pick-right" title="Choose Jet Ski">
                    <img :src="require('@/assets/P Singa/tour 2.png')" alt="Langkawi Jet Ski Tour">
                    <div class="meta">
                      <div>
                        <h3 class="title">UNESCO Geopark Jet-Ski – 8 Islands</h3>
                        <ul class="points">
                          <li>Island-hopping from Pantai Cenang</li>
                          <li>Guide & safety briefing included</li>
                        </ul>
                      </div>
                      <a class="cta" href="https://www.getyourguide.com/en-gb/langkawi-l903/langkawi-premium-unesco-geopark-jet-ski-tour-of-8-islands-t24907/" target="_blank" rel="noopener">Check Tour</a>
                    </div>
                  </label>
                </div>
              </div>
            </div>
            
            <!-- Tips & Conservation Section -->
            <div class="tips-conservation-section">
              <!-- Tips Section -->
              <div class="info-card">
                <h3 class="info-title">Tips</h3>
                <div class="info-content">
                  <p>The best time to visit is during the dry season (November – April) when seas are calmer and visibility is better.</p>
                  <p>During the wetter months (May – October), sea conditions may be choppier.</p>
                  <p>Watch out for jellyfish, which may occasionally be present in the waters.</p>
                  <p>Don't miss the sunrise and sunset — true highlights of the island.</p>
                </div>
              </div>
              
              <!-- Conservation Section -->
              <div class="info-card">
                <h3 class="info-title">Conservation-friendly practices</h3>
                <div class="info-content">
                  <p>Visitors are encouraged to use reef-safe sunscreen, avoid touching or stepping on corals and seagrass beds, and take all rubbish back to Langkawi as the island has no waste facilities. Feeding fish or disturbing marine life should be avoided, and tours are best arranged with licensed operators who follow Langkawi Marine Park guidelines to ensure the reefs remain healthy for future visitors.</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Mertang Content Sections -->
      <div class="content-sections" v-else-if="islandName === 'Mertang'">
        <div class="section-container">
          
          <!-- Description Section -->
          <div class="description-section">
            <h3 class="description-title">Pulau Mertang, Johor</h3>
            <div class="description-content">
              <h4>Description</h4>
              <p>Pulau Mertang, situated within the state of Johor, Malaysia, comprises a group of very small offshore islets (Mertang Barat / Mertang Timur) located off the east coast of Johor near the waters of Mersing. Geographical records classify them as 'islets' (small islands or sandbanks), typically lacking accommodation.</p>
            </div>
          </div>
          
          <!-- Carousel Section -->
          <div class="carousel-section">
            <div class="carousel" id="carousel">
              <div class="card" v-for="(image, index) in mertangCarouselImages" :key="index">
                <img :src="image" :alt="`Mertang Image ${index + 1}`">
                <div class="card-source">
                  Source: <a :href="mertangCarouselSources[index].url" target="_blank">{{ mertangCarouselSources[index].text }}</a>
                </div>
              </div>
            </div>
          </div>
          
          <!-- Travel Information Section -->
          <div class="travel-info-section">
            <!-- Arrive Section -->
            <div class="info-card">
              <h3 class="info-title">Arrive</h3>
              <div class="info-content">
                <p>You may choose to depart from piers in the Mersing/Endau area. Please confirm with local boat operators or resort management whether they offer stops at Mertang's island-hopping/snorkelling sites. It is advisable to combine Mertang with more commonly visited neighbouring island groups (such as Rawa, Sembilang, Sri Buat, Dayang, etc.) to enhance trip feasibility and route flexibility. The geographical and destination overview indicates that the Mersing area serves as the primary departure point for accessing these offshore islands.</p>
                <p class="reminder"><strong>Kindly reminder:</strong> Mertang is a very small island with no publicly available permanent landing facilities or scheduled ferry services. It is essential to confirm with the boat operator the day's sea conditions, whether snorkelling will be the sole activity without island landings, and other details (to avoid trampling/disturbing bird or reptile habitats).</p>
              </div>
            </div>
            
            <!-- Snorkeling/Diving Section -->
            <div class="info-card">
              <h3 class="info-title">Snorkeling / Diving</h3>
              <div class="info-content">
                <p>If you choose to snorkel in coastal activities in the Pulau Mertang area, please adhere to the principles of "no touching, no trampling, and no collecting".</p>
              </div>
            </div>
            
            <!-- Accommodation Section -->
            <div class="info-card">
              <h3 class="info-title">Accommodation</h3>
              <div class="info-content">
                <p><strong>Mersing / Johor</strong></p>
                <p>As Mertang consists primarily of uninhabited islets, accommodation is typically arranged in Mersing or neighbouring islands such as Rawa. From there, visitors undertake day trips, island-hopping, or charter shared boats to explore the Mertang waters. Rawa offers comprehensive destination guides and resort product information, facilitating tailored trips in collaboration with boat operators.</p>
              </div>
            </div>
            
            <!-- Hotel Recommendation Section -->
            <div class="hotel-recommendation-section">
              <h3 class="hotel-section-title">Hotel / Resort Recommendation</h3>
              <div class="hotel-row">
                <div class="hotel-card">
                  <div class="hotel-img">
                    <img :src="require('@/assets/Mertang/rawaislandresort.png')" alt="Rawa Island Resort">
                  </div>
                  <div class="hotel-body">
                    <div class="hotel-name">Rawa Island Resort</div>
                    <div class="hotel-desc">Idyllic tropical island resort with pristine beaches and coral reefs</div>
                  </div>
                  <a class="hotel-book" href="https://rawaislandresort.com/" target="_blank">Booking</a>
                </div>

                <div class="hotel-card">
                  <div class="hotel-img">
                    <img :src="require('@/assets/Mertang/orcanation.png')" alt="Orca Nation Resort">
                  </div>
                  <div class="hotel-body">
                    <div class="hotel-name">Orca Nation Resort</div>
                    <div class="hotel-desc">Private island escape with crystal-clear waters and white sand beaches</div>
                  </div>
                  <a class="hotel-book" href="https://www.alangsrawa.com/" target="_blank">Booking</a>
                </div>
              </div>
            </div>
            
            <!-- Tour Activities Section -->
            <div class="tour-activities-section">
              <h3 class="tour-section-title">Tour Activities & Adventures</h3>
              
              <!-- Hero Diving Card -->
              <div class="hero-tour-card">
                <img :src="require('@/assets/Mertang/diving.jpg')" alt="Diving">
                <div class="hero-tour-overlay">
                  <h2 class="hero-tour-title">Orca Nation Diving</h2>
                  <p class="hero-tour-desc">Discover the underwater world with certified guides and pristine coral reefs.</p>
                  <a href="https://orcanation.org/diving/" target="_blank" class="hero-tour-btn">Explore Diving</a>
                </div>
              </div>

              <!-- Sub Activities Grid -->
              <div class="sub-activities-grid">
                <div class="sub-activity-card">
                  <img :src="require('@/assets/Mertang/rawaislandresort.png')" alt="Rawa Island Resort">
                  <div class="sub-activity-overlay">
                    <h3 class="sub-activity-title">Rawa Island Resort</h3>
                    <a href="https://rawaislandresort.com/" target="_blank" class="sub-activity-btn">Book</a>
                  </div>
                </div>
                <div class="sub-activity-card">
                  <img :src="require('@/assets/Mertang/orcanation.png')" alt="Orca Nation">
                  <div class="sub-activity-overlay">
                    <h3 class="sub-activity-title">Orca Nation</h3>
                    <a href="https://www.alangsrawa.com/" target="_blank" class="sub-activity-btn">Book</a>
                  </div>
                </div>
                <div class="sub-activity-card">
                  <img :src="require('@/assets/Mertang/vacation_279.png')" alt="Island Hopping">
                  <div class="sub-activity-overlay">
                    <h3 class="sub-activity-title">Island Hopping</h3>
                    <a href="https://ami.travel/en/vacations/279" target="_blank" class="sub-activity-btn">Book</a>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- Tips & Conservation Section -->
            <div class="tips-conservation-section">
              <!-- Tips Section -->
              <div class="info-card">
                <h3 class="info-title">Tips</h3>
                <div class="info-content">
                  <p>During the monsoon season (around November to February), sea conditions are unstable and trips are frequently adjusted or cancelled.</p>
                </div>
              </div>
              
              <!-- Conservation Section -->
              <div class="info-card">
                <h3 class="info-title">Conservation-friendly practices</h3>
                <div class="info-content">
                  <p>Use reef-safe sunscreen, avoid touching the seabed or grabbing objects while snorkelling, don't feed fish, and don't tread on nesting areas on sandbanks. Rawa is situated within Sultan Iskandar Marine Park, where fishing and motorised water activities are restricted.</p>
                </div>
              </div>
            </div>
          </div>
          
        </div>
      </div>
      
      <!-- Sipadan Content Sections -->
      <div class="content-sections" v-else-if="islandName === 'Sipadan'">
        <div class="section-container">
          
          <!-- Description Section -->
          <div class="description-section">
            <h3 class="description-title">Pulau Sipadan, Sabah</h3>
            <div class="description-content">
              <h4>Description</h4>
              <p>Pulau Sipadan, located off the east coast of Sabah, is Malaysia's only true oceanic island, formed by an extinct volcano rising 600 metres from the seabed. Recognised worldwide as one of the top diving destinations, Sipadan is home to extraordinary marine biodiversity including turtles, barracuda, reef sharks, and vibrant coral walls. The island is a protected marine park, and overnight stays are not permitted.</p>
            </div>
          </div>
          
          <!-- Carousel Section -->
          <div class="carousel-section">
            <div class="carousel" id="carousel">
              <div class="card" v-for="(image, index) in sipadanCarouselImages" :key="index">
                <img :src="image" :alt="`Sipadan Image ${index + 1}`">
                <div class="card-source">
                  Source: <a href="#" target="_blank">CoralKita</a>
                </div>
              </div>
            </div>
          </div>
          
          <!-- Travel Information Section -->
          <div class="travel-info-section">
            <!-- Arrive Section -->
            <div class="info-card">
              <h3 class="info-title">Arrive</h3>
              <div class="info-content">
                <p>Access to Sipadan is via the town of Semporna. Visitors usually fly to Tawau Airport in Sabah from major cities such as Kuala Lumpur or Kota Kinabalu. From Tawau, it is about a 1-hour drive to Semporna, followed by a 45–60 minute speedboat ride from the Semporna Jetty to Sipadan, or to nearby islands such as Mabul or Kapalai, where resorts and dive operators arrange day trips to Sipadan.</p>
                <p class="reminder"><strong>Reminder:</strong> Sipadan is a protected marine park with no overnight stays or public facilities. Access is limited to 120 permits per day via licensed dive operators, so visitors should confirm permits, boat schedules, and sea conditions well in advance.</p>
              </div>
            </div>
            
            <!-- Snorkeling/Diving Section -->
            <div class="info-card">
              <h3 class="info-title">Snorkeling / Diving</h3>
              <div class="info-content">
                <p>Sipadan is globally recognised as a premier diving destination. The island's steep drop-offs and surrounding reefs provide some of the most spectacular underwater experiences in the world.</p>
                <p><strong>Iconic dive sites:</strong> Barracuda Point, Turtle Cavern, and The Drop-Off.</p>
                <p><strong>Marine life:</strong> Large schools of jackfish, swirling barracuda, bumphead parrotfish, reef sharks, and dozens of green and hawksbill turtles. Advanced divers may also encounter hammerheads and whale sharks during certain seasons.</p>
                <p><strong>Snorkeling:</strong> Limited snorkeling is possible in shallow areas, but the island is primarily suited to experienced divers due to depth, currents, and the protected permit system.</p>
              </div>
            </div>
            
            <!-- Accommodation Section -->
            <div class="info-card">
              <h3 class="info-title">Accommodation</h3>
              <div class="info-content">
                <p>There is no accommodation on Sipadan. Most visitors stay on nearby islands such as Mabul or Kapalai, which offer resorts, water chalets, and full dive packages including Sipadan permits.</p>
              </div>
            </div>
          </div>
          
          <!-- Hotel Recommendation Section -->
          <div class="hotel-recommendation-section">
            <h3 class="hotel-section-title">Hotel / Resort Recommendation</h3>
            <div class="hotel-row">
              <div class="hotel-card">
                <div class="hotel-img">
                  <img :src="require('@/assets/Sipadan/Kapalai_Dive.png')" alt="Sipadan Kapalai Dive Resort">
                </div>
                <div class="hotel-body">
                  <div class="hotel-name">Sipadan Kapalai Dive Resort</div>
                  <div class="hotel-desc">Water village resort on stilts with world-class diving access to Sipadan</div>
                </div>
                <a class="hotel-book" href="https://dive-malaysia.com/sipadan-kapalai-dive-resort/" target="_blank">Booking</a>
              </div>
              <div class="hotel-card">
                <div class="hotel-img">
                  <img :src="require('@/assets/Sipadan/Mabul_Beach.png')" alt="Mabul Beach Resort">
                </div>
                <div class="hotel-body">
                  <div class="hotel-name">Mabul Beach Resort</div>
                  <div class="hotel-desc">Eco-friendly beachfront resort with award-winning PADI dive center</div>
                </div>
                <a class="hotel-book" href="https://www.scuba-junkie.com/mabul-beach-resort/" target="_blank">Booking</a>
              </div>
              <div class="hotel-card">
                <div class="hotel-img">
                  <img :src="require('@/assets/Sipadan/Seaventures_Dive.png')" alt="Seaventures Dive Rig">
                </div>
                <div class="hotel-body">
                  <div class="hotel-name">Seaventures Dive Rig</div>
                  <div class="hotel-desc">Unique oil rig converted into luxury dive resort with direct Sipadan access</div>
                </div>
                <a class="hotel-book" href="https://seaventuresdive.com/" target="_blank">Booking</a>
              </div>
            </div>
          </div>
          
          <!-- Tips & Conservation Section -->
          <div class="tips-conservation-section">
            <!-- Tips Section -->
            <div class="info-card">
              <h3 class="info-title">Tips</h3>
              <div class="info-content">
                <p>The best time to visit is April – December, when seas are calm and visibility is 20–40 metres. From January – March, seas may be rougher, though there are fewer tourists.</p>
                <p>Book early as permits are limited to 120 per day and usually tied to resort or dive packages. Carry dive certification cards; Sipadan is not suitable for beginners.</p>
                <p>There are no facilities on the island; accommodation, meals, and dive logistics are arranged from Mabul, Kapalai, or Semporna. Bring reef-safe sunscreen and other essentials.</p>
              </div>
            </div>
            
            <!-- Conservation Section -->
            <div class="info-card">
              <h3 class="info-title">Conservation-friendly practices</h3>
              <div class="info-content">
                <p>Sipadan is a strictly protected marine park. Visitors should dive only with licensed operators, avoid touching or disturbing marine life, and maintain proper buoyancy to protect fragile corals.</p>
                <p>Fishing, motorised water activities, and overnight stays are prohibited. Choosing eco-certified dive resorts helps ensure the island remains pristine for future generations.</p>
              </div>
            </div>
          </div>
          
        </div>
      </div>
      
      <!-- Pulau Lima Content -->
      <div class="content-sections" v-else-if="islandName === 'Pulau Lima'">
        <div class="section-container">
          <!-- Description Section -->
          <div class="description-section">
            <h3 class="description-title">Pulau Lima, Terengganu</h3>
            <div class="description-content">
              <h4>Description</h4>
              <p>Pulau Lima comprises a pair of small islands (Lima Besar / Lima Kecil) within the Sultan Iskandar Marine Park. Managed by Johor Parks, the marine park encompasses multiple islands off the coast of Mersing, conducting conservation and educational activities.</p>
            </div>
          </div>
          
          <!-- Carousel Section -->
          <div class="carousel-section">
            <div class="carousel" id="carousel">
              <div class="card" v-for="(image, index) in pulauLimaCarouselImages" :key="index">
                <img :src="image" :alt="`Pulau Lima Image ${index + 1}`">
                <div class="card-source">
                  Source: <a :href="pulauLimaCarouselSources[index].url" target="_blank">{{ pulauLimaCarouselSources[index].text }}</a>
                </div>
              </div>
            </div>
          </div>
          
          <!-- Travel Information Section -->
          <div class="travel-info-section">
            <!-- Arrive Section -->
            <div class="info-card">
              <h3 class="info-title">Arrive</h3>
              <div class="info-content">
                <p>You may first proceed to Mersing in Johor (the staging point for embarking on sea journeys) to complete formalities at Mersing Harbour Centre (Mersing Ferry Terminal Service Centre). Here, you will confirm ferry schedules/assembly points and pay marine park/conservation fees (charges apply for both island access and entry into marine park zones). (<a href="https://mersingharbourcentre.com/" target="_blank">mersingharbourcentre.com</a>)</p>
                <p>Alternatively, you may choose to take a speedboat/chartered vessel or join an organised island-hopping tour from Mersing Jetty to the Pulau Lima area. As an uninhabited island, Pulau Lima typically lacks regular public ferry services. Common approaches include:</p>
                <p>• Booking an island-hopping/snorkelling tour departing from Mersing, allowing the tour leader to determine the day's stops and snorkelling sites based on sea conditions (inquire before booking whether Lima Besar/Lima Kechil waters are included).</p>
                <p>• Stay on nearby Pulau Sibu and arrange offshore trips to the Lima area through local resorts/dive centres (e.g., Sea Gypsy Village Resort & Dive Base explicitly covers the Lima region in its itineraries and dive sites page).</p>
              </div>
            </div>
            
            <!-- Snorkeling/Diving Section -->
            <div class="info-card">
              <h3 class="info-title">Snorkeling / Diving</h3>
              <div class="info-content">
                <p><strong>Lima Besar & Lima Kecil Snorkelling Sites:</strong> Local dive centres and resorts in the Sibu area explicitly list Lima Besar and Lima Kecil as snorkelling routes and coastal experience points, suitable for beginners and families (subject to visibility and sea conditions).</p>
              </div>
            </div>
            
            <!-- Marine Park Snorkeling Card -->
            <div class="hero-tour-card">
              <img :src="require('@/assets/Pulau Lima/dive.png')" alt="Marine Park Snorkeling">
              <div class="hero-tour-overlay">
                <h2 class="hero-tour-title">Marine Park Snorkeling</h2>
                <p class="hero-tour-desc">Explore pristine coral reefs and diverse marine life in Sultan Iskandar Marine Park with certified guides.</p>
                <a href="https://www.siburesort.com/dive-base/snorkel-trips-to-tropical-islands-within-the-marine-park.html" target="_blank" class="hero-tour-btn">Explore Snorkeling</a>
              </div>
            </div>
            
          </div>
          
          <!-- Accommodation Section -->
          <div class="travel-info-section">
            <div class="info-card">
              <h3 class="info-title">Accommodation</h3>
              <div class="info-content">
                <p><strong>Mersing / Johor</strong></p>
                <p>As Pulau Lima consists primarily of uninhabited islets, accommodation is typically arranged in Mersing or neighbouring islands such as Sibu. From there, visitors undertake day trips, island-hopping, or charter shared boats to explore the Lima waters. Sibu offers comprehensive destination guides and resort product information, facilitating tailored trips in collaboration with boat operators.</p>
              </div>
            </div>
          </div>
          
          <!-- Hotel Recommendation Section -->
          <div class="hotel-recommendation-section">
            <h3 class="hotel-section-title">Hotel / Resort Recommendation</h3>
            <div class="hotel-row">
              <div class="hotel-card">
                <div class="hotel-img">
                  <img :src="require('@/assets/Pulau Lima/sibu.png')" alt="Sea Gypsy Village Resort">
                </div>
                <div class="hotel-body">
                  <div class="hotel-name">Sea Gypsy Village Resort</div>
                  <div class="hotel-desc">Eco-friendly resort with professional snorkeling guides and marine park access</div>
                </div>
                <a class="hotel-book" href="https://www.siburesort.com/dive-base/snorkel-trips-to-tropical-islands-within-the-marine-park.html" target="_blank">Booking</a>
              </div>
              <div class="hotel-card">
                <div class="hotel-img">
                  <img :src="require('@/assets/Pulau Lima/rimbaresort.png')" alt="Rimba Resort">
                </div>
                <div class="hotel-body">
                  <div class="hotel-name">Rimba Resort</div>
                  <div class="hotel-desc">Natural bay resort with diving school and pristine beachfront accommodation</div>
                </div>
                <a class="hotel-book" href="https://rimbaresort.com/" target="_blank">Booking</a>
              </div>
            </div>
          </div>
          
          <!-- Arc Gallery Activities Section -->
          <div class="arc-activities-section">
            <h3 class="arc-section-title">Tour Activities & Adventures</h3>
            <div class="arc">
              <div class="orbit">
                <div class="ph">
                  <img :src="require('@/assets/Pulau Lima/act1.png')" alt="MYVilla Recreation">
                  <div class="ph-overlay">
                    <h4 class="ph-title">MYVilla Recreation</h4>
                    <p class="ph-desc">Eco-friendly homestay with fishing, diving, and jungle trekking activities</p>
                  </div>
                  <a class="cta" href="https://myvillasibu.com/Recreation-Facilities.html" target="_blank">Book</a>
                </div>
                <div class="ph">
                  <img :src="require('@/assets/Pulau Lima/act2.png')" alt="Island Hopping">
                  <div class="ph-overlay">
                    <h4 class="ph-title">Island Hopping</h4>
                    <p class="ph-desc">Explore multiple islands with guided tours and snorkeling adventures</p>
                  </div>
                  <a class="cta" href="https://causewaylinkholidays.com/product-detail/?id=2145-Mersing-Island-Hopping-1-Day-Trip" target="_blank">Book</a>
                </div>
                <div class="ph">
                  <img :src="require('@/assets/Pulau Lima/act3.png')" alt="VReal Island Hopping">
                  <div class="ph-overlay">
                    <h4 class="ph-title">VReal Adventures</h4>
                    <p class="ph-desc">Professional island hopping tours with marine park access</p>
                  </div>
                  <a class="cta" href="https://www.tripadvisor.com/Attraction_Review-g298279-d26267273-Reviews-VReal_Mersing_Island_Hopping-Mersing_Mersing_District_Johor.html" target="_blank">Book</a>
                </div>
              </div>
            </div>
          </div>
          
          <!-- Tips Section -->
          <div class="tips-conservation-section">
            <div class="info-card">
              <h3 class="info-title">Tips</h3>
              <div class="info-content">
                <p>Diving conditions and visibility in the Mersing region (best from March to September; multiple operators suspend or reduce services during the northeast monsoon season from November to February).</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Pulau Seri Buat Content -->
      <div class="content-sections" v-else-if="islandName === 'Seri Buat'">
        <div class="section-container">
          <!-- Description Section -->
          <div class="description-section">
            <h3 class="description-title">Pulau Seri Buat, Pahang</h3>
            <div class="description-content">
              <h4>Description</h4>
              <p>Pulau Seri Buat, also known as Pulau Rahsia, is one of Pahang's best-kept secrets within the Sultan Iskandar Marine Park. Known for its limestone cliffs, crystal-clear waters, and secluded beaches, Seri Buat is a peaceful stop often combined with nearby islands such as Pulau Rawa and Pulau Sembilang. Often called the "Krabi of Malaysia" for its resemblance to Thailand's Krabi coastline, the island offers both tranquility and adventure, making it a perfect getaway for eco-travelers and weekend explorers.</p>
            </div>
          </div>
          
          <!-- Carousel Section -->
          <div class="carousel-section">
            <div class="carousel" id="carousel">
              <div class="card" v-for="(image, index) in seriBuatCarouselImages" :key="index">
                <img :src="image" :alt="`Seri Buat Image ${index + 1}`">
                <div class="card-source">
                  Source: <a :href="seriBuatCarouselSources[index].url" target="_blank">{{ seriBuatCarouselSources[index].text }}</a>
                </div>
              </div>
            </div>
          </div>
          
          <!-- Travel Information Section -->
          <div class="travel-info-section">
            <!-- Arrive Section -->
            <div class="info-card">
              <h3 class="info-title">Arrive</h3>
              <div class="info-content">
                <p>Access to Pulau Seri Buat is by boat from either Mersing Jetty (Johor) or Tanjung Gemok Jetty (Pahang), with travel times of about 30–45 minutes depending on the departure point. Visitors typically charter boats or join island-hopping tours that include Seri Buat along with other islands in the Mersing cluster, such as Pulau Rawa, Pulau Sembilang, and Pulau Harimau.</p>
                <p class="reminder"><strong>Reminder:</strong> Seri Buat has no public ferry services, scheduled routes, or permanent jetty. Trips depend on sea conditions, and in some cases, visits may be limited to snorkeling or offshore stops.</p>
              </div>
            </div>
            
            <!-- Snorkeling/Diving Section -->
            <div class="info-card">
              <h3 class="info-title">Snorkeling / Diving</h3>
              <div class="info-content">
                <p>Seri Buat's turquoise waters are ideal for snorkeling, with colorful reef fish and good visibility. While diving opportunities are more developed around nearby islands, Seri Buat is a popular stop for:</p>
                <p>• Snorkeling in sheltered coves</p>
                <p>• Swimming in emerald waters beneath dramatic cliffs</p>
                <p>• Cliff-jumping into the sea at designated safe spots</p>
              </div>
            </div>
            
            <!-- Hiking & Adventure Section -->
            <div class="info-card">
              <h3 class="info-title">Hiking & Adventure</h3>
              <div class="info-content">
                <p>A short 10–15 minute hike leads to a scenic viewpoint with panoramic views of Seri Buat and the surrounding waters. For more adventure, visitors can explore rocky outcrops or join island-hopping tours that include nearby Pulau Harimau, known for its hidden natural pool and popular cliff-jumping spots.</p>
              </div>
            </div>
          </div>
          
          <!-- Recommended Resorts & Operators Section -->
          <div class="polaroid-operators-section">
            <h3 class="polaroid-section-title">Recommended Resorts & Operators</h3>
            <div class="polaroids">
              <div class="pol">
                <div class="ph">
                  <img :src="require('@/assets/Seri Buat/operator1.png')" alt="Rawa Island Resort">
                  <div class="ph-overlay">
                    <p class="ph-desc">Luxury island resort with pristine beaches and water activities</p>
                  </div>
                </div>
                <div class="title">Rawa Island Resort</div>
                <a class="btn" href="https://rawaislandresort.com/" target="_blank">Explore</a>
              </div>
              
              <div class="pol">
                <div class="ph">
                  <img :src="require('@/assets/Seri Buat/operator2.png')" alt="1Step1Footprint">
                  <div class="ph-overlay">
                    <p class="ph-desc">Professional island hopping tours with hiking and snorkeling</p>
                  </div>
                </div>
                <div class="title">1Step1Footprint</div>
                <a class="btn" href="https://1step1footprint.com/johor-island-hopping-day-trip-package/" target="_blank">Explore</a>
              </div>
              
              <div class="pol">
                <div class="ph">
                  <img :src="require('@/assets/Seri Buat/operator3.png')" alt="AMI Travel">
                  <div class="ph-overlay">
                    <p class="ph-desc">8-island day trip package with comprehensive activities</p>
                  </div>
                </div>
                <div class="title">AMI Travel</div>
                <a class="btn" href="https://ami.travel/ms/pakej-percutian/965" target="_blank">Explore</a>
              </div>
            </div>
          </div>
          <!-- Tips Section -->
          <div class="tips-conservation-section">
            <div class="info-card">
              <h3 class="info-title">Tips</h3>
              <div class="info-content">
                <p>The best time to visit is March – October, when seas are calm.</p>
                <p>Avoid November – February (monsoon season), when sea conditions can be rough.</p>
                <p>Bring reef-safe sunscreen, water, and snacks, as there are no facilities on the island.</p>
                <p>Book island-hopping tours in advance to secure your spot.</p>
                <p>Don't miss the short hike to the viewpoint for breathtaking scenery.</p>
                <p>Be cautious when snorkeling or cliff-jumping, and always follow operator guidance.</p>
              </div>
            </div>
            <div class="info-card">
              <h3 class="info-title">Conservation-friendly practices</h3>
              <div class="info-content">
                <p>As part of the Sultan Iskandar Marine Park, Seri Buat is under protection. Visitors should avoid touching or stepping on corals, refrain from feeding fish, and carry all rubbish back to the mainland. Use reef-safe sunscreen to protect marine life, and respect nesting areas on sandbanks or rocky outcrops. Choosing licensed eco-friendly operators helps preserve the island's natural beauty for future visitors.</p>
              </div>
            </div>
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
      <p>&copy; 2024 CoralKita. Protecting Malaysia's Marine Heritage.</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'IslandDetail',
  data() {
    return {
      islandName: '',
      // Travel dropdown related
      showTravelDropdown: false,
      travelIslands: ['Mertang', 'P Singa', 'Sipadan', 'Pulau Lima', 'Seri Buat'],
      // Carousel related
      carouselImages: [
        require('@/assets/P Singa/pic1.png'),
        require('@/assets/P Singa/pic2.png'),
        require('@/assets/P Singa/pic3.png'),
        require('@/assets/P Singa/pic4.png'),
        require('@/assets/P Singa/pic5.png')
      ],
      // Mertang carousel images
      mertangCarouselImages: [
        require('@/assets/Mertang/pic1.png'),
        require('@/assets/Mertang/pic2.png'),
        require('@/assets/Mertang/pic3.png'),
        require('@/assets/Mertang/pic4.png'),
        require('@/assets/Mertang/pic5.png')
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
        require('@/assets/Sipadan/pic1.png'),
        require('@/assets/Sipadan/pic2.png'),
        require('@/assets/Sipadan/pic3.png'),
        require('@/assets/Sipadan/pic4.png'),
        require('@/assets/Sipadan/pic5.png')
      ],
      // Pulau Lima carousel images
      pulauLimaCarouselImages: [
        require('@/assets/Pulau Lima/pic1.png'),
        require('@/assets/Pulau Lima/pic2.png'),
        require('@/assets/Pulau Lima/pic3.png'),
        require('@/assets/Pulau Lima/pic4.png'),
        require('@/assets/Pulau Lima/pic5.png')
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
        require('@/assets/Seri Buat/pic1.png'),
        require('@/assets/Seri Buat/pic2.png'),
        require('@/assets/Seri Buat/pic3.png'),
        require('@/assets/Seri Buat/pic4.png'),
        require('@/assets/Seri Buat/pic5.png')
      ],
      // Seri Buat carousel sources
      seriBuatCarouselSources: [
        { url: 'https://www.facebook.com/photo/?fbid=980541993478215&set=br.AbrY6KWU7FbUIDE4VvDMsPod-RiT5AgSY0JrbpvuxTH2Q-4uRM7QStBuy2_pG-ha4Ki0mi8uMfTbEe_hnvdiYTBfFaQ8e4SgKkgeU-CqIsdzbYE7tZtCR7dSJLghBQl8xWZ7EeZvvP37aT1V7emLzXIG', text: 'facebook.com' },
        { url: 'https://www.instagram.com/p/CNwDHjoJBur/', text: 'instagram.com' },
        { url: 'https://www.facebook.com/photo/?fbid=10159323664036150&set=br.AbrY6KWU7FbUIDE4VvDMsPod-RiT5AgSY0JrbpvuxTH2Q-4uRM7QStBuy2_pG-ha4Ki0mi8uMfTbEe_hnvdiYTBfFaQ8e4SgKkgeU-CqIsdzbYE7tZtCR7dSJLghBQl8xWZ7EeZvvP37aT1V7emLzXIG', text: 'facebook.com' },
        { url: 'https://www.facebook.com/photo/?fbid=10159323662991150&set=br.AbrY6KWU7FbUIDE4VvDMsPod-RiT5AgSY0JrbpvuxTH2Q-4uRM7QStBuy2_pG-ha4Ki0mi8uMfTbEe_hnvdiYTBfFaQ8e4SgKkgeU-CqIsdzbYE7tZtCR7dSJLghBQl8xWZ7EeZvvP37aT1V7emLzXIG', text: 'facebook.com' },
        { url: 'https://www.yusufmadi.com/theuntouchedisland-pulauseribuat', text: 'yusufmadi.com' }
      ],
      currentSlide: 2 // 默认居中第三张
    }
  },
  created() {
    // Get island name from route parameter
    this.islandName = this.$route.params.islandName || 'Unknown Island';
  },
  watch: {
    // 监听路由参数变化
    '$route'(to, from) {
      if (to.params.islandName !== from.params.islandName) {
        console.log('岛屿切换:', from.params.islandName, '->', to.params.islandName);
        this.updateIslandName(to.params.islandName);
      }
    }
  },
  methods: {
    // Update island name and force component update
    updateIslandName(newIslandName) {
      console.log('更新岛屿名称:', this.islandName, '->', newIslandName);
      this.islandName = newIslandName || 'Unknown Island';
      
      // 强制更新组件以确保所有内容都刷新
      this.$forceUpdate();
    },
    
    // Travel navigation methods
    goToIslandDetail(islandName) {
      console.log('导航到岛屿详情页面:', islandName);
      this.showTravelDropdown = false;
      
      // 如果是当前岛屿，不需要切换
      if (islandName === this.islandName) {
        console.log('已经在当前岛屿页面，无需切换');
        return;
      }
      
      this.$nextTick(() => {
        const targetPath = `/travel/${encodeURIComponent(islandName)}`;
        console.log('准备导航到:', targetPath);
        
        this.$router.push(targetPath).then(() => {
          console.log('导航成功');
        }).catch(err => {
          if (err.name !== 'NavigationDuplicated') {
            console.error('Navigation error:', err);
          } else {
            console.log('导航重复，但这是正常的');
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
    
    goToGovernment() {
      this.$router.push('/government').catch(err => {
        if (err.name !== 'NavigationDuplicated') {
          console.error('Navigation error:', err);
        }
      });
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
    }
  },
  
  mounted() {
    this.initCarousel();
  },
  
  updated() {
    this.initCarousel();
  }
}
</script>

<style scoped>
.island-detail-container {
  background-image: url('@/assets/island_bg3.png'); 
  background-size: cover;       /* 保持比例，铺满屏幕 */
  background-position: center;  /* 居中显示 */
  background-repeat: no-repeat; /* 不平铺 */
  background-attachment: fixed; /* 背景固定 */
  background-color: rgba(33, 54, 78, 0.15);
  background-blend-mode: darken;
}

/* Top Navigation Styles */
.top-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 24px;
  background: linear-gradient(90deg, #1A1D25 0%, #01A2EB 100%);
  backdrop-filter: blur(10px);
  color: white;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
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

/* Island 下拉菜单样式 - 改进版 */
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
  padding: 60px 0;
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

/* Description Section */
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

/* Travel Information Section */
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
  filter: none; /* 点击后保持清晰 */
}

#pick-left:checked ~ .duel-wrap .right {
  flex: 0.65;
  filter: blur(var(--blur)) saturate(0.7) brightness(0.85);
}

#pick-right:checked ~ .duel-wrap .right {
  flex: 1.35;
  filter: none; /* 点击后保持清晰 */
}

#pick-right:checked ~ .duel-wrap .left {
  flex: 0.65;
  filter: blur(var(--blur)) saturate(0.5) brightness(0.85);
}

/* 默认两边都是模糊的 */
.duel-wrap .panel {
  filter: blur(var(--blur)) saturate(0.5) brightness(0.85);
  transition: all 0.6s ease;
}

/* Hover preview effects */
.duel-wrap .panel:hover {
  filter: none;
  flex: 1.35;
}

/* 非 hover 的 panel 保持模糊 */
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
  text-align: center;
  padding: 32px;
  background: rgba(0, 0, 0, 0.2);
  color: rgba(255, 255, 255, 0.7);
  border-top: 1px solid rgba(255, 255, 255, 0.1);
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
</style>
