import { createRouter, createWebHistory } from 'vue-router'
import EventListTable from '@/components/events/List/EventListTable.vue'
import SportEventDetail from '@/components/events/Details/SportEventDetail.vue'

const routes = [
  {
    path: '/',
    name: 'EventList',
    component: EventListTable
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
