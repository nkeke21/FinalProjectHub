import { createRouter, createWebHistory } from 'vue-router'
import EventListPage from '@/components/events/List/EventListPage.vue'
import SportEventDetail from '@/components/events/Details/SportEventDetail.vue'
import SignInSignupPage from '@/components/auth/SignInSignupPage.vue'
import AboutPage from '@/components/about/AboutPage.vue' 

const routes = [
  {
    path: '/',
    name: 'Auth',
    component: SignInSignupPage
  },
  {
    path: '/',
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
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
