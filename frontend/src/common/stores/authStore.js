// common/stores/authStore.js
import { defineStore } from 'pinia'
import { useSettingsStore } from './settingsStore';
import { useAlertStore } from './alertStore';
import autenticacionService from '../services/autenticacion.service';

import { scopedLogger } from '../utils/loggerUtils';
const logger = scopedLogger("authStore.js");
let initPromise = null;

export const useAuthStore = defineStore('auth', { 
  state: () => ({
    acceso_token: null,   // SOLO memoria
    usuario_id: null,
    usuario: null,
    grupo: null,
    rol: null,
    esAdmin: false,
    permisos: [],

    inicializado: false,
  }),

  getters: {
    estaAutenticado(state) {
      return !!state.acceso_token
    },

    esSuperAdministrador(state) {
      return state.esAdmin
    },

    hasPermiso(state) {
      return (modulo, accion) => {
        // Super admin: acceso total
        if (this.esSuperAdministrador) return true;

        return (
          Array.isArray(state.permisos) &&
          state.permisos.some(
            p =>
              p.modulo === modulo &&
              p.accion === accion
          )
        )
      }
    }
  },

  actions: {
    async init(msg = 'authStore.js :: init') {
      logger.info("init", { msg });

      const settings = useSettingsStore();

      // Si ya terminó correctamente, no repetir
      if (this.inicializado) return;

      // Si ya está ejecutándose, devolver la misma promesa
      if (initPromise) {
        return initPromise;
      }

      initPromise = (async () => {
        try {

          if (settings.recuerdame || settings.usuario) {
            const renovarToken = await autenticacionService.refresh();
            logger.info("init", "renovarToken", renovarToken);

            if (renovarToken.status == 200) {
              const { contenido } = renovarToken.data;

              this.loguearse(
                contenido?.acceso_token,
                contenido?.info_usuario
              );

              settings.recuerdame = true;
            }
          }

        } catch (e) {
          logger.error("init::catch", { e });

          await autenticacionService.logout();

          const alert = useAlertStore();
          await alert.alert({
            title: 'AVISO',
            message: 'Desloguerse (p. 1)',
          });

        } finally {
          this.inicializado = true;
          initPromise = null; // liberar
        }
      })();

      return initPromise;
    },
    loguearse(acceso_token, usuario_info) {
      const settings = useSettingsStore();
      settings.recuerdame = usuario_info?.recuerdame;
      settings.usuario = usuario_info?.usuario;

      this.acceso_token = acceso_token;
      this.esAdmin = usuario_info?.es_admin;
      this.usuario_id = usuario_info?.usuario_id;
      this.usuario = usuario_info?.usuario;
      this.grupo = usuario_info?.grupo;
      this.rol = usuario_info?.rol;
      this.permisos = usuario_info?.permisos;
    },

    desloguearse() {
      this.$reset();

      const settings = useSettingsStore();
      settings.$reset();
    },

    async logout() {
      await autenticacionService.logout();
      this.$reset();

      const settings = useSettingsStore();
      settings.recuerdame = false;
    },

  },
});