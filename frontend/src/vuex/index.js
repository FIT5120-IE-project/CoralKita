import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // Removed user authentication related state, kept application state management
    appVersion: '1.0.0'
  },
  
  mutations: {
    // Application common mutations
    SET_APP_VERSION(state, version) {
      state.appVersion = version
    }
  },
  
  actions: {
    // Application common actions
    updateAppVersion({ commit }, version) {
      commit('SET_APP_VERSION', version)
    }
  },
  
  getters: {
    // Removed authentication related getters, added common getters
    getAppVersion: state => state.appVersion,
    // For compatibility, return default values
    isAuthenticated: () => true, // Always return true, indicating no authentication required
    currentUser: () => ({ name: 'Guest User', level: 1, points: 0, experience: 0 }),
    userToken: () => null
  }
})
