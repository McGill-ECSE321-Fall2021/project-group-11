<template>
  <div id="homepage">
    <div id="homepage-content">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <form autocomplete="off">
    <img src="../assets/test.png" id="logo">
    <div class="search">
      <!-- <input type="text" class="search-bar" placeholder="Search an item by name..." size="50"> -->
      <input id="search-input" list="items" placeholder="Search an item by name..." size="46">
      <datalist id="items">
        <option v-for="archive in this.archives" :key="archive.id" :value="'ARCHIVE'+': '+archive.name"></option>
        <option v-for="newspaper in this.newspapers" :key="newspaper.id" :value="'NEWSPAPER'+': '+newspaper.name"></option>
        <option v-for="musicalbum in this.musicalbums" :key="musicalbum.id" :value="'MUSICALBUM'+': '+musicalbum.name"></option>
        <option v-for="book in this.books" :key="book.id" :value="'BOOK'+': '+book.name"></option>
        <option v-for="movie in this.movies" :key="movie.id" :value="'MOVIE'+': '+movie.name"></option>
      </datalist>
      <button id="search-button" @click="searchItem()"><i class="fa fa-search"></i></button>
      <br><br>
      <view-schedule :login-status="$store.state.loginStatus" :entityId="0"></view-schedule>

    </div>
    <!-- <h1>Welcome</h1> <br>

    <h3>Item catalogue</h3>
    <button @click="$router.push({name:'Browse Item',
                                  params: {userId: userId}})">Items</button>
    <br><br>

    <h3>Events</h3>
    <button @click="$router.push('/events')">Events</button>
    <br><br>

    <h3>Log in</h3>
    <p>To book items or events, you need to sign in to your account</p>
    <button @click="$router.push('/profile')">Profile</button>
    <button @click="$router.push('/profile')">Sign In</button> -->
    </form>
  </div>
  </div>
</template>

<script>
import axios from 'axios'
import decodeError from '../api_errors.js'
import ViewSchedule from "./ViewSchedule.vue"


var frontendUrl = process.env.FRONTEND_HOST + ':' + process.env.FRONTEND_PORT
var backendUrl = process.env.API_HOST + ':' + process.env.API_PORT

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
  name: 'homepage',
  components: { 'view-schedule':ViewSchedule },

  computed:{
    isLoggedIn(){
      return this.$store.state.loginStatus !== null
    }
  },

  data () {
		return {
			archives: [],
			newspapers: [],
			books: [],
			movies: [],
			musicalbums: [],
		}
	},

  created(){
    this.loadArchives()
		this.loadNewspapers()
		this.loadBooks()
		this.loadMovies()
		this.loadMusicAlbums()
  },

  methods: {
		async viewItem(itemId, itemName, itemType) {
			console.log(itemName)

			this.$router.push({name: 'Item',
				params: { itemId: itemId, itemName: itemName, itemType: itemType }});

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
		},
    async searchItem(){
      var input = document.getElementById("search-input").value
      var itemType = input.substring(0, input.indexOf(':')).toLowerCase()
      var itemName = input.substring(input.indexOf(':')+2)
      var item = {}

      switch (itemType){
        case 'archive':
          for (var archive in this.archives){
            if (this.archives[archive].name === itemName){
              item = this.archives[archive]
            }
          }
          break
        case 'newspaper':
          for (var newspaper in this.newspapers){
            if (this.newspapers[newspaper].name === itemName){
              item = this.newspapers[newspaper]
            }
          }
          break
        case 'book':
          for (var book in this.books){
            if (this.books[book].name === itemName){
              item = this.books[book]
            }
          }
          break
        case 'movie':
          for (var movie in this.movies){
            if (this.movies[movie].name === itemName){
              item = this.movies[movie]
            }
          }
          break
        case 'musicalbum':
          for (var musicalbum in this.musicalbums){
            if (this.musicalbums[musicalbum].name === itemName){
              item = this.musicalbums[musicalbum]
            }
          }
          break

        default:
          window.alert("No such item.")
          return
      }

      var itemId = item.id
     this.viewItem(itemId, itemName, itemType)
      // console.log(this.musicalbums[0])
    },
	}
}
</script>

<style scoped>

  #homepage-content{
    position:absolute;
    top:0;
    left:0;
    right:0;
    bottom:0;
    height:100vh;
    width:100%;
    background-image: url("../assets/test-background.jpg");
      /* background-color: red; */
  }

  #logo{
    -moz-user-select: none;
    -webkit-user-select: none;
    user-select: none;
    pointer-events: none;
  }

  #search-input{
    padding-left: 10px;
    font-family: 'Fixedsys', Arial, Helvetica, sans-serif;
    font-weight: 100;
    font-size: 18px;
    border: 2px outset black;
    border-bottom: 2px outset white;
    border-right: 2px outset white;
  }
  #search-input:focus{
    outline: none;
  }

  .search{
    position:absolute;
    top:650px;
    left:36%;
    right:auto;
  }

  input::-webkit-calendar-picker-indicator{
    display:none;
    opacity:0;
  }

</style>
