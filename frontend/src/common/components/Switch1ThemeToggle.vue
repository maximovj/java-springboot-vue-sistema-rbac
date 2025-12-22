<script setup>
import { ref, onMounted } from 'vue';
import ToggleSwitch from 'primevue/toggleswitch';
import { useStorage } from '@vueuse/core';

// Guardar preferencia en localStorage
const isDarkMode = useStorage('dark-mode', false);

onMounted(() => {
    if (isDarkMode.value) {
        document.documentElement.classList.add('dark');
    }
});

const toggleDarkMode = () => {
    isDarkMode.value = !isDarkMode.value;
    document.documentElement.classList.toggle('dark');
}
</script>

<template>
    <div class="flex items-center space-x-4 p-4 bg-white dark:bg-gray-900 rounded-lg shadow-lg transition-colors duration-300">
        <i class="pi pi-sun text-yellow-500 text-xl"></i>
        
        <ToggleSwitch 
            v-model="isDarkMode"
            @change="toggleDarkMode"
            class="[&>div]:bg-gray-300 [&>div]:dark:bg-gray-700"
        />
        
        <i class="pi pi-moon text-blue-400 text-xl"></i>
        
        <span class="text-gray-700 dark:text-gray-300 font-medium">
            {{ isDarkMode ? 'Oscuro' : 'Claro' }}
        </span>
    </div>
</template>