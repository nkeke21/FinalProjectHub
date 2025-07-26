import { API_BASE_URL, ENDPOINTS } from '../../constants/apis'
import { getAuthHeaders } from '../../utils/auth'
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
  private static readonly BASE_URL = '/api/teams'

  static async createTeam(teamRequest: TeamRequest): Promise<Team> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.CREATE_TEAM}`, {
      method: 'POST',
      headers: getAuthHeaders(),
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
      console.log('Fetching teams from:', `${this.BASE_URL}/my`)
      const response = await fetch(`${this.BASE_URL}/my`, {
        method: 'GET',
        headers: getAuthHeaders(),
      })

      console.log('Response status:', response.status)
      console.log('Response ok:', response.ok)

      if (!response.ok) {
        const errorText = await response.text()
        console.error('Response error text:', errorText)
        throw new Error(`Failed to get my teams: ${response.statusText}`)
      }

      const backendResponses = await response.json()
      console.log('Backend responses:', backendResponses)
      
      const convertedTeams = backendResponses.map(this.convertToTeam)
      console.log('Converted teams:', convertedTeams)
      
      return convertedTeams
    } catch (error) {
      console.error('Error getting my teams:', error)
      return []
    }
  }

  static async getTeamsByCaptain(): Promise<Team[]> {
    try {
      const response = await fetch(`${this.BASE_URL}/captain`, {
        method: 'GET',
        headers: getAuthHeaders(),
      })

      if (!response.ok) {
        throw new Error(`Failed to get captain teams: ${response.statusText}`)
      }

      const backendResponses = await response.json()
      return backendResponses.map(this.convertToTeam)
    } catch (error) {
      console.error('Error getting captain teams:', error)
      return []
    }
  }

  static async getTeamById(teamId: string): Promise<Team | null> {
    try {
      console.log('Getting team by ID:', teamId)
      const response = await fetch(`${this.BASE_URL}/${teamId}`, {
        method: 'GET',
        headers: getAuthHeaders(),
      })

      console.log('Team response status:', response.status)
      console.log('Team response ok:', response.ok)

      if (!response.ok) {
        if (response.status === 404) {
          console.log('Team not found (404)')
          return null
        }
        const errorText = await response.text()
        console.error('Team error response:', errorText)
        throw new Error(`Failed to get team: ${response.statusText}`)
      }

      const backendResponse = await response.json()
      console.log('Raw team data:', backendResponse)
      const convertedTeam = this.convertToTeam(backendResponse)
      console.log('Converted team:', convertedTeam)
      return convertedTeam
    } catch (error) {
      console.error('Error getting team:', error)
      return null
    }
  }

  static async getAvailableTeams(): Promise<Team[]> {
    try {
      const response = await fetch(`${this.BASE_URL}/available`, {
        method: 'GET',
        headers: getAuthHeaders(),
      })

      if (!response.ok) {
        throw new Error(`Failed to get available teams: ${response.statusText}`)
      }

      const backendResponses = await response.json()
      return backendResponses.map(this.convertToTeam)
    } catch (error) {
      console.error('Error getting available teams:', error)
      return []
    }
  }

  static async getTeam(teamId: string): Promise<Team> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.GET_TEAM(teamId)}`, {
      method: 'GET',
      headers: getAuthHeaders()
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
      headers: getAuthHeaders()
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
      headers: getAuthHeaders(),
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
      headers: getAuthHeaders()
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

  private static convertToTeam(backendResponse: any): Team {
    return {
      id: backendResponse.id,
      name: backendResponse.name,
      description: backendResponse.description,
      sportType: backendResponse.sportType,
      captainId: backendResponse.captainId,
      captainName: backendResponse.captainName,
      members: (backendResponse.members || []).map((member: any) => ({
        userId: member.userId,
        name: member.name,
        email: member.email,
        age: member.age,
        role: member.role,
        joinedAt: new Date(member.joinedAt).toISOString()
      })),
      maxMembers: backendResponse.maxMembers,
      minAge: backendResponse.minAge,
      maxAge: backendResponse.maxAge,
      createdAt: new Date(backendResponse.createdAt).toISOString(),
      updatedAt: new Date(backendResponse.updatedAt).toISOString()
    }
  }
} 