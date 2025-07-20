<template>
  <n-modal :show="show" @update:show="updateShow" preset="card" title="Registration Requests" style="width: 900px; max-height: 80vh; overflow-y: auto;">
    <div class="notification-content">
      <div v-if="loading" class="loading-state">
        <n-spin size="large" />
        <p>Loading registration requests...</p>
      </div>
      
      <div v-else-if="notifications.length === 0" class="empty-state">
        <n-icon size="48" color="#cbd5e1">
          <CheckmarkCircleOutline />
        </n-icon>
        <h3>No pending requests</h3>
        <p>All registration requests have been processed</p>
      </div>
      
      <div v-else class="notifications-list">
        <div 
          v-for="notification in notifications" 
          :key="notification.id"
          class="notification-item"
          :class="{ 'unread': !notification.isRead }"
        >
          <div class="notification-header">
            <div class="notification-info">
              <h4>{{ notification.tournamentName }}</h4>
              <p class="requester-info">
                <n-icon size="16" color="#64748b">
                  <PersonOutline />
                </n-icon>
                {{ notification.requesterName }} wants to register
              </p>
              <p class="timestamp">{{ formatTimestamp(notification.createdAt) }}</p>
            </div>
            
            <div class="notification-actions">
              <n-button 
                type="info" 
                size="small"
                @click="toggleRegistrationDetails(notification.id)"
                :loading="loadingDetails === notification.id"
                color="#3b82f6"
              >
                <template #icon>
                  <n-icon size="16">
                    <EyeOutline />
                  </n-icon>
                </template>
                {{ expandedNotifications.has(notification.id) ? 'Hide' : 'View' }} Details
              </n-button>
              
              <n-button 
                type="success" 
                size="small"
                @click="approveRegistration(notification)"
                :loading="processingRegistration === notification.registrationId"
                color="#10b981"
              >
                <template #icon>
                  <n-icon size="16">
                    <CheckmarkOutline />
                  </n-icon>
                </template>
                Approve
              </n-button>
              
              <n-button 
                type="error" 
                size="small"
                @click="rejectRegistration(notification)"
                :loading="processingRegistration === notification.registrationId"
                color="#ef4444"
              >
                <template #icon>
                  <n-icon size="16">
                    <CloseOutline />
                  </n-icon>
                </template>
                Reject
              </n-button>
            </div>
          </div>
          
          <!-- Registration Details Section -->
          <div v-if="expandedNotifications.has(notification.id)" class="registration-details">
            <div v-if="registrationDetails[notification.id]" class="details-content">
              <!-- Team Information (if team registration) -->
              <div v-if="registrationDetails[notification.id].teamId" class="team-info-section">
                <h5>Team Information</h5>
                <div class="team-details">
                  <div class="detail-item">
                    <span class="detail-label">Team Name:</span>
                    <span class="detail-value">{{ teamDetails[notification.id]?.name || 'Loading...' }}</span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">Team Captain:</span>
                    <span class="detail-value">{{ teamDetails[notification.id]?.captainName || 'Loading...' }}</span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">Team Members:</span>
                    <span class="detail-value">{{ teamDetails[notification.id]?.members?.length || 0 }}/{{ teamDetails[notification.id]?.maxMembers || 0 }}</span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">Team Description:</span>
                    <span class="detail-value">{{ teamDetails[notification.id]?.description || 'No description' }}</span>
                  </div>
                </div>
                
                <div v-if="teamDetails[notification.id]?.members" class="team-members">
                  <h6>Team Members</h6>
                  <div class="members-list">
                    <div 
                      v-for="member in teamDetails[notification.id].members" 
                      :key="member.userId"
                      class="member-item"
                    >
                      <div class="member-info">
                        <span class="member-name">{{ member.name }}</span>
                        <span v-if="member.age" class="member-age">{{ member.age }} years</span>
                      </div>
                      <span class="member-role">{{ member.role }}</span>
                    </div>
                  </div>
                </div>
              </div>

              <h5>Participant Information</h5>
              <div class="details-grid">
                <div class="detail-item">
                  <span class="detail-label">Full Name:</span>
                  <span class="detail-value">{{ registrationDetails[notification.id].fullName || 'Not specified' }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">Age:</span>
                  <span class="detail-value">{{ registrationDetails[notification.id].age ? `${registrationDetails[notification.id].age} years` : 'Not specified' }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">Email:</span>
                  <span class="detail-value">{{ registrationDetails[notification.id].email || 'Not specified' }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">Phone:</span>
                  <span class="detail-value">{{ registrationDetails[notification.id].phoneNumber || 'Not specified' }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">Address:</span>
                  <span class="detail-value">{{ registrationDetails[notification.id].address || 'Not specified' }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">Skill Level:</span>
                  <span class="detail-value">{{ registrationDetails[notification.id].skillLevel || 'Not specified' }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">Previous Experience:</span>
                  <span class="detail-value">{{ registrationDetails[notification.id].previousExperience || 'Not specified' }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">Previous Achievements:</span>
                  <span class="detail-value">{{ registrationDetails[notification.id].previousAchievements || 'Not specified' }}</span>
                </div>
              </div>
              
              <h5>Emergency Contact</h5>
              <div class="details-grid">
                <div class="detail-item">
                  <span class="detail-label">Name:</span>
                  <span class="detail-value">{{ registrationDetails[notification.id].emergencyContactName || 'Not specified' }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">Relationship:</span>
                  <span class="detail-value">{{ registrationDetails[notification.id].emergencyContactRelationship || 'Not specified' }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">Phone:</span>
                  <span class="detail-value">{{ registrationDetails[notification.id].emergencyContactPhone || 'Not specified' }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">Email:</span>
                  <span class="detail-value">{{ registrationDetails[notification.id].emergencyContactEmail || 'Not specified' }}</span>
                </div>
              </div>
            </div>
            
            <div v-else-if="loadingDetails === notification.id" class="loading-details">
              <n-spin size="small" />
              <span>Loading registration details...</span>
            </div>
            
            <div v-else class="error-details">
              <n-icon size="16" color="#ef4444">
                <AlertCircleOutline />
              </n-icon>
              <span>Failed to load registration details</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </n-modal>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { 
  NModal, NButton, NIcon, NSpin, useMessage 
} from 'naive-ui'
import { 
  PersonOutline, CheckmarkOutline, CloseOutline, CheckmarkCircleOutline, EyeOutline, AlertCircleOutline
} from '@vicons/ionicons5'
import { TournamentRegistrationNotificationService, type TournamentRegistrationNotification } from '@/services/apis/TournamentRegistrationNotificationService'
import { TournamentRegistrationService } from '@/services/apis/TournamentRegistrationService'
import { UserTeamService } from '@/services/apis/UserTeamService'

interface Props {
  show: boolean
}

interface Emits {
  (e: 'update:show', value: boolean): void
  (e: 'notifications-updated'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const message = useMessage()
const loading = ref(false)
const notifications = ref<TournamentRegistrationNotification[]>([])
const processingRegistration = ref<string | null>(null)
const expandedNotifications = ref<Set<string>>(new Set())
const registrationDetails = ref<Record<string, any>>({})
const teamDetails = ref<Record<string, any>>({})
const loadingDetails = ref<string | null>(null)

const updateShow = (value: boolean) => {
  emit('update:show', value)
}

const loadNotifications = async () => {
  try {
    loading.value = true
    notifications.value = await TournamentRegistrationNotificationService.getHostNotifications()
  } catch (error) {
    console.error('Error loading notifications:', error)
    message.error('Failed to load registration requests')
  } finally {
    loading.value = false
  }
}

const toggleRegistrationDetails = async (notificationId: string) => {
  if (expandedNotifications.value.has(notificationId)) {
    // Hide details
    expandedNotifications.value.delete(notificationId)
    delete registrationDetails.value[notificationId]
  } else {
    // Show details
    expandedNotifications.value.add(notificationId)
    await loadRegistrationDetails(notificationId)
  }
}

const loadRegistrationDetails = async (notificationId: string) => {
  const notification = notifications.value.find(n => n.id === notificationId)
  if (!notification) return
  
  try {
    loadingDetails.value = notificationId
    console.log('Loading details for registration ID:', notification.registrationId)
    const details = await TournamentRegistrationService.getRegistrationById(notification.registrationId)
    console.log('Registration details received:', details)
    
    if (details) {
      registrationDetails.value[notificationId] = details
      console.log('Stored registration details:', registrationDetails.value[notificationId])
      
      // If this is a team registration, load team details
      if (details.teamId) {
        console.log('Loading team details for team ID:', details.teamId)
        await loadTeamDetails(notificationId, details.teamId)
      }
    }
  } catch (error) {
    console.error('Error loading registration details:', error)
    message.error('Failed to load registration details')
  } finally {
    loadingDetails.value = null
  }
}

const loadTeamDetails = async (notificationId: string, teamId: string) => {
  try {
    const team = await UserTeamService.getTeamById(teamId)
    if (team) {
      teamDetails.value[notificationId] = team
    }
  } catch (error) {
    console.error('Error loading team details:', error)
  }
}

const approveRegistration = async (notification: TournamentRegistrationNotification) => {
  try {
    processingRegistration.value = notification.registrationId
    
    const response = await TournamentRegistrationService.approveRegistration(notification.registrationId)
    
    if (response.success) {
      message.success('Registration approved successfully!')
      // Remove the notification from the list
      notifications.value = notifications.value.filter(n => n.id !== notification.id)
      emit('notifications-updated')
    } else {
      message.error(response.message || 'Failed to approve registration')
    }
  } catch (error) {
    console.error('Error approving registration:', error)
    message.error('Failed to approve registration')
  } finally {
    processingRegistration.value = null
  }
}

const rejectRegistration = async (notification: TournamentRegistrationNotification) => {
  try {
    processingRegistration.value = notification.registrationId
    
    const response = await TournamentRegistrationService.rejectRegistration(notification.registrationId)
    
    if (response.success) {
      message.success('Registration rejected successfully!')
      // Remove the notification from the list
      notifications.value = notifications.value.filter(n => n.id !== notification.id)
      emit('notifications-updated')
    } else {
      message.error(response.message || 'Failed to reject registration')
    }
  } catch (error) {
    console.error('Error rejecting registration:', error)
    message.error('Failed to reject registration')
  } finally {
    processingRegistration.value = null
  }
}

const formatTimestamp = (timestamp: string) => {
  const date = new Date(timestamp)
  const now = new Date()
  const diffInMinutes = Math.floor((now.getTime() - date.getTime()) / (1000 * 60))
  
  if (diffInMinutes < 1) {
    return 'Just now'
  } else if (diffInMinutes < 60) {
    return `${diffInMinutes} minute${diffInMinutes > 1 ? 's' : ''} ago`
  } else if (diffInMinutes < 1440) {
    const hours = Math.floor(diffInMinutes / 60)
    return `${hours} hour${hours > 1 ? 's' : ''} ago`
  } else {
    const days = Math.floor(diffInMinutes / 1440)
    return `${days} day${days > 1 ? 's' : ''} ago`
  }
}

watch(() => props.show, (newValue) => {
  if (newValue) {
    loadNotifications()
  }
})
</script>

<style scoped>
.notification-content {
  padding: 1rem;
}

.loading-state,
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem 1rem;
  text-align: center;
}

.loading-state p,
.empty-state p {
  margin-top: 1rem;
  color: #64748b;
}

.empty-state h3 {
  margin: 1rem 0 0.5rem 0;
  color: #475569;
}

.notifications-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.notification-item {
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 1rem;
  background: #f8fafc;
  transition: all 0.2s ease;
}

.notification-item.unread {
  border-left: 4px solid #3b82f6;
  background: #eff6ff;
}

.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 1rem;
}

.notification-info h4 {
  margin: 0 0 0.5rem 0;
  color: #1e293b;
  font-size: 1.1rem;
  font-weight: 600;
}

.requester-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin: 0.25rem 0;
  color: #64748b;
  font-size: 0.875rem;
}

.timestamp {
  margin: 0.25rem 0 0 0;
  color: #94a3b8;
  font-size: 0.75rem;
}

.notification-actions {
  display: flex;
  gap: 0.5rem;
  flex-shrink: 0;
}

.registration-details {
  margin-top: 1rem;
  padding: 1rem;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

.details-content h5 {
  margin: 0 0 0.75rem 0;
  color: #1e293b;
  font-size: 1rem;
  font-weight: 600;
}

.details-content h5:not(:first-child) {
  margin-top: 1.5rem;
}

.details-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 0.75rem;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.detail-label {
  font-size: 0.75rem;
  color: #64748b;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.detail-value {
  font-size: 0.875rem;
  color: #1e293b;
  font-weight: 500;
}

.loading-details,
.error-details {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 1rem;
  color: #64748b;
  font-size: 0.875rem;
}

.error-details {
  color: #ef4444;
}

.team-info-section {
  margin-bottom: 1.5rem;
  padding: 1rem;
  background: #f0f9ff;
  border-radius: 8px;
  border: 1px solid #bae6fd;
}

.team-details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.team-members h6 {
  margin: 0 0 0.5rem 0;
  color: #1e293b;
  font-size: 0.875rem;
  font-weight: 600;
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
  padding: 0.5rem;
  background: #ffffff;
  border-radius: 4px;
  border: 1px solid #e2e8f0;
}

.member-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.member-name {
  font-weight: 500;
  color: #1e293b;
}

.member-age {
  font-size: 0.75rem;
  color: #64748b;
}

.member-role {
  font-size: 0.75rem;
  color: #64748b;
  text-transform: uppercase;
  font-weight: 600;
  padding: 0.25rem 0.5rem;
  background: #f1f5f9;
  border-radius: 4px;
}
</style> 