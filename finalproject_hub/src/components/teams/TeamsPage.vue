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

    <div class="teams-sections">
      <div class="teams-section">
        <h2>My Teams</h2>
        <div class="teams-grid">
          <TeamCard
            v-for="team in myTeams"
            :key="team.id"
            :team="team"
            :show-role="true"
            :current-user-id="currentUserId"
            @manage-team="openTeamManagement"
          />
        </div>
      </div>

      <div class="teams-section">
        <h2>Available Teams to Join</h2>
        <div class="teams-grid">
          <TeamCard
            v-for="team in availableTeams"
            :key="team.id"
            :team="team"
            :show-role="false"
            @join-team="requestToJoinTeam"
          />
        </div>
      </div>
    </div>

    <div class="empty-state" v-if="myTeams.length === 0 && availableTeams.length === 0">
      <n-icon size="64" color="#cbd5e1">
        <PeopleOutline />
      </n-icon>
      <h3>No teams available</h3>
      <p>Create your first team or browse available teams to join</p>
    </div>

    <!-- Create Team Modal -->
    <CreateTeamModal
      v-model:show="showCreateTeamModal"
      @team-created="handleTeamCreated"
    />

    <!-- Team Management Modal -->
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
import { ref, computed } from 'vue'
import { NButton, NIcon } from 'naive-ui'
import { AddOutline, PeopleOutline } from '@vicons/ionicons5'
import { mockTeams } from '@/data/mockTournaments'
import type { Team } from '@/models/Tournament'
import TeamCard from './TeamCard.vue'
import CreateTeamModal from './CreateTeamModal.vue'
import TeamManagementModal from './TeamManagementModal.vue'

const teams = ref<Team[]>(mockTeams)
const showCreateTeamModal = ref(false)
const showTeamManagementModal = ref(false)
const selectedTeam = ref<Team | null>(null)
const currentUserId = 'user-1'

const myTeams = computed(() => 
  teams.value.filter(team => 
    team.members.some(member => member.userId === currentUserId || member.userId === 'user-3')
  )
)

const availableTeams = computed(() => 
  teams.value.filter(team => 
    !team.members.some(member => member.userId === currentUserId || member.userId === 'user-3') &&
    team.members.length < team.maxMembers
  )
)

const isCaptain = computed(() => {
  if (!selectedTeam.value) return false
  const user = selectedTeam.value.members.find(member => member.userId === currentUserId)
  return user?.role === 'CAPTAIN'
})

const openTeamManagement = (team: Team) => {
  selectedTeam.value = team
  showTeamManagementModal.value = true
}

const requestToJoinTeam = (team: Team) => {
  console.log('Requesting to join team:', team.name)
}

const handleTeamCreated = (newTeam: Team) => {
  teams.value.push(newTeam)
}

const handleTeamUpdated = (updatedTeam: Team) => {
  const index = teams.value.findIndex(team => team.id === updatedTeam.id)
  if (index !== -1) {
    teams.value[index] = updatedTeam
  }
}

const handleMemberRemoved = (memberId: string) => {
  console.log('Member removed:', memberId)
}
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