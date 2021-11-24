<template>
    <div id="transaction">
        <h2> Transactions </h2>

        <div>
            <button @click="$router.push('/profile')">Return</button>
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

    name: "transaction",
    data() {
        return{
            transactions: []
        }
    },

    methods: {

        async getUserTransactions(id) {
            AXIOS.get('/Transactions/')
        }
    }
}


</script>