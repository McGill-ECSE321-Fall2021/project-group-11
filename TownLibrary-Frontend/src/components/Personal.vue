<template>
    <div id="personal">
        <h2> Account Information </h2>
        <br>
        <br>
        <div id="info">
            <label class="title"> User ID </label>
            <label class="textbox"> {{ $store.state.loginStatus.userInfo.id }} </label> <br>
            <label class="title"> Full Name </label>
            <label class="textbox"> {{ $store.state.loginStatus.userInfo.name }} </label> <br>
            <label class="title"> Email </label>
            <label class="textbox">  {{ $store.state.loginStatus.userInfo.email }} </label> <br>
            <label class="title"> Address </label>
            <label class="textbox">  {{ $store.state.loginStatus.userInfo.address }}</label> <br>
            <label class="title"> In Town </label>
            <label class="textbox">  {{ $store.state.loginStatus.userInfo.inTown }} </label> <br>
            <br>    
        </div>
        <div id="event-block">
            <table id="event-table">
                <tr style="text-align:center;">
                    <th colspan="2" style="border-bottom: 2px outset black;">Attending Events</th>
                </tr>
                <tr v-if="null === events">
                    <td style="color: #DE482B;">Failed to load from server, try again later. </td>
                </tr>
                <tr v-else-if="events.length === 0">
                    <td style="color: #DE482B; text-align:center; padding:10px;"> Currently not attending any events. </td>
                </tr>
                <tr v-else>
                    <th style="padding-left: 10px; width: 35%;">ID</th>
                    <th style="width:auto;">Name</th>
                </tr>
                <tr v-for="event in (events || [])" :key="event.id">
                    <td style="padding: 10px;">{{event.id}}</td>
                    <td>{{event.name}}</td>
                </tr>
            </table>

        </div>    

        <button @click="$router.push('/profile')">back</button>

    </div>

</template>


<script>
import axios from 'axios'

var frontendUrl =  process.env.FRONTEND_HOST + ':' + process.env.FRONTEND_PORT
var backendUrl = process.env.API_HOST + ':' + process.env.API_PORT

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
    name: 'Personal',

    data () {
        return {
            events: []
        }
    },

    async created () {
        try {
            let response = await AXIOS.get('/users/' + this.$store.state.loginStatus.userInfo.id + '/events')
            this.events = response.data
        } catch (error) {
            this.events = null
        }
    }
}
</script>

<style scoped>

#event-block{
    display: block;
    background: #bfbfc1;
    width: 25%;
    border: 5px outset rgb(0,0,0,0.8);
    border-left: 2px outset white;
    border-top: 2px outset white;
    margin: 0 auto;
    margin-block: 10px;
    color: black;
}

#event-table{
    width:100%;
    text-align: left;
}

.title {
    display: block;
    text-align: left;
    margin: auto;
    width: 50%;
    color: black;
    font-weight: bold;
}

.textbox {
    color: black;
    display: block;
    padding-left: 5px;
    background-color: white;
    border: 2px outset white;
    border-left: 2.5px outset rgba(0, 0, 0, 0.7);
    border-top: 2.5px outset rgba(0, 0, 0, 0.7);
    text-align: left;
    margin-block: 10px;
    margin: auto;
    width: 50%;
}

#info{
  display: block;
  padding-top: 10px;
  margin: auto;
  margin-bottom: 10px;
  background-color: #bfbfc1;
  width: 30%;
  height: 50%;
  border: 5px outset rgba(0, 0, 0, 0.8);
  border-left: 2px outset white;
  border-top: 2px outset white;
}
</style>
