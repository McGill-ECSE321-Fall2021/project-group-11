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
                <tr v-for="(time,idx1) in timeslot" :key="time"> 
                    <th>{{time}}</th>
                    <td v-for="n in dayOfWeek.length" :key="n">
                        
                        <!-- the boolean false here is to check whether that timeslot is selected for schedule -->
                        <!-- true means there's a shift at that timeslot, false means otherwise -->
                            <input type="checkbox" v-if="!false && user === 'head-librarian'">
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
    <!-- <button class="reset-button">Reset</button>
    <button class="apply-button">Apply</button> -->
    </div>
</template>

<script>
import 'jquery'
import $ from 'jquery'

export default {
    name: "calendar",
    
    props: {
        user: {
            type: String,
            required: true,
        },
        userSchedule:{
            type: Array,
            required: true,
            default: []
        }
    },

    data() {
        return{
            dayOfWeek: ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"],
            timeslot:[
                "6:00-7:00", 
                "7:00-8:00",
                "8:00-9:00",
                "9:00-10:00",
                "10:00-11:00",
                "11:00-12:00",
                "12:00-13:00",
                "13:00-14:00",
                "14:00-15:00",
                "15:00-16:00",
                "16:00-17:00",
                "17:00-18:00",
                "18:00-19:00",
                "19:00-20:00"
            ],      
        }        
    },
    
    mounted: function(){
        $(".calendar-table>tbody").selectable({
            filter: 'td',
            stop: function() {
                $("input").attr('checked',false);
                $("input", this).each(function(){
                    this.checked = true;
                });
            }
        });
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