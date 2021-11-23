<template>
    <div id="calendar">
    <form>
    <table>
        <tr>
            <td class="timeCol unselectable"></td>
            <th>Mon</th>
            <th>Tue</th>
            <th>Wed</th>
            <th>Thu</th>
            <th>Fri</th>
            <th>Sat</th>
            <th>Sun</th>
        </tr>
        <tr><th>6:00-7:00</th>
            <!-- <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td> -->
            <td v-for="n in 7" :key="n">
                <input type="checkbox" v-if="false" then checked>
                <input type="checkbox" v-else>
            </td>
        </tr>
        <tr><th>7:00-8:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr><th>8:00-9:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr><th>9:00-10:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>    
        </tr>
        <tr><th>10:00-11:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr><th>11:00-12:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr><th>12:00-13:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr><th>13:00-14:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr><th>14:00-15:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr><th>15:00-16:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr><th>16:00-17:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr><th>17:00-18:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr><th>18:00-19:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr><th>19:00-20:00</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
    </table>
    <input type="submit">
    <input type="reset">
    </form>
    <!-- <button class="reset-button">Reset</button>
    <button class="apply-button">Apply</button> -->
    
    </div>
</template>

<script>
export default {
    name: "calendar",
    
    // props: {
    //     entity: Object
    // },

    data: {
        times:[
            // ["6:00","7:00"],
            // ["7:00","8:00"],
            // ["8:00","9:00"],
            // ["9:00","10:00"],
            // ["10:00","11:00"],
            // ["11:00","12:00"],
            // ["12:00","13:00"],
            // ["13:00","14:00"],
            // ["14:00","15:00"],
            // ["15:00","16:00"],
            // ["16:00","17:00"],
            // ["17:00","18:00"],
            // ["18:00","19:00"],
            // ["19:00","20:00"]
            ["1","2"],
            ["3","4"],
            ["5","6"],
            ["7","8"],
            ["9","10"],
            ["1","1"],
            ["1","1"]
        ]
        
           
    }

}
</script>

<style>
    
    td {
        cursor:pointer;
        border: 1px solid #999;
        text-align: center; 
    }
    table{
        width: 800px;
        border: 2px solid #000;
    }
    .timeCol{
        width: 80px;
    }
    th {
        width: 50px;
        border: 1px solid #999;
        text-align: center;
    }

    .unselectable{
        -webkit-touch-callout: none;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
        visibility: hidden;
    }

</style>