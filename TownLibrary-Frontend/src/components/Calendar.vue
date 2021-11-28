<template>
    <div id="calendar">
    <form>
        <table id="calendar-table">
            <!-- header table row -->
            <thead>
                <tr>
                    <th id="empty"></th>
                    <th v-for="day in dayOfWeek" :key="day"> {{day}} </th>
                </tr>
            </thead>
            <!-- table body -->
            <tbody>
                <!-- each row -->
                <!-- time header | data cell | data cell | data cell | ...   -->
                <tr v-for="(time,idx1) in timeslot" :key="idx1"> 
                    <th>{{time.startTime}}-{{time.endTime}}</th>
                    <!-- each data cell is just a big checkbox that contains value (day, {startTime, endTime}) -->
                    <td v-for="n in dayOfWeek.length" :key="n" id="data">
                        <!-- reads entity's schedule and checks (colours cell) if there's a schedule at that timeslot -->
                        <!-- if user is head-librarian, can modify -->
                        <!-- <div id="test"> -->
                        <!-- <label> -->
                        <input type="checkbox" 
                            :id="[n, idx1+1]" :value="checkboxLocation(n, idx1+1)"
                            v-if="
                            !checkIfWithinRange(
                            checkSchedulesByDay(n).startTime,
                            checkSchedulesByDay(n).endTime,
                            time.startTime,
                            time.endTime) 
                            && user === 'head-librarian'">
                        <input type="checkbox" :id="[n, idx1+1]" v-else-if="user === 'head-librarian'" then checked>
                        
                        <!-- if not, cannot modify/interact with checkboxes -->
                        <input type="checkbox" 
                            v-if="
                            !checkIfWithinRange(
                            checkSchedulesByDay(n).startTime,
                            checkSchedulesByDay(n).endTime,
                            time.startTime,
                            time.endTime) 
                            && user !== 'head-librarian'" then onclick="return false;">
                        <input type="checkbox" v-else-if="user !== 'head-librarian'" then checked onclick="return false;">
                        <!-- </label> -->
                        <!-- </div> -->

                    </td>
                </tr>
            </tbody>
        </table><br>
    <button class="submit-button" @click="requestSelected(entityId)" v-if="user === 'head-librarian'">Submit</button>
    <input class="reset-button" id="reset" type="reset" v-if="user === 'head-librarian'">
    <button class="clear-button" @click="clearCheckboxes()" v-if="user === 'head-librarian'">Clear</button>
    </form>
    </div>
</template>

<script>
import axios from 'axios'
import { isEmptyObject, $ } from 'jquery'

var frontendUrl =  process.env.FRONTEND_HOST + ':' + process.env.FRONTEND_PORT
var backendUrl = process.env.API_HOST + ':' + process.env.API_PORT

var AXIOS = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
    name: "calendar",
    
    props: {
        // user who is currently logged in
        user: {
            type: String,
            // required: true,
            default: 'no-user'
        },
        // entity (schedule's owner) id
        entityId:{
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

            schedules: [],

            schedulesByDay: {
                MONDAY: {},
                TUESDAY: {},
                WEDNESDAY: {},
                THURSDAY: {},
                FRIDAY: {},
                SATURDAY: {},
                SUNDAY: {}

            }
        }        
    },

    async created() {  
        this.getSchedule()     
    },

    methods:{
        /** Creates, updates or delete schedules depending on cells highlighted (checkbox checked)
         * 
         * @param id - entity's id (for example, a library or librarian)
         */
        // prob should add stuff to handle exceptions
        async requestSelected(id){
            // get all checked checkboxes
            var checkboxArray = []
            var checkboxes = document.querySelectorAll('input[type=checkbox]:checked')
            for (var checkbox of checkboxes){
                checkboxArray.push(checkbox)
            }
            try {
                // each day, look for the start time and end time
                for(var day in this.dayOfWeek){
                    // temp values
                    var earliestStartTime = "24:00"
                    var latestEndTime = ""

                    // iterate through all the checked cells
                    for(var n=0; n < checkboxArray.length; n++){
                        var scheduleCell = Object.values(checkboxArray[n])[0]
                      
                        // if that cell is during that day
                        // look for earliest start time and latest end time
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
                    // get the schedule id of the day schedule in the system
                    var scheduleId = this.checkSchedulesByDay(this.dayOfWeek[day]).id

                    // if temp values unchanged, then nothing checked for that day
                    // if something is checked, then post or put requests
                    if (earliestStartTime != "24:00" && latestEndTime != ""){
                        try{
                            // attempt post (create schedule) first
                            // for library
                            if (id == 0){
                                await AXIOS.post('/schedules/library/0/'+this.dayOfWeek[day], null ,
                                {
                                    params:{
                                        startTime : earliestStartTime,
                                        endTime : latestEndTime
                                    }
                                })

                            // for librarians
                            } else {
                                await AXIOS.post('/schedules/librarian/'+ id +'/'+this.dayOfWeek[day], null ,
                                {
                                    params:{
                                        startTime : earliestStartTime,
                                        endTime : latestEndTime
                                    }
                                })
                            }

                        }catch (error){
                            // if post cannot be made because there is already a schedule for that day,
                            // then put request (update schedule)
                            if (error.response.data == "ALREADY-SCHEDULE-ON-DAY"){
                                try {
                                    // if the new and old times are not the same, then do put request
                                    // else do nothing
                                    if (!(this.checkSchedulesByDay(this.dayOfWeek[day]).startTime == earliestStartTime+":00" &&
                                    this.checkSchedulesByDay(this.dayOfWeek[day]).endTime == latestEndTime+":00")){
                                        await AXIOS.put('/schedules/'+scheduleId, null,{
                                            params:{
                                                newStartTime : earliestStartTime,
                                                newEndTime : latestEndTime
                                            }
                                        })
                                    }

                                } catch (error) {
                                    console.log(error)
                                }
                            }
                        }
                        
                    // no checked boxes, so no schedule on that day
                    }else{
                        // if the schedule exists, delete request
                        // else keep as is
                        if (!isEmptyObject(this.checkSchedulesByDay(this.dayOfWeek[day]))){
                            await AXIOS.delete('/schedules/'+scheduleId)
                        }
                    }
                }
                this.getSchedule()
                var resetButton = document.getElementById("reset")
                resetButton.click() 
                setTimeout(function() {
                    window.alert("Schedule successfully updated!")
                },100) 
                
            } catch (error) {
                window.alert("Something went wrong.")
            }
        },

        /** Returns an object with properties "day" and "time".
         * Used to give a value to checkbox given their location in the table
         * 
         * @param dayInput - index for dayOfWeek
         * @param timeInput - index for timeslot
         */
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
        },

        /** Checks whether the time cell fits within a time slot.
         * 
         * @param startTime - time slot startTime
         * @param endTime - time slot endTime
         * @param currentStartTime - time cell startTime
         * @param currentEndTime - time cell endTime
         */
        checkIfWithinRange(startTime, endTime, currentStartTime, currentEndTime){
            // probably add something to check for null input
            return (startTime<=currentStartTime+":00" && endTime >= currentEndTime+":00")
        },

        /** Return a schedule on a day
         * 
         * @param day - day of week in integer/string 
         */
        // wait what the hell, I had a GET request for this :)
        checkSchedulesByDay(day){
            switch(day){
                case 'MONDAY':
                case 1:
                    return this.schedulesByDay.MONDAY
                case 'TUESDAY':
                case 2:
                    return this.schedulesByDay.TUESDAY
                case 'WEDNESDAY':
                case 3:
                    return this.schedulesByDay.WEDNESDAY
                case 'THURSDAY':
                case 4:
                    return this.schedulesByDay.THURSDAY
                case 'FRIDAY':
                case 5:
                    return this.schedulesByDay.FRIDAY
                case 'SATURDAY':
                case 6:
                    return this.schedulesByDay.SATURDAY
                case 'SUNDAY':
                case 7:
                    return this.schedulesByDay.SUNDAY
                default:
                    console.log("invalid input for checkSchedulesByDay")
                    break
            }
        },

        /** Clears all the checkbox
         *  Taken directly from stackoverflow from user T J 
         *  stackoverflow.com/questions/22574344/how-to-make-a-button-that-will-uncheck-all-checkboxes/22574416
         */
        clearCheckboxes(){
            document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => checkbox.checked = false)
        },
        
        /** Get a schedule in database

         */
        async getSchedule(){
            this.schedules=[]
            this.schedulesByDay={
                MONDAY: {},
                TUESDAY: {},
                WEDNESDAY: {},
                THURSDAY: {},
                FRIDAY: {},
                SATURDAY: {},
                SUNDAY: {}
            }

            try {
            // these are to get schedule (and store in this.schedules)
            // for library
            if (this.entityId === 0){
                const request = await AXIOS.get('/schedules/library/0')
                this.schedules = request.data
            }
            // for librarian
            else{
                const request = await AXIOS.get('/schedules/librarian/' + this.entityId)
                this.schedules = request.data
            }

            // this is to sort schedule by day (by storing in object this.schedulesByDay)
            for (var i in this.schedules){
                switch(this.schedules.at(i).dayOfWeek){
                    case 'MONDAY':
                        this.schedulesByDay.MONDAY = this.schedules.at(i)
                        break
                    case 'TUESDAY':
                        this.schedulesByDay.TUESDAY = this.schedules.at(i)
                        break
                    case 'WEDNESDAY':
                        this.schedulesByDay.WEDNESDAY = this.schedules.at(i)
                        break
                    case 'THURSDAY':
                        this.schedulesByDay.THURSDAY = this.schedules.at(i)
                        break
                    case 'FRIDAY':
                        this.schedulesByDay.FRIDAY = this.schedules.at(i)
                        break
                    case 'SATURDAY':
                        this.schedulesByDay.SATURDAY = this.schedules.at(i)
                        break
                    case 'SUNDAY':
                        this.schedulesByDay.SUNDAY = this.schedules.at(i)
                        break
                    default:
                        console.log("uh")   
                        break
                }
            }
            
        } catch (error) {
            console.log(error)
        }
        }
    },
    mounted(){
        // jquery causes and error, something with import
        // see if this works? 
        //https://stackoverflow.com/questions/659508/how-can-i-shift-select-multiple-checkboxes-like-gmail

    }

}
</script>

<style scoped>
    #calendar{
        background-color:#bfbfc1;
        font-family: 'Fixedsys', Helvetica, Arial, sans-serif;
    }

    #empty{
        border:none;
    }

    .reset-button{
        border: 3px outset rgba(0, 0, 0, 0.856);
        border-top:1px outset white;
        border-left:1px outset white;
        text-transform: capitalize; 
        color: black;
        background-color:#bfbfc1;
    }

    td {
        border: 2px outset black;
        align-content: center;
        vertical-align: middle;
    }
    table{
        width: 1200px;
        height: 600px;
    }
    th {
        width: 150px;
        height: 40px;
        border: 3px outset rgba(0, 0, 0, 0.856);
        border-top: 3px outset white;
        border-left: 3px outset white;
        text-align: center;
        user-select: none;
        color: black;
        font-weight: 100;
    }
    input[type='checkbox']{
        -webkit-appearance: none;
        width:100%;
        height:100%;
        float:left;
        margin: 0 auto;

    }
    input[type='checkbox']:checked{
        background: rgba(255, 217, 4, 0.7);
    }

   
</style>