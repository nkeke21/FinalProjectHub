export const API_BASE_URL = 'http://13.60.247.145:3000'

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
    CHECK_FRIEND_REQUEST: (user1Id: string, user2Id: string) => `/friends/check-request/${user1Id}/${user2Id}`,
    CREATE_TEAM: '/teams',
    GET_TEAM: (id: string) => `/teams/${id}`,
    UPDATE_TEAM: (id: string) => `/teams/${id}`,
    MY_TEAMS: '/teams/my',
    AVAILABLE_TEAMS: '/teams/available',
    JOIN_TEAM: (id: string) => `/teams/${id}/join`,
    LEAVE_TEAM: (id: string) => `/teams/${id}/leave`,
    REMOVE_TEAM_MEMBER: (teamId: string, memberId: string) => `/teams/${teamId}/members/${memberId}`,
    SEND_TEAM_JOIN_REQUEST: '/team-requests/send',
    GET_PENDING_TEAM_REQUESTS_FOR_TEAM: (teamId: string) => `/team-requests/team/${teamId}/pending`,
    GET_PENDING_TEAM_REQUESTS_FOR_USER: '/team-requests/user/pending',
    GET_PENDING_TEAM_REQUESTS_FOR_CAPTAIN: '/team-requests/captain/pending',
    GET_ALL_TEAM_REQUESTS_FOR_TEAM: (teamId: string) => `/team-requests/team/${teamId}/all`,
    GET_ALL_TEAM_REQUESTS_FOR_USER: '/team-requests/user/all',
    RESPOND_TO_TEAM_JOIN_REQUEST: '/team-requests/respond',
    CHECK_TEAM_JOIN_REQUEST: (teamId: string, userId: string) => `/team-requests/check-request/${teamId}/${userId}`,
    GET_CURRENT_USER_ID: '/team-requests/current-user',
    SEND_EVENT_INVITATION: '/event-invitations/send',
    GET_PENDING_EVENT_INVITATIONS: '/event-invitations/user/pending',
    GET_SENT_EVENT_INVITATIONS: '/event-invitations/user/sent',
    RESPOND_TO_EVENT_INVITATION: '/event-invitations/respond',
    DELETE_EVENT_INVITATION: (invitationId: string) => `/event-invitations/${invitationId}`,
    CHECK_EVENT_INVITATION: (eventId: string, userId: string) => `/event-invitations/check-invitation/${eventId}/${userId}`
}

export const HEADERS = {
    JSON: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
}