<template>
	<div id="events">
		<h1>Events</h1>
		<div v-if="state === 0">

			<div id="create-block" style="width: 10%;">
				<table>
					<tr>
						<th class="title-header">Create event</th>
					</tr>
					<tr>
						<button @click="$router.push('/create-event')">Create</button>
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
						<td v-if="isLibrarian"><button @click="deleteEvent(event.id)">Delete</button></td>
					</tr>
				</table>
			</div>

			<div id="create-block" style="width: 18%;">
				<table>
					<tr>
						<th class="title-header">View Event Details</th>
					</tr>
					<tr>
						<input type="text" v-model="eventId" placeholder="Event ID" size="10" autocomplete="off" style="margin-inline: 5px;">
						<button @click="eventDetails(eventId)">View</button>
					</tr>
				</table>
			</div>
		</div>

		<div v-if="state === 1">
			<div id="create-block" style="width:25%; padding-block: 10px;">
				<table>
					<tr>
						<th style="border-bottom: 2px outset black;">Event Details</th>
					</tr>
					<tr>
						<label class="info-row">Name: {{loadedEvent.name}}</label> <br>
						<label class="info-row">ID: {{loadedEvent.id}}</label> <br>
						<label class="info-row">Registered Users: {{loadedEvent.users.length}}</label>						
					</tr>
				</table>
			</div>
			<div id="create-block" style="width:20%; padding-block: 10px;">
				<table>
					<tr>
						<th style="border-bottom: 2px outset black;">Event Management</th>
					</tr>
					<tr>
						<input type="text" id="online-member-id" placeholder="Online Member ID" autocomplete="off"> <br>
						<button @click="addUserToEvent(loadedEvent.id)" style="margin-inline: 5px;">Add User</button>
						<button v-if="isLibrarian" @click="removeUserFromEvent(loadedEvent.id)">Remove User</button>
					</tr>
					<label style="color:#DE482B">Note: Users must be online members.</label>
				</table>
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
		// changed params, because there was a weird behaviour with the text input boxes
		async addUserToEvent(eventid) {
			try {
				var memberId = document.getElementById('online-member-id').value
				// console.log(this.userId)
				// ?? this suddenly stopped throwing an error when we try to add an online member twice???? 
				// does not update the counter (which is good), but does not go through catch to display error alert
				let response = await AXIOS.post("/events/" + eventid + "/users/" + memberId, null)
				this.loadedEvent = response.data
				window.alert("Successfully registered!")
			} catch(error) {
				window.alert("This user does not exist.")
				this.serverResponse = null
				try{
					if (memberId !== ""){
						await AXIOS.get('/online-members/'+memberId)	
						window.alert("This user has already been registered for this event.")
					}else {
						window.alert("Please enter an ID.")
					}
					
				}catch(error){
					window.alert("ID does not correspond to an online member.")
				}
			}
		},
		// same as add
		async removeUserFromEvent(eventid) {
			try {
				var memberId = document.getElementById('online-member-id').value

				let response = await AXIOS.delete("/events/" + eventid + "/users/" + memberId, null)
				this.loadedEvent = response.data
				// same as above??? tested with timeout to make sure the response goes through
				window.alert("Successfully removed!")

			} catch(error) {
				window.alert("This user does not exist.")
				this.serverResponse = null
				window.alert("ID does not belong to any online members registered to this event.")
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
		margin-block: 30px;
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
	.info-row{
		border: 2px outset black;
		border-right: 2px outset white;
		border-bottom: 2px outset white;
		background: white;
		width: 80%;
		text-align: left;
		padding-left: 5px;
		margin-block: 10px;
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