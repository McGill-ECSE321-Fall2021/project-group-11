<template>
	<div id="create-item">
		<h1>Create Item</h1>

		<input type="text" v-model="newItem.name" placeholder="Item Name">
		<br/>
		<select v-model="newItem">
			<option disabled value="">Item Type</option>
			<option v-bind:value="{ type: archive }">Archive</option>
			<option v-bind:value="{ type: newspaper }">Newspaper</option>
			<option v-bind:value="{ type: book }">Book</option>
			<option v-bind:value="{ type: movie }">Movie</option>
			<option v-bind:value="{ type: musicalbum }">Music Album</option>
		</select>

		<button>Add Item</button>
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
  name: 'create-item',

  data () {
    return {
      newItem: {
        name: '',
        type: '',
      },

      serverResponse: []
	}
  },

  methods: {
	  async createItem (itemInfo) {
		  if (itemInfo.type == archive) {
			  //WIP
		  }
	  }
  }
}
</script>