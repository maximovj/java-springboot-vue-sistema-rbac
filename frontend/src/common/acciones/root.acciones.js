import { computed } from "vue";
import { useAuthStore } from "../stores/authStore";
const auth = useAuthStore();

const ACCIONES_ROOT = computed(() => ({
  root: auth.esSuperAdministrador,
}));

export default ACCIONES_ROOT;
