<template>
  <div id="profile">
    <h2>Welcome back, {{ displayName }}!</h2>


    <div id="create-block" v-if="isLibrarian">
      <table id="create-block-table">
        <tr style="border-bottom: 2px outset black;">
          <th>Create entities</th>
        </tr>
        <tr>
          <td style="padding: 10px;">
            <button @click="$router.push('/newacc/offline')" style="margin-bottom: 10px;">Create a new offline member</button> <br>
            <button v-if="isHeadLibrarian" @click="$router.push('/newacc/librarian')" style="margin-bottom: 10px;">Create a new librarian</button> <br v-if="isHeadLibrarian">
            <button @click="$router.push({ name: 'Create Item' })">Create item</button>
          </td>
        </tr>
      </table>
    </div>

    <div id="create-block" style="width:30%;" v-if="isLibrarian">
      <table id="town-status-block-table">
        <tr style="border-bottom: 2px outset black;">
          <th>Set location status</th>
        </tr>
        <tr>
          <td style="padding: 10px;">
            <input type="text" placeholder="Member ID" v-model="affectedUserId" style="border: 2px outset black; border-right: 2px outset white; border-bottom: 2px outset white;">
            <button @click="getAddressOfAffectedUser">Get address</button>
          </td>
        </tr>
        <tr>
          <td style="padding-bottom: 10px;">
            <button @click="setInTownAffectedUser(true)">Set In Town</button>
            <button @click="setInTownAffectedUser(false)">Set Out of Town</button>
          </td>
        </tr>
        <tr>
          <td style="padding-bottom: 10px;">
            <button @click="checkUserTransactions" class="buttonofblocks">transactions</button>
          </td>
        </tr>
      </table>
    </div>

    <div id="create-block" style="width:45%;" v-if="isHeadLibrarian">
      <table id="librarian-block-table">
        <tr style="border-bottom: 2px outset black;">
          <th>List of librarians</th>
        </tr>
         <tr v-if="librarians !== null">
          <table style="text-align:left;">
            <tr>
              <th class="id-header first-col">ID</th>
              <th class="name-header">Name</th>
              <th class="address-header">Address</th>
              <th class="button-header"></th>
            </tr>
            <tr v-for="librarian in (librarians || [])" :key="librarian.id" class="librarian-table-row">
              <td class="librarian-data first-col">{{librarian.id}}</td>
              <td class="librarian-data">{{librarian.name}}</td>
              <td class="librarian-data">{{librarian.address}}</td>
              <td class="librarian-data" style="padding:5px;"><button class="delete-librarian-button" :disabled="librarian.id === userId"
                    @click="deleteLibrarian(librarian.id)">Delete</button></td>
            </tr>
          </table>
        </tr>
      </table>
      <button @click="reloadLibrarians()" style="margin-block:10px;">Refresh</button> <br>
      <label v-if="librarians === null" style="color:#DE482B">Failed to load from server, try again later by clicking on refresh</label>
    </div>

    <div id="create-block" style="width:33%;" v-if="isHeadLibrarian">
      <table id="schedule-block-table">
        <tr style="border-bottom: 2px outset black;">
          <th>Schedules</th>
        </tr>
         <tr v-if="librarians !== null">
          <table style="text-align:left;">
            <tr>
              <th class="id-header-schedule first-col">ID</th>
              <th class="name-header-schedule">Name</th>
              <th class="button-header-schedule"></th>
            </tr>
            <tr class="librarian-table-row">
              <td class="librarian-data first-col">0</td>
              <td class="librarian-data">Library</td>
              <td style="padding: 10px;"><view-schedule :login-status="loginStatus" :entity-id="0"></view-schedule></td>
            </tr>
            <tr v-for="librarian in (librarians || [])" :key="librarian.id" class="librarian-table-row">
              <td class="librarian-data first-col">{{librarian.id}}</td>
              <td class="librarian-data">{{librarian.name}}</td>
              <!-- <td class="librarian-data">{{librarian.address}}</td> -->
              <td style="padding: 10px;">
                <view-schedule class="librarian-schedule-button" :login-status="loginStatus" :entity-id="librarian.id" :schedule-owner="librarian.name"> </view-schedule>
              </td>
            </tr>
          </table>
        </tr>
      </table>
    </div>
    <div id="create-block" style="width:15%;" v-else-if="isLibrarian">
      <table>
        <tr style="border-bottom: 2px outset black;">
          <th>Schedules</th>
        </tr>
        <tr>
          <td style="padding: 10px;">
            <b>Library</b><view-schedule :login-status="loginStatus" :entity-id="0"></view-schedule>
          </td>
        </tr>
        <tr>
          <td style="padding:10px;">
            <b>Your schedule</b>
            <view-schedule :login-status="loginStatus" :entity-id="userId" :scheduleOwner="displayName"></view-schedule>
          </td>
        </tr>
      </table>
    </div>

    <div v-if="isOnlineMember" id="buttonblock">
      <br>
      <button @click="$router.push({name: 'User Transactions',
                                    params: {
                                      id: userId
                                   }})" class="buttonofblocks">transactions</button>
      <br>
      <button @click="$router.push({ name:'Personal Information' })" class="buttonofblocks">personal information</button>
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
    },

    userId () {
      return this.loginStatus.userInfo.id
    },

    displayName () {
      return this.loginStatus.userInfo.name
    }
  },

  methods: {
    checkUserTransactions () {
      if ('' === this.affectedUserId)
        return

      this.$router.push({name: 'User Transactions',
        params: {
          id: this.affectedUserId
      }})
    },

    doLogout () {
      this.$store.commit('logout')
      this.$router.push('/login')
    },

    async reloadUserInfo () {
      try {
        let response = await AXIOS.post('/auth/' + this.loginStatus.userType + 's/' + this.loginStatus.username, null, {
          params: {
            password: this.loginStatus.password
          }
        })

        // In case old info is outdated, just reapply the new stuff
        this.loginStatus.userInfo = response.data
        this.$store.commit('updateUserInfo', response.data)
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

  .librarian-table-row{
    width:100%;
    text-align:center
  }
  .librarian-schedule-button{
    display: inline;
  }
  .librarian-schedule-button:disabled{
    display: inline;
  }
  .delete-librarian-button{
    display: inline;
  }
  .delete-librarian-button:disabled{
    display: inline;
  }
  #create-block{
    color: black;
    display:block;
    padding-top: 5px;
    margin: auto;
    margin-bottom: 10px;
    background-color:#bfbfc1;
    border: 3px outset rgba(0,0,0,0.8);
    border-left: 2px outset white;
    border-top: 2px outset white;
    width: 20%;
  }
  #create-block-table{
    width: 100%;
    border-collapse: collapse;
    margin: 0 auto;
    text-align: center;
  }

  .id-header{
		width: 20%;
	}
  .id-header-schedule{
		width: 25%;
	}
	.name-header{
		width: 30%;
	}
	.name-header-schedule{
		width: 45%;
	}
	.address-header{
		width: 40%;
	}
  .button-header{
    width: auto;
  }

#buttonblock{
  display: block;
  /* padding-top: 10px; */
  margin: auto;
  margin-bottom: 10px;
  background-color: #bfbfc1;
  width: 15%;
  height: 27%;
  border: 5px outset rgba(0, 0, 0, 0.8);
  border-left: 2px outset white;
  border-top: 2px outset white;
}

.buttonofblocks {
  width : 90%;
  margin-bottom: 20px;
}
  .button-header-schedule{
    width: auto;
  }
  .librarian-data{
    text-align:left;
  }
  .first-col{
    padding-left: 15px;
  }
</style>
