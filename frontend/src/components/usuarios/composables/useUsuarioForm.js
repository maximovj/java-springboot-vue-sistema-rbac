// src/components/permisos/composables/useUsuarioForm.js
import { ref, computed } from "vue";
import { useForm, useField } from "vee-validate";
import * as yup from "yup";

export function useUsuarioForm(usuarioForm) {
  const visible = ref(false);

  // Esquema de validación
  const schema = yup.object({
    usuario: yup
      .string()
      .required("El campo usuario es obligatorio")
      .min(3, "Mínimo 3 caracteres")
      .max(20, "Máximo 20 caracteres")
      .matches(/^[a-zA-Z][a-zA-Z0-9._-]*$/, "Debe comenzar con una letra y solo puede contener letras, números, puntos, guiones bajos o medios"),
    correo: yup
      .string()
      .required("El campo correo es obligatorio")
      .max(160, "Máximo 160 caracteres")
      .email("El campo debe ser un correo electrónico"),
    grupo: yup
      .number()
      .required("Selecciona un grupo")
      .positive("El grupo debe ser válido")
      .integer("El grupo debe ser un número entero"),
    contrasena: yup
      .string()
      .required("El campo contraseña es obligatorio")
      .min(8, "Mínimo 8 caracteres")
      .max(30, "Máximo 30 caracteres")
      .matches(/[A-Z]/, "Debe contener al menos una letra mayúscula")
      .matches(/[a-z]/, "Debe contener al menos una letra minúscula")
      .matches(/[0-9]/, "Debe contener al menos un número")
      .matches(/[@$!%*?&]/, "Debe contener al menos un carácter especial (@$!%*?&)"),
    confirmar_contrasena: yup
      .string()
      .required("El campo confirmar contraseña es obligatorio")
      .oneOf([yup.ref('contrasena'), null], "Las contraseñas no coinciden"),
    es_activo: yup
      .boolean()
      .required("El campo estado de cuenta es obligatorio")
      .nullable(false)
      .defined(),
  });

  // Inicializamos el formulario
  const { handleSubmit, resetForm, errors } = useForm({
    validationSchema: schema,
    initialValues: {
      usuario: usuarioForm.value?.usuario || "",
      correo: usuarioForm.value?.correo || "",
      contrasena: usuarioForm.value?.contrasena || "",
      confirmar_contrasena: usuarioForm.value?.confirmar_contrasena || "",
      es_activo: usuarioForm.value?.es_activado || false,
      grupo: usuarioForm.value?.grupos || null,
      rol: usuarioForm.value?.rol || null,
    }
  });

  // Campos individuales
  const { value: usuario } = useField("usuario");
  const { value: correo } = useField("correo");
  const { value: es_activo } = useField("es_activo");
  const { value: rol } = useField("rol");
  const { value: grupo } = useField("grupo");
  const { value: contrasena } = useField("contrasena");
  const { value: confirmar_contrasena } = useField("confirmar_contrasena");

  // Computed para cambios y vacío
  const formularioVacio = computed(() => {
    return !usuario.value?.trim() && 
          !correo.value?.trim() && 
          !contrasena.value?.trim() &&
          !grupo.value &&
          !rol.value;
  });
  
  const formularioConCambios = computed(() => {
    return usuario.value?.trim() !== (usuarioForm.value?.usuario || "").trim() ||
          correo.value?.trim() !== (usuarioForm.value?.correo || "").trim() ||
          contrasena.value?.trim() !== (usuarioForm.value?.contrasena || "").trim() ||
          confirmar_contrasena.value?.trim() !== (usuarioForm.value?.confirmar_contrasena || "").trim() ||
          es_activo.value !== (usuarioForm.value?.es_activado || false) ||
          grupo.value !== (usuarioForm.value?.grupos || null) ||
          rol.value !== (usuarioForm.value?.rol || null);
  });

  const abrirFormulario = () => {
    visible.value = true;
    resetForm({
      values: {
        usuario: usuarioForm.value?.usuario || "",
        correo: usuarioForm.value?.correo || "",
        contrasena: usuarioForm.value?.contrasena || "",
        confirmar_contrasena: usuarioForm.value?.confirmar_contrasena || "",
        es_activo: usuarioForm.value?.es_activado || false,
        rol: usuarioForm.value?.rol || null,
        grupo: usuarioForm.value?.grupos || null,
      }
    });
  };

  // CORREGIDO: guardarFormulario debe retornar el handleSubmit
  const guardarFormulario = (emit) => {
    handleSubmit((values) => {
      emit("guardar", {
        datos: values,
        esVacio: formularioVacio.value,
        esDiferente: formularioConCambios.value
      });
      visible.value = false;
    })();
  };

  const cancelarFormulario = (emit) => {
    emit("cancelar");
    visible.value = false;
  };

  return {
    visible,
    usuario,
    correo,
    contrasena,
    confirmar_contrasena,
    es_activo,
    rol,
    grupo,
    errors,
    formularioVacio,
    formularioConCambios,
    abrirFormulario,
    guardarFormulario,
    cancelarFormulario
  };
}