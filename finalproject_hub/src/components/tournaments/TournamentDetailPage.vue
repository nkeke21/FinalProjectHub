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

      <div class="quick-stats">
        <div class="stat-card">
          <div class="stat-value">{{ tournament.currentParticipants }}/{{ tournament.maxParticipants }}</div>
          <div class="stat-label">Participants</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${{ tournament.entryFee }}</div>
          <div class="stat-label">Entry Fee</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${{ tournament.prizePool }}</div>
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
                    <n-button 
                      v-if="canRegister" 
                      type="primary" 
                      size="large"
                      @click="registerForTournament"
                      :loading="registering"
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
                    <n-button 
                      v-else 
                      type="info" 
                      size="large"
                      disabled
                    >
                      Registration Closed
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

        <n-tab-pane name="matches" tab="Matches">
          <div class="matches-content">
            <div class="matches-header">
              <h3>Tournament Matches</h3>
              <div class="matches-filter">
                <n-select
                  v-model:value="matchFilter"
                  :options="matchFilterOptions"
                  placeholder="Filter by status"
                  style="width: 200px"
                />
              </div>
            </div>

            <div class="matches-list">
              <div v-for="match in filteredMatches" :key="match.id" class="match-card">
                <div class="match-header">
                  <div class="match-round">Round {{ match.roundNumber }}</div>
                  <div class="match-status" :class="match.status.toLowerCase().replace(' ', '-')">
                    {{ match.status }}
                  </div>
                </div>
                
                <div class="match-content">
                  <div class="player player1" :class="{ winner: match.winnerId === match.player1Id }">
                    <span class="player-name">{{ match.player1Name }}</span>
                  </div>
                  
                  <div class="match-vs">
                    <span class="vs-text">VS</span>
                    <div v-if="match.score" class="match-score">{{ match.score }}</div>
                  </div>
                  
                  <div class="player player2" :class="{ winner: match.winnerId === match.player2Id }">
                    <span class="player-name">{{ match.player2Name }}</span>
                  </div>
                </div>

                <div class="match-meta">
                  <span v-if="match.scheduledTime" class="match-time">
                    {{ formatDateTime(match.scheduledTime) }}
                  </span>
                  <span v-if="match.completedAt" class="match-completed">
                    Completed {{ formatDateTime(match.completedAt) }}
                  </span>
                </div>
              </div>
            </div>

            <div v-if="filteredMatches.length === 0" class="empty-matches">
              <n-icon size="48" color="#cbd5e1">
                <TrophyOutline />
              </n-icon>
              <p>No matches scheduled yet</p>
            </div>
          </div>
        </n-tab-pane>

        <n-tab-pane name="bracket" tab="Bracket">
          <div class="bracket-content">
            <div class="bracket-header">
              <h3>Tournament Bracket</h3>
            </div>

            <div v-if="tournamentBracket" class="bracket-visualization">
              <div v-for="round in tournamentBracket.rounds" :key="round.roundNumber" class="bracket-round">
                <h4 class="round-title">{{ round.roundName }}</h4>
                <div class="round-matches">
                  <div v-for="match in round.matches" :key="match.id" class="bracket-match">
                    <div class="bracket-player" :class="{ winner: match.winnerId === match.player1Id }">
                      {{ match.player1Name }}
                    </div>
                    <div class="bracket-player" :class="{ winner: match.winnerId === match.player2Id }">
                      {{ match.player2Name }}
                    </div>
                    <div v-if="match.score" class="bracket-score">{{ match.score }}</div>
                  </div>
                </div>
              </div>
            </div>

            <div v-else class="empty-bracket">
              <n-icon size="48" color="#cbd5e1">
                <TrophyOutline />
              </n-icon>
              <p>Bracket will be generated once registration closes</p>
            </div>
          </div>
        </n-tab-pane>
      </n-tabs>
    </div>
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
  TrophyOutline
} from '@vicons/ionicons5'
import { 
  getTournamentById, 
  getTournamentParticipants, 
  getTournamentMatches, 
  getTournamentBracket 
} from '@/data/mockTournaments'
import type { 
  Tournament, 
  TournamentParticipant, 
  TournamentMatch, 
  TournamentBracket,
  ParticipantStatus,
  MatchStatus 
} from '@/models/Tournament'

const route = useRoute()
const router = useRouter()

const loading = ref(true)
const tournament = ref<Tournament | null>(null)
const participants = ref<TournamentParticipant[]>([])
const matches = ref<TournamentMatch[]>([])
const tournamentBracket = ref<TournamentBracket | null>(null)
const registering = ref(false)
const withdrawing = ref(false)

const participantFilter = ref<string>('all')
const matchFilter = ref<string>('all')

const canRegister = computed(() => {
  if (!tournament.value) return false
  return tournament.value.status === 'Registration Open' && 
         tournament.value.currentParticipants < tournament.value.maxParticipants
})

const isRegistered = computed(() => {
  return participants.value.some(p => 
    p.participantId === 'user-1' && p.status === 'Confirmed'
  )
})

const participantFilterOptions = [
  { label: 'All Participants', value: 'all' },
  { label: 'Confirmed', value: 'Confirmed' },
  { label: 'Registered', value: 'Registered' },
  { label: 'Waitlisted', value: 'Waitlisted' },
  { label: 'Withdrawn', value: 'Withdrawn' }
]

const matchFilterOptions = [
  { label: 'All Matches', value: 'all' },
  { label: 'Scheduled', value: 'Scheduled' },
  { label: 'In Progress', value: 'In Progress' },
  { label: 'Completed', value: 'Completed' },
  { label: 'Cancelled', value: 'Cancelled' }
]

const filteredParticipants = computed(() => {
  if (participantFilter.value === 'all') return participants.value
  return participants.value.filter(p => p.status === participantFilter.value)
})

const filteredMatches = computed(() => {
  if (matchFilter.value === 'all') return matches.value
  return matches.value.filter(m => m.status === matchFilter.value)
})

const loadTournamentData = async () => {
  try {
    loading.value = true
    const tournamentId = route.params.id as string
    
    const tournamentData = getTournamentById(tournamentId)
    if (!tournamentData) {
      tournament.value = null
      return
    }
    
    tournament.value = tournamentData
    
    participants.value = getTournamentParticipants(tournamentId)
    matches.value = getTournamentMatches(tournamentId)
    tournamentBracket.value = getTournamentBracket(tournamentId) || null
    
  } catch (error) {
    console.error('Error loading tournament data:', error)
  } finally {
    loading.value = false
  }
}

const registerForTournament = async () => {
  if (!tournament.value) return
  
  try {
    registering.value = true
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    const newParticipant: TournamentParticipant = {
      id: `participant-${Date.now()}`,
      tournamentId: tournament.value.id,
      participantId: 'user-1',
      participantName: 'John Doe',
      participantType: 'individual',
      registrationDate: new Date().toISOString(),
      status: 'Confirmed'
    }
    
    participants.value.push(newParticipant)
    tournament.value.currentParticipants++
    
    console.log('Successfully registered for tournament')
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
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    const userParticipant = participants.value.find(p => p.participantId === 'user-1')
    if (userParticipant) {
      userParticipant.status = 'Withdrawn'
      tournament.value.currentParticipants--
    }
    
    console.log('Successfully withdrew from tournament')
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

const formatDateTime = (dateString: string) => {
  return new Date(dateString).toLocaleString('en-US', { 
    year: 'numeric', 
    month: 'short', 
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
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
  display: inline-block;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
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

.match-card {
  padding: 1.5rem;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

.match-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.match-round {
  font-weight: 600;
  color: #1e293b;
}

.match-status {
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
}

.match-status.scheduled {
  background: #dbeafe;
  color: #1e40af;
}

.match-status.in-progress {
  background: #fef3c7;
  color: #92400e;
}

.match-status.completed {
  background: #dcfce7;
  color: #166534;
}

.match-status.cancelled {
  background: #fee2e2;
  color: #991b1b;
}

.match-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
}

.player {
  flex: 1;
  padding: 0.75rem;
  background: white;
  border-radius: 6px;
  text-align: center;
  border: 2px solid transparent;
}

.player.winner {
  border-color: #10b981;
  background: #f0fdf4;
}

.player-name {
  font-weight: 600;
  color: #1e293b;
}

.match-vs {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0 1rem;
}

.vs-text {
  font-weight: 600;
  color: #64748b;
  font-size: 0.875rem;
}

.match-score {
  font-weight: 700;
  color: #1e293b;
  font-size: 1.125rem;
  margin-top: 0.25rem;
}

.match-meta {
  display: flex;
  justify-content: center;
  gap: 1rem;
  color: #64748b;
  font-size: 0.875rem;
}

.bracket-visualization {
  display: flex;
  gap: 2rem;
  overflow-x: auto;
  padding: 1rem 0;
}

.bracket-round {
  min-width: 250px;
}

.round-title {
  font-size: 1rem;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 1rem;
  text-align: center;
}

.round-matches {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.bracket-match {
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  overflow: hidden;
}

.bracket-player {
  padding: 0.5rem 0.75rem;
  border-bottom: 1px solid #e2e8f0;
  font-size: 0.875rem;
}

.bracket-player:last-child {
  border-bottom: none;
}

.bracket-player.winner {
  background: #f0fdf4;
  color: #166534;
  font-weight: 600;
}

.bracket-score {
  padding: 0.25rem 0.75rem;
  background: #f8fafc;
  text-align: center;
  font-weight: 600;
  color: #1e293b;
  font-size: 0.75rem;
}

.empty-participants,
.empty-matches,
.empty-bracket {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem 2rem;
  color: #64748b;
  text-align: center;
}

.empty-participants p,
.empty-matches p,
.empty-bracket p {
  margin-top: 1rem;
}

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