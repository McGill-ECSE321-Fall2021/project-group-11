import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store/index.js'
import Hello from '@/components/Hello'
import Login from '@/components/Login'
import Profile from '@/components/Profile'
import Personal from '@/components/Personal'
import Transaction from '@/components/Transaction'
import SetupLibrary from '@/components/SetupLibrary'
import CreateLibrarian from '@/components/CreateLibrarian'
import CreateOnlineAccount from '@/components/CreateOnlineAccount'
import CreateOfflineAccount from '@/components/CreateOfflineAccount'
import Homepage from '@/components/Homepage'
import CreateEvent from '@/components/CreateEvent'
import Events from '@/components/Events'
import CreateItem from '@/components/CreateItem'
import BrowseItem from '@/components/BrowseItem'
import Item from '@/components/Item'
import Sink from '@/components/Sink'

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
    },
    {
      path: '/create-event',
      name: 'Create Event',
      component: CreateEvent
    },
    {
      path: '/events',
      name: 'Events',
      component: Events
    },
    {
      path: '/additem',
      name: 'Create Item',
      component: CreateItem
    },
    {
      path: '/browseitem',
      name: 'Browse Item',
      component: BrowseItem
    },
    {
      path: '/item',
      name: 'Item',
      component: Item
    },

    {
      path: '/profile/transactions',
      name: 'User Transactions',
      component: Transaction
    },

    {
      path: '/profile/personal',
      name: 'Personal Information',
      component: Personal
    },

    // Must be the last route
    {
      path: '/*',
      name: '404',
      component: Sink
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
  case 'Item':
  case 'Create Event':
    // must be logged in (doesn't matter who)
    return userType !== null
  case 'Personal Information':
    // must be a online-member
    return userType === 'online-member'
  case 'Create Librarian':
    // must be a head-librarian
    return userType === 'head-librarian'
  case 'Create Offline Account':
  case 'Create Item':
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
    next({
      name: 'Login',
      params: {
        redirect: to.name,
        with: to.params
      }
    })
  else if (to.name == 'Login' && store.state.loginStatus)
    next({ name: 'User Profile' })
  else
    next()
})

// Export it...
export default router
