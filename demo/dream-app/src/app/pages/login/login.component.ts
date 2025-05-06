import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';
  loading = false;

  constructor(private auth: AuthService, private router: Router) {}

  login() {
    if (!this.username || !this.password) {
      this.error = 'Please enter username and password';
      return;
    }

    this.loading = true;
    this.error = '';

    console.log('Attempting login with:', this.username);

    this.auth.login(this.username, this.password).subscribe({
      next: (success) => {
        this.loading = false;
        if (success) {
          console.log('Login successful, navigating to home');
          this.router.navigate(['/home']);
        } else {
          this.error = 'Login failed, please try again';
        }
      },
      error: (err: HttpErrorResponse) => {
        this.loading = false;
        if (err.status === 401) {
          this.error = 'Invalid username or password';
        } else if (err.status === 0) {
          this.error = 'Cannot connect to server. Please try again later.';
        } else {
          this.error = `Login error: ${err.message || 'Unknown error'}`;
        }
        console.error('Login error:', err);
      }
    });
  }
}