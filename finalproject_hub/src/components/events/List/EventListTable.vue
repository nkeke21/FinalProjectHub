<template>
    <div class="table-container">
        <n-space justify="start" style="margin-bottom: 16px;">
            <n-dropdown
                trigger="click"
                :options="[{ label: 'Sport Type', key: 'sport-type' }]"
                @select="handleFilterSelect"
                >
                <n-button type="primary" color="orange">
                    Add Filter
                </n-button>
            </n-dropdown>

            <n-popselect
                v-if="showSportTypeSelector"
                v-model:value="selectedSportType"
                :options="sportTypeOptions"
                >
                <n-button>
                    {{ selectedSportType || 'Sport Type' }}
                </n-button>
            </n-popselect>
        </n-space>
  
        <n-data-table
            :columns="columns"
            :data="paginatedData"
            :row-props="rowProps"
            size="large"
        />
        <Pagination
            :item-count="filteredEvents.length"
            @page-change="handlePageChange"
            @page-size-change="handlePageSizeChange"
        />
    </div>
</template>
  
<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { NDropdown, NButton, NSpace, NDataTable, NPopselect } from 'naive-ui'
import { useRouter } from 'vue-router'
import Pagination from '../../common/Pagination.vue'
import events  from './events.json'

const currentPage = ref(1)
const pageSize = ref(10)
const router = useRouter()

const columns = [
    { title: 'Host', key: 'host' },
    { title: 'Address', key: 'location' },
    { title: 'Date', key: 'formattedDate' },
    { title: 'Age Range', key: 'ageRange' },
    { title: 'Participants', key: 'participants' },
    { title: 'Sport Type', key: 'sportType' },
]

const showSportTypeSelector = ref(false)
const selectedSportType = ref<string | null>(null)

const sportTypeOptions = [
  { label: 'Football', value: 'Football' },
  { label: 'Basketball', value: 'Basketball' },
  { label: 'Running', value: 'Running' },
  { label: 'Tennis', value: 'Tennis' },
  { label: 'Cycling', value: 'Cycling' },
]

const filterOptions = [
  {
    label: 'Sport Type',
    key: 'sport-type'
  },
]

const filteredEvents = computed(() => {
  return events.filter(event => {
    return !selectedSportType.value || event.sportType === selectedSportType.value
  })
})

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value

  return filteredEvents.value.slice(start, end).map(event => ({
    key: event.id,
    host: event.host,
    location: event.location,
    formattedDate: new Date(event.date).toLocaleString(),
    ageRange: event.ageRange,
    participants: `${event.joined}/${event.total}`,
    sportType: event.sportType,
  }))
})

const handleFilterSelect = (key: string) => {
  if (key === 'sport-type') {
    showSportTypeSelector.value = true
  }
}

watch(selectedSportType, () => {
  currentPage.value = 1
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
        router.push(`/events/${row.key}`) // Navigate to event detail page
    }
})
</script>

<style>
.table-container {
    width: 100%;
    margin: 40px auto;
    padding: 20px;
}
</style>
