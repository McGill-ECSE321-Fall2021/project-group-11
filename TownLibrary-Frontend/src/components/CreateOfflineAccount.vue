<template>
  <div id="create-offline-account">
    <h1>Create Offline Account</h1>

    <div v-if="state === 0">
      <p>Please enter the user's information below</p>

      <input type="text" v-model="newOfflineMember.name" placeholder="Name">
      <br/>
      <input type="text" v-model="newOfflineMember.address" placeholder="Address">

      <table>
        <tr v-for="msg in errorMessages" :key="msg">
          <td style="color: red">{{ msg }}</td>
        </tr>
      </table>
      <br/>

      <button :disabled="0 !== errorMessages.length"
              @click="createAccount(newOfflineMember)">Create Offline Account</button>
    </div>

    <div v-if="state === 1">
      <h2>Successfully added {{ createdUser.name }}!</h2>
      <p>
        This new offline member has been assigned an user id of {{ createdUser.id }}.
        After the address has been validated,
        remember to update the status via the user management portal!</p>

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
  name: 'create-offline-account',

  data () {
    return {
      state: -1,

      newOfflineMember: {
        name: '',
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
    async createAccount (userInfo) {
      try {
        let response = await AXIOS.post('/offline-members/' + userInfo.name, null, {
          params: {
            address: userInfo.address,
            library: 0,
            initId: this.initId,
            initPass: this.initPass
          }
        })
        this.createdUser = response.data;
        this.state++;
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
      this.$router.replace('/profile')
      this.state = 0
    }
  },

  computed: {
    errorMessages () {
      let localizedErrs = []
      if ('' === this.newOfflineMember.name)
        localizedErrs.push('Name cannot be empty')
      if ('' === this.newOfflineMember.address)
        localizedErrs.push('Address cannot be empty')
      if (0 !== localizedErrs.length)
        return localizedErrs

      return decodeError(this.serverResponse)
    }
  },

  watch: {
    newOfflineMember: {
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
