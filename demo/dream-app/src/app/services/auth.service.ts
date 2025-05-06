import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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
  private apiUrl = 'http://localhost:8081/api';  // Changed from https to http and removed /auth

  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string): Observable<boolean> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }),
      withCredentials: true
    };

    console.log('Attempting login for user:', username);
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, { username, password }, httpOptions)
      .pipe(
        tap(response => console.log('Raw login response:', response)),
        map(response => {
          if (response && response.token) {
            console.log('Login successful, token received');
            localStorage.setItem(this.tokenKey, response.token);
            localStorage.setItem(this.usernameKey, response.username);
            return true;
          }
          console.warn('Login response missing token');
          return false;
        }),
        catchError(err => {
          console.error('Login error details:', {
            status: err.status,
            message: err.message,
            error: err.error,
            url: err.url
          });
          return throwError(() => new Error(`Authentication failed: ${err.message}`));
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

  searchOsobaVakcina(query: string): Observable<any> {
    const headers = {
      'Authorization': `Bearer ${this.getToken()}`,
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    };
    
    // Changed from /auth/osobavakcina to /osobavakcina to match backend
    return this.http.get(`${this.apiUrl}/osobavakcina/search`, {
      headers,
      params: { query: query } // Changed from term to query to match backend
    }).pipe(
      tap(response => console.log('Search response:', response)),
      catchError(err => {
        console.error('Error while searching:', err);
        if (err.status === 403) {
          this.router.navigate(['/login']); // Redirect to login on auth failure
          return throwError(() => new Error('Session expired. Please login again.'));
        }
        return throwError(() => new Error(err.error?.message || 'Search failed. Please try again.'));
      })
    );
  }
}