<template>
	<div id="create-item">
		<h1>Create Item</h1>

		<input type="number" v-model="newItem.id" placeholder="Item ID">
		<br/>
		<input type="text" v-model="newItem.name" placeholder="Item Name">
		<br/>
		<select v-model="newItem.type">
			<option disabled value="">Item Type</option>
			<option value="archive">Archive</option>
			<option value="newspaper">Newspaper</option>
			<option value="book">Book</option>
			<option value="movie">Movie</option>
			<option value="musicalbum">Music Album</option>
		</select>
		<br/>

		<table>
			<tr v-for="msg in errorMessages" :key="msg">
				<td style="color: red">{{ msg }}</td>
			</tr>
		</table>

		<button :disabled="0 !== errorMessages.length"
				@click="createItem(newItem)">Add Item</button>
		<br/>
		<button @click="navBrowse">View Items</button>
	</div>
</template>

<script>
import axios from 'axios'
import decodeError from '../api_errors.js'

var frontendUrl = process.env.FRONTEND_HOST + ':' + process.env.FRONTEND_PORT
var backendUrl = process.env.API_HOST + ':' + process.env.API_PORT

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
  name: 'create-item',

  data () {
    return {
      newItem: {
		id: '',
        name: '',
        type: '',
	  },

      serverResponse: null
	}
  },

  watch: {
    newItem: {
      deep: true,
      handler (val) {
        this.serverResponse = null
      }
    }
  },

  methods: {
	async createItem (itemInfo) {
		try {
			console.log(itemInfo.type + " selected");
			await AXIOS.post('/' + itemInfo.type + 's/' + itemInfo.id , null, {
				params: {
					name: itemInfo.name,
					type: itemInfo.type,
					libraryId: 0,
				}
			})

			this.navBrowse()

	 	} catch (error) {
        	this.serverResponse = error
      	}
	},

	// Navigate to Browse Item page with useful params
	async navBrowse () {
		await this.$router.push({ name: 'Browse Item' })
	}
  },

  computed: {
    errorMessages () {
      let localizedErrs = []
      if ('' === this.newItem.id)
        localizedErrs.push('Please enter an ID')
      if ('' === this.newItem.name)
        localizedErrs.push('Please enter a name')
      if ('' === this.newItem.type)
        localizedErrs.push('Please select a type')
      if (0 !== localizedErrs.length)
        return localizedErrs

      return decodeError(this.serverResponse)
    }
  },
}
</script>
