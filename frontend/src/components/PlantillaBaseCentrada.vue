<script setup>
defineProps({
  maxWidth: {
    type: String,
    default: 'max-w-md'
  }
})
</script>

<template>
  <div class="h-screen flex items-center justify-center bg-gray-50 dark:bg-gray-900">
    <div
      :class="[
        'w-full bg-white dark:bg-gray-800 p-8 rounded-lg shadow-lg',
        maxWidth
      ]"
    >
      <slot />
    </div>
  </div>
</template>
