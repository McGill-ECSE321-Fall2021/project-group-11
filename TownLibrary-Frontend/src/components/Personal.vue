<template>
    <div id="personal">
        <h2> Account Information </h2>
        <div id="info">
            <label class="title"> User ID </label>
            <label class="textbox"> {{ $store.state.loginStatus.userInfo.id }} </label> <br>
            <label class="title"> Full Name </label>
            <label class="textbox"> {{ $store.state.loginStatus.userInfo.name }} </label> <br>
            <label class="title"> Email </label>
            <label class="textbox">  {{ $store.state.loginStatus.userInfo.email }} </label> <br>
            <label class="title"> Address </label>
            <label class="textbox">  {{ $store.state.loginStatus.userInfo.address }}</label> <br>
            <label class="title"> In Town? </label>
            <label class="textbox">  {{ $store.state.loginStatus.userInfo.inTown }} </label> <br>
        </div>

        <div>
            <button @click="$router.push('/profile')">Return</button>
        </div>

    </div>

</template>


<script>
export default {
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
