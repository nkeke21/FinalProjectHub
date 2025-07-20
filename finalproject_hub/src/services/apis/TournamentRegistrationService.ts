import { TournamentRegistration, RegistrationRequest, RegistrationResponse, RegistrationStatus } from '../../models/TournamentRegistration'

interface ExtendedRegistrationRequest extends RegistrationRequest {
  registrationType?: 'individual' | 'team'
  teamId?: string | null
  participantInfo?: {
    fullName: string
    age: number
    email: string
    phoneNumber: string
    address: string
    emergencyContact: {
      name: string
      relationship: string
      phone: string
      email: string
    }
    previousExperience?: string
    skillLevel?: string
    previousAchievements?: string
  }
}

interface BackendRegistrationResponse {
  id: string
  tournamentId: string
  userId: string
  registrationType: string
  teamId: string | null
  status: string
  registeredAt: number
  updatedAt: number
  fullName: string
  age: number
  email: string
  phoneNumber: string
  address: string
  emergencyContactName: string
  emergencyContactRelationship: string
  emergencyContactPhone: string
  emergencyContactEmail: string
  previousExperience: string
  skillLevel: string
  previousAchievements: string
}

export class TournamentRegistrationService {
  private static readonly BASE_URL = '/api/tournament-registrations'

  static async registerForTournament(request: ExtendedRegistrationRequest): Promise<RegistrationResponse> {
    try {
      const response = await fetch(this.BASE_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          tournamentId: request.tournamentId,
          registrationType: request.registrationType || 'individual',
          teamId: request.teamId,
          participantInfo: request.participantInfo
        }),
        credentials: 'include',
      })

      if (!response.ok) {
        const errorText = await response.text()
        throw new Error(`Failed to register for tournament: ${errorText}`)
      }

      const backendResponse: BackendRegistrationResponse = await response.json()
      
      const registration: TournamentRegistration = {
        id: backendResponse.id,
        tournamentId: backendResponse.tournamentId,
        userId: backendResponse.userId,
        status: backendResponse.status as RegistrationStatus,
        registeredAt: new Date(backendResponse.registeredAt)
      }

      return {
        success: true,
        registration,
        message: 'Successfully registered for tournament'
      }

    } catch (error) {
      console.error('Error registering for tournament:', error)
      return {
        success: false,
        message: error instanceof Error ? error.message : 'Failed to register for tournament'
      }
    }
  }

  static async canRegisterForTournament(tournamentId: string): Promise<boolean> {
    try {
      const response = await fetch(`${this.BASE_URL}/tournament/${tournamentId}/can-register`, {
        method: 'GET',
        credentials: 'include',
      })

      if (!response.ok) {
        return false
      }

      return await response.json()
    } catch (error) {
      console.error('Error checking registration eligibility:', error)
      return false
    }
  }

  static async withdrawFromTournament(tournamentId: string): Promise<RegistrationResponse> {
    try {
      const response = await fetch(`${this.BASE_URL}/${tournamentId}/withdraw`, {
        method: 'POST',
        credentials: 'include',
      })

      if (!response.ok) {
        const errorText = await response.text()
        throw new Error(`Failed to withdraw from tournament: ${errorText}`)
      }

      const backendResponse: BackendRegistrationResponse = await response.json()
      
      const registration: TournamentRegistration = {
        id: backendResponse.id,
        tournamentId: backendResponse.tournamentId,
        userId: backendResponse.userId,
        status: backendResponse.status as RegistrationStatus,
        registeredAt: new Date(backendResponse.registeredAt)
      }

      return {
        success: true,
        registration,
        message: 'Successfully withdrawn from tournament'
      }

    } catch (error) {
      console.error('Error withdrawing from tournament:', error)
      return {
        success: false,
        message: error instanceof Error ? error.message : 'Failed to withdraw from tournament'
      }
    }
  }

  static async getUserRegistration(tournamentId: string): Promise<TournamentRegistration | null> {
    try {
      const response = await fetch(`${this.BASE_URL}/tournament/${tournamentId}`, {
        method: 'GET',
        credentials: 'include',
      })

      if (!response.ok) {
        if (response.status === 404) {
          return null
        }
        throw new Error(`Failed to get user registration: ${response.statusText}`)
      }

      const backendResponse: BackendRegistrationResponse = await response.json()
      
      return {
        id: backendResponse.id,
        tournamentId: backendResponse.tournamentId,
        userId: backendResponse.userId,
        status: backendResponse.status as RegistrationStatus,
        registeredAt: new Date(backendResponse.registeredAt)
      }
    } catch (error) {
      console.error('Error getting user registration:', error)
      return null
    }
  }

  static async approveRegistration(registrationId: string): Promise<RegistrationResponse> {
    try {
      const response = await fetch(`${this.BASE_URL}/${registrationId}/approve`, {
        method: 'PUT',
        credentials: 'include',
      })

      if (!response.ok) {
        const errorText = await response.text()
        throw new Error(`Failed to approve registration: ${errorText}`)
      }

      const backendResponse: BackendRegistrationResponse = await response.json()
      
      const registration: TournamentRegistration = {
        id: backendResponse.id,
        tournamentId: backendResponse.tournamentId,
        userId: backendResponse.userId,
        status: backendResponse.status as RegistrationStatus,
        registeredAt: new Date(backendResponse.registeredAt)
      }

      return {
        success: true,
        registration,
        message: 'Registration approved successfully'
      }

    } catch (error) {
      console.error('Error approving registration:', error)
      return {
        success: false,
        message: error instanceof Error ? error.message : 'Failed to approve registration'
      }
    }
  }

  static async rejectRegistration(registrationId: string): Promise<RegistrationResponse> {
    try {
      const response = await fetch(`${this.BASE_URL}/${registrationId}/reject`, {
        method: 'PUT',
        credentials: 'include',
      })

      if (!response.ok) {
        const errorText = await response.text()
        throw new Error(`Failed to reject registration: ${errorText}`)
      }

      const backendResponse: BackendRegistrationResponse = await response.json()
      
      const registration: TournamentRegistration = {
        id: backendResponse.id,
        tournamentId: backendResponse.tournamentId,
        userId: backendResponse.userId,
        status: backendResponse.status as RegistrationStatus,
        registeredAt: new Date(backendResponse.registeredAt)
      }

      return {
        success: true,
        registration,
        message: 'Registration rejected successfully'
      }

    } catch (error) {
      console.error('Error rejecting registration:', error)
      return {
        success: false,
        message: error instanceof Error ? error.message : 'Failed to reject registration'
      }
    }
  }
} 