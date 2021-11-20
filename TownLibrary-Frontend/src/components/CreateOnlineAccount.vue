<template>
  <div id="create-online-account">
    <h1>Create Online Account</h1>

    <p>Please enter your information below</p>

    <input type="text" v-model="newOnlineMember.username" placeholder="Username">
    <br/>
    <input type="password" v-model="newOnlineMember.password" placeholder="Password">
    <br/>
    <br/>
    <input type="text" v-model="newOnlineMember.name" placeholder="Name">
    <br/>
    <input type="email" v-model="newOnlineMember.email" placeholder="Email">
    <br/>
    <input type="text" v-model="newOnlineMember.address" placeholder="Address">

    <ul>
      <li style="color: red" v-for="msg in errorMessages">{{ msg }}</li>
    </ul>

    <button v-bind:disabled="0 !== errorMessages.length"
            v-on:click="createAccount(newOnlineMember)">Create Online Account</button>
    <br/>

    Already have an account?
    <button v-on:click="$router.push('/login')">Login</button>
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
  name: 'create-online-account',

  data () {
    return {
      newOnlineMember: {
        username: '',
        password: '',
        name: '',
        email: '',
        address: ''
      },

      serverResponse: []
    }
  },

  methods: {
    async createAccount (userInfo) {
        try {
          await AXIOS.post('/online-members/' + userInfo.username, null, {
            params: {
              password: userInfo.password,
              email: userInfo.email,
              name: userInfo.name,
              address: userInfo.address,
              library: 0
            }
          })

        // We already have all the info we need to login, so just do them a
        // favour by logging them in right now!
        this.$store.commit('login', {
          userType: 'online-member',
          username: userInfo.username,
          password: userInfo.password
        })
        this.$router.push('/profile')
      } catch (error) {
        this.serverResponse = error.response.data.split(',')
      }
    }
  },

  computed: {
    errorMessages () {
      let localizedErrs = []
      if ('' === this.newOnlineMember.username)
        localizedErrs.push('Username cannot be empty')
      if ('' === this.newOnlineMember.password)
        localizedErrs.push('Password cannot be empty')
      if ('' === this.newOnlineMember.name)
        localizedErrs.push('Name cannot be empty')
      if ('' === this.newOnlineMember.email)
        localizedErrs.push('Email cannot be empty')
      if ('' === this.newOnlineMember.address)
        localizedErrs.push('Address cannot be empty')
      if (0 !== localizedErrs.length)
        return localizedErrs

      return this.serverResponse.map(res => {
        switch (res) {
        case 'NULL-LIBRARY':
          // Getting a null library would mean the library does not exist yet,
          // which likely means setup has not been executed yet...
          return 'Invalid library, has setup been run yet?'
        case 'DUP-USERNAME':
          return 'Duplicate username'
        case 'DUP-EMAIL':
          return 'Duplicate email'
        case 'EMPTY-EMAIL':
        case 'BADFMT-EMAIL':
          return 'Invalid Email'
        case 'EMPTY-PASSWORD':
        case 'UNDERSIZED-PASSWORD':
        case 'OVERSIZED-PASSWORD':
          return 'Password must be 4 to 32 characters long'
        case 'BADCHAR-PASSWORD':
          return 'Password can only contain alphanumeric characters'
        default:
          return 'Unknown error: ' + res;
        }
      })
    }
  },

  watch: {
    newOnlineMember: {
      deep: true,
      handler (val) {
        this.serverResponse = []
      }
    }
  }
}
</script>

<style>
</style>
