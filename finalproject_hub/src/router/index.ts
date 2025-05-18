import { createRouter, createWebHistory } from 'vue-router'
import EventListPage from '@/components/events/List/EventListPage.vue'
import SportEventDetail from '@/components/events/Details/SportEventDetail.vue'

const routes = [
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
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
