<template>
  <div class="teams-page">
    <div class="page-header">
      <h1>Teams</h1>
      <p>Create and manage your teams for tournament participation</p>
    </div>

    <div class="teams-actions">
      <n-button type="primary" color="blue" @click="showCreateTeamModal = true">
        <template #icon>
          <n-icon><AddOutline /></n-icon>
        </template>
        Create New Team
      </n-button>
    </div>

    <div class="teams-sections" v-if="!loading">
      <div class="teams-section">
        <h2>My Teams</h2>
        <div class="teams-grid">
          <TeamCard
            v-for="(team, index) in paginatedMyTeams"
            :key="team?.id || index"
            :team="team"
            :show-role="true"
            :current-user-id="currentUserId"
            @manage-team="openTeamManagement"
          />
        </div>
        <div v-if="myTeams.length > 0" class="pagination-wrapper">
          <n-pagination
            v-model:page="myTeamsPage"
            v-model:page-size="myTeamsPageSize"
            :page-count="Math.ceil(myTeams.length / myTeamsPageSize)"
            show-size-picker
            :page-sizes="[5, 10, 20, 50]"
            @update:page="handleMyTeamsPageChange"
            @update:page-size="handleMyTeamsPageSizeChange"
            style="margin-top: 1rem;"
          />
        </div>
        <div v-if="myTeams.length === 0" class="empty-section">
          <p>You haven't joined any teams yet. Create or join a team to get started!</p>
        </div>
      </div>

      <div class="teams-section">
        <h2>Available Teams to Join</h2>
        <div class="teams-grid">
          <TeamCard
            v-for="(team, index) in paginatedAvailableTeams"
            :key="team?.id || index"
            :team="team"
            :show-role="false"
            @join-team="requestToJoinTeam"
          />
        </div>
        <div v-if="availableTeams.length > 0" class="pagination-wrapper">
          <n-pagination
            v-model:page="availableTeamsPage"
            v-model:page-size="availableTeamsPageSize"
            :page-count="Math.ceil(availableTeams.length / availableTeamsPageSize)"
            show-size-picker
            :page-sizes="[5, 10, 20, 50]"
            @update:page="handleAvailableTeamsPageChange"
            @update:page-size="handleAvailableTeamsPageSizeChange"
            style="margin-top: 1rem;"
          />
        </div>
        <div v-if="availableTeams.length === 0 && myTeams.length > 0" class="empty-section">
          <p>No teams available to join at the moment.</p>
        </div>
      </div>
    </div>

    <div v-if="loading" class="loading-state">
      <p>Loading teams...</p>
    </div>

    <div class="empty-state" v-if="!loading && myTeams.length === 0 && availableTeams.length === 0">
      <n-icon size="64" color="#cbd5e1">
        <PeopleOutline />
      </n-icon>
      <h3>No teams available</h3>
      <p>Create your first team or browse available teams to join</p>
    </div>

    <CreateTeamModal
      v-model:show="showCreateTeamModal"
      @team-created="handleTeamCreated"
    />

    <TeamManagementModal
      v-model:show="showTeamManagementModal"
      :team="selectedTeam"
      :is-captain="isCaptain"
      @team-updated="handleTeamUpdated"
      @member-removed="handleMemberRemoved"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { NButton, NIcon, useMessage, NPagination } from 'naive-ui'
import { AddOutline, PeopleOutline } from '@vicons/ionicons5'
import { UserTeamService } from '@/services/apis/UserTeamService'
import { TeamJoinRequestService } from '@/services/apis/TeamJoinRequestService'
import type { Team } from '@/models/Tournament'
import TeamCard from './TeamCard.vue'
import CreateTeamModal from './CreateTeamModal.vue'
import TeamManagementModal from './TeamManagementModal.vue'

const message = useMessage()
const myTeams = ref<Team[]>([])
const availableTeams = ref<Team[]>([])
const showCreateTeamModal = ref(false)
const showTeamManagementModal = ref(false)
const selectedTeam = ref<Team | null>(null)
const loading = ref(false)
const currentUserId = ref<string>('') 

const myTeamsPageSize = ref(10)
const availableTeamsPageSize = ref(10)
const myTeamsPage = ref(1)
const availableTeamsPage = ref(1)

const paginatedMyTeams = computed(() => {
  const start = (myTeamsPage.value - 1) * myTeamsPageSize.value
  return myTeams.value.slice(start, start + myTeamsPageSize.value)
})
const paginatedAvailableTeams = computed(() => {
  const start = (availableTeamsPage.value - 1) * availableTeamsPageSize.value
  return availableTeams.value.slice(start, start + availableTeamsPageSize.value)
})

const isCaptain = computed(() => {
  if (!selectedTeam.value || !selectedTeam.value.members || !currentUserId.value) return false
  const currentUserMember = selectedTeam.value.members.find(member => member && member.userId === currentUserId.value)
  return currentUserMember?.role === 'CAPTAIN'
})

const loadTeams = async () => {
  try {
    loading.value = true
    
    if (!currentUserId.value) {
      currentUserId.value = await TeamJoinRequestService.getCurrentUserId()
    }
        
    const [myTeamsData, availableTeamsData] = await Promise.all([
      UserTeamService.getMyTeams(),
      UserTeamService.getAvailableTeams()
    ])
    
    myTeams.value = Array.isArray(myTeamsData) ? myTeamsData : []
    availableTeams.value = Array.isArray(availableTeamsData) ? availableTeamsData : []
        
  } catch (error) {
    console.error('Failed to load teams:', error)
    message.error('Failed to load teams')
    myTeams.value = []
    availableTeams.value = []
  } finally {
    loading.value = false
  }
}

const openTeamManagement = (team: Team) => {
  selectedTeam.value = team
  showTeamManagementModal.value = true
}

const requestToJoinTeam = async (team: Team) => {
  try {
    loading.value = true
    await TeamJoinRequestService.sendJoinRequest(team.id)
    message.success(`Join request sent to ${team.name}! The team captain will review your request.`)
    await loadTeams()
  } catch (error) {
    console.error('Failed to send join request:', error)
    message.error(error instanceof Error ? error.message : 'Failed to send join request')
  } finally {
    loading.value = false
  }
}

const handleTeamCreated = async (newTeam: Team) => {
  message.success(`Team "${newTeam.name}" created successfully!`)
  await loadTeams()
}

const handleTeamUpdated = async (updatedTeam: Team) => {
  message.success('Team updated successfully!')
  await loadTeams()
  showTeamManagementModal.value = false
}

const handleMemberRemoved = async (memberId: string) => {
  message.success('Member removed successfully!')
  await loadTeams()
}

const handleMyTeamsPageChange = (page: number) => {
  myTeamsPage.value = page
}

const handleMyTeamsPageSizeChange = (size: number) => {
  myTeamsPageSize.value = size
  myTeamsPage.value = 1
}

const handleAvailableTeamsPageChange = (page: number) => {
  availableTeamsPage.value = page
}

const handleAvailableTeamsPageSizeChange = (size: number) => {
  availableTeamsPageSize.value = size
  availableTeamsPage.value = 1
}

onMounted(() => {
  loadTeams()
})
</script>

<style scoped>
.teams-page {
  padding: 2rem;
  width: 90%;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 2rem;
  text-align: center;
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

.teams-actions {
  margin-bottom: 2rem;
  display: flex;
  justify-content: flex-end;
}

.teams-sections {
  display: flex;
  flex-direction: column;
  gap: 3rem;
}

.teams-section h2 {
  font-size: 1.5rem;
  color: #1e293b;
  margin-bottom: 1.5rem;
  font-weight: 600;
}

.teams-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 1.5rem;
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #64748b;
}

.empty-state h3 {
  margin: 1rem 0 0.5rem 0;
  color: #475569;
}

.empty-state p {
  font-size: 1rem;
}

.loading-state {
  text-align: center;
  padding: 2rem;
  color: #64748b;
  font-size: 1.1rem;
}

.empty-section {
  text-align: center;
  padding: 2rem;
  color: #64748b;
  font-style: italic;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 1rem;
}

@media (max-width: 768px) {
  .teams-page {
    padding: 1rem;
  }
  
  .teams-grid {
    grid-template-columns: 1fr;
  }
  
  .page-header h1 {
    font-size: 2rem;
  }
}
</style> 