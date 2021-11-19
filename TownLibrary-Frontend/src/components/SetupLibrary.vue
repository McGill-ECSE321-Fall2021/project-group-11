<template>
  <div id="setup-library">
    <h1>Setup Town Library System</h1>

    <div v-if="state === 0">
      <p>Please enter the library's information below</p>

      <input type="text" v-model="newLibrary.address" placeholder="Address">

      <ul>
        <li style="color: red" v-for="msg in errorMessages">{{ msg }}</li>
      </ul>

      <button v-on:click="createLibrary(newLibrary)">Next Step</button>
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

      <button v-on:click="createHeadLibrarian(newHeadLibrarian)">Next Step</button>

    </div>
    <div v-if="state === 3">
      <h2>Setup was a success!</h2>
      <p>
        You have been assigned an librarian id of {{ createdId }}.
        You will need this, along with your password to login.<br/>

        And when you do, remember to <em>login as librarian</em></p>

      <button v-on:click="successRedirect()">Go to login page</button>
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

      createdId: -1,

      serverResponse: []
    }
  },

  created: function() {
    this.state = 0
  },

  watch: {
    state: function (val) {
      switch (val) {
      case 0:
        AXIOS.get('/libraries/0')
          .then(response => {
            // the library actually exists!
            this.state = 1
          })
          .catch(error => {
            switch (error.response.data) {
            case 'NOT-FOUND-LIBRARY':
              // that's the whole point: if the library is not there, then we
              // create it!
              break;
            default:
              this.serverResponse = error.response.data.split(',')
            }
          })
        break
      case 1:
        AXIOS.get('/libraries/0')
          .then(response => {
            // and it actually already has a head librarian assigned to it!
            if (null !== response.data.headLibrarianId)
              this.state = 2
          })
        // Ignore the error
        break
      case 2:
        // redirect to login page WITHOUT adding a new history entry.
        this.$router.replace('login')
        this.state = 0
        break
      }
    }
  },

  methods: {
    createLibrary: function (libraryInfo) {
      // disallow empty data
      if ('' === libraryInfo.address)
        return

      AXIOS.post('/libraries/0', null, { params: libraryInfo })
        .then(response => {
          // we successfully created the library
          this.state = 1
        })
        .catch(error => {
          this.serverResponse = error.response.data.split(',')
        })
    },
    createHeadLibrarian: function (headLibrarianInfo) {
      // disallow empty data
      if ('' === headLibrarianInfo.name
          || '' === headLibrarianInfo.password
          || '' === headLibrarianInfo.address)
        return

      AXIOS.post('/head-librarians/' + headLibrarianInfo.name, null, {
        params: {
          library: 0,
          address: headLibrarianInfo.address,
          password: headLibrarianInfo.password
        }
      })
      .then(response => {
        // Secret state 3!?
        this.createdId = response.data.id
        this.state = 3
      })
      .catch(error => {
        this.serverResponse = error.response.data.split(',')
      })
    },
    successRedirect: function () {
      // and let watch do it's magic
      this.state = 2
    }
  },

  computed: {
    errorMessages: function () {
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
        case 'EMPTY-ADDRESS':
          return 'Empty address'
        default:
          return 'Unknown error: ' + res;
        }
      })
    }
  }
}
</script>

<style>
</style>
