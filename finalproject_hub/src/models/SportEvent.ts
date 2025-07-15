export interface SportEvent {
    eventId: string | null
    hostId: string | null
    hostName: string | null
    latitude: number | null
    longitude: number | null
    location: string
    participants: number
    numberOfParticipantsTotal: number
    numberOfParticipantsRegistered: number
    ageRange: string
    eventTime: number | null
    sportType: string | null
    description: string | null
    participantsList?: Array<{ name: string; age: number }>
}