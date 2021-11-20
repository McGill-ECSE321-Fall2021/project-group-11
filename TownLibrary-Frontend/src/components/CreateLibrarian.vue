<template>
  <div id="create-librarian">
    <h1>Create New Librarian</h1>

    <p>Please enter the librarian's information below</p>

    <input type="text" v-model="newLibrarian.name" placeholder="Name">
    <br/>
    <input type="password" v-model="newLibrarian.password" placeholder="Password">
    <br/>
    <input type="text" v-model="newLibrarian.address" placeholder="Address">

    <ul>
      <li style="color: red" v-for="msg in errorMessages">{{ msg }}</li>
    </ul>

    <button v-on:click="createLibrarian(newLibrarian)">Create New Librarian</button>
    <br/>
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
  name: 'create-librarian',

  data () {
    return {
      newLibrarian: {
        name: '',
        password: '',
        address: ''
      },

      initId: '',
      initPass: '',
      serverResponse: []
    }
  },

  created () {
    this.initId = this.$store.state.loginStatus.username
    this.initPass = this.$store.state.loginStatus.password
  },

  methods: {
    async createLibrarian (info) {
      if ('' === info.name
          || '' === info.password
          || '' === info.address)
        return

      try {
        await AXIOS.post('/librarians/' + info.name, null, {
          params: {
            password: info.password,
            address: info.address,
            library: 0,
            initId: this.initId,
            initPass: this.initPass
          }
        })

        // for now just send them back to profile.
        // TODO: probably want to, like the setup page, show something like
        // success! horray! or something...
        this.$router.push('/profile')
      } catch (error) {
        this.serverResponse = error.response.data.split(',')
      }
    }
  },

  computed: {
    errorMessages () {
      let localizedErrs = []
      if ('' === this.newLibrarian.name)
        localizedErrs.push('Name cannot be empty')
      if ('' === this.newLibrarian.password)
        localizedErrs.push('Password cannot be empty')
      if ('' === this.newLibrarian.address)
        localizedErrs.push('Address cannot be empty')
      if (0 !== localizedErrs.length)
        return localizedErrs

      return this.serverResponse.map(res => {
        switch (res) {
        case 'NULL-LIBRARY':
          // Getting a null library would mean the library does not exist yet,
          // which likely means setup has not been executed yet...
          return 'Invalid library, has setup been run yet?'
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
    newLibrarian: {
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
