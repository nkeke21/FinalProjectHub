import { API_BASE_URL, HEADERS, ENDPOINTS } from "@/constants/apis"
import { SportEvent } from "@/models/sportEvent"

export async function getUserRegisteredEvents(userId: string): Promise<SportEvent[]> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.USER_REGISTERED_EVENTS(userId)}`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
    })

    if (!response.ok) {
        const errorData = await response.json()
        console.error("Failed to fetch registered events:", errorData)
        throw new Error("Failed to fetch registered events")
    }

    return await response.json()
}

export async function getUserHostedEvents(userId: string): Promise<SportEvent[]> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.USER_HOSTED_EVENTS(userId)}`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
    })

    if (!response.ok) {
        const errorData = await response.json()
        console.error("Failed to fetch hosted events:", errorData)
        throw new Error("Failed to fetch hosted events")
    }

    return await response.json()
    }
