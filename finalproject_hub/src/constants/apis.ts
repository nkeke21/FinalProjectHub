export const API_BASE_URL = ''

export const ENDPOINTS = {
    CREATE_EVENT: '/events',
    USER_DETAILS: (id: string) => `/users/details/${id}`,
    USER_REGISTERED_EVENTS: (id: string) => `/users/events/registered/${id}`,
    USER_HOSTED_EVENTS: (id: string) => `/users/events/hosted/${id}`,
    CURRENT_USER_DETAILS: '/users/details',
    CURRENT_USER_REGISTERED_EVENTS: '/users/events/registered',
    CURRENT_USER_HOSTED_EVENTS: '/users/events/hosted',
    UPDATE_CURRENT_USER_DETAILS: '/users/details',
    SEND_FRIEND_REQUEST: '/friends/send',
    GET_PENDING_FRIEND_REQUESTS: (userId: string) => `/friends/${userId}/requests`,
    CURRENT_USER_PENDING_FRIEND_REQUESTS: '/friends/requests',
    RESPOND_TO_FRIEND_REQUEST: '/friends/respond',
    GET_FRIENDS: (userId: string) => `/friends/${userId}/`,
    CURRENT_USER_FRIENDS: '/friends/',
    DELETE_FRIEND: (userId: string, friendId: string) => `/friends/${userId}/friends/${friendId}`,
    CHECK_FRIEND_REQUEST: (user1Id: string, user2Id: string) => `/friends/check-request/${user1Id}/${user2Id}`
}

export const HEADERS = {
    JSON: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
}