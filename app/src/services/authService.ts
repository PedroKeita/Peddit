import api from '../api/axios'
import type { AuthResponse, UserResponse } from '../types/auth'

export interface LoginData {
    email: string
    password: string
}

export interface RegisterData {
    username: string
    email: string
    password: string
}

export async function login(data: LoginData): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/api/auth/login', data)
    return response.data
}

export async function register(data: RegisterData): Promise<UserResponse> {
    const response = await api.post<UserResponse>('/api/auth/register', data)
    return response.data
}