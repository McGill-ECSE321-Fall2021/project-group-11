import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    loginStatus: null
  },

  mutations: {
    logout (state) {
      state.loginStatus = null
    },

    /// When you do commit the mutation, you need several payloads:
    /// - enum userType: 'online-member' | 'librarian' | 'head-librarian'
    /// - string username: username or id depending on member or librarian.
    /// - string password: password
    login (state, { userType, username, password }) {
      // We use deconstruction to show what fields are needed, and then we
      // reconstruct the object to avoid potentially bizarre reference issues
      state.loginStatus = {
        userType: userType,
        username: username,
        password: password
      }
    }
  }
})
