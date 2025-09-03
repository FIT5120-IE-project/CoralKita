import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: null,
    token: null,
    isAuthenticated: false
  },
  
  mutations: {
    SET_USER(state, user) {
      state.user = user
      state.isAuthenticated = !!user
    },
    
    SET_TOKEN(state, token) {
      state.token = token
    },
    
    CLEAR_USER(state) {
      state.user = null
      state.token = null
      state.isAuthenticated = false
    },
    
    UPDATE_USER_STATS(state, stats) {
      if (state.user) {
        state.user = { ...state.user, ...stats }
        // 同步更新本地存储
        localStorage.setItem('userInfo', JSON.stringify(state.user))
      }
    }
  },
  
  actions: {
    login({ commit }, { user, token }) {
      commit('SET_USER', user)
      commit('SET_TOKEN', token)
      
      // 保存到本地存储
      localStorage.setItem('userInfo', JSON.stringify(user))
      localStorage.setItem('token', token)
    },
    
    logout({ commit }) {
      commit('CLEAR_USER')
      
      // 清除本地存储
      localStorage.removeItem('userInfo')
      localStorage.removeItem('token')
    },
    
    // 初始化时从本地存储恢复用户状态
    initAuth({ commit }) {
      const userInfo = localStorage.getItem('userInfo')
      const token = localStorage.getItem('token')
      
      if (userInfo && token) {
        try {
          const user = JSON.parse(userInfo)
          commit('SET_USER', user)
          commit('SET_TOKEN', token)
        } catch (error) {
          console.error('Failed to parse user info:', error)
          localStorage.removeItem('userInfo')
          localStorage.removeItem('token')
        }
      }
    },
    
    // 更新用户统计信息
    updateUserStats({ commit }, stats) {
      commit('UPDATE_USER_STATS', stats)
    }
  },
  
  getters: {
    isAuthenticated: state => state.isAuthenticated,
    currentUser: state => state.user,
    userToken: state => state.token
  }
})
