<template>
  <div id="create-librarian">
    <h1>Create New Librarian</h1>

    <div v-if="state === 0">
      <p>Please enter the librarian's information below</p>

      <input type="text" v-model="newLibrarian.name" placeholder="Name">
      <br/>
      <input type="password" v-model="newLibrarian.password" placeholder="Password">
      <br/>
      <input type="text" v-model="newLibrarian.address" placeholder="Address">

      <table>
        <tr v-for="msg in errorMessages" :key="msg">
          <td style="color: red">{{ msg }}</td>
        </tr>
      </table>
      <br/>

      <button :disabled="0 !== errorMessages.length"
              @click="createLibrarian(newLibrarian)">Create New Librarian</button>
    </div>

    <div v-if="state === 1">
      <h2>Successfully added {{ createdUser.name }}!</h2>
      <p>
        This new librarian has been assigned an librarian id of {{ createdUser.id }}.
        This is needed, along with the supplied password to login.<br/>

        And when doing so, remember to <em>login as librarian</em></p>

      <button @click="successRedirect()">Return to profile</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import decodeError from '../api_errors.js'

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
      state: -1,

      newLibrarian: {
        name: '',
        password: '',
        address: ''
      },

      createdUser: {},

      initId: '',
      initPass: '',
      serverResponse: null
    }
  },

  created () {
    this.initId = this.$store.state.loginStatus.username
    this.initPass = this.$store.state.loginStatus.password

    this.state = 0
  },

  methods: {
    async createLibrarian (info) {
      try {
        let response = await AXIOS.post('/librarians/' + info.name, null, {
          params: {
            password: info.password,
            address: info.address,
            library: 0,
            initId: this.initId,
            initPass: this.initPass
          }
        })
        this.createdUser = response.data
        this.state++
      } catch (error) {
        if (error.response && error.response.data
            && 'BAD-ACCESS' === error.response.data)
          // how did we get here!? redirect to profile
          this.$router.replace('/profile')
          return

        this.serverResponse = error
      }
    },

    successRedirect () {
      this.$router.push('/profile')
      this.state = 0
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

      return decodeError(this.serverResponse)
    }
  },

  watch: {
    newLibrarian: {
      deep: true,
      handler (val) {
        this.serverResponse = null
      }
    }
  }
}
</script>

<style>
</style>
