<template>
    <div id="transaction">
        <h2> Transactions </h2>
            <table>
                <tr>
                    <th><!-- Empty cell just for aligning the table --></th>
                    <th>Transaction id</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Type</th>
                    <th>Renew</th>
                </tr>
                <tr v-for="transaction in transactions" :key="transaction.id">
                    <td>   </td>
                    <td> {{ transaction.id }} </td>
                    <td> {{ getTransactionObject(transaction.id) }} </td>
                    <td> {{ transaction.startDate }} </td>
                    <td> {{ transaction.endDate }} </td>
                    <td> Event </td>
                    <td>
                        <button @click="renewItem(transaction.id)">Renew</button>
                    </td>
                </tr>
            </table>
        <div>
            <button @click="$router.push('/profile')">Return</button>
        </div>

    </div>

</template>

<script>
import axios, { Axios } from 'axios'

var frontendUrl = process.env.FRONTEND_HOST + ':' + process.env.FRONTEND_PORT
var backendUrl = process.env.API_HOST + ':' + process.env.API_PORT

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {

    name: "transaction",
    data() {
        return{
            transactions: [],
            transactionObjects: [],
            type: [],
            errorTransaction: ''
        }
    },
    created: function() {

        this.reloadTransactions
    },

    methods: {
        async reloadTransactions(id) {
            AXIOS.get('/transactions/', {})
                .then(response => {
                    this.transactions = response.data
                })
                .catch(e => {
                    this.errorTransaction = e
                })
            if (!this.errorTransaction > 0)
            {
                for (transaction in this.transactions) {
                    if (transaction.userId !== id){
                        this.transactions.splice(this.transactions.indexOf(transaction), 1)
                    }
                }
            }

        },

        async getTransactionObjects(){
            for (transaction in this.transactions) {



            }
        },



        async renewTransactionObject(id){

        }
    }
}


</script>
