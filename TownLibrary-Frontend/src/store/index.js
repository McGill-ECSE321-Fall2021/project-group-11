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
    /// - userInfo: the user's info
    login (state, { userType, username, password, userInfo }) {
      // We use deconstruction to show what fields are needed, and then we
      // reconstruct the object to avoid potentially bizarre reference issues
      state.loginStatus = {
        userType,
        username,
        password,
        userInfo
      }
    },

    /// Only does something if you are logged in!
    updateUserInfo (state, userInfo) {
      if (state.loginStatus)
        state.loginStatus.userInfo = userInfo
    }
  }
})
