import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, map, of } from 'rxjs';
import { environment } from '../../environments/environment';
import { Person, Vaccine, Vaccination } from '../models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = environment.apiUrl;
  
  constructor(private http: HttpClient) {}
  
  // Get all persons
  getAllPersons(): Observable<Person[]> {
    return this.http.get<Person[]>(`${this.apiUrl}/osoby/all`);
  }
  
  // Add new person
  addPerson(person: Person): Observable<any> {
    return this.http.post<Person>(`${this.apiUrl}/osoby/add`, person)
      .pipe(
        map(response => ({ error: null, data: response })),
        catchError(error => of({ error: error, endpoint: 'osoby/add' }))
      );
  }
  
  // Get all vaccines
  getAllVaccines(): Observable<Vaccine[]> {
    return this.http.get<Vaccine[]>(`${this.apiUrl}/vakcina/all`);
  }
  
  // Add new vaccine
  addVaccine(vaccine: Vaccine): Observable<any> {
    return this.http.post<Vaccine>(`${this.apiUrl}/vakcina/add`, vaccine)
      .pipe(
        map(response => ({ error: null, data: response })),
        catchError(error => of({ error: error, endpoint: 'vakcina/add' }))
      );
  }
  
  // Register vaccination
  registerVaccination(vaccination: Vaccination): Observable<any> {
    // Create a clean payload ensuring it matches the backend expectations
    const payload = {
      osobaId: vaccination.osobaId,
      vakcinaId: vaccination.vakcinaId,
      datumAplikacie: typeof vaccination.datumAplikacie === 'string' 
        ? vaccination.datumAplikacie 
        : this.formatDate(vaccination.datumAplikacie),
      poradieDavky: vaccination.poradieDavky
    };
    
    // Debug log
    console.log('Sending payload to API:', payload);
    
    // Use the correct endpoint from the backend
    return this.http.post(`${this.apiUrl}/osobavakcina/add`, payload);
  }
  
  // Search for vaccination records
  searchVaccinationRecords(query: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/osobavakcina/search?query=${encodeURIComponent(query)}`);
  }
  
  // Helper method to format date as YYYY-MM-DD
  private formatDate(date: Date): string {
    const d = new Date(date);
    const year = d.getFullYear();
    const month = ('0' + (d.getMonth() + 1)).slice(-2);
    const day = ('0' + d.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
  }
}