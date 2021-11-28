<template>
  <body>
  <div id="app">
    <div id="header-bar" > 
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
      <label id="library-name">Big Yoshi's Library</label>
      <div id="left"> 
        <button class="header-button" @click="$router.push('/browseitem')">Browse Items</button>
        <button class="header-button" @click="$router.push('/events')">Browse An Event</button>
      </div>

      <div id="right">
        <button id="signup-button" class="header-button" @click="$router.push('/newacc')" v-if="!isLoggedIn">Sign Up</button>
        <button id="login-button" class="header-button" @click="$router.push('/login')" v-if="!isLoggedIn">Log In</button>
        <button id="profile-button" class="header-button" @click="$router.push('/profile')" v-if="isLoggedIn">Profile</button>
        <button id="logout-button" class="header-button" @click="$store.commit('logout');$router.push('/')" v-if="isLoggedIn">Log Out</button>
        <button id="home-button" class="header-button" @click="$router.push('/')"><i class="fa fa-home"></i></button>
      </div> 
    
    </div>
    
    <router-view id="router-view"></router-view>


  </div>
  </body>
</template>

<script>

export default{
  name: 'app',
  computed:{
    isLoggedIn(){
      return this.$store.state.loginStatus !== null
    }
  }
}
</script>

<style>
@font-face {
  font-family: Fixedsys;
  src: url(./fonts/fixedsys-excelsior-301-cufonfonts-webfont/FSEX300.woff);
}
#app {
  font-family: 'Fixedsys', Helvetica, Arial, sans-serif;
  /* font-family: 'Consolas', Courier, monospace; */
  -webkit-font-smoothing: none;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: white;
  /* text-shadow: 0 0 2px black, 0 0 2px black, 0 0 2px black, 0 0 2px black; */
  margin: 0px;
  height: 100%;
  width:100%;
  overflow:auto;
  /* background-color:#018281;
  background-attachment: scroll; */

}

#router-view {
  position:relative;
  /* display: block; */
  /* border: 10px solid black; */
  /* margin-top: 53px; */
  /* padding-block: 50px; */
  padding-top: 50px;
  padding-bottom: 20px;
  top:0%;
  right:0%;
  left:0%;
  bottom:0%;
  width: 100%;
  /* height:100vh; */
  min-height: 100vh;
  background:#018281;
  /* background-attachment: scroll; */

  /* background-image: url('./assets/test-background.jpg'); */
  /* add background here yup */
  
}

button{
  border: 3px outset rgba(0, 0, 0, 0.856);
  border-top:1px outset white;
  border-left:1px outset white;
  text-transform: capitalize;
  color: black;
  background-color:#bfbfc1;
  /* text-shadow: 0 0 3px black, 0 0 3px black; */
  
}
button:disabled{
  border: 3px outset rgba(0, 0, 0, 0.856);
  border-top:1px outset white;
  border-left:1px outset white;
  text-transform: capitalize;
  color: white;
  background-color:#8a8a8b;
  /* text-shadow: 0 0 3px black, 0 0 3px black; */
}

table {
  width: 100%
}

#library-name{
  font-family: 'Fixedsys', Helvetica, Arial, sans-serif;
  position:absolute;
  left:45%; /* CHANGE THIS LATER */
  right:auto;
  margin-top: 10px;
}

#header-bar{
  position: sticky;
  position: -webkit-sticky;
  z-index: 10000;
  width: 100%;
  height:53px;
  color: black;
  /* background-color: #245DDA; */
  /* background-color: #3B77BC; */
  background-color: #bfbfc1;
}

.header-button{
  padding: 5px;
  margin-top:5px;
  border: 3px outset rgba(0, 0, 0, 0.856);
  border-top:1px outset white;
  border-left:1px outset white;
  color: black;
  /* text-shadow: 0 0 3px black, 0 0 3px black; */
  background: none;
}
.header-button + .header-button{
  margin-left: 10px;
}

#left{
  float: left;
}

#right{
  float: right;
}

</style>
