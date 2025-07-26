import { AuthService } from '../services/apis/AuthService'

export function getAuthHeaders(): Record<string, string> {
    const token = AuthService.getToken()
    return {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        ...(token && { 'Authorization': `Bearer ${token}` })
    }
}

export function getAuthHeadersWithCredentials(): Record<string, string> {
    const token = AuthService.getToken()
    return {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        ...(token && { 'Authorization': `Bearer ${token}` })
    }
} 