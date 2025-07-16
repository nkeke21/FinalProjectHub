import { SportEvent } from "@/models/sportEvent";
import { API_BASE_URL, ENDPOINTS, HEADERS } from "@/constants/apis";

export async function getSportEventById(id: string): Promise<Response> {
    return fetch(`${API_BASE_URL}/api/events/${id}`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
    })
}

export async function getAllSportEvents(): Promise<Response> {
    return fetch(`${API_BASE_URL}/api/events`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
    })
}

export async function createSportEvent(event: SportEvent): Promise<Response> {
    return fetch(`${API_BASE_URL}/api${ENDPOINTS.CREATE_EVENT}`, {
        method: 'POST',
        headers: HEADERS.JSON,
        credentials: 'include',
        body: JSON.stringify({
            minAge: event.minAge,
            maxAge: event.maxAge,
            description: event.description,
            eventTime: event.eventTime,
            latitude: event.locationLat,
            longitude: event.locationLng,
            location: event.location,
            numberOfParticipantsTotal: event.participants,
            sportType: event.sportType
        })
    })
}
  
export async function updateSportEvent(id: string, event: SportEvent): Promise<Response> {
    return fetch(`${API_BASE_URL}/api/events/${id}`, {
        method: 'PUT',
        headers: HEADERS.JSON,
        credentials: 'include',
        body: JSON.stringify({
            ageRange: event.ageRange,
            description: event.description,
            eventTime: event.eventTime,
            latitude: event.locationLat,
            longitude: event.locationLng,
            location: event.location,
            numberOfParticipantsTotal: event.participants,
            sportType: event.sportType
        })
    })
}

export async function joinEvent(id: string): Promise<Response> {
    return fetch(`${API_BASE_URL}/api/events/${id}/join`, {
        method: 'POST',
        headers: HEADERS.JSON,
        credentials: 'include'
    })
}

export async function checkParticipation(id: string): Promise<Response> {
    return fetch(`${API_BASE_URL}/api/events/${id}/participating`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
    })
}
  
