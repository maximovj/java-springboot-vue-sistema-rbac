<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useRoute } from 'vue-router';
import { useAlertStore } from '@/common/stores/alertStore';
import autenticacionService from '@/common/services/autenticacion.service';
import { useAuthStore } from '@/common/stores/authStore';

// Referencias computadas
const opInfoUsuario = ref();
const router = useRouter();
const route = useRoute();
const showIconToolbar = ref(false);
const auth = useAuthStore();

// Métodos
const isActive = (path) => {
    return route.path.startsWith(path) ? 'p-button-primary' : ''
}

const verInfoUsuario = (event) => {
    opInfoUsuario.value.toggle(event)
}

const salir =  async () =>  { 
    const alert = useAlertStore();
    const desicion = await alert.confirm({ 
    message: '¿Seguro que deseas salir?',
    buttons: {visible: ["yes", "cancel"] }
    });

    if(desicion == 'yes') {
        await autenticacionService.logout();
        router.push('/');
    }
};

</script>

<template>
<!-- Toolbar principal -->
<div class="flex justify-between items-center p-4 bg-white dark:bg-gray-800 border-b dark:border-gray-700">

    <!-- Izquierda -->
    <div class="flex items-center gap-2 sm:gap-3">
    <Button
        icon="pi pi-th-large"
        class="p-button-text"
        @click="showIconToolbar = !showIconToolbar"
    />

    <!-- Oculto en mobile -->
    <div class="hidden sm:flex items-center gap-2">
        <Button icon="pi pi-search" class="p-button-text" />
        <input
        type="text"
        placeholder="Buscar..."
        class="px-3 py-1 border rounded dark:bg-gray-700 dark:border-gray-600 focus:outline-none"
        />
    </div>
    </div>

    

    <!-- Derecha -->
    <div class="flex items-center gap-2 sm:gap-3">
        <p>
            <span class="font-medium truncate">Usuario:&nbsp;</span>
            <span class="font-medium truncate">
                        {{ auth.usuario }}
            </span>
        </p>
        <Avatar 
            icon="pi pi-user" 
            class="mr-2" 
            size="large" 
            style="background-color: #ece9fc; color: #2a1261" 
            shape="circle" 
            @click="verInfoUsuario" />
        
        <Popover
            ref="opInfoUsuario"
            dismissable
            class="p-0 border-none shadow-2xl rounded-xl"
            >
            <div class="w-56 p-0">

                <!-- Info compacta -->
                <div class="px-4 py-3 border-b dark:border-gray-700">
               
                <p class="text-xs text-gray-500 truncate">
                    {{ auth.grupo }} · {{ auth.rol }}
                </p>
                </div>

                <!-- Acciones -->
                <div class="py-1">

                <button
                    class="w-full flex items-center gap-3 px-4 py-2 text-sm
                        hover:bg-gray-100 dark:hover:bg-gray-700
                        transition-colors"
                    @click="$router.push('/perfil')"
                >
                    <i class="pi pi-user text-gray-500"></i>
                    Mi perfil
                </button>

                <button
                    class="w-full flex items-center gap-3 px-4 py-2 text-sm
                        hover:bg-gray-100 dark:hover:bg-gray-700
                        transition-colors"
                    @click="$router.push('/configuracion')"
                >
                    <i class="pi pi-cog text-gray-500"></i>
                    Configuración
                </button>

                <div class="my-1 border-t dark:border-gray-700"></div>

                <button
                    @click="salir"
                    class="w-full flex items-center gap-3 px-4 py-2 text-sm
                        text-red-500 hover:bg-red-50
                        dark:hover:bg-red-900/30
                        transition-colors"
                >
                    <i class="pi pi-power-off"></i>
                    Cerrar sesión
                </button>

                </div>
            </div>
        </Popover>
    </div>
</div>

<!-- Toolbar flotante -->
<transition name="fade-slide">
    <div
    v-if="showIconToolbar"
    class="absolute left-0 right-0 top-full
            bg-white dark:bg-gray-800
            border-b dark:border-gray-700
            shadow-lg z-50"
    >
    <div class="flex justify-around sm:justify-start sm:gap-3 px-4 py-2">

        <RouterLink to="/panel">
        <Button
            icon="pi pi-home"
            class="p-button-text p-button-rounded"
            :class="isActive('/panel')"
            v-tooltip.bottom="'Dashboard'"
        />
        </RouterLink>

        <RouterLink to="/usuarios">
        <Button
            icon="pi pi-users"
            class="p-button-text p-button-rounded"
            :class="isActive('/usuarios')"
            v-tooltip.bottom="'Usuarios'"
        />
        </RouterLink>

        <RouterLink to="/permisos">
        <Button
            icon="pi pi-unlock"
            class="p-button-text p-button-rounded"
            :class="isActive('/permisos')"
            v-tooltip.bottom="'permisos'"
        />
        </RouterLink>
    </div>
    </div>
</transition>
</template>

<style>
.p-popover-content {
    padding: 0px !important;
}
</style>