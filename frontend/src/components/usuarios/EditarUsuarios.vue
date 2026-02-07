<script setup>
import { ref, defineProps, watch } from "vue";
import { onMounted, onUpdated, onUnmounted } from "vue";
import usuariosService from "@/common/services/usuarios.service";

import { scopedLogger } from "@/common/utils/loggerUtils";
const logger = scopedLogger("EditarUsuarios.vue");

const props = defineProps({
    usuarioId: {
        type: Number,
        required: true,
        default: null
    }
});

const initialState = {
  visible: false,
  usuario: {},
  cargandoDatos: false,
  tituloCabecera: "EDITAR USUARIO",
  grupoSeleccionado: 1,
  grupos: [{ name: 'ADMINISTRADOR', code: 1 }]
};

const data = ref({ ...initialState });

function resetData() {
  data.value = { ...initialState };
}

onMounted(async () => {
    logger.info("onMounted","Componente montado");
});

onUpdated(() => {
  logger.info("onUpdated","Componente actualizado");
});

onUnmounted(() => {
  logger.info("onUnmounted","Componente destruido");
});

watch(() => data.value.visible, async (isVisible) => {
    if (!isVisible) {
        resetData(); // reset solo al cerrar
        return;
    }

    data.value.cargandoDatos = true;
    const res = await usuariosService.getById(props.usuarioId);
    logger.info("watch", {res});
    
    if(res.data?.exitosa) {
        data.value.usuario = res.data?.contenido;
        logger.info("watch::if", "data.value.usuario", data.value.usuario);
        //data.value.tituloCabecera = "Usuario: " + data.value.usuario.usuario;
        data.value.grupoSeleccionado = data.value.usuario?.grupo?.usuario_grupo_id;
    }

    data.value.cargandoDatos = false;
});
</script>

<template>
        <Button icon="pi pi-pencil" class="p-button-text p-button-warning mr-2" @click="data.visible = true"  />
        <Dialog
            v-model:visible="data.visible"
            modal
            :header="data.tituloCabecera"
            :style="{ width: '50rem' }"
            :breakpoints="{ '1199px': '75vw', '575px': '90vw' }"
            >
                <span class="text-surface-500 dark:text-surface-400 block mb-8">
                Actualizar información del usuario
                </span>
                
                <CustomField label="Nombre de usuario" forId="usuario">
                <Skeleton v-if="data.cargandoDatos" />
                <InputText
                    v-else
                    id="usuario"
                    class="flex-auto"
                    v-model="data.usuario.usuario"
                />
                </CustomField>

                <CustomField label="Correo electrónico" forId="correo">
                <Skeleton v-if="data.cargandoDatos" />
                <InputText
                    v-else
                    id="correo"
                    class="flex-auto"
                    v-model="data.usuario.correo"
                />
                </CustomField>

                <Divider align="center" type="dotted">
                <b>Opciones avanzadas</b>
                </Divider>

                <CustomField label="¿Es activo?">
                <Skeleton v-if="data.cargandoDatos" width="12rem" />
                <ToggleSwitch v-else v-model="data.usuario.es_activo" />
                </CustomField>

                <CustomField label="Seleccione un grupo">
                <Skeleton v-if="data.cargandoDatos" width="12rem" />
                <Select
                    v-else
                    :disabled="!data.usuario.es_activo"
                    v-model="data.grupoSeleccionado"
                    :options="data.grupos"
                    optionValue="code"
                    optionLabel="name"
                />
                </CustomField>
                <template #footer>
                    <Button type="button" label="Cancelar" severity="secondary" @click="data.visible = false"></Button>
                    <Button type="button" label="Guardar" @click="data.visible = false"></Button>
                </template>
        </Dialog>
</template>

