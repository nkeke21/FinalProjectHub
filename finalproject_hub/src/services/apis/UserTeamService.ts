import { mockTeams } from '../../data/mockTournaments'
import type { Team } from '../../models/Tournament'

export class UserTeamService {
  static async getUserTeams(sportType?: string): Promise<Team[]> {
    try {
      await new Promise(resolve => setTimeout(resolve, 200))
      
      let userTeams = mockTeams
      if (sportType) {
        userTeams = mockTeams.filter(team => team.sportType === sportType)
      }
      
      return userTeams
    } catch (error) {
      console.error('Error getting user teams:', error)
      return []
    }
  }

  static async isUserTeamMember(userId: string, teamId: string): Promise<boolean> {
    try {
      await new Promise(resolve => setTimeout(resolve, 100))
      
      const team = mockTeams.find(t => t.id === teamId)
      if (!team) return false
      
      return team.members.some(member => member.userId === userId)
    } catch (error) {
      console.error('Error checking team membership:', error)
      return false
    }
  }

  static async getUserTeamMemberships(userId: string): Promise<Team[]> {
    try {
      await new Promise(resolve => setTimeout(resolve, 200))
      
      return mockTeams.filter(team => 
        team.members.some(member => member.userId === userId)
      )
    } catch (error) {
      console.error('Error getting user team memberships:', error)
      return []
    }
  }
} 