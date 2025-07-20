import { TournamentRegistration } from '../../models/TournamentRegistration'
import { TournamentParticipant, ParticipantStatus } from '../../models/Tournament'
import { UserTeamService } from './UserTeamService'

export class TournamentParticipantService {
  static async convertRegistrationsToParticipants(registrations: TournamentRegistration[]): Promise<TournamentParticipant[]> {
    const participants: TournamentParticipant[] = []
    
    console.log('Converting registrations to participants:', registrations)
    
    for (const registration of registrations) {
      console.log('Processing registration:', {
        id: registration.id,
        status: registration.status,
        registrationType: registration.registrationType,
        teamId: registration.teamId,
        fullName: registration.fullName
      })
      
      if (registration.status === 'REGISTERED') {
        let participantName = registration.fullName || 'Unknown Participant'
        let participantType: 'individual' | 'team' = 'individual'
        let teamId: string | undefined = undefined
        let teamName: string | undefined = undefined
        let captainName: string | undefined = undefined
        
        if (registration.registrationType === 'team' && registration.teamId) {
          console.log('Team registration detected, loading team details for teamId:', registration.teamId)
          try {
            const team = await UserTeamService.getTeamById(registration.teamId)
            console.log('Loaded team details:', team)
            if (team) {
              participantName = team.name
              participantType = 'team'
              teamId = team.id
              teamName = team.name
              captainName = team.captainName
            } else {
              console.warn('Team not found for teamId:', registration.teamId)
              participantType = 'team'
              teamId = registration.teamId
              participantName = `Team (${registration.teamId})`
            }
          } catch (error) {
            console.error('Error loading team details for registration:', registration.id, error)
            participantType = 'team'
            teamId = registration.teamId
            participantName = `Team (${registration.teamId})`
          }
        } else {
          console.log('Not a team registration or missing teamId:', {
            registrationType: registration.registrationType,
            teamId: registration.teamId
          })
        }
        
        const participant = {
          id: registration.id,
          tournamentId: registration.tournamentId,
          participantId: registration.userId,
          participantName,
          participantType,
          teamId,
          teamName,
          captainName,
          registrationDate: registration.registeredAt.toISOString(),
          status: this.mapRegistrationStatusToParticipantStatus(registration.status)
        }
        
        console.log('Created participant:', participant)
        participants.push(participant)
      }
    }
    
    console.log('Final participants list:', participants)
    return participants
  }

  private static mapRegistrationStatusToParticipantStatus(registrationStatus: string): ParticipantStatus {
    switch (registrationStatus) {
      case 'REGISTERED':
        return ParticipantStatus.CONFIRMED
      case 'PENDING':
        return ParticipantStatus.REGISTERED
      case 'WITHDRAWN':
        return ParticipantStatus.WITHDRAWN
      default:
        return ParticipantStatus.REGISTERED
    }
  }
} 