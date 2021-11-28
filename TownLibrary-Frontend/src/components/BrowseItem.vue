<template>
	<div id="browse-item">
		<h1>Item Catalogue</h1>
		<table id="archive-table" v-if="archives.length != 0">
			<thead>
				<tr>
					<th colspan="3" class="type-header">Archives</th>
				</tr>
				<tr class="info-row">
					<th class="id-header first-col">Item ID</th>
					<th class="title-header">Title</th>
					<th class="details-header">Details</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="archive in (archives || [])" :key="archive.id">
					<td class="first-col">{{ archive.id }}</td>
					<td>{{ archive.name }}</td>
					<td>
						<button @click="viewItem(archive.id, archive.name, 'archive')">View</button>
					</td>
				</tr>
			</tbody>
		</table>

		<table id="newspaper-table" v-if="newspapers.length != 0">
			<thead>
				<tr>
					<th colspan="3" class="type-header">Newspapers</th>
				</tr>
				<tr class="info-row">
					<th class="id-header first-col">Item ID</th>
					<th class="title-header">Title</th>
					<th class="details-header">Details</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="newspaper in (newspapers || [])" :key="newspaper.id">
					<td class="first-col">{{ newspaper.id }}</td>
					<td>{{ newspaper.name }}</td>
					<td>
						<button @click="viewItem(newspaper.id, newspaper.name, 'newspaper')">View</button>
					</td>
				</tr>
			</tbody>
		</table>

		<table id="book-table" v-if="books.length != 0">
			<thead>
				<tr>
					<th colspan="3" class="type-header">Books</th>
				</tr>
				<tr class="info-row">
					<th class="id-header first-col">Item ID</th>
					<th class="title-header">Title</th>
					<th class="details-header">Details</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="book in (books || [])" :key="book.id">
					<td class="first-col">{{ book.id }}</td>
					<td>{{ book.name }}</td>
					<td>
						<button @click="viewItem(book.id, book.name, 'book')">View</button>
					</td>
				</tr>
			</tbody>
		</table>

		<table id="movie-table" v-if="movies.length != 0">
			<thead>
				<tr>
					<th colspan="3" class="type-header">Movies</th>
				</tr>
				<tr class="info-row">
					<th class="id-header first-col">Item ID</th>
					<th class="title-header">Title</th>
					<th class="details-header">Details</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="movie in (movies || [])" :key="movie.id">
					<td class="first-col">{{ movie.id }}</td>
					<td>{{ movie.name }}</td>
					<td>
						<button @click="viewItem(movie.id, movie.name, 'movie')">View</button>
					</td>
				</tr>
			</tbody>
		</table>

		<table id="musicalbum-table" v-if="musicalbums.length != 0">
			<thead>
				<tr>
					<th colspan="3" class="type-header">Music Albums</th>
				</tr>
				<tr class="info-row">
					<th class="id-header first-col">Item ID</th>
					<th class="title-header">Title</th>
					<th class="details-header">Details</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="musicalbum in (musicalbums || [])" :key="musicalbum.id">
					<td class="first-col">{{ musicalbum.id }}</td>
					<td>{{ musicalbum.name }}</td>
					<td>
						<button @click="viewItem(musicalbum.id, musicalbum.name, 'musicalbum')">View</button>
					</td>
				</tr>
			</tbody>
		</table>


		<!-- <table>
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
		</table> -->

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
<style scoped>

	table {
		border-collapse: collapse;
		border: 5px outset rgba(0, 0, 0, 0.8);
		border-left: 2px outset white;
		border-top: 2px outset white;
		background-color:#bfbfc1;
		color: black;
		margin-left:auto;
		margin-right:auto;
		width: 50%;
		margin-bottom: 20px;
		table-layout: auto;
	}
	.type-header{
		vertical-align: center;
		font-size: 30px;
		font-weight: bolder;
		border-bottom: 3px outset rgba(0, 0, 0, 0.80);
		
	}
	.info-row{
		/* border-bottom: 2px solid black;
		border-top: 2px solid black; */
		text-align: left;
	}
	.id-header{
		width: 25%;
		/* padding-left: 5%; */
	}
	.title-header{
		width: 65%;
	}
	.details-header{
		width: auto;
	}

	td{
		word-wrap: break-word;
		overflow-wrap: break-word;
		vertical-align: top;
		text-align: left;
		padding-top:5px;
		padding-right: 5px;
		padding-bottom: 5px;
		/* padding-block: 5px; */
	}
	.first-col{
		padding-left: 2%;
	}


</style>