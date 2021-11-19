import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store/index.js'
import Hello from '@/components/Hello'
import Login from '@/components/Login'
import Profile from '@/components/Profile'
import CreateAccount from '@/components/CreateAccount'
import SetupLibrary from '@/components/SetupLibrary'

Vue.use(Router)

// Setup the routes / endpoints
const router = new Router({
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
    },
    {
      path: '/profile',
      name: 'User Profile',
      component: Profile
    }
  ]
})

// Helper function that returns true if-and-only-if the name of the page needs
// the user to be already logged in.
function pageNeedsLogin(page) {
  switch (page.name) {
  case 'User Profile':
    return true
  default:
    return false
  }
}

// Custom global routing rules (such as redirecting to login if not-logged-in
// yet and stuff)
router.beforeEach((to, from, next) => {
  if (pageNeedsLogin(to) && !store.state.loginStatus)
    next({ name: 'Login' })
  else if (to.name == 'Login' && store.state.loginStatus)
    next({ name: 'User Profile' })
  else
    next()
})

// Export it...
export default router
