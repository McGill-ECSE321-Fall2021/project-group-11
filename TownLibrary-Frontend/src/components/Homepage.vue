<template>
  <div id="homepage">
    <h2>People</h2>
    <table>
      <tr>
          <td>John</td>
          <td>Event to attend</td>
      </tr>
      <tr>
          <td>
              <input type="text" placeholder="Person Name">
          </td>
          <td>
              <button>Create</button>
          </td>
      </tr>
    </table>
    <p>
      <span style="color:red">Error: Message text comes here</span>
    </p>
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