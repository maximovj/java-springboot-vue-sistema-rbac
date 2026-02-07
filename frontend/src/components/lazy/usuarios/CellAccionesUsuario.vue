<!-- AccionesCell.vue -->
<script setup>
defineProps({ row: Object });
import { scopedLogger } from '@/common/utils/loggerUtils';


const logger = scopedLogger("CellAccionesUsuario.vue");

const editar = () => { logger.info("editar", "row.usuario_id", row.usuario_id) }
const eliminar = () => { logger.info("eliminar", "row.usuario_id", row.usuario_id) }
</script>

<template>
  <EditarUsuarios :usuarioId="row.usuario_id" />
  <Button icon="pi pi-trash" class="p-button-text p-button-danger" />
</template>
