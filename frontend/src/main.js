import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

//import PrimeVuePlugin from '@plugins/primevue'
import PrimeVue from "primevue/config";
import SportYellowPreset from './themes/SportYellowTheme'
import '@styles/main.css';              // estilos globales
//import '@styles/theme-light.css';     // variables light
//import '@styles/theme-dark.css';      // variables dark
import '@styles/primevue-theme.css';    // variables dark
import '@styles/gradients.css';         // degradados para botones/cards

import CommonPlugin from './plugins/common'

const app = createApp(App)

app.use(createPinia())

app.use(router)

app.use(PrimeVue, {
  ripple: true,
  theme: {
    preset: SportYellowPreset,
    options: {
      prefix: 'p',
      darkModeSelector: 'none', // O 'system' si quieres que cambie automáticamente
      cssLayer: false
    }
  }
})

app.use(CommonPlugin)

app.mount('#app')
