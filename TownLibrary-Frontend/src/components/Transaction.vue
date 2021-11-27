<template>
    <div id="transaction">
        <h2> Transactions </h2>
            <table>
                <tr>
                    <th><!-- Empty cell just for aligning the table --></th>
                    <th>Transaction id</th>
                    <th>Type</th>
                    <th>Name</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Renew</th>
                    <th>Return</th>
                </tr>
                <tr v-for="(transaction, index) in transactions" :key="transaction.id">
                    <td>   </td>
                    <td> {{ transaction.id }} </td>
                    <td> {{ transaction.type }} </td>
                    <td> {{ items[index].name }} </td>
                    <td> {{ transaction.startDate }} </td>
                    <td> {{ transaction.endDate }} </td>
                    <td> Event </td>
                    <td>
                        <button
                        @click="renewItem(transaction,this.$route.params.id)">Renew</button>
                    </td>
                    <td>
                        <button
                        @click="returnItem(transaction, this.$route.params.id)">Return</button>
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
            items: [],
            errorTransaction: ''
        }
    },
    created: function() {
        this.reloadTransactions(this.$route.params.id)
    },

    methods: {
        async reloadTransactions(id) {
            try {
                let response  = await AXIOS.get('/transactions/' + id)
                this.transactions = response.data
            } catch (error) {
                this.errorTransaction  = e
            }
            for (transaction in this.transactions){
                this.getTransactionItem(transaction, id)
            }
        },

        async getTransactionItem(transaction){

            try {
                let response = await  AXIOS.get('/' + transaction.type + '/transactions/' + transaction.id)     
                this.items[this.transactions.indexOf(transaction)] = response.data
            } catch (error) {
                this.errorTransaction = e
            }
        },
        async renewItem(transaction, id){
            try {
                let response = await AXIOS.post('/transactions/' + id + '/' + transaction.id , null, {})
                this.reloadTransactions(id)
            } catch (error) {
                this.errorTransaction = e
            }
        },

        async returnItem(transaction, id){
            try {
                let response = await AXIOS.delete('/transactions/' + id + '/' + transaction.id , null)
                this.reloadTransactions(id)
            } catch (error) {
                this.errorTransaction = e
            }
        }

    }
}


</script>
