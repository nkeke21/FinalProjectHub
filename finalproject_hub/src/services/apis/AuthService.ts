import { API_BASE_URL } from '../../constants/apis'

export interface SignUpRequest {
    name: string
    email: string
    phoneNumber: string
    birthDate: number | null
    description: string
    password: string
}

export interface SignInRequest {
    username: string
    password: string
}

export interface AuthResponse {
    userId: string
    name: string
    email: string
    message: string
}

export class AuthService {
    static async signUp(signUpData: SignUpRequest): Promise<AuthResponse> {
        const response = await fetch(`${API_BASE_URL}/api/auth/signup`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(signUpData),
        })

        if (!response.ok) {
            const errorData = await response.json()
            throw new Error(errorData.message || 'Registration failed')
        }

        return response.json()
    }

    static async signIn(signInData: SignInRequest): Promise<AuthResponse> {
        const response = await fetch(`${API_BASE_URL}/api/auth/signin`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(signInData),
        })

        if (!response.ok) {
            const errorData = await response.json()
            throw new Error(errorData.message || 'Login failed')
        }

        return response.json()
    }
} 