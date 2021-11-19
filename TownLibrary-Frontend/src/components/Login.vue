<template>
  <div id="login">
    <h1>Login</h1>

    <h2>{{ onlineMemberMode ? 'Online Member' : 'Librarian' }}</h2>
    <p>Please enter your login informtion below</p>

    <input type="text" v-model="username" v-bind:placeholder="onlineMemberMode ? 'Username' : 'ID'">
    <br/>
    <input type="password" v-model="password" placeholder="Password">
    <br/>

    <p style="color: red">{{ errorMessage }}</p>

    <button v-on:click="onlineMemberMode ? authOnlineMember(username, password) : authLibrarian(username, password)">Login</button>

    <br/>

    <div v-if="onlineMemberMode">
      Not an online member?
      <button v-on:click="onlineMemberMode = false">Login as librarian</button>
      <br/>

      Don't have an account yet?
      <button v-on:click="$router.push('newacc')">Create an online account</button>
    </div>
    <div v-if="!onlineMemberMode">
      Not a librarian?
      <button v-on:click="onlineMemberMode = true">Login as online member</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
var config = require('../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
  name: 'login',

  data () {
    return {
      onlineMemberMode: true,
      username: '',
      password: '',

      serverResponse: ''
    }
  },

  methods: {
    authOnlineMember (username, password) {
      // Empty things will fail miserably, so handle them here...
      if ('' === username || '' === password)
        return

      // AXIOS post is weird: the request params go on the 3rd slot...
      AXIOS.post('/auth/online-members/' + username, null, {
        params: {
          password: password
        }
      })
      .then(response => {
        // log the user in by storing the user's information
        this.$store.commit('login', 'online-member', username, password)
        // and we're ready to jump
        this.$router.push('profile')
      })
      .catch(error => {
        this.serverResponse = error.response.data
      })
    },

    authLibrarian (username, password) {
      // Empty things will fail miserably, so handle them here...
      if ('' === username || '' === password)
        return

      // a trick to use is try logging in as generic librarian, and if that
      // works, check if the id refers to a head-librarian.
      AXIOS.post('/auth/librarians/' + username, null, {
        params: {
          password: password
        }
      })
      .then(response => {
        AXIOS.post('/head-librarians/' + username)
          .then(response => {
            // it's a head-librarian
            this.$store.commit('login', 'head-librarian', username, password)
            // and we're ready to jump
            this.$router.push('profile')
          })
          .catch(error => {
            // we know person must be at least a generic librarian
            this.$store.commit('login', 'librarian', username, password)
            // and we're ready to jump
            this.$router.push('profile')
          })
      })
      .catch(error => {
        this.serverResponse = error.response.data
      })
    }
  },

  computed: {
    errorMessage () {
      if ('' === this.username)
        return (this.onlineMemberMode ? 'Username' : 'ID') + ' cannot be empty'
      if ('' === this.password)
        return 'Password cannot be empty'

      switch (this.serverResponse) {
      case '':
        return ''
      case 'BAD-AUTH-ONLINE-MEMBER':
        return 'Incorrect username or password'
      case 'BAD-ACCESS':
        return 'Incorrect id or password'
      default:
        return 'Unknown error occurred, please try again later'
      }
    }
  },

  watch: {
    username (val) {
      this.serverResponse = ''
    },
    password (val) {
      this.serverResponse = ''
    }
  }
}
</script>

<style>
</style>
