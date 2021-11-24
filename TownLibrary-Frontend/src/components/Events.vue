<template>
	<div id="events">
		<h1>Events</h1>
		<br> <br>
		<h2>Event listing:</h2>
		<table>
			<tr v-if="events === null">
				<td>There are no events available at the moment. You can create one here:</td>
			</tr>
			<br>

			<tr v-if="events === null">
				<button @click="$router.push('/create-event')">Create Event</button>
			</tr>
			<tr v-if="events !== null">
				<th>Name</th>
				<th>ID</th>
			</tr>
			<tr v-for="event in (events || [])" :key="event.id">
				<td>{{ event.name }}</td>
				<td>{{ event.id }}</td>
			</tr>
		</table>
	</div>
</template>

<script>
import axios from 'axios'
var frontendUrl = 'http://' + process.env.FRONTEND_HOST + ':' + process.env.FRONTEND_PORT
var backendUrl = 'http://' + process.env.API_HOST + ':' + process.env.API_PORT
var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})
export default {
	name: 'events',
	data () {
		return {
			events: []
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
		}
	}
}
</script>