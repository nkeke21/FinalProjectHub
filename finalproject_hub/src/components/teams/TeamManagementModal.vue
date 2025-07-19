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

      <n-tabs type="line" animated>
        <n-tab-pane name="members" tab="Members">
          <div class="members-section">
            <div class="members-header">
              <h4>Team Members</h4>
              <n-button 
                size="small" 
                @click="showInviteModal = true"
                :disabled="isTeamFull"
                :title="isTeamFull ? 'Team is full' : 'Invite a new member'"
              >
                <template #icon>
                  <n-icon><PersonAddOutline /></n-icon>
                </template>
                Invite Member
              </n-button>
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
                    v-if="isCaptain && member.role !== 'CAPTAIN'"
                    size="small"
                    @click="removeMember(member)"
                  >
                    Remove
                  </n-button>
                </div>
              </div>
            </div>
          </div>
        </n-tab-pane>
        
        <n-tab-pane v-if="isCaptain" name="requests" tab="Join Requests">
          <div class="requests-section">
            <div class="requests-header">
              <h4>Pending Join Requests</h4>
              <n-button 
                size="small" 
                @click="showJoinRequestsModal = true"
              >
                <template #icon>
                  <n-icon><NotificationsOutline /></n-icon>
                </template>
                View Requests
              </n-button>
            </div>
            <p class="requests-info">Manage requests from users who want to join your team.</p>
          </div>
        </n-tab-pane>
        
        <n-tab-pane name="settings" tab="Settings">
          <div class="settings-section">
            <h4>Team Settings</h4>
            <n-form :model="settingsForm" label-placement="left" label-width="auto">
              <n-form-item label="Team Name">
                <n-input v-model:value="settingsForm.name" />
              </n-form-item>
              <n-form-item label="Description">
                <n-input v-model:value="settingsForm.description" type="textarea" :rows="3" />
              </n-form-item>
              <n-form-item label="Max Members">
                <n-input-number v-model:value="settingsForm.maxMembers" :min="2" :max="50" />
              </n-form-item>
              <n-form-item label="Age Range">
                <div class="age-range-inputs">
                  <n-input-number v-model:value="settingsForm.minAge" :min="8" :max="80" />
                  <span class="age-separator">to</span>
                  <n-input-number v-model:value="settingsForm.maxAge" :min="8" :max="80" />
                  <span class="age-unit">years</span>
                </div>
              </n-form-item>
            </n-form>
          </div>
        </n-tab-pane>
      </n-tabs>
    </div>
    
    <template #footer>
      <div class="modal-footer">
        <n-button @click="close">Close</n-button>
        <n-button v-if="isCaptain" type="primary" @click="saveSettings">Save Changes</n-button>
      </div>
    </template>

    <!-- Invite Member Modal -->
    <n-modal v-model:show="showInviteModal" preset="card" title="Invite Member" style="width: 500px">
      <n-form :model="inviteForm" label-placement="left" label-width="auto">
        <n-form-item label="Email Address">
          <n-input v-model:value="inviteForm.email" placeholder="Enter email address" />
        </n-form-item>
        <n-form-item label="Message (Optional)">
          <n-input
            v-model:value="inviteForm.message"
            type="textarea"
            placeholder="Add a personal message"
            :rows="3"
          />
        </n-form-item>
      </n-form>
      
      <template #footer>
        <div class="modal-footer">
          <n-button @click="showInviteModal = false">Cancel</n-button>
          <n-button type="primary" @click="sendInvite" :loading="sendingInvite">Send Invite</n-button>
        </div>
      </template>
    </n-modal>

    <!-- Team Join Requests Modal -->
    <TeamJoinRequestsModal
      v-model:show="showJoinRequestsModal"
      :team-id="team?.id || ''"
      @request-processed="handleRequestProcessed"
    />
  </n-modal>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch } from 'vue'
import { NModal, NForm, NFormItem, NInput, NInputNumber, NButton, NTabs, NTabPane, NIcon } from 'naive-ui'
import { PersonAddOutline, NotificationsOutline } from '@vicons/ionicons5'
import type { Team } from '@/models/Tournament'
import TeamJoinRequestsModal from './TeamJoinRequestsModal.vue'

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

const showInviteModal = ref(false)
const showJoinRequestsModal = ref(false)
const sendingInvite = ref(false)

const isTeamFull = computed(() => {
  if (!props.team) return false
  return props.team.members.length >= props.team.maxMembers
})

const settingsForm = reactive({
  name: '',
  description: '',
  maxMembers: 11,
  minAge: 18,
  maxAge: 35
})

const inviteForm = reactive({
  email: '',
  message: ''
})

const updateShow = (value: boolean) => {
  emit('update:show', value)
}

const close = () => {
  emit('update:show', false)
}

const saveSettings = () => {
  if (!props.team) return
  
  Object.assign(props.team, {
    name: settingsForm.name,
    description: settingsForm.description,
    maxMembers: settingsForm.maxMembers,
    minAge: settingsForm.minAge,
    maxAge: settingsForm.maxAge,
    updatedAt: new Date().toISOString()
  })
  
  emit('team-updated', props.team)
  close()
}

const removeMember = (member: any) => {
  if (!props.team) return
  
  props.team.members = props.team.members.filter(m => m.userId !== member.userId)
  props.team.updatedAt = new Date().toISOString()
  emit('member-removed', member.userId)
}

const sendInvite = async () => {
  sendingInvite.value = true
  
  await new Promise(resolve => setTimeout(resolve, 1000))
  
  console.log('Sending invite to:', inviteForm.email)
  console.log('Message:', inviteForm.message)
  
  inviteForm.email = ''
  inviteForm.message = ''
  
  showInviteModal.value = false
  sendingInvite.value = false
}

const handleRequestProcessed = () => {
  // Refresh team data when a request is processed
  emit('team-updated', props.team!)
}

watch(() => props.team, (newTeam) => {
  if (newTeam) {
    Object.assign(settingsForm, {
      name: newTeam.name,
      description: newTeam.description,
      maxMembers: newTeam.maxMembers,
      minAge: newTeam.minAge,
      maxAge: newTeam.maxAge
    })
  }
}, { immediate: true })
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

.settings-section {
  margin-top: 1rem;
}

.settings-section h4 {
  margin-bottom: 1rem;
  color: #1e293b;
}

.requests-section {
  margin-top: 1rem;
}

.requests-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.requests-header h4 {
  margin: 0;
  color: #1e293b;
}

.requests-info {
  color: #64748b;
  font-size: 0.875rem;
  margin: 0;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}

.age-range-inputs {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.age-separator {
  color: #64748b;
  font-size: 0.875rem;
}

.age-unit {
  color: #64748b;
  font-size: 0.875rem;
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