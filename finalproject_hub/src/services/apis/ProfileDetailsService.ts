import { API_BASE_URL, HEADERS, ENDPOINTS } from "@/constants/apis"
import { UserUpdateDTO } from "@/models/UserUpdateDTO"

export interface UserDetailsResponse {
  name: string
  email: string
  phoneNumber: string
  birthDate: string
  description: string
}

export async function getUserDetails(userId: string): Promise<UserDetailsResponse> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.USER_DETAILS(userId)}`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
    })

    if (!response.ok) {
        const errorData = await response.json()
        console.error("❌ Failed to fetch user details:", errorData)
        throw new Error("Could not load user details.")
    }

    return await response.json()
}

export async function getCurrentUserDetails(): Promise<UserDetailsResponse> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.CURRENT_USER_DETAILS}`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
    })

    if (!response.ok) {
        const errorData = await response.json()
        console.error("❌ Failed to fetch current user details:", errorData)
        throw new Error("Could not load current user details.")
    }

    return await response.json()
}

export async function updateUserDetails(userId: string, update: UserUpdateDTO): Promise<UserUpdateDTO> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.USER_DETAILS(userId)}`, {
        method: 'PATCH',
        headers: HEADERS.JSON,
        credentials: 'include',
        body: JSON.stringify(update)
    })

    if (!response.ok) {
        const errorData = await response.json()
        console.error("❌ Failed to update user details:", errorData)
        throw new Error("Could not update user details")
    }

    return await response.json()
}

// New method for updating current user details (session-based)
export async function updateCurrentUserDetails(update: UserUpdateDTO): Promise<UserUpdateDTO> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.UPDATE_CURRENT_USER_DETAILS}`, {
        method: 'PATCH',
        headers: HEADERS.JSON,
        credentials: 'include',
        body: JSON.stringify(update)
    })

    if (!response.ok) {
        const errorData = await response.json()
        console.error("❌ Failed to update current user details:", errorData)
        throw new Error("Could not update current user details")
    }

    return await response.json()
}
