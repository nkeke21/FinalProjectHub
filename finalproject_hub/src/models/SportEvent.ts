export interface SportEvent {
    locationLat: number | null
    locationLng: number | null
    participants: number
    ageRange: string
    eventTime: number | null
    sportType: string | null
    description: string | null
}