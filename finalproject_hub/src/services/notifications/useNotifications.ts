import { ref, computed } from 'vue'

export interface Notification {
  id: number
  type: 'friend-request' | 'event-invitation'
  senderName: string
  senderId: string
  timestamp: Date
  read: boolean
  message: string
  eventId?: string
  eventTitle?: string
  eventDate?: Date
  eventLocation?: string
  eventSport?: string
}

// Mock data
const mockNotifications: Notification[] = [
  {
    id: 1,
    type: 'friend-request',
    senderName: 'John Smith',
    senderId: 'user123',
    timestamp: new Date(Date.now() - 2 * 60 * 60 * 1000), // 2 hours ago
    read: false,
    message: 'John Smith wants to connect with you on Join2Play!'
  },
  {
    id: 2,
    type: 'event-invitation',
    senderName: 'Sarah Johnson',
    senderId: 'user456',
    eventId: 'event789',
    eventTitle: 'Weekend Basketball Tournament',
    eventDate: new Date(Date.now() + 3 * 24 * 60 * 60 * 1000), // 3 days from now
    eventLocation: 'Central Park Basketball Court',
    eventSport: 'Basketball',
    timestamp: new Date(Date.now() - 1 * 60 * 60 * 1000), // 1 hour ago
    read: false,
    message: 'Sarah Johnson invited you to join Weekend Basketball Tournament'
  },
  {
    id: 3,
    type: 'friend-request',
    senderName: 'Mike Wilson',
    senderId: 'user789',
    timestamp: new Date(Date.now() - 6 * 60 * 60 * 1000), // 6 hours ago
    read: true,
    message: 'Mike Wilson wants to connect with you on Join2Play!'
  },
  {
    id: 4,
    type: 'event-invitation',
    senderName: 'Emma Davis',
    senderId: 'user101',
    eventId: 'event202',
    eventTitle: 'Tennis Doubles Match',
    eventDate: new Date(Date.now() + 1 * 24 * 60 * 60 * 1000), // 1 day from now
    eventLocation: 'Riverside Tennis Club',
    eventSport: 'Tennis',
    timestamp: new Date(Date.now() - 12 * 60 * 60 * 1000), // 12 hours ago
    read: true,
    message: 'Emma Davis invited you to join Tennis Doubles Match'
  },
  {
    id: 5,
    type: 'friend-request',
    senderName: 'Alex Thompson',
    senderId: 'user202',
    timestamp: new Date(Date.now() - 24 * 60 * 60 * 1000), // 1 day ago
    read: true,
    message: 'Alex Thompson wants to connect with you on Join2Play!'
  }
]

export function useNotifications() {
  const notifications = ref<Notification[]>(mockNotifications)
  const activeFilter = ref<'all' | 'friend-requests' | 'event-invitations'>('all')

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

  // Methods
  const markAsRead = (notificationId: number) => {
    const notification = notifications.value.find(n => n.id === notificationId)
    if (notification) {
      notification.read = true
    }
  }

  const acceptFriendRequest = (notificationId: number) => {
    markAsRead(notificationId)
    // Here you would typically make an API call to accept the friend request
    console.log('Friend request accepted:', notificationId)
  }

  const declineFriendRequest = (notificationId: number) => {
    markAsRead(notificationId)
    // Here you would typically make an API call to decline the friend request
    console.log('Friend request declined:', notificationId)
  }

  const acceptEventInvitation = (notificationId: number) => {
    markAsRead(notificationId)
    // Here you would typically make an API call to accept the event invitation
    console.log('Event invitation accepted:', notificationId)
  }

  const declineEventInvitation = (notificationId: number) => {
    markAsRead(notificationId)
    // Here you would typically make an API call to decline the event invitation
    console.log('Event invitation declined:', notificationId)
  }

  const markAllAsRead = () => {
    notifications.value.forEach(notification => {
      notification.read = true
    })
  }

  const deleteNotification = (notificationId: number) => {
    const index = notifications.value.findIndex(n => n.id === notificationId)
    if (index !== -1) {
      notifications.value.splice(index, 1)
    }
  }

  const addNotification = (notification: Omit<Notification, 'id'>) => {
    const newId = Math.max(...notifications.value.map(n => n.id), 0) + 1
    notifications.value.unshift({
      ...notification,
      id: newId
    })
  }

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
    addNotification
  }
}

// Legacy function for backward compatibility
export function useNotificationsLegacy(userId: string) {
  const notification = ref({
    message: 'Welcome to Join2Play!',
    eventId: 'event123',
    senderUserId: 'system'
  })

  return { notification }
}
