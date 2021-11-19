import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import Login from '@/components/Login'
import CreateAccount from '@/components/CreateAccount'
import SetupLibrary from '@/components/SetupLibrary'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Hello',
      component: Hello
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/newacc',
      name: 'Create Account',
      component: CreateAccount
    },
    {
      path: '/setup',
      name: 'Setup Library',
      component: SetupLibrary
    }
  ]
})
