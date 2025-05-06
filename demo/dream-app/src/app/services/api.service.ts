import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Person, Vaccine, Vaccination } from '../models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  private handleError(error: HttpErrorResponse) {
    console.error('API Error:', error);
    
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      console.error('Client-side error:', error.error.message);
    } else {
      // Server-side error
      console.error(`Backend returned code ${error.status}, body was:`, error.error);
    }
    
    return throwError(() => new Error('Something went wrong with the API request. Please try again later.'));
  }

  // Person endpoints
  getAllPersons(): Observable<Person[]> {
    return this.http.get<Person[]>(`${this.apiUrl}/osoba/all`).pipe(
      tap(data => console.log('Fetched persons:', data)),
      catchError(this.handleError)
    );
  }

  addPerson(person: Person): Observable<any> {
    return this.http.post(`${this.apiUrl}/osoba/add`, person).pipe(
      tap(response => console.log('Person added:', response)),
      catchError(this.handleError)
    );
  }

  // Vaccine endpoints
  getAllVaccines(): Observable<Vaccine[]> {
    return this.http.get<Vaccine[]>(`${this.apiUrl}/vakcina/all`).pipe(
      tap(data => console.log('Fetched vaccines:', data)),
      catchError(this.handleError)
    );
  }

  addVaccine(vaccine: Vaccine): Observable<any> {
    return this.http.post(`${this.apiUrl}/vakcina/add`, vaccine).pipe(
      tap(response => console.log('Vaccine added:', response)),
      catchError(this.handleError)
    );
  }

  // Vaccination endpoints
  registerVaccination(vaccination: Vaccination): Observable<any> {
    return this.http.post(`${this.apiUrl}/osoba-vakcina/add`, vaccination).pipe(
      tap(response => console.log('Vaccination registered:', response)),
      catchError(this.handleError)
    );
  }

  getVaccinationsByPerson(personId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/osoba-vakcina/by-osoba/${personId}`).pipe(
      tap(data => console.log(`Fetched vaccinations for person ${personId}:`, data)),
      catchError(this.handleError)
    );
  }

  searchVaccinatedPersons(searchTerm: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/osoba-vakcina/search?term=${searchTerm}`).pipe(
      tap(data => console.log('Search results:', data)),
      catchError(this.handleError)
    );
  }
}