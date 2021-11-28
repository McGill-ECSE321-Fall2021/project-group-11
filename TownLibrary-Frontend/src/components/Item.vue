<template>
	<div id="item">
		<h2>Item Details</h2>

		<table>
			<tr>ID: {{ itemId }}</tr>
			<tr>Title: {{ itemName }}</tr>
			<tr>Status: {{ status }}</tr>
		</table>
		<!-- <ul>
			<li>ID: {{ this.itemId }}</li>
			<li>Title: {{ this.itemName }}</li>
			<li>Status: {{ this.status }}</li>
		</ul> -->

		<div v-if="!isViewOnly">
			<div v-if="!isLibrarian">
				<button :disabled="this.status === 'CHECKED_OUT'"
						@click="createTransactionOnline">Checkout</button>
			</div>
			<div v-if="isLibrarian">
				<input type="number" v-model="offlineMemberId" placeholder="Offline Member Id">
				<br/>
				<button :disabled="this.status === 'CHECKED_OUT'"
						@click="createTransactionOffline">Checkout</button>
			</div>

			<button :disabled="this.status !== 'AVAILABLE'"
					@click="reserveItem">Reserve</button>
		</div>
		<button @click="navBrowse">Back</button>
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
	name: 'item',

	data () {
		return {
			userType: '',
			userId: '',

			itemId: '',
        	itemName: '',
			itemType: '',

			status: '',

			offlineMemberId: ''
		}
	},

	created () {
		console.log('Params: ', this.$route.params)
		this.itemId = this.$route.params.itemId
		this.itemName = this.$route.params.itemName
		this.itemType = this.$route.params.itemType

		this.userType = this.$store.state.loginStatus.userType
		this.userId = this.$store.state.loginStatus.userInfo.id

		this.showStatus()
	},

	computed: {
		// Reserve & Checkout buttons don't appear for view-only items
		isViewOnly () {
			if (this.itemType == "archive" || this.itemType == "newspaper") {
				return true
			} else {
				return false
			}
		},

		isLibrarian () {
			switch (this.userType) {
			default:
				return false
			case 'librarian':
			case 'head-librarian':
				return true
			}
		}
	},

	methods: {
		// Navigate to Browse Item page with useful params
		async navBrowse () {
			await this.$router.push({ name: 'Browse Item' })
		},

		async showStatus () {
			let response = await AXIOS.get('/' + this.itemType + 's/' + this.itemId)
			this.status = response.data.status
			console.log(response.data)
			return this.status
		},

		async reserveItem () {
			let response = await AXIOS.put('/' + this.itemType + 's/' + this.itemId + '/reserve')
			this.status = response.data.status
			console.log(response.data.status)
			//return this.status
		},


		// Online members have their id stored in this.userId
		// and may checkout by themselves
		async createTransactionOnline () {
			console.log("creating transaction")
			console.log(this.userId)

			await AXIOS.post('/transactions/' + this.userId + this.itemId, null, {
				  params: {
					  startDate: new Date(),
					  // 2 weeks later
					  endDate: new Date(Date.now() + 12096e5),
					  transactionType: this.itemType + "s"
				  }
			})

			checkoutItem()
		},

		// Offline member's id must be typed in manually by the librarian
		async createTransactionOffline () {
			console.log("creating transaction")
			console.log(this.offlineMemberId)

			await AXIOS.post('/transactions/' + this.offlineMemberId + this.itemId, null, {
				  params: {
					  startDate: new Date(),
					  // 2 weeks later
					  endDate: new Date(Date.now() + 12096e5),
					  transactionType: this.itemType + "s"
				  }
			})

			checkoutItem()
		},

		async checkoutItem () {
			console.log("checkout")

			let response = await AXIOS.put
				('/' + this.itemType + 's/' + this.itemId + '/checkout',
				{ transactionId: this.itemId })

			this.status = response.data.status
			console.log(response.data.status)
			return this.status
		}
	}

}
</script>
