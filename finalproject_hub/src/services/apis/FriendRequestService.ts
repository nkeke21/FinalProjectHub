import { API_BASE_URL, ENDPOINTS } from "../../constants/apis"
import { getAuthHeaders } from "../../utils/auth"

export interface FriendRequestPayload {
    fromUserId: string;
    toUserId: string;
}

export interface FriendRequest {
    requestId: string;
    fromUserId: string;
    toUserId: string;
    sentAt: string;
    status: 'PENDING' | 'ACCEPTED' | 'REJECTED';
}

export interface FriendRequestResponse {
    requestId: string;
    status: string;
}

export async function sendFriendRequest(payload: FriendRequestPayload): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.SEND_FRIEND_REQUEST}`, {
        method: 'POST',
        headers: HEADERS.JSON,
        credentials: 'include',
        body: JSON.stringify(payload)
    })

    if (!response.ok) {
        const errorData = await response.json()
        console.error("❌ Failed to send friend request:", errorData)
        throw new Error("Could not send friend request")
    }
}

export async function getPendingFriendRequests(userId: string): Promise<FriendRequest[]> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.GET_PENDING_FRIEND_REQUESTS(userId)}`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
    })

    if (!response.ok) {
        const errorData = await response.json()
        console.error("❌ Failed to fetch pending friend requests:", errorData)
        throw new Error("Could not fetch pending friend requests")
    }

    return await response.json()
}

export async function getCurrentUserPendingFriendRequests(): Promise<FriendRequest[]> {
    const url = `${API_BASE_URL}/api${ENDPOINTS.CURRENT_USER_PENDING_FRIEND_REQUESTS}`
    console.log('Fetching friend requests from:', url)
    
    const response = await fetch(url, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
    })

    console.log('Response status:', response.status)
    console.log('Response ok:', response.ok)

    if (!response.ok) {
        const errorData = await response.json()
        console.error("❌ Failed to fetch current user's pending friend requests:", errorData)
        throw new Error("Could not fetch pending friend requests")
    }

    const data = await response.json()
    console.log('Received friend requests data:', data)
    return data
}

export async function respondToFriendRequest(requestId: string, status: 'ACCEPTED' | 'REJECTED'): Promise<FriendRequest> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.RESPOND_TO_FRIEND_REQUEST}`, {
        method: 'POST',
        headers: HEADERS.JSON,
        credentials: 'include',
        body: JSON.stringify({
            requestId: requestId,
            status: status
        })
    })

    if (!response.ok) {
        const errorData = await response.json()
        console.error("❌ Failed to respond to friend request:", errorData)
        throw new Error("Could not respond to friend request")
    }

    return await response.json()
}

export async function getFriends(userId: string): Promise<string[]> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.GET_FRIENDS(userId)}`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
    })

    if (!response.ok) {
        const errorData = await response.json()
        console.error("❌ Failed to fetch friends:", errorData)
        throw new Error("Could not fetch friends")
    }

    return await response.json()
}

export async function getCurrentUserFriends(): Promise<string[]> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.CURRENT_USER_FRIENDS}`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
    })

    if (!response.ok) {
        const errorData = await response.json()
        console.error("❌ Failed to fetch current user's friends:", errorData)
        throw new Error("Could not fetch friends")
    }

    return await response.json()
}

export async function deleteFriend(userId: string, friendId: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.DELETE_FRIEND(userId, friendId)}`, {
        method: 'DELETE',
        headers: HEADERS.JSON,
        credentials: 'include'
    })

    if (!response.ok) {
        const errorData = await response.json()
        console.error("❌ Failed to delete friend:", errorData)
        throw new Error("Could not delete friend")
    }
}

export async function checkFriendRequestAPI(user1Id: string, user2Id: string): Promise<FriendRequest | null> {
    const response = await fetch(`${API_BASE_URL}/api${ENDPOINTS.CHECK_FRIEND_REQUEST(user1Id, user2Id)}`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
    })

    if (response.status === 404) {
        return null
    }

    if (!response.ok) {
        const errorData = await response.json()
        console.error("❌ Failed to check friend request:", errorData)
        throw new Error("Could not check friend request")
    }

    return await response.json()
}
