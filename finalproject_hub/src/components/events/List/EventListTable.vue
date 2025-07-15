<template>
  <div class="events-container">
    <div class="events-toolbar">
      <div class="left-controls">
        <n-dropdown
          trigger="click"
          :options="[{ label: 'Sport Type', key: 'sport-type' }]"
          @select="handleFilterSelect"
        >
          <n-button type="primary" color="#22c55e" class="filter-button">
            <template #icon>
              <n-icon><FilterOutline /></n-icon>
            </template>
            Add Filter
          </n-button>
        </n-dropdown>

        <n-popselect
          v-if="showSportTypeSelector"
          v-model:value="selectedSportType"
          :options="sportTypeOptions"
        >
          <n-button class="sport-selector">
            <template #icon>
              <n-icon><FootballOutline /></n-icon>
            </template>
            {{ selectedSportType || 'Sport Type' }}
          </n-button>
        </n-popselect>
      </div>

      <div class="right-controls" v-if="showAddEvent !== false">
        <AddSportEventModal />
      </div>
    </div>

    <div class="events-grid">
      <div 
        v-for="event in paginatedData" 
        :key="event.key"
        class="event-card"
        @click="router.push(`/events/${event.key}`)"
      >
        <div class="event-header">
          <div class="sport-badge">
            <n-icon size="20" color="#22c55e">
              <FootballOutline v-if="event.sportType === 'Football'" />
              <BasketballOutline v-else-if="event.sportType === 'Basketball'" />
              <FitnessOutline v-else-if="event.sportType === 'Running'" />
              <TennisballOutline v-else-if="event.sportType === 'Tennis'" />
              <BicycleOutline v-else-if="event.sportType === 'Cycling'" />
              <FootballOutline v-else />
            </n-icon>
            <span>{{ event.sportType }}</span>
          </div>
          <div class="event-status" :class="{ 'full': isEventFull(event) }">
            {{ isEventFull(event) ? 'Full' : 'Open' }}
          </div>
        </div>

        <div class="event-content">
          <h3 class="event-title">{{ truncateDescription(event.description) || 'Sport Event' }}</h3>
          
          <div class="event-meta">
            <div class="meta-item">
              <n-icon size="16" color="#64748b">
                <PersonOutline />
              </n-icon>
              <span>{{ event.hostName }}</span>
            </div>
            <div class="meta-item">
              <n-icon size="16" color="#64748b">
                <LocationOutline />
              </n-icon>
              <span>{{ event.location }}</span>
            </div>
            <div class="meta-item">
              <n-icon size="16" color="#64748b">
                <CalendarOutline />
              </n-icon>
              <span>{{ formatEventDate(event.formattedDate) }}</span>
            </div>
            <div class="meta-item">
              <n-icon size="16" color="#64748b">
                <PeopleOutline />
              </n-icon>
              <span>{{ event.ageRange }} years</span>
            </div>
          </div>

          <div class="participants-section">
            <div class="participants-info">
              <span class="participants-count">{{ event.participants }}</span>
              <span class="participants-label">participants</span>
            </div>
            <div class="progress-bar">
              <div 
                class="progress-fill" 
                :style="{ width: `${getParticipantPercentage(event)}%` }"
              ></div>
            </div>
          </div>
        </div>

        <div class="event-footer">
          <n-button 
            type="primary" 
            color="#22c55e" 
            size="small"
            class="view-details-btn"
            @click.stop="router.push(`/events/${event.key}`)"
          >
            View Details
          </n-button>
        </div>
      </div>
    </div>

    <div v-if="paginatedData.length === 0" class="empty-state">
      <n-icon size="64" color="#cbd5e1">
        <CalendarOutline />
      </n-icon>
      <h3>No events found</h3>
      <p>{{ selectedSportType ? `No ${selectedSportType} events available` : 'No events available at the moment' }}</p>
    </div>

    <div class="pagination-wrapper">
      <Pagination
        :item-count="filteredEvents.length"
        @page-change="handlePageChange"
        @page-size-change="handlePageSizeChange"
      />
    </div>
  </div>
</template>
  
<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { 
  NDropdown, 
  NButton, 
  NDataTable, 
  NPopselect, 
  NIcon 
} from 'naive-ui'
import { 
  FilterOutline,
  FootballOutline,
  BasketballOutline,
  FitnessOutline,
  TennisballOutline,
  BicycleOutline,
  PersonOutline,
  LocationOutline,
  CalendarOutline,
  PeopleOutline
} from '@vicons/ionicons5'
import { useRouter } from 'vue-router'
import Pagination from '../../common/Pagination.vue'
import AddSportEventModal from '../Dialog/AddSportEventModal.vue'

const props = defineProps<{
  events: any[]
  showAddEvent?: boolean
}>()

const currentPage = ref(1)
const pageSize = ref(6)
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

const filteredEvents = computed(() => {
    return props.events.filter(event => {
        return !selectedSportType.value || event.sportType === selectedSportType.value
    })
})

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value

  return filteredEvents.value.slice(start, end).map(event => ({
    key: event.eventId,
    hostName: event.hostName,
    location: event.location,
    formattedDate: new Date(event.eventTime).toLocaleString(),
    ageRange: event.ageRange,
    participants: `${event.numberOfParticipantsRegistered}/${event.numberOfParticipantsTotal}`,
    sportType: event.sportType,
    description: event.description
  }))
})

const formatEventDate = (dateString: string) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const getParticipantPercentage = (event: any) => {
  const [registered, total] = event.participants.split('/').map(Number)
  return Math.round((registered / total) * 100)
}

const isEventFull = (event: any) => {
  const [registered, total] = event.participants.split('/').map(Number)
  return registered >= total
}

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

const truncateDescription = (description: string) => {
  if (!description) return null
  if (description.length <= 60) return description
  return description.substring(0, 60) + '...'
}
</script>

<style scoped>
.events-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem;
}

.events-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  padding: 1rem;
  background: white;
  border-radius: 1rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.left-controls {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.filter-button, .sport-selector {
  font-weight: 600;
  border-radius: 0.75rem;
}

.right-controls {
  display: flex;
}

.events-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 2rem;
  margin-bottom: 2rem;
}

.event-card {
  background: white;
  border-radius: 1rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  border: 1px solid #e2e8f0;
  overflow: hidden;
  transition: all 0.3s ease;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.event-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 25px -3px rgba(0, 0, 0, 0.1);
}

.event-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 1.5rem;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  border-bottom: 1px solid #e2e8f0;
}

.sport-badge {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: white;
  padding: 0.5rem 1rem;
  border-radius: 2rem;
  font-weight: 600;
  font-size: 0.875rem;
  color: #1e293b;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.event-status {
  padding: 0.25rem 0.75rem;
  border-radius: 1rem;
  font-size: 0.75rem;
  font-weight: 600;
  background: #22c55e;
  color: white;
}

.event-status.full {
  background: #ef4444;
}

.event-content {
  padding: 1.5rem;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.event-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 1rem;
  line-height: 1.4;
  min-height: 3.5rem;
  display: flex;
  align-items: center;
}

.event-meta {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
  color: #64748b;
}

.participants-section {
  margin-bottom: 1.5rem;
  margin-top: auto;
}

.participants-info {
  display: flex;
  align-items: baseline;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.participants-count {
  font-size: 1.5rem;
  font-weight: 700;
  color: #22c55e;
}

.participants-label {
  font-size: 0.875rem;
  color: #64748b;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background: #e2e8f0;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #22c55e 0%, #16a34a 100%);
  transition: width 0.3s ease;
}

.event-footer {
  padding: 1rem 1.5rem;
  background: #f8fafc;
  border-top: 1px solid #e2e8f0;
  margin-top: auto;
}

.view-details-btn {
  width: 100%;
  font-weight: 600;
  border-radius: 0.75rem;
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #64748b;
}

.empty-state h3 {
  font-size: 1.5rem;
  font-weight: 600;
  margin: 1rem 0 0.5rem;
  color: #1e293b;
}

.empty-state p {
  font-size: 1rem;
  color: #64748b;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
}

@media (max-width: 768px) {
  .events-container {
    padding: 1rem;
  }
  
  .events-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .events-toolbar {
    flex-direction: column;
    gap: 1rem;
    align-items: stretch;
  }
  
  .left-controls {
    justify-content: center;
  }
}
</style>
