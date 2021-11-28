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

    <table>
      <tr v-for="msg in errorMessages" :key="msg">
        <td style="color: #DE482B">{{ msg }}</td>
      </tr>
    </table>
    <br/>

    <button :disabled="0 !== errorMessages.length"
            @click="createAccount(newOnlineMember)">Create Online Account</button>
    <br/>
    <br>

    Already have an account?
    <button @click="$router.push('/login')">Login</button>
  </div>
</template>

<script>
import axios from 'axios'
import decodeError from '../api_errors.js'

var frontendUrl = process.env.FRONTEND_HOST + ':' + process.env.FRONTEND_PORT
var backendUrl = process.env.API_HOST + ':' + process.env.API_PORT

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

      serverResponse: null
    }
  },

  methods: {
    async createAccount (userInfo) {
        try {
          let response = await AXIOS.post('/online-members/' + userInfo.username, null, {
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
          password: userInfo.password,
          userInfo: response.data
        })
        this.$router.push('/profile')
      } catch (error) {
        this.serverResponse = error
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

      return decodeError(this.serverResponse)
    }
  },

  watch: {
    newOnlineMember: {
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
