import { API_BASE_URL, HEADERS, ENDPOINTS } from "../../constants/apis"

export interface CommunityMember {
  id: string
  fullName: string
  email: string
  phone: string
  age: number
  description?: string
  birthDate?: string
}

export interface CommunityStats {
  totalFriends: number
}

export async function getCommunityMembers(): Promise<CommunityMember[]> {
  try {
    const friendsResponse = await fetch(`${API_BASE_URL}/api${ENDPOINTS.CURRENT_USER_FRIENDS}`, {
      method: 'GET',
      headers: HEADERS.JSON,
      credentials: 'include'
    })

    if (!friendsResponse.ok) {
      throw new Error('Failed to fetch friends')
    }

    const friendIds: string[] = await friendsResponse.json()
    
    const memberPromises = friendIds.map(async (friendId) => {
      const userResponse = await fetch(`${API_BASE_URL}/api${ENDPOINTS.USER_DETAILS(friendId)}`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
      })

      if (!userResponse.ok) {
        console.warn(`Failed to fetch details for user ${friendId}`)
        return null
      }

      const userDetails = await userResponse.json()
      
      const age = userDetails.birthDate ? calculateAge(userDetails.birthDate) : 0
      
      return {
        id: userDetails.id,
        fullName: userDetails.name,
        email: userDetails.email,
        phone: userDetails.phoneNumber || '',
        age,
        description: userDetails.description,
        birthDate: userDetails.birthDate
      }
    })

    const members = await Promise.all(memberPromises)
    return members.filter(member => member !== null) as CommunityMember[]
  } catch (error) {
    console.error('Error fetching community members:', error)
    throw error
  }
}

export async function searchCommunityMembers(query: string): Promise<CommunityMember[]> {
  try {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.SEARCH_USERS}?query=${encodeURIComponent(query)}`, {
      method: 'GET',
      headers: HEADERS.JSON,
      credentials: 'include'
    })

    if (!response.ok) {
      throw new Error('Failed to search users')
    }

    const users = await response.json()
    
    return users.map((user: any) => ({
      id: user.id,
      fullName: user.name,
      email: user.email,
      phone: user.phoneNumber || '',
      age: user.birthDate ? calculateAge(user.birthDate) : 0,
      description: user.description,
      birthDate: user.birthDate
    }))
  } catch (error) {
    console.error('Error searching community members:', error)
    throw error
  }
}

export async function getCommunityStats(): Promise<CommunityStats> {
  try {
    const friendsResponse = await fetch(`${API_BASE_URL}/api${ENDPOINTS.CURRENT_USER_FRIENDS}`, {
      method: 'GET',
      headers: HEADERS.JSON,
      credentials: 'include'
    })

    if (!friendsResponse.ok) {
      throw new Error('Failed to fetch friends')
    }

    const friends: string[] = await friendsResponse.json()
    
    return {
      totalFriends: friends.length
    }
  } catch (error) {
    console.error('Error fetching community stats:', error)
    throw error
  }
}

function calculateAge(birthDate: string): number {
  const today = new Date()
  const birth = new Date(birthDate)
  let age = today.getFullYear() - birth.getFullYear()
  const monthDiff = today.getMonth() - birth.getMonth()
  
  if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
    age--
  }
  
  return age
} 