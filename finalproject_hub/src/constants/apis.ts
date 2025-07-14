export const API_BASE_URL = ''

export const ENDPOINTS = {
    CREATE_EVENT: '/events',
    USER_DETAILS: (id: string) => `/users/details/${id}`,
    USER_REGISTERED_EVENTS: (id: string) => `/users/events/registered/${id}`,
    USER_HOSTED_EVENTS: (id: string) => `/users/events/hosted/${id}`,
    CURRENT_USER_DETAILS: '/users/me/details',
    CURRENT_USER_REGISTERED_EVENTS: '/users/me/events/registered',
    CURRENT_USER_HOSTED_EVENTS: '/users/me/events/hosted',
    UPDATE_CURRENT_USER_DETAILS: '/users/me/details'
}

export const HEADERS = {
    JSON: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
}