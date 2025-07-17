<template>
  <div class="teams-page">
    <div class="page-header">
      <h1>Teams</h1>
      <p>Create and manage your teams for tournament participation</p>
    </div>

    <div class="teams-actions">
      <n-button type="primary" color="blue">
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
          <div class="team-card" v-for="team in myTeams" :key="team.id">
            <div class="team-header">
              <div class="team-badge">
                <n-icon size="20" color="#3b82f6">
                  <FootballOutline v-if="team.sportType === 'Football'" />
                  <BasketballOutline v-else-if="team.sportType === 'Basketball'" />
                  <TennisballOutline v-else-if="team.sportType === 'Tennis'" />
                  <FitnessOutline v-else />
                </n-icon>
                <span class="sport-type">{{ team.sportType }}</span>
              </div>
              <div class="team-role" :class="getUserRole(team).toLowerCase()">
                {{ getUserRole(team) }}
              </div>
            </div>
            
            <h3 class="team-name">{{ team.name }}</h3>
            
            <div class="team-meta">
              <div class="meta-item">
                <n-icon size="16" color="#64748b">
                  <PersonOutline />
                </n-icon>
                <span>{{ team.captainName }}</span>
              </div>
              <div class="meta-item">
                <n-icon size="16" color="#64748b">
                  <LocationOutline />
                </n-icon>
                <span>{{ team.location }}</span>
              </div>
              <div class="meta-item">
                <n-icon size="16" color="#64748b">
                  <CalendarOutline />
                </n-icon>
                <span>Created {{ formatDate(team.createdAt) }}</span>
              </div>
              <div class="meta-item">
                <n-icon size="16" color="#64748b">
                  <PeopleOutline />
                </n-icon>
                <span>{{ team.ageRange.min }}-{{ team.ageRange.max }} years</span>
              </div>
            </div>

            <div class="team-participants">
              <span class="participant-count">{{ team.members.length }}/{{ team.maxMembers }} members</span>
              <div class="progress-bar">
                <div class="progress-fill" :style="{ width: (team.members.length / team.maxMembers * 100) + '%' }"></div>
              </div>
            </div>

            <div class="team-actions">
              <n-button type="primary" color="blue" class="manage-team-btn">
                Manage Team
              </n-button>
            </div>
          </div>
        </div>
      </div>

      <div class="teams-section">
        <h2>Available Teams to Join</h2>
        <div class="teams-grid">
          <div class="team-card" v-for="team in availableTeams" :key="team.id">
            <div class="team-header">
              <div class="team-badge">
                <n-icon size="20" color="#3b82f6">
                  <FootballOutline v-if="team.sportType === 'Football'" />
                  <BasketballOutline v-else-if="team.sportType === 'Basketball'" />
                  <TennisballOutline v-else-if="team.sportType === 'Tennis'" />
                  <FitnessOutline v-else />
                </n-icon>
                <span class="sport-type">{{ team.sportType }}</span>
              </div>
              <div class="team-privacy">
                <n-icon size="16" color="#64748b">
                  <GlobeOutline />
                </n-icon>
              </div>
            </div>
            
            <h3 class="team-name">{{ team.name }}</h3>
            
            <div class="team-meta">
              <div class="meta-item">
                <n-icon size="16" color="#64748b">
                  <PersonOutline />
                </n-icon>
                <span>Captain: {{ team.captainName }}</span>
              </div>
              <div class="meta-item">
                <n-icon size="16" color="#64748b">
                  <LocationOutline />
                </n-icon>
                <span>{{ team.location }}</span>
              </div>
              <div class="meta-item">
                <n-icon size="16" color="#64748b">
                  <CalendarOutline />
                </n-icon>
                <span>Created {{ formatDate(team.createdAt) }}</span>
              </div>
              <div class="meta-item">
                <n-icon size="16" color="#64748b">
                  <PeopleOutline />
                </n-icon>
                <span>{{ team.ageRange.min }}-{{ team.ageRange.max }} years</span>
              </div>
            </div>

            <div class="team-participants">
              <span class="participant-count">{{ team.members.length }}/{{ team.maxMembers }} members</span>
              <div class="progress-bar">
                <div class="progress-fill" :style="{ width: (team.members.length / team.maxMembers * 100) + '%' }"></div>
              </div>
            </div>

            <div class="team-actions">
              <n-button type="primary" color="blue" class="join-team-btn">
                Request to Join
              </n-button>
            </div>
          </div>
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
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { 
  NButton, 
  NIcon 
} from 'naive-ui'
import { 
  AddOutline,
  FootballOutline,
  BasketballOutline,
  TennisballOutline,
  FitnessOutline,
  PeopleOutline,
  CalendarOutline,
  PersonOutline,
  GlobeOutline,
  LocationOutline
} from '@vicons/ionicons5'
import { mockTeams } from '@/data/mockTournaments'
import type { Team } from '@/models/Tournament'

const teams = ref<Team[]>(mockTeams)

const myTeams = ref<Team[]>(teams.value.filter(team => 
  team.members.some(member => member.userId === 'user-1' || member.userId === 'user-3')
))

const availableTeams = ref<Team[]>(teams.value.filter(team => 
  !team.members.some(member => member.userId === 'user-1' || member.userId === 'user-3')
))

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('en-US', { 
    year: 'numeric', 
    month: 'long', 
    day: 'numeric' 
  })
}

const getUserRole = (team: Team) => {
  const user = team.members.find(member => member.userId === 'user-1');
  if (user) {
    return 'Captain';
  }
  const user2 = team.members.find(member => member.userId === 'user-3');
  if (user2) {
    return 'Member';
  }
  return 'Member'; // Default role if user not found
};
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

.team-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  border: 1px solid #e2e8f0;
  transition: all 0.2s ease;
  cursor: pointer;
}

.team-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

.team-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.team-badge {
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

.team-role {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
}

.team-role.captain {
  background: #fef3c7;
  color: #92400e;
}

.team-role.member {
  background: #dbeafe;
  color: #1e40af;
}

.team-privacy {
  display: flex;
  align-items: center;
}

.team-name {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 1rem;
  line-height: 1.4;
}

.team-meta {
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

.team-participants {
  margin-bottom: 1.5rem;
}

.participant-count {
  display: block;
  color: #3b82f6;
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
  background: #3b82f6;
  border-radius: 4px;
  transition: width 0.3s ease;
}

.team-actions {
  display: flex;
  justify-content: center;
}

.manage-team-btn,
.join-team-btn {
  width: 100%;
  height: 40px;
  border-radius: 8px;
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