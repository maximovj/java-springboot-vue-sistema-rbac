// common/stores/settingsStore.js
import { defineStore } from 'pinia'

export const useSettingsStore = defineStore('settings', {
    state: () => ({
        recuerdame: false,
        tema: 'light',
    }),

    getters: { },

    actions: { },

    persist: {
        pick: [
            'recuerdame',
            'tema',
        ],
        storage: localStorage
    }
})
