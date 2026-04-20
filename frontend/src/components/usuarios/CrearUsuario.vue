<script setup>
import { ref, watch } from 'vue';
import { useUsuarioForm } from './composables/useUsuarioForm';
import gruposService from "@/common/services/grupos.service";

import { scopedLogger } from "@/common/utils/loggerUtils";
const logger = scopedLogger("CrearUsuarios.vue");

const initialState = () => ({
    usuario: "",
    correo: "",
    es_activado: false,
    contrasena: "",
    confirmar_contrasena: "",
    grupos: null,
    rol: null,
});

const usuarioForm = ref(initialState());
const lstgrupos = ref([]);

const emits = defineEmits(["guardar", "cancelar"]);

const {
    visible,
    usuario,
    correo,
    es_activo,
    contrasena,
    confirmar_contrasena,
    grupo,
    errors,
    formularioVacio,
    abrirFormulario,
    guardarFormulario,
    cancelarFormulario,
} = useUsuarioForm(usuarioForm);

watch(visible, async (isVisible) => {
    if(!isVisible) {
        return;
    }

    const resGrupos = await gruposService.getAll();
    lstgrupos.value = resGrupos.data?.contenido?.content;
    logger.info("Grupos cargados:", lstgrupos.value);
});
</script>

<template>
    <Button label="Nuevo" icon="pi pi-plus" @click="abrirFormulario" />

    <CustomCuadroDialogo
        v-model="visible"
        tituloHeader="CREAR UN NUEVO USUARIO"
        :btnGuardarDisable="Object.keys(errors).length > 0 || formularioVacio"
        @guardar="guardarFormulario(emits)"
        @cancelar="cancelarFormulario(emits)"
    >
    <template #formulario>
        <Fieldset legend="Usuario" :toggleable="false">
            <InputText autofocus id="usuario" placeholder="Escribe usuario" v-model="usuario" fluid />
            <small v-if="errors.usuario" class="text-red-500">{{ errors.usuario }}</small>
        </Fieldset>
        
        <Fieldset legend="Correo electrónico" :toggleable="false">
            <InputText id="correo" placeholder="Escribe correo electrónico" v-model="correo" fluid />
            <small v-if="errors.correo" class="text-red-500">{{ errors.correo }}</small>
        </Fieldset>

        <Fieldset legend="Contraseña" :toggleable="false">
            <Password id="contrasena" placeholder="Escribe contraseña" v-model="contrasena" fluid toggleMask />
            <small v-if="errors.contrasena" class="text-red-500">{{ errors.contrasena }}</small>
        </Fieldset>

        <Fieldset legend="Confirmar contraseña" :toggleable="false">
            <Password id="confirmar_contrasena" placeholder="Confirma contraseña" v-model="confirmar_contrasena" fluid toggleMask />
            <small v-if="errors.confirmar_contrasena" class="text-red-500">{{ errors.confirmar_contrasena }}</small>
        </Fieldset>

        <Fieldset legend="Grupo de usuario" :toggleable="false">
            <label for="grupo" class="font-medium block mb-2">Selecciona un grupo</label>
            <Select 
                id="grupo"
                v-model="grupo"
                :options="lstgrupos"
                optionLabel="nombre"
                optionValue="grupo_id"
                placeholder="Selecciona un grupo"
                fluid
            />
            <small v-if="errors.grupo" class="text-red-500">{{ errors.grupo }}</small>
        </Fieldset>

        <Fieldset legend="Estado de cuenta" :toggleable="false">
            <div class="flex align-items-center justify-content-between">
                <InputSwitch 
                    id="es_activo" 
                    v-model="es_activo" 
                    :binary="true"
                />
                <label for="es_activo" class="ml-2 font-medium">Cuenta activada</label>
            </div>
            <small v-if="errors.es_activo" class="text-red-500">{{ errors.es_activo }}</small>
            <small class="text-gray-400 block mt-2">
                {{ es_activo ? 'La cuenta estará activa inmediatamente' : 'La cuenta se creará como inactiva' }}
            </small>
        </Fieldset>
    </template>
    </CustomCuadroDialogo>
</template>