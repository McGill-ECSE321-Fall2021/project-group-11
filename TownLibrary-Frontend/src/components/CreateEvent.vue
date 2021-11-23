<template>
	<div id="create-event">
		<h1>Create Event</h1>

		<input type="text" v-model="newEvent.name" placeholder="Event Name">
		<br/><br>

		<button @click="createEvent(newEvent)">Add Event</button>
		<br/><br>
		<button @click="$router.push('/events')">View Events</button>
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
  name: 'create-event',
  data () {
    return {
      newEvent: {
          name: '',
      },
      serverResponse: null
	}
  },
  methods: {
    async createEvent (event) {
        try {
            await AXIOS.post('/events/' + event.name, null, {
                params: {
                    name: event.name,
                    lib: 0
                }
            })
        } catch (error) {
            this.serverResponse = error
        }
    }
  }
}
</script>

<style>
</style>