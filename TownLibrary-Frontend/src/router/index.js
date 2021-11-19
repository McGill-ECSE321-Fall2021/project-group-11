import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
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
      path: '/setup',
      name: 'Setup Library',
      component: SetupLibrary
    }
  ]
})
