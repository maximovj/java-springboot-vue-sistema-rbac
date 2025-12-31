// main.js - Versión simple
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import pinia from './plugins/pinia'
import CommonPlugin from './plugins/common'

import PrimeVue from "primevue/config";
import SportYellowPreset from './themes/SportYellowTheme'
import '@styles/main.css'
import '@styles/theme-primevue.css'
import '@styles/gradients.css'

const app = createApp(App)

app.use(pinia)
app.use(CommonPlugin)
app.use(PrimeVue, {
  ripple: true,
  theme: {
    preset: SportYellowPreset,
    options: {
      prefix: 'p',
      darkModeSelector: '[data-theme="dark"]',
      cssLayer: false
    }
  }
})

app.use(router)
app.mount('#app')