import { computed } from "vue";
import { PERMISOS } from "../constants/permisos";
import { MODULOS } from "../constants/modulos";
import ACCIONES_ROOT from "./root.acciones";

import { useAuthStore } from "../stores/authStore";
const auth = useAuthStore();

const ACCIONES_MODULO_USUARIOS = computed(() => ({
  ...ACCIONES_ROOT.value,
  view: auth.hasPermiso(MODULOS.USUARIOS, PERMISOS.USUARIOS.VIEW),
  create: auth.hasPermiso(MODULOS.USUARIOS, PERMISOS.USUARIOS.CREATE),
  read: auth.hasPermiso(MODULOS.USUARIOS, PERMISOS.USUARIOS.READ),
  update: auth.hasPermiso(MODULOS.USUARIOS, PERMISOS.USUARIOS.UPDATE),
  delete: auth.hasPermiso(MODULOS.USUARIOS, PERMISOS.USUARIOS.DELETE),
}));

export default ACCIONES_MODULO_USUARIOS;
