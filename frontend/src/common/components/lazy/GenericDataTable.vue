<template>
  <DataTable
    :value="value"
    :filters="filters"
    paginator
    :rows="localRows"
    stripedRows
    showGridlines
    :loading="localLoading"
  >
    <slot />

    <template #paginatorcontainer="{ first, last, page, pageCount, prevPageCallback, nextPageCallback, rowChangeCallback, totalRecords }">
        <div class="flex items-stretch divide-x divide-gray-200 w-full overflow-auto">
          <!-- Dropdown -->
          <div class="flex items-center gap-3 px-4">
            <i class="pi pi-list text-gray-400 text-sm"></i>
            <div>
              <span class="text-xs text-gray-400 block mb-1">FILAS</span>
              <Dropdown 
                :options="[10, 50, 100]" 
                v-model="localRows" 
                @change="rowChangeCallback(localRows)"
                style="height: 36px;"
              />
            </div>
          </div>
          
          <!-- Progreso -->
          <div class="hidden md:flex items-center justify-start gap-4 px-4 flex-1">
            <i class="pi pi-chart-line text-gray-400 text-sm"></i>
            <div class="w-full">
              <span class="text-xs text-gray-400 block mb-1">PROGRESO</span>
              <div class="flex items-center gap-3">
                <div class="w-full h-2 bg-gray-100 rounded-full overflow-hidden">
                  <div 
                    class="h-full bg-blue-600 transition-all"
                    :style="{ width: `${((page + 1) / pageCount) * 100}%` }"
                  ></div>
                </div>
                <span class="text-sm font-medium text-gray-700">{{ Math.round(((page + 1) / pageCount) * 100) }}%</span>
              </div>
            </div>
          </div>
          
          <!-- Info registros -->
          <div class="hidden md:flex items-center gap-3 px-4">
            <i class="pi pi-database text-gray-400 text-sm"></i>
            <div>
              <span class="text-xs text-gray-400 block mb-1">REGISTROS</span>
              <div class="text-sm">
                <span class="font-medium text-gray-900">{{ first }}</span>
                <span class="text-gray-400 mx-1">–</span>
                <span class="font-medium text-gray-900">{{ last }}</span>
                <span class="text-gray-300 mx-2">/</span>
                <span class="font-medium text-gray-900">{{ totalRecords }}</span>
              </div>
            </div>
          </div>
          
          <!-- Navegación -->
          <div class="flex items-center px-4 md:gap-3">
            <i class="pi pi-file text-gray-400 text-sm"></i>
            <div>
              <span class="text-xs text-gray-400 block mb-1">PÁGINA</span>
              <div class="flex items-center md:gap-2">
                <Button 
                  icon="pi pi-chevron-left" 
                  severity="secondary" 
                  text
                  @click="prevPageCallback" 
                  :disabled="page === 0"
                  class="w-7 h-7"
                  size="small"
                />
                <span class="text-sm font-medium text-gray-700 min-w-[5px] text-center">{{ page + 1 }}</span>
                <Button 
                  icon="pi pi-chevron-right" 
                  severity="secondary" 
                  text
                  @click="nextPageCallback" 
                  :disabled="page === pageCount - 1"
                  class="w-7 h-7"
                  size="small"
                />
                <span class="text-sm text-gray-500 ml-1">/{{ pageCount }}</span>
              </div>
            </div>
          </div>
        </div>
    </template>
  </DataTable>
</template>

<script>
import { get, set } from '@vueuse/core';
import DataTable from 'primevue/datatable'

export default {
  components: { DataTable },
  props: {
    value: Array,
    filters: Object,
    loading: Boolean,
    rows: Number
  },
  computed: {
    localRows: {
      get() {
        return this.rows || 10;
      },
      set(value) {
        this.$emit('update:rows', value);
      }
    },
    localLoading() {
      return this.loading || false;
    }
  }
}
</script>
