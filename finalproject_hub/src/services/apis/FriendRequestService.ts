import { API_BASE_URL, HEADERS } from "@/constants/apis"

export interface FriendRequestPayload {
    fromUserId: string;
    toUserId: string;
}

export async function sendFriendRequest(payload: FriendRequestPayload): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/api/friends/send`, {
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
