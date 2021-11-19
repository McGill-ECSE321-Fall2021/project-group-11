<template>
  <div id="profile">
    <p>Just to show that stuff does work:</p>
    <ul>
      <li>User type: {{ loginStatus.userType }}</li>
      <li>Username: {{ loginStatus.username }}</li>
      <li>Password: {{ loginStatus.password }}</li>
    </ul>

    <button v-on:click="doLogout">Logout</button>
  </div>
</template>

<script>
export default {
  name: 'profile',

  data () {
    return {
      loginStatus: {}
    }
  },

  created () {
    // Copy the login status out so that it does not cause issues when logout
    // is requested.
    Object.assign(this.loginStatus, this.$store.state.loginStatus)
  },

  methods: {
    doLogout () {
      this.$store.commit('logout')
      this.$router.push('login')
    }
  }
}
</script>

<style>
</style>
