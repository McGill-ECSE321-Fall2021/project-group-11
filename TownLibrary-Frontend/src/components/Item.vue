<template>
	<div id="item">
		<h2>Item Details</h2>

		<ul>
			<li>ID: {{ itemInfo.id }}</li>
			<li>Title: {{ itemInfo.name }}</li>
			<li>Status?</li>
		</ul>

		<div>
			<button>Reserve</button>
			<button>Checkout</button>
		</div>
		<button @click="$router.push('/browseitem')">Back</button>
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
	name: 'item',

	data () {
		return {
			itemInfo: {}
		}
	}
	
}
</script>