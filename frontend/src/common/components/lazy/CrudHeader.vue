<template>
  <CustomHeaderPagina :title="title" :subtitle="subtitle">
    <template #actions>
      <Button
        v-if="showCreate"
        label="Nuevo"
        icon="pi pi-plus"
        @click="$emit('create')"
      />

      <Button
        icon="pi pi-refresh"
        outlined
        @click="$emit('refresh')"
      />

      <Button
        v-if="menuItems?.length"
        icon="pi pi-ellipsis-v"
        class="p-button-sm p-button-text"
        @click="toggle"
      />

      <Menu ref="menu" :model="menuItems" popup />
    </template>
  </CustomHeaderPagina>
</template>

<script>
export default {
  props: {
    title: String,
    subtitle: String,
    menuItems: Array,
    showCreate: { type: Boolean, default: true }
  },
  methods: {
    toggle(event) {
      this.$refs.menu.toggle(event)
    }
  }
}
</script>
