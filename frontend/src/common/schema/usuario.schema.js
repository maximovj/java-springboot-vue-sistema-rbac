import * as yup from "yup";

// Esquema de validación
export const UsuarioSchema = yup.object({
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