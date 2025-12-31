// common/services/autenticacion.service.js
import BaseService from '@/common/services/base.service'
import { useSettingsStore } from '@/common/stores/settingsStore'
import { useUiStore } from '../stores/uiStore'
import { useAlertStore } from '../stores/alertStore'
import { useAuthStore } from '../stores/authStore'

class AutenticacionService extends BaseService {
  constructor() {
    super('/autenticacion')
  }

  async login(usuario, contrasena, recuerdame) {
    const ui = useUiStore();
    ui.loading = true;

    return this.custom('post', '/login', { usuario, contrasena, recuerdame })
      .then(res => {
        const settings = useSettingsStore();
        settings.recuerdame = recuerdame;

        const auth = useAuthStore();
        auth.loguearse(
          res.data?.contenido?.acceso_token,
          res.data?.contenido?.info_usuario,
        );
        return res
      })
      .catch( err => {
        const alert = useAlertStore();
        alert.alert({ 
          title:'Iniciar sesión',
          message: err.response.data?.error || err.message,
        })
      })
      .finally(() => {
        ui.loading = false;
      })
  }

  async logout() {
    const auth = useAuthStore()
    return this.custom('post', '/logout', auth.usuario)
      .finally(() => auth.desloguearse())
  }

  refresh() {
    return this.custom('post', '/refresh')
  }
}

export default new AutenticacionService()
