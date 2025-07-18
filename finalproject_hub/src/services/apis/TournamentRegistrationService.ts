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
    medicalConditions?: string
    bloodType?: string
    previousExperience?: string
    skillLevel?: string
    previousAchievements?: string
    tshirtSize?: string
    dietaryRestrictions?: string
    specialRequirements?: string
  }
}
import { mockTournamentRegistrations, isUserRegisteredForTournament, getUserRegistrationForTournament } from '../../data/mockTournamentRegistrations'
import { mockTournaments } from '../../data/mockTournaments'

export class TournamentRegistrationService {
  static async registerForTournament(request: ExtendedRegistrationRequest): Promise<RegistrationResponse> {
    try {
      await new Promise(resolve => setTimeout(resolve, 500))

    const existingRegistration = getUserRegistrationForTournament('current-user-id', request.tournamentId)
    if (existingRegistration && existingRegistration.status === 'REGISTERED') {
      return {
        success: false,
        message: 'Already registered for this tournament'
      }
    }

      const tournament = mockTournaments.find(t => t.id === request.tournamentId)
      if (!tournament) {
        return {
          success: false,
          message: 'Tournament not found'
        }
      }

      let newRegistration: TournamentRegistration

      if (existingRegistration && existingRegistration.status === 'WITHDRAWN') {
        existingRegistration.status = RegistrationStatus.REGISTERED
        existingRegistration.registeredAt = new Date()
        newRegistration = existingRegistration
      } else {
        newRegistration = {
          id: `reg-${Date.now()}`,
          tournamentId: request.tournamentId,
          userId: 'current-user-id',
          status: RegistrationStatus.REGISTERED,
          registeredAt: new Date()
        }
        mockTournamentRegistrations.push(newRegistration)
      }

      return {
        success: true,
        registration: newRegistration,
        message: 'Successfully registered for tournament'
      }

    } catch (error) {
      console.error('Error registering for tournament:', error)
      return {
        success: false,
        message: 'Failed to register for tournament'
      }
    }
  }

  static async canRegisterForTournament(tournamentId: string): Promise<boolean> {
    try {
      await new Promise(resolve => setTimeout(resolve, 200))
      
      const registration = getUserRegistrationForTournament('current-user-id', tournamentId)
      return !registration || registration.status === 'WITHDRAWN'
    } catch (error) {
      console.error('Error checking registration eligibility:', error)
      return false
    }
  }

  static async withdrawFromTournament(tournamentId: string): Promise<RegistrationResponse> {
    try {
      await new Promise(resolve => setTimeout(resolve, 300))

      const registration = getUserRegistrationForTournament('current-user-id', tournamentId)
      if (!registration) {
        return {
          success: false,
          message: 'Not registered for this tournament'
        }
      }

      registration.status = RegistrationStatus.WITHDRAWN

      return {
        success: true,
        registration,
        message: 'Successfully withdrawn from tournament'
      }

    } catch (error) {
      console.error('Error withdrawing from tournament:', error)
      return {
        success: false,
        message: 'Failed to withdraw from tournament'
      }
    }
  }

  static async getUserRegistration(tournamentId: string): Promise<TournamentRegistration | null> {
    try {
      await new Promise(resolve => setTimeout(resolve, 200))
      return getUserRegistrationForTournament('current-user-id', tournamentId)
    } catch (error) {
      console.error('Error getting user registration:', error)
      return null
    }
  }
} 