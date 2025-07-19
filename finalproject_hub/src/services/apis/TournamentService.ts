import { Tournament } from '../../models/Tournament'

export interface TournamentRequest {
  name: string
  description: string
  sportType: string
  format: string
  tournamentType: 'individual' | 'team'
  location: string
  latitude: number
  longitude: number
  startDate: string
  endDate: string
  registrationDeadline: string
  maxParticipants: number
  entryFee: number
  prizePool: number
  minAge: number
  maxAge: number
  rules: string[]
}

export interface TournamentResponse {
  id: string
  name: string
  description: string
  sportType: string
  format: string
  tournamentType: string
  status: string
  hostId: string
  hostName: string
  location: string
  latitude: number
  longitude: number
  startDate: string
  endDate: string
  registrationDeadline: string
  maxParticipants: number
  currentParticipants: number
  entryFee: number
  prizePool: number
  minAge: number
  maxAge: number
  rules: string[]
  createdAt: string
  updatedAt: string
}

export class TournamentService {
  private static readonly BASE_URL = '/api/tournaments'

  static async createTournament(request: TournamentRequest): Promise<TournamentResponse> {
    try {
      const response = await fetch(this.BASE_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(request),
        credentials: 'include', // Include session cookies
      })

      if (!response.ok) {
        throw new Error(`Failed to create tournament: ${response.statusText}`)
      }

      return await response.json()
    } catch (error) {
      console.error('Error creating tournament:', error)
      throw error
    }
  }

  static async getTournamentById(id: string): Promise<TournamentResponse> {
    try {
      const response = await fetch(`${this.BASE_URL}/${id}`)

      if (!response.ok) {
        throw new Error(`Failed to get tournament: ${response.statusText}`)
      }

      return await response.json()
    } catch (error) {
      console.error('Error getting tournament:', error)
      throw error
    }
  }

  static async getAllTournaments(): Promise<TournamentResponse[]> {
    try {
      const response = await fetch(this.BASE_URL)

      if (!response.ok) {
        throw new Error(`Failed to get tournaments: ${response.statusText}`)
      }

      return await response.json()
    } catch (error) {
      console.error('Error getting tournaments:', error)
      throw error
    }
  }

  static async getCurrentUserTournaments(): Promise<TournamentResponse[]> {
    try {
      const response = await fetch(`${this.BASE_URL}/host`, {
        credentials: 'include', // Include session cookies
      })

      if (!response.ok) {
        throw new Error(`Failed to get current user tournaments: ${response.statusText}`)
      }

      return await response.json()
    } catch (error) {
      console.error('Error getting current user tournaments:', error)
      throw error
    }
  }

  static async updateTournament(id: string, request: TournamentRequest): Promise<TournamentResponse> {
    try {
      const response = await fetch(`${this.BASE_URL}/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(request),
        credentials: 'include', // Include session cookies
      })

      if (!response.ok) {
        throw new Error(`Failed to update tournament: ${response.statusText}`)
      }

      return await response.json()
    } catch (error) {
      console.error('Error updating tournament:', error)
      throw error
    }
  }

  static async deleteTournament(id: string): Promise<void> {
    try {
      const response = await fetch(`${this.BASE_URL}/${id}`, {
        method: 'DELETE',
        credentials: 'include', // Include session cookies
      })

      if (!response.ok) {
        throw new Error(`Failed to delete tournament: ${response.statusText}`)
      }
    } catch (error) {
      console.error('Error deleting tournament:', error)
      throw error
    }
  }

  static async updateTournamentStatus(id: string, status: string): Promise<TournamentResponse> {
    try {
      const response = await fetch(`${this.BASE_URL}/${id}/status?status=${status}`, {
        method: 'PATCH',
        credentials: 'include', // Include session cookies
      })

      if (!response.ok) {
        throw new Error(`Failed to update tournament status: ${response.statusText}`)
      }

      return await response.json()
    } catch (error) {
      console.error('Error updating tournament status:', error)
      throw error
    }
  }

  // Helper method to convert backend response to frontend Tournament model
  static convertToTournament(response: TournamentResponse): Tournament {
    return {
      id: response.id,
      name: response.name,
      description: response.description,
      sportType: response.sportType as any,
      format: response.format as any,
      tournamentType: response.tournamentType as 'individual' | 'team',
      status: response.status as any,
      hostId: response.hostId,
      hostName: response.hostName,
      location: response.location,
      latitude: response.latitude,
      longitude: response.longitude,
      startDate: response.startDate,
      endDate: response.endDate,
      registrationDeadline: response.registrationDeadline,
      maxParticipants: response.maxParticipants,
      currentParticipants: response.currentParticipants,
      entryFee: response.entryFee,
      prizePool: response.prizePool,
      ageRange: {
        min: response.minAge,
        max: response.maxAge
      },
      rules: response.rules,
      createdAt: response.createdAt,
      updatedAt: response.updatedAt
    }
  }
} 