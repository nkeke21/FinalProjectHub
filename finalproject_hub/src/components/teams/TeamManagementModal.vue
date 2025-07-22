<template>
  <n-modal :show="show" preset="card" title="Manage Team" style="width: 800px" @update:show="updateShow">
    <div v-if="team" class="team-management">
      <div class="team-info-section">
        <h3>{{ team.name }}</h3>
        <p>{{ team.description }}</p>
        
        <div class="team-stats">
          <div class="stat-item">
            <span class="stat-label">Members:</span>
            <span class="stat-value">{{ team.members.length }}/{{ team.maxMembers }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">Sport:</span>
            <span class="stat-value">{{ team.sportType }}</span>
          </div>
        </div>
      </div>

      <div class="members-section">
        <div class="members-header">
          <h4>Team Members</h4>
        </div>
        
        <div class="members-list">
          <div v-for="member in team.members" :key="member.userId" class="member-item">
            <div class="member-info">
              <div class="member-name">{{ member.name }}</div>
              <div class="member-email">{{ member.email }}</div>
              <div class="member-role" :class="member.role.toLowerCase()">
                {{ member.role }}
              </div>
            </div>
            <div class="member-actions">
              <n-button
                v-if="isCaptain && member.role !== 'CAPTAIN' && team.members.length > 1"
                size="small"
                type="error"
                @click="confirmRemoveMember(member)"
              >
                Remove
              </n-button>
              <span v-else-if="isCaptain && team.members.length <= 1" class="disabled-text">
                Cannot remove last member
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <template #footer>
      <div class="modal-footer">
        <n-button @click="close">Close</n-button>
      </div>
    </template>



    <n-modal v-model:show="showRemoveConfirmation" preset="card" title="Remove Team Member" style="width: 400px">
      <div class="confirmation-content">
        <p>Are you sure you want to remove <strong>{{ memberToRemove?.name }}</strong> from the team?</p>
        <p class="warning-text">This action cannot be undone.</p>
      </div>
      
      <template #footer>
        <div class="modal-footer">
          <n-button @click="showRemoveConfirmation = false">Cancel</n-button>
          <n-button 
            type="error" 
            @click="removeMember(memberToRemove)"
            :loading="false"
          >
            Remove Member
          </n-button>
        </div>
      </template>
    </n-modal>
  </n-modal>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { NModal, NButton, useMessage } from 'naive-ui'
import type { Team } from '@/models/Tournament'
import { UserTeamService } from '@/services/apis/UserTeamService'

interface Props {
  show: boolean
  team: Team | null
  isCaptain: boolean
}

interface Emits {
  (e: 'update:show', value: boolean): void
  (e: 'team-updated', team: Team): void
  (e: 'member-removed', memberId: string): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const memberToRemove = ref<any>(null)
const showRemoveConfirmation = ref(false)

const updateShow = (value: boolean) => {
  emit('update:show', value)
}

const close = () => {
  emit('update:show', false)
}


const message = useMessage()

const confirmRemoveMember = (member: any) => {
  memberToRemove.value = member
  showRemoveConfirmation.value = true
}

const removeMember = async (member: any) => {
  if (!props.team) return
  
  try {
    await UserTeamService.removeTeamMember(props.team.id, member.userId)
    
    props.team.members = props.team.members.filter(m => m.userId !== member.userId)
    props.team.updatedAt = new Date().toISOString()
    
    message.success(`${member.name} has been removed from the team`)
    emit('member-removed', member.userId)
    
    showRemoveConfirmation.value = false
    memberToRemove.value = null
  } catch (error) {
    console.error('Failed to remove team member:', error)
    message.error('Failed to remove team member. Please try again.')
  }
}


</script>

<style scoped>
.team-management {
  max-height: 600px;
  overflow-y: auto;
}

.team-info-section {
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #e2e8f0;
}

.team-info-section h3 {
  margin-bottom: 0.5rem;
  color: #1e293b;
}

.team-info-section p {
  color: #64748b;
  margin-bottom: 1rem;
}

.team-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 1rem;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem;
  background: #f8fafc;
  border-radius: 8px;
}

.stat-label {
  font-weight: 600;
  color: #64748b;
  font-size: 0.875rem;
}

.stat-value {
  color: #1e293b;
  font-weight: 600;
}

.members-section {
  margin-top: 1rem;
}

.members-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.members-header h4 {
  margin: 0;
  color: #1e293b;
}

.members-list {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.member-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

.member-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.member-name {
  font-weight: 600;
  color: #1e293b;
}

.member-email {
  color: #64748b;
  font-size: 0.875rem;
}

.member-role {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
}

.member-role.captain {
  background: #fef3c7;
  color: #92400e;
}

.member-role.member {
  background: #dbeafe;
  color: #1e40af;
}


.confirmation-content {
  text-align: center;
  margin-bottom: 1rem;
}

.confirmation-content p {
  margin-bottom: 0.5rem;
  color: #1e293b;
}

.warning-text {
  color: #dc2626;
  font-size: 0.875rem;
  font-weight: 500;
}

.disabled-text {
  color: #94a3b8;
  font-size: 0.75rem;
  font-style: italic;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}




@media (max-width: 768px) {
  .team-stats {
    grid-template-columns: 1fr;
  }

  .member-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
}
</style> 