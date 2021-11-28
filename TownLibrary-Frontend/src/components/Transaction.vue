<template>
    <div id="transaction">
        <h2> Transactions </h2>
        <span v-if="errorTransaction" style="color:red">Error : {{ errorTransaction }} </span>
            <table id="transactions">
                <tr>
                    <th><!-- Empty cell just for aligning the table --></th>
                    <th>Type</th>
                    <th>Name</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Renew</th>
                    <th>Return</th>
                </tr>
                <tr v-for="(transaction, index) in transactions" :key="transaction.id">
                    <td>   </td>
                    <td> {{ transaction.type }} </td>
                    <td> {{ items[index].name }} </td>
                    <td> {{ transaction.startDate }} </td>
                    <td> {{ transaction.endDate }} </td>
                    <td>
                        <button
                        @click="renewItem(transaction,$route.params.id)">Renew</button>
                    </td>
                    <td>
                        <button
                        @click="returnItem(transaction, $route.params.id)">Return</button>
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
            errorTransaction: 'Hello World'
        }
    },
    created: function() {
        this.reloadTransactions(this.$route.params.id)
    },

    methods: {
        async reloadTransactions(id) {
            try {
                let response  = await AXIOS.get('/transactions/' + id)
                let listOfTransactions = response.data

                for (let transaction in listOfTransactions){
                    await this.getTransactionItem(listOfTransactions[transaction], transaction)
                }

                // Update this last to trigger a reactive change!
                this.transactions = listOfTransactions
            } catch (error) {
                this.errorTransaction  = error
            }
        },

        async getTransactionItem(transaction, index){
            try {
                let response = await  AXIOS.get('/' + transaction.type + '/transactions/' + transaction.id)
                this.items[index] = response.data
            } catch (error) {
                this.errorTransaction = error
            }

        },
        async renewItem(transaction, id){

            this.isAfterDate(transaction.endDate)
            if (this.errorTransaction === "")
            {
                try {
                    let response = await AXIOS.put('/transactions/' + id + '/' + transaction.id , null, {})
                    this.reloadTransactions(id)
                } catch (error) {
                    this.errorTransaction = e
                }
            }
        },

        async returnItem(transaction, id){
            this.isAfterDate(transaction.endDate)
            if (this.errorTransaction === "")
            {
                try {
                    let response = await AXIOS.delete('/transactions/' + id + '/' + transaction.id , null)
                    this.reloadTransactions(id)
                } catch (error) {
                    this.errorTransaction = e
                }
            }
        },
        isAfterDate(date){
            var today = new Date();
            var transactionDate = new Date(date);
            if (today > transactionDate) {
                this.errorTransaction = 'Action not possible';
            }
        }

    }
}


</script>

<style>

#transactions {
    margin-left: auto;
    margin-right: auto;
    border: 1px solid black;
    border-collapse: collapse;
    max-width: 800px;
}

</style>
