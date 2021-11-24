<template>
	<div id="create-event">
    <div v-if="state === 0">
      <h1>Create Event</h1>

      <input type="text" v-model="newEvent.name" placeholder="Event Name">
      <br/><br>

      <button @click="createEvent(newEvent)">Create</button><br>
      Note: events can only be created if you are signed in to an account.
      <br/><br>
      <button @click="$router.push('/events')">View Events</button>
    </div>

    <div v-if="state === 1">
      <h2>Successfully added {{ createdEvent.name }}</h2>
      <p>This event has been assigned an Event ID of {{ createdEvent.id }}!</p>
      <button @click="successRedirect()">Return to event page</button>
    </div>
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
      state: 0,

      newEvent: {
          name: '',
      },
      createdEvent: {},
      serverResponse: null
	}
  },
  methods: {
    async createEvent (event) {
        try {
            let response = await AXIOS.post('/events/' + event.name, null, {
                params: {
                    name: event.name,
                    lib: 0
                }
            })
            this.createdEvent = response.data
            this.state++
            
        } catch (error) {
            this.serverResponse = error
        }
    },

    successRedirect() {
      this.$router.push('events')
      this.state = 0
    }
  }
}
</script>

<style>
</style>