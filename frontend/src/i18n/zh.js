export default {
  // 导航栏
  nav: {
    logo: 'CoralKita',
    mapRecommendation: {
      line1: '地图与',
      line2: '推荐'
    },
    island: '岛屿',
    education: '教育',
    aiClassification: 'AI分类',
    government: '政府',
    actionHub: '行动中心',
    policies: '政策',
    faq: '常见问题',
    changeLanguage: '语言'
  },
  
  // 首页
  home: {
    coral: {
      healthy: '这是您记忆中的珊瑚礁吗——明亮、多彩、充满生机？',
      fading: '如今，大多数珊瑚礁正在褪色，在气候变化和人类压力下挣扎求存。',
      bleached: '如果不采取行动，它们可能很快就会变成像这样毫无生气的白色骨架。'
    },
    intro: {
      title: '智能规划。绿色旅行。保护珊瑚礁。',
      description: 'CoralKita用真实的珊瑚礁数据指导您的旅行——帮助您在正确的时间、以正确的方式选择正确的岛屿，让您在享受马来西亚珊瑚礁美景的同时保护它们的美丽。'
    },
    features: {
      dataDriven: '数据驱动',
      responsible: '负责任',
      userFriendly: '用户友好'
    },
    exploreButton: '开始探索',
    footer: {
      copyright: '© 2025 CoralKita',
      contact: '联系我们'
    }
  },
  
  // 岛屿名称
  islands: {
    mertang: 'Mertang',
    pSinga: 'P Singa',
    sipadan: 'Sipadan',
    pulauLima: 'Pulau Lima',
    seriBuat: 'Seri Buat'
  },
  
  // 地图页面
  map: {
    title: '交互式热力地图',
    dataSource: '*基于2024年数据',
    gameMode: '🎮 珊瑚冒险游戏',
    mapMode: '🗺️ 交互式地图',
    selectPanel: {
      title: '选择面板',
      filterByLocation: '按州/岛屿筛选',
      selectType: '选择类型...',
      state: '州',
      island: '岛屿',
      selectState: '选择州',
      selectIsland: '选择岛屿',
      selectLocation: '选择位置',
      typeState: '输入或选择州...',
      typeIsland: '输入或选择岛屿...',
      selectFirst: '请先选择位置类型...',
      noStates: '未找到州',
      noIslands: '未找到岛屿',
      selectLocationLabel: '选择位置'
    },
    mapInstructions: {
      useLeftPanel: '使用左侧面板按州或岛屿筛选。',
      clickMarkers: '点击地图上的标记探索岛屿详情。',
      rightPanel: '右侧面板显示推荐的岛屿，您可以点击查看更多信息。'
    },
    coordinates: '坐标',
    recommendedIslands: {
      title: '推荐岛屿列表',
      subtitle: '列表基于活珊瑚覆盖率(%)和整体健康状况',
      clickToEnter: '点击进入岛屿页面'
    },
    comparison: {
      title: '岛屿比较可视化',
      panel: {
        title: '比较面板',
        description: '选择两个岛屿和一个属性来比较珊瑚礁健康趋势',
        selectIsland: '🏝️选择岛屿：',
        compareIsland: '⚖️比较岛屿：',
        selectAttribute: '📊选择属性：',
        chooseIsland: '选择岛屿',
        placeholder: '至少选择一个岛屿查看数据'
      },
      chart: {
        title: '岛屿比较 - 水平图表',
        yAxis: '百分比内容',
        xAxis: '时间'
      },
      additionalInfo: {
        title: '附加信息',
        question: '想了解每个指标揭示的珊瑚礁状况吗？',
        instruction: '请向下滚动查看珊瑚礁健康指标分析。'
      }
    },
    indicators: {
      title: '珊瑚礁健康指标分析',
      subtitle: '每个指标（如活珊瑚覆盖率、干扰、污染）代表珊瑚礁状况的一个比例，它们加起来等于100%，显示珊瑚礁区域如何分布在这些因素中。',
      lcc: {
        title: 'LCC（活珊瑚覆盖率）',
        good: '健康状态，表明活珊瑚覆盖面积大，生态系统稳定。',
        moderate: '中等水平，仍有恢复潜力但对外部压力敏感。',
        poor: '严重退化，可能被藻类、沙子或碎石替代，生态系统服务下降。'
      },
      as: {
        title: 'AS（可用基质）',
        good: '理想状态，大部分基质被珊瑚覆盖。',
        moderate: '警告区域，表明最近珊瑚死亡或破碎。',
        critical: '严重退化区域，表明大量珊瑚死亡，容易被藻类占据。'
      },
      sd: {
        title: 'SD（沙子/软珊瑚覆盖）',
        good: '适量，维持生态平衡的最佳状态。',
        poorHigh: '沙子过多可能掩埋硬珊瑚并阻碍幼虫定居。',
        poorLow: '过低，可能表明生态系统失衡，缺乏必要的软珊瑚。'
      },
      di: {
        title: 'DI（干扰指标）',
        good: '理想状态，几乎没有明显损害。',
        moderate: '警告水平，表明最近台风、捕鱼或锚定损害。',
        poor: '明显损害，生态系统处于脆弱或退化状态。'
      },
      pi: {
        title: 'PI（污染指标）',
        good: '理想，外部污染影响最小。',
        moderate: '中等水平，表明存在一些污染源，需要关注。',
        poor: '高污染，可能导致珊瑚白化、疾病增加和死亡率上升。'
      }
    },
    loading: {
      connecting: '连接地图服务器中...',
      loading: '加载地图数据中...',
      locating: '定位岛屿信息中...',
      generating: '生成交互式地图中...',
      almostReady: '即将完成...',
      success: '地图数据加载成功！',
      backup: '使用备用数据...'
    },
    comingSoon: {
      title: '交互式地图',
      subtitle: '即将推出',
      description: '我们正在开发一个令人惊叹的交互式地图功能，帮助您探索马来西亚周边的珊瑚礁位置，并实时追踪它们的健康状况。',
      featuresTitle: '即将推出的功能：',
      features: {
        locations: '📍 实时珊瑚礁位置',
        visualization: '🌊 交互式海洋数据可视化',
        health: '📊 健康状态指示器',
        info: '🔍 详细位置信息',
        mobile: '📱 移动端友好导航'
      },
      backButton: '← 返回上一页'
    },
    game: {
      title: '🎮 珊瑚冒险游戏',
      unavailable: '游戏组件已移除。此功能当前不可用。',
      switchBack: '您可以使用上方的切换按钮返回交互式地图视图。'
    }
  },
  
  // 教育页面
  education: {
    title: '教育与负责任的旅游中心',
    subtitle: '了解珊瑚礁，成为负责任的旅行者',
    gameButton: '🎮 想试试游戏吗？',
    tipsGuidesButton: '📋 贴士和指南',
    dropdown: {
      tourismHub: '旅游中心',
      tourismChecklist: '旅行清单'
    },
    sections: {
      videos: '视频学习',
      quiz: '知识测验',
      resources: '学习资源',
      numbers: '珊瑚礁数据'
    },
    quiz: {
      button: '🎯 开始测验',
      description: '测试您的珊瑚礁知识'
    },
    videoCategories: {
      basics: '了解珊瑚礁',
      threats: '威胁与保护',
      protection: '如何保护'
    },
    numbers: {
      mainTypes: {
        title: '珊瑚礁的主要类型',
        description: '岸礁紧贴海岸线，堡礁位于泻湖后面，环礁在开阔海洋中形成环形。了解它们的形成有助于我们理解为什么某些珊瑚礁在特定地方繁茂生长。',
        learnMore: '了解更多'
      },
      tourismValue: {
        title: '刁曼岛年旅游价值（马币）',
        description: '健康的珊瑚礁吸引潜水员，保护海岸，支持渔业——推动马来西亚的旅游经济。',
        learnMore: '了解更多'
      },
      marineSpecies: {
        title: '海洋物种依赖珊瑚礁',
        description: '珊瑚礁覆盖不到1%的海洋，却支持25%的海洋生物，保护海岸，喂养社区，并具有潜在的医疗价值。',
        learnMore: '了解更多'
      },
      bombFishing: {
        title: '炸弹捕鱼事件',
        description: 'AI帮助保护珊瑚礁——从追踪鱼类"歌声"信号恢复，到在广阔水域检测非法爆炸捕鱼。',
        learnMore: '了解更多'
      },
      fishSpecies: {
        title: '单个珊瑚礁上的鱼类物种',
        description: '像水下城市一样，珊瑚礁充满生机——从微小的珊瑚鱼到顶级捕食者——使它们成为高生产力的生态系统。',
        learnMore: '了解更多'
      },
      coralsOutplanted: {
        title: '大堡礁上移植的珊瑚',
        description: '珊瑚培育计划在苗圃中培育片段并恢复珊瑚礁——科学与旅游携手合作。',
        learnMore: '了解更多'
      }
    },
    verification: {
      title: '珊瑚礁健康验证',
      description: '请识别以下6张图片中的珊瑚礁是否健康',
      close: '×'
    },
    videos: {
      important: '为什么珊瑚礁如此重要',
      dieOff: '如果所有珊瑚礁都死了会怎样',
      dying: '珊瑚礁正在死亡。我们如何拯救它们',
      bleaching: '珊瑚白化解释：珊瑚弗兰克的故事'
    }
  },
  
  // AI工具页面
  aiTools: {
    title: 'AI珊瑚分类',
    subtitle: '使用我们先进的AI模型上传珊瑚图片',
    disclaimer: {
      warning: '⚠️ 此功能仅用于珊瑚健康状态识别。请不要上传无关图片。',
      accuracy: '📋 AI结果可能不准确，仅供参考。'
    },
    upload: {
      title: '上传珊瑚图片',
      description: '将您的珊瑚图片拖放到这里，或点击浏览',
      formats: ['JPG', 'PNG', 'JPEG'],
      fileSizeLimit: '最大文件大小：3MB',
      changeImage: '🔄 更换图片',
      remove: '❌ 删除',
      classify: '分类珊瑚',
      classifying: '分类中...'
    },
    results: {
      title: '分类结果',
      species: 'AI识别的珊瑚物种',
      classifyAnother: '🔄 分类另一张图片'
    },
    error: {
      title: '分类失败',
      close: '×'
    }
  },
  
  // 页脚
  footer: {
    copyright: '© 2025 CoralKita',
    contact: '联系我们'
  },

  // 岛屿详情页面
  islandDetail: {
    description: '描述',
    travelInfo: '旅行信息',
    arrive: '到达',
    reminder: '提醒',
    snorkelingDiving: '浮潜与潜水',
    accommodation: '住宿',
    hotelRecommendation: '酒店/度假村推荐',
    tourActivities: '旅游活动与冒险',
    exploreDiving: '探索潜水',
    marineParkSnorkeling: '海洋公园浮潜',
    exploreSnorkeling: '探索浮潜',
    chooseDiving: '选择潜水',
    chooseSnorkeling: '选择浮潜',
    loading: '加载岛屿信息中...',
    pSinga: {
      title: 'Pulau Singa, 兰卡威',
      description: 'Pulau Singa是吉打州兰卡威群岛的一部分，是兰卡威海洋公园内的一座小岛。以其清澈的海水、浅水珊瑚礁和宁静的沙滩而闻名，通常包含在从兰卡威出发的跳岛游行程中。该岛无人居住，没有住宿或永久设施，主要是一个一日游和浮潜站点。',
      arrive: '从兰卡威乘快艇到达Pulau Singa（从瓜镇码头或珍南海滩20-40分钟），通常与Dayang Bunting和Beras Basah一起包含在跳岛游中。',
      reminder: '没有码头，因此登陆取决于天气条件；一些行程可能只是浮潜。',
      snorkeling: '浅水珊瑚花园适合初学者和家庭，有色彩缤纷的鱼类（小丑鱼、雀鲷、隆头鱼）和偶尔的海龟。潜水有限，但可通过兰卡威海洋公园套餐获得。',
      accommodation: 'Pulau Singa没有住宿；游客住在兰卡威，那里有酒店和生态旅馆。运营商安排一日游。',
      tips: '小贴士',
      tipsContent: '最佳访问时间是旱季（11月至4月），此时海面更平静，能见度更好。',
      tipsContent2: '在较潮湿的月份（5月至10月），海况可能更汹涌。',
      conservation: '环保实践',
      conservationContent: '鼓励游客使用珊瑚礁安全防晒霜，避免触摸或践踏珊瑚和海草床，并将所有垃圾带回兰卡威，因为该岛没有废物处理设施。应避免喂鱼或干扰海洋生物，最好与遵循兰卡威海洋公园指南的持牌运营商安排旅游，以确保珊瑚礁为未来游客保持健康。',
      dataiResort: 'The Datai兰卡威',
      dataiDesc: '拥有令人惊叹的安达曼海景的豪华雨林度假村',
      dannaResort: 'The Danna兰卡威',
      dannaDesc: '拥有私人海滩通道的殖民风格豪华度假村',
      frangipaniResort: 'The Frangipani兰卡威',
      frangipaniDesc: '屡获殊荣的环保海滨度假村',
      ambongResort: 'Ambong Ambong',
      ambongDesc: '拥有私人泳池和海景的可持续豪华别墅',
      booking: '预订',
      operatorsTours: '运营商与旅游',
      operatorsDesc: '从珍南海滩和瓜镇码头出发的跳岛游（多个当地运营商）',
      operatorsDesc2: '提供持牌兰卡威海洋公园导游的定制生态旅游。',
      padiDiving: 'PADI兰卡威水肺潜水中心',
      padiDesc1: '兰卡威海洋公园周围的船只潜水',
      padiDesc2: '初学者友好和认证选项',
      checkTour: '查看旅游',
      unescoJetSki: 'UNESCO地质公园水上摩托艇-岛屿',
      jetSkiDesc1: '从珍南海滩出发的跳岛游',
      jetSkiDesc2: '包含导游和安全简报'
    },
    mertang: {
      title: 'Pulau Mertang, 柔佛',
      description: 'Pulau Mertang位于马来西亚柔佛州，由一组非常小的离岸小岛（Mertang Barat / Mertang Timur）组成，位于柔佛东海岸靠近丰盛港的水域。地理记录将它们分类为"小岛"（小岛屿或沙洲），通常缺乏住宿。',
      arrive: '您可以选择从丰盛港/恩道地区的码头出发。请与当地船运公司或度假村管理确认他们是否提供Mertang跳岛游/浮潜站点的停靠服务。建议将Mertang与更常访问的邻近岛屿群（如Rawa、Sembilang、Sri Buat、Dayang等）结合，以增强行程可行性和路线灵活性。地理和目的地概览表明，丰盛港地区是访问这些离岸岛屿的主要出发地。',
      reminder: 'Mertang是一个非常小的岛屿，没有公开可用的永久登陆设施或定期渡轮服务。必须与船运公司确认当天的海况，是否浮潜将是唯一活动而不进行岛屿登陆，以及其他细节（以避免践踏/干扰鸟类或爬行动物栖息地）。',
      snorkeling: '如果您选择在Pulau Mertang地区进行沿海浮潜活动，请遵守"不触摸、不践踏、不收集"的原则。',
      accommodation: '丰盛港/柔佛',
      accommodationDesc: '由于Mertang主要由无人居住的小岛组成，住宿通常安排在丰盛港或邻近岛屿如Rawa。从那里，游客进行一日游、跳岛游或包租共享船只探索Mertang水域。Rawa提供全面的目的地指南和度假村产品信息，与船运公司合作促进定制行程。',
      orcaDiving: 'Orca Nation潜水',
      orcaDesc: '与认证导游一起发现水下世界和原始珊瑚礁。',
      rawaResort: 'Rawa岛度假村',
      rawaDesc: '拥有原始海滩和珊瑚礁的理想热带岛屿度假村',
      orcaResort: 'Orca Nation度假村',
      orcaResortDesc: '拥有水晶般清澈海水和白色沙滩的私人岛屿度假胜地',
      islandHopping: '跳岛游',
      book: '预订',
      tips: '小贴士',
      tipsContent: '在季风季节（大约11月至2月），海况不稳定，行程经常调整或取消。',
      conservation: '环保实践',
      conservationContent: '使用珊瑚礁安全防晒霜，避免在浮潜时触摸海底或抓取物体，不要喂鱼，不要践踏沙洲上的筑巢区域。Rawa位于苏丹依斯干达海洋公园内，钓鱼和机动水上活动受到限制。'
    },
    sipadan: {
      title: 'Pulau Sipadan, 沙巴',
      description: 'Pulau Sipadan位于沙巴东海岸，是马来西亚唯一真正的海洋岛屿，由一座从海底600米处升起的死火山形成。被全世界公认为顶级潜水目的地之一，Sipadan拥有非凡的海洋生物多样性，包括海龟、梭鱼、礁鲨和充满活力的珊瑚墙。该岛是受保护的海洋公园，不允许过夜住宿。',
      arrive: '前往Sipadan需要通过仙本那镇。游客通常从吉隆坡或哥打京那巴鲁等主要城市飞往沙巴的斗湖机场。从斗湖到仙本那约需1小时车程，然后从仙本那码头乘快艇45-60分钟到达Sipadan，或前往附近的岛屿如Mabul或Kapalai，那里的度假村和潜水运营商安排到Sipadan的一日游。',
      reminder: 'Sipadan是受保护的海洋公园，没有过夜住宿或公共设施。每天仅限120个许可证，通过持牌潜水运营商获得，因此游客应提前确认许可证、船只时间表和海况。',
      snorkeling: 'Sipadan被全球公认为顶级潜水目的地。该岛的陡峭落差和周围珊瑚礁提供了一些世界上最壮观的水下体验。',
      iconicSites: '标志性潜水点：梭鱼点、海龟洞穴和落差。',
      marineLife: '海洋生物：大群杰克鱼、旋转梭鱼、隆头鹦嘴鱼、礁鲨和数十只绿海龟和玳瑁。高级潜水员在特定季节也可能遇到锤头鲨和鲸鲨。',
      snorkelingNote: '在浅水区可以进行有限的浮潜，但由于深度、水流和受保护的许可制度，该岛主要适合有经验的潜水员。',
      accommodation: 'Sipadan没有住宿。大多数游客住在附近的岛屿，如Mabul或Kapalai，这些地方提供度假村、水上小屋和包括Sipadan许可证在内的完整潜水套餐。',
      kapalaiResort: 'Sipadan Kapalai潜水度假村',
      kapalaiDesc: '建在支柱上的水上村庄度假村，拥有世界级的Sipadan潜水通道',
      mabulResort: 'Mabul海滩度假村',
      mabulDesc: '环保海滨度假村，拥有获奖的PADI潜水中心',
      seaventuresResort: 'Seaventures潜水平台',
      seaventuresDesc: '独特的石油平台改建为豪华潜水度假村，直接通往Sipadan',
      booking: '预订',
      tips: '小贴士',
      tipsContent: '最佳访问时间是4月至12月，此时海面平静，能见度为20-40米。1月至3月海面可能更汹涌，但游客较少。',
      tipsContent2: '提前预订，因为许可证每天限制120个，通常与度假村或潜水套餐绑定。携带潜水认证卡；Sipadan不适合初学者。',
      tipsContent3: '岛上没有设施；住宿、餐饮和潜水后勤安排从Mabul、Kapalai或仙本那进行。携带珊瑚礁安全防晒霜和其他必需品。',
      conservation: '环保实践',
      conservationContent: 'Sipadan是严格保护的海洋公园。游客应只与持牌运营商潜水，避免触摸或干扰海洋生物，并保持适当的浮力以保护脆弱的珊瑚。',
      conservationContent2: '禁止钓鱼、机动水上活动和过夜住宿。选择生态认证的潜水度假村有助于确保岛屿为后代保持原始状态。'
    },
    pulauLima: {
      title: 'Pulau Lima, 登嘉楼',
      description: 'Pulau Lima由苏丹依斯干达海洋公园内的一对小岛（Lima Besar / Lima Kecil）组成。由柔佛公园管理，海洋公园包括丰盛港海岸外的多个岛屿，进行保护和教育活动。',
      arrive: '您可以首先前往柔佛的丰盛港（海上旅程的集结地）在丰盛港中心（丰盛港渡轮码头服务中心）完成手续。在这里，您将确认渡轮时间表/集合点并支付海洋公园/保护费用（岛屿进入和海洋公园区域进入都收费）。或者，您可以选择乘坐快艇/包船或参加从丰盛港码头到Pulau Lima地区的有组织跳岛游。作为无人居住的岛屿，Pulau Lima通常缺乏定期的公共渡轮服务。',
      reminder: '常见方法包括从丰盛港包船、参加跳岛游，或通过Sibu地区的当地潜水中心和度假村安排访问。',
      accommodation: '丰盛港/柔佛',
      accommodationDesc: '由于Pulau Lima主要由无人居住的小岛组成，住宿通常安排在丰盛港或邻近岛屿如Sibu。从那里，游客进行一日游、跳岛游或包租共享船只探索Lima水域。Sibu提供全面的目的地指南和度假村产品信息，与船运公司合作促进定制行程。',
      seaGypsyResort: 'Sea Gypsy Village Resort',
      seaGypsyDesc: '拥有专业浮潜导游和海洋公园通道的环保度假村',
      rimbaResort: 'Rimba Resort',
      rimbaDesc: '拥有潜水学校和原始海滨住宿的天然海湾度假村',
      myVillaRecreation: 'MYVilla Recreation',
      myVillaDesc: '拥有钓鱼、潜水和丛林徒步活动的环保民宿',
      islandHopping: '跳岛游',
      islandHoppingDesc: '通过导游带领的旅游和浮潜冒险探索多个岛屿',
      marineParkTours: '海洋公园旅游',
      marineParkToursDesc: '与认证导游一起进行苏丹依斯干达海洋公园的专业导游旅游',
      book: '预订',
      snorkeling: 'Lima Besar和Lima Kecil浮潜点：Sibu地区的当地潜水中心和度假村明确将Lima Besar和Lima Kecil列为浮潜路线和沿海体验点，适合初学者和家庭（取决于能见度和海况）。',
      marinePark: '海洋公园浮潜',
      marineParkDesc: '与认证导游一起探索苏丹依斯干达海洋公园的原始珊瑚礁和多样化的海洋生物。',
      tips: '小贴士',
      tipsContent: '丰盛港地区的潜水条件和能见度（3月至9月最佳；11月至2月的东北季风季节期间，多个运营商暂停或减少服务）。'
    },
    seriBuat: {
      title: 'Pulau Seri Buat, 彭亨',
      description: 'Pulau Seri Buat，也被称为Pulau Rahsia，是彭亨州在苏丹依斯干达海洋公园内保存最好的秘密之一。以其石灰岩悬崖、水晶般清澈的海水和僻静的海滩而闻名，Seri Buat是一个宁静的停靠点，通常与附近的岛屿如Pulau Rawa和Pulau Sembilang结合。由于与泰国甲米海岸线的相似性，常被称为"马来西亚的甲米"，该岛提供宁静和冒险，是生态旅行者和周末探索者的完美度假胜地。',
      arrive: '前往Pulau Seri Buat需要从丰盛港码头（柔佛）或丹绒格莫码头（彭亨）乘船，根据出发地点的不同，行程时间约为30-45分钟。游客通常包船或参加跳岛游，这些行程包括Seri Buat以及Mersing群中的其他岛屿，如Pulau Rawa、Pulau Sembilang和Pulau Harimau。',
      reminder: 'Seri Buat没有公共渡轮服务、定期航线或永久码头。行程取决于海况，在某些情况下，访问可能仅限于浮潜或离岸停靠。',
      snorkeling: 'Seri Buat的绿松石海水非常适合浮潜，有色彩缤纷的珊瑚鱼和良好的能见度。虽然附近岛屿的潜水机会更发达，但Seri Buat是以下活动的热门停靠点：',
      snorkelingList: [
        '在受保护的海湾中浮潜',
        '在戏剧性悬崖下的翡翠海水中游泳',
        '在指定的安全地点从悬崖跳入海中'
      ],
      hikingAdventure: '徒步与冒险',
      hikingContent: '短暂的10-15分钟徒步旅行可到达一个风景优美的观景点，可俯瞰Seri Buat和周围水域的全景。对于更多冒险，游客可以探索岩石露头或参加跳岛游，包括附近的Pulau Harimau，以其隐藏的天然泳池和受欢迎的悬崖跳点而闻名。',
      recommendedResorts: '推荐度假村与运营商',
      rawaResort: 'Rawa岛度假村',
      rawaDesc: '拥有原始海滩和水上活动的豪华岛屿度假村',
      stepFootprint: '1Step1Footprint',
      stepFootprintDesc: '专业的跳岛游，包含徒步和浮潜',
      amiTravel: 'AMI Travel',
      amiTravelDesc: '8岛一日游套餐，包含全面活动',
      explore: '探索',
      tips: '小贴士',
      tipsContent: '最佳访问时间是3月至10月，此时海面平静。',
      tipsContent2: '避免11月至2月（季风季节），此时海况可能很汹涌。',
      tipsContent3: '携带珊瑚礁安全防晒霜、水和零食，因为岛上没有设施。',
      tipsContent4: '提前预订跳岛游以确保您的名额。',
      tipsContent5: '不要错过到观景点的短途徒步，那里有令人叹为观止的风景。',
      tipsContent6: '在浮潜或悬崖跳时保持谨慎，始终遵循运营商指导。',
      conservation: '环保实践',
      conservationContent: '作为苏丹依斯干达海洋公园的一部分，Seri Buat受到保护。游客应避免触摸或践踏珊瑚，不要喂鱼，并将所有垃圾带回大陆。使用珊瑚礁安全防晒霜保护海洋生物，尊重沙洲或岩石露头上的筑巢区域。选择持牌的生态友好运营商有助于为未来游客保护岛屿的自然美景。',
      accommodation: '岛上没有住宿。游客通常住在丰盛港或附近的海滨城镇，并参加一日游到该岛。'
    },
    actionButtons: {
      reefPolicies: '了解珊瑚礁政策',
      reefConservation: '学习珊瑚礁保护'
    }
  },

  // 测验页面
  quiz: {
    title: '珊瑚礁知识测验',
    startButton: '开始测验',
    nextButton: '下一题',
    submitButton: '提交测验',
    restartButton: '重新开始',
    question: '问题',
    of: '/',
    score: '得分',
    correct: '正确！',
    incorrect: '错误',
    completed: '测验完成！',
    yourScore: '您的得分',
    feedback: {
      excellent: '太棒了！您是珊瑚礁专家！',
      good: '很好！您对珊瑚礁了解很多。',
      average: '不错！继续学习珊瑚礁知识。',
      needImprovement: '继续学习！访问教育中心了解更多。'
    }
  },
  
  
  // 清单页面
  checklist: {
    title: '旅行清单',
    subtitle: '为您的珊瑚礁探险做好准备',
    categories: {
      equipment: '潜水装备',
      safety: '安全物品',
      documentation: '证件',
      personal: '个人物品'
    },
    markComplete: '标记为完成',
    addItem: '添加项目'
  },
  
  // 通用
  common: {
    loading: '加载中...',
    error: '错误',
    success: '成功',
    save: '保存',
    cancel: '取消',
    confirm: '确认',
    delete: '删除',
    edit: '编辑',
    close: '关闭',
    search: '搜索',
    filter: '筛选',
    sort: '排序',
    viewDetails: '查看详情',
    learnMore: '了解更多',
    backToHome: '返回首页'
  },
  
  // 聊天小部件
  chat: {
    title: '珊瑚助手',
    placeholder: '询问我任何关于珊瑚礁的问题...',
    send: '发送',
    minimize: '最小化',
    close: '关闭'
  },
  
  // AI工具页面
  aiTools: {
    title: 'AI珊瑚分类',
    subtitle: '上传珊瑚图片，获得即时物种识别',
    disclaimer: {
      warning: '此工具仅用于教育目的',
      accuracy: '结果可能有所不同，不应替代专业识别'
    },
    upload: {
      title: '上传您的珊瑚图片',
      description: '选择一张清晰、光线良好的珊瑚图片以获得最佳结果',
      formats: ['JPG', 'PNG', 'JPEG', 'WEBP'],
      fileSizeLimit: '最大文件大小：10MB',
      changeImage: '更换图片',
      remove: '删除',
      classify: '分类珊瑚',
      classifying: '分析中...'
    },
    results: {
      title: '分类结果',
      species: '珊瑚物种',
      classifyAnother: '分类另一个'
    },
    error: {
      title: '分类错误',
      close: '关闭'
    },
    howToUse: {
      title: '使用方法',
      step1: {
        title: '上传图片',
        description: '从您的设备中选择一张清晰的珊瑚图片'
      },
      step2: {
        title: 'AI分析',
        description: '我们的AI模型分析珊瑚的特征和模式'
      },
      step3: {
        title: '获取结果',
        description: '获得即时的物种识别和信息'
      }
    }
  },
  
  // 常见问题页面
  faq: {
    title: '常见问题',
    subtitle: '查找关于 CoralKita 的常见问题解答',
    generalInfo: {
      title: '一般信息',
      q1: {
        question: 'Q: 这个网站是关于什么的？',
        answer: 'A: 该平台提供交互式工具、数据可视化和AI聊天机器人，帮助用户了解马来西亚珊瑚礁的健康状况。它旨在提高人们对珊瑚白化的认识，促进负责任的旅游，并使科学礁石数据更易于访问。'
      },
      q2: {
        question: 'Q: 这个平台适合谁使用？',
        answer: 'A: 它专为对珊瑚礁健康和可持续旅行感兴趣的游客、学生、研究人员、保护主义者、非政府组织和政策制定者设计。'
      }
    },
    dataAndSources: {
      title: '数据与来源',
      q1: {
        question: 'Q: 数据来自哪里？',
        answer: 'A: 数据主要来源于年度珊瑚礁调查报告和Reef Check Malaysia的原始元数据。链接：<a href="https://reefcheck.org.my/interactive-map/" target="_blank" style="color: #4facfe; text-decoration: underline;">https://reefcheck.org.my/interactive-map/</a>'
      },
      q2: {
        question: 'Q: 数据多久更新一次？',
        answer: 'A: 珊瑚礁调查报告每年发布一次，因此数据每年更新一次。'
      },
      q3: {
        question: 'Q: 该网站是否隶属于任何政府机构？',
        answer: 'A: 不，CoralKita 是一个独立网站。所有获得的信息都会注明出处以确保透明度。'
      }
    },
    disclaimer: {
      title: '免责声明与限制',
      q1: {
        question: 'Q: 信息是否100%准确？',
        answer: 'A: 虽然数据来自可信来源，但由于环境变化或调查数据不完整，准确性可能会有所不同。该平台不能替代专业的环境评估。'
      },
      q2: {
        question: 'Q: 谁对聊天机器人的回复负责？',
        answer: 'A: 回复是基于官方报告和预训练模型自动生成的。它们仅供教育目的使用，不应作为专业建议。'
      }
    }
  },
  
  // 旅行清单
  travelChecklist: {
    title: '负责任的旅行清单',
    subtitle: '完成可持续旅游的关键步骤',
    back: '返回',
    conservationAction: 'CoralKita 保护行动',
    questionnaire: {
      beforeWeStart: '开始之前...',
      description: '回答几个问题来获取您的个性化清单',
      progress: '第 {current} 题，共 {total} 题',
      previous: '上一题',
      next: '下一题',
      showChecklist: '显示我的清单'
    },
    questions: {
      firstTime: '这是您第一次浮潜/潜水吗？',
      travellingWithKids: '您带孩子旅行吗？',
      reefCondition: '您计划访问的岛屿的珊瑚礁状况：',
      swimmingLevel: '游泳技能水平：'
    },
    options: {
      yes: '是',
      no: '否',
      good: '良好',
      fair: '一般',
      poor: '较差',
      unknown: '未知',
      beginner: '初学者',
      intermediate: '中级',
      advanced: '高级'
    },
    categories: {
      experience: {
        title: '基于您的经验',
        firstTime: '新手提示',
        experienced: '经验指导',
        description: '基于您的潜水/浮潜经验的基本准备'
      },
      traveling: {
        titleWithKids: '带孩子旅行',
        titleSolo: '独自/成人旅行',
        familyTips: '家庭友好提示',
        independent: '独立旅行',
        descriptionWithKids: '带孩子旅行时的特殊考虑',
        descriptionSolo: '独立旅行者的提示'
      },
      reefCondition: {
        title: '基于珊瑚礁状况',
        goodCondition: '良好状况',
        fairCondition: '一般状况',
        poorCondition: '较差状况',
        unknownCondition: '未知状况',
        description: '针对您目的地珊瑚礁健康状况的具体指导'
      },
      swimmingSkills: {
        title: '基于您的游泳技能',
        beginnerLevel: '初学者水平',
        intermediateLevel: '中级水平',
        advancedLevel: '高级水平',
        description: '针对您技能水平的安全和活动建议'
      }
    },
    checklist: {
      question1: {
        yes: {
          item1: '在进入水中之前参加基本的浮潜/潜水安全简报。',
          item2: '在浅水或游泳池中练习使用浮潜装备。',
          item3: '阅读珊瑚礁安全行为（例如，永远不要触摸或站在珊瑚上）。',
          item4: '使用珊瑚礁安全防晒霜（锌基，无毒）。',
          item5: '携带防疹衣或长袖泳衣以保护免受阳光照射。',
          item6: '在旅行前检查当地天气和潮汐时间。',
          item7: '选择促进珊瑚礁保护的认证生态旅游运营商。'
        },
        no: {
          item1: '更新您对珊瑚礁保护规则的了解——即使是经验丰富的潜水员也可能忘记。',
          item2: '如果您自带装备，请检查并清洁您的装备，以避免在珊瑚礁之间携带入侵物种。',
          item3: '鼓励您团队中的新手遵循保护指导原则。',
          item4: '在潜水前查看任何新的当地法规或珊瑚礁健康更新。'
        }
      },
      question2: {
        yes: {
          item1: '选择有平静、浅水的家庭友好海滩或浮潜点。',
          item2: '携带适合儿童的浮潜装备并在旅行前测试。',
          item3: '为儿童准备防紫外线泳衣和珊瑚礁安全防晒霜。',
          item4: '早期教育孩子："看，不要摸"——解释为什么必须保护珊瑚和海洋生物。',
          item5: '始终让孩子靠近岸边或在伸手可及的范围内。',
          item6: '在早晨安排水上活动，此时海面更平静，人群更少。',
          item7: '参加有儿童安全学习活动的引导生态旅游（例如，珊瑚礁漫步，海洋中心参观）。',
          item8: '携带充足的饮用水、零食和休息时间——疲惫的孩子是不安全的游泳者。',
          item9: '鼓励孩子收集回忆，而不是纪念品——不要贝壳或珊瑚片。'
        },
        no: {
          item1: '如果您在水中自信，选择更长或更高级的浮潜/潜水课程。',
          item2: '支持为家庭提供海洋教育的运营商，即使您独自旅行——您的贡献有助于资助外展活动。',
          item3: '利用您的灵活性访问不太拥挤的珊瑚礁，减少对热门家庭景点的压力。'
        }
      },
      question3: {
        good: {
          item1: '帮助维护珊瑚礁状况——避免触摸或追逐海洋生物。',
          item2: '在浮潜/潜水时参与公民科学项目（例如，珊瑚礁健康报告）。',
          item3: '支持当地环保运营商和保护项目。',
          item4: '不留垃圾，将所有垃圾带回去。'
        },
        fair: {
          item1: '避免与珊瑚礁的任何身体接触——它很脆弱，正在努力恢复。',
          item2: '支持或捐赠给当地珊瑚礁恢复项目。',
          item3: '选择非侵入性活动（浮潜而不是锚定船只或走在珊瑚礁上）。',
          item4: '负责任地分享照片和意识——突出保护需求。'
        },
        poor: {
          item1: '避免与珊瑚礁的任何身体接触——它很脆弱，正在努力恢复。',
          item2: '支持或捐赠给当地珊瑚礁恢复项目。',
          item3: '选择非侵入性活动（浮潜而不是锚定船只或走在珊瑚礁上）。',
          item4: '负责任地分享照片和意识——突出保护需求。'
        },
        unknown: {
          item1: '在访问前在我们的交互地图上研究珊瑚礁状况的最新报告。',
          item2: '联系当地潜水中心或海洋公园获取更新。',
          item3: '学习如何识别珊瑚白化并向当地当局报告目击情况。',
          item4: '教育自己了解所有珊瑚礁状况的最佳环境实践。'
        }
      },
      question4: {
        beginner: {
          item1: '浮潜时使用救生衣或浮力装置。',
          item2: '靠近岸边或与认证导游一起。',
          item3: '避免强流和深水。',
          item4: '在旅行前练习基本的水上安全和呼吸技巧。',
          item5: '为了安全，总是与伙伴或导游一起。',
          item6: '不要追逐或过于接近海洋生物。'
        },
        intermediate: {
          item1: '在指定的浮潜/潜水区域内。',
          item2: '注意水流变化和天气条件。',
          item3: '如果需要，帮助经验较少的游泳者——但不要妥协您的安全。',
          item4: '保持良好的浮力控制，避免触摸珊瑚。',
          item5: '如果潜水，携带表面标记浮标（SMB）。'
        },
        advanced: {
          item1: '彻底检查潜水装备并负责任地计划您的潜水。',
          item2: '志愿参加珊瑚礁清理或监测潜水。',
          item3: '负责任地探索——避免过度潜水同一地点以减少影响。',
          item4: '与经验较少的游客分享您的知识。',
          item5: '通过当地海洋团体支持珊瑚礁保护工作。'
        }
      }
    },
    resetButton: '重置并重新开始'
  },

  // Government & Policy Information Hub
  government: {
    title: '政府与政策信息中心',
    subtitle: '马来西亚珊瑚礁保护政策和官方倡议的可靠、最新信息',
    
    policyUpdates: {
      title: '政府与政策更新',
      subtitle: '当前政府政策和保护倡议的简化更新',
      
      updates: {
        sunscreen: {
          date: '2025年6月',
          title: '海洋公园部门强制执行珊瑚安全防晒霜指南',
          description: '马来西亚渔业部已实施新规定，要求所有海洋公园游客只能使用珊瑚安全防晒霜。此倡议旨在减少损害珊瑚礁的化学污染。',
          source: '官方来源：马来西亚渔业部'
        },
        fishing: {
          date: '2025年8月',
          title: '海洋公园管理局禁止在热浪岛珊瑚礁附近进行破坏性捕鱼',
          description: '新的执法措施禁止在热浪岛珊瑚礁2海里范围内进行任何形式的破坏性捕鱼活动。违规者将面临更严厉的处罚以保护海洋生物多样性。',
          source: '官方来源：马来西亚海洋公园'
        },
        monitoring: {
          date: '2024年',
          title: '国家珊瑚礁监测计划扩展至200多个站点',
          description: '马来西亚珊瑚礁检查组织与渔业部合作，将监测网络扩展至全国200多个珊瑚礁站点，提供珊瑚礁健康和恢复的综合数据。',
          source: '官方来源：马来西亚珊瑚礁检查组织'
        },
        restoration: {
          date: '2024年',
          title: '敦慕沙法海洋公园珊瑚恢复计划',
          description: '沙巴公园通过与志愿者和当地社区的合作，成功在敦慕沙法海洋公园安装了1000多个珊瑚礁框架并移植了12000个珊瑚片段。',
          source: '官方来源：沙巴公园'
        }
      }
    },
    
    conservation: {
      title: '支持珊瑚礁保护',
      subtitle: '经过验证的非政府组织和官方捐赠计划，提供安全链接',
      
      ngos: {
        reefCheck: {
          name: '马来西亚珊瑚礁检查组织',
          verified: '✓ 验证的非政府组织',
          description: '马来西亚珊瑚礁检查组织是一个注册的非营利组织，致力于马来西亚珊瑚礁生态系统的保护和管理。他们在全国范围内进行定期珊瑚礁健康调查、珊瑚恢复项目和社区保护计划。',
          focusAreas: '重点领域：',
          focusAreasText: '珊瑚礁监测、珊瑚恢复、海洋教育',
          coverage: '覆盖范围：',
          coverageText: '全国珊瑚礁站点，包括热浪岛、刁曼岛、停泊岛等',
          button: '了解更多并通过官方网站捐赠 →'
        },
        wwf: {
          name: '世界自然基金会马来西亚分会',
          verified: '✓ 验证的非政府组织',
          description: '世界自然基金会马来西亚分会是全球WWF网络的一部分，致力于保护马来西亚的海洋生态系统，包括珊瑚礁。他们的珊瑚礁保护工作专注于减少海洋污染、促进可持续旅游和保护关键海洋栖息地。',
          focusAreas: '重点领域：',
          focusAreasText: '海洋保护、可持续渔业、气候变化缓解',
          coverage: '覆盖范围：',
          coverageText: '马来西亚沿海和海洋保护区',
          button: '了解更多并通过官方网站捐赠 →'
        },
        tracc: {
          name: '热带研究与保护中心 (TRACC)',
          verified: '✓ 验证的非政府组织',
          description: 'TRACC在马来西亚沙巴运营珊瑚礁恢复和海洋研究项目。他们与当地社区和志愿者合作，修复受损的珊瑚礁，并提供可持续海洋资源管理培训。',
          focusAreas: '重点领域：',
          focusAreasText: '珊瑚礁恢复、海洋研究、社区培训',
          coverage: '覆盖范围：',
          coverageText: '沙巴仙本那及周边水域',
          button: '了解更多并通过官方网站捐赠 →'
        }
      }
    }
  }
}

