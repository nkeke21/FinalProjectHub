<template>
    <div class="table-container">
        <n-data-table :columns="columns" :data="paginatedData" :row-props="rowProps" />
        <Pagination
        :item-count="events.length"
        @page-change="handlePageChange"
        @page-size-change="handlePageSizeChange"
        />
    </div>
</template>
  
<script setup lang="ts">
import { computed, ref } from 'vue'
import { NDataTable } from 'naive-ui'
import Pagination from '../../common/Pagination.vue'
import events  from './events.json'

const currentPage = ref(1)
const pageSize = ref(10)

const columns = [
    { title: 'Host', key: 'host' },
    { title: 'Address', key: 'location' },
    { title: 'Date', key: 'formattedDate' },
    { title: 'Age Range', key: 'ageRange' },
    { title: 'Participants', key: 'participants' },
    { title: 'Sport Type', key: 'sportType' },
]

const paginatedData = computed(() => {
const start = (currentPage.value - 1) * pageSize.value
const end = currentPage.value * pageSize.value
return events.slice(start, end).map(event => ({
        key: event.id,
        host: event.host,
        location: event.location,
        formattedDate: event.date.toLocaleString(),
        ageRange: event.ageRange,
        participants: `${event.joined}/${event.total}`,
        sportType: event.sportType,
    }))
})

const handlePageChange = (page: number) => {
    currentPage.value = page
}

const handlePageSizeChange = (size: number) => {
    pageSize.value = size
}

const rowProps = (row: any) => ({
    style: 'cursor: pointer;',
    onClick: () => {
        console.log(`Event by ${row.host} - ${row.sportType}`)
    }
})
</script>

<style>
.table-container {
    width: 1200px;
    max-width: 100%;
    margin: 40px auto;
    padding: 20px;
}
</style>
