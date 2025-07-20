import { Tournament, TournamentFormat } from '../../models/Tournament'

export interface TournamentRequest {
  name: string
  description: string
  sportType: string
  format: string
  tournamentType: 'individual' | 'team'
  location: string
  latitude: number
  longitude: number
  startDate: number
  endDate: number
  registrationDeadline: number
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
  hostId: string
  hostName: string
  location: string
  latitude: number
  longitude: number
  startDate: number
  endDate: number
  registrationDeadline: number
  maxParticipants: number
  currentParticipants: number
  entryFee: number
  prizePool: number
  minAge: number
  maxAge: number
  rules: string[]
  createdAt: number
  updatedAt: number
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

  static convertToTournament(response: TournamentResponse): Tournament {
    return {
      id: response.id,
      name: response.name,
      description: response.description,
      sportType: response.sportType as any,
      format: TournamentService.mapBackendFormatToFrontend(response.format) as TournamentFormat,
      tournamentType: response.tournamentType as 'individual' | 'team',
      hostId: response.hostId,
      hostName: response.hostName,
      location: response.location,
      latitude: response.latitude,
      longitude: response.longitude,
      startDate: new Date(response.startDate).toISOString(),
      endDate: new Date(response.endDate).toISOString(),
      registrationDeadline: new Date(response.registrationDeadline).toISOString(),
      maxParticipants: response.maxParticipants,
      currentParticipants: response.currentParticipants,
      entryFee: response.entryFee,
      prizePool: response.prizePool,
      ageRange: {
        min: response.minAge,
        max: response.maxAge
      },
      rules: response.rules,
      createdAt: new Date(response.createdAt).toISOString(),
      updatedAt: new Date(response.updatedAt).toISOString()
    }
  }

  static mapBackendFormatToFrontend(backendFormat: string): string {
    const formatMap: { [key: string]: string } = {
      'SINGLE_ELIMINATION': 'Single Elimination',
      'DOUBLE_ELIMINATION': 'Double Elimination',
      'ROUND_ROBIN': 'Round Robin',
      'SWISS_SYSTEM': 'Swiss System'
    }
    return formatMap[backendFormat] || backendFormat
  }
} 