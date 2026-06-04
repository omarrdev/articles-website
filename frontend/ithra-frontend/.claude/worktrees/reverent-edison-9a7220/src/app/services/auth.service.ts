import { Injectable, signal, computed, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { User } from '../models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private apiUrl = environment.apiUrl;

  currentUser = signal<User | null>(null);
  isLoggedIn = computed(() => this.currentUser() !== null);
  isAdmin = computed(() => this.currentUser()?.role === 'ADMIN');

  login(email: string, password: string) {
    return this.http.post<{ accessToken: string; refreshToken: string }>(
      `${this.apiUrl}/auth/login`, { email, password }
    ).pipe(
      tap(res => {
        localStorage.setItem('accessToken', res.accessToken);
        localStorage.setItem('refreshToken', res.refreshToken);
        this.decodeAndSetUser();
      })
    );
  }

  register(username: string, email: string, password: string) {
    return this.http.post(`${this.apiUrl}/auth/register`, { username, email, password });
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  forgotPassword(email: string) {
    return this.http.post(`${this.apiUrl}/auth/forgot-password`, { email });
  }

  resetPassword(token: string, newPassword: string) {
    return this.http.post(`${this.apiUrl}/auth/reset-password`, { token, newPassword });
  }

  verifyEmail(token: string) {
    return this.http.post(`${this.apiUrl}/auth/verify-email`, { token });
  }

  resendVerification(email: string) {
    return this.http.post(`${this.apiUrl}/auth/resend-verification`, { email });
  }

  refreshToken() {
    const refreshToken = localStorage.getItem('refreshToken');
    return this.http.post<{ accessToken: string }>(`${this.apiUrl}/auth/refresh`, { refreshToken }).pipe(
      tap(res => localStorage.setItem('accessToken', res.accessToken))
    );
  }

  decodeAndSetUser(): void {
    const token = localStorage.getItem('accessToken');
    if (!token) { this.currentUser.set(null); return; }
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      if (payload.exp * 1000 < Date.now()) {
        this.logout();
        return;
      }
      this.currentUser.set({
        id: payload.id,
        username: payload.username,
        email: payload.email,
        role: payload.role,
        isEmailVerified: payload.isEmailVerified,
        createdAt: payload.createdAt
      });
    } catch {
      this.currentUser.set(null);
    }
  }
}
