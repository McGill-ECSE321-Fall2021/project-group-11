<template>
  <body>
  <div id="app">
    <div class="header-bar"> 
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

      <label id="library-name">BEEG YOSHI'S LAND</label>
      <div id="left"> 
        <button class="header-button" @click="$router.push('/browseitem')">BROWSE ITEMS</button>
        <button class="header-button" @click="$router.push('/events')">BOOK AN EVENT</button>
      </div>

      <div id="right">
        <button id="signup-button" class="header-button" @click="$router.push('/newacc')" v-if="!isLoggedIn">SIGN UP</button>
        <button id="login-button" class="header-button" @click="$router.push('/login')" v-if="!isLoggedIn">LOG IN</button>
        <button id="profile-button" class="header-button" @click="$router.push('/profile')" v-if="isLoggedIn">PROFILE</button>
        <button id="logout-button" class="header-button" @click="$store.commit('logout');$router.push('/')" v-if="isLoggedIn">LOG OUT</button>
        <button id="home-button" class="header-button" @click="$router.push('/')"><i class="fa fa-home"></i></button>
      </div> 
   
    </div>
    <router-view id="router-view"></router-view>
  </div>
  </body>
</template>

<script>
export default {
  name: 'app',
  computed:{
    isLoggedIn(){
      return this.$store.state.loginStatus !== null
    }
  }
}
</script>

<style>
#app {
  /* font-family: 'Consolas', Helvetica, Arial, sans-serif; */
  font-family: 'Consolas', Courier, monospace;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: white;
  text-shadow: 0 0 2px black, 0 0 2px black, 0 0 2px black, 0 0 2px black;
  margin: 0px;
  height: 100%;
  width:100%;
  overflow: auto;

}

#router-view {
  position:absolute;
  margin-top: 40px;
  padding-top: 20px;
  top:0;
  right:0;
  left:0;
  bottom:0;
  background-image: url('./assets/test-background.jpg');
  /* add background here yup */
  
}

button{
  background-color:#3ab82c;
  /* border: 1px #123b0d; */
  border: 3px outset;
  border-color: rgba(2, 112, 10, 0.404);
  /* border-style: outset; */
  text-transform: uppercase;
  color:white;
  text-shadow: 0 0 3px black, 0 0 3px black;
  
}

table {
  width: 100%
}

#library-name{
  position:absolute;
  left:45.5%; /* CHANGE THIS LATER */
  right:auto;
  margin-top: 5px;
}

.header-bar{
  position: fixed;
  width: 100%;
  height:50px;
  color: white;
  /* background-color: #245DDA; */
  background-color: #3B77BC;
}

.header-button{
  padding: 5px;
  margin-top:0px;
  border: none;
  color: white;
  text-shadow: 0 0 3px black, 0 0 3px black;
  background: none;
}
.header-button + .header-button{
  margin-left: 10px;
}

#home-button{
  text-shadow: 0 0 3px #DE482B, 0 0 3px #DE482B;
}

#logout-button, #login-button{
  text-shadow: 0 0 3px #FCCF03
}

#profile-button, #signup-button{
  text-shadow: 0 0 3px #81c046
}

#left{
  float: left;
}

#right{
  float: right;
}

</style>
