import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
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
  
  // Get all vaccines
  getAllVaccines(): Observable<Vaccine[]> {
    return this.http.get<Vaccine[]>(`${this.apiUrl}/vakcina/all`);
  }
  
  // Register vaccination - Fixed version
  registerVaccination(vaccination: Vaccination): Observable<any> {
    // Make sure date is formatted correctly
    const payload = {
      ...vaccination,
      // If date is a string (yyyy-MM-dd format), keep it as is
      // Otherwise ensure it's properly formatted
      datumAplikacie: typeof vaccination.datumAplikacie === 'string' ? 
        vaccination.datumAplikacie : 
        this.formatDate(vaccination.datumAplikacie)
    };
    
    // Debug log
    console.log('Sending payload to API:', payload);
    
    // Use the correct endpoint from the backend
    return this.http.post(`${this.apiUrl}/osobavakcina/add`, payload);
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