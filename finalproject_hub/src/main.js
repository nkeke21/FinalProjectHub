import './assets/main.css'
import '@coreui/coreui/dist/css/coreui.min.css'
import '@coreui/icons/css/all.min.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createVfm } from 'vue-final-modal'
import router from './router'

import App from './App.vue'
const app = createApp(App)
const vfm = createVfm()
const pinia = createPinia();

app.use(router)
app.use(vfm)
app.use(pinia)
app.mount('#app')