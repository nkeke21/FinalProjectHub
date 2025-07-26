import { API_BASE_URL } from '../../constants/apis'
import { getAuthHeaders } from '../../utils/auth'

export interface EventInvitationDTO {
  eventId: string
  toUserId: string
  message?: string
}

export interface EventInvitationResponseDTO {
  invitationId: string
  status: 'ACCEPTED' | 'REJECTED'
}

export interface EventInvitation {
  invitationId: string
  eventId: string
  fromUserId: string
  toUserId: string
  sentAt: string
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED'
}

export interface EventInvitationNotification {
  invitationId: string
  eventId: string
  fromUserId: string
  senderName: string
  eventDescription: string
  eventTime: string
  location: string
  message: string
}

export class EventInvitationService {
  static async sendInvitation(invitation: EventInvitationDTO): Promise<{ message: string; invitationId: string }> {
    console.log('Sending event invitation:', invitation)
    const response = await fetch(`${API_BASE_URL}/api/event-invitations/send`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(invitation)
    })

    console.log('Send invitation response status:', response.status)
    
    if (!response.ok) {
      const errorData = await response.json()
      console.error('Failed to send invitation:', errorData)
      throw new Error(errorData.error || 'Failed to send invitation')
    }

    const result = await response.json()
    console.log('Invitation sent successfully:', result)
    return result
  }

  static async getPendingInvitations(): Promise<EventInvitation[]> {
    console.log('Fetching pending event invitations...')
    const response = await fetch(`${API_BASE_URL}/api/event-invitations/user/pending`, {
      method: 'GET',
      headers: getAuthHeaders()
    })

    console.log('Event invitations response status:', response.status)
    
    if (!response.ok) {
      const errorText = await response.text()
      console.error('Failed to fetch pending invitations:', errorText)
      throw new Error('Failed to fetch pending invitations')
    }

    const invitations = await response.json()
    console.log('Received event invitations:', invitations)
    return invitations
  }

  static async getSentInvitations(): Promise<EventInvitation[]> {
    const response = await fetch(`${API_BASE_URL}/api/event-invitations/user/sent`, {
      method: 'GET',
      headers: getAuthHeaders()
    })

    if (!response.ok) {
      throw new Error('Failed to fetch sent invitations')
    }

    return await response.json()
  }

  static async respondToInvitation(response: EventInvitationResponseDTO): Promise<EventInvitation> {
    const apiResponse = await fetch(`${API_BASE_URL}/api/event-invitations/respond`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(response)
    })

    if (!apiResponse.ok) {
      const errorData = await apiResponse.json()
      throw new Error(errorData.error || 'Failed to respond to invitation')
    }

    return await apiResponse.json()
  }

  static async deleteInvitation(invitationId: string): Promise<{ message: string }> {
    const response = await fetch(`${API_BASE_URL}/api/event-invitations/${invitationId}`, {
      method: 'DELETE',
      headers: getAuthHeaders()
    })

    if (!response.ok) {
      const errorData = await response.json()
      throw new Error(errorData.error || 'Failed to delete invitation')
    }

    return await response.json()
  }

  static async checkInvitationBetweenEventAndUser(eventId: string, userId: string): Promise<EventInvitation | null> {
    const response = await fetch(`${API_BASE_URL}/api/event-invitations/check-invitation/${eventId}/${userId}`, {
      method: 'GET',
      headers: getAuthHeaders()
    })

    if (response.status === 404) {
      return null
    }

    if (!response.ok) {
      throw new Error('Failed to check invitation')
    }

    return await response.json()
  }
} 