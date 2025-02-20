import { createApp } from 'vue'
import './style.css'
import App from './App.vue'

import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(App)

// Pinia for state management
app.use(createPinia())

// Element Plus for UI Componenets
app.use(ElementPlus)

app.mount('#app')

