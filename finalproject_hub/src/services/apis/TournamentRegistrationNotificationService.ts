export interface TournamentRegistrationNotification {
  id: string
  tournamentId: string
  tournamentName: string
  requesterId: string
  requesterName: string
  hostId: string
  hostName: string
  registrationId: string
  createdAt: string
  isRead: boolean
}

export class TournamentRegistrationNotificationService {
  private static readonly BASE_URL = '/api/tournament-registration-notifications'

  static async getHostNotifications(): Promise<TournamentRegistrationNotification[]> {
    try {
      const response = await fetch(`${this.BASE_URL}/host`, {
        method: 'GET',
        credentials: 'include',
      })

      if (!response.ok) {
        throw new Error(`Failed to get notifications: ${response.statusText}`)
      }

      return await response.json()
    } catch (error) {
      console.error('Error getting host notifications:', error)
      return []
    }
  }

  static async getUnreadCount(): Promise<number> {
    try {
      const response = await fetch(`${this.BASE_URL}/host/unread-count`, {
        method: 'GET',
        credentials: 'include',
      })

      if (!response.ok) {
        throw new Error(`Failed to get unread count: ${response.statusText}`)
      }

      return await response.json()
    } catch (error) {
      console.error('Error getting unread count:', error)
      return 0
    }
  }

  static async markAsRead(notificationId: string): Promise<boolean> {
    try {
      const response = await fetch(`${this.BASE_URL}/${notificationId}/mark-read`, {
        method: 'PUT',
        credentials: 'include',
      })

      if (!response.ok) {
        throw new Error(`Failed to mark notification as read: ${response.statusText}`)
      }

      return true
    } catch (error) {
      console.error('Error marking notification as read:', error)
      return false
    }
  }
} 