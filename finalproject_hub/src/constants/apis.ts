export const API_BASE_URL = ''

export const ENDPOINTS = {
    CREATE_EVENT: '/events',
    USER_DETAILS: (id: string) => `/users/details/${id}`,
    USER_REGISTERED_EVENTS: (id: string) => `/users/events/registered/${id}`,
    USER_HOSTED_EVENTS: (id: string) => `/users/events/hosted/${id}`
}

export const HEADERS = {
    JSON: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
}