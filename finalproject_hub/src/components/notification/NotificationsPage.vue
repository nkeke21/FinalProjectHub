<template>
  <div class="notifications-page">
    <!-- Header Section -->
    <div class="notifications-header">
      <div class="header-content">
        <div class="header-title">
          <n-icon size="32" color="#64748b">
            <NotificationsOutline />
          </n-icon>
          <h1>Notifications</h1>
        </div>
        <p class="header-subtitle">Stay updated with your sports community</p>
      </div>
      
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon">
            <n-icon size="24" color="#64748b">
              <NotificationsOutline />
            </n-icon>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ notifications.length }}</div>
            <div class="stat-label">Total</div>
          </div>
        </div>
        
        <div class="stat-card">
          <div class="stat-icon">
            <n-icon size="24" color="#64748b">
              <PersonAddOutline />
            </n-icon>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ friendRequestCount }}</div>
            <div class="stat-label">Friend Requests</div>
          </div>
        </div>
        
        <div class="stat-card">
          <div class="stat-icon">
            <n-icon size="24" color="#64748b">
              <CalendarOutline />
            </n-icon>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ eventInvitationCount }}</div>
            <div class="stat-label">Event Invites</div>
          </div>
        </div>
        
      </div>
    </div>

    <div class="filter-section">
      <div class="filter-tabs">
        <button 
          class="filter-tab" 
          :class="{ active: activeFilter === 'all' }"
          @click="activeFilter = 'all'"
        >
          <n-icon size="16" color="currentColor">
            <AppsOutline />
          </n-icon>
          All ({{ notifications.length }})
        </button>
        <button 
          class="filter-tab" 
          :class="{ active: activeFilter === 'friend-requests' }"
          @click="activeFilter = 'friend-requests'"
        >
          <n-icon size="16" color="currentColor">
            <PersonAddOutline />
          </n-icon>
          Friend Requests ({{ friendRequestCount }})
        </button>
        <button 
          class="filter-tab" 
          :class="{ active: activeFilter === 'event-invitations' }"
          @click="activeFilter = 'event-invitations'"
        >
          <n-icon size="16" color="currentColor">
            <CalendarOutline />
          </n-icon>
          Event Invitations ({{ eventInvitationCount }})
        </button>
      </div>
      
      <n-button 
        type="default" 
        size="small"
        @click="refreshNotifications"
        class="refresh-button"
      >
        <template #icon>
          <n-icon size="16">
            <RefreshOutline />
          </n-icon>
        </template>
        Refresh
      </n-button>
    </div>

    <div class="notifications-list" v-if="filteredNotifications.length > 0">
      <div 
        v-for="notification in filteredNotifications" 
        :key="notification.id"
        class="notification-card"
        :class="{ unread: !notification.read }"
      >
        <div v-if="notification.type === 'friend-request'" class="notification-content" :class="getNotificationStatusClass(notification.status)">
          <div class="notification-avatar">
            <div class="avatar-circle" :class="getAvatarClass(notification.status)">
              <n-icon size="24" color="white">
                <PersonAddOutline />
              </n-icon>
            </div>
          </div>
          <div class="notification-details">
            <div class="notification-header">
              <h3 class="notification-title">
                <strong>{{ notification.senderName }}</strong> 
                <span v-if="notification.status === 'PENDING'">sent you a friend request</span>
                <span v-else-if="notification.status === 'ACCEPTED'">friend request accepted</span>
                <span v-else-if="notification.status === 'REJECTED'">friend request declined</span>
              </h3>
              <span class="notification-time">{{ formatTime(notification.timestamp) }}</span>
            </div>
            <p class="notification-message">
              {{ notification.message }}
            </p>
            <div v-if="notification.status === 'PENDING'" class="notification-actions">
              <n-button 
                type="primary" 
                color="#22c55e" 
                size="small"
                @click="acceptFriendRequest(notification.id)"
              >
                <template #icon>
                  <n-icon size="16">
                    <CheckmarkOutline />
                  </n-icon>
                </template>
                Accept
              </n-button>
              <n-button 
                type="error" 
                size="small"
                @click="declineFriendRequest(notification.id)"
              >
                <template #icon>
                  <n-icon size="16">
                    <CloseOutline />
                  </n-icon>
                </template>
                Decline
              </n-button>
            </div>
            <div v-else class="notification-status">
              <n-icon size="16" :color="getStatusIconColor(notification.status)">
                <CheckmarkOutline v-if="notification.status === 'ACCEPTED'" />
                <CloseOutline v-else-if="notification.status === 'REJECTED'" />
              </n-icon>
              <span :class="getStatusTextClass(notification.status)">
                {{ notification.status === 'ACCEPTED' ? 'Accepted' : 'Declined' }}
              </span>
            </div>
          </div>
        </div>

        <div v-else-if="notification.type === 'event-invitation'" class="notification-content">
          <div class="notification-avatar">
            <div class="avatar-circle event-invitation">
              <n-icon size="24" color="white">
                <CalendarOutline />
              </n-icon>
            </div>
          </div>
          <div class="notification-details">
            <div class="notification-header">
              <h3 class="notification-title">
                <strong>{{ notification.senderName }}</strong> invited you to join an event
              </h3>
              <span class="notification-time">{{ formatTime(notification.timestamp) }}</span>
            </div>
            <div class="event-details">
              <h4 class="event-title">{{ notification.eventTitle }}</h4>
              <div class="event-info">
                <div class="event-info-item">
                  <n-icon size="14" color="#64748b">
                    <CalendarOutline />
                  </n-icon>
                  <span>{{ formatDate(notification.eventDate) }}</span>
                </div>
                <div class="event-info-item">
                  <n-icon size="14" color="#64748b">
                    <LocationOutline />
                  </n-icon>
                  <span>{{ notification.eventLocation }}</span>
                </div>
                <div class="event-info-item">
                  <n-icon size="14" color="#64748b">
                    <FootballOutline />
                  </n-icon>
                  <span>{{ notification.eventSport }}</span>
                </div>
              </div>
            </div>
            <div class="notification-actions">
                             <n-button 
                 type="primary" 
                 color="#f59e0b" 
                 size="small"
                 @click="acceptEventInvitation(notification.id)"
               >
                <template #icon>
                  <n-icon size="16">
                    <CheckmarkOutline />
                  </n-icon>
                </template>
                Join Event
              </n-button>
              <n-button 
                type="error" 
                size="small"
                @click="declineEventInvitation(notification.id)"
              >
                <template #icon>
                  <n-icon size="16">
                    <CloseOutline />
                  </n-icon>
                </template>
                Decline
              </n-button>
              <n-button 
                type="default" 
                size="small"
                @click="viewEventDetails(notification.eventId)"
              >
                <template #icon>
                  <n-icon size="16">
                    <InformationCircleOutline />
                  </n-icon>
                </template>
                View Details
              </n-button>
            </div>
          </div>
        </div>

      </div>
    </div>

    <div v-else class="empty-state">
      <n-icon size="64" color="#cbd5e1">
        <NotificationsOutline />
      </n-icon>
      <h3>No notifications</h3>
      <p>{{ getEmptyStateMessage() }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useNotifications } from '@/services/notifications/useNotifications'
import { 
  NButton, 
  NIcon 
} from 'naive-ui'
import { 
  NotificationsOutline,
  PersonAddOutline,
  CalendarOutline,
  AppsOutline,
  CheckmarkOutline,
  CloseOutline,
  InformationCircleOutline,
  LocationOutline,
  FootballOutline,
  RefreshOutline
} from '@vicons/ionicons5'

const router = useRouter()

const {
  notifications,
  activeFilter,
  filteredNotifications,
  unreadCount,
  friendRequestCount,
  eventInvitationCount,
  acceptFriendRequest,
  declineFriendRequest,
  acceptEventInvitation,
  declineEventInvitation,
  refreshNotifications
} = useNotifications()

onMounted(() => {
  refreshNotifications()
})

const formatTime = (timestamp: Date) => {
  const now = new Date()
  const diff = now.getTime() - timestamp.getTime()
  const minutes = Math.floor(diff / (1000 * 60))
  const hours = Math.floor(diff / (1000 * 60 * 60))
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))

  if (minutes < 60) {
    return `${minutes}m ago`
  } else if (hours < 24) {
    return `${hours}h ago`
  } else {
    return `${days}d ago`
  }
}

const formatDate = (date: Date) => {
  return date.toLocaleDateString('en-US', {
    weekday: 'short',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const viewEventDetails = (eventId: string) => {
  router.push(`/events/${eventId}`)
}

const getEmptyStateMessage = () => {
  switch (activeFilter.value) {
    case 'friend-requests':
      return 'No friend requests at the moment. Check back later!'
    case 'event-invitations':
      return 'No event invitations yet. Start connecting with others to get invited!'
    default:
      return 'You\'re all caught up! No new notifications.'
  }
}

const getNotificationStatusClass = (status?: string) => {
  switch (status) {
    case 'ACCEPTED':
      return 'notification-accepted'
    case 'REJECTED':
      return 'notification-rejected'
    default:
      return ''
  }
}

const getAvatarClass = (status?: string) => {
  switch (status) {
    case 'ACCEPTED':
      return 'friend-request accepted'
    case 'REJECTED':
      return 'friend-request rejected'
    default:
      return 'friend-request'
  }
}

const getStatusIconColor = (status?: string) => {
  switch (status) {
    case 'ACCEPTED':
      return '#22c55e'
    case 'REJECTED':
      return '#ef4444'
    default:
      return '#64748b'
  }
}

const getStatusTextClass = (status?: string) => {
  switch (status) {
    case 'ACCEPTED':
      return 'status-accepted'
    case 'REJECTED':
      return 'status-rejected'
    default:
      return ''
  }
}

onMounted(() => {
  console.log('Notifications page mounted')
  console.log('Friend request count:', friendRequestCount.value)
  console.log('Filtered notifications:', filteredNotifications.value)
  console.log('All notifications:', notifications.value)
})
</script>

<style scoped>
.notifications-page {
  width: 90%;
  margin: 0 auto;
  padding: 2rem;
  min-height: 100vh;
}

.notifications-header {
  background: white;
  color: #1e293b;
  padding: 3rem 2rem;
  border-radius: 1rem;
  margin-bottom: 2rem;
  text-align: center;
  border: 1px solid #e2e8f0;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.header-content {
  margin-bottom: 2rem;
}

.header-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 0.5rem;
}

.header-title h1 {
  font-size: 2.5rem;
  font-weight: 700;
  margin: 0;
}

.header-subtitle {
  font-size: 1.125rem;
  opacity: 0.9;
  margin: 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
  max-width: 800px;
  margin: 0 auto;
}

.stat-card {
  background: white;
  padding: 1.5rem;
  border-radius: 1rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  border: 1px solid #e2e8f0;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  background: #f8fafc;
  padding: 0.75rem;
  border-radius: 0.75rem;
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 0.875rem;
  opacity: 0.9;
}

.filter-section {
  background: white;
  padding: 1.5rem;
  border-radius: 1rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  margin-bottom: 2rem;
  border: 1px solid #e2e8f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
}

.filter-tabs {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.filter-tab {
  background: #f1f5f9;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 0.75rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #64748b;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.filter-tab:hover {
  background: #e2e8f0;
  color: #475569;
}

.filter-tab.active {
  background: #22c55e;
  color: white;
  box-shadow: 0 4px 15px rgba(34, 197, 94, 0.3);
}

.refresh-button {
  background: #f1f5f9;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 0.75rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #64748b;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.refresh-button:hover {
  background: #e2e8f0;
  color: #475569;
}

.notifications-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.notification-card {
  background: white;
  border-radius: 1rem;
  padding: 1.5rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
  border-left: 4px solid transparent;
  margin-bottom: 1rem;
}

.notification-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 25px -3px rgba(0, 0, 0, 0.1);
}

.notification-card.unread {
  border-left-color: #22c55e;
  background: #f0fdf4;
}

.notification-content {
  display: flex;
  gap: 1rem;
}

.notification-avatar {
  flex-shrink: 0;
}

.avatar-circle {
  width: 4rem;
  height: 4rem;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.avatar-circle.friend-request {
  background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
  color: #64748b;
}

.avatar-circle.friend-request.accepted {
  background: linear-gradient(135deg, #dcfce7 0%, #bbf7d0 100%);
  color: #16a34a;
}

.avatar-circle.friend-request.rejected {
  background: linear-gradient(135deg, #fee2e2 0%, #fecaca 100%);
  color: #dc2626;
}

.avatar-circle.event-invitation {
  background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
  color: #64748b;
}


.notification-details {
  flex: 1;
}

.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 0.75rem;
}

.notification-title {
  margin: 0;
  font-size: 1.125rem;
  color: #1e293b;
  font-weight: 600;
}

.notification-time {
  color: #64748b;
  font-size: 0.875rem;
  font-weight: 500;
}

.notification-message {
  color: #64748b;
  margin-bottom: 1rem;
  line-height: 1.6;
}

.event-details {
  background: #f8fafc;
  padding: 1rem;
  border-radius: 0.75rem;
  margin-bottom: 1rem;
  border: 1px solid #e2e8f0;
}

.event-title {
  margin: 0 0 0.75rem 0;
  color: #1e293b;
  font-weight: 600;
  font-size: 1rem;
}

.event-info {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.event-info-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
  color: #64748b;
}

.notification-actions {
  display: flex;
  gap: 0.75rem;
  flex-wrap: wrap;
}

.notification-status {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  font-weight: 600;
  font-size: 0.875rem;
}

.notification-accepted {
  border-left-color: #22c55e;
  background: #f0fdf4;
}

.notification-rejected {
  border-left-color: #ef4444;
  background: #fef2f2;
}

.status-accepted {
  color: #16a34a;
}

.status-rejected {
  color: #dc2626;
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #64748b;
  background: white;
  border-radius: 1rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.empty-state h3 {
  font-size: 1.5rem;
  font-weight: 600;
  margin: 1rem 0 0.5rem;
  color: #1e293b;
}

.empty-state p {
  font-size: 1rem;
  color: #64748b;
}

@media (max-width: 768px) {
  .notifications-page {
    padding: 1rem;
  }
  
  .notifications-header {
    padding: 2rem 1rem;
  }
  
  .header-title h1 {
    font-size: 2rem;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .filter-tabs {
    flex-direction: column;
  }
  
  .notification-content {
    flex-direction: column;
    text-align: center;
  }
  
  .notification-header {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .event-info {
    align-items: center;
  }
  
  .notification-actions {
    justify-content: center;
  }
}
</style>
