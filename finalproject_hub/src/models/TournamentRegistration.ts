export enum RegistrationStatus {
  PENDING = 'PENDING',
  REGISTERED = 'REGISTERED',
  WITHDRAWN = 'WITHDRAWN'
}

export interface TournamentRegistration {
  id: string
  tournamentId: string
  userId: string
  status: RegistrationStatus
  registeredAt: Date
}

export interface RegistrationRequest {
  tournamentId: string
}

export interface RegistrationResponse {
  success: boolean
  message: string
  registration?: TournamentRegistration
}

export interface TournamentRegistrationSettings {
  tournamentId: string
  maxParticipants: number
  currentParticipants: number
  minTeamSize?: number
  maxTeamSize?: number
  registrationDeadline: Date
  isRegistrationOpen: boolean
  allowWaitlist: boolean
  waitlistCount: number
} 