import { 
  Tournament, 
  Team, 
  TournamentParticipant, 
  TournamentMatch, 
  TournamentBracket,
  SportType, 
  TournamentFormat, 
  TournamentStatus, 
  TeamRole, 
  ParticipantStatus, 
  MatchStatus 
} from '@/models/Tournament'

// Mock Tournaments
export const mockTournaments: Tournament[] = [
  {
    id: '1',
    name: 'Tbilisi Football Championship 2025',
    description: 'Join the ultimate football competition in Tbilisi! This championship brings together the best teams from across the city for an exciting tournament with great prizes.',
    sportType: SportType.FOOTBALL,
    format: TournamentFormat.SINGLE_ELIMINATION,
    status: TournamentStatus.REGISTRATION_OPEN,
    hostId: 'user-1',
    hostName: 'John Doe',
    location: 'Vake Park, Tbilisi',
    latitude: 41.725788,
    longitude: 44.727753,
    startDate: '2025-03-15T09:00:00Z',
    endDate: '2025-03-20T18:00:00Z',
    registrationDeadline: '2025-03-10T23:59:59Z',
    maxParticipants: 8,
    currentParticipants: 6,
    entryFee: 50,
    prizePool: 2000,
    ageRange: { min: 18, max: 35 },
    rules: [
      'Teams must have exactly 11 players',
      'Standard FIFA rules apply',
      'Matches are 90 minutes with extra time if needed',
      'Substitutions allowed up to 3 times per match'
    ],
    createdAt: '2025-01-15T10:00:00Z',
    updatedAt: '2025-02-20T14:30:00Z'
  },
  {
    id: '2',
    name: 'Tbilisi Tennis Open 2025',
    description: 'Individual tennis championship for players of all skill levels. Compete in a single elimination bracket for cash prizes and recognition.',
    sportType: SportType.TENNIS,
    format: TournamentFormat.SINGLE_ELIMINATION,
    status: TournamentStatus.REGISTRATION_OPEN,
    hostId: 'user-2',
    hostName: 'Jane Smith',
    location: 'Tennis Club, Tbilisi',
    latitude: 41.715788,
    longitude: 44.717753,
    startDate: '2025-04-10T08:00:00Z',
    endDate: '2025-04-15T20:00:00Z',
    registrationDeadline: '2025-04-05T23:59:59Z',
    maxParticipants: 32,
    currentParticipants: 28,
    entryFee: 20,
    prizePool: 500,
    ageRange: { min: 16, max: 50 },
    rules: [
      'Best of 3 sets',
      'Tie-break at 6-6',
      'Standard tennis rules apply',
      'Players must provide their own rackets'
    ],
    createdAt: '2025-01-20T11:00:00Z',
    updatedAt: '2025-02-25T16:45:00Z'
  },
  {
    id: '3',
    name: 'Basketball League Spring 2025',
    description: 'Team basketball tournament featuring the best local teams. Double elimination format ensures every team gets multiple chances to compete.',
    sportType: SportType.BASKETBALL,
    format: TournamentFormat.DOUBLE_ELIMINATION,
    status: TournamentStatus.IN_PROGRESS,
    hostId: 'user-3',
    hostName: 'Mike Johnson',
    location: 'Saburtalo Arena',
    latitude: 41.735788,
    longitude: 44.737753,
    startDate: '2025-03-01T10:00:00Z',
    endDate: '2025-03-30T22:00:00Z',
    registrationDeadline: '2025-02-25T23:59:59Z',
    maxParticipants: 8,
    currentParticipants: 4,
    entryFee: 30,
    prizePool: 1500,
    ageRange: { min: 20, max: 40 },
    rules: [
      'Teams must have exactly 5 players',
      'Standard basketball rules apply',
      'Matches are 4 quarters of 10 minutes each',
      'Overtime periods of 5 minutes if tied'
    ],
    createdAt: '2025-01-10T09:00:00Z',
    updatedAt: '2025-03-01T10:00:00Z'
  },
  {
    id: '4',
    name: 'Tbilisi Running Marathon 2025',
    description: 'Annual city marathon with multiple distance categories. Open to runners of all levels with beautiful routes through Tbilisi.',
    sportType: SportType.RUNNING,
    format: TournamentFormat.ROUND_ROBIN,
    status: TournamentStatus.REGISTRATION_OPEN,
    hostId: 'user-4',
    hostName: 'Sarah Wilson',
    location: 'Freedom Square, Tbilisi',
    latitude: 41.725788,
    longitude: 44.727753,
    startDate: '2025-05-15T07:00:00Z',
    endDate: '2025-05-15T14:00:00Z',
    registrationDeadline: '2025-05-10T23:59:59Z',
    maxParticipants: 100,
    currentParticipants: 75,
    entryFee: 15,
    prizePool: 800,
    ageRange: { min: 18, max: 65 },
    rules: [
      'Multiple distance categories available',
      'Timing chips provided',
      'Water stations every 5km',
      'Medical support available throughout'
    ],
    createdAt: '2025-01-25T12:00:00Z',
    updatedAt: '2025-02-28T15:20:00Z'
  },
  {
    id: '5',
    name: 'Volleyball Beach Championship 2025',
    description: 'Beach volleyball tournament at Turtle Lake. Teams of 2 players compete in a Swiss system format.',
    sportType: SportType.VOLLEYBALL,
    format: TournamentFormat.SWISS_SYSTEM,
    status: TournamentStatus.DRAFT,
    hostId: 'user-5',
    hostName: 'Alex Brown',
    location: 'Turtle Lake, Tbilisi',
    latitude: 41.745788,
    longitude: 44.747753,
    startDate: '2025-06-20T09:00:00Z',
    endDate: '2025-06-22T18:00:00Z',
    registrationDeadline: '2025-06-15T23:59:59Z',
    maxParticipants: 16,
    currentParticipants: 0,
    entryFee: 25,
    prizePool: 600,
    ageRange: { min: 18, max: 45 },
    rules: [
      'Teams of exactly 2 players',
      'Best of 3 sets to 21 points',
      'Swiss system pairing',
      'Beach volleyball rules apply'
    ],
    createdAt: '2025-02-01T10:00:00Z',
    updatedAt: '2025-02-01T10:00:00Z'
  }
]

export const mockTeams: Team[] = [
  {
    id: 'team-1',
    name: 'FC Thunderbolts',
    description: 'Competitive football team from Tbilisi with a focus on attacking play and team chemistry.',
    sportType: SportType.FOOTBALL,
    captainId: 'user-1',
    captainName: 'John Doe',
    members: [
      { userId: 'user-1', name: 'John Doe', email: 'john@example.com', role: TeamRole.CAPTAIN, joinedAt: '2025-03-01T10:00:00Z' },
      { userId: 'user-6', name: 'Mike Johnson', email: 'mike@example.com', role: TeamRole.MEMBER, joinedAt: '2025-03-02T11:00:00Z' },
      { userId: 'user-7', name: 'Alex Smith', email: 'alex@example.com', role: TeamRole.MEMBER, joinedAt: '2025-03-03T12:00:00Z' },
      { userId: 'user-8', name: 'Chris Brown', email: 'chris@example.com', role: TeamRole.MEMBER, joinedAt: '2025-03-04T13:00:00Z' },
      { userId: 'user-9', name: 'Steve Miller', email: 'steve@example.com', role: TeamRole.MEMBER, joinedAt: '2025-03-05T14:00:00Z' },
      { userId: 'user-10', name: 'Paul Taylor', email: 'paul@example.com', role: TeamRole.MEMBER, joinedAt: '2025-03-06T15:00:00Z' },
      { userId: 'user-11', name: 'David Wilson', email: 'david@example.com', role: TeamRole.MEMBER, joinedAt: '2025-03-07T16:00:00Z' },
      { userId: 'user-12', name: 'Tom Davis', email: 'tom@example.com', role: TeamRole.MEMBER, joinedAt: '2025-03-08T17:00:00Z' }
    ],
    maxMembers: 11,
    isPublic: true,
    location: 'Tbilisi, Georgia',
    ageRange: { min: 18, max: 35 },
    createdAt: '2025-03-01T10:00:00Z',
    updatedAt: '2025-03-08T17:00:00Z'
  },
  {
    id: 'team-2',
    name: 'Red Dragons',
    description: 'Basketball team known for fast-paced offense and strong defense.',
    sportType: SportType.BASKETBALL,
    captainId: 'user-3',
    captainName: 'Mike Johnson',
    members: [
      { userId: 'user-3', name: 'Mike Johnson', email: 'mike@example.com', role: TeamRole.CAPTAIN, joinedAt: '2025-02-15T10:00:00Z' },
      { userId: 'user-13', name: 'Sarah Wilson', email: 'sarah@example.com', role: TeamRole.MEMBER, joinedAt: '2025-02-16T11:00:00Z' },
      { userId: 'user-14', name: 'Emma Davis', email: 'emma@example.com', role: TeamRole.MEMBER, joinedAt: '2025-02-17T12:00:00Z' },
      { userId: 'user-15', name: 'James Wilson', email: 'james@example.com', role: TeamRole.MEMBER, joinedAt: '2025-02-18T13:00:00Z' },
      { userId: 'user-16', name: 'Lisa Brown', email: 'lisa@example.com', role: TeamRole.MEMBER, joinedAt: '2025-02-19T14:00:00Z' }
    ],
    maxMembers: 5,
    isPublic: true,
    location: 'Tbilisi, Georgia',
    ageRange: { min: 20, max: 40 },
    createdAt: '2025-02-15T10:00:00Z',
    updatedAt: '2025-02-19T14:00:00Z'
  },
  {
    id: 'team-3',
    name: 'Blue Eagles',
    description: 'Rising football stars with a focus on youth development and skill building.',
    sportType: SportType.FOOTBALL,
    captainId: 'user-17',
    captainName: 'Steve Miller',
    members: [
      { userId: 'user-17', name: 'Steve Miller', email: 'steve@example.com', role: TeamRole.CAPTAIN, joinedAt: '2025-02-20T10:00:00Z' },
      { userId: 'user-18', name: 'Paul Taylor', email: 'paul@example.com', role: TeamRole.MEMBER, joinedAt: '2025-02-21T11:00:00Z' },
      { userId: 'user-19', name: 'David Wilson', email: 'david@example.com', role: TeamRole.MEMBER, joinedAt: '2025-02-22T12:00:00Z' },
      { userId: 'user-20', name: 'Tom Davis', email: 'tom@example.com', role: TeamRole.MEMBER, joinedAt: '2025-02-23T13:00:00Z' },
      { userId: 'user-21', name: 'Mark Johnson', email: 'mark@example.com', role: TeamRole.MEMBER, joinedAt: '2025-02-24T14:00:00Z' },
      { userId: 'user-22', name: 'Ryan Smith', email: 'ryan@example.com', role: TeamRole.MEMBER, joinedAt: '2025-02-25T15:00:00Z' },
      { userId: 'user-23', name: 'Kevin Brown', email: 'kevin@example.com', role: TeamRole.MEMBER, joinedAt: '2025-02-26T16:00:00Z' },
      { userId: 'user-24', name: 'Brian Wilson', email: 'brian@example.com', role: TeamRole.MEMBER, joinedAt: '2025-02-27T17:00:00Z' },
      { userId: 'user-25', name: 'Jason Davis', email: 'jason@example.com', role: TeamRole.MEMBER, joinedAt: '2025-02-28T18:00:00Z' }
    ],
    maxMembers: 11,
    isPublic: true,
    location: 'Tbilisi, Georgia',
    ageRange: { min: 18, max: 35 },
    createdAt: '2025-02-20T10:00:00Z',
    updatedAt: '2025-02-28T18:00:00Z'
  }
]

export const mockTournamentParticipants: TournamentParticipant[] = [
  { id: '1', tournamentId: '1', participantId: 'team-1', participantName: 'FC Thunderbolts', participantType: 'team', teamId: 'team-1', teamName: 'FC Thunderbolts', registrationDate: '2025-02-15T10:00:00Z', status: ParticipantStatus.CONFIRMED },
  { id: '2', tournamentId: '1', participantId: 'team-3', participantName: 'Blue Eagles', participantType: 'team', teamId: 'team-3', teamName: 'Blue Eagles', registrationDate: '2025-02-16T11:00:00Z', status: ParticipantStatus.CONFIRMED },
  { id: '3', tournamentId: '1', participantId: 'team-4', participantName: 'Green Lions', participantType: 'team', teamId: 'team-4', teamName: 'Green Lions', registrationDate: '2025-02-17T12:00:00Z', status: ParticipantStatus.CONFIRMED },
  { id: '4', tournamentId: '1', participantId: 'team-5', participantName: 'Yellow Tigers', participantType: 'team', teamId: 'team-5', teamName: 'Yellow Tigers', registrationDate: '2025-02-18T13:00:00Z', status: ParticipantStatus.CONFIRMED },
  { id: '5', tournamentId: '1', participantId: 'team-6', participantName: 'Purple Panthers', participantType: 'team', teamId: 'team-6', teamName: 'Purple Panthers', registrationDate: '2025-02-19T14:00:00Z', status: ParticipantStatus.CONFIRMED },
  { id: '6', tournamentId: '1', participantId: 'team-7', participantName: 'Orange Bears', participantType: 'team', teamId: 'team-7', teamName: 'Orange Bears', registrationDate: '2025-02-20T15:00:00Z', status: ParticipantStatus.CONFIRMED },

  { id: '7', tournamentId: '2', participantId: 'user-30', participantName: 'Anna Johnson', participantType: 'individual', registrationDate: '2025-03-01T10:00:00Z', status: ParticipantStatus.CONFIRMED },
  { id: '8', tournamentId: '2', participantId: 'user-31', participantName: 'Maria Garcia', participantType: 'individual', registrationDate: '2025-03-01T11:00:00Z', status: ParticipantStatus.CONFIRMED },
  { id: '9', tournamentId: '2', participantId: 'user-32', participantName: 'Carlos Rodriguez', participantType: 'individual', registrationDate: '2025-03-02T10:00:00Z', status: ParticipantStatus.CONFIRMED },
  { id: '10', tournamentId: '2', participantId: 'user-33', participantName: 'Sophie Martin', participantType: 'individual', registrationDate: '2025-03-02T11:00:00Z', status: ParticipantStatus.CONFIRMED },

  { id: '11', tournamentId: '3', participantId: 'team-2', participantName: 'Red Dragons', participantType: 'team', teamId: 'team-2', teamName: 'Red Dragons', registrationDate: '2025-02-10T10:00:00Z', status: ParticipantStatus.CONFIRMED },
  { id: '12', tournamentId: '3', participantId: 'team-8', participantName: 'Black Knights', participantType: 'team', teamId: 'team-8', teamName: 'Black Knights', registrationDate: '2025-02-11T11:00:00Z', status: ParticipantStatus.CONFIRMED },
  { id: '13', tournamentId: '3', participantId: 'team-9', participantName: 'White Wolves', participantType: 'team', teamId: 'team-9', teamName: 'White Wolves', registrationDate: '2025-02-12T12:00:00Z', status: ParticipantStatus.CONFIRMED },
  { id: '14', tournamentId: '3', participantId: 'team-10', participantName: 'Silver Sharks', participantType: 'team', teamId: 'team-10', teamName: 'Silver Sharks', registrationDate: '2025-02-13T13:00:00Z', status: ParticipantStatus.CONFIRMED }
]

export const mockTournamentMatches: TournamentMatch[] = [
  { id: '1', tournamentId: '1', roundNumber: 1, matchNumber: 1, player1Id: 'team-1', player1Name: 'FC Thunderbolts', player2Id: 'team-3', player2Name: 'Blue Eagles', winnerId: 'team-1', winnerName: 'FC Thunderbolts', status: MatchStatus.COMPLETED, score: '3-1', scheduledTime: '2025-03-15T10:00:00Z', completedAt: '2025-03-15T11:45:00Z' },
  { id: '2', tournamentId: '1', roundNumber: 1, matchNumber: 2, player1Id: 'team-4', player1Name: 'Green Lions', player2Id: 'team-5', player2Name: 'Yellow Tigers', winnerId: 'team-4', winnerName: 'Green Lions', status: MatchStatus.COMPLETED, score: '2-0', scheduledTime: '2025-03-15T14:00:00Z', completedAt: '2025-03-15T15:30:00Z' },
  { id: '3', tournamentId: '1', roundNumber: 1, matchNumber: 3, player1Id: 'team-6', player1Name: 'Purple Panthers', player2Id: 'team-7', player2Name: 'Orange Bears', winnerId: 'team-6', winnerName: 'Purple Panthers', status: MatchStatus.COMPLETED, score: '4-2', scheduledTime: '2025-03-16T10:00:00Z', completedAt: '2025-03-16T11:30:00Z' },
  { id: '4', tournamentId: '1', roundNumber: 1, matchNumber: 4, player1Id: 'team-2', player1Name: 'Red Dragons', player2Id: 'team-8', player2Name: 'Black Knights', winnerId: 'team-2', winnerName: 'Red Dragons', status: MatchStatus.COMPLETED, score: '1-0', scheduledTime: '2025-03-16T14:00:00Z', completedAt: '2025-03-16T15:15:00Z' },

  { id: '5', tournamentId: '1', roundNumber: 2, matchNumber: 1, player1Id: 'team-1', player1Name: 'FC Thunderbolts', player2Id: 'team-4', player2Name: 'Green Lions', winnerId: 'team-1', winnerName: 'FC Thunderbolts', status: MatchStatus.COMPLETED, score: '2-1', scheduledTime: '2025-03-18T10:00:00Z', completedAt: '2025-03-18T11:30:00Z' },
  { id: '6', tournamentId: '1', roundNumber: 2, matchNumber: 2, player1Id: 'team-6', player1Name: 'Purple Panthers', player2Id: 'team-2', player2Name: 'Red Dragons', winnerId: 'team-6', winnerName: 'Purple Panthers', status: MatchStatus.COMPLETED, score: '3-2', scheduledTime: '2025-03-18T14:00:00Z', completedAt: '2025-03-18T15:45:00Z' },

  { id: '7', tournamentId: '1', roundNumber: 3, matchNumber: 1, player1Id: 'team-1', player1Name: 'FC Thunderbolts', player2Id: 'team-6', player2Name: 'Purple Panthers', status: MatchStatus.SCHEDULED, scheduledTime: '2025-03-20T15:00:00Z' },

  { id: '8', tournamentId: '2', roundNumber: 1, matchNumber: 1, player1Id: 'user-30', player1Name: 'Anna Johnson', player2Id: 'user-31', player2Name: 'Maria Garcia', winnerId: 'user-30', winnerName: 'Anna Johnson', status: MatchStatus.COMPLETED, score: '6-4, 6-2', scheduledTime: '2025-04-10T09:00:00Z', completedAt: '2025-04-10T10:15:00Z' },
  { id: '9', tournamentId: '2', roundNumber: 1, matchNumber: 2, player1Id: 'user-32', player1Name: 'Carlos Rodriguez', player2Id: 'user-33', player2Name: 'Sophie Martin', winnerId: 'user-33', winnerName: 'Sophie Martin', status: MatchStatus.COMPLETED, score: '7-5, 6-3', scheduledTime: '2025-04-10T11:00:00Z', completedAt: '2025-04-10T12:30:00Z' }
]

export const mockTournamentBrackets: TournamentBracket[] = [
  {
    id: 'bracket-1',
    tournamentId: '1',
    matches: mockTournamentMatches.filter(match => match.tournamentId === '1'),
    rounds: [
      {
        roundNumber: 1,
        roundName: 'Quarterfinals',
        matches: mockTournamentMatches.filter(match => match.tournamentId === '1' && match.roundNumber === 1)
      },
      {
        roundNumber: 2,
        roundName: 'Semifinals',
        matches: mockTournamentMatches.filter(match => match.tournamentId === '1' && match.roundNumber === 2)
      },
      {
        roundNumber: 3,
        roundName: 'Final',
        matches: mockTournamentMatches.filter(match => match.tournamentId === '1' && match.roundNumber === 3)
      }
    ]
  },
  {
    id: 'bracket-2',
    tournamentId: '2',
    matches: mockTournamentMatches.filter(match => match.tournamentId === '2'),
    rounds: [
      {
        roundNumber: 1,
        roundName: 'Round 1',
        matches: mockTournamentMatches.filter(match => match.tournamentId === '2' && match.roundNumber === 1)
      }
    ]
  }
]

export const getTournamentById = (id: string): Tournament | undefined => {
  return mockTournaments.find(tournament => tournament.id === id)
}

export const getTeamById = (id: string): Team | undefined => {
  return mockTeams.find(team => team.id === id)
}

export const getTournamentParticipants = (tournamentId: string): TournamentParticipant[] => {
  return mockTournamentParticipants.filter(participant => participant.tournamentId === tournamentId)
}

export const getTournamentMatches = (tournamentId: string): TournamentMatch[] => {
  return mockTournamentMatches.filter(match => match.tournamentId === tournamentId)
}

export const getTournamentBracket = (tournamentId: string): TournamentBracket | undefined => {
  return mockTournamentBrackets.find(bracket => bracket.tournamentId === tournamentId)
} 