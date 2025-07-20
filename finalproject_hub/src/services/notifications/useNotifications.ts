import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/store/profile/userStore'
import type { FriendRequest } from '@/services/apis/FriendRequestService'
import { TeamJoinRequestService, type TeamJoinRequest } from '@/services/apis/TeamJoinRequestService'
import { UserService, type UserDetails } from '@/services/apis/UserService'
import { UserTeamService, type Team } from '@/services/apis/UserTeamService'
import { EventInvitationService, type EventInvitation } from '@/services/apis/EventInvitationService'

export interface Notification {
  id: string
  type: 'friend-request' | 'event-invitation' | 'team-join-request'
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
  teamId?: string
  teamName?: string
}

export function useNotifications() {
  const userStore = useUserStore()
  const notifications = ref<Notification[]>([])
  const activeFilter = ref<'all' | 'friend-requests' | 'event-invitations' | 'team-join-requests'>('all')

  const convertFriendRequestsToNotifications = async (friendRequests: FriendRequest[]): Promise<Notification[]> => {
    const notifications: Notification[] = []
    
    for (const request of friendRequests) {
      try {
        const userDetails = await UserService.getUserById(request.fromUserId)
        const senderName = userDetails.name || `User ${request.fromUserId.slice(0, 8)}`
        
        notifications.push({
          id: request.requestId,
          type: 'friend-request',
          senderName: senderName,
          senderId: request.fromUserId,
          timestamp: new Date(request.sentAt),
          read: false,
          message: `${senderName} wants to connect with you on Join2Play!`,
          status: request.status,
          requestId: request.requestId
        })
      } catch (error) {
        console.error(`Failed to fetch user details for ${request.fromUserId}:`, error)
        // Fallback to truncated user ID if user details fetch fails
        notifications.push({
          id: request.requestId,
          type: 'friend-request',
          senderName: `User ${request.fromUserId.slice(0, 8)}`,
          senderId: request.fromUserId,
          timestamp: new Date(request.sentAt),
          read: false,
          message: `User ${request.fromUserId.slice(0, 8)} wants to connect with you on Join2Play!`,
          status: request.status,
          requestId: request.requestId
        })
      }
    }
    
    return notifications
  }

  const convertTeamJoinRequestsToNotifications = async (teamRequests: TeamJoinRequest[]): Promise<Notification[]> => {
    const notifications: Notification[] = []
    
    for (const request of teamRequests) {
      try {
        const userDetails = await UserService.getUserById(request.fromUserId)
        const senderName = userDetails.name || `User ${request.fromUserId.slice(0, 8)}`
        
        const teamDetails = await UserTeamService.getTeam(request.teamId)
        const teamName = teamDetails.name || `Team ${request.teamId.slice(0, 8)}`
        
        notifications.push({
          id: request.requestId,
          type: 'team-join-request',
          senderName: senderName,
          senderId: request.fromUserId,
          timestamp: new Date(request.sentAt),
          read: false,
          message: `${senderName} wants to join your team "${teamName}"`,
          status: request.status,
          requestId: request.requestId,
          teamId: request.teamId,
          teamName: teamName
        })
      } catch (error) {
        console.error(`Failed to fetch details for request ${request.requestId}:`, error)
        notifications.push({
          id: request.requestId,
          type: 'team-join-request',
          senderName: `User ${request.fromUserId.slice(0, 8)}`,
          senderId: request.fromUserId,
          timestamp: new Date(request.sentAt),
          read: false,
          message: `User ${request.fromUserId.slice(0, 8)} wants to join your team`,
          status: request.status,
          requestId: request.requestId,
          teamId: request.teamId,
          teamName: `Team ${request.teamId.slice(0, 8)}`
        })
      }
    }
    
    return notifications
  }

  const convertEventInvitationsToNotifications = async (eventInvitations: EventInvitation[]): Promise<Notification[]> => {
    const notifications: Notification[] = []
    
    for (const invitation of eventInvitations) {
      try {
        const userDetails = await UserService.getUserById(invitation.fromUserId)
        const senderName = userDetails.name || `User ${invitation.fromUserId.slice(0, 8)}`
        
        // Try to fetch event details to make notification more informative
        let eventTitle = 'Sports Event'
        let eventDate: Date | undefined
        let eventLocation = ''
        let eventSport = ''
        
        try {
          // Import SportEventService dynamically to avoid circular dependencies
          const { getSportEventById } = await import('../apis/SportEventService')
          const eventResponse = await getSportEventById(invitation.eventId)
          if (eventResponse.ok) {
            const eventData = await eventResponse.json()
            eventTitle = eventData.description || 'Sports Event'
            eventDate = eventData.eventTime ? new Date(eventData.eventTime) : undefined
            eventLocation = eventData.location || ''
            eventSport = eventData.sportType || ''
          }
        } catch (eventError) {
          console.warn(`Failed to fetch event details for ${invitation.eventId}:`, eventError)
        }
        
        notifications.push({
          id: invitation.invitationId,
          type: 'event-invitation',
          senderName: senderName,
          senderId: invitation.fromUserId,
          timestamp: new Date(invitation.sentAt),
          read: false,
          message: `${senderName} invited you to join "${eventTitle}"`,
          status: invitation.status,
          eventId: invitation.eventId,
          eventTitle: eventTitle,
          eventDate: eventDate,
          eventLocation: eventLocation,
          eventSport: eventSport
        })
      } catch (error) {
        console.error(`Failed to fetch user details for ${invitation.fromUserId}:`, error)
        notifications.push({
          id: invitation.invitationId,
          type: 'event-invitation',
          senderName: `User ${invitation.fromUserId.slice(0, 8)}`,
          senderId: invitation.fromUserId,
          timestamp: new Date(invitation.sentAt),
          read: false,
          message: `User ${invitation.fromUserId.slice(0, 8)} invited you to join an event`,
          status: invitation.status,
          eventId: invitation.eventId,
          eventTitle: 'Sports Event',
          eventDate: undefined,
          eventLocation: '',
          eventSport: ''
        })
      }
    }
    
    return notifications
  }

  const loadFriendRequests = async () => {
    try {
      console.log('Loading friend requests...')
      await userStore.fetchCurrentUserPendingFriendRequests()
      console.log('Pending friend requests from store:', userStore.pendingFriendRequests)
      
      const friendRequestNotifications = await convertFriendRequestsToNotifications(userStore.pendingFriendRequests)
      console.log('Converted to notifications:', friendRequestNotifications)
      
      const existingNotifications = notifications.value.filter(n => n.type !== 'friend-request')
      notifications.value = [...existingNotifications, ...friendRequestNotifications]
      console.log('Updated notifications array:', notifications.value)
    } catch (error) {
      console.error('Failed to load friend requests:', error)
    }
  }

  const loadTeamJoinRequests = async () => {
    try {
      console.log('Loading team join requests for captain...')
      const teamRequests = await TeamJoinRequestService.getPendingRequestsForTeamCaptain()
      console.log('Pending team join requests for captain:', teamRequests)
      
      const teamRequestNotifications = await convertTeamJoinRequestsToNotifications(teamRequests)
      console.log('Converted team requests to notifications:', teamRequestNotifications)
      
      const existingNotifications = notifications.value.filter(n => n.type !== 'team-join-request')
      notifications.value = [...existingNotifications, ...teamRequestNotifications]
      console.log('Updated notifications array with team requests:', notifications.value)
    } catch (error) {
      console.error('Failed to load team join requests:', error)
    }
  }

  const loadEventInvitations = async () => {
    try {
      console.log('Loading event invitations...')
      const eventInvitations = await EventInvitationService.getPendingInvitations()
      console.log('Pending event invitations:', eventInvitations)
      
      if (eventInvitations.length === 0) {
        console.log('No pending event invitations found')
        return
      }
      
      const eventInvitationNotifications = await convertEventInvitationsToNotifications(eventInvitations)
      console.log('Converted event invitations to notifications:', eventInvitationNotifications)
      
      const existingNotifications = notifications.value.filter(n => n.type !== 'event-invitation')
      notifications.value = [...existingNotifications, ...eventInvitationNotifications]
      console.log('Updated notifications array with event invitations:', notifications.value)
    } catch (error) {
      console.error('Failed to load event invitations:', error)
    }
  }

  const filteredNotifications = computed(() => {
    if (activeFilter.value === 'all') {
      return notifications.value
    }
    let filterType: string
    switch (activeFilter.value) {
      case 'friend-requests':
        filterType = 'friend-request'
        break
      case 'team-join-requests':
        filterType = 'team-join-request'
        break
      default:
        filterType = 'event-invitation'
    }
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

  const teamJoinRequestCount = computed(() => {
    return notifications.value.filter(notification => notification.type === 'team-join-request').length
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

  const acceptEventInvitation = async (notificationId: string) => {
    try {
      // First, respond to the invitation
      await EventInvitationService.respondToInvitation({
        invitationId: notificationId,
        status: 'ACCEPTED'
      })
      
      // Get the notification to find the event ID
      const notification = notifications.value.find(n => n.id === notificationId)
      if (notification && notification.eventId) {
        // Join the event
        try {
          const { joinEvent } = await import('../apis/SportEventService')
          await joinEvent(notification.eventId)
          console.log('Successfully joined event:', notification.eventId)
        } catch (joinError) {
          console.error('Failed to join event:', joinError)
          // Don't throw here - the invitation was still accepted
        }
      }
      
      markAsRead(notificationId)
      
      if (notification) {
        notification.status = 'ACCEPTED'
        notification.message = `Event invitation accepted - you've joined the event!`
        
        setTimeout(() => {
          const index = notifications.value.findIndex(n => n.id === notificationId)
          if (index !== -1) {
            notifications.value.splice(index, 1)
          }
        }, 5000)
      }
      
      console.log('Event invitation accepted:', notificationId)
    } catch (error) {
      console.error('Failed to accept event invitation:', error)
      throw error
    }
  }

  const declineEventInvitation = async (notificationId: string) => {
    try {
      await EventInvitationService.respondToInvitation({
        invitationId: notificationId,
        status: 'REJECTED'
      })
      markAsRead(notificationId)
      
      const notification = notifications.value.find(n => n.id === notificationId)
      if (notification) {
        notification.status = 'REJECTED'
        notification.message = `Event invitation declined`
        
        setTimeout(() => {
          const index = notifications.value.findIndex(n => n.id === notificationId)
          if (index !== -1) {
            notifications.value.splice(index, 1)
          }
        }, 5000)
      }
      
      console.log('Event invitation declined:', notificationId)
    } catch (error) {
      console.error('Failed to decline event invitation:', error)
      throw error
    }
  }

  const approveTeamJoinRequest = async (notificationId: string) => {
    try {
      await TeamJoinRequestService.respondToRequest(notificationId, 'APPROVED')
      markAsRead(notificationId)
      
      const notification = notifications.value.find(n => n.id === notificationId)
      if (notification) {
        notification.status = 'ACCEPTED'
        notification.message = `Team join request approved!`
        
        setTimeout(() => {
          const index = notifications.value.findIndex(n => n.id === notificationId)
          if (index !== -1) {
            notifications.value.splice(index, 1)
          }
        }, 5000)
      }
      
      console.log('Team join request approved:', notificationId)
    } catch (error) {
      console.error('Failed to approve team join request:', error)
      throw error
    }
  }

  const declineTeamJoinRequest = async (notificationId: string) => {
    try {
      await TeamJoinRequestService.respondToRequest(notificationId, 'REJECTED')
      markAsRead(notificationId)
      
      const notification = notifications.value.find(n => n.id === notificationId)
      if (notification) {
        notification.status = 'REJECTED'
        notification.message = `Team join request declined`
        
        setTimeout(() => {
          const index = notifications.value.findIndex(n => n.id === notificationId)
          if (index !== -1) {
            notifications.value.splice(index, 1)
          }
        }, 5000)
      }
      
      console.log('Team join request declined:', notificationId)
    } catch (error) {
      console.error('Failed to decline team join request:', error)
      throw error
    }
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
    await Promise.all([
      loadFriendRequests(),
      loadTeamJoinRequests(),
      loadEventInvitations()
    ])
  }

  onMounted(() => {
    refreshNotifications()
  })

  return {
    notifications,
    activeFilter,
    filteredNotifications,
    unreadCount,
    friendRequestCount,
    eventInvitationCount,
    teamJoinRequestCount,
    markAsRead,
    acceptFriendRequest,
    declineFriendRequest,
    acceptEventInvitation,
    declineEventInvitation,
    approveTeamJoinRequest,
    declineTeamJoinRequest,
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
