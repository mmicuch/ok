import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService, private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.auth.getToken();
    
    // Add token to authorized requests if available
    if (token) {
      const cloned = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
      });
      
      return next.handle(cloned).pipe(
        catchError((error: HttpErrorResponse) => {
          // Handle 401 Unauthorized errors (expired token, etc.)
          if (error.status === 401) {
            this.auth.logout();
            this.router.navigate(['/login']);
          }
          return throwError(() => error);
        })
      );
    }
    
    return next.handle(req);
  }
}