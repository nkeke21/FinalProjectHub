import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createVfm } from 'vue-final-modal'

import App from './App.vue'
const app = createApp(App)
const vfm = createVfm()
const pinia = createPinia();

app.use(vfm)
app.use(pinia)
app.mount('#app')