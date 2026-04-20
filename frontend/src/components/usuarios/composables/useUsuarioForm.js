// src/components/permisos/composables/useUsuarioForm.js
import { ref, computed } from "vue";
import { useForm } from "vee-validate";
import { UsuarioSchema } from "@/common/schema/usuario.schema";

export function useUsuarioForm() {
  const visible = ref(false);

  const initialValues = {
    usuario: "",
    correo: "",
    contrasena: "",
    confirmar_contrasena: "",
    es_activo: false,
    grupo: null,
    rol: null,
  };

  const {
    defineField,
    handleSubmit,
    resetForm,
    errors,
    meta,
    values,
  } = useForm({
    validationSchema: UsuarioSchema,
    initialValues,
  });

  // Fields correctamente ligados al form
  const [usuario] = defineField("usuario");
  const [correo] = defineField("correo");
  const [contrasena] = defineField("contrasena");
  const [confirmar_contrasena] = defineField("confirmar_contrasena");
  const [es_activo] = defineField("es_activo");
  const [grupo] = defineField("grupo");
  const [rol] = defineField("rol");

  // Computed reales basados en el estado del form
  const formularioVacio = computed(() => meta.value.initialValues === values);

  const formularioConCambios = computed(() => meta.value.dirty);

  const abrirFormulario = () => {
    visible.value = true;
    resetForm();
  };

  // Validación correcta
  const validarFormulario = handleSubmit((vals) => {
    return {
      datos: vals,
      esVacio: formularioVacio.value,
      esDiferente: formularioConCambios.value,
    };
  });

  return {
    visible,
    errors,
    resetForm,
    abrirFormulario,
    validarFormulario,
    formularioVacio,
    formularioConCambios,
    handleSubmit,

    // fields
    usuario,
    correo,
    contrasena,
    confirmar_contrasena,
    es_activo,
    grupo,
    rol,
  };
}