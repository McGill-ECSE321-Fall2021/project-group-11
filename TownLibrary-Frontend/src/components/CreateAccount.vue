<template>
  <div id="create-account">
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

    <button v-on:click="createAccount(newOnlineMember)">Create Online Account</button>
    <br/>

    Already have an account?
    <button v-on:click="$router.push('login')">Login</button>
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
  name: 'create-account',

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
    createAccount: function (userInfo) {
      // make sure we abort early if any of the fields are empty.
      if ('' === this.newOnlineMember.username
          || '' === this.newOnlineMember.password
          || '' === this.newOnlineMember.email
          || '' === this.newOnlineMember.name
          || '' === this.newOnlineMember.address)
          return

      let params = {
        params: {
          password: this.newOnlineMember.password,
          email: this.newOnlineMember.email,
          name: this.newOnlineMember.name,
          address: this.newOnlineMember.address,
          library: 0
        }
      }

      AXIOS.post('/online-members/' + this.newOnlineMember.username, null, params)
        .then(response => {
          alert("!?" + response)
        })
        .catch(error => {
          this.serverResponse = error.response.data.split(',')
        })
    }
  },

  computed: {
    errorMessages: function () {
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
          return 'Invalid library'
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
      handler: function (val) {
        this.serverResponse = []
      }
    }
  }
}
</script>

<style>
</style>
