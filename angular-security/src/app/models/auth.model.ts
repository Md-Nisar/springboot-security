import { Role } from "./user.model";

export interface SignupRequest {
    username: string;
    email: string;
    password?: string;
    confirmPassword?: string;
}

export interface SignupResponse {
    id: number;
    username: string;
    email: string;
    message: string;
    success: boolean;
}

export interface LoginRequest {
    username: string;
    password: string;
}

export interface LoginResponse {
    username: string;
    email: string;
    token: string;
}

export interface JwtResponse {
    token: string;
    type: string;
    username: string;
    expiration: Date;
    roles: Set<Role>
}

export interface AuthResponse {
    accessToken: string;
    refreshToken: string;
}
