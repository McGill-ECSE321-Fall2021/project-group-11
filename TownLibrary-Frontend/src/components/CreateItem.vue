<template>
	<div id="create-item">
		<h1>Create Item</h1>

		<input type="text" v-model="newItem.id" placeholder="Item ID">
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

		<button @click="createItem(newItem)">Add Item</button>
		<br/>|
		<button @click="$router.push('/browseitem')">View Items</button>
	</div>
</template>

<script>
import axios from 'axios'
import decodeError from '../api_errors.js'

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
		id: '',
        name: '',
        type: '',
      },

      serverResponse: null
	}
  },

  methods: {
	async createItem (itemInfo) {
		try {
		  if (itemInfo.type == "archive") {
			  console.log("Archive selected");
			  await AXIOS.post('/archives/' + itemInfo.id , null, {
				  params: {
					  name: itemInfo.name,
					  type: itemInfo.type,
					  libraryId: 0
				  }
			  })
		  } else if (itemInfo.type == "newspaper") {
			  console.log("Newspaper selected");
			  await AXIOS.post('/newspapers/' + itemInfo.id , null, {
				  params: {
					  name: itemInfo.name,
					  type: itemInfo.type,
					  libraryId: 0
				  }
			  })
		  } else if (itemInfo.type == "book") {
			  console.log("Book selected");
			  await AXIOS.post('/books/' + itemInfo.id , null, {
				  params: {
					  name: itemInfo.name,
					  type: itemInfo.type,
					  libraryId: 0
				  }
			  })
		  } else if (itemInfo.type == "movie") {
			  console.log("Movie selected");
			  await AXIOS.post('/movies/' + itemInfo.id , null, {
				  params: {
					  name: itemInfo.name,
					  type: itemInfo.type,
					  libraryId: 0
				  }
			  })
		  } else if (itemInfo.type == "musicalbum") {
			  console.log("MusicAlbum selected");
			  await AXIOS.post('/musicalbums/' + itemInfo.id , null, {
				  params: {
					  name: itemInfo.name,
					  type: itemInfo.type,
					  libraryId: 0
				  }
			  })
		  }
	 	} catch (error) {
        	this.serverResponse = error
      	}
	}
  }
}
</script>