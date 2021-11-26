<template>
  <div id="homepage">
    <h1>TO DO HERE: CHANGE THIS PAGE WITH LIKE A SEARCH BAR TO LOOK FOR ITEM OR SMTH</h1>
    <div class="search">
      <input type="text" class="search-bar" placeholder="Search an item by name..." size="50">
      <button type="submit" class="submit-button">Search</button>
    </div>
    <h1>Welcome</h1> <br>

    <h3>Item catalogue</h3>
    <button @click="$router.push('/browseitem')">Items</button>
    <br><br>

    <h3>Events</h3>
    <button @click="$router.push('/events')">Events</button>
    <br><br>

    <h3>Log in</h3>
    <p>To book items or events, you need to sign in to your account</p>
    <button @click="$router.push('/profile')">Profile</button>
    <button @click="$router.push('/profile')">Sign In</button>
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
  name: 'homepage',
  computed:{
    isLoggedIn(){
      return this.$store.state.loginStatus !== null
    }
  }
}
</script>

<style>
</style>
