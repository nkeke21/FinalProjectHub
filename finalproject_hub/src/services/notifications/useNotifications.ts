import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/store/profile/userStore'
import type { FriendRequest } from '@/services/apis/FriendRequestService'

export interface Notification {
  id: string
  type: 'friend-request' | 'event-invitation'
  senderName: string
  senderId: string
  timestamp: Date
  read: boolean
  message: string
  status?: 'PENDING' | 'ACCEPTED' | 'REJECTED'
  eventId?: string
  eventTitle?: string
  eventDate?: Date
  eventLocation?: string
  eventSport?: string
  requestId?: string
}

export function useNotifications() {
  const userStore = useUserStore()
  const notifications = ref<Notification[]>([])
  const activeFilter = ref<'all' | 'friend-requests' | 'event-invitations'>('all')

  const convertFriendRequestsToNotifications = (friendRequests: FriendRequest[]): Notification[] => {
    return friendRequests.map(request => ({
      id: request.requestId,
      type: 'friend-request',
      senderName: `User ${request.fromUserId.slice(0, 8)}`,
      senderId: request.fromUserId,
      timestamp: new Date(request.sentAt),
      read: false,
      message: `User ${request.fromUserId.slice(0, 8)} wants to connect with you on Join2Play!`,
      status: request.status,
      requestId: request.requestId
    }))
  }

  const loadFriendRequests = async () => {
    try {
      console.log('Loading friend requests...')
      await userStore.fetchCurrentUserPendingFriendRequests()
      console.log('Pending friend requests from store:', userStore.pendingFriendRequests)
      
      const friendRequestNotifications = convertFriendRequestsToNotifications(userStore.pendingFriendRequests)
      console.log('Converted to notifications:', friendRequestNotifications)
      
      const existingNotifications = notifications.value.filter(n => n.type !== 'friend-request')
      notifications.value = [...existingNotifications, ...friendRequestNotifications]
      console.log('Updated notifications array:', notifications.value)
    } catch (error) {
      console.error('Failed to load friend requests:', error)
    }
  }

  // Computed properties
  const filteredNotifications = computed(() => {
    if (activeFilter.value === 'all') {
      return notifications.value
    }
    const filterType = activeFilter.value === 'friend-requests' ? 'friend-request' : 'event-invitation'
    return notifications.value.filter(notification => notification.type === filterType)
  })

  const unreadCount = computed(() => {
    return notifications.value.filter(notification => !notification.read).length
  })

  const friendRequestCount = computed(() => {
    return notifications.value.filter(notification => notification.type === 'friend-request').length
  })

  const eventInvitationCount = computed(() => {
    return notifications.value.filter(notification => notification.type === 'event-invitation').length
  })

  const markAsRead = (notificationId: string) => {
    const notification = notifications.value.find(n => n.id === notificationId)
    if (notification) {
      notification.read = true
    }
  }

  const acceptFriendRequest = async (notificationId: string) => {
    try {
      const updatedRequest = await userStore.respondToFriendRequest(notificationId, 'ACCEPTED')
      markAsRead(notificationId)
      
      const notification = notifications.value.find(n => n.id === notificationId)
      if (notification && updatedRequest) {
        notification.status = updatedRequest.status
        notification.message = `Friend request accepted! You are now connected with ${notification.senderName}`
        
        setTimeout(() => {
          const index = notifications.value.findIndex(n => n.id === notificationId)
          if (index !== -1) {
            notifications.value.splice(index, 1)
          }
        }, 5000)
      }
      
      console.log('Friend request accepted:', notificationId)
    } catch (error) {
      console.error('Failed to accept friend request:', error)
      throw error
    }
  }

  const declineFriendRequest = async (notificationId: string) => {
    try {
      const updatedRequest = await userStore.respondToFriendRequest(notificationId, 'REJECTED')
      markAsRead(notificationId)
      
      const notification = notifications.value.find(n => n.id === notificationId)
      if (notification && updatedRequest) {
        notification.status = updatedRequest.status
        notification.message = `Friend request declined from ${notification.senderName}`
        
        setTimeout(() => {
          const index = notifications.value.findIndex(n => n.id === notificationId)
          if (index !== -1) {
            notifications.value.splice(index, 1)
          }
        }, 5000)
      }
      
      console.log('Friend request declined:', notificationId)
    } catch (error) {
      console.error('Failed to decline friend request:', error)
      throw error
    }
  }

  const acceptEventInvitation = (notificationId: string) => {
    markAsRead(notificationId)
    console.log('Event invitation accepted:', notificationId)
  }

  const declineEventInvitation = (notificationId: string) => {
    markAsRead(notificationId)
    console.log('Event invitation declined:', notificationId)
  }

  const markAllAsRead = () => {
    notifications.value.forEach(notification => {
      notification.read = true
    })
  }

  const deleteNotification = (notificationId: string) => {
    const index = notifications.value.findIndex(n => n.id === notificationId)
    if (index !== -1) {
      notifications.value.splice(index, 1)
    }
  }

  const addNotification = (notification: Omit<Notification, 'id'>) => {
    const newId = Math.random().toString(36).substr(2, 9)
    notifications.value.unshift({
      ...notification,
      id: newId
    })
  }

  const refreshNotifications = async () => {
    await loadFriendRequests()
  }

  onMounted(() => {
    loadFriendRequests()
  })

  return {
    notifications,
    activeFilter,
    filteredNotifications,
    unreadCount,
    friendRequestCount,
    eventInvitationCount,
    markAsRead,
    acceptFriendRequest,
    declineFriendRequest,
    acceptEventInvitation,
    declineEventInvitation,
    markAllAsRead,
    deleteNotification,
    addNotification,
    refreshNotifications
  }
}

export function useNotificationsLegacy(userId: string) {
  const notification = ref({
    message: 'Welcome to Join2Play!',
    eventId: 'event123',
    senderUserId: 'system'
  })

  return { notification }
}
