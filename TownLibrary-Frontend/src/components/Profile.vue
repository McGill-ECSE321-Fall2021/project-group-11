<template>
  <div id="profile">
    <p>Just to show that stuff does work:</p>
    <ul>
      <li>User type: {{ loginStatus.userType }}</li>
      <li>Username: {{ loginStatus.username }}</li>
      <li>Password: {{ loginStatus.password }}</li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'profile',

  computed: {
    loginStatus () {
      return this.$store.state.loginStatus
    }
  }
}
</script>

<style>
</style>
