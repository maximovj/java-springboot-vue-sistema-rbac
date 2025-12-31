// common/stores/authStore.js
import { defineStore } from 'pinia'
import { useSettingsStore } from './settingsStore';
import { useAlertStore } from './alertStore';
import autenticacionService from '../services/autenticacion.service';

export const useAuthStore = defineStore('auth', { 
  state: () => ({
    acceso_token: null,   // SOLO memoria
    usuario_id: null,
    usuario: null,
    grupo: null,
    rol: null,
    permisos: [],

    inicializado: false,
  }),

  getters: {
    estaAutenticado: (state) => !!state.acceso_token,
    esAdministrador: (state) => state.rol === 'ADMIN',
  },

  actions: {
    async init(msg = 'authStore.js :: linea 19') {
      console.log(msg);

      const settings = useSettingsStore();
      if(this.inicializado && settings.recuerdame) return;
      
      try {
        if(settings.recuerdame) {
          const renovarToken = await autenticacionService.refresh();
          
          if(renovarToken.status == 200) {
            const { contenido } = renovarToken.data;
            
            this.loguearse(
              contenido?.acceso_token,
              contenido?.info_usuario,
            );

            settings.recuerdame = true;
          }
        }
      } catch (e) {
        console.log("authStore.js hubo un error: ",e);
        await this.logout();
      } finally {
        this.inicializado = true;
      }
    },

    loguearse(acceso_token, usuario_info) {
      this.acceso_token = acceso_token;
      this.usuario_id = usuario_info?.usuario_id;
      this.usuario = usuario_info?.usuario;
      this.grupo = usuario_info?.grupo;
      this.rol = usuario_info?.rol;
      this.permisos = usuario_info?.permisos;
    },

    desloguearse() {
      this.$reset()
    },

    async logout() {
      await autenticacionService.logout();
      this.$reset();

      const settings = useSettingsStore();
      settings.recuerdame = false;
    },

  },
});