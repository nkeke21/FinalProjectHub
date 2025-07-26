import { TournamentRegistration, RegistrationRequest, RegistrationResponse, RegistrationStatus } from '../../models/TournamentRegistration'
import { getAuthHeaders } from '../../utils/auth'
import { API_BASE_URL } from '../../constants/apis'

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
  registrationType: 'individual' | 'team'
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
  private static readonly BASE_URL = `${API_BASE_URL}/api/tournament-registrations`

  static async registerForTournament(request: ExtendedRegistrationRequest): Promise<RegistrationResponse> {
    try {
      const response = await fetch(this.BASE_URL, {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify({
          tournamentId: request.tournamentId,
          registrationType: request.registrationType || 'individual',
          teamId: request.teamId,
          participantInfo: request.participantInfo
        }),
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
        registrationType: backendResponse.registrationType,
        teamId: backendResponse.teamId,
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
        headers: getAuthHeaders(),
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
        headers: getAuthHeaders(),
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
        registrationType: backendResponse.registrationType,
        teamId: backendResponse.teamId,
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
        headers: getAuthHeaders(),
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
        registrationType: backendResponse.registrationType,
        teamId: backendResponse.teamId,
        status: backendResponse.status as RegistrationStatus,
        registeredAt: new Date(backendResponse.registeredAt),
        fullName: backendResponse.fullName,
        age: backendResponse.age,
        email: backendResponse.email,
        phoneNumber: backendResponse.phoneNumber,
        address: backendResponse.address,
        emergencyContactName: backendResponse.emergencyContactName,
        emergencyContactRelationship: backendResponse.emergencyContactRelationship,
        emergencyContactPhone: backendResponse.emergencyContactPhone,
        emergencyContactEmail: backendResponse.emergencyContactEmail,
        previousExperience: backendResponse.previousExperience,
        skillLevel: backendResponse.skillLevel,
        previousAchievements: backendResponse.previousAchievements
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
        headers: getAuthHeaders(),
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
        registrationType: backendResponse.registrationType,
        teamId: backendResponse.teamId,
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
        headers: getAuthHeaders(),
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
        registrationType: backendResponse.registrationType,
        teamId: backendResponse.teamId,
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

  static async getTournamentRegistrations(tournamentId: string): Promise<TournamentRegistration[]> {
    try {
      const response = await fetch(`${this.BASE_URL}/tournament/${tournamentId}/all`, {
        method: 'GET',
        headers: getAuthHeaders(),
      })

      if (!response.ok) {
        throw new Error(`Failed to get tournament registrations: ${response.statusText}`)
      }

      const backendResponses: BackendRegistrationResponse[] = await response.json()
      
      return backendResponses.map(response => ({
        id: response.id,
        tournamentId: response.tournamentId,
        userId: response.userId,
        registrationType: response.registrationType,
        teamId: response.teamId,
        status: response.status as RegistrationStatus,
        registeredAt: new Date(response.registeredAt),
        fullName: response.fullName,
        age: response.age,
        email: response.email,
        phoneNumber: response.phoneNumber,
        address: response.address,
        emergencyContactName: response.emergencyContactName,
        emergencyContactRelationship: response.emergencyContactRelationship,
        emergencyContactPhone: response.emergencyContactPhone,
        emergencyContactEmail: response.emergencyContactEmail,
        previousExperience: response.previousExperience,
        skillLevel: response.skillLevel,
        previousAchievements: response.previousAchievements
      }))
    } catch (error) {
      console.error('Error getting tournament registrations:', error)
      return []
    }
  }

  static async getRegistrationById(registrationId: string): Promise<TournamentRegistration | null> {
    try {
      const response = await fetch(`${this.BASE_URL}/${registrationId}`, {
        method: 'GET',
        headers: getAuthHeaders(),
      })

      if (!response.ok) {
        if (response.status === 404) {
          return null
        }
        throw new Error(`Failed to get registration: ${response.statusText}`)
      }

      const backendResponse: BackendRegistrationResponse = await response.json()
      
      const registration = {
        id: backendResponse.id,
        tournamentId: backendResponse.tournamentId,
        userId: backendResponse.userId,
        registrationType: backendResponse.registrationType,
        teamId: backendResponse.teamId,
        status: backendResponse.status as RegistrationStatus,
        registeredAt: new Date(backendResponse.registeredAt),
        fullName: backendResponse.fullName,
        age: backendResponse.age,
        email: backendResponse.email,
        phoneNumber: backendResponse.phoneNumber,
        address: backendResponse.address,
        emergencyContactName: backendResponse.emergencyContactName,
        emergencyContactRelationship: backendResponse.emergencyContactRelationship,
        emergencyContactPhone: backendResponse.emergencyContactPhone,
        emergencyContactEmail: backendResponse.emergencyContactEmail,
        previousExperience: backendResponse.previousExperience,
        skillLevel: backendResponse.skillLevel,
        previousAchievements: backendResponse.previousAchievements
      }
      return registration
    } catch (error) {
      console.error('Error getting registration:', error)
      return null
    }
  }
} 