import { createRouter, createWebHistory } from 'vue-router'
import EventListPage from '@/components/events/List/EventListPage.vue'
import SportEventDetail from '@/components/events/Details/SportEventDetail.vue'
import AboutPage from '@/components/about/AboutPage.vue' 
import AuthPageVue from '../components/auth/AuthPage.vue'
import ProfileComponent from '@/components/profile/ProfileComponent.vue'
import NotificationsPage from '@/components/notification/NotificationsPage.vue'

const routes = [
  {
    path: '/notifications',
    name: 'Notifications',
    component: NotificationsPage
  },
  {
    path: '/',
    name: 'Auth',
    component: AuthPageVue
  },
  {
    path: '/homepage',
    name: 'EventList',
    component: EventListPage
  },
  {
    path: '/events/:id',
    name: 'SportEventDetail',
    component: SportEventDetail,
    props: true
  },
  {
    path: '/about',
    name: 'About',
    component: AboutPage
  },
  {
    path: '/profile',
    name: 'Profile',
    component: ProfileComponent
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
