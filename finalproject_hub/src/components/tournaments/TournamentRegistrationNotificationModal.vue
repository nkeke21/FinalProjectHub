<template>
  <n-modal :show="show" @update:show="updateShow" preset="card" title="Registration Requests" style="width: 800px; max-height: 80vh; overflow-y: auto;">
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
  PersonOutline, CheckmarkOutline, CloseOutline, CheckmarkCircleOutline
} from '@vicons/ionicons5'
import { TournamentRegistrationNotificationService, type TournamentRegistrationNotification } from '@/services/apis/TournamentRegistrationNotificationService'
import { TournamentRegistrationService } from '@/services/apis/TournamentRegistrationService'

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
</style> 