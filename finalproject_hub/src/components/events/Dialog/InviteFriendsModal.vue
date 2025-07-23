<template>
  <n-modal v-model:show="show" preset="card" title="Invite Friends" class="invite-friends-modal" style="max-width: 50%;">
    <div class="modal-content">
      <div class="search-section">
        <h3>Search Friends</h3>
        <UserSearchAutocomplete @user-selected="addUserToSelection" />
        <div v-if="props.eventAgeRange" class="age-requirement">
          <n-icon size="16" color="#64748b">
            <InformationCircleOutline />
          </n-icon>
          <span>Age requirement: {{ props.eventAgeRange.min }}-{{ props.eventAgeRange.max }} years</span>
        </div>
        <div class="validation-rules">
          <h4>Invitation Rules:</h4>
          <ul>
            <li>Users must be within the age range ({{ props.eventAgeRange?.min || 'any' }}-{{ props.eventAgeRange?.max || 'any' }} years)</li>
            <li>Users cannot already be participants in this event</li>
            <li>Users cannot be invited multiple times</li>
          </ul>
        </div>
        
        <div v-if="props.currentParticipants && props.currentParticipants.length > 0" class="current-participants-info">
          <h4>Current Participants ({{ props.currentParticipants.length }}):</h4>
          <div class="participants-list">
            <div 
              v-for="participant in props.currentParticipants.slice(0, 3)" 
              :key="participant.userId"
              class="participant-item"
            >
              {{ participant.name }} ({{ participant.age }} years)
            </div>
            <div v-if="props.currentParticipants.length > 3" class="more-participants">
              +{{ props.currentParticipants.length - 3 }} more
            </div>
          </div>
        </div>
      </div>

      <div v-if="selectedUsers.length > 0" class="selected-users-section">
        <h3>Selected Friends ({{ selectedUsers.length }})</h3>
        <div class="selected-users-list">
          <div 
            v-for="user in selectedUsers" 
            :key="user.id" 
            class="selected-user-item"
          >
            <div class="user-info">
              <div class="user-avatar">
                {{ user.name.charAt(0).toUpperCase() }}
              </div>
              <div class="user-details">
                <div class="user-name">{{ user.name }}</div>
                <div class="user-email">{{ user.email }}</div>
                <div v-if="user.age" class="user-age">{{ user.age }} years old</div>
              </div>
            </div>
            <n-button 
              type="error" 
              size="small" 
              @click="removeUser(user.id)"
              class="remove-btn"
            >
              <template #icon>
                <n-icon><CloseOutline /></n-icon>
              </template>
            </n-button>
          </div>
        </div>
      </div>


      <div class="modal-footer">
        <n-button @click="closeModal" :disabled="isSending">
          Cancel
        </n-button>
        <n-button 
          type="primary" 
          @click="sendInvitations" 
          :loading="isSending"
          :disabled="selectedUsers.length === 0"
        >
          <template #icon>
            <n-icon><PaperPlaneOutline /></n-icon>
          </template>
          Send Invitations ({{ selectedUsers.length }})
        </n-button>
      </div>
    </div>
  </n-modal>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { NModal, NButton, NInput, NIcon, useMessage } from 'naive-ui'
import { CloseOutline, PaperPlaneOutline, InformationCircleOutline } from '@vicons/ionicons5'
import UserSearchAutocomplete from '@/components/search/UserSearchAutocomplete.vue'
import { EventInvitationService, type EventInvitationDTO } from '@/services/apis/EventInvitationService'
import { UserService } from '@/services/apis/UserService'

interface User {
  id: string
  name: string
  email: string
  age?: number
}

interface Props {
  show: boolean
  eventId: string
  eventAgeRange?: { min: number; max: number }
  currentParticipants?: Array<{ userId: string; name: string; email: string; age: number }>
}

interface Emits {
  (e: 'update:show', value: boolean): void
  (e: 'invitations-sent'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const message = useMessage()
const selectedUsers = ref<User[]>([])
const isSending = ref(false)

const show = computed({
  get: () => props.show,
  set: (value) => emit('update:show', value)
})

// Utility function to calculate age from birth date
const calculateAge = (birthDate: string): number => {
  const birth = new Date(birthDate)
  const today = new Date()
  let age = today.getFullYear() - birth.getFullYear()
  const monthDiff = today.getMonth() - birth.getMonth()
  
  if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
    age--
  }
  
  return age
}

// Check if user meets age requirements
const isUserAgeValid = (user: User): boolean => {
  if (!props.eventAgeRange || !user.age) return true
  return user.age >= props.eventAgeRange.min && user.age <= props.eventAgeRange.max
}

// Check if user is already a participant
const isUserAlreadyParticipant = (userId: string): boolean => {
  if (!props.currentParticipants) return false
  return props.currentParticipants.some(participant => participant.userId === userId)
}

const addUserToSelection = async (userId: string) => {
  // Check if user is already selected
  if (selectedUsers.value.some(user => user.id === userId)) {
    message.warning('This friend is already selected')
    return
  }

  try {
    const userDetails = await UserService.getUserById(userId)
    const age = userDetails.birthDate ? calculateAge(userDetails.birthDate) : undefined
    
    const user: User = {
      id: userDetails.id,
      name: userDetails.name,
      email: userDetails.email,
      age
    }

    // Check age requirements
    if (!isUserAgeValid(user)) {
      message.error(`${user.name} does not meet the age requirements (${props.eventAgeRange?.min}-${props.eventAgeRange?.max} years)`)
      return
    }

    // Check if user is already a participant
    if (isUserAlreadyParticipant(userId)) {
      message.error(`${user.name} is already a participant in this event`)
      return
    }

    selectedUsers.value.push(user)
    message.success(`${user.name} added to invitation list`)
  } catch (error) {
    message.error('Failed to get user details')
  }
}

const removeUser = (userId: string) => {
  selectedUsers.value = selectedUsers.value.filter(user => user.id !== userId)
}

const sendInvitations = async () => {
  if (selectedUsers.value.length === 0) {
    message.warning('Please select at least one friend to invite')
    return
  }

  isSending.value = true
  let successCount = 0
  let errorCount = 0

  try {
    for (const user of selectedUsers.value) {
      try {
        const invitation: EventInvitationDTO = {
          eventId: props.eventId,
          toUserId: user.id
        }

        await EventInvitationService.sendInvitation(invitation)
        successCount++
      } catch (error: any) {
        console.error(`Failed to send invitation to ${user.name}:`, error)
        errorCount++
      }
    }

    if (successCount > 0) {
      message.success(`Successfully sent ${successCount} invitation${successCount > 1 ? 's' : ''}`)
      if (errorCount > 0) {
        message.warning(`${errorCount} invitation${errorCount > 1 ? 's' : ''} failed to send`)
      }
      closeModal()
      emit('invitations-sent')
    } else {
      message.error('Failed to send any invitations')
    }
  } catch (error) {
    message.error('An unexpected error occurred')
  } finally {
    isSending.value = false
  }
}

const closeModal = () => {
  selectedUsers.value = []
  show.value = false
}

// Reset form when modal is closed
watch(show, (newValue) => {
  if (!newValue) {
    selectedUsers.value = []
  }
})
</script>

<style scoped>
.invite-friends-modal {
  max-width: 400px;
}

.modal-content {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.search-section h3,
.selected-users-section h3 {
  font-size: 0.875rem;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 0.5rem;
}

.age-requirement {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-top: 0.5rem;
  padding: 0.5rem;
  background: #f8fafc;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  color: #64748b;
}

.validation-rules {
  margin-top: 1rem;
  padding: 0.75rem;
  background: #f8fafc;
  border-radius: 0.5rem;
  border: 1px solid #e2e8f0;
}

.validation-rules h4 {
  font-size: 0.875rem;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 0.5rem;
}

.validation-rules ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.validation-rules li {
  font-size: 0.875rem;
  color: #64748b;
  margin-bottom: 0.25rem;
}

.current-participants-info {
  margin-top: 1rem;
  padding: 0.75rem;
  background: #f8fafc;
  border-radius: 0.5rem;
  border: 1px solid #e2e8f0;
}

.current-participants-info h4 {
  font-size: 0.875rem;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 0.5rem;
}

.participants-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  font-size: 0.875rem;
  color: #64748b;
}

.participant-item {
  background: #e0e7ff;
  padding: 0.25rem 0.75rem;
  border-radius: 1rem;
  border: 1px solid #c7d2fe;
}

.more-participants {
  font-size: 0.875rem;
  color: #64748b;
  margin-left: 0.5rem;
}

.selected-users-list {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  max-height: 150px;
  overflow-y: auto;
}

.selected-user-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.5rem;
  background: #f8fafc;
  border-radius: 0.5rem;
  border: 1px solid #e2e8f0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  flex: 1;
}

.user-avatar {
  width: 2rem;
  height: 2rem;
  border-radius: 50%;
  background: #22c55e;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 0.875rem;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.user-name {
  font-weight: 600;
  color: #1e293b;
}

.user-email {
  font-size: 0.875rem;
  color: #64748b;
}

.user-age {
  font-size: 0.75rem;
  color: #64748b;
}

.remove-btn {
  flex-shrink: 0;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  margin-top: 0.75rem;
  padding-top: 0.75rem;
  border-top: 1px solid #e2e8f0;
}
</style> 