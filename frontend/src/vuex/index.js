import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // 移除了用户认证相关状态，保留应用状态管理
    appVersion: '1.0.0'
  },
  
  mutations: {
    // 应用通用mutations
    SET_APP_VERSION(state, version) {
      state.appVersion = version
    }
  },
  
  actions: {
    // 应用通用actions
    updateAppVersion({ commit }, version) {
      commit('SET_APP_VERSION', version)
    }
  },
  
  getters: {
    // 移除认证相关getters，添加通用getters
    getAppVersion: state => state.appVersion,
    // 为了保持兼容性，返回默认值
    isAuthenticated: () => true, // 始终返回true，表示无需认证
    currentUser: () => ({ name: 'Guest User', level: 1, points: 0, experience: 0 }),
    userToken: () => null
  }
})
