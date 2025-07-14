<template>
  <div class="notifications-container">
    <div class="notifications-header">
      <h1 class="page-title">
        <i class="cil-bell"></i>
        Notifications
      </h1>
             <div class="notification-stats">
         <span class="badge bg-primary me-2">{{ notifications.length }} total</span>
         <span class="badge bg-warning">{{ unreadCount }} unread</span>
       </div>
    </div>

    <div class="notifications-content">
      <!-- Filter Tabs -->
      <div class="filter-tabs">
        <button 
          class="filter-tab" 
          :class="{ active: activeFilter === 'all' }"
          @click="activeFilter = 'all'"
        >
          All ({{ notifications.length }})
        </button>
        <button 
          class="filter-tab" 
          :class="{ active: activeFilter === 'friend-requests' }"
          @click="activeFilter = 'friend-requests'"
        >
          Friend Requests ({{ friendRequestCount }})
        </button>
        <button 
          class="filter-tab" 
          :class="{ active: activeFilter === 'event-invitations' }"
          @click="activeFilter = 'event-invitations'"
        >
          Event Invitations ({{ eventInvitationCount }})
        </button>
      </div>

      <!-- Notifications List -->
      <div class="notifications-list" v-if="filteredNotifications.length > 0">
        <div 
          v-for="notification in filteredNotifications" 
          :key="notification.id"
          class="notification-card"
          :class="{ unread: !notification.read }"
        >
          <!-- Friend Request Notification -->
          <div v-if="notification.type === 'friend-request'" class="notification-content">
            <div class="notification-avatar">
              <div class="avatar-circle">
                <i class="cil-user-follow"></i>
              </div>
            </div>
            <div class="notification-details">
              <div class="notification-header">
                <h4 class="notification-title">
                  <strong>{{ notification.senderName }}</strong> sent you a friend request
                </h4>
                <span class="notification-time">{{ formatTime(notification.timestamp) }}</span>
              </div>
              <p class="notification-message">
                {{ notification.senderName }} wants to connect with you on Join2Play!
              </p>
              <div class="notification-actions">
                <button class="btn btn-success btn-sm" @click="acceptFriendRequest(notification.id)">
                  <i class="cil-check"></i> Accept
                </button>
                <button class="btn btn-danger btn-sm" @click="declineFriendRequest(notification.id)">
                  <i class="cil-x"></i> Decline
                </button>
              </div>
            </div>
          </div>

          <!-- Event Invitation Notification -->
          <div v-else-if="notification.type === 'event-invitation'" class="notification-content">
            <div class="notification-avatar">
              <div class="avatar-circle event">
                <i class="cil-calendar"></i>
              </div>
            </div>
            <div class="notification-details">
              <div class="notification-header">
                <h4 class="notification-title">
                  <strong>{{ notification.senderName }}</strong> invited you to join an event
                </h4>
                <span class="notification-time">{{ formatTime(notification.timestamp) }}</span>
              </div>
              <div class="event-details">
                <h5 class="event-title">{{ notification.eventTitle }}</h5>
                <div class="event-info">
                  <span class="event-date">
                    <i class="cil-calendar"></i> {{ formatDate(notification.eventDate) }}
                  </span>
                  <span class="event-location">
                    <i class="cil-location-pin"></i> {{ notification.eventLocation }}
                  </span>
                  <span class="event-sport">
                    <i class="cil-basketball"></i> {{ notification.eventSport }}
                  </span>
                </div>
              </div>
              <div class="notification-actions">
                <button class="btn btn-success btn-sm" @click="acceptEventInvitation(notification.id)">
                  <i class="cil-check"></i> Join Event
                </button>
                <button class="btn btn-secondary btn-sm" @click="declineEventInvitation(notification.id)">
                  <i class="cil-x"></i> Decline
                </button>
                <button class="btn btn-info btn-sm" @click="viewEventDetails(notification.eventId)">
                  <i class="cil-info"></i> View Details
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Empty State -->
      <div v-else class="empty-state">
        <div class="empty-icon">
          <i class="cil-bell"></i>
        </div>
        <h3>No notifications</h3>
        <p>{{ getEmptyStateMessage() }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useNotifications } from '@/services/notifications/useNotifications'

const router = useRouter()

// Use the notification service
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
  declineEventInvitation
} = useNotifications()

// Utility methods
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

onMounted(() => {
  console.log('Notifications page mounted')
})
</script>

<style scoped>
.notifications-container {
  width: 80%;
  margin: 0 auto;
  padding: 2rem;
  min-height: 100vh;
  background: #f5f6fa; /* unified light gray background */
}

.notifications-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  background: #fff; /* unified white for header */
  padding: 1.5rem;
  border-radius: 15px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.page-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin: 0;
  color: #2c3e50;
  font-size: 2rem;
  font-weight: 700;
}

.page-title i {
  color: #667eea;
  font-size: 2.2rem;
}

.notification-stats {
  display: flex;
  gap: 0.5rem;
}

.badge {
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-weight: 600;
  font-size: 0.875rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.bg-primary {
  background-color: #667eea !important;
}

.bg-warning {
  background-color: #ffc107 !important;
  color: #212529 !important;
}

.notifications-content {
  background: #fff; /* unified white for content */
  border-radius: 15px;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.filter-tabs {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  border-bottom: 2px solid #e9ecef;
  padding-bottom: 1rem;
  background: transparent;
}

.filter-tab {
  background: none;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #6c757d;
}

.filter-tab:hover {
  background: #f8f9fa;
  color: #495057;
}

.filter-tab.active {
  background: #667eea;
  color: white;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.notifications-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.notification-card {
  background: #fff; /* unified white for cards */
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  border-left: 4px solid transparent;
}

.notification-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.notification-card.unread {
  border-left-color: #667eea;
  background: #f8f9ff;
}

.notification-content {
  display: flex;
  gap: 1rem;
}

.notification-avatar {
  flex-shrink: 0;
}

.avatar-circle {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  color: white;
  background: #667eea;
}

.avatar-circle.event {
  background: #ff6b6b;
}

.notification-details {
  flex: 1;
}

.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 0.5rem;
}

.notification-title {
  margin: 0;
  font-size: 1.1rem;
  color: #2c3e50;
  font-weight: 600;
}

.notification-time {
  color: #6c757d;
  font-size: 0.875rem;
  font-weight: 500;
}

.notification-message {
  color: #6c757d;
  margin-bottom: 1rem;
  line-height: 1.5;
}

.event-details {
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 8px;
  margin-bottom: 1rem;
}

.event-title {
  margin: 0 0 0.5rem 0;
  color: #2c3e50;
  font-weight: 600;
}

.event-info {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  font-size: 0.875rem;
  color: #6c757d;
}

.event-info span {
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.notification-actions {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 6px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.875rem;
}

.btn-sm {
  padding: 0.375rem 0.75rem;
  font-size: 0.8rem;
}

.btn-success {
  background: #28a745;
  color: white;
}

.btn-success:hover {
  background: #218838;
  transform: translateY(-1px);
}

.btn-danger {
  background: #dc3545;
  color: white;
}

.btn-danger:hover {
  background: #c82333;
  transform: translateY(-1px);
}

.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background: #5a6268;
  transform: translateY(-1px);
}

.btn-info {
  background: #17a2b8;
  color: white;
}

.btn-info:hover {
  background: #138496;
  transform: translateY(-1px);
}

.empty-state {
  text-align: center;
  padding: 3rem 1rem;
  color: #6c757d;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.empty-icon {
  font-size: 4rem;
  color: #dee2e6;
  margin-bottom: 1rem;
}

.empty-state h3 {
  margin-bottom: 0.5rem;
  color: #495057;
}

.empty-state p {
  font-size: 1.1rem;
  line-height: 1.6;
}

/* Responsive Design */
@media (max-width: 768px) {
  .notifications-container {
    padding: 1rem;
  }
  
  .notifications-header {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
  
  .filter-tabs {
    flex-wrap: wrap;
    justify-content: center;
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
    justify-content: center;
  }
  
  .notification-actions {
    justify-content: center;
  }
}
</style>
