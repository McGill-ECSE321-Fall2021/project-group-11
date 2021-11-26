<template>
	<div id="events">
		<h1>Events</h1>
		<br> <br>
		<div v-if="state === 0">
			<h2>Create event:</h2>
			<button @click="$router.push('/create-event')">Create Event</button>
			<br><br>
			<h2>Event listing:</h2>
			<table>
				<tr>
					<th>Name</th>
					<th>Event ID</th>
				</tr>
				<tr v-for="event in (events || [])" :key="event.id">
					<td>{{event.name}}</td>
					<td>{{event.id}}</td>
				</tr>
			</table>
			<br><br>
			<h2>View event details</h2>
			<br>
			<input type="text" v-model="eventId" placeholder="Event ID">
			<br>
			<button @click="eventDetails(eventId)">Event details</button>
			<br>
			<button @click="$router.push('/')">Home</button>
		</div>
		<div v-if="state === 1">
			<h2>Name: {{loadedEvent.name}}</h2>
			<h2>ID: {{loadedEvent.id}}</h2>
			<table>
				<tr>
					<th>Name</th>
					<th>User ID</th>
				</tr>
				<tr v-for="user in (users || [])" :key="user.id">
					<td>{{user.name}}</td>
					<td>{{user.id}}</td>
			<br>
			<input type="text" v-model="userId" placeholder="Online Member ID">
			<button @click="addUserToEvent(loadedEvent.id, userId)">Add user to event</button>
			<br>
			<button @click="successRedirect()">Return to events</button>
		</div>
	</div>
</template>

<script>
import axios from 'axios'
var frontendUrl = process.env.FRONTEND_HOST + ':' + process.env.FRONTEND_PORT
var backendUrl = process.env.API_HOST + ':' + process.env.API_PORT
var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})
export default {
	name: 'events',
	data () {
		return {
			state: 0,

			events: [],
			eventId: '',
			loadedEvent: {},
			users: [],
			serviceResponse = null
		}
	},
	created () {
		this.loadEvents()
	},
	methods: {
		async loadEvents() {
			try {
				let response = await AXIOS.get('/events')
				this.events = response.data
			} catch (error) {
				this.events = null
			}
		},
		async eventDetails(id) {
			try {
				let response = await AXIOS.get('/events/' + id, null, {
					params: {
						lib: 0
					}
				})
				this.loadedEvent = response.data
				this.state++
			} catch(error) {
				this.loadedEvent = null
			}
		},
		async addUserToEvent(eventid, userid) {
			try {
				await AXIOS.post("/events/" + eventid + "/" + userid, null)
			} catch(error) {
				this.serverResponse = null
			}
		}, 
		async getEventUsers(eventid) {
			try {
				let response = await AXIOS.get("/events/" + eventid + "/users", null)
				this.users = response.data
			} catch(error) {
				this.users = null
			}
		},
		successRedirect() {
		this.$router.push('events')
		this.state = 0
    }
	}
}
</script>
