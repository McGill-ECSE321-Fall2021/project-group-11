<template>
  <div id="homepage">
    <h1>Welcome</h1> <br>

    <h3>Item catalogue</h3>
    <button>Items</button>
    <br><br>

    <h3>Events</h3>
    <button @click="$router.push('/events')">Events</button>
    <br><br>

    <h3>Log in</h3>
    <p>To book items or events, you need to sign in to your account</p>
    <button @click="$router.push('/profile')">Profile</button>
    <button @click="$router.push('/newacc')">Sign up </button>
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
  name: 'homepage'
}
</script>

<style>
</style>
