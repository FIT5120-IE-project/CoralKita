export default {
  // Navigasi
  nav: {
    logo: 'CoralKita',
    mapRecommendation: {
      line1: 'Peta &',
      line2: 'Cadangan'
    },
    island: 'Pulau',
    education: 'Pendidikan',
    aiClassification: 'Klasifikasi AI',
    government: 'Kerajaan',
    actionHub: 'Pusat Tindakan',
    policies: 'Dasar',
    faq: 'Soalan Lazim',
    changeLanguage: 'Bahasa'
  },
  
  // Halaman utama
  home: {
    coral: {
      healthy: 'Adakah ini terumbu karang yang anda ingat — terang, berwarna-warni, dan hidup?',
      fading: 'Hari ini, kebanyakan terumbu karang sedang pudar, bergelut di bawah perubahan iklim dan tekanan manusia.',
      bleached: 'Tanpa tindakan, mereka mungkin tidak lama lagi menjadi rangka putih yang tidak bernyawa seperti ini.'
    },
    intro: {
      title: 'Rancang Bijak. Perjalanan Hijau. Lindungi Terumbu Karang.',
      description: 'CoralKita membimbing perjalanan anda dengan data terumbu yang sebenar — membantu anda memilih pulau yang tepat, pada masa yang tepat, dengan cara yang betul, supaya anda boleh menikmati terumbu karang Malaysia sambil melindungi keindahannya.'
    },
    features: {
      dataDriven: 'Berdasarkan Data',
      responsible: 'Bertanggungjawab',
      userFriendly: 'Mesra Pengguna'
    },
    exploreButton: 'Mari Terokai',
    footer: {
      copyright: '© 2025 CoralKita',
      contact: 'Hubungi Kami'
    }
  },
  
  // Nama pulau
  islands: {
    mertang: 'Mertang',
    pSinga: 'P Singa',
    sipadan: 'Sipadan',
    pulauLima: 'Pulau Lima',
    seriBuat: 'Seri Buat'
  },
  
  // Halaman peta
  map: {
    title: 'Peta Haba Interaktif',
    dataSource: '*Berdasarkan data 2024',
    gameMode: '🎮 Permainan Pengembaraan Karang',
    mapMode: '🗺️ Peta Interaktif',
    selectPanel: {
      title: 'Panel Pilihan',
      filterByLocation: 'Tapis mengikut Negeri/Pulau',
      selectType: 'Pilih jenis...',
      state: 'Negeri',
      island: 'Pulau',
      selectState: 'Pilih Negeri',
      selectIsland: 'Pilih Pulau',
      selectLocation: 'Pilih Lokasi',
      typeState: 'Taip atau pilih negeri...',
      typeIsland: 'Taip atau pilih pulau...',
      selectFirst: 'Pilih jenis lokasi dahulu...',
      noStates: 'Tiada negeri ditemui',
      noIslands: 'Tiada pulau ditemui',
      selectLocationLabel: 'Pilih Lokasi'
    },
    mapInstructions: {
      useLeftPanel: 'Gunakan panel kiri untuk menapis mengikut negeri atau pulau.',
      clickMarkers: 'Klik penanda pada peta untuk meneroka butiran pulau.',
      rightPanel: 'Panel kanan menunjukkan pulau yang disyorkan, yang boleh anda klik untuk melihat maklumat lanjut.'
    },
    coordinates: 'Koordinat',
    recommendedIslands: {
      title: 'Senarai Pulau Disyorkan',
      subtitle: 'Senarai berdasarkan Liputan Karang Hidup(%) dan status kesihatan keseluruhan',
      clickToEnter: 'Klik untuk masuk ke halaman pulau'
    },
    comparison: {
      title: 'Visualisasi Perbandingan Pulau',
      panel: {
        title: 'Panel Perbandingan',
        description: 'Pilih dua pulau dan satu atribut untuk membandingkan trend kesihatan terumbu karang',
        selectIsland: '🏝️Pilih Pulau:',
        compareIsland: '⚖️Banding Pulau:',
        selectAttribute: '📊Pilih Atribut:',
        chooseIsland: 'Pilih Pulau',
        placeholder: 'Pilih sekurang-kurangnya satu pulau untuk melihat data'
      },
      chart: {
        title: 'Perbandingan Pulau - Carta Horizon',
        yAxis: 'Kandungan Peratusan',
        xAxis: 'Masa'
      },
      additionalInfo: {
        title: 'Maklumat Tambahan',
        question: 'Ingin memahami apa yang ditunjukkan setiap penunjuk tentang keadaan terumbu karang?',
        instruction: 'Sila tatal ke bawah untuk melihat Analisis Penunjuk Kesihatan Terumbu Karang.'
      }
    },
    indicators: {
      title: 'Analisis Penunjuk Kesihatan Terumbu Karang',
      subtitle: 'Setiap penunjuk (contohnya, liputan karang hidup, gangguan, pencemaran) mewakili bahagian keadaan terumbu, dan bersama-sama mereka berjumlah 100%, menunjukkan bagaimana kawasan terumbu diedarkan merentas faktor-faktor ini.',
      lcc: {
        title: 'LCC (Liputan Karang Hidup)',
        good: 'Keadaan sihat, menunjukkan kawasan liputan karang hidup yang besar dan ekosistem yang stabil.',
        moderate: 'Tahap sederhana, masih mempunyai potensi pemulihan tetapi sensitif terhadap tekanan luaran.',
        poor: 'Teruk merosot, mungkin digantikan oleh alga, pasir atau serpihan, perkhidmatan ekosistem menurun.'
      },
      as: {
        title: 'AS (Substrat Tersedia)',
        good: 'Keadaan ideal, kebanyakan substrat ditutupi oleh karang.',
        moderate: 'Zon amaran, menunjukkan kematian atau pemecahan karang baru-baru ini.',
        critical: 'Kawasan teruk merosot, menunjukkan kematian karang besar-besaran, mudah diduduki oleh alga.'
      },
      sd: {
        title: 'SD (Liputan Pasir/Karang Lembut)',
        good: 'Jumlah yang sesuai, keadaan optimum mengekalkan keseimbangan ekologi.',
        poorHigh: 'Pasir berlebihan mungkin menanam karang keras dan menghalang penempatan larva.',
        poorLow: 'Terlalu rendah, mungkin menunjukkan ketidakseimbangan ekosistem, kekurangan karang lembut yang diperlukan.'
      },
      di: {
        title: 'DI (Penunjuk Gangguan)',
        good: 'Keadaan ideal, hampir tiada kerosakan yang jelas.',
        moderate: 'Tahap amaran, menunjukkan kerosakan taufan, perikanan atau sauh baru-baru ini.',
        poor: 'Kerosakan yang jelas, ekosistem dalam keadaan terdedah atau merosot.'
      },
      pi: {
        title: 'PI (Penunjuk Pencemaran)',
        good: 'Ideal, kesan pencemaran luaran minimum.',
        moderate: 'Tahap sederhana, menunjukkan beberapa sumber pencemaran, perlu perhatian.',
        poor: 'Pencemaran tinggi, mungkin menyebabkan pemutihan karang, peningkatan penyakit dan kematian.'
      }
    },
    loading: {
      connecting: 'Menyambung ke pelayan peta...',
      loading: 'Memuatkan data peta...',
      locating: 'Mengesan maklumat pulau...',
      generating: 'Menjana peta interaktif...',
      almostReady: 'Hampir selesai...',
      success: 'Data peta berjaya dimuatkan!',
      backup: 'Menggunakan data sandaran...'
    },
    comingSoon: {
      title: 'Peta Interaktif',
      subtitle: 'Akan Datang',
      description: 'Kami sedang membangunkan ciri peta interaktif yang menakjubkan untuk membantu anda meneroka lokasi terumbu karang di sekitar Malaysia dan menjejaki status kesihatan mereka secara masa nyata.',
      featuresTitle: 'Apa yang Akan Datang:',
      features: {
        locations: '📍 Lokasi terumbu karang masa nyata',
        visualization: '🌊 Visualisasi data marin interaktif',
        health: '📊 Penunjuk status kesihatan',
        info: '🔍 Maklumat lokasi terperinci',
        mobile: '📱 Navigasi mesra mudah alih'
      },
      backButton: '← Kembali ke Halaman Sebelumnya'
    },
    game: {
      title: '🎮 Permainan Pengembaraan Karang',
      unavailable: 'Komponen permainan telah dialih keluar. Ciri ini tidak tersedia pada masa ini.',
      switchBack: 'Anda boleh beralih kembali ke paparan Peta Interaktif menggunakan togol di atas.'
    }
  },
  
  // Halaman pendidikan
  education: {
    title: 'Hab Pendidikan & Pelancongan Bertanggungjawab',
    subtitle: 'Pelajari tentang terumbu karang dan menjadi pelancong yang bertanggungjawab',
    gameButton: '🎮 Nak Cuba Permainan?',
    tipsGuidesButton: '📋 Tips dan Panduan',
    dropdown: {
      tourismHub: 'Hab Pelancongan',
      tourismChecklist: 'Senarai Perjalanan'
    },
    sections: {
      videos: 'Pembelajaran Video',
      quiz: 'Uji Pengetahuan Anda',
      resources: 'Sumber Pembelajaran',
      numbers: 'TERUMBU KARANG DALAM NOMBOR'
    },
    quiz: {
      button: '🎯 Ambil Kuiz',
      description: 'Uji pengetahuan anda tentang terumbu karang'
    },
    videoCategories: {
      basics: 'Memahami Terumbu Karang',
      threats: 'Ancaman dan Pemuliharaan',
      protection: 'Cara Melindungi'
    },
    numbers: {
      mainTypes: {
        title: 'jenis utama terumbu karang',
        description: 'Terumbu pinggir memeluk garis pantai, terumbu penghalang terletak di belakang lagun, dan atol membentuk cincin di lautan terbuka. Mengetahui pembentukannya membantu kita memahami mengapa terumbu tertentu berkembang maju di tempat tertentu.',
        learnMore: 'Ketahui Lebih Lanjut'
      },
      tourismValue: {
        title: 'nilai pelancongan tahunan RM di Tioman',
        description: 'Terumbu yang sihat menarik penyelam, melindungi pantai, dan menyokong perikanan—memacu ekonomi pelancongan Malaysia.',
        learnMore: 'Ketahui Lebih Lanjut'
      },
      marineSpecies: {
        title: 'spesies marin bergantung pada terumbu',
        description: 'Terumbu meliputi kurang daripada 1% lautan namun menyokong 25% kehidupan marin, melindungi pantai, memberi makan komuniti, dan menyimpan potensi penawar perubatan.',
        learnMore: 'Ketahui Lebih Lanjut'
      },
      bombFishing: {
        title: 'kejadian pengeboman ikan',
        description: 'AI membantu melindungi terumbu—dari menjejaki "lagu" ikan yang menandakan pemulihan hingga mengesan pengeboman ikan haram di perairan yang luas.',
        learnMore: 'Ketahui Lebih Lanjut'
      },
      fishSpecies: {
        title: 'spesies ikan pada terumbu tunggal',
        description: 'Seperti bandar bawah air, terumbu penuh dengan kehidupan—dari ikan terumbu kecil hingga pemangsa puncak—menjadikannya ekosistem yang sangat produktif.',
        learnMore: 'Ketahui Lebih Lanjut'
      },
      coralsOutplanted: {
        title: 'karang ditanam semula di GBR',
        description: 'Program Penjagaan Karang menanam serpihan di tapak semaian dan memulihkan terumbu—sains dan pelancongan bekerjasama.',
        learnMore: 'Ketahui Lebih Lanjut'
      }
    },
    verification: {
      title: 'Pengesahan Kesihatan Terumbu Karang',
      description: 'Sila kenal pasti sama ada terumbu karang dalam 6 imej berikut adalah sihat',
      close: '×'
    },
    videos: {
      important: 'Mengapa terumbu karang begitu penting',
      dieOff: 'Apa yang akan berlaku jika semua terumbu karang mati',
      dying: 'Terumbu karang sedang mati. Inilah cara kita menyelamatkannya',
      bleaching: 'Pemutihan Karang Dijelaskan: Kisah Frank si Karang'
    }
  },
  
  // Halaman alat AI
  aiTools: {
    title: 'Klasifikasi Karang AI',
    subtitle: 'Muat naik imej karang menggunakan model AI canggih kami',
    disclaimer: {
      warning: '⚠️ Ciri ini hanya untuk pengenalan status kesihatan karang. Sila jangan muat naik imej yang tidak berkaitan.',
      accuracy: '📋 Hasil AI mungkin tidak tepat dan hanya untuk rujukan.'
    },
    upload: {
      title: 'Muat Naik Imej Karang',
      description: 'Seret dan lepaskan imej karang anda di sini, atau klik untuk melayari',
      formats: ['JPG', 'PNG', 'JPEG'],
      fileSizeLimit: 'Saiz fail maksimum: 3MB',
      changeImage: '🔄 Tukar Imej',
      remove: '❌ Buang',
      classify: 'Klasifikasi Karang',
      classifying: 'Mengklasifikasi...'
    },
    results: {
      title: 'Hasil Klasifikasi',
      species: 'Spesies karang dikenal pasti oleh AI',
      classifyAnother: '🔄 Klasifikasi Imej Lain'
    },
    error: {
      title: 'Klasifikasi Gagal',
      close: '×'
    }
  },
  
  // Pengaki
  footer: {
    copyright: '© 2025 CoralKita',
    contact: 'Hubungi Kami'
  },

  // Halaman butiran pulau
  islandDetail: {
    description: 'Penerangan',
    travelInfo: 'Maklumat Perjalanan',
    arrive: 'Tiba',
    reminder: 'Peringatan',
    snorkelingDiving: 'Snorkeling & Menyelam',
    accommodation: 'Penginapan',
    hotelRecommendation: 'Cadangan Hotel / Resort',
    tourActivities: 'Aktiviti Pelancongan & Pengembaraan',
    exploreDiving: 'Terokai Menyelam',
    marineParkSnorkeling: 'Snorkeling Taman Marin',
    exploreSnorkeling: 'Terokai Snorkeling',
    chooseDiving: 'Pilih Menyelam',
    chooseSnorkeling: 'Pilih Snorkeling',
    loading: 'Memuatkan maklumat pulau...',
    pSinga: {
      title: 'Pulau Singa, Langkawi',
      description: 'Pulau Singa, sebahagian daripada kepulauan Langkawi di Kedah, adalah pulau kecil dalam Taman Marin Langkawi. Dikenali dengan airnya yang jernih, terumbu cetek, dan pantai berpasir yang tenang, ia sering dimasukkan dalam itinerari melompat pulau dari Langkawi. Pulau ini tidak berpenghuni dan tidak mempunyai penginapan atau kemudahan tetap, menjadikannya terutamanya sebagai perhentian perjalanan sehari dan snorkeling.',
      arrive: 'Pulau Singa dicapai dengan bot laju dari Langkawi (20-40 minit dari Jetty Kuah atau Pantai Cenang), sering digabungkan dengan Dayang Bunting dan Beras Basah dalam lawatan melompat pulau.',
      reminder: 'Tiada jeti wujud, jadi pendaratan bergantung pada cuaca; sesetengah perjalanan mungkin hanya snorkeling.',
      snorkeling: 'Taman karang cetek sesuai untuk pemula dan keluarga, dengan ikan berwarna-warni (ikan badut, damselfish, wrasses) dan penyu sekali-sekala. Menyelam terhad tetapi tersedia melalui pakej Taman Marin Langkawi.',
      accommodation: 'Tiada penginapan di Pulau Singa; pelawat tinggal di Langkawi, di mana hotel dan eko-lodge tersedia. Pengendali mengatur perjalanan sehari.',
      tips: 'Petua',
      tipsContent: 'Masa terbaik untuk melawat adalah semasa musim kering (November – April) apabila laut lebih tenang dan keterlihatan lebih baik.',
      tipsContent2: 'Semasa bulan yang lebih basah (Mei – Oktober), keadaan laut mungkin lebih bergelora.',
      conservation: 'Amalan mesra pemuliharaan',
      conservationContent: 'Pelawat digalakkan menggunakan pelindung matahari selamat terumbu, elakkan menyentuh atau memijak karang dan katil rumput laut, dan bawa semua sampah kembali ke Langkawi kerana pulau tidak mempunyai kemudahan sisa. Memberi makan ikan atau mengganggu hidupan marin harus dielakkan, dan lawatan terbaik diatur dengan pengendali berlesen yang mengikuti garis panduan Taman Marin Langkawi untuk memastikan terumbu kekal sihat untuk pelawat masa depan.',
      dataiResort: 'The Datai Langkawi',
      dataiDesc: 'Resort hutan hujan mewah dengan pemandangan Laut Andaman yang menakjubkan',
      dannaResort: 'The Danna Langkawi',
      dannaDesc: 'Resort mewah gaya kolonial dengan akses pantai persendirian',
      frangipaniResort: 'The Frangipani Langkawi',
      frangipaniDesc: 'Resort tepi pantai mesra alam pemenang anugerah',
      ambongResort: 'Ambong Ambong',
      ambongDesc: 'Vila mewah mampan dengan kolam persendirian dan pemandangan laut',
      booking: 'Tempahan',
      operatorsTours: 'Pengendali & Lawatan',
      operatorsDesc: 'Lawatan melompat pulau dari Pantai Cenang dan Jetty Kuah (pelbagai pengendali tempatan)',
      operatorsDesc2: 'Lawatan eko tersuai tersedia dengan panduan Taman Marin Langkawi berlesen.',
      padiDiving: 'PADI Langkawi Scuba - Pusat Menyelam',
      padiDesc1: 'Menyelam bot di sekitar Taman Marin Langkawi',
      padiDesc2: 'Mesra pemula & pilihan pensijilan',
      checkTour: 'Semak Lawatan',
      unescoJetSki: 'UNESCO Geopark Jet-Ski - Pulau',
      jetSkiDesc1: 'Melompat pulau dari Pantai Cenang',
      jetSkiDesc2: 'Panduan & taklimat keselamatan disertakan'
    },
    mertang: {
      title: 'Pulau Mertang, Johor',
      description: 'Pulau Mertang, terletak dalam negeri Johor, Malaysia, terdiri daripada sekumpulan pulau kecil luar pesisir (Mertang Barat / Mertang Timur) yang terletak di pantai timur Johor berhampiran perairan Mersing. Rekod geografi mengklasifikasikan mereka sebagai \'pulau kecil\' (pulau kecil atau banc pasir), biasanya tidak mempunyai penginapan.',
      arrive: 'Anda boleh memilih untuk bertolak dari jeti di kawasan Mersing/Endau. Sila sahkan dengan pengendali bot tempatan atau pengurusan resort sama ada mereka menawarkan perhentian di tapak melompat pulau/snorkeling Mertang. Adalah dinasihatkan untuk menggabungkan Mertang dengan kumpulan pulau jiran yang lebih kerap dikunjungi (seperti Rawa, Sembilang, Sri Buat, Dayang, dll.) untuk meningkatkan kebolehgunaan perjalanan dan fleksibiliti laluan. Gambaran keseluruhan geografi dan destinasi menunjukkan bahawa kawasan Mersing berfungsi sebagai titik berlepas utama untuk mengakses pulau-pulau luar pesisir ini.',
      reminder: 'Mertang adalah pulau yang sangat kecil tanpa kemudahan pendaratan kekal yang tersedia secara awam atau perkhidmatan feri berjadual. Adalah penting untuk mengesahkan dengan pengendali bot tentang keadaan laut hari itu, sama ada snorkeling akan menjadi satu-satunya aktiviti tanpa pendaratan pulau, dan butiran lain (untuk mengelakkan memijak/mengganggu habitat burung atau reptilia).',
      snorkeling: 'Jika anda memilih untuk snorkeling dalam aktiviti pantai di kawasan Pulau Mertang, sila patuhi prinsip "tidak menyentuh, tidak memijak, dan tidak mengumpul".',
      accommodation: 'Mersing / Johor',
      accommodationDesc: 'Oleh kerana Mertang terdiri terutamanya daripada pulau kecil yang tidak berpenghuni, penginapan biasanya diatur di Mersing atau pulau jiran seperti Rawa. Dari sana, pelawat melakukan perjalanan sehari, melompat pulau, atau menyewa bot bersama untuk meneroka perairan Mertang. Rawa menawarkan panduan destinasi yang komprehensif dan maklumat produk resort, memudahkan perjalanan yang disesuaikan dalam kerjasama dengan pengendali bot.',
      orcaDiving: 'Orca Nation Diving',
      orcaDesc: 'Temui dunia bawah air dengan panduan bertauliah dan terumbu karang yang murni.',
      rawaResort: 'Rawa Island Resort',
      rawaDesc: 'Resort pulau tropika idilis dengan pantai murni dan terumbu karang',
      orcaResort: 'Orca Nation Resort',
      orcaResortDesc: 'Pelarian pulau persendirian dengan perairan jernih kristal dan pantai pasir putih',
      islandHopping: 'Melompat Pulau',
      book: 'Tempah',
      tips: 'Petua',
      tipsContent: 'Semasa musim monsun (sekitar November hingga Februari), keadaan laut tidak stabil dan perjalanan sering diselaraskan atau dibatalkan.',
      conservation: 'Amalan mesra pemuliharaan',
      conservationContent: 'Gunakan pelindung matahari selamat terumbu, elakkan menyentuh dasar laut atau mengambil objek semasa snorkeling, jangan beri makan ikan, dan jangan memijak kawasan bersarang di banc pasir. Rawa terletak dalam Taman Marin Sultan Iskandar, di mana memancing dan aktiviti air bermotor adalah terhad.'
    },
    sipadan: {
      title: 'Pulau Sipadan, Sabah',
      description: 'Pulau Sipadan, terletak di pantai timur Sabah, adalah satu-satunya pulau lautan sebenar Malaysia, dibentuk oleh gunung berapi yang telah pupus yang naik 600 meter dari dasar laut. Diiktiraf di seluruh dunia sebagai salah satu destinasi menyelam teratas, Sipadan adalah rumah kepada biodiversiti marin yang luar biasa termasuk penyu, barakuda, jerung terumbu, dan dinding karang yang bersemangat. Pulau ini adalah taman marin yang dilindungi, dan penginapan semalaman tidak dibenarkan.',
      arrive: 'Akses ke Sipadan adalah melalui bandar Semporna. Pelawat biasanya terbang ke Lapangan Terbang Tawau di Sabah dari bandar-bandar utama seperti Kuala Lumpur atau Kota Kinabalu. Dari Tawau, ia adalah kira-kira 1 jam perjalanan ke Semporna, diikuti dengan perjalanan bot laju 45-60 minit dari Jetty Semporna ke Sipadan, atau ke pulau-pulau berhampiran seperti Mabul atau Kapalai, di mana resort dan pengendali menyelam mengatur perjalanan sehari ke Sipadan.',
      reminder: 'Sipadan adalah taman marin yang dilindungi tanpa penginapan semalaman atau kemudahan awam. Akses terhad kepada 120 permit sehari melalui pengendali menyelam berlesen, jadi pelawat harus mengesahkan permit, jadual bot, dan keadaan laut terlebih dahulu.',
      snorkeling: 'Sipadan diiktiraf secara global sebagai destinasi menyelam utama. Jatuhan curam pulau dan terumbu sekitarnya menyediakan beberapa pengalaman bawah air yang paling menakjubkan di dunia.',
      iconicSites: 'Tapak menyelam ikonik: Barracuda Point, Turtle Cavern, dan The Drop-Off.',
      marineLife: 'Hidupan marin: Sekolah besar ikan jack, barakuda berputar, ikan kakatua kepala bonggol, jerung terumbu, dan puluhan penyu hijau dan penyu sisik. Penyelam maju juga mungkin menemui jerung tukul dan jerung paus pada musim tertentu.',
      snorkelingNote: 'Snorkeling terhad mungkin di kawasan cetek, tetapi pulau ini terutamanya sesuai untuk penyelam berpengalaman kerana kedalaman, arus, dan sistem permit yang dilindungi.',
      accommodation: 'Tiada penginapan di Sipadan. Kebanyakan pelawat tinggal di pulau berhampiran seperti Mabul atau Kapalai, yang menawarkan resort, chalet air, dan pakej menyelam penuh termasuk permit Sipadan.',
      kapalaiResort: 'Sipadan Kapalai Dive Resort',
      kapalaiDesc: 'Resort kampung air di atas tiang dengan akses menyelam bertaraf dunia ke Sipadan',
      mabulResort: 'Mabul Beach Resort',
      mabulDesc: 'Resort tepi pantai mesra alam dengan pusat menyelam PADI pemenang anugerah',
      seaventuresResort: 'Seaventures Dive Rig',
      seaventuresDesc: 'Platform minyak unik yang ditukar menjadi resort menyelam mewah dengan akses langsung ke Sipadan',
      booking: 'Tempahan',
      tips: 'Petua',
      tipsContent: 'Masa terbaik untuk melawat adalah April – Disember, apabila laut tenang dan keterlihatan adalah 20–40 meter. Dari Januari – Mac, laut mungkin lebih kasar, walaupun terdapat lebih sedikit pelancong.',
      tipsContent2: 'Tempah awal kerana permit terhad kepada 120 sehari dan biasanya terikat dengan pakej resort atau menyelam. Bawa kad pensijilan menyelam; Sipadan tidak sesuai untuk pemula.',
      tipsContent3: 'Tiada kemudahan di pulau; penginapan, makanan, dan logistik menyelam diatur dari Mabul, Kapalai, atau Semporna. Bawa pelindung matahari selamat terumbu dan keperluan lain.',
      conservation: 'Amalan mesra pemuliharaan',
      conservationContent: 'Sipadan adalah taman marin yang dilindungi dengan ketat. Pelawat harus menyelam hanya dengan pengendali berlesen, elakkan menyentuh atau mengganggu hidupan marin, dan mengekalkan daya apung yang betul untuk melindungi karang yang rapuh.',
      conservationContent2: 'Memancing, aktiviti air bermotor, dan penginapan semalaman adalah dilarang. Memilih resort menyelam yang diperakui eko membantu memastikan pulau kekal murni untuk generasi akan datang.'
    },
    pulauLima: {
      title: 'Pulau Lima, Terengganu',
      description: 'Pulau Lima terdiri daripada sepasang pulau kecil (Lima Besar / Lima Kecil) dalam Taman Marin Sultan Iskandar. Dikelolakan oleh Taman Johor, taman marin merangkumi pelbagai pulau di luar pantai Mersing, menjalankan aktiviti pemuliharaan dan pendidikan.',
      arrive: 'Anda boleh terlebih dahulu pergi ke Mersing di Johor (titik persinggahan untuk memulakan perjalanan laut) untuk menyelesaikan formaliti di Mersing Harbour Centre (Pusat Perkhidmatan Terminal Feri Mersing). Di sini, anda akan mengesahkan jadual feri/titik perhimpunan dan membayar yuran taman marin/pemuliharaan (caj dikenakan untuk kedua-dua akses pulau dan kemasukan ke zon taman marin). Sebagai alternatif, anda boleh memilih untuk menaiki bot laju/bot sewaan atau menyertai lawatan melompat pulau yang dianjurkan dari Jetty Mersing ke kawasan Pulau Lima. Sebagai pulau yang tidak berpenghuni, Pulau Lima biasanya tidak mempunyai perkhidmatan feri awam yang tetap.',
      reminder: 'Pendekatan biasa termasuk menyewa bot dari Mersing, menyertai lawatan melompat pulau, atau mengatur lawatan melalui pusat menyelam tempatan dan resort di kawasan Sibu.',
      accommodation: 'Mersing / Johor',
      accommodationDesc: 'Oleh kerana Pulau Lima terdiri terutamanya daripada pulau kecil yang tidak berpenghuni, penginapan biasanya diatur di Mersing atau pulau jiran seperti Sibu. Dari sana, pelawat melakukan perjalanan sehari, melompat pulau, atau menyewa bot bersama untuk meneroka perairan Lima. Sibu menawarkan panduan destinasi yang komprehensif dan maklumat produk resort, memudahkan perjalanan yang disesuaikan dalam kerjasama dengan pengendali bot.',
      seaGypsyResort: 'Sea Gypsy Village Resort',
      seaGypsyDesc: 'Resort mesra alam dengan panduan snorkeling profesional dan akses taman marin',
      rimbaResort: 'Rimba Resort',
      rimbaDesc: 'Resort teluk semula jadi dengan sekolah menyelam dan penginapan tepi pantai murni',
      myVillaRecreation: 'MYVilla Recreation',
      myVillaDesc: 'Homestay mesra alam dengan aktiviti memancing, menyelam, dan trekking hutan',
      islandHopping: 'Melompat Pulau',
      islandHoppingDesc: 'Terokai pelbagai pulau dengan lawatan berpandu dan pengembaraan snorkeling',
      marineParkTours: 'Lawatan Taman Marin',
      marineParkToursDesc: 'Lawatan berpandu profesional ke Taman Marin Sultan Iskandar dengan panduan bertauliah',
      book: 'Tempah',
      snorkeling: 'Tapak Snorkeling Lima Besar & Lima Kecil: Pusat menyelam tempatan dan resort di kawasan Sibu secara eksplisit menyenaraikan Lima Besar dan Lima Kecil sebagai laluan snorkeling dan titik pengalaman pantai, sesuai untuk pemula dan keluarga (tertakluk kepada keterlihatan dan keadaan laut).',
      marinePark: 'Snorkeling Taman Marin',
      marineParkDesc: 'Terokai terumbu karang murni dan hidupan marin yang pelbagai di Taman Marin Sultan Iskandar dengan panduan bertauliah.',
      tips: 'Petua',
      tipsContent: 'Keadaan menyelam dan keterlihatan di wilayah Mersing (terbaik dari Mac hingga September; beberapa pengendali menggantung atau mengurangkan perkhidmatan semasa musim monsun timur laut dari November hingga Februari).'
    },
    seriBuat: {
      title: 'Pulau Seri Buat, Pahang',
      description: 'Pulau Seri Buat, juga dikenali sebagai Pulau Rahsia, adalah salah satu rahsia terbaik Pahang dalam Taman Marin Sultan Iskandar. Dikenali dengan tebing batu kapur, air jernih kristal, dan pantai terpencil, Seri Buat adalah perhentian yang tenang yang sering digabungkan dengan pulau berhampiran seperti Pulau Rawa dan Pulau Sembilang. Sering dipanggil "Krabi Malaysia" kerana persamaannya dengan garis pantai Krabi Thailand, pulau ini menawarkan kedua-dua ketenangan dan pengembaraan, menjadikannya pelarian yang sempurna untuk pelancong eko dan penjelajah hujung minggu.',
      arrive: 'Akses ke Pulau Seri Buat adalah dengan bot dari sama ada Jetty Mersing (Johor) atau Jetty Tanjung Gemok (Pahang), dengan masa perjalanan kira-kira 30-45 minit bergantung pada titik berlepas. Pelawat biasanya menyewa bot atau menyertai lawatan melompat pulau yang termasuk Seri Buat bersama pulau-pulau lain dalam kluster Mersing, seperti Pulau Rawa, Pulau Sembilang, dan Pulau Harimau.',
      reminder: 'Seri Buat tidak mempunyai perkhidmatan feri awam, laluan berjadual, atau jeti kekal. Perjalanan bergantung pada keadaan laut, dan dalam sesetengah kes, lawatan mungkin terhad kepada snorkeling atau perhentian luar pesisir.',
      snorkeling: 'Perairan turquoise Seri Buat adalah ideal untuk snorkeling, dengan ikan terumbu berwarna-warni dan keterlihatan yang baik. Walaupun peluang menyelam lebih maju di sekitar pulau berhampiran, Seri Buat adalah perhentian popular untuk:',
      snorkelingList: [
        'Snorkeling di teluk terlindung',
        'Berenang di perairan zamrud di bawah tebing dramatik',
        'Melompat tebing ke laut di tempat selamat yang ditetapkan'
      ],
      hikingAdventure: 'Mendaki & Pengembaraan',
      hikingContent: 'Pendakian pendek 10-15 minit membawa ke titik pandangan yang indah dengan pemandangan panorama Seri Buat dan perairan sekitarnya. Untuk pengembaraan yang lebih, pelawat boleh meneroka batu karang atau menyertai lawatan melompat pulau yang termasuk Pulau Harimau berhampiran, terkenal dengan kolam semula jadi tersembunyi dan tempat melompat tebing yang popular.',
      recommendedResorts: 'Resort & Pengendali Disyorkan',
      rawaResort: 'Rawa Island Resort',
      rawaDesc: 'Resort pulau mewah dengan pantai murni dan aktiviti air',
      stepFootprint: '1Step1Footprint',
      stepFootprintDesc: 'Lawatan melompat pulau profesional dengan mendaki dan snorkeling',
      amiTravel: 'AMI Travel',
      amiTravelDesc: 'Pakej perjalanan sehari 8 pulau dengan aktiviti komprehensif',
      explore: 'Terokai',
      tips: 'Petua',
      tipsContent: 'Masa terbaik untuk melawat adalah Mac – Oktober, apabila laut tenang.',
      tipsContent2: 'Elakkan November – Februari (musim monsun), apabila keadaan laut boleh kasar.',
      tipsContent3: 'Bawa pelindung matahari selamat terumbu, air, dan snek, kerana tiada kemudahan di pulau.',
      tipsContent4: 'Tempah lawatan melompat pulau terlebih dahulu untuk memastikan tempat anda.',
      tipsContent5: 'Jangan terlepas pendakian pendek ke titik pandangan untuk pemandangan yang menakjubkan.',
      tipsContent6: 'Berhati-hati semasa snorkeling atau melompat tebing, dan sentiasa ikuti panduan pengendali.',
      conservation: 'Amalan mesra pemuliharaan',
      conservationContent: 'Sebagai sebahagian daripada Taman Marin Sultan Iskandar, Seri Buat berada di bawah perlindungan. Pelawat harus elakkan menyentuh atau memijak karang, jangan beri makan ikan, dan bawa semua sampah kembali ke daratan. Gunakan pelindung matahari selamat terumbu untuk melindungi hidupan marin, dan hormati kawasan bersarang di banc pasir atau batu karang. Memilih pengendali mesra alam yang berlesen membantu memelihara keindahan semula jadi pulau untuk pelawat masa depan.',
      accommodation: 'Tiada penginapan tersedia di pulau. Pelawat biasanya tinggal di Mersing atau bandar pantai berhampiran dan mengambil perjalanan sehari ke pulau.'
    },
    actionButtons: {
      reefPolicies: 'Pelajari Dasar Terumbu Karang',
      reefConservation: 'Pelajari Pemuliharaan Terumbu'
    }
  },

  // Halaman kuiz
  quiz: {
    title: 'Kuiz Pengetahuan Terumbu Karang',
    startButton: 'Mula Kuiz',
    nextButton: 'Soalan Seterusnya',
    submitButton: 'Hantar Kuiz',
    restartButton: 'Mula Semula',
    question: 'Soalan',
    of: 'daripada',
    score: 'Skor',
    correct: 'Betul!',
    incorrect: 'Salah',
    completed: 'Kuiz Selesai!',
    yourScore: 'Skor Anda',
    feedback: {
      excellent: 'Cemerlang! Anda seorang pakar terumbu karang!',
      good: 'Bagus! Anda tahu banyak tentang terumbu karang.',
      average: 'Usaha yang baik! Terus belajar tentang terumbu karang.',
      needImprovement: 'Terus belajar! Lawati Pusat Pendidikan untuk ketahui lebih lanjut.'
    }
  },
  
  
  // Halaman senarai semak
  checklist: {
    title: 'Senarai Semak Perjalanan',
    subtitle: 'Bersedia untuk pengembaraan terumbu karang anda',
    categories: {
      equipment: 'Peralatan Menyelam',
      safety: 'Barangan Keselamatan',
      documentation: 'Dokumentasi',
      personal: 'Barangan Peribadi'
    },
    markComplete: 'Tandakan Selesai',
    addItem: 'Tambah Item'
  },
  
  // Umum
  common: {
    loading: 'Memuatkan...',
    error: 'Ralat',
    success: 'Berjaya',
    save: 'Simpan',
    cancel: 'Batal',
    confirm: 'Sahkan',
    delete: 'Padam',
    edit: 'Edit',
    close: 'Tutup',
    search: 'Cari',
    filter: 'Tapis',
    sort: 'Susun',
    viewDetails: 'Lihat Butiran',
    learnMore: 'Ketahui Lebih Lanjut',
    backToHome: 'Kembali ke Halaman Utama'
  },
  
  // Widget sembang
  chat: {
    title: 'Pembantu Karang',
    placeholder: 'Tanya saya apa sahaja tentang terumbu karang...',
    send: 'Hantar',
    minimize: 'Minimumkan',
    close: 'Tutup'
  },
  
  // Halaman Alat AI
  aiTools: {
    title: 'Klasifikasi Karang AI',
    subtitle: 'Muat naik imej karang dan dapatkan pengenalan spesies serta-merta',
    disclaimer: {
      warning: 'Alat ini hanya untuk tujuan pendidikan',
      accuracy: 'Hasil mungkin berbeza dan tidak boleh menggantikan pengenalan profesional'
    },
    upload: {
      title: 'Muat Naik Imej Karang Anda',
      description: 'Pilih imej karang yang jelas dan terang untuk hasil terbaik',
      formats: ['JPG', 'PNG', 'JPEG', 'WEBP'],
      fileSizeLimit: 'Saiz fail maksimum: 10MB',
      changeImage: 'Tukar Imej',
      remove: 'Buang',
      classify: 'Klasifikasi Karang',
      classifying: 'Menganalisis...'
    },
    results: {
      title: 'Hasil Klasifikasi',
      species: 'Spesies Karang',
      classifyAnother: 'Klasifikasi Lain'
    },
    error: {
      title: 'Ralat Klasifikasi',
      close: 'Tutup'
    },
    howToUse: {
      title: 'Cara Menggunakan',
      step1: {
        title: 'Muat Naik Imej',
        description: 'Pilih imej karang yang jelas dari peranti anda'
      },
      step2: {
        title: 'Analisis AI',
        description: 'Model AI kami menganalisis ciri-ciri dan corak karang'
      },
      step3: {
        title: 'Dapatkan Hasil',
        description: 'Terima pengenalan spesies dan maklumat serta-merta'
      }
    }
  },
  
  // Halaman Soalan Lazim
  faq: {
    title: 'Soalan Lazim',
    subtitle: 'Cari jawapan kepada soalan lazim tentang CoralKita',
    generalInfo: {
      title: 'Maklumat Umum',
      q1: {
        question: 'S: Apakah tujuan laman web ini?',
        answer: 'J: Platform ini menyediakan alat interaktif, visualisasi data, dan chatbot AI untuk membantu pengguna memahami kesihatan terumbu karang Malaysia. Ia bertujuan untuk meningkatkan kesedaran tentang pemutihan karang, mempromosikan pelancongan yang bertanggungjawab, dan menjadikan data saintifik terumbu lebih mudah diakses.'
      },
      q2: {
        question: 'S: Untuk siapa platform ini direka?',
        answer: 'J: Ia direka untuk pelancong, pelajar, penyelidik, pencinta alam sekitar, NGO, dan pembuat dasar yang berminat dalam kesihatan terumbu karang dan perjalanan mampan.'
      }
    },
    dataAndSources: {
      title: 'Data dan Sumber',
      q1: {
        question: 'S: Dari mana data ini diperoleh?',
        answer: 'J: Data diperoleh terutamanya daripada laporan tinjauan terumbu karang tahunan dan metadata mentah dari Reef Check Malaysia. Pautan: <a href="https://reefcheck.org.my/interactive-map/" target="_blank" style="color: #4facfe; text-decoration: underline;">https://reefcheck.org.my/interactive-map/</a>'
      },
      q2: {
        question: 'S: Berapa kerap data dikemaskini?',
        answer: 'J: Laporan tinjauan terumbu karang dikeluarkan setiap tahun oleh itu data dikemaskini sekali setahun.'
      },
      q3: {
        question: 'S: Adakah laman web ini berafiliasi dengan mana-mana badan kerajaan?',
        answer: 'J: Tidak, CoralKita adalah laman web bebas. Semua maklumat yang diperoleh disebut untuk memastikan ketelusan.'
      }
    },
    disclaimer: {
      title: 'Penafian dan Had',
      q1: {
        question: 'S: Adakah maklumat ini 100% tepat?',
        answer: 'J: Walaupun data diperoleh daripada sumber yang boleh dipercayai, ketepatan mungkin berbeza-beza kerana perubahan alam sekitar atau data tinjauan yang tidak lengkap. Platform ini tidak menggantikan penilaian alam sekitar profesional.'
      },
      q2: {
        question: 'S: Siapa yang bertanggungjawab terhadap respons chatbot?',
        answer: 'J: Respons dijana secara automatik berdasarkan laporan rasmi dan model pra-latihan. Ia bertujuan untuk tujuan pendidikan dan tidak boleh digunakan sebagai nasihat profesional.'
      }
    }
  },
  
  // Senarai Perjalanan
  travelChecklist: {
    title: 'Senarai Perjalanan Bertanggungjawab',
    subtitle: 'Lengkapkan langkah-langkah utama untuk pelancongan lestari',
    back: 'Kembali',
    conservationAction: 'Tindakan Pemuliharaan CoralKita',
    questionnaire: {
      beforeWeStart: 'Sebelum Kita Mulai...',
      description: 'Jawab beberapa soalan untuk mendapatkan senarai peribadi anda',
      progress: 'Soalan {current} daripada {total}',
      previous: 'Sebelumnya',
      next: 'Seterusnya',
      showChecklist: 'Tunjukkan Senarai Saya'
    },
    questions: {
      firstTime: 'Adakah ini kali pertama anda menyelam/berenang?',
      travellingWithKids: 'Adakah anda melancong dengan kanak-kanak?',
      reefCondition: 'Keadaan terumbu karang pulau yang anda rancang untuk lawati:',
      swimmingLevel: 'Tahap kemahiran berenang:'
    },
    options: {
      yes: 'Ya',
      no: 'Tidak',
      good: 'Baik',
      fair: 'Sederhana',
      poor: 'Lemah',
      unknown: 'Tidak diketahui',
      beginner: 'Pemula',
      intermediate: 'Pertengahan',
      advanced: 'Lanjutan'
    },
    categories: {
      experience: {
        title: 'Berdasarkan Pengalaman Anda',
        firstTime: 'Petua untuk pemula',
        experienced: 'Panduan berpengalaman',
        description: 'Persiapan penting berdasarkan pengalaman menyelam/berenang anda'
      },
      traveling: {
        titleWithKids: 'Melancong dengan Kanak-kanak',
        titleSolo: 'Melancong Sendiri/Dewasa',
        familyTips: 'Petua mesra keluarga',
        independent: 'Perjalanan bebas',
        descriptionWithKids: 'Pertimbangan khas semasa melancong dengan kanak-kanak',
        descriptionSolo: 'Petua untuk pelancong bebas'
      },
      reefCondition: {
        title: 'Berdasarkan Keadaan Terumbu Karang',
        goodCondition: 'Keadaan baik',
        fairCondition: 'Keadaan sederhana',
        poorCondition: 'Keadaan lemah',
        unknownCondition: 'Keadaan tidak diketahui',
        description: 'Garis panduan khusus untuk kesihatan terumbu karang di destinasi anda'
      },
      swimmingSkills: {
        title: 'Berdasarkan Kemahiran Renang Anda',
        beginnerLevel: 'Tahap pemula',
        intermediateLevel: 'Tahap pertengahan',
        advancedLevel: 'Tahap lanjutan',
        description: 'Cadangan keselamatan dan aktiviti untuk tahap kemahiran anda'
      }
    },
    checklist: {
      question1: {
        yes: {
          item1: 'Hadiri taklimat keselamatan menyelam/berenang asas sebelum memasuki air.',
          item2: 'Berlatih menggunakan peralatan berenang di air cetek atau kolam.',
          item3: 'Baca tentang tingkah laku selamat terumbu (cth, jangan sentuh atau berdiri di atas karang).',
          item4: 'Gunakan pelindung matahari selamat terumbu (berasaskan zink, tidak toksik).',
          item5: 'Bawa rash guard atau pakaian renang berlengan panjang untuk perlindungan matahari.',
          item6: 'Periksa cuaca tempatan dan masa air pasang sebelum perjalanan anda.',
          item7: 'Pilih operator eko-pelancongan yang diperakui yang mempromosikan pemuliharaan terumbu.'
        },
        no: {
          item1: 'Segarkan pengetahuan anda tentang peraturan pemuliharaan terumbu — walaupun penyelam berpengalaman boleh lupa.',
          item2: 'Jika anda membawa peralatan sendiri, periksa dan bersihkan peralatan anda untuk mengelakkan membawa spesies invasif antara terumbu.',
          item3: 'Galakkan pemula dalam kumpulan anda untuk mengikuti garis panduan pemuliharaan.',
          item4: 'Semak sebarang peraturan tempatan baru atau kemas kini kesihatan terumbu sebelum menyelam.'
        }
      },
      question2: {
        yes: {
          item1: 'Pilih pantai atau tempat berenang mesra keluarga dengan air yang tenang dan cetek.',
          item2: 'Bawa peralatan berenang kanak-kanak yang sesuai dan uji sebelum perjalanan.',
          item3: 'Bungkus pakaian renang pelindung UV dan pelindung matahari selamat terumbu untuk kanak-kanak.',
          item4: 'Didik kanak-kanak awal: "Lihat, jangan sentuh" — terangkan mengapa karang dan hidupan marin mesti dilindungi.',
          item5: 'Pastikan kanak-kanak dekat dengan pantai atau dalam jangkauan tangan pada setiap masa.',
          item6: 'Jadualkan aktiviti air pada waktu pagi apabila laut lebih tenang dan orang ramai lebih sedikit.',
          item7: 'Sertai eko-pelancongan berpandu dengan aktiviti pembelajaran selamat kanak-kanak (cth, berjalan terumbu, lawatan pusat marin).',
          item8: 'Bawa banyak air minuman, snek, dan rehat — kanak-kanak letih menjadi perenang yang tidak selamat.',
          item9: 'Galakkan kanak-kanak mengumpul kenangan, bukan cenderamata — tiada cengkerang atau kepingan karang.'
        },
        no: {
          item1: 'Pilih sesi berenang/menyelam yang lebih lama atau lebih maju jika anda yakin di dalam air.',
          item2: 'Sokong operator yang menawarkan pendidikan marin untuk keluarga, walaupun anda melancong solo — sumbangan anda membantu membiayai outreach.',
          item3: 'Gunakan fleksibiliti anda untuk melawat terumbu yang kurang sesak dan mengurangkan tekanan pada tempat keluarga yang popular.'
        }
      },
      question3: {
        good: {
          item1: 'Bantu mengekalkan keadaan terumbu — elakkan menyentuh atau mengejar hidupan marin.',
          item2: 'Sertai program sains warganegara semasa berenang/menyelam (cth, pelaporan kesihatan terumbu).',
          item3: 'Sokong operator mesra alam tempatan dan projek pemuliharaan.',
          item4: 'Jangan tinggalkan sampah, dan bawa semua sampah kembali.'
        },
        fair: {
          item1: 'Elakkan sebarang sentuhan fizikal dengan terumbu — ia rapuh dan cuba pulih.',
          item2: 'Sokong atau dermakan kepada projek pemulihan terumbu tempatan.',
          item3: 'Pilih aktiviti tidak invasif (berenang bukan mengangkar bot atau berjalan di terumbu).',
          item4: 'Kongsi foto dan kesedaran secara bertanggungjawab — serlahkan keperluan pemuliharaan.'
        },
        poor: {
          item1: 'Elakkan sebarang sentuhan fizikal dengan terumbu — ia rapuh dan cuba pulih.',
          item2: 'Sokong atau dermakan kepada projek pemulihan terumbu tempatan.',
          item3: 'Pilih aktiviti tidak invasif (berenang bukan mengangkar bot atau berjalan di terumbu).',
          item4: 'Kongsi foto dan kesedaran secara bertanggungjawab — serlahkan keperluan pemuliharaan.'
        },
        unknown: {
          item1: 'Kaji laporan terkini tentang keadaan terumbu di peta interaktif kami sebelum lawatan anda.',
          item2: 'Hubungi pusat menyelam tempatan atau taman marin untuk kemas kini.',
          item3: 'Pelajari cara mengenali pemutihan karang dan laporkan penemuan kepada pihak berkuasa tempatan.',
          item4: 'Didik diri anda tentang amalan alam sekitar terbaik untuk semua keadaan terumbu.'
        }
      },
      question4: {
        beginner: {
          item1: 'Gunakan jaket hayat atau peranti apungan semasa berenang.',
          item2: 'Tinggal dekat pantai atau dengan panduan yang diperakui.',
          item3: 'Elakkan arus kuat dan air dalam.',
          item4: 'Berlatih teknik keselamatan air asas dan pernafasan sebelum perjalanan anda.',
          item5: 'Sentiasa pergi dengan rakan atau pelancongan berpandu untuk keselamatan.',
          item6: 'Jangan mengejar atau mendekati hidupan marin terlalu dekat.'
        },
        intermediate: {
          item1: 'Tinggal dalam zon berenang/menyelam yang ditetapkan.',
          item2: 'Perhatikan perubahan arus dan keadaan cuaca.',
          item3: 'Bantu perenang yang kurang berpengalaman jika diperlukan — tetapi jangan kompromi keselamatan anda.',
          item4: 'Kekalkan kawalan apungan yang baik untuk mengelakkan menyentuh karang.',
          item5: 'Bawa penanda apungan permukaan (SMB) jika menyelam.'
        },
        advanced: {
          item1: 'Periksa peralatan menyelam dengan teliti dan rancang penyelaman anda secara bertanggungjawab.',
          item2: 'Sukarelawan untuk pembersihan terumbu atau penyelaman pemantauan.',
          item3: 'Terokai secara bertanggungjawab — elakkan menyelam berlebihan di tempat yang sama untuk mengurangkan impak.',
          item4: 'Kongsi pengetahuan anda dengan pelancong yang kurang berpengalaman.',
          item5: 'Sokong usaha perlindungan terumbu melalui kumpulan marin tempatan.'
        }
      }
    },
    resetButton: 'Set Semula & Mulakan Semula'
  },

  // Government & Policy Information Hub
  government: {
    title: 'Hab Maklumat Kerajaan & Dasar',
    subtitle: 'Maklumat yang boleh dipercayai dan terkini mengenai dasar pemuliharaan terumbu karang Malaysia dan inisiatif rasmi',
    
    policyUpdates: {
      title: 'Kemaskini Kerajaan & Dasar',
      subtitle: 'Kemaskini ringkas mengenai dasar kerajaan semasa dan inisiatif pemuliharaan',
      
      updates: {
        sunscreen: {
          date: 'Jun 2025',
          title: 'Jabatan Taman Marin Menguatkuasakan Garis Panduan Pelindung Matahari Selamat Terumbu',
          description: 'Jabatan Perikanan Malaysia telah melaksanakan peraturan baru yang memerlukan semua pengunjung taman marin menggunakan pelindung matahari yang selamat untuk terumbu sahaja. Inisiatif ini bertujuan untuk mengurangkan pencemaran kimia yang merosakkan terumbu karang.',
          source: 'Sumber Rasmi: Jabatan Perikanan Malaysia'
        },
        fishing: {
          date: 'Ogos 2025',
          title: 'Pihak Berkuasa Taman Marin Melarang Perikanan Merosakkan Berhampiran Terumbu Redang',
          description: 'Langkah penguatkuasaan baru melarang semua bentuk amalan perikanan merosakkan dalam 2 batu nautika dari terumbu karang Pulau Redang. Pelanggar menghadapi penalti yang lebih tinggi untuk melindungi biodiversiti marin.',
          source: 'Sumber Rasmi: Taman Marin Malaysia'
        },
        monitoring: {
          date: '2024',
          title: 'Program Pemantauan Terumbu Kebangsaan Diperluas ke 200+ Tapak',
          description: 'Reef Check Malaysia, bekerjasama dengan Jabatan Perikanan, telah memperluas rangkaian pemantauan mereka ke lebih 200 tapak terumbu di seluruh negara, menyediakan data komprehensif mengenai kesihatan dan pemulihan terumbu.',
          source: 'Sumber Rasmi: Reef Check Malaysia'
        },
        restoration: {
          date: '2024',
          title: 'Program Pemulihan Karang di Taman Marin Tun Mustapha',
          description: 'Taman-Taman Sabah telah berjaya memasang lebih 1,000 bingkai terumbu dan memindahkan 12,000 serpihan karang di Taman Marin Tun Mustapha melalui kerjasama dengan sukarelawan dan komuniti tempatan.',
          source: 'Sumber Rasmi: Taman-Taman Sabah'
        }
      }
    },
    
    conservation: {
      title: 'Sokong Pemuliharaan Terumbu',
      subtitle: 'NGO yang disahkan dan program derma rasmi dengan pautan selamat',
      
      ngos: {
        reefCheck: {
          name: 'Reef Check Malaysia',
          verified: '✓ NGO Disahkan',
          description: 'Reef Check Malaysia adalah organisasi bukan untung berdaftar yang komited kepada pemuliharaan dan pengurusan mampan ekosistem terumbu karang Malaysia. Mereka menjalankan tinjauan kesihatan terumbu berkala, projek pemulihan karang, dan program pemuliharaan komuniti di seluruh negara.',
          focusAreas: 'Kawasan Fokus:',
          focusAreasText: 'Pemantauan terumbu, pemulihan karang, pendidikan marin',
          coverage: 'Liputan:',
          coverageText: 'Tapak terumbu di seluruh negara termasuk Redang, Tioman, Perhentian, dan lain-lain',
          button: 'Ketahui Lebih Lanjut & Derma melalui Laman Rasmi →'
        },
        wwf: {
          name: 'WWF Malaysia',
          verified: '✓ NGO Disahkan',
          description: 'WWF Malaysia adalah sebahagian daripada rangkaian WWF global, bekerja untuk melindungi ekosistem marin Malaysia termasuk terumbu karang. Usaha pemuliharaan terumbu mereka memberi tumpuan kepada mengurangkan pencemaran marin, mempromosikan pelancongan mampan, dan melindungi habitat marin kritikal.',
          focusAreas: 'Kawasan Fokus:',
          focusAreasText: 'Pemuliharaan marin, perikanan mampan, mitigasi perubahan iklim',
          coverage: 'Liputan:',
          coverageText: 'Kawasan perlindungan marin dan pantai Malaysia',
          button: 'Ketahui Lebih Lanjut & Derma melalui Laman Rasmi →'
        },
        tracc: {
          name: 'TRACC (Pusat Penyelidikan dan Pemuliharaan Tropika)',
          verified: '✓ NGO Disahkan',
          description: 'TRACC mengendalikan projek pemulihan terumbu karang dan penyelidikan marin di Sabah, Malaysia. Mereka bekerja dengan komuniti tempatan dan sukarelawan untuk memulihkan terumbu yang rosak dan menyediakan latihan mengenai pengurusan sumber marin yang mampan.',
          focusAreas: 'Kawasan Fokus:',
          focusAreasText: 'Pemulihan terumbu karang, penyelidikan marin, latihan komuniti',
          coverage: 'Liputan:',
          coverageText: 'Semporna dan perairan sekitarnya di Sabah',
          button: 'Ketahui Lebih Lanjut & Derma melalui Laman Rasmi →'
        }
      }
    }
  }
}

