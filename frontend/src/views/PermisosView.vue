<template>
  <PlantillaBase>
  <div class="card">

    <!-- Header -->
    <CrudHeader
      title="Gestión de Permisos"
      subtitle="Resumen general de la información"
      :menu-items="menuItems"
      @create="crearPermiso"
      @refresh="aplicarBusqueda"
    />

    <!-- 🔎 Barra de filtros - Expandible -->
    <FiltrosPermisos
      v-model="searchForm"
      :loading="cargandoRegistros"
      :visible="filtersVisible"
      :estados="estados"
      :active-filters="activeFilters"
      @update:visible="filtersVisible = $event"
      @search="aplicarBusqueda"
      @clear="clearFilters"
    />

    <!-- 📋 Tabla -->
    <TablaPermisos
      :value="permisos"
      :filters="filters"
      :rows="dataTableRows"
      :loading="cargandoRegistros"
      :total-records="totalRecords"
      :first="first"
      @page="onPage"
      @edit="editarPermiso"
      @delete="eliminarPermiso"
    />
    
  </div>
  </PlantillaBase>
</template>


<script>
import { FilterMatchMode } from '@primevue/core/api'

import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import Select from 'primevue/select'
import Tag from 'primevue/tag'
import { now } from '@vueuse/core'
import permisosService from '@/common/services/permisos.service'

import { scopedLogger } from '@/common/utils/loggerUtils'
const logger = scopedLogger('PermisosView.vue');

export default {
  components: {
    DataTable,
    Column,
    InputText,
    Button,
    Select,
    Tag
  },

  data() {
    const fechaFin = new Date(now());
    const fechaInicio = new Date();
    fechaInicio.setFullYear(fechaFin.getFullYear() - 10);

    return {
      cargandoRegistros: false,

      dataTableRows: 10,
      first: 0,
      totalRecords: 0,

      page: 0,
      
      permisos: [],

      searchForm: {
        accion: null,
        modulo: null,
        es_activo: null,
        fecha_creacion: [fechaInicio, fechaFin],
      },

      filtersVisible: true,

      filters: {
        accion: { value: null, matchMode: FilterMatchMode.CONTAINS },
        modulo: { value: null, matchMode: FilterMatchMode.CONTAINS },
        es_activo: { value: null, matchMode: FilterMatchMode.EQUALS },
        fecha_creacion: { value: null, matchMode: FilterMatchMode.DATE_BETWEEN },
      },

      estados: [
        { label: 'Activo', value: true, severity: 'success' },
        { label: 'Inactivo', value: false, severity: 'danger' }
      ],

      menu: null,

      menuItems: [
        { label: 'Editar', icon: 'pi pi-pencil' },
        { label: 'Eliminar', icon: 'pi pi-trash' },
        { separator: true },
        { label: 'Exportar', icon: 'pi pi-upload' }
      ]
    }
  },

  computed: {
    optParams() {
      let params = {};
      if (this.searchForm?.accion) {
        params.accion = this.searchForm?.accion
      }
      if (this.searchForm?.modulo) {
        params.modulo = this.searchForm?.modulo
      }
      if (this.searchForm?.es_activo !== null) {
        params.es_activo = this.searchForm?.es_activo
      }

      if (this.searchForm?.fecha_creacion && this.searchForm?.fecha_creacion?.length === 2) {
        params.creado_desde = this.searchForm.fecha_creacion?.[0]?.toISOString()
        params.creado_hasta = this.searchForm.fecha_creacion?.[1]?.toISOString()
      }

      return params;
    },
    
    activeFilters() {
      return [
        this.searchForm.accion && {
          icon: 'pi pi-search',
          key: 'accion',
          label: 'Acción',
          value: this.searchForm.accion,
          onRemove: () => this.searchForm.accion = null
        },
        this.searchForm.modulo && {
          icon: 'pi pi-box',
          key: 'modulo',
          label: 'Módulo',
          value: this.searchForm.modulo,
          onRemove: () => this.searchForm.modulo = null
        },
      ].filter(Boolean)
    },
},

  methods: {
    toggleMenu(event) {
      this.$refs.menu.toggle(event)
    },

    aplicarBusqueda() {
      this.filtersVisible = false
      this.page = 0
      this.first = 0

      this.filters.accion.value = this.searchForm.accion
      this.filters.modulo.value = this.searchForm.modulo
      this.filters.es_activo.value = this.searchForm.es_activo

      // Para el filtro de fecha, verificamos que haya un rango válido
      if (this.searchForm.fecha_creacion && this.searchForm.fecha_creacion.length === 2) {
        this.filters.fecha_creacion.value = this.searchForm.fecha_creacion
      } else {
        this.filters.fecha_creacion.value = null
      }

      this.cargarPermisos()
    },

    clearFilters() {
      this.searchForm = {
        accion: null,
        modulo: null,
        es_activo: null,
      }
      //this.aplicarBusqueda()
    },

    getEstadoSeverity(value) {
      const estado = this.estados.find(e => e.value === value)
      return estado ? estado.severity : null
    },

    getEstadoLabel(value) {
      const estado = this.estados.find(e => e.value === value)
      return estado ? estado.label : value
    },

    editarPermiso(permiso) {
      console.log('Editar:', permiso)
    },

    eliminarPermiso(permiso) {
      console.log('Eliminar:', permiso)
    },

    crearPermiso() {
      console.log('Crear nuevo permiso')
    },

    formatDate(date) {
      if (!date) return ''
      return date.toLocaleDateString()
    },

    onPage(event) {
      this.page = event.page
      this.dataTableRows = event.rows
      this.cargarPermisos()
    },

    async cargarPermisos() {
      this.cargandoRegistros = true;  

      await permisosService.getBuscar({
          page: this.page,
          size: this.dataTableRows,
          ...this.optParams,
      })
        .then(response => {
          logger.info('cargarPermisos', 'Permisos cargados:', response.data);
          if(response.data?.exitosa) {
            const contenido = response.data?.contenido;
            const content = response.data?.contenido?.content || [];
            logger.info('cargarPermisos', `Permisos obtenidos: ${content.length}`, content);
            
            this.permisos = content;
            this.totalRecords = contenido?.totalElements || content.length;
            this.first = contenido?.page * contenido?.size;

          } else {
            logger.warn('cargarPermisos', 'Respuesta exitosa pero sin datos de permisos:', response.data);
          }
        })
        .catch(error => {
          console.error('Error al cargar permisos:', error)
        })
        .finally(() => {
          this.cargandoRegistros = false;
        });
    }
  },

  mounted() {
    this.cargarPermisos();
  }
}
</script>
