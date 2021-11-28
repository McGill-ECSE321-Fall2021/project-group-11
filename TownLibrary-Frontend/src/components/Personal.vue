<template>
    <div id="personal">
        <h2> Account Information </h2>
        <br> 
        <br>
        <div id="info">
            <label class="title"> User ID </label>
            <label class="textbox"> {{ this.$route.params.id }} </label> <br>
            <label class="title"> Full Name </label>
            <label class="textbox"> {{ this.$route.params.fullName }} </label> <br>
            <label class="title"> Email </label>
            <label class="textbox">  {{ this.$route.params.email }} </label> <br>
            <label class="title"> Address </label>
            <label class="textbox">  {{ this.$route.params.address }}</label> <br>
            <label class="title"> In Town </label>
            <label class="textbox">  {{ this.$route.params.inTown }} </label> <br>
        </div>

        <div>
            <button @click="$router.push('/profile')">back</button>
        </div>

    </div>

</template>


<script>
import Login from './Login.vue'
export default {
  components: { Login },
    name: 'Personal'
}
</script>

<style scoped>

.title {
    display: block;
    text-align: left;
    margin: auto;
    width: 50%;
    color: black;
    font-weight: bold;
}

.textbox {
    color: black;
    display: block;
    padding-left: 5px;
    background-color: white;
    border: 2px outset white;
    border-left: 2.5px outset rgba(0, 0, 0, 0.7);
    border-top: 2.5px outset rgba(0, 0, 0, 0.7);
    text-align: left;
    margin-block: 10px;
    margin: auto;
    width: 50%;
}

#info{
  display: block;
  padding-top: 10px;
  margin: auto;
  margin-bottom: 10px;
  background-color: #bfbfc1;
  width: 30%;
  height: 50%;
  border: 5px outset rgba(0, 0, 0, 0.8);
  border-left: 2px outset white;
  border-top: 2px outset white;
}
</style>
