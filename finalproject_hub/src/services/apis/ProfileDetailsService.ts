import { API_BASE_URL, HEADERS } from "@/constants/apis"
import { UserUpdateDTO } from "@/models/UserUpdateDTO"

export interface UserDetailsResponse {
  name: string
  email: string
  phoneNumber: string
  birthDate: string
  description: string
}

export async function getUserDetails(userId: string): Promise<UserDetailsResponse> {
    const response = await fetch(`${API_BASE_URL}/api/users/details/${userId}`, {
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

export async function updateUserDetails(userId: string, update: UserUpdateDTO): Promise<UserUpdateDTO> {
    const response = await fetch(`${API_BASE_URL}/api/users/details/${userId}`, {
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