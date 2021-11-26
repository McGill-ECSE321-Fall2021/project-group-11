<template>
  <body>
  <div id="app">
    <div class="header-bar"> 
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

      <label id="library-name">LIBRARY'S NAME HERE</label>
      <div id="left"> 
        <button class="header-button" @click="$router.push('/browseitem')">Browse items</button>
        <button class="header-button" @click="$router.push('/events')">Book an event</button>
      </div>

      <div id="right">
        <button class="header-button" @click="$router.push('/newacc')" v-if="!isLoggedIn">Sign up</button>
        <button class="header-button" @click="$router.push('/login')" v-if="!isLoggedIn">Log in</button>
        <button class="header-button" @click="$router.push('/profile')" v-if="isLoggedIn">Profile</button>
        <button class="header-button" @click="$store.commit('logout');$router.push('/')" v-if="isLoggedIn">Log out</button>
        <button class="header-button" @click="$router.push('/')"><i class="fa fa-home"></i></button>
      </div> 
   
    </div>
    <router-view id="router-view" style="background-color:pink;" ></router-view>
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
  font-family: 'Comic Sans MS', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin: 0px;
  height: 100%;
  width:100%;
  overflow: auto;

}

#router-view {
  position:absolute;
  margin-top: 50px;
  top:0;
  right:0;
  left:0;
  bottom:0;
  /* add background here yup */
  
}

table {
  width: 100%
}

#library-name{
  position:absolute;
  left:44%; /* CHANGE THIS LATER */
  right:auto;
  margin-top: 5px;
}

.header-bar{
  position: absolute;
  width: 100%;
  height:50px;
  background-image: url("./assets/test.jpg");
}

.header-button{
  padding: 5px;
  margin-top:0px;
  border: none;
  background: none;
}
.header-button + .header-button{
  margin-left: 10px;
}
#left{
  float: left;
}
/* #center{
  text-align: center;
} */
#right{
  float: right;
}

</style>
