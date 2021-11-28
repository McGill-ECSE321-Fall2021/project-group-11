<template>
	<div id="events">
		<h1>Events</h1>
		<br> <br>
		<div v-if="state === 0">
			<h2>Create event:</h2>
			<button @click="$router.push('/create-event')">Create Event</button>
			<br><br>
			<h2>Event listing:</h2>
			<div v-if="isLibrarian">
				<table>
					<tr>
						<th></th>
						<th>Name</th>
						<th>Event ID</th>
					</tr>
					<tr v-for="event in (events || [])" :key="event.id">
						<td><button @click="deleteEvent(event.id)">delete</button>
						<td>{{event.name}}</td>
						<td>{{event.id}}</td>
					</tr>
				</table>
			</div>
			<div v-else>
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
			</div>
			<br><br>
			<h2>View event details</h2>
			<input type="text" v-model="eventId" placeholder="Event ID">
			<br>
			<button @click="eventDetails(eventId)">Event details</button>
			<!-- <br>
			<button @click="$router.push('/')">Home</button> -->
		</div>
		<div v-if="state === 1">
			<div v-if="isLibrarian">
				<h2>Name: {{loadedEvent.name}}</h2>
				<h2>ID: {{loadedEvent.id}}</h2>
				<h2>Registered Users: {{loadedEvent.users}}</h2>
				<br>
				<input type="text" v-model="userId" placeholder="Online Member ID">
				<button @click="addUserToEvent(loadedEvent.id, userId)">Add user to event</button>
				<button @click="removeUserFromEvent(loadedEvent.id, userId)">Remove user from event</button>
				<br>{{serverResponse}}
				<button @click="successRedirect()">Return to events</button>
			</div>
			<div v-else>
				<h2>Name: {{loadedEvent.name}}</h2>
				<h2>ID: {{loadedEvent.id}}</h2>
				<h2>Registered Users: {{loadedEvent.users}}</h2>
				<br>
				<input type="text" v-model="userId" placeholder="Online Member ID">
				<button @click="addUserToEvent(loadedEvent.id, userId)">Add user to event</button>
				<br>
				<button @click="successRedirect()">Return to events</button>
			</div>
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
			serverResponse: null,

			loginStatus: {}
		}
	},
	created () {
		Object.assign(this.loginStatus, this.$store.state.loginStatus)
		this.loadEvents()
	},

	computed: {
		isLibrarian() {
			switch (this.loginStatus.userType) {
				default: return false
				case 'librarian':
				case 'head-librarian':
					return true
			}
		}
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
				if (id.trim() == "") return
				let response = await AXIOS.get('/events/' + id, null, {
					params: {
						lib: 0
					}
				})
				this.loadedEvent = response.data
				this.state = 1
			} catch(error) {
				window.alert("This event does not exist.")
				this.loadedEvent = null
			}
		},
		async addUserToEvent(eventid, userid) {
			try {
				let response = await AXIOS.post("/events/" + eventid + "/" + userid, null)
				this.loadedEvent = response.data
			} catch(error) {
				this.serverResponse = "nol"
			}
		}, 
		async removeUserFromEvent(eventid, userid) {
			try {
				let response = await AXIOS.post("/events/remove/" + eventid + "/" + userid, null)
				this.loadedEvent = response.data
			} catch(error) {
				this.serverResponse = "hello"
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
		async deleteEvent(eventid) {
			await AXIOS.delete("/events/delete/" + eventid)
			this.loadEvents()
		},

		successRedirect() {
		// this.$router.push('events')
		this.loadedEvent = null
		this.state = 0
    }
	}
}
</script>
