import { SportEvent } from "../../models/SportEvent";
import { API_BASE_URL, ENDPOINTS } from "../../constants/apis";
import { getAuthHeaders } from "../../utils/auth";

export async function getSportEventById(id: string): Promise<Response> {
    return fetch(`${API_BASE_URL}/api/events/${id}`, {
        method: 'GET',
        headers: getAuthHeaders()
    })
}

export async function getAllSportEvents(): Promise<Response> {
    return fetch(`${API_BASE_URL}/api/events`, {
        method: 'GET',
        headers: getAuthHeaders()
    })
}

export async function createSportEvent(event: SportEvent): Promise<Response> {
    return fetch(`${API_BASE_URL}/api${ENDPOINTS.CREATE_EVENT}`, {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify({
            minAge: event.minAge,
            maxAge: event.maxAge,
            description: event.description,
            eventTime: event.eventTime,
            latitude: event.latitude,
            longitude: event.longitude,
            location: event.location,
            numberOfParticipantsTotal: event.participants,
            sportType: event.sportType
        })
    })
}
  
export async function updateSportEvent(id: string, event: SportEvent): Promise<Response> {
    return fetch(`${API_BASE_URL}/api/events/${id}`, {
        method: 'PUT',
        headers: getAuthHeaders(),
        body: JSON.stringify({
            minAge: event.minAge,
            maxAge: event.maxAge,
            description: event.description,
            eventTime: event.eventTime,
            latitude: event.latitude,
            longitude: event.longitude,
            location: event.location,
            numberOfParticipantsTotal: event.participants,
            sportType: event.sportType
        })
    })
}

export async function joinEvent(id: string): Promise<Response> {
    return fetch(`${API_BASE_URL}/api/events/${id}/join`, {
        method: 'POST',
        headers: getAuthHeaders()
    })
}

export async function removeParticipant(eventId: string, participantId: string): Promise<Response> {
    return fetch(`${API_BASE_URL}/api/events/${eventId}/participants/${participantId}`, {
        method: 'DELETE',
        headers: getAuthHeaders()
    })
}

export async function checkParticipation(id: string): Promise<Response> {
    return fetch(`${API_BASE_URL}/api/events/${id}/participating`, {
        method: 'GET',
        headers: getAuthHeaders()
    })
}

export async function deleteEvent(id: string): Promise<Response> {
    return fetch(`${API_BASE_URL}/api/events/${id}`, {
        method: 'DELETE',
        headers: getAuthHeaders()
    })
}
