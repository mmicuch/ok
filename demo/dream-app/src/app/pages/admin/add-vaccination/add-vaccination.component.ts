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
    <h2>Register Vaccination</h2>
    <form (ngSubmit)="onSubmit()">
      <div>
        <select [(ngModel)]="vaccination.osobaId" name="osobaId" required>
          <option value="">Select Person</option>
          <option *ngFor="let person of people" [value]="person.id">
            {{person.meno}} {{person.priezvisko}}
          </option>
        </select>
      </div>
      <div>
        <select [(ngModel)]="vaccination.vakcinaId" name="vakcinaId" required>
          <option value="">Select Vaccine</option>
          <option *ngFor="let vaccine of vaccines" [value]="vaccine.id">
            {{vaccine.nazov}} ({{vaccine.typ}})
          </option>
        </select>
      </div>
      <div>
        <input [(ngModel)]="vaccination.datumAplikacie" name="datumAplikacie" type="date" required>
      </div>
      <div>
        <input [(ngModel)]="vaccination.poradieDavky" name="poradieDavky" type="number" min="1" placeholder="Dose Number" required>
      </div>
      <button type="submit">Register Vaccination</button>
      <div *ngIf="message">{{ message }}</div>
    </form>
  `,
  styles: [`
    form { max-width: 400px; margin: 20px auto; }
    select, input { width: 100%; margin: 10px 0; padding: 8px; }
    button { width: 100%; padding: 10px; margin-top: 10px; }
  `]
})
export class AddVaccinationComponent implements OnInit {
  vaccination: Vaccination = {
    osobaId: 0,
    vakcinaId: 0,
    datumAplikacie: '',
    poradieDavky: 1
  };
  people: Person[] = [];
  vaccines: Vaccine[] = [];
  message = '';

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.apiService.getAllPersons().subscribe(
      persons => this.people = persons,
      error => console.error('Error loading persons:', error)
    );

    this.apiService.getAllVaccines().subscribe(
      vaccines => this.vaccines = vaccines,
      error => console.error('Error loading vaccines:', error)
    );
  }

  onSubmit() {
    const vaccinationPayload = {
      osobaId: this.vaccination.osobaId,
      vakcinaId: this.vaccination.vakcinaId,
      datumAplikacie: this.vaccination.datumAplikacie,
      poradieDavky: this.vaccination.poradieDavky
    };
  
    console.log('Sending vaccination:', vaccinationPayload);  // Log the payload being sent
  
    this.apiService.registerVaccination(vaccinationPayload)
      .subscribe({
        next: () => {
          this.message = 'Vaccination registered successfully';
          this.vaccination = {
            osobaId: 0,
            vakcinaId: 0,
            datumAplikacie: '',
            poradieDavky: 1
          };
        },
        error: () => this.message = 'Error registering vaccination'
      });
  }
  
  
  
}
