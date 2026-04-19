<template>
  <PlantillaBase>
    <!-- Cards -->
    <div class="grid grid-cols-1 sm:grid-cols-3 gap-6">
          <div class="bg-white dark:bg-gray-800 p-4 rounded">
            <p class="text-gray-500 text-sm">Usuarios</p>
            <p class="text-xl font-semibold">1,245</p>
          </div>
          <div class="bg-white dark:bg-gray-800 p-4 rounded">
            <p class="text-gray-500 text-sm">Ventas</p>
            <p class="text-xl font-semibold">$34,210</p>
          </div>
          <div class="bg-white dark:bg-gray-800 p-4 rounded">
            <p class="text-gray-500 text-sm">Productos</p>
            <p class="text-xl font-semibold">320</p>
          </div>
    </div>

    <!-- Tabla -->
    <div class="bg-white dark:bg-gray-800 p-4 rounded">
          <h2 class="text-md font-medium mb-2">Usuarios recientes</h2>
          <CustomTablaGenerica :data="usuarios" :columns="tablaColumnas" :loading="cargandoDatos" />
    </div>
  </PlantillaBase>
</template>

<script>
import usuariosService from '@/common/services/usuarios.service';
import CellAccionesUsuario from '@/components/lazy/usuarios/CellAccionesUsuario.vue';
import CellActivoUsuario from '@/components/lazy/usuarios/CellActivoUsuario.vue';
import { markRaw } from 'vue';

import { scopedLogger } from '@/common/utils/loggerUtils';
const logger = scopedLogger("UsuariosView.vue");

export default {
  // Definir propiedades
  data() {
    return {
      metaTitle: 'Usuarios',
      cargandoDatos: false,
      usuarios: [],
      tablaColumnas: [
        { field: 'usuario_id', header: 'ID' },
        { field: 'usuario', header: 'Nombre' },
        { field: 'correo', header: 'Correo' },
        { 
          field: 'es_activo', 
          header: 'Activo',
          cellComponent: markRaw(CellActivoUsuario),
        },
        {
          field: 'acciones',
          header: 'Acciones',
          cellComponent: markRaw(CellAccionesUsuario),
        }
      ],
    }
  },

  // Agregar componentes
  components: {},
  
  // Definir propiedades computadas
  computed: {
    route() {
      return this.$route
    }
  },

  // Definir métodos
  methods: {
    async cargarUsuarios() {
      try {
        this.cargandoDatos = true;
        const getAll = await usuariosService.getAll();
        logger.info("cargarUsuarios", "getAll", getAll);
        this.usuarios = getAll?.data?.contenido?.content || [];
      } catch (e) {
        console.error(e);
      } finally {
        this.cargandoDatos = false;
      }
    }
  },

  // Montaje
  mounted() {
    this.cargarUsuarios();
    this.$utils.setMetaTitleWelcome(this.metaTitle);
  }
}
</script>
