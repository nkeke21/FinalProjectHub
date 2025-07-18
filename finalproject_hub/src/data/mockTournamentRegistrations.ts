import { TournamentRegistration, RegistrationStatus } from '../models/TournamentRegistration'

export const mockTournamentRegistrations: TournamentRegistration[] = [
  {
    id: 'reg-1',
    tournamentId: '1',
    userId: 'user-1',
    status: RegistrationStatus.REGISTERED,
    registeredAt: new Date('2024-12-15T10:00:00Z')
  },
  {
    id: 'reg-2',
    tournamentId: '1',
    userId: 'user-2',
    status: RegistrationStatus.REGISTERED,
    registeredAt: new Date('2024-12-16T14:30:00Z')
  },
  {
    id: 'reg-3',
    tournamentId: '2',
    userId: 'user-3',
    status: RegistrationStatus.REGISTERED,
    registeredAt: new Date('2024-12-17T09:15:00Z')
  }
]

export function getRegistrationsByTournament(tournamentId: string): TournamentRegistration[] {
  return mockTournamentRegistrations.filter(reg => reg.tournamentId === tournamentId)
}

export function isUserRegisteredForTournament(userId: string, tournamentId: string): boolean {
  return mockTournamentRegistrations.some(reg => 
    reg.userId === userId && 
    reg.tournamentId === tournamentId && 
    reg.status === RegistrationStatus.REGISTERED
  )
}

export function getUserRegistrationForTournament(userId: string, tournamentId: string): TournamentRegistration | null {
  return mockTournamentRegistrations.find(reg => 
    reg.userId === userId && 
    reg.tournamentId === tournamentId
  ) || null
} 