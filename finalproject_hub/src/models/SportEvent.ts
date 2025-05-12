export interface SportEvent {
    locationLat: number | null
    locationLng: number | null
    location: string
    participants: number
    ageRange: string
    eventTime: number | null
    sportType: string | null
    description: string | null
}