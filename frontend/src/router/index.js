// router/index.js
import { createRouter, createWebHistory } from 'vue-router'

import { useAuthStore } from '@/common/stores/authStore'
import { useAlertStore } from '@/common/stores/alertStore'

import { PERMISOS } from '@/common/constants/permisos'
import { MODULOS } from '@/common/constants/modulos'

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
      path: '/permisos',
      name: 'permisos',
      component: () => import('@/views/PermisosView.vue'),
      meta: { requiresAuth: true, modulo: MODULOS.PERMISOS, permiso: PERMISOS.PERMISOS.VIEW }
    },
    {
      path: '/configuracion',
      name: 'configuracion',
      component: () => import('@/views/ConfiguracionView.vue'),
      meta: { requiresAuth: true }
    },
    { 
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('@/views/Error404.vue')
    },
  ],
});

router.beforeEach(async (to) => {
  const auth = useAuthStore();

  if (!auth.inicializado) {
    try {
      await auth.init("router>index.js::beforeEach");
    } finally {
      auth.inicializado = true;
    }
  }

  if (to.name === 'not-found') {
    return auth.estaAutenticado
      ? { name: 'panel' }
      : { name: 'acceder' }
  }

  if (to.meta.requiresAuth && !auth.estaAutenticado) {
    return { name: 'acceder' }
  }

  if (to.meta.requiresGuest && auth.estaAutenticado) {
    return { name: 'panel' }
  }

  if (!auth.esSuperAdministrador && to.meta.modulo && to.meta.permiso) {
    if (!auth.hasPermiso?.(to.meta.modulo, to.meta.permiso)) {
      const customAlert = useAlertStore();
      await customAlert.alert({
        title: 'ACCESO NO AUTORIZADO',
        message: 'No tiene permiso suficiente',
      });
      return { name: 'panel' }
    }
  }
});

export default router