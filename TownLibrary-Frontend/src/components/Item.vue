<template>
	<div id="item">
		<h2>Item Details</h2>

		<ul>
			<li>ID: {{ item.id }}</li>
			<li>Title: {{ item.name }}</li>
			<li>Status: {{ this.status }}</li>
		</ul>

		<div v-if="!isViewOnly">
			<button :disabled="this.status !== 'AVAILABLE'"
					@click="reserveItem(item.id)">Reserve</button>
			<button :disabled="this.status === 'CHECKED_OUT'"
					@click="checkoutItem(item.id)">Checkout</button>
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
			item: {
				id: '',
        		name: '',
				type: '',
			},
			status: ''
			
		}
	},

	created () {
		console.log('Params: ', this.$route.params)
		this.item = this.$route.params
		console.log(this.item.type)
		this.showStatus(this.$route.params.id)
	},

	computed: {
		// Reserve & Checkout buttons don't appear for view-only items
		isViewOnly () {
			if (this.item.type == "archive" || this.item.type == "newspaper") {
				return true
			} else {
				return false
			}
		}
	},

	methods: {
		async showStatus (itemId) {
			let response = await AXIOS.get('/' + this.item.type + 's/' + itemId)
			this.status = response.data.status
			console.log(response.data)
			return this.status
		},

		async reserveItem (itemId) {
			let response = await AXIOS.put('/' + this.item.type + 's/' + itemId + '/reserve')
			this.status = response.data.status
			console.log(response.data.status)
			return this.status
		},

		// async checkoutItem (itemId) {
		// 	console.log("here")
		// 	// Set the transaction id to the same as the associated item's id
		// 	let response = await AXIOS.post('transactions/' + itemId, null, {
		// 		params: {
		// 			startDate: "2021-11-07T00:00:00",
		// 			endDate: "2021-11-09T00:00:00",
		// 			transactionType: this.item.type + "s"
		// 		}
		// 	})
		// 	console.log(response.data)
		// 	return this.status
		// }
	}
	
}
</script>