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

            <label class="title">Attending Events</label>
            <p v-if="null === events" style="color: #DE482B;">Failed to load from server, try again later</p>
            <table v-if="null !== events">
              <tr>
                <th>Name</th>
                <th>Event ID</th>
              </tr>
              <tr v-for="event in (events || [])">
                <td>{{ event.name }}</td>
                <td>{{ event.id }}</td>
              </tr>
            </table>
            <br>
        </div>

        <div>
            <button @click="$router.push('/profile')">back</button>
        </div>

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
