import { API_BASE_URL, HEADERS } from "@/constants/apis";

export interface UserSearchResult {
    id: string;
    name: string;
    email: string;
}

export async function searchUsers(query: string): Promise<UserSearchResult[]> {
    const response = await fetch(`${API_BASE_URL}/api/users/search?query=${encodeURIComponent(query)}`, {
        method: 'GET',
        headers: HEADERS.JSON,
        credentials: 'include'
    });

    if (!response.ok) {
        const errorData = await response.json();
        console.error("❌ Failed to search users:", errorData);
        throw new Error("Could not search users");
    }

    return await response.json();
}
