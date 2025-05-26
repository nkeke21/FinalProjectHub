import { API_BASE_URL, HEADERS } from "@/constants/apis";
import { SportEvent } from "@/models/sportEvent";

export async function getUserRegisteredEvents(userId: string): Promise<SportEvent[]> {
    const response = await fetch(`${API_BASE_URL}/api/users/events/registered/${userId}`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
    });

    if (!response.ok) {
        const errorData = await response.json();
        console.error("Failed to fetch registered events:", errorData);
        throw new Error("Failed to fetch registered events");
    }

    return await response.json();
}

export async function getUserHostedEvents(userId: string): Promise<SportEvent[]> {
    const response = await fetch(`${API_BASE_URL}/api/users/events/hosted/${userId}`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
    });

    if (!response.ok) {
        const errorData = await response.json();
        console.error("Failed to fetch hosted events:", errorData);
        throw new Error("Failed to fetch hosted events");
    }

    return await response.json();
}