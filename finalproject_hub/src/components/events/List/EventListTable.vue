<template>
    <div class="table-container">
      <div class="table-toolbar">
        <div class="left-controls">
          <n-dropdown
            trigger="click"
            :options="[{ label: 'Sport Type', key: 'sport-type' }]"
            @select="handleFilterSelect"
          >
            <n-button type="primary" color="green">Add Filter</n-button>
          </n-dropdown>

          <n-popselect
            v-if="showSportTypeSelector"
            v-model:value="selectedSportType"
            :options="sportTypeOptions"
          >
            <n-button>{{ selectedSportType || 'Sport Type' }}</n-button>
          </n-popselect>
        </div>

        <div class="right-controls" v-if="showAddEvent !== false">
          <AddSportEventModal />
        </div>
      </div>

      <n-data-table
        :columns="columns"
        :data="paginatedData"
        :row-props="rowProps"
        :row-class-name="() => 'custom-row-height'"
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
import AddSportEventModal from '../Dialog/AddSportEventModal.vue'

const props = defineProps<{
  events: any[]
  showAddEvent?: boolean
}>()

const currentPage = ref(1)
const pageSize = ref(10)
const showSportTypeSelector = ref(false)
const selectedSportType = ref<string | null>(null)
const router = useRouter()

const sportTypeOptions = [
  { label: 'Football', value: 'Football' },
  { label: 'Basketball', value: 'Basketball' },
  { label: 'Running', value: 'Running' },
  { label: 'Tennis', value: 'Tennis' },
  { label: 'Cycling', value: 'Cycling' },
]

const columns = [
  { title: 'Host', key: 'host' },
  { title: 'Address', key: 'location' },
  { title: 'Date', key: 'formattedDate' },
  { title: 'Age Range', key: 'ageRange' },
  { title: 'Participants', key: 'participants' },
  { title: 'Sport Type', key: 'sportType' }
]

// Computed filtering and pagination
const filteredEvents = computed(() => {
    return props.events.filter(event => {
        return !selectedSportType.value || event.sportType === selectedSportType.value
    })
})

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value

  return filteredEvents.value.slice(start, end).map(event => ({
    key: event.id,
    host: event.hostName,
    location: event.location,
    formattedDate: new Date(event.eventTime).toLocaleString(),
    ageRange: event.ageRange,
    participants: `${event.numberOfParticipantsRegistered}/${event.numberOfParticipantsTotal}`,
    sportType: event.sportType
  }))
})

// Handlers
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
    router.push(`/events/${row.key}`)
  }
})
</script>

<style>
.table-container {
    width: 100%;
    margin: 40px auto;
    padding: 20px;
}

.table-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.left-controls {
  display: flex;
  gap: 12px;
}

.right-controls {
  display: flex;
}

.custom-row-height {
  height: 100px !important;
}

.n-data-table .n-data-table__th {
  font-size: 18px;
  font-weight: bold;
}

</style>
