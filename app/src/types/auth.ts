export interface AuthResponse {
    token: string
    type: string
    userId: number
    username: string
    role: string
    expiresAt: number
}

export interface UserResponse {
    id: number
    username: string
    email: string
    role: string
    createdAt: string
}