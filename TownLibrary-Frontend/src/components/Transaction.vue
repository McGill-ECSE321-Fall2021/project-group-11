<template>
    <div id="transaction">
        <h2> Transactions </h2>
        <span v-if="errorTransaction" style="color:#de482b">{{ errorTransaction }} </span>
            <table id="transactions">
                <tr class="type-header">
                    <th><!-- Empty cell just for aligning the table --></th>
                    <th>Category</th>
                    <th>Name</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th colspan="2">Modify</th>
                </tr>
                <tr v-for="(transaction, index) in transactions" :key="transaction.id" class="infoheader">
                    <td >   </td>
                    <td class="info"> {{ transaction.type }} </td>
                    <td class="info"> {{ items[index].name }} </td>
                    <td class="info"> {{ fromUTCtoString(transaction.startDate)}} </td>
                    <td class="info"> {{ fromUTCtoString(transaction.endDate) }} </td>
                    <td class="info">
                        <button class='modify'
                        @click="renewItem(transaction,$route.params.id)">Renew</button>
                        <button class='modify'
                        @click="returnItem(transaction, $route.params.id)">Return</button>
                    </td>
                </tr>
            </table>
        <div>
            <button @click="$router.push('/profile')">Back</button>
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
                    this.errorTransaction = error
                }
            }
        },

        async returnItem(transaction, id){
            var response = '';
            this.isAfterDate(transaction.endDate)
            if (this.errorTransaction === "")
            {
                try {
                    let response = await AXIOS.delete('/transactions/' + id + '/' + transaction.id , null)
                    this.reloadTransactions(id)
                } catch (error) {
                    this.errorTransaction = error.response.status
                }
            }
        },
        isAfterDate(date){
            var today = new Date();
            var transactionDate = new Date(date);
            if (today > transactionDate) {
                this.errorTransaction = 'Action not possible';
            }
        },
        fromUTCtoString(time){
            var date = new Date(time).toString().split(' ')
            var displayDate = date[1] + ' ' + date[2] + ',' + date[3]
            return displayDate;
        }

    }
}


</script>

<style>

#transactions {
    border-collapse: collapse;
    border: 5px outset rgba(0, 0, 0, 0.8);
    border-left: 2px outset white;
    border-top: 2px outset white;
    background-color:#bfbfc1;
    color: black;
    margin-left:auto;
    margin-right:auto;
    width: 50%;
    margin-bottom: 20px;
    table-layout: auto;
}


.type-header{
        vertical-align: center;
        font-size: 20px;
        font-weight: bolder;
        border-bottom: 3px outset rgba(0, 0, 0, 0.80);

    }

.info {
    padding-top: 3px;
    padding-bottom: 3px;
}


</style>
