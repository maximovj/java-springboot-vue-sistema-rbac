<template>
  <GenericDataTable
    :value="value"
    :filters="filters"
    :rows="rows"
    :loading="loading"
    :totalRecords="totalRecords"
    :first="first"
    @page="$emit('page', $event)"
  >
    <Column field="permiso_id" header="ID" sortable style="width: 80px" />

    <Column field="accion" header="Acción" sortable />
    
    <Column field="modulo" header="Módulo" sortable />

    <Column field="es_activo" header="Estado" style="width: 120px">
      <template #body="{ data }">
        <Tag
          :value="estadoLabel(data.es_activo)"
          :severity="estadoSeverity(data.es_activo)"
        />
      </template>
    </Column>

    <Column field="creado_en" header="Creación" style="width: 150px">
      <template #body="{ data }">
        {{ formatDate(data.creado_en) }}
      </template>
    </Column>

    <Column field="actualizado_en" header="Actualización" style="width: 150px">
      <template #body="{ data }">
        {{ formatDate(data.actualizado_en) }}
      </template>
    </Column>

    <Column header="Acciones" style="width: 180px">
      <template #body="{ data }">
        <div class="flex gap-2">
          <EditarPermiso :permiso="data" @guardar="mtdEmitirActualizar" />
          
          <Button
            icon="pi pi-trash"
            severity="danger"
            rounded
            text
            @click="$emit('delete', data)"
          />
        </div>
      </template>
    </Column>
  </GenericDataTable>
</template>

<script>
import Column from 'primevue/column'
import Button from 'primevue/button'
import Tag from 'primevue/tag'

export default {
  name: 'TablaPermisos',

  components: {
    Column,
    Button,
    Tag
  },

  props: {
    value: { type: Array, required: true },
    filters: Object,
    rows: Number,
    loading: Boolean,
    totalRecords: Number,
    first: Number
  },

  emits: ['page', 'actualizar', 'delete'],

  methods: {
    formatDate(date) {
      return date ? new Date(date).toLocaleDateString() : ''
    },

    estadoLabel(value) {
      return value ? 'Activo' : 'Inactivo'
    },

    estadoSeverity(value) {
      return value ? 'success' : 'danger'
    },

    mtdEmitirActualizar(args) {
      this.$emit('actualizar', args);
    }
  }
}
</script>
