export interface Tournament {
  id: string
  name: string
  description: string
  sportType: SportType
  format: TournamentFormat
  tournamentType: TournamentType
  status: TournamentStatus
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
  ageRange: {
    min: number
    max: number
  }
  rules: string[]
  createdAt: string
  updatedAt: string
}

export interface Team {
  id: string
  name: string
  description: string
  sportType: SportType
  captainId: string
  captainName: string
  members: TeamMember[]
  maxMembers: number
  isPublic: boolean
  ageRange: {
    min: number
    max: number
  }
  createdAt: string
  updatedAt: string
}

export interface TeamMember {
  userId: string
  name: string
  email: string
  role: TeamRole
  joinedAt: string
}

export interface TournamentParticipant {
  id: string
  tournamentId: string
  participantId: string
  participantName: string
  participantType: 'individual' | 'team'
  teamId?: string
  teamName?: string
  registrationDate: string
  status: ParticipantStatus
}

export interface TournamentMatch {
  id: string
  tournamentId: string
  roundNumber: number
  matchNumber: number
  player1Id: string
  player1Name: string
  player2Id: string
  player2Name: string
  winnerId?: string
  winnerName?: string
  status: MatchStatus
  score?: string
  scheduledTime?: string
  completedAt?: string
}

export interface TournamentBracket {
  id: string
  tournamentId: string
  matches: TournamentMatch[]
  rounds: BracketRound[]
}

export interface BracketRound {
  roundNumber: number
  roundName: string
  matches: TournamentMatch[]
}

export enum SportType {
  FOOTBALL = 'Football',
  BASKETBALL = 'Basketball',
  TENNIS = 'Tennis',
  RUNNING = 'Running',
  CYCLING = 'Cycling',
  VOLLEYBALL = 'Volleyball',
  SWIMMING = 'Swimming'
}

export enum TournamentFormat {
  SINGLE_ELIMINATION = 'Single Elimination',
  DOUBLE_ELIMINATION = 'Double Elimination',
  ROUND_ROBIN = 'Round Robin',
  SWISS_SYSTEM = 'Swiss System'
}

export enum TournamentStatus {
  DRAFT = 'Draft',
  REGISTRATION_OPEN = 'Registration Open',
  REGISTRATION_CLOSED = 'Registration Closed',
  IN_PROGRESS = 'In Progress',
  COMPLETED = 'Completed',
  CANCELLED = 'Cancelled'
}

export enum TeamRole {
  CAPTAIN = 'Captain',
  MEMBER = 'Member',
  INVITED = 'Invited'
}

export enum ParticipantStatus {
  REGISTERED = 'Registered',
  CONFIRMED = 'Confirmed',
  WAITLISTED = 'Waitlisted',
  WITHDRAWN = 'Withdrawn'
}

export enum MatchStatus {
  SCHEDULED = 'Scheduled',
  IN_PROGRESS = 'In Progress',
  COMPLETED = 'Completed',
  CANCELLED = 'Cancelled'
}

export type TournamentType = 'individual' | 'team'

export interface TournamentFilters {
  sportType?: SportType
  format?: TournamentFormat
  status?: TournamentStatus
  location?: string
  dateRange?: {
    start: string
    end: string
  }
  priceRange?: {
    min: number
    max: number
  }
}

export interface TournamentStats {
  totalTournaments: number
  activeTournaments: number
  completedTournaments: number
  totalParticipants: number
  totalPrizeMoney: number
} 