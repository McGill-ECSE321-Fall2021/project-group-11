<template>
    <div id="schedule-viewer">
        <!-- <h1> {{entityId}} </h1>
        <button v-on:click="view = true"> View Schedule </button>
        <h1 v-if="view"> It works </h1> -->
        <div v-for="schedule in (WeekSchedule || [])" :key="schedule.id">
            <h1>one schedule found</h1>
        </div>

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
    name: 'schedule-viewer',

    data(){
        return {
            view: false,
            loginStatus: {},
            entityId: '',
            weekSchedule: [],
        }
        serverResponse: null
    },

    methods: {
    
        async reloadSchedule(id){
            try {
                if (id === 0){
                    const library = await AXIOS.get('/schedules/library/' + id)
                    const result = await library.data

                    this.entityId = result
                }
                else if (id > 1){
                    const librarian = await AXIOS.get('/schedules/librarian/' + id)
                    const result = await librarian.data

                    this.entityId = result.name
                }
               
            }
            catch (error) {
                this.serverResponse = error
            }
        }
    },

    computed: {
        isHeadLibrarian(){
            switch (this.loginStatus.userType) {
                default:
                    return false
                case 'head-librarian':
                    return true
            }
        },
        isLibrarian(){
            switch (this.loginStatus.userType) {
                default:
                    return false
                case 'librarian':
                    return true
            } 
        },
        isOnlineMember(){
            switch (this.loginStatus.userType) {
                default:
                    return false
                case 'online-member':
                    return true
            }
        } 
    }




}
</script>

<style>

</style>