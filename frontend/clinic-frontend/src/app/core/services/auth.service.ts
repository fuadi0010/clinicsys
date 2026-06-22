import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { jwtDecode } from 'jwt-decode';
import { JwtPayload } from '../models/auth/jwt-payload.model';

import { environment } from '../../../environments/environment';

import { LoginRequest } from '../models/auth/login-request.model';
import { LoginResponse } from '../models/auth/login-response.model';
import { RegisterRequest } from '../models/auth/register-request.model';
import { VerifyOtpRequest } from '../models/auth/verify-otp-request.model';
import { ApiResponse } from '../models/api-response.model';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly http = inject(HttpClient);

  private readonly API =
    `${environment.apiUrl}/api/auth`;

  login(
  request: LoginRequest
): Observable<LoginResponse> {

  return this.http.post<ApiResponse<LoginResponse>>(
    `${this.API}/login`,
    request
  ).pipe(
      map(res => res.data)
    );

}

  register(
    request: RegisterRequest
  ) {
    return this.http.post<ApiResponse<any>>(
      `${this.API}/register`,
      request
    ).pipe(
      map(res => res.data)
    );
  }

  verifyOtp(
    request: VerifyOtpRequest
  ) {
    return this.http.post<ApiResponse<string>>(
      `${this.API}/verify-otp`,
      request
    ).pipe(
      map(res => res.data)
    );
  }

  resendOtp(
    email: string
  ) {
    return this.http.post<ApiResponse<string>>(
      `${this.API}/resend-otp`,
      {
        email
      }
    ).pipe(
      map(res => res.data)
    );
  }

  // ===== Refresh Token =====

  refreshToken(): Observable<LoginResponse> {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
      throw new Error('No refresh token');
    }
    return this.http.post<ApiResponse<LoginResponse>>(
      `${this.API}/refresh`,
      { refreshToken }
    ).pipe(
      map(res => res.data)
    );
  }

  // ===== JWT Helper =====

  saveAuthData(
    token: string,
    refreshToken: string
  ): void {
    localStorage.setItem('token', token);
    localStorage.setItem('refreshToken', refreshToken);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getRefreshToken(): string | null {
    return localStorage.getItem('refreshToken');
  }

  logout(): void {
    const refreshToken = this.getRefreshToken();
    if (refreshToken) {
      this.http.post<ApiResponse<void>>(`${this.API}/logout`, { refreshToken }).pipe(
        map(res => res.data)
      ).subscribe({
        error: () => {}
      });
    }
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
  }

  isLoggedIn(): boolean {

  const token = this.getToken();

  if (!token) {
    return false;
  }

  return !this.isTokenExpired();
}
  getPayload(): JwtPayload | null {

  const token = this.getToken();

  if (!token) {
    return null;
  }

  return jwtDecode<JwtPayload>(token);
}
getUserId(): number | null {

  const payload = this.getPayload();

  return payload?.userId ?? null;
}
getRole(): string | null {

  const payload = this.getPayload();

  return payload?.role ?? null;
}
isPatient(): boolean {

  return this.getRole() === 'PATIENT';
}
isDoctor(): boolean {

  return this.getRole() === 'DOCTOR';
}
  isAdmin(): boolean {

    return this.getRole() === 'ADMIN';
  }
  isVerified(): boolean {
    return this.getPayload()?.isVerified ?? false;
  }
  getEmail(): string | null {
    return this.getPayload()?.email ?? null;
  }
  isTokenExpired(): boolean {

    const payload = this.getPayload();

    if (!payload) {
      return true;
    }

    const currentTime =
      Math.floor(Date.now() / 1000);

    return payload.exp < currentTime;
  }

  getAllUsers(): Observable<any[]> {
    return this.http.get<ApiResponse<any[]>>(`${environment.apiUrl}/api/users`).pipe(
      map(res => res.data)
    );
  }

  getAdminsCount(): Observable<number> {
    return this.http.get<ApiResponse<number>>(`${environment.apiUrl}/api/users/count/admins`).pipe(
      map(res => res.data)
    );
  }
}