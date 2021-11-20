import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store/index.js'
import Hello from '@/components/Hello'
import Login from '@/components/Login'
import Profile from '@/components/Profile'
import SetupLibrary from '@/components/SetupLibrary'
import CreateAccount from '@/components/CreateAccount'
import CreateLibrarian from '@/components/CreateLibrarian'

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
      path: '/hire',
      name: 'Create Librarian',
      component: CreateLibrarian
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

function userCanAccessPage(page, userType) {
  // note that the user-type may be null!
  switch (page.name) {
  default:
    // assume all users (including non-logged in) can access the page.
    return true
  case 'User Profile':
    // must be logged in (doesn't matter)
    return userType !== null
  case 'Create Librarian':
    // must be a head-librarian
    return userType === 'head-librarian'
  }
}

// Custom global routing rules (such as redirecting to login if not-logged-in
// yet and stuff)
router.beforeEach((to, from, next) => {
  if (!userCanAccessPage(to, store.state.loginStatus && store.state.loginStatus.userType))
    // if insufficient priviledges, just send them over to login page (and let
    // login handle the other redirections)
    next({ name: 'Login' })
  else if (to.name == 'Login' && store.state.loginStatus)
    next({ name: 'User Profile' })
  else
    next()
})

// Export it...
export default router
