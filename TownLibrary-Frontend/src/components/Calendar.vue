<template>
    <div id="calendar">
    <form>
        <table id="calendar-table">
            <thead>
                <tr>
                    <th></th>
                    <th v-for="day in dayOfWeek" :key="day"> {{day}} </th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(time,idx1) in timeslot" :key="idx1"> 
                    <th>{{time.startTime}}-{{time.endTime}}</th>
                    <td v-for="n in dayOfWeek.length" :key="n" id="data">
                        
                        <!-- the boolean false here is to check whether that timeslot is selected for schedule -->
                        <!-- true means there's a shift at that timeslot, false means otherwise -->
                        <!-- ADD SOMETHING FOR SELECTION PROCESS -->
                            <input type="checkbox" 
                            :id="[n, idx1+1]" :value="checkboxLocation(n, idx1+1)"
                            v-if="!false && user === 'head-librarian'">
                            <input type="checkbox" :id="[n, idx1+1]" v-else-if="user === 'head-librarian'" then checked>
                            <input type="checkbox" v-if="!false && user !== 'head-librarian'" then onclick="return false;">
                            <input type="checkbox" v-else-if="user !== 'head-librarian'" then checked onclick="return false;">
                    </td>
                </tr>
            </tbody>
        </table>
    <!-- change this later -->
    <!-- <input type="submit">  -->
    <input type="submit" v-if="user === 'head-librarian'">
    <input type="reset" v-if="user === 'head-librarian'">
    </form>
    <!-- <button class="reset-button">Reset</button> -->
    <button class="apply-button" @click="getSelected">Apply</button>
    <!-- <h1> {{
        userSchedule.then((response) => response.json()).then((library) => {
            console.log(library)
        })
        }} </h1> -->

    
    
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
    name: "calendar",
    
    props: {
        user: {
            type: String,
            required: true,
        },
        userSchedule:{
            type: Promise,
            required: true,
            default: Promise.reject()
        },
        userId:{
            type: Number,
            default: 0
        }
    },

    data() {
        return{
            dayOfWeek: ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"],
            timeslot:[
                {startTime: "06:00", endTime: "07:00"},
                {startTime: "07:00", endTime: "08:00"},
                {startTime: "08:00", endTime: "09:00"},
                {startTime: "09:00", endTime: "10:00"},
                {startTime: "10:00", endTime: "11:00"},
                {startTime: "11:00", endTime: "12:00"},
                {startTime: "12:00", endTime: "13:00"},
                {startTime: "13:00", endTime: "14:00"},
                {startTime: "14:00", endTime: "15:00"},
                {startTime: "15:00", endTime: "16:00"},
                {startTime: "16:00", endTime: "17:00"},
                {startTime: "17:00", endTime: "18:00"},
                {startTime: "18:00", endTime: "19:00"},
                {startTime: "19:00", endTime: "20:00"}
            ],
            checkboxes: [],      
        }        
    },

    methods:{
        // add method to check if schedule already in
        // add method to put (update)
        // add method to delete
        async getSelected(id){
            var checkboxArray = []
            var checkboxes = document.querySelectorAll('input[type=checkbox]:checked')
            for (var checkbox of checkboxes){
                checkboxArray.push(checkbox)
                // array.push(Object.values(checkbox)[0])
                // document.body.append(checkbox.value.day+' ');
                // var x = JSON.parse(JSON.stringify(checkbox.value))
                // console.log(Object.values(checkbox)[0].time.startTime)
 
            }
            // this.checkboxes = checkboxes
            try {
                for(var day in this.dayOfWeek){
                    console.log(this.dayOfWeek[day])
                    var earliestStartTime = "24:00"
                    var latestEndTime = ""
                    for(var n=0; n < checkboxArray.length; n++){
                        var scheduleCell = Object.values(checkboxArray[n])[0]
                        if (scheduleCell.day == this.dayOfWeek[day]) {
                            console.log("startTime: "+scheduleCell.time.startTime)
                            console.log("endTime: "+scheduleCell.time.endTime)
                        }
                      
                        if (this.dayOfWeek[day] == scheduleCell.day) {
                            var currentStartTime = scheduleCell.time.startTime
                            var currentEndTime = scheduleCell.time.endTime

                            if (currentStartTime < earliestStartTime){
                                earliestStartTime = currentStartTime
                            }
                            if (currentEndTime > latestEndTime){
                                latestEndTime = currentEndTime
                            }
                        }
                    }
                    
                    
                    if (earliestStartTime != "24:00" && latestEndTime != ""){
                        console.log("early: "+earliestStartTime)
                        console.log("late: "+latestEndTime)
                        
                        if (id == 0){
                            let request = await axios.post('/schedules/library/'+id+'/'+this.dayOfWeek[day], null ,
                            {
                                    params:{
                                        startTime : earliestStartTime,
                                        endTime : latestEndTime
                                    }
                            })
                            if (request.status == '200'){
                                console.log("poggers")
                            }else{
                                console.log("pepehands")
                            }

                        } else {

                        }

                        

                    }
                }
                    
            } catch (error) {
                console.log(error)
            }
        },
        checkboxLocation(dayInput, timeInput){
            var dayString = ''
            var timeObject = {}
            
            dayString = this.dayOfWeek[dayInput-1]
            timeObject = this.timeslot[timeInput-1]
        
            let location = {
                "day":dayString,
                "time":timeObject
            }
            return location
        }

    },
    
    mounted() {
        // $(".calendar-table>tbody").selectable({
        //     filter: 'td',
        //     stop: function() {
        //         $("input").attr('checked',false);
        //         $("input", this).each(function(){
        //             this.checked = true;
        //         });
        //     }
        // });
        var targetBox = document.getElementById("data");
        targetBox.onpointerdown = handleDown;

        function handleDown(){
            targetBox="checked";

        }
    }

}
</script>

<style scoped>
    
    td {
        border: 1px solid #999;
        align-content: center;
        vertical-align: middle;
    }
    table{
        width: 1200px;
        height: 600px;
        border: 2px solid #000;
    }
    th {
        width: 150px;
        height: 40px;
        border: 1px solid #999;
        text-align: center;
    }
    input[type='checkbox']{
        -webkit-appearance: none;
        width:100%;
        height:100%;
        float:left;
        margin: 0 auto;

    }
    input[type='checkbox']:checked{
        background: #abc;
    }
    /* .unselectable{
        -webkit-touch-callout: none;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
        visibility: hidden;
    } */

</style>