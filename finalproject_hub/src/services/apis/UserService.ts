import { API_BASE_URL, ENDPOINTS, HEADERS } from '../../constants/apis'

export interface UserDetails {
  name: string
  email: string
  phoneNumber: string
  birthDate: string
  description: string
}

export class UserService {
  static async getUserById(userId: string): Promise<UserDetails> {
    try {
      const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.USER_DETAILS(userId)}`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include',
      })

      if (!response.ok) {
        throw new Error(`Failed to fetch user details: ${response.status}`)
      }

      const userDetails = await response.json()
      return userDetails
    } catch (error) {
      console.error('Error fetching user details:', error)
      throw error
    }
  }

  static async getCurrentUser(): Promise<UserDetails> {
    try {
      const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.CURRENT_USER_DETAILS}`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include',
      })

      if (!response.ok) {
        throw new Error(`Failed to fetch current user details: ${response.status}`)
      }

      const userDetails = await response.json()
      return userDetails
    } catch (error) {
      console.error('Error fetching current user details:', error)
      throw error
    }
  }
} 