import { createApp } from 'vue'
import './style.css'
import router from './router'
import App from './App.vue'

import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(App)

app.use(router)

// Pinia for state management
app.use(createPinia())

// Element Plus for UI Componenets
app.use(ElementPlus)

app.mount('#app')

