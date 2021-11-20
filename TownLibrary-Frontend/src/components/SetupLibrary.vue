<template>
  <div id="setup-library">
    <h1>Setup Town Library System</h1>

    <div v-if="state === 0">
      <p>Please enter the library's information below</p>

      <input type="text" v-model="newLibrary.address" placeholder="Address">

      <ul>
        <li style="color: red" v-for="msg in errorMessages">{{ msg }}</li>
      </ul>

      <button v-bind:disabled="0 !== errorMessages.length"
              v-on:click="createLibrary(newLibrary)">Next Step</button>
    </div>
    <div v-if="state === 1">
      <p>Please enter the head-librarian information below</p>

      <input type="text" v-model="newHeadLibrarian.name" placeholder="Name">
      <br/>
      <input type="password" v-model="newHeadLibrarian.password" placeholder="Password">
      <br/>
      <input type="text" v-model="newHeadLibrarian.address" placeholder="Address">

      <ul>
        <li style="color: red" v-for="msg in errorMessages">{{ msg }}</li>
      </ul>

      <button v-bind:disabled="0 !== errorMessages.length"
              v-on:click="createHeadLibrarian(newHeadLibrarian)">Next Step</button>
    </div>
    <div v-if="state === 2">
      <h2>Setup was a success!</h2>
      <p>
        You have been assigned an librarian id of {{ createdUser.id }}.
        You will need this, along with your password to login.<br/>

        And when you do, remember to <em>login as librarian</em></p>

      <button v-on:click="successRedirect()">Done</button>
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
  name: 'setup-library',

  data () {
    return {
      state: -1,

      newLibrary: {
        address: '',
      },

      newHeadLibrarian: {
        name: '',
        password: '',
        address: ''
      },

      createdUser: {},

      serverResponse: []
    }
  },

  async created () {
    this.state = 0

    // Try to enter the correct state:
    try {
      let response = await AXIOS.get('/libraries/0')

      if (null === response.data.headLibrarianId) {
        // we enter state 1: want to assign a head librarian
        this.state = 1
        return
      }

      // reaching this point means that the head librarian has been assigneed
      // to a library: there is nothing to setup!
      //
      // in this case, swap-in the login page
      this.$router.replace('/login')
    } catch (error) {
      if ('NOT-FOUND-LIBRARY' === error.response.data)
        // we enter state 0: want to create a library
        return

      this.serverResponse = error.response.data.split(',')
    }
  },

  methods: {
    async createLibrary (libraryInfo) {
      try {
        await AXIOS.post('/libraries/0', null, { params: libraryInfo })

        // we successfully created the library
        this.state++
      } catch (error) {
        this.serverResponse = error.response.data.split(',')
      }
    },

    async createHeadLibrarian (headLibrarianInfo) {
      try {
        let response = await AXIOS.post('/head-librarians/' + headLibrarianInfo.name, null, {
          params: {
            library: 0,
            address: headLibrarianInfo.address,
            password: headLibrarianInfo.password
          }
        })

        this.createdUser = response.data
        this.state++
      } catch (error) {
        this.serverResponse = error.response.data.split(',')
      }
    },

    successRedirect () {
      // Since we have all the information, use it to automatically login the
      // head-librarian.
      this.$store.commit('login', {
        userType: 'head-librarian',
        username: this.createdUser.id,
        password: this.newHeadLibrarian.password
      })
      this.$router.push('/profile')
      this.state = 0
    }
  },

  computed: {
    errorMessages () {
      let localizedErrs = []
      switch (this.state) {
      case 0:
        if ('' === this.newLibrary.address)
          localizedErrs.push('Address cannot be empty')
        break
      case 1:
        if ('' === this.newHeadLibrarian.name)
          localizedErrs.push('Name cannot be empty')
        if ('' === this.newHeadLibrarian.password)
          localizedErrs.push('Password cannot be empty')
        if ('' === this.newHeadLibrarian.address)
          localizedErrs.push('Address cannot be empty')
        break
      }
      if (0 !== localizedErrs.length)
        return localizedErrs

      return this.serverResponse.map(res => {
        switch (res) {
        case 'DUP-LIBRARY':
          return 'Duplicate library (try reloading the page)'
        case 'DUP-HEAD-LIBRARIAN':
          return 'Duplicate head librarian'
        case 'EMPTY-NAME':
          return 'Empty name'
        case 'EMPTY-ADDRESS':
          return 'Empty address'
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
    newLibrary: {
      deep: true,
      handler (val) {
        this.serverResponse = []
      }
    },

    newHeadLibrarian: {
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
