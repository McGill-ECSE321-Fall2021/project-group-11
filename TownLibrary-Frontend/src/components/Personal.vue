<template>
    <div id="personal">
        <h2> Personal Information </h2>
        <ul>
            <li>Library ID : {{ this.$route.params.id }} </li>
            <li>Fullname : {{ this.$route.params.fullName }} </li>
            <li>Email : {{ this.$route.params.email }} </li>
            <li>Address : {{ this.$route.params.address }} </li>
        </ul>

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
