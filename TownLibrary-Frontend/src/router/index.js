import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store/index.js'
import Hello from '@/components/Hello'
import Login from '@/components/Login'
import Profile from '@/components/Profile'
import SetupLibrary from '@/components/SetupLibrary'
import CreateLibrarian from '@/components/CreateLibrarian'
import CreateOnlineAccount from '@/components/CreateOnlineAccount'
import CreateOfflineAccount from '@/components/CreateOfflineAccount'
import Homepage from '@/components/Homepage'

Vue.use(Router)

// Setup the routes / endpoints
const router = new Router({
  routes: [
    {
      path: '/',
      name: 'Homepage',
      component: Homepage
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/newacc/librarian',
      name: 'Create Librarian',
      component: CreateLibrarian
    },
    {
      path: '/newacc/offline',
      name: 'Create Offline Account',
      component: CreateOfflineAccount
    },
    {
      path: '/newacc/online',
      alias: '/newacc',
      name: 'Create Online Account',
      component: CreateOnlineAccount
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
  case 'Create Offline Account':
    // must be a librarian
    return userType === 'librarian' || userType === 'head-librarian'
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
