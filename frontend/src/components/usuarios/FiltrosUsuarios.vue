<template>
    <CustomCardFiltros :model-value="visibleModel" @update:modelValue="visibleModel = $event" :filters="activeFilters"
        :loading="loadingModel">
        <form @submit.prevent="$emit('search')">
            <div v-if="visibleModel" class="flex flex-col gap-4">

                <!-- Campos -->
                <div class="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-3">

                    <!-- Usuario -->
                    <span class="p-input-icon-left w-full">
                        <span>
                            <i class="pi pi-search" />
                            <span class="font-bold text-sm ml-2">Usuario</span>
                        </span>
                        <InputText v-model="model.usuario" placeholder="Buscar por usuario..." class="w-full" />
                    </span>

                    <!-- Correo -->
                    <span class="p-input-icon-left w-full">
                        <span>
                            <i class="pi pi-box" />
                            <span class="font-bold text-sm ml-2">Correo</span>
                        </span>
                        <InputText v-model="model.correo" placeholder="Buscar por correo..." class="w-full" />
                    </span>

                    <!-- Estado -->
                    <span class="p-input-icon-left w-full">
                        <span>
                            <i class="pi pi-filter" />
                            <span class="font-bold text-sm ml-2">Estado</span>
                        </span>
                        <Select v-model="model.es_activo" :options="estados" option-label="label" option-value="value"
                            placeholder="Seleccionar estado..." class="w-full" />
                    </span>

                    <!-- Fecha -->
                    <span class="w-full">
                        <span>
                            <i class="pi pi-calendar" />
                            <span class="font-bold text-sm ml-2">Fecha de creación (Rango)</span>
                        </span>

                        <DatePicker v-model="model.fecha_creacion" selectionMode="range" dateFormat="yy-mm-dd" showIcon
                            class="w-full" />
                    </span>

                </div>

                <!-- Acciones -->
                <div class="flex justify-between items-center">

                    <Button label="Limpiar todo" icon="pi pi-filter-slash" severity="secondary" text size="small"
                        :disabled="loadingModel" @click="$emit('clear')" />

                    <CustomFiltrosActivos :filters="activeFilters" />

                    <Button label="Buscar" icon="pi pi-search" severity="secondary" size="small"
                        :disabled="loadingModel" type="submit" />

                </div>
            </div>
        </form>
    </CustomCardFiltros>
</template>

<script>

export default {
    name: 'FiltrosUsuarios',

    props: {
        modelValue: { type: Object, required: true },
        activeFilters: { type: Array, default: () => [] },
        estados: { type: Array, required: true },
        visible: { type: Boolean, default: false },
        loading: { type: Boolean, default: false }
    },

    emits: [
        'update:modelValue',
        'update:visible',
        'update:loading',
        'search',
        'clear'
    ],

    computed: {
        model: {
            get() {
                return this.modelValue
            },
            set(value) {
                this.$emit('update:modelValue', value)
            }
        },
        visibleModel: {
            get() {
                return this.visible
            },
            set(value) {
                this.$emit('update:visible', value)
            }
        },
        loadingModel: {
            get() {
                return this.loading
            },
            set(value) {
                this.$emit('update:loading', value)
            }
        }
    }

}
</script>