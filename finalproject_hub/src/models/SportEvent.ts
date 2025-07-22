export interface ParticipantInfo {
    userId: string;
    name: string;
    email: string;
    age: number;
}

export interface SportEvent {
    eventId: string | null
    hostId: string | null
    hostName: string | null
    hostEmail: string | null
    hostPhone: string | null
    latitude: number | null
    longitude: number | null
    location: string
    participants: number
    numberOfParticipantsTotal: number
    numberOfParticipantsRegistered: number
    minAge: number
    maxAge: number
    eventTime: number | null
    sportType: string | null
    description: string | null
    participantsList: ParticipantInfo[]
}