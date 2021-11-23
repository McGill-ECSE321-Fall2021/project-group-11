<template>
	<div id="browse-item">
		<h1>Item Catalogue</h1>

		<h4>Archives</h4>
		<table>
			<tr v-for="archive in (archives || [])" :key="archive.name">
				<td>{{ archive.name }}</td>
				<td>
					<button>View</button>
				</td>
			</tr>
		</table>

		<h4>Newspapers</h4>
		<table>
			<tr v-for="newspaper in (newspapers || [])" :key="newspaper.name">
				<td>{{ newspaper.name }}</td>
				<td>
					<button>View</button>
				</td>
			</tr>
		</table>

		<h4>Books</h4>
		<table>
			<tr v-for="book in (books || [])" :key="book.name">
				<td>{{ book.name }}</td>
				<td>
					<button>View</button>
				</td>
			</tr>
		</table>

		<h4>Movies</h4>
		<table>
			<tr v-for="movie in (movies || [])" :key="movie.name">
				<td>{{ movie.name }}</td>
				<td>
					<button>View</button>
				</td>
			</tr>
		</table>

		<h4>Music Albums</h4>
		<table>
			<tr v-for="musicalbum in (musicalbums || [])" :key="musicalbum.name">
				<td>{{ musicalbum.name }}</td>
				<td>
					<button>View</button>
				</td>
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
	name: 'browse-item',

	data () {
		return {
			archives: [],
			newspapers: [],
			books: [],
			movies: [],
			musicalbums: []
		}
	},

	created () {
		this.loadArchives()
		this.loadNewspapers()
		this.loadBooks()
		this.loadMovies()
		this.loadMusicAlbums()
	},

	methods: {
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
		},
	}
}
</script>