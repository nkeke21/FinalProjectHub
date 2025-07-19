import { API_BASE_URL, ENDPOINTS, HEADERS } from '../../constants/apis'
import type { Team, TeamMember } from '../../models/Tournament'

export interface TeamRequest {
  name: string
  description: string
  sportType: string
  maxMembers: number
  minAge: number
  maxAge: number
}

export class UserTeamService {
  static async createTeam(teamRequest: TeamRequest): Promise<Team> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.CREATE_TEAM}`, {
      method: 'POST',
      headers: HEADERS.JSON,
      credentials: 'include',
      body: JSON.stringify(teamRequest)
    })

    if (!response.ok) {
      const errorData = await response.json()
      console.error('Failed to create team:', errorData)
      throw new Error('Failed to create team')
    }

    return await response.json()
  }

  static async getMyTeams(): Promise<Team[]> {
    try {
      const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.MY_TEAMS}`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
      })

      if (!response.ok) {
        const errorData = await response.json()
        console.error('Failed to fetch my teams:', errorData)
        throw new Error('Failed to fetch my teams')
      }

      return await response.json()
    } catch (error) {
      console.error('Error getting my teams:', error)
      return []
    }
  }

  static async getAvailableTeams(): Promise<Team[]> {
    try {
      const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.AVAILABLE_TEAMS}`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
      })

      if (!response.ok) {
        const errorData = await response.json()
        console.error('Failed to fetch available teams:', errorData)
        throw new Error('Failed to fetch available teams')
      }

      return await response.json()
    } catch (error) {
      console.error('Error getting available teams:', error)
      return []
    }
  }

  static async getTeam(teamId: string): Promise<Team> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.GET_TEAM(teamId)}`, {
      method: 'GET',
      headers: HEADERS.JSON,
      credentials: 'include'
    })

    if (!response.ok) {
      const errorData = await response.json()
      console.error('Failed to fetch team:', errorData)
      throw new Error('Failed to fetch team')
    }

    return await response.json()
  }

  static async joinTeam(teamId: string): Promise<Team> {
    throw new Error('Please use TeamJoinRequestService.sendJoinRequest() to request joining a team')
  }

  static async leaveTeam(teamId: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.LEAVE_TEAM(teamId)}`, {
      method: 'DELETE',
      headers: HEADERS.JSON,
      credentials: 'include'
    })

    if (!response.ok) {
      const errorData = await response.json()
      console.error('Failed to leave team:', errorData)
      throw new Error('Failed to leave team')
    }
  }

  static async updateTeam(teamId: string, teamRequest: TeamRequest): Promise<Team> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.UPDATE_TEAM(teamId)}`, {
      method: 'PUT',
      headers: HEADERS.JSON,
      credentials: 'include',
      body: JSON.stringify(teamRequest)
    })

    if (!response.ok) {
      const errorData = await response.json()
      console.error('Failed to update team:', errorData)
      throw new Error('Failed to update team')
    }

    return await response.json()
  }

  static async removeTeamMember(teamId: string, memberId: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.REMOVE_TEAM_MEMBER(teamId, memberId)}`, {
      method: 'DELETE',
      headers: HEADERS.JSON,
      credentials: 'include'
    })

    if (!response.ok) {
      const errorData = await response.text()
      console.error('Failed to remove team member:', errorData)
      throw new Error(errorData || 'Failed to remove team member')
    }
  }

  static async getUserTeams(sportType?: string): Promise<Team[]> {
    return this.getMyTeams()
  }

  static async isUserTeamMember(userId: string, teamId: string): Promise<boolean> {
    try {
      const team = await this.getTeam(teamId)
      return team.members.some(member => member.userId === userId)
    } catch (error) {
      console.error('Error checking team membership:', error)
      return false
    }
  }

  static async getUserTeamMemberships(userId: string): Promise<Team[]> {
    return this.getMyTeams()
  }
} 