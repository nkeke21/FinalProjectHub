<template>
  <div class="team-card">
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
      <div v-if="showRole" class="team-role" :class="userRole.toLowerCase()">
        {{ userRole }}
      </div>
    </div>
    
    <h3 class="team-name">{{ team.name }}</h3>
    <p class="team-description">{{ team.description }}</p>
    
    <div class="team-meta">
      <div class="meta-item">
        <n-icon size="16" color="#64748b">
          <PersonOutline />
        </n-icon>
        <span v-if="showRole">{{ team.captainName }}</span>
        <span v-else>Captain: {{ team.captainName }}</span>
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
                 <span>{{ team.minAge || 0 }}-{{ team.maxAge || 0 }} years</span>
      </div>
    </div>

    <div class="team-participants">
      <span class="participant-count">{{ team.members.length }}/{{ team.maxMembers }} members</span>
      <div class="progress-bar">
        <div class="progress-fill" :style="{ width: (team.members.length / team.maxMembers * 100) + '%' }"></div>
      </div>
    </div>

    <div class="team-actions">
      <n-button 
        v-if="showRole" 
        type="primary" 
        color="blue" 
        class="manage-team-btn" 
        @click="$emit('manage-team', team)"
      >
        Manage Team
      </n-button>
      <n-button 
        v-else 
        type="primary" 
        color="blue" 
        class="join-team-btn" 
        @click="$emit('join-team', team)"
      >
        Request to Join
      </n-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { NButton, NIcon } from 'naive-ui'
import { 
  FootballOutline,
  BasketballOutline,
  TennisballOutline,
  FitnessOutline,
  PeopleOutline,
  CalendarOutline,
  PersonOutline
} from '@vicons/ionicons5'
import type { Team } from '@/models/Tournament'

interface Props {
  team: Team
  showRole?: boolean
  currentUserId?: string
}

interface Emits {
  (e: 'manage-team', team: Team): void
  (e: 'join-team', team: Team): void
}

const props = withDefaults(defineProps<Props>(), {
  showRole: false,
  currentUserId: 'user-1'
})

defineEmits<Emits>()

const userRole = computed(() => {
  try {
    if (!props.currentUserId) return 'Member'
    if (!props.team?.members) {
      console.log('No members array found in team')
      return 'Member'
    }
    const user = props.team.members.find(member => member?.userId === props.currentUserId)
    if (user) {
      return user.role === 'CAPTAIN' ? 'Captain' : 'Member'
    }
    return 'Member'
  } catch (error) {
    console.error('Error in userRole computed:', error)
    return 'Member'
  }
})

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('en-US', { 
    year: 'numeric', 
    month: 'long', 
    day: 'numeric' 
  })
}
</script>

<style scoped>
.team-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  border: 1px solid #e2e8f0;
  transition: all 0.2s ease;
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 320px;
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



.team-name {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 0.5rem;
  line-height: 1.4;
}

.team-description {
  color: #64748b;
  font-size: 0.875rem;
  margin-bottom: 1rem;
  line-height: 1.5;
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
  margin-top: auto;
}

.manage-team-btn,
.join-team-btn {
  width: 100%;
  height: 40px;
  border-radius: 8px;
}
</style> 