<template>
  <div id="login">
    <h1>Login</h1>

    <h2>{{ onlineMemberMode ? 'Online Member' : 'Librarian' }}</h2>
    <p>Please enter your login informtion below</p>

    <input type="text" v-model="username" v-bind:placeholder="onlineMemberMode ? 'Username' : 'ID'">
    <br/>
    <input type="password" v-model="password" placeholder="Password">
    <br/>

    <table>
      <tr v-for="msg in errorMessages">
        <td style="color: red">{{ msg }}</td>
      </tr>
    </table>
    <br/>

    <button v-bind:disabled="0 !== errorMessages.length"
            v-on:click="onlineMemberMode ? authOnlineMember(username, password) : authLibrarian(username, password)">Login</button>

    <br/>

    <div v-if="onlineMemberMode">
      Not an online member?
      <button v-on:click="onlineMemberMode = false">Login as librarian</button>
      <br/>

      Don't have an account yet?
      <button v-on:click="$router.push('/newacc')">Create an online account</button>
    </div>
    <div v-if="!onlineMemberMode">
      Not a librarian?
      <button v-on:click="onlineMemberMode = true">Login as online member</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

var frontendUrl = 'http://' + process.env.FRONTEND_HOST + ':' + process.env.FRONTEND_PORT
var backendUrl = 'http://' + process.env.API_HOST + ':' + process.env.API_PORT

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

      serverResponse: []
    }
  },

  methods: {
    async authOnlineMember (username, password) {
      try {
        // AXIOS post is weird: the request params go on the 3rd slot...
        await AXIOS.post('/auth/online-members/' + username, null, {
          params: {
            password: password
          }
        })

        // log the user in by storing the user's information
        this.$store.commit('login', {
          userType: 'online-member',
          username: username,
          password: password
        })
        // and we're ready to jump
        this.$router.push('/profile')
      } catch (error) {
        this.serverResponse = error.response.data.split(',')
      }
    },

    async authLibrarian (username, password) {
      try {
        // a trick to use is try logging in as generic librarian, and if that
        // works, check if the id refers to a head-librarian.
        await AXIOS.post('/auth/librarians/' + username, null, {
          params: {
            password: password
          }
        })

        try {
          await AXIOS.get('/head-librarians/' + username)

          // it's a head-librarian
          this.$store.commit('login', {
            userType: 'head-librarian',
            username: username,
            password: password
          })
        } catch (error) {
          // we know person must be at least a generic librarian
          this.$store.commit('login', {
            userType: 'librarian',
            username: username,
            password: password
          })
        }

        // and we're ready to jump
        this.$router.push('/profile')
      } catch (error) {
        this.serverResponse = error.response.data.split(',')
      }
    }
  },

  computed: {
    errorMessages () {
      let localizedErrs = []
      if ('' === this.username)
        localizedErrs.push((this.onlineMemberMode ? 'Username' : 'ID') + ' cannot be empty')
      if ('' === this.password)
        localizedErrs.push('Password cannot be empty')
      if (0 !== localizedErrs.length)
        return localizedErrs

      return this.serverResponse.map(res => {
        switch (res) {
        case 'BAD-AUTH-ONLINE-MEMBER':
          return 'Incorrect username or password'
        case 'BAD-ACCESS':
          return 'Incorrect id or password'
        default:
          return res
        }
      })
    }
  },

  watch: {
    username (val) {
      this.serverResponse = []
    },
    password (val) {
      this.serverResponse = []
    }
  }
}
</script>

<style>
</style>
