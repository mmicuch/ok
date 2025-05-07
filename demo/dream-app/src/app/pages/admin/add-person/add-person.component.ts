import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../../services/api.service';
import { Person } from '../../../models/interfaces';

@Component({
  selector: 'app-add-person',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="form-container">
      <h2>Pridať novú osobu</h2>
      <form (ngSubmit)="onSubmit()" #personForm="ngForm">
        <div class="form-group">
          <label for="meno">Meno *</label>
          <input 
            type="text"
            id="meno"
            [(ngModel)]="person.meno" 
            name="meno" 
            required
            class="form-control"
            #meno="ngModel">
          <div class="error" *ngIf="meno.invalid && (meno.dirty || meno.touched || formSubmitted)">
            Meno je povinné
          </div>
        </div>

        <div class="form-group">
          <label for="priezvisko">Priezvisko *</label>
          <input 
            type="text"
            id="priezvisko"
            [(ngModel)]="person.priezvisko" 
            name="priezvisko" 
            required
            class="form-control"
            #priezvisko="ngModel">
          <div class="error" *ngIf="priezvisko.invalid && (priezvisko.dirty || priezvisko.touched || formSubmitted)">
            Priezvisko je povinné
          </div>
        </div>

        <div class="form-group">
          <label for="datumNarodenia">Dátum narodenia *</label>
          <input 
            id="datumNarodenia"
            [(ngModel)]="person.datumNarodenia" 
            name="datumNarodenia" 
            type="date"
            required
            class="form-control"
            #datumNarodenia="ngModel">
          <div class="error" *ngIf="datumNarodenia.invalid && (datumNarodenia.dirty || datumNarodenia.touched || formSubmitted)">
            Dátum narodenia je povinný
          </div>
        </div>

        <button type="submit" [disabled]="loading" class="btn btn-primary">
          Pridať osobu
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
export class AddPersonComponent {
  person: Person = {
    meno: '',
    priezvisko: '',
    datumNarodenia: new Date().toISOString().split('T')[0]
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
    if (!this.person.meno || !this.person.priezvisko || !this.person.datumNarodenia) {
      this.message = 'Prosím, vyplňte všetky povinné polia';
      this.messageType = 'failure';
      return;
    }
    
    this.loading = true;
    console.log('Odosielam osobu:', this.person);
    
    this.apiService.addPerson(this.person).subscribe({
      next: (response) => {
        if (response.error) {
          console.error(`Error with endpoint ${response.endpoint}:`, response.error);
          this.message = `Chyba pri pridávaní osoby: ${response.error.status || response.error.message || 'Unknown error'}`;
          this.messageType = 'failure';
        } else {
          this.message = 'Osoba bola úspešne pridaná';
          this.messageType = 'success';
          this.resetForm();
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error adding person:', error);
        this.message = `Chyba pri pridávaní osoby: ${error.message || error.status || 'Unknown error'}`;
        this.messageType = 'failure';
        this.loading = false;
      }
    });
  }

  private resetForm() {
    this.person = {
      meno: '',
      priezvisko: '',
      datumNarodenia: new Date().toISOString().split('T')[0]
    };
    this.formSubmitted = false;
  }
}