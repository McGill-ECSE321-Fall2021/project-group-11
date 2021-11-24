// CHANGE NAME OF FILE AFTER
<template>
    <div id="create-schedule" v-if="isHeadLibrarian">
        <button v-on:click="open=true">Create Schedule</button>
        <div class="modal-block" v-if="open">
            <div class="overlay"></div>
            <div class="modal-card card">
                <h4 class="title"> <b>Create schedule</b> </h4>
                <button class="exit-button" v-on:click="open=false">x</button>
                <h5 style="text-align:left;">{{scheduleOwner}} schedule</h5>
                <body class="content">
                    <calendar v-bind:user="loginStatus.userType" v-bind:user-schedule="scheduleArray"> </calendar>
                    <h1>ddd</h1>
                </body>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios'
import Calendar from './Calendar.vue'

var frontendUrl = 'http://' + process.env.FRONTEND_HOST + ':' + process.env.FRONTEND_PORT
var backendUrl = 'http://' + process.env.API_HOST + ':' + process.env.API_PORT

var AXIOS = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
})
export default {
    name: "create-schedule",
    props: {
        loginStatus:{
            type: Object,
            required: true,
            default: false
        },
        entityId:{
            type: Number,
            required: true
        },
        scheduleOwner:{
            type: String,
            required: true,
            default: "Library"
        }

    },

    components:{
        'calendar': Calendar
    },

    data(){
        return{
            open: false,  
            scheduleArray: []
        }
        
    },
    methods: {
      
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
        async getSchedule(){
            try {
                if (this.entityId === 0){
                    const request = await AXIOS.get('/schedules/library/0')
                    this.scheduleArray = request.data
                }
               
            } catch (error) {
                if (error.response.status == '400'){
                    this.scheduleArray = []
                }
            }
        }
    }

}

</script>

<style scoped>
    .modal-block{
        position: fixed;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .overlay{
        background: rgba(0, 0, 0, 0.5);
        position: fixed;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
    }
    .modal-card{
        background: #FFFFFF;
        padding: 50px;
        position: fixed;
        top: 10%;
    }
    
    .exit-button{
        position: absolute;
        top: 0px;
        right: 5px;
        padding: 0;
        border: none;
        background: none;
    }

    .title{
        position: absolute;
        top: 5px;
        left: 0%;
        right: 0%;
        font-size: 20px;
        border-bottom: 1px solid black;
    }
    /* table, th, td{border:1px solid black;} */
    
    
</style>