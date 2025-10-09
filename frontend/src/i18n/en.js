export default {
  // Navigation
  nav: {
    logo: 'CoralKita',
    mapRecommendation: {
      line1: 'Map &',
      line2: 'Recommendation'
    },
    island: 'Island',
    education: 'Education',
    aiClassification: 'AI Classification',
    government: 'Government',
    actionHub: 'Action Hub',
    policies: 'Policies',
    faq: 'FAQ',
    changeLanguage: 'Language'
  },
  
  // Home page
  home: {
    coral: {
      healthy: 'Is this the coral reef you remember — bright, colorful, and alive?',
      fading: 'Today, most reefs are fading, struggling under climate change and human pressure.',
      bleached: 'Without action, they may soon become lifeless white skeletons like this.'
    },
    intro: {
      title: 'Plan Smart. Travel Green. Protect Reefs.',
      description: 'CoralKita guides your trip with real reef data — helping you choose the right island, at the right time, the right way, so you can enjoy Malaysia\'s reefs while protecting their beauty.'
    },
    features: {
      dataDriven: 'Data-Driven',
      responsible: 'Responsible',
      userFriendly: 'User-friendly'
    },
    exploreButton: 'Let\'s Explore',
    footer: {
      copyright: '© 2025 CoralKita',
      contact: 'Contact Us'
    }
  },
  
  // Island names
  islands: {
    mertang: 'Mertang',
    pSinga: 'P Singa',
    sipadan: 'Sipadan',
    pulauLima: 'Pulau Lima',
    seriBuat: 'Seri Buat'
  },
  
  // Map page
  map: {
    title: 'Interactive Heat Map',
    dataSource: '*Based on 2024 data',
    gameMode: '🎮 Coral Adventure Game',
    mapMode: '🗺️ Interactive Map',
    selectPanel: {
      title: 'Select Panel',
      filterByLocation: 'Filter by State/Island',
      selectType: 'Select type...',
      state: 'State',
      island: 'Island',
      selectState: 'Select State',
      selectIsland: 'Select Island',
      selectLocation: 'Select Location',
      typeState: 'Type or select a state...',
      typeIsland: 'Type or select an island...',
      selectFirst: 'Select location type first...',
      noStates: 'No states found',
      noIslands: 'No islands found',
      selectLocationLabel: 'Select Location'
    },
    mapInstructions: {
      useLeftPanel: 'Use the left panel to filter by state or island.',
      clickMarkers: 'Click the markers on the map to explore island details.',
      rightPanel: 'The right panel shows recommended islands, which you can click to view more information.'
    },
    coordinates: 'Coordinates',
    recommendedIslands: {
      title: 'Recommended Islands List',
      subtitle: 'The list is based on Live Coral Cover(%) and the overall health status',
      clickToEnter: 'Click to enter the island page'
    },
    comparison: {
      title: 'Island Comparison Visualization',
      panel: {
        title: 'Comparison Panel',
        description: 'Select two islands and an attribute to compare coral reef health trends',
        selectIsland: '🏝️Select Island:',
        compareIsland: '⚖️Compare Island:',
        selectAttribute: '📊Select Attribute:',
        chooseIsland: 'Choose Island',
        placeholder: 'Select at least one island to view data'
      },
      chart: {
        title: 'Island Comparison - Horizon Chart',
        yAxis: 'Percentage Content',
        xAxis: 'Time'
      },
      additionalInfo: {
        title: 'Additional Infomation',
        question: 'Want to understand what each indicator reveals about coral reef conditions?',
        instruction: 'Please scroll down to see the Coral Reef Health Indicators Analysis.'
      }
    },
    indicators: {
      title: 'Coral Reef Health Indicators Analysis',
      subtitle: 'Each indicator (e.g., live coral cover, disturbance, pollution) represents a proportion of the reef condition, and together they add up to 100%, showing how the reef area is distributed across these factors.',
      lcc: {
        title: 'LCC (Live Coral Cover)',
        good: 'Healthy condition, indicating large live coral coverage area and stable ecosystem.',
        moderate: 'Moderate level, still has recovery potential but sensitive to external pressure.',
        poor: 'Severely degraded, may be replaced by algae, sand or rubble, declining ecosystem services.'
      },
      as: {
        title: 'AS (Available Substrate)',
        good: 'Ideal state, most substrate covered by corals.',
        moderate: 'Warning zone, indicating recent coral death or fragmentation.',
        critical: 'Severely degraded area, indicating massive coral death, easily occupied by algae.'
      },
      sd: {
        title: 'SD (Sand/Soft Coral Coverage)',
        good: 'Appropriate amount, optimal state maintaining ecological balance.',
        poorHigh: 'Excessive sand may bury hard corals and hinder larvae settlement.',
        poorLow: 'Too low, may indicate ecosystem imbalance, lacking necessary soft corals.'
      },
      di: {
        title: 'DI (Disturbance Indicator)',
        good: 'Ideal state, almost no obvious damage.',
        moderate: 'Warning level, indicating recent typhoon, fishing or anchor damage.',
        poor: 'Obvious damage, ecosystem in vulnerable or degraded state.'
      },
      pi: {
        title: 'PI (Pollution Indicator)',
        good: 'Ideal, minimal external pollution impact.',
        moderate: 'Moderate level, indicating some pollution sources, needs attention.',
        poor: 'High pollution, may cause coral bleaching, increased disease and mortality.'
      }
    },
    loading: {
      connecting: 'Connecting to map server...',
      loading: 'Loading map data...',
      locating: 'Locating island information...',
      generating: 'Generating interactive map...',
      almostReady: 'Almost ready...',
      success: 'Map data loaded successfully!',
      backup: 'Using backup data...'
    },
    comingSoon: {
      title: 'Interactive Map',
      subtitle: 'Coming Soon',
      description: 'We\'re working on an amazing interactive map feature that will help you explore coral reef locations around Malaysia and track their health status in real-time.',
      featuresTitle: 'What\'s Coming:',
      features: {
        locations: '📍 Real-time coral reef locations',
        visualization: '🌊 Interactive marine data visualization',
        health: '📊 Health status indicators',
        info: '🔍 Detailed location information',
        mobile: '📱 Mobile-friendly navigation'
      },
      backButton: '← Back to Previous Page'
    },
    game: {
      title: '🎮 Coral Adventure Game',
      unavailable: 'Game component has been removed. This feature is currently unavailable.',
      switchBack: 'You can switch back to the Interactive Map view using the toggle above.'
    }
  },
  
  // Education page
  education: {
    title: 'Education & Responsible Tourism Hub',
    subtitle: 'Learn about coral reefs and become a responsible traveler',
    gameButton: '🎮 Wanna Try a Game?',
    tipsGuidesButton: '📋 Tips and Guides',
    dropdown: {
      tourismHub: 'Tourism Hub',
      tourismChecklist: 'Travel Checklist'
    },
    sections: {
      videos: 'Video Learning',
      quiz: 'Test Your Knowledge',
      resources: 'Learning Resources',
      numbers: 'CORAL REEFS IN NUMBERS'
    },
    quiz: {
      button: '🎯 Take Quiz',
      description: 'Test your knowledge about coral reefs'
    },
    videoCategories: {
      basics: 'Understanding Coral Reefs',
      threats: 'Threats and Conservation',
      protection: 'How to Protect'
    },
    numbers: {
      mainTypes: {
        title: 'main types of coral reefs',
        description: 'Fringing reefs hug coastlines, barrier reefs sit behind lagoons, and atolls form rings in the open ocean. Knowing their formation helps us understand why certain reefs thrive in specific places.',
        learnMore: 'Learn More'
      },
      tourismValue: {
        title: 'RM annual tourism value on Tioman',
        description: 'Healthy reefs attract divers, protect coasts, and support fisheries—driving Malaysia\'s tourism economy.',
        learnMore: 'Learn More'
      },
      marineSpecies: {
        title: 'marine species depend on reefs',
        description: 'Reefs cover less than 1% of the ocean yet support 25% of marine life, protect coasts, feed communities, and hold potential medical cures.',
        learnMore: 'Learn More'
      },
      bombFishing: {
        title: 'bomb fishing incidents',
        description: 'AI helps protect reefs—from tracking fish "songs" that signal recovery to detecting illegal blast fishing across vast waters.',
        learnMore: 'Learn More'
      },
      fishSpecies: {
        title: 'fish species on a single reef',
        description: 'Like underwater cities, reefs teem with life—from tiny reef fish to apex predators—making them highly productive ecosystems.',
        learnMore: 'Learn More'
      },
      coralsOutplanted: {
        title: 'corals outplanted on the GBR',
        description: 'The Coral Nurture Programme grows fragments in nurseries and restores reefs—science and tourism working together.',
        learnMore: 'Learn More'
      }
    },
    verification: {
      title: 'Coral Reef Health Verification',
      description: 'Please identify whether the coral reefs in the following 6 images are healthy',
      close: '×'
    },
    videos: {
      important: 'Why are coral reefs so important',
      dieOff: 'What Would Happen If All The Coral Reefs Died Off',
      dying: 'Coral Reefs Are Dying. Here\'s How We Can Save Them',
      bleaching: 'Coral Bleaching Explained: The Story of Frank the Coral'
    }
  },
  
  // AI Tools page
  aiTools: {
    title: 'AI Coral Classification',
    subtitle: 'Upload coral images using our advanced AI model',
    disclaimer: {
      warning: '⚠️ This feature is only for coral health status identification. Please do not upload irrelevant images.',
      accuracy: '📋 AI results may not be accurate and are for reference only.'
    },
    upload: {
      title: 'Upload Coral Image',
      description: 'Drag and drop your coral image here, or click to browse',
      formats: ['JPG', 'PNG', 'JPEG'],
      fileSizeLimit: 'Maximum file size: 3MB',
      changeImage: '🔄 Change Image',
      remove: '❌ Remove',
      classify: 'Classify Coral',
      classifying: 'Classifying...'
    },
    results: {
      title: 'Classification Result',
      species: 'Coral species identified by AI',
      classifyAnother: '🔄 Classify Another Image'
    },
    error: {
      title: 'Classification Failed',
      close: '×'
    }
  },
  
  // Footer
  footer: {
    copyright: '© 2025 CoralKita',
    contact: 'Contact Us'
  },

  // Island Detail page
  islandDetail: {
    description: 'Description',
    travelInfo: 'Travel Information',
    arrive: 'Arrive',
    reminder: 'Reminder',
    snorkelingDiving: 'Snorkeling & Diving',
    accommodation: 'Accommodation',
    hotelRecommendation: 'Hotel / Resort Recommendation',
    tourActivities: 'Tour Activities & Adventures',
    exploreDiving: 'Explore Diving',
    marineParkSnorkeling: 'Marine Park Snorkeling',
    exploreSnorkeling: 'Explore Snorkeling',
    chooseDiving: 'Choose Diving',
    chooseSnorkeling: 'Choose Snorkeling',
    loading: 'Loading island information...',
    pSinga: {
      title: 'Pulau Singa, Langkawi',
      description: 'Pulau Singa, part of the Langkawi archipelago in Kedah, is a small island within the Langkawi Marine Park. Known for its clear waters, shallow reefs, and peaceful sandy stretches, it is often included in island-hopping itineraries from Langkawi. The island is uninhabited and does not have accommodation or permanent facilities, making it primarily a day-trip and snorkeling stop.',
      arrive: 'Pulau Singa is reached by speedboat from Langkawi (20–40 min from Kuah Jetty or Pantai Cenang), often combined with Dayang Bunting and Beras Basah in island-hopping tours.',
      reminder: 'No jetty exists, so landings depend on weather; some trips may be snorkeling-only.',
      snorkeling: 'Shallow coral gardens suit beginners and families, with colorful fish (clownfish, damselfish, wrasses) and occasional turtles. Diving is limited but available via Langkawi Marine Park packages.',
      accommodation: 'No stays on Pulau Singa; visitors stay in Langkawi, where hotels and eco-lodges are available. Operators arrange day trips.',
      tips: 'Tips',
      tipsContent: 'The best time to visit is during the dry season (November – April) when seas are calmer and visibility is better.',
      tipsContent2: 'During the wetter months (May – October), sea conditions may be choppier.',
      conservation: 'Conservation-friendly practices',
      conservationContent: 'Visitors are encouraged to use reef-safe sunscreen, avoid touching or stepping on corals and seagrass beds, and take all rubbish back to Langkawi as the island has no waste facilities. Feeding fish or disturbing marine life should be avoided, and tours are best arranged with licensed operators who follow Langkawi Marine Park guidelines to ensure the reefs remain healthy for future visitors.',
      dataiResort: 'The Datai Langkawi',
      dataiDesc: 'Luxury rainforest resort with stunning Andaman Sea views',
      dannaResort: 'The Danna Langkawi',
      dannaDesc: 'Colonial-style luxury resort with private beach access',
      frangipaniResort: 'The Frangipani Langkawi',
      frangipaniDesc: 'Award-winning eco-friendly resort with beachfront location',
      ambongResort: 'Ambong Ambong',
      ambongDesc: 'Sustainable luxury villas with private pools and sea views',
      booking: 'Booking',
      operatorsTours: 'Operators & Tours',
      operatorsDesc: 'Island-hopping tours from Pantai Cenang and Kuah Jetty (multiple local operators)',
      operatorsDesc2: 'Custom eco-tours available with licensed Langkawi Marine Park guides.',
      padiDiving: 'PADI Langkawi Scuba - Dive Center',
      padiDesc1: 'Boat dives around Langkawi Marine Park',
      padiDesc2: 'Beginner friendly & certification options',
      checkTour: 'Check Tour',
      unescoJetSki: 'UNESCO Geopark Jet-Ski - Islands',
      jetSkiDesc1: 'Island hopping from Pantai Cenang',
      jetSkiDesc2: 'Guide & safety briefing included'
    },
    mertang: {
      title: 'Pulau Mertang, Johor',
      description: 'Pulau Mertang, situated within the state of Johor, Malaysia, comprises a group of very small offshore islets (Mertang Barat / Mertang Timur) located off the east coast of Johor near the waters of Mersing. Geographical records classify them as \'islets\' (small islands or sandbanks), typically lacking accommodation.',
      arrive: 'You may choose to depart from piers in the Mersing/Endau area. Please confirm with local boat operators or resort management whether they offer stops at Mertang\'s island-hopping/snorkelling sites. It is advisable to combine Mertang with more commonly visited neighbouring island groups (such as Rawa, Sembilang, Sri Buat, Dayang, etc.) to enhance trip feasibility and route flexibility. The geographical and destination overview indicates that the Mersing area serves as the primary departure point for accessing these offshore islands.',
      reminder: 'Mertang is a very small island with no publicly available permanent landing facilities or scheduled ferry services. It is essential to confirm with the boat operator the day\'s sea conditions, whether snorkelling will be the sole activity without island landings, and other details (to avoid trampling/disturbing bird or reptile habitats).',
      snorkeling: 'If you choose to snorkel in coastal activities in the Pulau Mertang area, please adhere to the principles of "no touching, no trampling, and no collecting".',
      accommodation: 'Mersing / Johor',
      accommodationDesc: 'As Mertang consists primarily of uninhabited islets, accommodation is typically arranged in Mersing or neighbouring islands such as Rawa. From there, visitors undertake day trips, island-hopping, or charter shared boats to explore the Mertang waters. Rawa offers comprehensive destination guides and resort product information, facilitating tailored trips in collaboration with boat operators.',
      orcaDiving: 'Orca Nation Diving',
      orcaDesc: 'Discover the underwater world with certified guides and pristine coral reefs.',
      rawaResort: 'Rawa Island Resort',
      rawaDesc: 'Idyllic tropical island resort with pristine beaches and coral reefs',
      orcaResort: 'Orca Nation Resort',
      orcaResortDesc: 'Private island escape with crystal-clear waters and white sand beaches',
      islandHopping: 'Island Hopping',
      book: 'Book',
      tips: 'Tips',
      tipsContent: 'During the monsoon season (around November to February), sea conditions are unstable and trips are frequently adjusted or cancelled.',
      conservation: 'Conservation-friendly practices',
      conservationContent: 'Use reef-safe sunscreen, avoid touching the seabed or grabbing objects while snorkelling, don\'t feed fish, and don\'t tread on nesting areas on sandbanks. Rawa is situated within Sultan Iskandar Marine Park, where fishing and motorised water activities are restricted.'
    },
    sipadan: {
      title: 'Pulau Sipadan, Sabah',
      description: 'Pulau Sipadan, located off the east coast of Sabah, is Malaysia\'s only true oceanic island, formed by an extinct volcano rising 600 metres from the seabed. Recognised worldwide as one of the top diving destinations, Sipadan is home to extraordinary marine biodiversity including turtles, barracuda, reef sharks, and vibrant coral walls. The island is a protected marine park, and overnight stays are not permitted.',
      arrive: 'Access to Sipadan is via the town of Semporna. Visitors usually fly to Tawau Airport in Sabah from major cities such as Kuala Lumpur or Kota Kinabalu. From Tawau, it is about a 1-hour drive to Semporna, followed by a 45–60 minute speedboat ride from the Semporna Jetty to Sipadan, or to nearby islands such as Mabul or Kapalai, where resorts and dive operators arrange day trips to Sipadan.',
      reminder: 'Sipadan is a protected marine park with no overnight stays or public facilities. Access is limited to 120 permits per day via licensed dive operators, so visitors should confirm permits, boat schedules, and sea conditions well in advance.',
      snorkeling: 'Sipadan is globally recognised as a premier diving destination. The island\'s steep drop-offs and surrounding reefs provide some of the most spectacular underwater experiences in the world.',
      iconicSites: 'Iconic dive sites: Barracuda Point, Turtle Cavern, and The Drop-Off.',
      marineLife: 'Marine life: Large schools of jackfish, swirling barracuda, bumphead parrotfish, reef sharks, and dozens of green and hawksbill turtles. Advanced divers may also encounter hammerheads and whale sharks during certain seasons.',
      snorkelingNote: 'Limited snorkeling is possible in shallow areas, but the island is primarily suited to experienced divers due to depth, currents, and the protected permit system.',
      accommodation: 'There is no accommodation on Sipadan. Most visitors stay on nearby islands such as Mabul or Kapalai, which offer resorts, water chalets, and full dive packages including Sipadan permits.',
      kapalaiResort: 'Sipadan Kapalai Dive Resort',
      kapalaiDesc: 'Water village resort on stilts with world-class diving access to Sipadan',
      mabulResort: 'Mabul Beach Resort',
      mabulDesc: 'Eco-friendly beachfront resort with award-winning PADI dive center',
      seaventuresResort: 'Seaventures Dive Rig',
      seaventuresDesc: 'Unique oil rig converted into luxury dive resort with direct Sipadan access',
      booking: 'Booking',
      tips: 'Tips',
      tipsContent: 'The best time to visit is April – December, when seas are calm and visibility is 20–40 metres. From January – March, seas may be rougher, though there are fewer tourists.',
      tipsContent2: 'Book early as permits are limited to 120 per day and usually tied to resort or dive packages. Carry dive certification cards; Sipadan is not suitable for beginners.',
      tipsContent3: 'There are no facilities on the island; accommodation, meals, and dive logistics are arranged from Mabul, Kapalai, or Semporna. Bring reef-safe sunscreen and other essentials.',
      conservation: 'Conservation-friendly practices',
      conservationContent: 'Sipadan is a strictly protected marine park. Visitors should dive only with licensed operators, avoid touching or disturbing marine life, and maintain proper buoyancy to protect fragile corals.',
      conservationContent2: 'Fishing, motorised water activities, and overnight stays are prohibited. Choosing eco-certified dive resorts helps ensure the island remains pristine for future generations.'
    },
    pulauLima: {
      title: 'Pulau Lima, Terengganu',
      description: 'Pulau Lima comprises a pair of small islands (Lima Besar / Lima Kecil) within the Sultan Iskandar Marine Park. Managed by Johor Parks, the marine park encompasses multiple islands off the coast of Mersing, conducting conservation and educational activities.',
      arrive: 'You may first proceed to Mersing in Johor (the staging point for embarking on sea journeys) to complete formalities at Mersing Harbour Centre (Mersing Ferry Terminal Service Centre). Here, you will confirm ferry schedules/assembly points and pay marine park/conservation fees (charges apply for both island access and entry into marine park zones). Alternatively, you may choose to take a speedboat/chartered vessel or join an organised island-hopping tour from Mersing Jetty to the Pulau Lima area. As an uninhabited island, Pulau Lima typically lacks regular public ferry services.',
      reminder: 'Common approaches include chartering boats from Mersing, joining island-hopping tours, or arranging visits through local dive centres and resorts in the Sibu area.',
      accommodation: 'Mersing / Johor',
      accommodationDesc: 'As Pulau Lima consists primarily of uninhabited islets, accommodation is typically arranged in Mersing or neighbouring islands such as Sibu. From there, visitors undertake day trips, island-hopping, or charter shared boats to explore the Lima waters. Sibu offers comprehensive destination guides and resort product information, facilitating tailored trips in collaboration with boat operators.',
      seaGypsyResort: 'Sea Gypsy Village Resort',
      seaGypsyDesc: 'Eco-friendly resort with professional snorkeling guides and marine park access',
      rimbaResort: 'Rimba Resort',
      rimbaDesc: 'Natural bay resort with diving school and pristine beachfront accommodation',
      myVillaRecreation: 'MYVilla Recreation',
      myVillaDesc: 'Eco-friendly homestay with fishing, diving, and jungle trekking activities',
      islandHopping: 'Island Hopping',
      islandHoppingDesc: 'Explore multiple islands with guided tours and snorkeling adventures',
      marineParkTours: 'Marine Park Tours',
      marineParkToursDesc: 'Professional guided tours to Sultan Iskandar Marine Park with certified guides',
      book: 'Book',
      snorkeling: 'Lima Besar & Lima Kecil Snorkelling Sites: Local dive centres and resorts in the Sibu area explicitly list Lima Besar and Lima Kecil as snorkelling routes and coastal experience points, suitable for beginners and families (subject to visibility and sea conditions).',
      marinePark: 'Marine Park Snorkeling',
      marineParkDesc: 'Explore pristine coral reefs and diverse marine life in Sultan Iskandar Marine Park with certified guides.',
      tips: 'Tips',
      tipsContent: 'Diving conditions and visibility in the Mersing region (best from March to September; multiple operators suspend or reduce services during the northeast monsoon season from November to February).'
    },
    seriBuat: {
      title: 'Pulau Seri Buat, Pahang',
      description: 'Pulau Seri Buat, also known as Pulau Rahsia, is one of Pahang\'s best-kept secrets within the Sultan Iskandar Marine Park. Known for its limestone cliffs, crystal-clear waters, and secluded beaches, Seri Buat is a peaceful stop often combined with nearby islands such as Pulau Rawa and Pulau Sembilang. Often called the "Krabi of Malaysia" for its resemblance to Thailand\'s Krabi coastline, the island offers both tranquility and adventure, making it a perfect getaway for eco-travelers and weekend explorers.',
      arrive: 'Access to Pulau Seri Buat is by boat from either Mersing Jetty (Johor) or Tanjung Gemok Jetty (Pahang), with travel times of about 30–45 minutes depending on the departure point. Visitors typically charter boats or join island-hopping tours that include Seri Buat along with other islands in the Mersing cluster, such as Pulau Rawa, Pulau Sembilang, and Pulau Harimau.',
      reminder: 'Seri Buat has no public ferry services, scheduled routes, or permanent jetty. Trips depend on sea conditions, and in some cases, visits may be limited to snorkeling or offshore stops.',
      snorkeling: 'Seri Buat\'s turquoise waters are ideal for snorkeling, with colorful reef fish and good visibility. While diving opportunities are more developed around nearby islands, Seri Buat is a popular stop for:',
      snorkelingList: [
        'Snorkeling in sheltered coves',
        'Swimming in emerald waters beneath dramatic cliffs',
        'Cliff-jumping into the sea at designated safe spots'
      ],
      hikingAdventure: 'Hiking & Adventure',
      hikingContent: 'A short 10–15 minute hike leads to a scenic viewpoint with panoramic views of Seri Buat and the surrounding waters. For more adventure, visitors can explore rocky outcrops or join island-hopping tours that include nearby Pulau Harimau, known for its hidden natural pool and popular cliff-jumping spots.',
      recommendedResorts: 'Recommended Resorts & Operators',
      rawaResort: 'Rawa Island Resort',
      rawaDesc: 'Luxury island resort with pristine beaches and water activities',
      stepFootprint: '1Step1Footprint',
      stepFootprintDesc: 'Professional island hopping tours with hiking and snorkeling',
      amiTravel: 'AMI Travel',
      amiTravelDesc: '8-island day trip package with comprehensive activities',
      explore: 'Explore',
      tips: 'Tips',
      tipsContent: 'The best time to visit is March – October, when seas are calm.',
      tipsContent2: 'Avoid November – February (monsoon season), when sea conditions can be rough.',
      tipsContent3: 'Bring reef-safe sunscreen, water, and snacks, as there are no facilities on the island.',
      tipsContent4: 'Book island-hopping tours in advance to secure your spot.',
      tipsContent5: 'Don\'t miss the short hike to the viewpoint for breathtaking scenery.',
      tipsContent6: 'Be cautious when snorkeling or cliff-jumping, and always follow operator guidance.',
      conservation: 'Conservation-friendly practices',
      conservationContent: 'As part of the Sultan Iskandar Marine Park, Seri Buat is under protection. Visitors should avoid touching or stepping on corals, refrain from feeding fish, and carry all rubbish back to the mainland. Use reef-safe sunscreen to protect marine life, and respect nesting areas on sandbanks or rocky outcrops. Choosing licensed eco-friendly operators helps preserve the island\'s natural beauty for future visitors.',
      accommodation: 'No accommodation available on the island. Visitors typically stay in Mersing or nearby coastal towns and take day trips to the island.'
    },
    actionButtons: {
      reefPolicies: 'Learn about Reef Policies',
      reefConservation: 'Learn about Reef Conservation'
    }
  },

  // Quiz page
  quiz: {
    title: 'Coral Reef Knowledge Quiz',
    startButton: 'Start Quiz',
    nextButton: 'Next Question',
    submitButton: 'Submit Quiz',
    restartButton: 'Restart Quiz',
    question: 'Question',
    of: 'of',
    score: 'Score',
    correct: 'Correct!',
    incorrect: 'Incorrect',
    completed: 'Quiz Completed!',
    yourScore: 'Your Score',
    feedback: {
      excellent: 'Excellent! You\'re a coral reef expert!',
      good: 'Great job! You know a lot about coral reefs.',
      average: 'Good effort! Keep learning about coral reefs.',
      needImprovement: 'Keep studying! Visit the Education Center to learn more.'
    }
  },
  
  
  // Checklist page
  checklist: {
    title: 'Travel Checklist',
    subtitle: 'Prepare for your coral reef adventure',
    categories: {
      equipment: 'Diving Equipment',
      safety: 'Safety Items',
      documentation: 'Documentation',
      personal: 'Personal Items'
    },
    markComplete: 'Mark as Complete',
    addItem: 'Add Item'
  },
  
  // Common
  common: {
    loading: 'Loading...',
    error: 'Error',
    success: 'Success',
    save: 'Save',
    cancel: 'Cancel',
    confirm: 'Confirm',
    delete: 'Delete',
    edit: 'Edit',
    close: 'Close',
    search: 'Search',
    filter: 'Filter',
    sort: 'Sort',
    viewDetails: 'View Details',
    learnMore: 'Learn More',
    backToHome: 'Back to Home'
  },
  
  // Chat Widget
  chat: {
    title: 'Coral Assistant',
    placeholder: 'Ask me anything about coral reefs...',
    send: 'Send',
    minimize: 'Minimize',
    close: 'Close'
  },
  
  // AI Tools Page
  aiTools: {
    title: 'AI Coral Classification',
    subtitle: 'Upload a coral image and get instant species identification',
    disclaimer: {
      warning: 'This tool is for educational purposes only',
      accuracy: 'Results may vary and should not replace professional identification'
    },
    upload: {
      title: 'Upload Your Coral Image',
      description: 'Choose a clear, well-lit image of coral for best results',
      formats: ['JPG', 'PNG', 'JPEG', 'WEBP'],
      fileSizeLimit: 'Maximum file size: 10MB',
      changeImage: 'Change Image',
      remove: 'Remove',
      classify: 'Classify Coral',
      classifying: 'Analyzing...'
    },
    results: {
      title: 'Classification Result',
      species: 'Coral Species',
      classifyAnother: 'Classify Another'
    },
    error: {
      title: 'Classification Error',
      close: 'Close'
    },
    howToUse: {
      title: 'How to Use',
      step1: {
        title: 'Upload Image',
        description: 'Select a clear image of coral from your device'
      },
      step2: {
        title: 'AI Analysis',
        description: 'Our AI model analyzes the coral features and patterns'
      },
      step3: {
        title: 'Get Results',
        description: 'Receive instant species identification and information'
      }
    }
  },
  
  // FAQ Page
  faq: {
    title: 'Frequently Asked Questions',
    subtitle: 'Find answers to common questions about CoralKita',
    generalInfo: {
      title: 'General Information',
      q1: {
        question: 'Q: What is this website about?',
        answer: 'A: This platform provides interactive tools, data visualizations, and an AI chatbot to help users understand the health of Malaysia\'s coral reefs. It aims to raise awareness about coral bleaching, promote responsible tourism, and make scientific reef data more accessible.'
      },
      q2: {
        question: 'Q: Who is this platform for?',
        answer: 'A: It\'s designed for tourists, students, researchers, conservationists, NGOs, and policymakers interested in coral reef health and sustainable travel.'
      }
    },
    dataAndSources: {
      title: 'Data and Sources',
      q1: {
        question: 'Q: Where does the data come from?',
        answer: 'A: The data is sourced primarily from annual coral reef survey reports and raw metadata from Reef Check Malaysia. Link: <a href="https://reefcheck.org.my/interactive-map/" target="_blank" style="color: #4facfe; text-decoration: underline;">https://reefcheck.org.my/interactive-map/</a>'
      },
      q2: {
        question: 'Q: How often is the data updated?',
        answer: 'A: The coral reef survey reports are released annually therefore the data is updated once a year.'
      },
      q3: {
        question: 'Q: Is this website affiliated with any government bodies?',
        answer: 'A: No, CoralKita is an independent website. All information obtained is cited to ensure transparency.'
      }
    },
    disclaimer: {
      title: 'Disclaimer and Limitations',
      q1: {
        question: 'Q: Is the information 100% accurate?',
        answer: 'A: While data is obtained from credible sources, accuracy may vary due to environmental changes or incomplete survey data. The platform does not replace professional environmental assessments.'
      },
      q2: {
        question: 'Q: Who is responsible for the chatbot\'s responses?',
        answer: 'A: Responses are automatically generated based on official reports and pre-trained models. They are intended for educational purposes and should not be used as professional advice.'
      }
    }
  },
  
  // Travel Checklist
  travelChecklist: {
    title: 'Responsible Travel Checklist',
    subtitle: 'Complete key steps for sustainable tourism',
    back: 'Back',
    conservationAction: 'CoralKita Conservation Action',
    questionnaire: {
      beforeWeStart: 'Before We Start...',
      description: 'Answer a few questions to get your personalized checklist',
      progress: 'Question {current} of {total}',
      previous: 'Previous',
      next: 'Next',
      showChecklist: 'Show My Checklist'
    },
    questions: {
      firstTime: 'Is this your first time snorkelling/diving?',
      travellingWithKids: 'Are you travelling with kids?',
      reefCondition: 'Reef condition of island you plan to visit:',
      swimmingLevel: 'Swimming skill level:'
    },
    options: {
      yes: 'Yes',
      no: 'No',
      good: 'Good',
      fair: 'Fair',
      poor: 'Poor',
      unknown: 'Unknown',
      beginner: 'Beginner',
      intermediate: 'Intermediate',
      advanced: 'Advanced'
    },
    categories: {
      experience: {
        title: 'Based on Your Experience',
        firstTime: 'First-time tips',
        experienced: 'Experienced guidance',
        description: 'Essential preparation based on your diving/snorkeling experience'
      },
      traveling: {
        titleWithKids: 'Travelling with Kids',
        titleSolo: 'Travelling Solo/Adults',
        familyTips: 'Family-friendly tips',
        independent: 'Independent travel',
        descriptionWithKids: 'Special considerations when traveling with children',
        descriptionSolo: 'Tips for independent travelers'
      },
      reefCondition: {
        title: 'Based on Reef Condition',
        goodCondition: 'Good condition',
        fairCondition: 'Fair condition',
        poorCondition: 'Poor condition',
        unknownCondition: 'Unknown condition',
        description: 'Guidelines specific to the reef health at your destination'
      },
      swimmingSkills: {
        title: 'Based on Your Swimming Skills',
        beginnerLevel: 'Beginner level',
        intermediateLevel: 'Intermediate level',
        advancedLevel: 'Advanced level',
        description: 'Safety and activity recommendations for your skill level'
      }
    },
    checklist: {
      question1: {
        yes: {
          item1: 'Attend a basic snorkeling/diving safety briefing before entering the water.',
          item2: 'Practice using snorkel gear in shallow water or pool.',
          item3: 'Read up on reef-safe behaviors (e.g., never touch or stand on corals).',
          item4: 'Use reef-safe sunscreen (zinc-based, non-toxic).',
          item5: 'Bring a rash guard or long-sleeve swimwear for sun protection.',
          item6: 'Check local weather and tide times before your trip.',
          item7: 'Choose a certified eco-tour operator that promotes reef conservation.'
        },
        no: {
          item1: 'Refresh your knowledge of reef conservation rules — even experienced divers can forget.',
          item2: 'If you bring your own gear, inspect and clean your gear to avoid carrying invasive species between reefs.',
          item3: 'Encourage first-timers in your group to follow conservation guidelines.',
          item4: 'Review any new local regulations or reef health updates before diving.'
        }
      },
      question2: {
        yes: {
          item1: 'Choose family-friendly beaches or snorkeling spots with calm, shallow water.',
          item2: 'Bring well-fitted child snorkel gear and test it before the trip.',
          item3: 'Pack UV-protective swimwear and reef-safe sunscreen for children.',
          item4: 'Educate kids early on: "Look, don\'t touch" — explain why corals and marine life must be protected.',
          item5: 'Keep children close to shore or within arm\'s reach at all times.',
          item6: 'Schedule water activities in the morning when the sea is calmer and crowds are fewer.',
          item7: 'Join guided eco-tours with child-safe learning activities (e.g., reef walks, marine center visits).',
          item8: 'Bring plenty of drinking water, snacks, and rest breaks — tired kids make unsafe swimmers.',
          item9: 'Encourage kids to collect memories, not souvenirs — no shells or coral pieces.'
        },
        no: {
          item1: 'Choose longer or more advanced snorkeling/diving sessions if you\'re confident in the water.',
          item2: 'Support operators that offer marine education for families, even if you\'re traveling solo — your contribution helps fund outreach.',
          item3: 'Use your flexibility to visit less crowded reefs and reduce stress on popular family spots.'
        }
      },
      question3: {
        good: {
          item1: 'Help maintain the reef condition — avoid touching or chasing marine life.',
          item2: 'Participate in citizen science programs while snorkeling/diving (e.g., reef health reporting).',
          item3: 'Support local eco-friendly operators and conservation projects.',
          item4: 'Leave no litter, and take all trash back with you.'
        },
        fair: {
          item1: 'Avoid any physical contact with the reef — it\'s fragile and trying to recover.',
          item2: 'Support or donate to local reef restoration projects.',
          item3: 'Opt for non-invasive activities (snorkeling instead of anchoring boats or walking on reefs).',
          item4: 'Share photos and awareness responsibly — highlight conservation needs.'
        },
        poor: {
          item1: 'Avoid any physical contact with the reef — it\'s fragile and trying to recover.',
          item2: 'Support or donate to local reef restoration projects.',
          item3: 'Opt for non-invasive activities (snorkeling instead of anchoring boats or walking on reefs).',
          item4: 'Share photos and awareness responsibly — highlight conservation needs.'
        },
        unknown: {
          item1: 'Research recent reports on reef condition on our interactive map before your visit.',
          item2: 'Contact local dive centers or marine parks for updates.',
          item3: 'Learn how to recognize coral bleaching and report sightings to local authorities.',
          item4: 'Educate yourself on best environmental practices for all reef conditions.'
        }
      },
      question4: {
        beginner: {
          item1: 'Use a life jacket or flotation device when snorkeling.',
          item2: 'Stay close to shore or with a certified guide.',
          item3: 'Avoid strong currents and deep water.',
          item4: 'Practice basic water safety and breathing techniques before your trip.',
          item5: 'Always go with a buddy or guided tour for safety.',
          item6: 'Do not chase or approach marine life too closely.'
        },
        intermediate: {
          item1: 'Stay within designated snorkeling/diving zones.',
          item2: 'Keep an eye on current changes and weather conditions.',
          item3: 'Help less experienced swimmers if needed — but don\'t compromise your safety.',
          item4: 'Maintain good buoyancy control to avoid touching corals.',
          item5: 'Carry a surface marker buoy (SMB) if diving.'
        },
        advanced: {
          item1: 'Check dive equipment thoroughly and plan your dives responsibly.',
          item2: 'Volunteer for reef clean-ups or monitoring dives.',
          item3: 'Explore responsibly — avoid over-diving the same spots to reduce impact.',
          item4: 'Share your knowledge with less experienced tourists.',
          item5: 'Support reef protection efforts through local marine groups.'
        }
      }
    },
    resetButton: 'Reset & Start Over'
  },

  // Government & Policy Information Hub
  government: {
    title: 'Government & Policy Information Hub',
    subtitle: 'Reliable, up-to-date information on Malaysia\'s reef conservation policies and official initiatives',
    
    policyUpdates: {
      title: 'Government & Policy Updates',
      subtitle: 'Simplified updates on current government policies and conservation initiatives',
      
      updates: {
        sunscreen: {
          date: 'June 2025',
          title: 'Marine Park Department Enforces Coral-Safe Sunscreen Guidelines',
          description: 'The Department of Fisheries Malaysia has implemented new regulations requiring all marine park visitors to use only reef-safe sunscreen. This initiative aims to reduce chemical pollution that damages coral reefs.',
          source: 'Official Source: Department of Fisheries Malaysia'
        },
        fishing: {
          date: 'August 2025',
          title: 'Marine Park Authority Bans Destructive Fishing Near Redang Reefs',
          description: 'New enforcement measures prohibit all forms of destructive fishing practices within 2 nautical miles of Redang Island\'s coral reefs. Violators face increased penalties to protect marine biodiversity.',
          source: 'Official Source: Marine Park Malaysia'
        },
        monitoring: {
          date: '2024',
          title: 'National Reef Monitoring Programme Expands to 200+ Sites',
          description: 'Reef Check Malaysia, in collaboration with the Department of Fisheries, has expanded its monitoring network to over 200 reef sites nationwide, providing comprehensive data on reef health and recovery.',
          source: 'Official Source: Reef Check Malaysia'
        },
        restoration: {
          date: '2024',
          title: 'Coral Restoration Programme in Tun Mustapha Marine Park',
          description: 'Sabah Parks has successfully installed over 1,000 reef frames and transplanted 12,000 coral fragments in Tun Mustapha Marine Park through collaboration with volunteers and local communities.',
          source: 'Official Source: Sabah Parks'
        }
      }
    },
    
    conservation: {
      title: 'Support Reef Conservation',
      subtitle: 'Verified NGOs and official donation programs with secure links',
      
      ngos: {
        reefCheck: {
          name: 'Reef Check Malaysia',
          verified: 'Verified NGO',
          description: 'Reef Check Malaysia is a registered non-profit organization dedicated to the conservation and sustainable management of Malaysia\'s coral reef ecosystems. They conduct regular reef health surveys, coral restoration projects, and community conservation programmes across the nation.',
          focusAreas: 'Focus Areas:',
          focusAreasText: 'Reef monitoring, coral restoration, marine education',
          coverage: 'Coverage:',
          coverageText: 'Nationwide reef sites including Redang, Tioman, Perhentian, and more',
          button: 'Learn More & Donate via Official Site →'
        },
        wwf: {
          name: 'WWF Malaysia',
          verified: 'Verified NGO',
          description: 'WWF Malaysia is part of the global WWF network, working to protect Malaysia\'s marine ecosystems including coral reefs. Their reef conservation efforts focus on reducing marine pollution, promoting sustainable tourism, and protecting critical marine habitats.',
          focusAreas: 'Focus Areas:',
          focusAreasText: 'Marine conservation, sustainable fisheries, climate change mitigation',
          coverage: 'Coverage:',
          coverageText: 'Malaysia\'s coastal and marine protected areas',
          button: 'Learn More & Donate via Official Site →'
        },
        tracc: {
          name: 'TRACC (Tropical Research and Conservation Centre)',
          verified: 'Verified NGO',
          description: 'TRACC operates hands-on coral reef restoration and marine research projects in Sabah, Malaysia. They work with local communities and volunteers to rehabilitate damaged reefs and provide training on sustainable marine resource management.',
          focusAreas: 'Focus Areas:',
          focusAreasText: 'Coral reef restoration, marine research, community training',
          coverage: 'Coverage:',
          coverageText: 'Semporna and surrounding waters in Sabah',
          button: 'Learn More & Donate via Official Site →'
        }
      }
    }
  }
}

