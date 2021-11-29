<template>
    <!-- button to access schedule + modal -->
    <div id="view-schedule">
        <button @click="open=true">View schedule</button>
        <div class="modal-block" v-if="open">
            <div class="overlay"></div>
            <div class="modal-card card">
                <h1 class="title"> <b>{{scheduleOwner.toUpperCase()}}'S SCHEDULE</b> </h1>
                <button class="exit-button" v-on:click="open=false">x</button>
                <body class="content">
                    <calendar :user="loginStatus && loginStatus.userType" :entity-id="entityId"> </calendar>
                </body>
            </div>
        </div>
    </div>
</template>

<script>
import Calendar from './Calendar.vue'

export default {
    name: "view-schedule",
    props: {
        loginStatus:{
            type: Object,
            // required: true
            default: {}
        },
        // could prob use an object instead 
        entityId:{
            type: Number,
            required: true
        },
        scheduleOwner:{
            type: String,
            default: "Library"
        },

    },

    components:{
        'calendar': Calendar
    },

    data(){
        return{
            open: false,  
        }
    },
}

</script>

<style scoped>

    #view-schedule{
        z-index: 100000;
    }

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
        z-index: 10000;
        position: fixed;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
    }
    .modal-card{
        z-index: 99999999;
        background-color: #bfbfc1;
        border: 5px outset black;
        border-left: 2px outset white;
        border-top: 2px outset white;
        padding: 50px;
        position: fixed;
        top: 10%;
    }
    .exit-button{
        position: absolute;
        top: 0px;
        right: 10px;
        padding: 0;
        border: none;
        background: none;
    }
    .title{
        position: absolute;
        top: 5px;
        left: 0%;
        right: 0%;
        font-size: 25px;
        font-weight:300;
        user-select: none;
        color:black;
  
    }
    
    
</style>