<template>
    <div class="pagination-container">
        <n-pagination
        v-model:page="currentPage"
        v-model:page-size="pageSize"
        :item-count="itemCount"
        show-size-picker
        :page-sizes="[5, 10, 20, 50]"
        @update:page="onPageChange"
        @update:page-size="onPageSizeChange"
        />
    </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { NPagination } from 'naive-ui'

const props = defineProps<{
    itemCount: number
}>()

const emit = defineEmits(['page-change', 'page-size-change'])

const currentPage = ref(1)
const pageSize = ref(10)

const onPageChange = (page: number) => {
    emit('page-change', page)
}

const onPageSizeChange = (size: number) => {
currentPage.value = 1 
    emit('page-size-change', size)
}
</script>

<style scoped>
.pagination-container {
    display: flex;
    justify-content: center;
    margin-top: 20px;
}
</style>
