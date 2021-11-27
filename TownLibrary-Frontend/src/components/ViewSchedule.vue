<template>
    <div id="view-schedule">
        <button @click="open=true">View schedule</button>
        <div class="modal-block" v-if="open">
            <div class="overlay"></div>
            <div class="modal-card card">
                <h1 class="title"> <b>{{scheduleOwner.toUpperCase()}}'S SCHEDULE</b> </h1>
                <button class="exit-button" v-on:click="open=false">x</button>
                <body class="content">
                    <calendar :user="loginStatus.userType" :entity-id="entityId"> </calendar>
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
            required: true
        },
        // could prob use an object instead 
        entityId:{
            type: Number,
            required: true
        },
        scheduleOwner:{
            type: String,
            default: "Library"
        }

    },

    components:{
        'calendar': Calendar
    },

    data(){
        return{
            open: false,  
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
        background-color:#3B77BC;
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
        text-shadow:0 0 3px #DE482B, 0 0 3px #DE482B,0 0 3px #DE482B;
    }
    .title{
        position: absolute;
        top: 5px;
        left: 0%;
        right: 0%;
        font-size: 25px;
        user-select: none;
        /* border-bottom: 1px solid black; */
    }
    /* table, th, td{border:1px solid black;} */
    
    
</style>