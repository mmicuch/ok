import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../services/api.service';
import { Person, Vaccine, Vaccination } from '../../../models/interfaces';

@Component({
  selector: 'app-add-vaccination',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="form-container">
      <h2>Register Vaccination</h2>
      <form (ngSubmit)="onSubmit()" #vaccinationForm="ngForm">
        <div class="form-group">
          <label for="osobaId">Person *</label>
          <select 
            id="osobaId"
            [(ngModel)]="vaccination.osobaId" 
            name="osobaId" 
            required
            class="form-control"
            #osobaId="ngModel">
            <option [ngValue]="null">Select Person</option>
            <option *ngFor="let person of people" [ngValue]="person.id">
              {{person.meno}} {{person.priezvisko}}
            </option>
          </select>
          <div class="error" *ngIf="osobaId.invalid && (osobaId.dirty || osobaId.touched)">
            Person selection is required
          </div>
        </div>
        
        <div class="form-group">
          <label for="vakcinaId">Vaccine *</label>
          <select 
            id="vakcinaId"
            [(ngModel)]="vaccination.vakcinaId" 
            name="vakcinaId" 
            required
            class="form-control"
            #vakcinaId="ngModel">
            <option [ngValue]="null">Select Vaccine</option>
            <option *ngFor="let vaccine of vaccines" [ngValue]="vaccine.id">
              {{vaccine.nazov}} ({{vaccine.typ}})
            </option>
          </select>
          <div class="error" *ngIf="vakcinaId.invalid && (vakcinaId.dirty || vakcinaId.touched)">
            Vaccine selection is required
          </div>
        </div>
        
        <div class="form-group">
          <label for="datumAplikacie">Application Date *</label>
          <input 
            id="datumAplikacie"
            [(ngModel)]="vaccination.datumAplikacie" 
            name="datumAplikacie" 
            type="date"
            required
            class="form-control"
            #datumAplikacie="ngModel">
          <div class="error" *ngIf="datumAplikacie.invalid && (datumAplikacie.dirty || datumAplikacie.touched)">
            Application date is required
          </div>
        </div>
        
        <div class="form-group">
          <label for="poradieDavky">Dose Number *</label>
          <input 
            id="poradieDavky"
            [(ngModel)]="vaccination.poradieDavky" 
            name="poradieDavky" 
            type="number" 
            min="1"
            required
            class="form-control"
            #poradieDavky="ngModel">
          <div class="error" *ngIf="poradieDavky.invalid && (poradieDavky.dirty || poradieDavky.touched)">
            Dose number is required and must be at least 1
          </div>
        </div>

        <button type="submit" [disabled]="!vaccinationForm.form.valid || loading" class="btn btn-primary">
          Register Vaccination
        </button>

        <div *ngIf="message" [class]="messageType">
          {{ message }}
        </div>

        <div *ngIf="loading" class="loading-indicator">
          Loading...
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
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .form-group { margin-bottom: 15px; }
    .form-control { 
      width: 100%;
      padding: 8px;
      border: 1px solid #ddd;
      border-radius: 4px;
      margin-top: 5px;
    }
    .error { color: red; font-size: 0.875em; margin-top: 5px; }
    .success { color: green; margin-top: 10px; }
    .failure { color: red; margin-top: 10px; }
    button { 
      width: 100%;
      padding: 10px;
      background: #007bff;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    button:disabled { 
      background: #cccccc;
      cursor: not-allowed;
    }
    label { font-weight: bold; }
    .loading-indicator {
      text-align: center;
      color: #007bff;
      margin-top: 10px;
    }
  `]
})
export class AddVaccinationComponent implements OnInit {
  vaccination: Vaccination = {
    osobaId: null,
    vakcinaId: null,
    datumAplikacie: new Date().toISOString().split('T')[0],
    poradieDavky: 1
  };
  
  people: Person[] = [];
  vaccines: Vaccine[] = [];
  message = '';
  messageType = '';
  loading = false;

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.loading = true;
    
    // Load people data
    this.apiService.getAllPersons().subscribe({
      next: (persons) => {
        this.people = persons;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading persons:', error);
        this.message = 'Error loading persons';
        this.messageType = 'failure';
        this.loading = false;
      }
    });

    // Load vaccines data
    this.apiService.getAllVaccines().subscribe({
      next: (vaccines) => {
        this.vaccines = vaccines;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading vaccines:', error);
        this.message = 'Error loading vaccines';
        this.messageType = 'failure';
        this.loading = false;
      }
    });
  }

  onSubmit() {
    // Reset message
    this.message = '';
    this.loading = true;
    
    // Validate data before sending
    if (!this.vaccination.osobaId || !this.vaccination.vakcinaId) {
      this.message = 'Please select both person and vaccine';
      this.messageType = 'failure';
      this.loading = false;
      return;
    }
    
    // Create a copy of the vaccination object to avoid mutating the original
    const vaccinationToSend = {
      ...this.vaccination,
      // Ensure date is properly formatted as YYYY-MM-DD string
      datumAplikacie: typeof this.vaccination.datumAplikacie === 'string' 
        ? this.vaccination.datumAplikacie 
        : new Date(this.vaccination.datumAplikacie).toISOString().split('T')[0]
    };
    
    console.log('Sending vaccination:', vaccinationToSend);
  
    this.apiService.registerVaccination(vaccinationToSend)
      .subscribe({
        next: (response) => {
          console.log('Vaccination registered successfully:', response);
          this.message = 'Vaccination registered successfully';
          this.messageType = 'success';
          this.resetForm();
          this.loading = false;
        },
        error: (error) => {
          console.error('Error registering vaccination:', error);
          this.message = 'Error registering vaccination: ' + 
                         (error.error?.message || error.message || 'Unknown error');
          this.messageType = 'failure';
          this.loading = false;
        }
      });
  }
  
  private resetForm() {
    this.vaccination = {
      osobaId: null,
      vakcinaId: null,
      datumAplikacie: new Date().toISOString().split('T')[0],
      poradieDavky: 1
    };
  }
}