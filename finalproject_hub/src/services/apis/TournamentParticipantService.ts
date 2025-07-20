import { TournamentRegistration } from '../../models/TournamentRegistration'
import { TournamentParticipant, ParticipantStatus } from '../../models/Tournament'

export class TournamentParticipantService {
  static convertRegistrationsToParticipants(registrations: TournamentRegistration[]): TournamentParticipant[] {
    return registrations
      .filter(registration => registration.status === 'REGISTERED')
      .map(registration => ({
        id: registration.id,
        tournamentId: registration.tournamentId,
        participantId: registration.userId,
        participantName: registration.fullName || 'Unknown Participant',
        participantType: 'individual' as const,
        registrationDate: registration.registeredAt.toISOString(),
        status: this.mapRegistrationStatusToParticipantStatus(registration.status)
      }))
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