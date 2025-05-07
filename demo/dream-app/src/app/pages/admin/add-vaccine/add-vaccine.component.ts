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
      <h2>Pridať novú vakcínu</h2>
      <form (ngSubmit)="onSubmit()" #vaccineForm="ngForm">
        <div class="form-group">
          <label for="nazov">Názov vakcíny *</label>
          <input 
            type="text"
            id="nazov"
            [(ngModel)]="vaccine.nazov" 
            name="nazov" 
            required
            class="form-control"
            #nazov="ngModel">
          <div class="error" *ngIf="nazov.invalid && (nazov.dirty || nazov.touched || formSubmitted)">
            Názov je povinný
          </div>
        </div>

        <div class="form-group">
          <label for="typ">Typ vakcíny *</label>
          <select 
            id="typ"
            [(ngModel)]="vaccine.typ" 
            name="typ" 
            required
            class="form-control"
            #typ="ngModel">
            <option value="">Vyberte typ vakcíny</option>
            <option value="mRNA">mRNA</option>
            <option value="vektorová">Vektorová</option>
            <option value="proteínová">Proteínová</option>
            <option value="iná">Iná</option>
          </select>
          <div class="error" *ngIf="typ.invalid && (typ.dirty || typ.touched || formSubmitted)">
            Typ je povinný
          </div>
        </div>

        <div class="form-group">
          <label for="vyrobca">Výrobca *</label>
          <input 
            type="text"
            id="vyrobca"
            [(ngModel)]="vaccine.vyrobca" 
            name="vyrobca" 
            required
            class="form-control"
            #vyrobca="ngModel">
          <div class="error" *ngIf="vyrobca.invalid && (vyrobca.dirty || vyrobca.touched || formSubmitted)">
            Výrobca je povinný
          </div>
        </div>

        <button type="submit" [disabled]="loading" class="btn btn-primary">
          Pridať vakcínu
        </button>

        <div *ngIf="message" [class]="messageType">
          {{ message }}
        </div>
        
        <div *ngIf="loading" class="loading-indicator">
          Načítavam...
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
      this.message = 'Prosím, vyplňte všetky povinné polia';
      this.messageType = 'failure';
      return;
    }
    
    this.loading = true;
    console.log('Sending vaccine:', this.vaccine);
  
    this.apiService.addVaccine(this.vaccine).subscribe({
      next: (response) => {
        if (response.error) {
          console.error(`Error with endpoint ${response.endpoint}:`, response.error);
          this.message = `Chyba pri pridávaní vakcíny: ${response.error.status || response.error.message || 'Unknown error'}`;
          this.messageType = 'failure';
        } else {
          this.message = 'Vakcína bola úspešne pridaná';
          this.messageType = 'success';
          this.resetForm();
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error adding vaccine:', error);
        this.message = `Chyba pri pridávaní vakcíny: ${error.message || error.status || 'Unknown error'}`;
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