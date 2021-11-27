<template>
  <div id="profile">
    <h2>Welcome back, {{ userInfo.name }}!</h2>
    <button @click="doLogout">Logout</button>
    <br><br>
    <!-- <p>Just to show that stuff does work:</p>
    <ul>
      <li>User type: {{ loginStatus.userType }}</li>
      <li>Username: {{ loginStatus.username }}</li>
      <li>Password: {{ loginStatus.password }}</li>
    </ul> -->

    <div v-if="isLibrarian">
      <button @click="$router.push('/newacc/offline')">Create a new offline member</button>
      <button @click="$router.push('/additem')">Add item</button>
      <br>
      <input type="text" v-model="affectedUserId">
      <button @click="getAddressOfAffectedUser">Get address</button>
      <button @click="setInTownAffectedUser(true)">Set In Town</button>
      <button @click="setInTownAffectedUser(false)">Set Out of Town</button>
      <br><br>view library
      <view-schedule :login-status="loginStatus" :entity-id="0"></view-schedule>
      <br>
      <label v-if="loginStatus.userType !== 'head-librarian'">view own schedule</label>
      <view-schedule v-if="loginStatus.userType !== 'head-librarian'" :login-status="loginStatus" :entity-id="userInfo.id" :scheduleOwner="userInfo.name"></view-schedule>

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
            <view-schedule v-if="librarian.id !== userInfo.id" :login-status="loginStatus" :entity-id="librarian.id" :schedule-owner="librarian.name"> </view-schedule>
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
    <div v-if="isOnlineMember">
      <button @click="$router.push({name: 'User Transactions',
                                    params: {
                                      id: userInfo.id
                                   }})">View transactions</button>
      <button @click="$router.push({name:'Personal Information',
                                    params:{
                                      id:       userInfo.id,
                                      fullName: userInfo.name,
                                      email:    userInfo.email,
                                      address:  userInfo.address
                                  }})">View personal information</button>
      <button @click="$router.push('')">View event schedule</button>
    </div>

  </div>
</template>

<script>
import axios from 'axios'
import ViewSchedule from './ViewSchedule.vue'

var frontendUrl =  process.env.FRONTEND_HOST + ':' + process.env.FRONTEND_PORT
var backendUrl = process.env.API_HOST + ':' + process.env.API_PORT

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
  components: { ViewSchedule },
  name: 'profile',

  component:{
    'view-schedule':ViewSchedule
  },

  data () {
    return {
      loginStatus: {},
      userInfo: {},
      librarians: [],

      affectedUserId: ''
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
    },

    async getAddressOfAffectedUser () {
      if ('' === this.affectedUserId)
        return

      try {
        let response = await AXIOS.get('/online-members/' + this.affectedUserId)
        alert('Online member has address: ' + response.data.address)
        return
      } catch (error) {
        // swallow it, try again as offline member
      }

      try {
        let response = await AXIOS.get('/offline-members/' + this.affectedUserId)
        alert('Offline member has address: ' + response.data.address)
        return
      } catch (error) {
        // swallow it.
      }

      alert('Failed to query address of member')
    },

    async setInTownAffectedUser(flag) {
      if ('' === this.affectedUserId)
        return

      let params = {
        params: {
          value: flag,
          initId: this.loginStatus.username,
          initPass: this.loginStatus.password
        }
      }
      try {
        let response = await AXIOS.put('/online-members/' + this.affectedUserId + '/in-town', null, params)
        alert('Updated for Online member')
        return
      } catch (error) {
        // swallow it, try again as offline member
      }

      try {
        let response = await AXIOS.put('/offline-members/' + this.affectedUserId + '/in-town', null, params)
        alert('Updated for Offline member')
        return
      } catch (error) {
        // swallow it.
      }

      alert('Failed to update in-town status of member')
    }
  }
}
</script>

<style>
</style>
