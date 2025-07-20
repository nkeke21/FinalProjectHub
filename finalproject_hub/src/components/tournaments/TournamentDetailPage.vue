<template>
  <div class="tournament-detail-page">
    <div v-if="loading" class="loading-state">
      <n-spin size="large" />
      <p>Loading tournament details...</p>
    </div>

    <div v-else-if="!tournament" class="not-found">
      <n-icon size="64" color="#cbd5e1">
        <AlertCircleOutline />
      </n-icon>
      <h3>Tournament not found</h3>
      <p>The tournament you're looking for doesn't exist or has been removed.</p>
      <n-button @click="$router.push('/tournaments')">Back to Tournaments</n-button>
    </div>

    <div v-else class="tournament-content">
      <div class="tournament-header">
        <div class="header-content">
          <div class="tournament-badge">
            <n-icon size="24" color="#3b82f6">
              <FootballOutline v-if="tournament.sportType === 'Football'" />
              <BasketballOutline v-else-if="tournament.sportType === 'Basketball'" />
              <TennisballOutline v-else-if="tournament.sportType === 'Tennis'" />
              <FitnessOutline v-else />
            </n-icon>
            <span class="sport-type">{{ tournament.sportType }}</span>
          </div>
          
          <div class="status-badge" :class="tournament.status.toLowerCase().replace(' ', '-')">
            {{ tournament.status }}
          </div>
        </div>

        <h1 class="tournament-title">{{ tournament.name }}</h1>
        <p class="tournament-description">{{ tournament.description }}</p>

        <div class="tournament-meta">
          <div class="meta-item">
            <n-icon size="16" color="#64748b">
              <PersonOutline />
            </n-icon>
            <span>Hosted by {{ tournament.hostName }}</span>
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
            <span>{{ formatDate(tournament.startDate) }} - {{ formatDate(tournament.endDate) }}</span>
          </div>
        </div>
      </div>

      <!-- Quick Stats -->
      <div class="quick-stats">
        <div class="stat-card">
          <div class="stat-value">{{ tournament.currentParticipants }}/{{ tournament.maxParticipants }}</div>
          <div class="stat-label">Participants</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ tournament.entryFee === 0 ? 'Free' : `${tournament.entryFee}₾` }}</div>
          <div class="stat-label">Entry Fee</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ tournament.prizePool }}₾</div>
          <div class="stat-label">Prize Pool</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">{{ tournament.format }}</div>
          <div class="stat-label">Format</div>
        </div>
      </div>

      <n-tabs type="line" animated class="tournament-tabs">
        <n-tab-pane name="overview" tab="Overview">
          <div class="overview-content">
            <div class="overview-section">
              <h3>Tournament Details</h3>
              <div class="details-grid">
                <div class="detail-item">
                  <span class="detail-label">Registration Deadline:</span>
                  <span class="detail-value">{{ formatDate(tournament.registrationDeadline) }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">Age Range:</span>
                  <span class="detail-value">{{ tournament.ageRange.min }}-{{ tournament.ageRange.max }} years</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">Created:</span>
                  <span class="detail-value">{{ formatDate(tournament.createdAt) }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">Last Updated:</span>
                  <span class="detail-value">{{ formatDate(tournament.updatedAt) }}</span>
                </div>
              </div>
            </div>

            <div class="overview-section">
              <h3>Tournament Rules</h3>
              <div class="rules-list">
                <div v-for="(rule, index) in tournament.rules" :key="index" class="rule-item">
                  <n-icon size="16" color="#3b82f6">
                    <CheckmarkCircleOutline />
                  </n-icon>
                  <span>{{ rule }}</span>
                </div>
              </div>
            </div>

            <div class="overview-section">
              <h3>Registration Status</h3>
              <div class="registration-status">
                <div class="status-info">
                  <div class="participants-progress">
                    <span class="progress-text">{{ tournament.currentParticipants }} of {{ tournament.maxParticipants }} spots filled</span>
                    <div class="progress-bar">
                      <div 
                        class="progress-fill" 
                        :style="{ width: (tournament.currentParticipants / tournament.maxParticipants * 100) + '%' }"
                      ></div>
                    </div>
                  </div>
                  
                  <div class="registration-actions">
                    <div v-if="registrationLoading" class="registration-loading">
                      <n-spin size="small" />
                      <span>Loading registration status...</span>
                    </div>
                    <div v-else-if="isUserProfileLoading" class="host-message">
                      <n-spin size="small" />
                      <span>Loading user profile...</span>
                    </div>
                    <div v-else-if="isTournamentHost" class="host-message">
                      <n-icon size="20" color="#3b82f6">
                        <PersonOutline />
                      </n-icon>
                      <span>You are the tournament host</span>
                      <n-button 
                        type="primary" 
                        size="small"
                        @click="showNotificationModal = true"
                        color="#3b82f6"
                        style="margin-left: 1rem;"
                      >
                        View Registration Requests
                        <n-badge v-if="unreadNotificationCount > 0" :value="unreadNotificationCount" />
                      </n-button>
                    </div>
                    <n-button
                      v-else-if="canRegister"
                      type="primary"
                      size="large"
                      @click="showRegistrationForm = true"
                      color="#3b82f6"
                    >
                      Register for Tournament
                    </n-button>
                    <n-button 
                      v-else-if="isRegistered" 
                      type="warning" 
                      size="large"
                      @click="withdrawFromTournament"
                      :loading="withdrawing"
                    >
                      Withdraw Registration
                    </n-button>
                    <div v-else-if="isPending" class="pending-message">
                      <n-icon size="20" color="#f59e0b">
                        <TimeOutline />
                      </n-icon>
                      <span>Registration request pending approval</span>
                    </div>
                    <n-button 
                      v-else 
                      type="info" 
                      size="large"
                      disabled
                    >
                      Already Registered
                    </n-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </n-tab-pane>

        <n-tab-pane name="participants" tab="Participants">
          <div class="participants-content">
            <div class="participants-header">
              <h3>Tournament Participants ({{ participants.length }})</h3>
              <div class="participants-filter">
                <n-select
                  v-model:value="participantFilter"
                  :options="participantFilterOptions"
                  placeholder="Filter by status"
                  style="width: 200px"
                />
              </div>
            </div>

            <div class="participants-list">
              <div v-for="participant in filteredParticipants" :key="participant.id" class="participant-card">
                <div class="participant-info">
                  <div class="participant-name">{{ participant.participantName }}</div>
                  <div class="participant-type">{{ participant.participantType }}</div>
                  <div class="participant-status" :class="participant.status.toLowerCase()">
                    {{ participant.status }}
                  </div>
                </div>
                <div class="participant-meta">
                  <span class="registration-date">Registered {{ formatDate(participant.registrationDate) }}</span>
                </div>
              </div>
            </div>

            <div v-if="filteredParticipants.length === 0" class="empty-participants">
              <n-icon size="48" color="#cbd5e1">
                <PeopleOutline />
              </n-icon>
              <p>No participants found</p>
            </div>
          </div>
        </n-tab-pane>
      </n-tabs>
    </div>
    
    <RegistrationConfirmationModal
      v-model:show="showConfirmationModal"
      :type="confirmationType"
      :title="confirmationTitle"
      :message="confirmationMessage"
      @confirm="handleConfirmationConfirm"
    />
    
    <!-- Tournament Registration Form Modal -->
    <TournamentRegistrationForm
      v-model:show="showRegistrationForm"
      :tournament="tournament"
      @registration-success="handleRegistrationFormSuccess"
      @registration-error="handleRegistrationFormError"
    />
    
    <!-- Tournament Registration Notification Modal -->
    <TournamentRegistrationNotificationModal
      v-model:show="showNotificationModal"
      @notifications-updated="handleNotificationsUpdated"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { 
  NSpin, 
  NIcon, 
  NButton, 
  NTabs, 
  NTabPane, 
  NSelect 
} from 'naive-ui'
import { 
  FootballOutline,
  BasketballOutline,
  TennisballOutline,
  FitnessOutline,
  PeopleOutline,
  CalendarOutline,
  PersonOutline,
  LocationOutline,
  AlertCircleOutline,
  CheckmarkCircleOutline,
  TimeOutline
} from '@vicons/ionicons5'
import type { 
  Tournament, 
  TournamentParticipant,
  ParticipantStatus
} from '@/models/Tournament'
import { TournamentService } from '@/services/apis/TournamentService'
import { TournamentRegistrationService } from '@/services/apis/TournamentRegistrationService'
import { TournamentParticipantService } from '@/services/apis/TournamentParticipantService'
import type { TournamentRegistration } from '@/models/TournamentRegistration'
import { useUserStore } from '@/store/profile/userStore'
import RegistrationConfirmationModal from './RegistrationConfirmationModal.vue'
import TournamentRegistrationForm from './TournamentRegistrationForm.vue'
import TournamentRegistrationNotificationModal from './TournamentRegistrationNotificationModal.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const loading = ref(true)
const tournament = ref<Tournament | null>(null)
const participants = ref<TournamentParticipant[]>([])
const registering = ref(false)
const withdrawing = ref(false)
const participantFilter = ref<string>('Confirmed')

const userRegistration = ref<TournamentRegistration | null>(null)
const canRegister = ref(false)
const registrationLoading = ref(false)

const showConfirmationModal = ref(false)
const confirmationType = ref<'success' | 'error'>('success')
const confirmationTitle = ref('')
const confirmationMessage = ref('')

// Registration form state
const showRegistrationForm = ref(false)
const showNotificationModal = ref(false)
const unreadNotificationCount = ref(0)

const isRegistered = computed(() => {
  return userRegistration.value?.status === 'REGISTERED'
})

const isPending = computed(() => {
  return userRegistration.value?.status === 'PENDING'
})

const isTournamentHost = computed(() => {
  if (!tournament.value || !userStore.profile) return false
  return String(tournament.value.hostId) === String(userStore.profile.id)
})

const isUserProfileLoading = computed(() => {
  return userStore.isLoading
})

const participantFilterOptions = [
  { label: 'Confirmed', value: 'Confirmed' }
]

const filteredParticipants = computed(() => {
  return participants.value.filter(p => p.status === participantFilter.value)
})

const loadTournamentData = async () => {
  try {
    loading.value = true
    const tournamentId = route.params.id as string
    
    const tournamentResponse = await TournamentService.getTournamentById(tournamentId)
    const tournamentData = TournamentService.convertToTournament(tournamentResponse)
    
    tournament.value = tournamentData
    
    await Promise.all([
      loadRegistrationData(tournamentId),
      loadParticipants(tournamentId)
    ])
    
  } catch (error) {
    console.error('Error loading tournament data:', error)
    tournament.value = null
  } finally {
    loading.value = false
  }
}

const loadRegistrationData = async (tournamentId: string) => {
  try {
    registrationLoading.value = true
    
    userRegistration.value = await TournamentRegistrationService.getUserRegistration(tournamentId)
    
    canRegister.value = await TournamentRegistrationService.canRegisterForTournament(tournamentId)
    
  } catch (error) {
    console.error('Error loading registration data:', error)
  } finally {
    registrationLoading.value = false
  }
}

const loadParticipants = async (tournamentId: string) => {
  try {
    const registrations = await TournamentRegistrationService.getTournamentRegistrations(tournamentId)
    participants.value = TournamentParticipantService.convertRegistrationsToParticipants(registrations)
  } catch (error) {
    console.error('Error loading participants:', error)
    participants.value = []
  }
}

const registerForTournament = async () => {
  if (!tournament.value) return
  
  try {
    registering.value = true
    
    const response = await TournamentRegistrationService.registerForTournament({
      tournamentId: tournament.value.id
    })
    
    if (response.success && response.registration) {
      userRegistration.value = response.registration
      canRegister.value = false
      showConfirmation('success', 'Registration Successful', 'You have been successfully registered for this tournament!')
    } else {
      showConfirmation('error', 'Registration Failed', response.message)
    }
  } catch (error) {
    console.error('Error registering for tournament:', error)
  } finally {
    registering.value = false
  }
}

const withdrawFromTournament = async () => {
  if (!tournament.value) return
  
  try {
    withdrawing.value = true
    
    const response = await TournamentRegistrationService.withdrawFromTournament(tournament.value.id)
    
    if (response.success && response.registration) {
      userRegistration.value = response.registration
      canRegister.value = true
      showConfirmation('success', 'Withdrawal Successful', 'You have successfully withdrawn from this tournament.')
    } else {
      showConfirmation('error', 'Withdrawal Failed', response.message)
    }
  } catch (error) {
    console.error('Error withdrawing from tournament:', error)
  } finally {
    withdrawing.value = false
  }
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('en-US', { 
    year: 'numeric', 
    month: 'long', 
    day: 'numeric' 
  })
}

const showConfirmation = (type: 'success' | 'error', title: string, message: string) => {
  confirmationType.value = type
  confirmationTitle.value = title
  confirmationMessage.value = message
  showConfirmationModal.value = true
}

const handleConfirmationConfirm = () => {
  showConfirmationModal.value = false
}

const handleRegistrationFormSuccess = async () => {
  // Reload registration data to update the UI
  if (tournament.value) {
    await loadRegistrationData(tournament.value.id)
  }
  showConfirmation('success', 'Registration Successful', 'You have been successfully registered for this tournament!')
}

const handleRegistrationFormError = (message: string) => {
  showConfirmation('error', 'Registration Failed', message)
}

const handleNotificationsUpdated = async () => {
  if (tournament.value) {
    await Promise.all([
      loadTournamentData(),
      loadParticipants(tournament.value.id)
    ])
  }
}

onMounted(async () => {
  if (!userStore.profile) {
    await userStore.fetchCurrentUserProfile()
  }
  loadTournamentData()
})
</script>

<style scoped>
.tournament-detail-page {
  padding: 2rem;
  width: 90%;
  margin: 0 auto;
}

.loading-state,
.not-found {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 2rem;
  text-align: center;
}

.loading-state p,
.not-found p {
  margin-top: 1rem;
  color: #64748b;
}

.not-found h3 {
  margin: 1rem 0 0.5rem 0;
  color: #475569;
}

.tournament-header {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  border: 1px solid #e2e8f0;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.tournament-badge {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: #eff6ff;
  padding: 0.5rem 0.75rem;
  border-radius: 20px;
}

.sport-type {
  font-weight: 600;
  color: #3b82f6;
  font-size: 0.875rem;
}

.status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
}

.status-badge.registration-open {
  background: #dcfce7;
  color: #166534;
}

.status-badge.in-progress {
  background: #dbeafe;
  color: #1e40af;
}

.status-badge.completed {
  background: #fef3c7;
  color: #92400e;
}

.status-badge.cancelled {
  background: #fee2e2;
  color: #991b1b;
}

.tournament-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 1rem;
  line-height: 1.2;
}

.tournament-description {
  color: #64748b;
  font-size: 1.1rem;
  line-height: 1.6;
  margin-bottom: 1.5rem;
}

.tournament-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #64748b;
  font-size: 0.875rem;
}

.quick-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
  margin-bottom: 2rem;
}

.stat-card {
  background: white;
  border-radius: 8px;
  padding: 1.5rem;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border: 1px solid #e2e8f0;
}

.stat-value {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 0.5rem;
}

.stat-label {
  color: #64748b;
  font-size: 0.875rem;
  font-weight: 500;
}

.tournament-tabs {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  border: 1px solid #e2e8f0;
}

.tournament-tabs :deep(.n-tabs-nav) {
  padding: 0 2rem;
}

.tournament-tabs :deep(.n-tabs-tab) {
  padding: 1rem 0;
}

.tournament-tabs :deep(.n-tabs-tab-pad) {
  padding-left: 0;
  padding-right: 0;
}

.overview-content {
  padding: 2.5rem;
}

.overview-section {
  margin-bottom: 2rem;
}

.overview-section h3 {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 1rem;
}

.details-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1rem;
}

.detail-item {
  display: flex;
  align-items: center;
  padding: 0.75rem;
  background: #f8fafc;
  border-radius: 8px;
  gap: 0.75rem;
}

.detail-label {
  font-weight: 500;
  color: #64748b;
}

.detail-value {
  color: #1e293b;
  font-weight: 600;
}

.rules-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.rule-item {
  display: flex;
  align-items: flex-start;
  gap: 0.75rem;
  padding: 0.75rem;
  background: #f8fafc;
  border-radius: 8px;
}

.registration-status {
  padding: 1.5rem;
  background: #f8fafc;
  border-radius: 8px;
}

.status-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 2rem;
}

.participants-progress {
  flex: 1;
}

.progress-text {
  display: block;
  color: #64748b;
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
  background: #3b82f6;
  border-radius: 4px;
  transition: width 0.3s ease;
}

.registration-loading {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #64748b;
  font-size: 0.875rem;
}

.host-message {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #3b82f6;
  font-size: 0.875rem;
  font-weight: 500;
  padding: 0.75rem 1rem;
  background: #eff6ff;
  border-radius: 8px;
  border: 1px solid #dbeafe;
}

.participants-content,
.matches-content,
.bracket-content {
  padding: 2rem;
}

.participants-header,
.matches-header,
.bracket-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.participants-header h3,
.matches-header h3,
.bracket-header h3 {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1e293b;
  margin: 0;
}

.participants-list,
.matches-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.participant-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

.participant-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.participant-name {
  font-weight: 600;
  color: #1e293b;
}

.participant-type {
  color: #64748b;
  font-size: 0.875rem;
  text-transform: capitalize;
}

.participant-status {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
}

.participant-status.confirmed {
  background: #dcfce7;
  color: #166534;
}

.participant-status.registered {
  background: #dbeafe;
  color: #1e40af;
}

.participant-status.waitlisted {
  background: #fef3c7;
  color: #92400e;
}

.participant-status.withdrawn {
  background: #fee2e2;
  color: #991b1b;
}

.participant-meta {
  color: #64748b;
  font-size: 0.875rem;
}

.empty-participants {
  text-align: center;
  padding: 4rem 2rem;
  color: #64748b;
}

.empty-participants p {
  margin-top: 1rem;
  font-size: 1rem;
}

/* Responsive design */
@media (max-width: 768px) {
  .tournament-detail-page {
    padding: 1rem;
  }
  
  .tournament-title {
    font-size: 2rem;
  }
  
  .quick-stats {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .status-info {
    flex-direction: column;
    align-items: stretch;
    gap: 1rem;
  }
  
  .bracket-visualization {
    flex-direction: column;
  }
  
  .bracket-round {
    min-width: auto;
  }
}
</style> 