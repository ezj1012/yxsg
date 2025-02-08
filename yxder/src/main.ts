import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './app/router'
import '@vscode/codicons/dist/codicon.css'
import { installMsg } from './app/directives'
import '@/app/monaco'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(installMsg)
app.mount('#app')
