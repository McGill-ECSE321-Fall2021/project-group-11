<template>
    <div id="personal">
        <h2> Account Information </h2>
        <div id="info">
            <label class="title"> User ID </label>
            <label class="textbox"> {{ this.$route.params.id }} </label> <br>
            <label class="title"> Full Name </label>
            <label class="textbox"> {{ this.$route.params.fullName }} </label> <br>
            <label class="title"> Email </label>
            <label class="textbox">  {{ this.$route.params.email }} </label> <br>
            <label class="title"> Address </label>
            <label class="textbox">  {{ this.$route.params.address }}</label> <br>
            <label class="title"> In Town? </label>
            <label class="textbox">  {{ this.$route.params.inTown }} </label> <br>
        </div>

        <div>
            <button @click="$router.push('/profile')">Return</button>
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
    width: 25%;
}

.textbox {
    display: block;
    padding-left: 5px;
    border: 1px solid black;
    border-collapse: collapse;
    text-align: left;
    margin-block: 10px;
    margin: auto;
    width: 25%;
}


</style>
