import { API_BASE_URL, ENDPOINTS } from '../../constants/apis'
import { getAuthHeaders } from '../../utils/auth'

export interface TeamJoinRequest {
  requestId: string
  teamId: string
  fromUserId: string
  sentAt: string
  status: 'PENDING' | 'APPROVED' | 'REJECTED'
}

export interface TeamJoinRequestDTO {
  teamId: string
  fromUserId: string
}

export interface TeamJoinRequestResponseDTO {
  requestId: string
  status: string
}

export class TeamJoinRequestService {
  static async sendJoinRequest(teamId: string): Promise<string> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.SEND_TEAM_JOIN_REQUEST}`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({ teamId })
    })

    if (!response.ok) {
      const errorData = await response.text()
      console.error('Failed to send team join request:', errorData)
      throw new Error(errorData || 'Failed to send team join request')
    }

    return await response.text()
  }

  static async getPendingRequestsForTeam(teamId: string): Promise<TeamJoinRequest[]> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.GET_PENDING_TEAM_REQUESTS_FOR_TEAM(teamId)}`, {
      method: 'GET',
      headers: HEADERS.JSON,
      credentials: 'include'
    })

    if (!response.ok) {
      console.error('Failed to get pending team requests for team')
      throw new Error('Failed to get pending team requests for team')
    }

    return await response.json()
  }

  static async getPendingRequestsForUser(): Promise<TeamJoinRequest[]> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.GET_PENDING_TEAM_REQUESTS_FOR_USER}`, {
      method: 'GET',
      headers: HEADERS.JSON,
      credentials: 'include'
    })

    if (!response.ok) {
      console.error('Failed to get pending team requests for user')
      throw new Error('Failed to get pending team requests for user')
    }

    return await response.json()
  }

  static async getPendingRequestsForTeamCaptain(): Promise<TeamJoinRequest[]> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.GET_PENDING_TEAM_REQUESTS_FOR_CAPTAIN}`, {
      method: 'GET',
      headers: HEADERS.JSON,
      credentials: 'include'
    })

    if (!response.ok) {
      console.error('Failed to get pending team requests for captain')
      throw new Error('Failed to get pending team requests for captain')
    }

    return await response.json()
  }

  static async getAllRequestsForTeam(teamId: string): Promise<TeamJoinRequest[]> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.GET_ALL_TEAM_REQUESTS_FOR_TEAM(teamId)}`, {
      method: 'GET',
      headers: HEADERS.JSON,
      credentials: 'include'
    })

    if (!response.ok) {
      console.error('Failed to get all team requests for team')
      throw new Error('Failed to get all team requests for team')
    }

    return await response.json()
  }

  static async getAllRequestsForUser(): Promise<TeamJoinRequest[]> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.GET_ALL_TEAM_REQUESTS_FOR_USER}`, {
      method: 'GET',
      headers: HEADERS.JSON,
      credentials: 'include'
    })

    if (!response.ok) {
      console.error('Failed to get all team requests for user')
      throw new Error('Failed to get all team requests for user')
    }

    return await response.json()
  }

  static async respondToRequest(requestId: string, status: 'APPROVED' | 'REJECTED'): Promise<TeamJoinRequest> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.RESPOND_TO_TEAM_JOIN_REQUEST}`, {
      method: 'POST',
      headers: HEADERS.JSON,
      credentials: 'include',
      body: JSON.stringify({ requestId, status })
    })

    if (!response.ok) {
      console.error('Failed to respond to team join request')
      throw new Error('Failed to respond to team join request')
    }

    return await response.json()
  }

  static async checkRequestBetweenTeamAndUser(teamId: string, userId: string): Promise<TeamJoinRequest | null> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.CHECK_TEAM_JOIN_REQUEST(teamId, userId)}`, {
      method: 'GET',
      headers: HEADERS.JSON,
      credentials: 'include'
    })

    if (response.status === 404) {
      return null
    }

    if (!response.ok) {
      console.error('Failed to check team join request')
      throw new Error('Failed to check team join request')
    }

    return await response.json()
  }

  static async getCurrentUserId(): Promise<string> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.GET_CURRENT_USER_ID}`, {
      method: 'GET',
      headers: HEADERS.JSON,
      credentials: 'include'
    })

    if (!response.ok) {
      console.error('Failed to get current user ID')
      throw new Error('Failed to get current user ID')
    }

    return await response.text()
  }
} 