import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs';
import { map, catchError, tap } from 'rxjs/operators';
import { Router } from '@angular/router';

interface AuthResponse {
  token: string;
  username: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'token';
  private usernameKey = 'username';
  private apiUrl = '/api/auth';  // Make sure this matches your backend

  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string): Observable<boolean> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, { username, password })
      .pipe(
        tap(response => console.log('Login response:', response)), // Added for debugging
        map(response => {
          if (response && response.token) {
            localStorage.setItem(this.tokenKey, response.token);
            localStorage.setItem(this.usernameKey, response.username);
            console.log('Token stored:', response.token);
            return true;
          }
          return false;
        }),
        catchError(err => {
          console.error('Authentication failed:', err);
          return throwError(() => new Error('Authentication failed'));
        })
      );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.usernameKey);
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    return !!token;
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getUsername(): string | null {
    return localStorage.getItem(this.usernameKey);
  }
}