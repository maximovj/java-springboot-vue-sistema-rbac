<template>
  <div class="relative">
    <!-- 🔒 OVERLAY -->
    <div v-if="loading" class="loading-overlay">
      <ProgressSpinner
        strokeWidth="2"
        animationDuration="1.2s"
        class="text-secondary drop-shadow-lg"
        style="width: 36px; height: 36px"
      />
    </div>

    <Card class="mb-4 overflow-auto">
      <template #title>
        <div class="flex items-center justify-between">
          <div @click="toggle" class="flex items-center cursor-pointer gap-2">
            <i class="pi" :class="[visible ? 'pi-chevron-down' : 'pi-chevron-right']" />
            <i class="pi pi-filter text-primary" />
            <h4 class="text-sm font-medium">Filtros de búsqueda</h4>
          </div>

          <div class="hidden md:flex gap-1">
            <Tag
              v-for="filter in filters"
              :key="filter.key"
              :value="`${filter.label}: ${filter.value}`"
              severity="info"
              rounded
              class="text-xs"
            />
          </div>
        </div>
      </template>

      <template #content>
        <Transition name="fade">
          <div v-if="visible" :class="{ 'pointer-events-none opacity-60': loading }">
            <slot />
          </div>
        </Transition>
      </template>
    </Card>
  </div>
</template>

<script>
import ProgressSpinner from 'primevue/progressspinner'

export default {
  components: {
    ProgressSpinner
  },

  props: {
    modelValue: Boolean,
    filters: Array,
    loading: {
      type: Boolean,
      default: false
    }
  },
  emits: ['update:modelValue'],
  computed: {
    visible() {
      return this.modelValue
    }
  },
  methods: {
    toggle() {
      this.$emit('update:modelValue', !this.visible)
    }
  }
}
</script>

<style scoped>
.loading-overlay {
  position: absolute;
  inset: 0;
  background: rgba(255, 255, 255, 0.75);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 50;
  border-radius: 0.75rem;
  pointer-events: all;
}

.drop-shadow-lg {
  filter: drop-shadow(0 0 6px rgba(59, 130, 246, 0.5));
}
</style>