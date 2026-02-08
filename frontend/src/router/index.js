// router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import { useSettingsStore } from '@/common/stores/settingsStore'
import { useAuthStore } from '@/common/stores/authStore'
import { PERMISOS } from '@/common/constants/permisos'
import { useAlertStore } from '@/common/stores/alertStore'

import { scopedLogger } from '@/common/utils/loggerUtils'
import { MODULOS } from '@/common/constants/modulos'
const logger = scopedLogger("router::index.js");

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/acceder',
      name: 'acceder',
      component: () => import('@/views/AccederView.vue'),
      meta: { requiresGuest: true }
    },
    {
      path: '/panel',
      name: 'panel',
      component: () => import('@/views/PanelView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/usuarios',
      name: 'usuarios',
      component: () => import('@/views/UsuariosView.vue'),
      meta: { requiresAuth: true, modulo: MODULOS.USUARIOS, permiso: PERMISOS.USUARIOS.VIEW }
    },
    {
      path: '/configuracion',
      name: 'configuracion',
      component: () => import('@/views/ConfiguracionView.vue'),
      meta: { requiresAuth: true }
    },
    { path: '/:pathMatch(.*)*', redirect: '/acceder'},
  ],
})

router.beforeEach(async (to, from, next) => {
  const auth = useAuthStore();

  // 1. Inicializar auth una sola vez
  if (!auth.inicializado) {
    try {
      await auth.init("router>index.js::beforeEach : Inicializar datos...");
    } catch (error) {
      logger.error("router.beforeEach::catch", 'Error inicializando autenticación:', error);
    } finally {
      auth.inicializado = true
    }
  }

  if(auth.esSuperAdministrador == false) {
    // 2. Rutas que requieren autenticación
    if (to.meta.requiresAuth && !auth.estaAutenticado) {
      return next({ name: 'acceder' })
    }

    // 3. Rutas solo para invitados
    if (to.meta.requiresGuest && auth.estaAutenticado) {
      return next({ name: 'panel' })
    }

    // 4. 🔐 Validación de permisos (NUEVO)
    if (to.meta.modulo && to.meta.permiso) {
      // Seguridad extra por si permisos aún no existen
      if (!auth.hasPermiso || !auth.hasPermiso(to.meta.modulo, to.meta.permiso)) {
        const customAlert = useAlertStore();
        await customAlert.alert({
          title: 'ACCESO NO AUTORIZADO',
          message: 'No tiene permiso suficiente',
        }); 
        return next({ name: 'panel' }) // o 403 si prefieres
      }
    }
  }

  next()
})


export default router