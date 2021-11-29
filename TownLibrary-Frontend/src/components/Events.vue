<template>
	<div id="events">
		<h1>Events</h1>
		<div v-if="state === 0">

			<div id="create-block" style="width: 20%;">
				<table>
					<tr>
						<th class="title-header">Create event</th>
					</tr>
					<tr>
						<button @click="$router.push('/create-event')">Create Event</button>
					</tr>
				</table>
			</div>

			<div id="create-block" style="width: 25%;">
				<table>
					<tr>
						<th colspan="3" class="title-header">Event listing</th>
					</tr>
					<tr v-if="events.length === 0">
						<label style="color:#DE482B"> There are currently no events in the system. </label>
					</tr>
					<tr v-else>
						<th class="some-header" style="width:33%;">ID</th>
						<th class="some-header" style="width:57%;">Name</th>
						<th class="some-header" style="width:auto; padding-right:10px;"></th>
					</tr>
					<tr v-for="event in (events || [])" :key="event.id">
						<td>{{event.id}}</td>
						<td>{{event.name}}</td>
						<td v-if="isLibrarian"><button @click="deleteEvent(event.id)">delete</button></td>
					</tr>
				</table>
			</div>

			<div id="create-block" style="width: 20%;">
				<table>
					<tr>
						<th class="title-header">View event details</th>
					</tr>
					<tr>
						<input type="text" v-model="eventId" placeholder="Event ID">
						<br>
						<button @click="eventDetails(eventId)">Event details</button>
					</tr>
				</table>
			</div>
		</div>
		
		<div v-if="state === 1">
			<div v-if="isLibrarian">
				<h2>Name: {{loadedEvent.name}}</h2>
				<h2>ID: {{loadedEvent.id}}</h2>
				<h2>Registered Users IDs: {{loadedEvent.users}}</h2>
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
				<h2>Registered Users IDs: {{loadedEvent.users}}</h2>
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
      userId: 0,

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
				let response = await AXIOS.post("/events/" + eventid + "/users/" + userid, null)
				this.loadedEvent = response.data
			} catch(error) {
				this.serverResponse = null
			}
		},
		async removeUserFromEvent(eventid, userid) {
			try {
				let response = await AXIOS.delete("/events/" + eventid + "/users/" + userid, null)
				this.loadedEvent = response.data
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
		async deleteEvent(eventid) {
			await AXIOS.delete("/events/" + eventid)
			this.loadEvents()
		},
		successRedirect() {
		this.loadedEvent = null
		this.state = 0
    	}
	}
}
</script>
<style scoped>
	#create-block{
		display: block;
		background-color: #bfbfc1;
		margin: 0 auto;
		text-align: center;
		color:black;
		border: 3px outset black;
		border-top: 2px outset white;
		border-left: 2px outset white;
		margin-block: 10px;
	}
	table{
		border-collapse: collapse;
		width: 100%;
	}
	.title-header{
		border-bottom: 2px outset black;
	}
	.some-header{
		text-align: left;
		padding-left: 10px;
	}
	button{
		margin-block: 10px;
	}
	td{
		text-align: left;
		padding-left:10px;
		padding-right:10px;
	}
	input{
		margin-block: 10px;
		border: 2px outset black;
		border-right: 2px outset white;
		border-bottom: 2px outset white;
	}
</style>