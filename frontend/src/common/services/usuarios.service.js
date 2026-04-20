// common/services/usuarios.service.js
import BaseService from '@/common/services/base.service'

class UsuariosService extends BaseService {
  constructor() {
    super('/usuarios')
  }

  async getBuscar(params) {
      return await this.custom('get', '/q/buscar', null, { params });
  }

}

export default new UsuariosService()
