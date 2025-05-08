import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../services/api.service';
import { Vaccine } from '../../../models/interfaces';

@Component({
  selector: 'app-add-vaccine',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="form-container">
      <h2>Add New Vaccine</h2>
      <form (ngSubmit)="onSubmit()" #vaccineForm="ngForm">
        <div class="form-group">
          <label for="nazov">Vaccine Name *</label>
          <input 
            type="text"
            id="nazov"
            [(ngModel)]="vaccine.nazov" 
            name="nazov" 
            required
            class="form-control"
            #nazov="ngModel">
          <div class="error" *ngIf="nazov.invalid && (nazov.dirty || nazov.touched || formSubmitted)">
            Vaccine name is required
          </div>
        </div>

        <div class="form-group">
          <label for="typ">Vaccine Type *</label>
          <select 
            id="typ"
            [(ngModel)]="vaccine.typ" 
            name="typ" 
            required
            class="form-control"
            #typ="ngModel">
            <option value="">Select vaccine type</option>
            <option value="mRNA">mRNA</option>
            <option value="vektorová">Vector</option>
            <option value="proteínová">Protein</option>
            <option value="iná">Other</option>
          </select>
          <div class="error" *ngIf="typ.invalid && (typ.dirty || typ.touched || formSubmitted)">
            Vaccine type is required
          </div>
        </div>

        <div class="form-group">
          <label for="vyrobca">Manufacturer *</label>
          <input 
            type="text"
            id="vyrobca"
            [(ngModel)]="vaccine.vyrobca" 
            name="vyrobca" 
            required
            class="form-control"
            #vyrobca="ngModel">
          <div class="error" *ngIf="vyrobca.invalid && (vyrobca.dirty || vyrobca.touched || formSubmitted)">
            Manufacturer is required
          </div>
        </div>

        <button type="submit" [disabled]="loading" class="btn-primary">
          Add Vaccine
        </button>

        <div *ngIf="message" [class]="messageType">
          {{ message }}
        </div>
        
        <div *ngIf="loading" class="loading-indicator">
          <div class="spinner"></div>
          <span>Processing...</span>
        </div>
      </form>
    </div>
  `,
  styles: [`
    .form-container { 
      max-width: 500px; 
      margin: 20px auto; 
      padding: 20px; 
      border-radius: 8px;
      background-color: #f8f8f8;
      box-shadow: 0 4px 10px rgba(0,0,0,0.1);
    }
    h2 {
      color: #212121;
      margin-bottom: 20px;
      font-weight: 600;
      text-align: center;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }
    .form-group { 
      margin-bottom: 20px;
    }
    .form-control { 
      width: 100%;
      padding: 12px;
      border: 1px solid #e0e0e0;
      border-radius: 4px;
      margin-top: 5px;
      transition: border-color 0.3s, box-shadow 0.3s;
      background-color: #fff;
    }
    .form-control:focus {
      border-color: #212121;
      outline: none;
      box-shadow: 0 0 0 2px rgba(33, 33, 33, 0.1);
    }
    select.form-control {
      appearance: none;
      background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='6' fill='none'%3E%3Cpath fill='%23666' d='M6 6 0 0h12L6 6Z'/%3E%3C/svg%3E");
      background-repeat: no-repeat;
      background-position: right 12px center;
      padding-right: 36px;
    }
    .error { 
      color: #d32f2f; 
      font-size: 0.875em; 
      margin-top: 5px; 
    }
    .success { 
      color: #388e3c; 
      margin-top: 10px; 
      padding: 8px;
      background-color: #e8f5e9;
      border-radius: 4px;
      text-align: center;
    }
    .failure { 
      color: #d32f2f; 
      margin-top: 10px; 
      padding: 8px;
      background-color: #ffebee;
      border-radius: 4px;
      text-align: center;
    }
    .btn-primary { 
      width: 100%;
      padding: 12px;
      background: #212121;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-weight: 500;
      text-transform: uppercase;
      letter-spacing: 0.5px;
      transition: background-color 0.3s;
    }
    .btn-primary:hover { 
      background: #424242;
    }
    .btn-primary:disabled { 
      background: #bdbdbd;
      cursor: not-allowed;
    }
    label { 
      font-weight: 500;
      color: #424242;
      display: block;
      margin-bottom: 6px;
    }
    .loading-indicator {
      display: flex;
      justify-content: center;
      align-items: center;
      gap: 10px;
      margin-top: 10px;
    }
    .spinner {
      width: 20px;
      height: 20px;
      border: 2px solid rgba(0, 0, 0, 0.1);
      border-top-color: #212121;
      border-radius: 50%;
      animation: spin 0.8s linear infinite;
    }
    @keyframes spin {
      to { transform: rotate(360deg); }
    }
  `]
})
export class AddVaccineComponent {
  vaccine: Vaccine = {
    nazov: '',
    typ: '',
    vyrobca: ''
  };

  message = '';
  messageType = '';
  loading = false;
  formSubmitted = false;

  constructor(private apiService: ApiService) {}

  onSubmit() {
    this.formSubmitted = true;
    this.message = '';
    
    // Validate form manually
    if (!this.vaccine.nazov || !this.vaccine.typ || !this.vaccine.vyrobca) {
      this.message = 'Please fill in all required fields';
      this.messageType = 'failure';
      return;
    }
    
    this.loading = true;
    console.log('Sending vaccine:', this.vaccine);
  
    this.apiService.addVaccine(this.vaccine).subscribe({
      next: (response) => {
        if (response.error) {
          console.error(`Error with endpoint ${response.endpoint}:`, response.error);
          this.message = `Error adding vaccine: ${response.error.status || response.error.message || 'Unknown error'}`;
          this.messageType = 'failure';
        } else {
          this.message = 'Vaccine added successfully';
          this.messageType = 'success';
          this.resetForm();
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error adding vaccine:', error);
        this.message = `Error adding vaccine: ${error.message || error.status || 'Unknown error'}`;
        this.messageType = 'failure';
        this.loading = false;
      }
    });
  }
  
  private resetForm() {
    this.vaccine = {
      nazov: '',
      typ: '',
      vyrobca: ''
    };
    this.formSubmitted = false;
  }
}