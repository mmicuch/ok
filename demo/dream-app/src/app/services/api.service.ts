import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { Person, Vaccine, Vaccination } from '../models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private endpoints = {
    personAdd: '/osoby/add',
    personAll: '/osoby/all',

    vaccineAdd: '/vakcina/add',
    vaccineAll: '/vakcina/all',

    vaccination: '/osobavakcina/add'
  };

  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    }),
    responseType: 'text' as 'json'  // This helps with empty responses
  };

  constructor(private http: HttpClient) {}

  addPerson(person: Person): Observable<any> {
    return this.http.post(
      `${environment.apiUrl}${this.endpoints.personAdd}`, 
      person,
      this.httpOptions
    ).pipe(
      map(response => {
        // Handle empty response or parse JSON if there is content
        if (response && response !== '') {
          try {
            return JSON.parse(response as string);
          } catch (e) {
            return { message: 'Success' };
          }
        }
        return { message: 'Success' };
      }),
      catchError(error => {
        console.error('Error adding person:', error);
        return throwError(() => error);
      })
    );
  }

  getAllPersons(): Observable<Person[]> {
    return this.http.get<Person[]>(`${environment.apiUrl}${this.endpoints.personAll}`);
  }

  addVaccine(vaccine: Vaccine): Observable<any> {
    return this.http.post(
      `${environment.apiUrl}${this.endpoints.vaccineAdd}`, 
      vaccine,
      this.httpOptions
    ).pipe(
      map(response => {
        // Handle empty response or parse JSON if there is content
        if (response && response !== '') {
          try {
            return JSON.parse(response as string);
          } catch (e) {
            return { message: 'Success' };
          }
        }
        return { message: 'Success' };
      }),
      catchError(error => {
        console.error('Error adding vaccine:', error);
        return throwError(() => error);
      })
    );
  }

  getAllVaccines(): Observable<Vaccine[]> {
    return this.http.get<Vaccine[]>(`${environment.apiUrl}${this.endpoints.vaccineAll}`);
  }

  registerVaccination(vaccination: Vaccination): Observable<any> {
    // Format the date in YYYY-MM-DD format for Java's LocalDate
    const formattedVaccination = {
      ...vaccination,
      // Ensure the ID values are numbers, not strings
      osobaId: typeof vaccination.osobaId === 'string' ? parseInt(vaccination.osobaId, 10) : vaccination.osobaId,
      vakcinaId: typeof vaccination.vakcinaId === 'string' ? parseInt(vaccination.vakcinaId, 10) : vaccination.vakcinaId
    };
    
    console.log('Formatted vaccination payload:', formattedVaccination);
    
    return this.http.post(
      `${environment.apiUrl}/osobavakcina/add`,
      formattedVaccination,
      this.httpOptions
    ).pipe(
      map(response => {
        // Handle empty response or parse JSON if there is content
        if (response && response !== '') {
          try {
            return JSON.parse(response as string);
          } catch (e) {
            return { message: 'Success' };
          }
        }
        return { message: 'Success' };
      }),
      catchError(error => {
        console.error('Error registering vaccination:', error);
        return throwError(() => error);
      })
    );
  }
}