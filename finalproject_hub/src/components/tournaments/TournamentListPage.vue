<template>
  <div class="tournament-list-page">
    <div class="page-header">
      <div class="header-content">
        <div class="header-text">
          <h1>Tournaments</h1>
          <p>Discover and join exciting tournaments in your area</p>
        </div>
        <n-button 
          type="primary" 
          size="large"
          @click="showCreateModal = true"
          color="#3b82f6"
        >
          <template #icon>
            <n-icon><AddOutline /></n-icon>
          </template>
          Create Tournament
        </n-button>
      </div>
    </div>

    <div class="filters-section">
      <div class="search-bar">
        <n-input
          v-model:value="searchQuery"
          placeholder="Search tournaments..."
          clearable
          size="large"
        >
          <template #prefix>
            <n-icon><SearchOutline /></n-icon>
          </template>
        </n-input>
      </div>

      <div class="filter-controls">
        <n-button 
          type="primary" 
          color="orange"
          @click="showFilters = !showFilters"
        >
          <template #icon>
            <n-icon><FilterOutline /></n-icon>
          </template>
          {{ showFilters ? 'Hide' : 'Show' }} Filters
        </n-button>
        
        <n-button 
          v-if="hasActiveFilters"
          @click="clearFilters"
          size="small"
        >
          Clear All
        </n-button>
      </div>

      <div v-if="showFilters" class="advanced-filters">
        <div class="filter-row">
          <div class="filter-group">
            <label>Sport Type</label>
            <n-select
              v-model:value="filters.sportType"
              :options="sportTypeOptions"
              placeholder="All Sports"
              clearable
              size="medium"
            />
          </div>


          <div class="filter-group">
            <label>Format</label>
            <n-select
              v-model:value="filters.format"
              :options="formatOptions"
              placeholder="All Formats"
              clearable
              size="medium"
            />
          </div>
        </div>

        <div class="filter-row">
          <div class="filter-group">
            <label>Date Range</label>
            <n-date-picker
              v-model:value="filters.dateRange"
              type="daterange"
              placeholder="Select date range"
              clearable
              size="medium"
            />
          </div>

          <div class="filter-group">
            <label>Location</label>
            <n-input
              v-model:value="filters.location"
              placeholder="Enter location"
              clearable
              size="medium"
            />
          </div>

          <div class="filter-group">
            <label>Entry Fee Range</label>
            <n-slider
              v-model:value="filters.entryFeeRange"
              :min="0"
              :max="200"
              :step="10"
              range
              size="medium"
            />
            <div class="slider-labels">
              <span>{{ filters.entryFeeRange[0] }}₾</span>
              <span>{{ filters.entryFeeRange[1] }}₾</span>
            </div>
          </div>
        </div>

        <div class="filter-row">
          <div class="filter-group">
            <label>Age Range</label>
            <n-slider
              v-model:value="filters.ageRange"
              :min="16"
              :max="65"
              :step="1"
              range
              size="medium"
            />
            <div class="slider-labels">
              <span>{{ filters.ageRange[0] }}-{{ filters.ageRange[1] }} years</span>
            </div>
          </div>

          <div class="filter-group">
            <label>Participants</label>
            <n-slider
              v-model:value="filters.participantsRange"
              :min="0"
              :max="100"
              :step="1"
              range
              size="medium"
            />
            <div class="slider-labels">
              <span>{{ filters.participantsRange[0] }}-{{ filters.participantsRange[1] }} participants</span>
            </div>
          </div>
        </div>
      </div>

      <div v-if="hasActiveFilters" class="active-filters">
        <span class="active-filters-label">Active filters:</span>
        <n-tag
          v-for="filter in activeFilterTags"
          :key="filter.key"
          :type="filter.type"
          closable
          @close="removeFilter(filter.key)"
          size="small"
        >
          {{ filter.label }}
        </n-tag>
      </div>
    </div>

    <div class="results-summary">
      <span>{{ filteredTournaments.length }} tournament{{ filteredTournaments.length !== 1 ? 's' : '' }} found</span>
      <div class="sort-controls">
        <label>Sort by:</label>
        <n-select
          v-model:value="sortBy"
          :options="sortOptions"
          size="small"
        />
      </div>
    </div>

    <div v-if="loading" class="loading-state">
      <n-spin size="large" />
      <p>Loading tournaments...</p>
    </div>
    
    <div v-else-if="sortedTournaments.length === 0" class="empty-state">
      <n-icon size="64" color="#94a3b8">
        <TrophyOutline />
      </n-icon>
      <h3>No tournaments found</h3>
      <p v-if="hasActiveFilters">Try adjusting your filters or search terms</p>
      <p v-else>Check back later for upcoming tournaments in your area</p>
    </div>
    
    <div v-else class="tournament-grid">
      <div class="tournament-card" v-for="tournament in sortedTournaments" :key="tournament.id">
        <div class="tournament-header">
          <div class="tournament-badge">
            <n-icon size="20" color="#f97316">
              <FootballOutline v-if="tournament.sportType === 'Football'" />
              <BasketballOutline v-else-if="tournament.sportType === 'Basketball'" />
              <TennisballOutline v-else-if="tournament.sportType === 'Tennis'" />
              <FitnessOutline v-else />
            </n-icon>
            <span class="sport-type">{{ tournament.sportType }}</span>
          </div>
        </div>
        
        <div class="registration-status-badge" v-if="!registrationLoading">
          <n-tag 
            v-if="isUserProfileLoading"
            type="default"
            size="small"
            color="#6b7280"
          >
            <template #icon>
              <n-icon size="12">
                <n-spin size="12" />
              </n-icon>
            </template>
            Loading...
          </n-tag>
          <n-tag 
            v-else-if="isTournamentHost(tournament)"
            type="info"
            size="small"
            color="#3b82f6"
          >
            <template #icon>
              <n-icon size="12">
                <PersonOutline />
              </n-icon>
            </template>
            Tournament Host
          </n-tag>
          <n-tag 
            v-else
            :type="isUserRegistered(tournament.id) ? 'success' : 'default'"
            size="small"
            :color="isUserRegistered(tournament.id) ? '#10b981' : '#6b7280'"
          >
            <template #icon>
              <n-icon size="12">
                <CheckmarkCircleOutline v-if="isUserRegistered(tournament.id)" />
                <PersonOutline v-else />
              </n-icon>
            </template>
            {{ isUserRegistered(tournament.id) ? 'Registered' : 'Not Registered' }}
          </n-tag>
        </div>
        
        <h3 class="tournament-title">{{ tournament.name }}</h3>
        
        <div class="tournament-meta">
          <div class="meta-item">
            <n-icon size="16" color="#64748b">
              <PersonOutline />
            </n-icon>
            <span>{{ tournament.hostName }}</span>
          </div>
          <div class="meta-item">
            <n-icon size="16" color="#64748b">
              <LocationOutline />
            </n-icon>
            <span>{{ tournament.location }}</span>
          </div>
          <div class="meta-item">
            <n-icon size="16" color="#64748b">
              <CalendarOutline />
            </n-icon>
            <span>{{ formatDateRange(tournament.startDate, tournament.endDate) }}</span>
          </div>
          <div class="meta-item">
            <n-icon size="16" color="#64748b">
              <PeopleOutline />
            </n-icon>
            <span>{{ tournament.ageRange.min }}-{{ tournament.ageRange.max }} years</span>
          </div>
          <div class="meta-item">
            <n-icon size="16" color="#64748b">
              <WalletOutline />
            </n-icon>
            <span>{{ tournament.entryFee === 0 ? 'Free entry' : `${tournament.entryFee}₾ entry fee` }}</span>
          </div>
        </div>

        <div class="tournament-participants">
          <span class="participant-count">{{ tournament.currentParticipants }}/{{ tournament.maxParticipants }} participants</span>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: (tournament.currentParticipants / tournament.maxParticipants * 100) + '%' }"></div>
          </div>
        </div>

        <div class="tournament-actions">
          <n-button 
            v-if="!isUserProfileLoading && !isUserRegistered(tournament.id) && !isTournamentHost(tournament)"
            type="primary" 
            color="#3b82f6" 
            class="quick-register-btn"
            @click="openQuickRegistration(tournament)"
            :loading="registrationLoading"
          >
            Register Now
          </n-button>
          <n-button 
            v-if="!isUserProfileLoading && isTournamentHost(tournament)"
            type="info" 
            color="#3b82f6" 
            class="host-badge"
            disabled
          >
            You're the Host
          </n-button>
          <n-button 
            v-if="isUserProfileLoading"
            type="info" 
            color="#3b82f6" 
            class="host-badge"
            disabled
          >
            <n-spin size="small" />
            Loading...
          </n-button>
          <n-button 
            type="primary" 
            color="orange" 
            class="view-details-btn"
            @click="router.push(`/tournaments/${tournament.id}`)"
          >
            View Details
          </n-button>
        </div>
      </div>
    </div>

    <CreateTournamentModal
      v-model:show="showCreateModal"
      @tournament-created="handleTournamentCreated"
    />
    
    <TournamentRegistrationForm
      v-model:show="showQuickRegistrationModal"
      :tournament="selectedTournament"
      @registration-success="handleQuickRegistrationSuccess"
      @registration-error="handleQuickRegistrationError"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { 
  NButton, 
  NIcon,
  NInput,
  NSelect,
  NDatePicker,
  NSlider,
  NTag,
  NSpin
} from 'naive-ui'
import { 
  FilterOutline,
  SearchOutline,
  FootballOutline,
  BasketballOutline,
  TennisballOutline,
  FitnessOutline,
  LocationOutline,
  CalendarOutline,
  PeopleOutline,
  PersonOutline,
  TrophyOutline,
  WalletOutline,
  AddOutline,
  CheckmarkCircleOutline
} from '@vicons/ionicons5'
import type { Tournament } from '@/models/Tournament'
import { SportType, TournamentStatus, TournamentFormat } from '@/models/Tournament'
import CreateTournamentModal from './CreateTournamentModal.vue'
import TournamentRegistrationForm from './TournamentRegistrationForm.vue'
import { TournamentRegistrationService } from '@/services/apis/TournamentRegistrationService'
import type { TournamentRegistration } from '@/models/TournamentRegistration'
import { TournamentService } from '@/services/apis/TournamentService'
import { useUserStore } from '@/store/profile/userStore'

const router = useRouter()
const userStore = useUserStore()
const tournaments = ref<Tournament[]>([])
const searchQuery = ref('')
const loading = ref(false)
const showFilters = ref(false)
const sortBy = ref('startDate')
const showCreateModal = ref(false)

const userRegistrations = ref<Map<string, TournamentRegistration>>(new Map())
const registrationLoading = ref(false)

const showQuickRegistrationModal = ref(false)
const selectedTournament = ref<Tournament | null>(null)

const filters = ref({
  sportType: null as string | null,
  format: null as string | null,
  dateRange: null as [number, number] | null,
  location: '',
  entryFeeRange: [0, 200] as [number, number],
  ageRange: [16, 65] as [number, number],
  participantsRange: [0, 100] as [number, number]
})

const sportTypeOptions = [
  { label: 'Football', value: 'Football' },
  { label: 'Basketball', value: 'Basketball' },
  { label: 'Tennis', value: 'Tennis' },
  { label: 'Running', value: 'Running' },
  { label: 'Volleyball', value: 'Volleyball' }
]


const formatOptions = [
  { label: 'Single Elimination', value: 'SINGLE_ELIMINATION' },
  { label: 'Double Elimination', value: 'DOUBLE_ELIMINATION' },
  { label: 'Round Robin', value: 'ROUND_ROBIN' },
  { label: 'Swiss System', value: 'SWISS_SYSTEM' }
]

const sortOptions = [
  { label: 'Start Date', value: 'startDate' },
  { label: 'Name', value: 'name' },
  { label: 'Entry Fee', value: 'entryFee' },
  { label: 'Participants', value: 'currentParticipants' },
  { label: 'Created Date', value: 'createdAt' }
]

const filteredTournaments = computed(() => {
  return tournaments.value.filter(tournament => {
    if (searchQuery.value && !tournament.name.toLowerCase().includes(searchQuery.value.toLowerCase()) &&
        !tournament.description.toLowerCase().includes(searchQuery.value.toLowerCase()) &&
        !tournament.location.toLowerCase().includes(searchQuery.value.toLowerCase())) {
      return false
    }

    if (filters.value.sportType && tournament.sportType !== filters.value.sportType) {
      return false
    }

    if (filters.value.format && tournament.format !== filters.value.format) {
      return false
    }

    if (filters.value.dateRange) {
      const tournamentStart = new Date(tournament.startDate).getTime()
      const [start, end] = filters.value.dateRange
      if (tournamentStart < start || tournamentStart > end) {
        return false
      }
    }

    if (filters.value.location && !tournament.location.toLowerCase().includes(filters.value.location.toLowerCase())) {
      return false
    }

    if (tournament.entryFee < filters.value.entryFeeRange[0] || tournament.entryFee > filters.value.entryFeeRange[1]) {
      return false
    }

    if (tournament.ageRange.min > filters.value.ageRange[1] || tournament.ageRange.max < filters.value.ageRange[0]) {
      return false
    }

    if (tournament.currentParticipants < filters.value.participantsRange[0] || tournament.currentParticipants > filters.value.participantsRange[1]) {
      return false
    }

    return true
  })
})

const sortedTournaments = computed(() => {
  const sorted = [...filteredTournaments.value]
  
  switch (sortBy.value) {
    case 'name':
      return sorted.sort((a, b) => a.name.localeCompare(b.name))
    case 'entryFee':
      return sorted.sort((a, b) => a.entryFee - b.entryFee)
    case 'currentParticipants':
      return sorted.sort((a, b) => b.currentParticipants - a.currentParticipants)
    case 'createdAt':
      return sorted.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
    case 'startDate':
    default:
      return sorted.sort((a, b) => new Date(a.startDate).getTime() - new Date(b.startDate).getTime())
  }
})

const hasActiveFilters = computed(() => {
  return searchQuery.value || 
         filters.value.sportType || 
         filters.value.format || 
         filters.value.dateRange || 
         filters.value.location || 
         filters.value.entryFeeRange[0] > 0 || 
         filters.value.entryFeeRange[1] < 200 ||
         filters.value.ageRange[0] > 16 || 
         filters.value.ageRange[1] < 65 ||
         filters.value.participantsRange[0] > 0 || 
         filters.value.participantsRange[1] < 100
})

const activeFilterTags = computed(() => {
  const tags = []
  
  if (searchQuery.value) {
    tags.push({ key: 'search', label: `Search: "${searchQuery.value}"`, type: 'primary' })
  }
  if (filters.value.sportType) {
    tags.push({ key: 'sportType', label: `Sport: ${filters.value.sportType}`, type: 'success' })
  }
  if (filters.value.format) {
    tags.push({ key: 'format', label: `Format: ${filters.value.format.replace('_', ' ')}`, type: 'info' })
  }
  if (filters.value.location) {
    tags.push({ key: 'location', label: `Location: ${filters.value.location}`, type: 'default' })
  }
  if (filters.value.dateRange) {
    const start = new Date(filters.value.dateRange[0]).toLocaleDateString()
    const end = new Date(filters.value.dateRange[1]).toLocaleDateString()
    tags.push({ key: 'dateRange', label: `Date: ${start} - ${end}`, type: 'default' })
  }
  if (filters.value.entryFeeRange[0] > 0 || filters.value.entryFeeRange[1] < 200) {
    tags.push({ key: 'entryFee', label: `Entry Fee: ${filters.value.entryFeeRange[0]}₾ - ${filters.value.entryFeeRange[1]}₾`, type: 'default' })
  }
  if (filters.value.ageRange[0] > 16 || filters.value.ageRange[1] < 65) {
    tags.push({ key: 'ageRange', label: `Age: ${filters.value.ageRange[0]}-${filters.value.ageRange[1]} years`, type: 'default' })
  }
  if (filters.value.participantsRange[0] > 0 || filters.value.participantsRange[1] < 100) {
    tags.push({ key: 'participantsRange', label: `Participants: ${filters.value.participantsRange[0]}-${filters.value.participantsRange[1]}`, type: 'default' })
  }
  
  return tags
})

const clearFilters = () => {
  searchQuery.value = ''
  filters.value = {
    sportType: null,
    format: null,
    dateRange: null,
    location: '',
    entryFeeRange: [0, 200],
    ageRange: [16, 65],
    participantsRange: [0, 100]
  }
}

const removeFilter = (key: string) => {
  switch (key) {
    case 'search':
      searchQuery.value = ''
      break
    case 'sportType':
      filters.value.sportType = null
      break
    case 'format':
      filters.value.format = null
      break
    case 'location':
      filters.value.location = ''
      break
    case 'dateRange':
      filters.value.dateRange = null
      break
    case 'entryFee':
      filters.value.entryFeeRange = [0, 200]
      break
    case 'ageRange':
      filters.value.ageRange = [16, 65]
      break
    case 'participantsRange':
      filters.value.participantsRange = [0, 100]
      break
  }
}

const formatDateRange = (startDate: string, endDate: string) => {
  const start = new Date(startDate).toLocaleDateString();
  const end = new Date(endDate).toLocaleDateString();
  return `${start} - ${end}`;
};

const handleTournamentCreated = async (newTournament: Tournament) => {
  await loadTournaments() // Reload tournaments from backend
  showCreateModal.value = false
}

const loadTournaments = async () => {
  try {
    loading.value = true
    const response = await TournamentService.getAllTournaments()
    tournaments.value = response.map(TournamentService.convertToTournament)
  } catch (error) {
    console.error('Error loading tournaments:', error)
    // Show user-friendly error message
    if (error instanceof Error) {
      console.error('Tournament loading error:', error.message)
    }
  } finally {
    loading.value = false
  }
}

const loadUserRegistrations = async () => {
  try {
    registrationLoading.value = true
    const registrations = new Map<string, TournamentRegistration>()
    
    for (const tournament of tournaments.value) {
      const registration = await TournamentRegistrationService.getUserRegistration(tournament.id)
      if (registration) {
        registrations.set(tournament.id, registration)
      }
    }
    
    userRegistrations.value = registrations
  } catch (error) {
    console.error('Error loading user registrations:', error)
  } finally {
    registrationLoading.value = false
  }
}

const isUserRegistered = (tournamentId: string): boolean => {
  const registration = userRegistrations.value.get(tournamentId)
  return registration?.status === 'REGISTERED'
}

const isTournamentHost = (tournament: Tournament): boolean => {
  if (userStore.isLoading) {
    console.log('User profile is loading, not showing host status')
    return false
  }
  
  if (!userStore.profile) {
    console.log('No user profile available')
    return false
  }
  
  const isHost = String(tournament.hostId) === String(userStore.profile.id)
  console.log(`Tournament ${tournament.id}: hostId=${tournament.hostId}, userId=${userStore.profile.id}, isHost=${isHost}`)
  return isHost
}

const isUserProfileLoading = computed(() => {
  const loading = userStore.isLoading
  console.log('User profile loading state:', loading)
  return loading
})

const getUserRegistrationStatus = (tournamentId: string): string => {
  const registration = userRegistrations.value.get(tournamentId)
  if (!registration) return 'not-registered'
  return registration.status.toLowerCase()
}

const openQuickRegistration = (tournament: Tournament) => {
  selectedTournament.value = tournament
  showQuickRegistrationModal.value = true
}

const handleQuickRegistrationSuccess = async () => {
  await loadUserRegistrations()
}

const handleQuickRegistrationError = (message: string) => {
  console.error('Registration failed:', message)
}

// Watch for user profile changes and reload registrations
watch(() => userStore.profile, async (newProfile) => {
  console.log('User profile changed:', newProfile)
  if (newProfile) {
    console.log('Loading user registrations for profile:', newProfile.id)
    await loadUserRegistrations()
  }
}, { immediate: true })

onMounted(async () => {
  console.log('TournamentListPage mounted, current profile:', userStore.profile)
  
  // Always fetch the current user profile to ensure it's loaded
  await userStore.fetchCurrentUserProfile()
  console.log('Profile loaded:', userStore.profile)
  
  await loadTournaments()
  await loadUserRegistrations()
})
</script>

<style scoped>
.tournament-list-page {
  padding: 2rem;
  width: 90%;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 2rem;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 2rem;
}

.header-text {
  flex: 1;
}

.page-header h1 {
  font-size: 2.5rem;
  color: #1e293b;
  margin-bottom: 0.5rem;
}

.page-header p {
  color: #64748b;
  font-size: 1.1rem;
}

.filters-section {
  margin-bottom: 2rem;
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border: 1px solid #e2e8f0;
}

.search-bar {
  margin-bottom: 1rem;
}

.filter-controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.advanced-filters {
  border-top: 1px solid #e2e8f0;
  padding-top: 1rem;
  margin-bottom: 1rem;
}

.filter-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1rem;
  margin-bottom: 1rem;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.filter-group label {
  font-weight: 600;
  color: #374151;
  font-size: 0.875rem;
}

.slider-labels {
  display: flex;
  justify-content: space-between;
  font-size: 0.75rem;
  color: #64748b;
  margin-top: 0.25rem;
}

.active-filters {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  flex-wrap: wrap;
  padding-top: 1rem;
  border-top: 1px solid #e2e8f0;
}

.active-filters-label {
  font-weight: 600;
  color: #374151;
  font-size: 0.875rem;
}

.results-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  color: #64748b;
  font-size: 0.875rem;
}

.sort-controls {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  white-space: nowrap;
}

.sort-controls label {
  font-weight: 600;
  white-space: nowrap;
}

.sort-controls .n-select {
  min-width: 160px;
}

.sort-controls .n-base-select-menu {
  min-width: 160px;
}

.tournament-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 2rem;
  margin-bottom: 2rem;
}

.tournament-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  border: 1px solid #e2e8f0;
  transition: all 0.2s ease;
  cursor: pointer;
}

.tournament-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

.tournament-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.tournament-badge {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: #fff7ed;
  padding: 0.5rem 0.75rem;
  border-radius: 20px;
}

.sport-type {
  font-weight: 600;
  color: #f97316;
  font-size: 0.875rem;
}

.tournament-status {
  padding: 0.1075rem 0.775rem;
  border-radius: 20px;
  font-size: 0.7rem;
  font-weight: 600;
  text-transform: uppercase;
  white-space: nowrap;
}

.tournament-status.registration-open {
  background: #fef3c7;
  color: #92400e;
}

.tournament-status.in-progress {
  background: #dbeafe;
  color: #1e40af;
}

.tournament-status.completed {
  background: #dcfce7;
  color: #166534;
}

.tournament-status.registration-closed {
  background: #fee2e2;
  color: #991b1b;
}

.tournament-status.draft {
  background: #f3f4f6;
  color: #374151;
}

.registration-status-badge {
  margin: 0.75rem 0;
  display: flex;
  justify-content: flex-start;
}

.tournament-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 1rem;
  line-height: 1.4;
}

.tournament-meta {
  margin-bottom: 1rem;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
  color: #64748b;
  font-size: 0.875rem;
}

.tournament-participants {
  margin-bottom: 1.5rem;
}

.participant-count {
  display: block;
  color: #f97316;
  font-weight: 600;
  font-size: 0.875rem;
  margin-bottom: 0.5rem;
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
  background: #f97316;
  border-radius: 4px;
  transition: width 0.3s ease;
}

.tournament-actions {
  display: flex;
  gap: 0.75rem;
  justify-content: center;
}

.quick-register-btn {
  min-width: 120px;
  height: 40px;
  border-radius: 8px;
  flex-shrink: 0;
}

.view-details-btn {
  min-width: 120px;
  height: 40px;
  border-radius: 8px;
  flex-shrink: 0;
}

.host-badge {
  min-width: 120px;
  height: 40px;
  border-radius: 8px;
  flex-shrink: 0;
  opacity: 0.8;
}

.loading-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #64748b;
}

.loading-state p {
  margin-top: 1rem;
  font-size: 1rem;
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #64748b;
}

@media (max-width: 768px) {
  .tournament-list-page {
    padding: 1rem;
  }
  
  .tournament-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .page-header h1 {
    font-size: 2rem;
  }
  
  .filters-section {
    flex-direction: column;
    gap: 1rem;
  }
  
  .filter-row {
    grid-template-columns: 1fr;
  }
}

.empty-state h3 {
  margin: 1rem 0 0.5rem 0;
  color: #475569;
}

.empty-state p {
  font-size: 1rem;
}

@media (max-width: 768px) {
  .tournament-list-page {
    padding: 1rem;
  }
  
  .tournament-grid {
    grid-template-columns: 1fr;
  }
  
  .page-header h1 {
    font-size: 2rem;
  }

  .filter-row {
    grid-template-columns: 1fr;
  }

  .results-summary {
    flex-direction: column;
    gap: 1rem;
    align-items: flex-start;
  }
}

:deep(.n-base-select-option) {
  white-space: nowrap;
  overflow: visible;
}

:deep(.n-base-select-option__label) {
  white-space: nowrap;
  overflow: visible;
}
</style> 