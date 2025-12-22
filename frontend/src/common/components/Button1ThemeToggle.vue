<script setup>
import { ref } from 'vue';
import Button from 'primevue/button';

const isDarkMode = ref(false);

const toggleDarkMode = () => {
    isDarkMode.value = !isDarkMode.value;
    document.documentElement.classList.toggle('dark');
}
</script>

<template>
    <Button 
        :label="isDarkMode ? '🌙 Modo Oscuro' : '☀️ Modo Claro'" 
        @click="toggleDarkMode"
        :class="[
            'transition-all duration-300',
            'hover:scale-105 active:scale-95',
            isDarkMode 
                ? 'bg-gray-800 hover:bg-gray-700 border-gray-700' 
                : 'bg-blue-500 hover:bg-blue-600 border-blue-400'
        ]"
        :severity="isDarkMode ? 'secondary' : 'primary'"
        :icon="isDarkMode ? 'pi pi-moon' : 'pi pi-sun'"
        outlined
        rounded
    />
</template>