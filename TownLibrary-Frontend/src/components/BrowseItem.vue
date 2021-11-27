<template>
	<div id="browse-item">
		<h1>Item Catalogue</h1>

		<table>
			<tr>
				<th>ID</th>
				<th>Title</th>
				<th>Details</th>
			</tr>
			<tr>
				<td colspan="3"><h4>Archives</h4></td>
			</tr>
			<tr v-for="archive in (archives || [])" :key="archive.id">
				<td>{{ archive.id }}</td>
				<td>{{ archive.name }}</td>
				<td>
					<button @click="viewItem(archive.id, archive.name, 'archive')">
						View</button>
				</td>
			</tr>
			<tr>
				<td colspan="3"><h4>Newspapers</h4></td>
			</tr>
			<tr v-for="newspaper in (newspapers || [])" :key="newspaper.id">
				<td>{{ newspaper.id }}</td>
				<td>{{ newspaper.name }}</td>
				<td>
					<button @click="viewItem(newspaper.id, newspaper.name, 'newspaper')">
						View</button>
				</td>
			</tr>
			<tr>
				<td colspan="3"><h4>Books</h4></td>
			</tr>
			<tr v-for="book in (books || [])" :key="book.id">
				<td>{{ book.id }}</td>
				<td>{{ book.name }}</td>
				<td>
					<button @click="viewItem(book.id, book.name, 'book')">
						View</button>
				</td>
			</tr>
			<tr>
				<td colspan="3"><h4>Movies</h4></td>
			</tr>
			<tr v-for="movie in (movies || [])" :key="movie.id">
				<td>{{ movie.id }}</td>
				<td>{{ movie.name }}</td>
				<td>
					<button @click="viewItem(movie.id, movie.name, 'movie')">
						View</button>
				</td>
			</tr>
			<tr>
				<td colspan="3"><h4>Music Albums</h4></td>
			</tr>
			<tr v-for="musicalbum in (musicalbums || [])" :key="musicalbum.id">
				<td>{{ musicalbum.id }}</td>
				<td>{{ musicalbum.name }}</td>
				<td>
					<button @click="viewItem(musicalbum.id, musicalbum.name, 'musicalbum')">
						View</button>
				</td>
			</tr>
		</table>

		<div v-if="isLibrarian">
			<button @click="$router.push({name: 'Create Item',
                                    params: {
                                      userType: loggedUser.userType,
                                      userId: loggedUser.userId
                                   }})">Add item</button>
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
	name: 'browse-item',

	data () {
		return {
			loggedUser: {
				userType: '',
				userId: '',
	  		},

			archives: [],
			newspapers: [],
			books: [],
			movies: [],
			musicalbums: [],
		}
	},

	created () {
		this.loadArchives()
		this.loadNewspapers()
		this.loadBooks()
		this.loadMovies()
		this.loadMusicAlbums()

		console.log('Params: ', this.$route.params)
		this.loggedUser = this.$route.params
	},

	computed: {
		isLibrarian () {
			switch (this.loggedUser.userType) {
			default:
				return false
			case 'librarian':
			case 'head-librarian':
				return true
			}
    	}
	},

	methods: {
		async viewItem(itemId, itemName, itemType) {
			console.log(itemName)

			this.$router.push({name: 'Item',
				params: { itemId: itemId, itemName: itemName, itemType: itemType,
				userType: this.loggedUser.userType, userId: this.loggedUser.userId }});
		},

		async loadArchives() {
			try {
				let response = await AXIOS.get('/archives')
				this.archives = response.data
			} catch (error) {
				this.archives = null
			}
		},

		async loadNewspapers() {
			try {
				let response = await AXIOS.get('/newspapers')
				this.newspapers = response.data
			} catch (error) {
				this.newspapers = null
			}
		},

		async loadBooks() {
			try {
				let response = await AXIOS.get('/books')
				this.books = response.data
			} catch (error) {
				this.books = null
			}
		},

		async loadMovies() {
			try {
				let response = await AXIOS.get('/movies')
				this.movies = response.data
			} catch (error) {
				this.movies = null
			}
		},

		async loadMusicAlbums() {
			try {
				let response = await AXIOS.get('/musicalbums')
				this.musicalbums = response.data
			} catch (error) {
				this.musicalbums = null
			}
		}

	}
}
</script>
