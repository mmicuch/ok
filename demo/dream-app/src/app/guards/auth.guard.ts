import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    if (this.auth.isLoggedIn()) {
      console.log('User is logged in, allowing access');
      return true;
    }
    
    console.log('User not logged in, redirecting to login page');
    // Store the attempted URL for redirecting after login
    const url = state.url;
    this.router.navigate(['/login'], { queryParams: { returnUrl: url } });
    return false;
  }
}