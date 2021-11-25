<template>
  <div id="profile">
    <h2>Welcome back {{ userInfo.name }}!</h2>
    <button @click="doLogout">Logout</button>
    <br><br>
    <button @click="$router.push('/')">Home</button>

    <p>Just to show that stuff does work:</p>
    <ul>
      <li>User type: {{ loginStatus.userType }}</li>
      <li>Username: {{ loginStatus.username }}</li>
      <li>Password: {{ loginStatus.password }}</li>
    </ul>

    <div v-if="isLibrarian">
      <button @click="$router.push('/newacc/offline')">Create a new offline member</button>
    </div>

    <div v-if="isHeadLibrarian">
      List of Librarians:
      <button @click="reloadLibrarians()">Refresh</button>
      <table>
        <tr v-if="librarians !== null">
          <th><!-- Empty cell just for aligning the table --></th>
          <th>ID</th>
          <th>Name</th>
          <th>Address</th>
        </tr>
        <tr v-if="librarians === null">
          <td style="color: red">Failed to load from server, try again later by clicking on refresh</td>
        </tr>
        <tr v-for="librarian in (librarians || [])" :key="librarian.id">
          <td>
            <button :disabled="librarian.id === userInfo.id"
                    @click="deleteLibrarian(librarian.id)">Delete</button>
          </td>
          <td>{{ librarian.id }}</td>
          <td>{{ librarian.name }}</td>
          <td>{{ librarian.address }}</td>
        </tr>
      </table>

      <button @click="$router.push('/newacc/librarian')">Add a new librarian</button>
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
  name: 'profile',
  
  data () {
    return {
      loginStatus: {},
      userInfo: {},
      librarians: []
    }
  },

  created () {
    // Copy the login status out so that it does not cause issues when logout
    // is requested.
    Object.assign(this.loginStatus, this.$store.state.loginStatus)

    this.reloadUserInfo()
    this.reloadLibrarians()
  },

  computed: {
    isOnlineMember () {
      switch (this.loginStatus.userType) {
      default:
        return false
      case 'online-member':
        return true
      }
    },

    isLibrarian () {
      switch (this.loginStatus.userType) {
      default:
        return false
      case 'librarian':
      case 'head-librarian':
        return true
      }
    },

    isHeadLibrarian () {
      switch (this.loginStatus.userType) {
      default:
        return false
      case 'head-librarian':
        return true
      }
    }
  },

  methods: {
    doLogout () {
      this.$store.commit('logout')
      this.$router.push('login')
    },

    async reloadUserInfo () {
      try {
        let response = await AXIOS.post('/auth/' + this.loginStatus.userType + 's/' + this.loginStatus.username, null, {
          params: {
            password: this.loginStatus.password
          }
        })
        this.userInfo = response.data
      } catch (error) {
        // this is awkward because we couldn't get the user's information...
        // assume the worst (maybe the password has changed or something) and
        // logout the user.
        this.doLogout()
      }
    },

    async reloadLibrarians () {
      try {
        let response = await AXIOS.get('/librarians')
        this.librarians = response.data
      } catch (error) {
        this.librarians = null
      }
    },

    async deleteLibrarian (id) {
      // trigger the delete event
      await AXIOS.delete('/librarians/' + id, {
        params: {
          initId: this.loginStatus.username,
          initPass: this.loginStatus.password
        }
      })

      // and try reloading the list regardless of success or failure
      this.reloadLibrarians()
    }
  }
}
</script>

<style>
</style>
