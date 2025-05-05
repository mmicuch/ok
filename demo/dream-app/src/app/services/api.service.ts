import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
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

  constructor(private http: HttpClient) {}

  addPerson(person: Person): Observable<any> {
    return this.http.post(`${environment.apiUrl}${this.endpoints.personAdd}`, person)
      .pipe(
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
    return this.http.post<any>(`${environment.apiUrl}${this.endpoints.vaccineAdd}`, vaccine)
      .pipe(
        catchError(error => {
          console.error('Error adding vaccine:', error);
          return throwError(() => error);
        })
      );
  }

  getAllVaccines(): Observable<Vaccine[]> {
    return this.http.get<Vaccine[]>(`${environment.apiUrl}${this.endpoints.vaccineAll}`);
  }

  registerVaccination(vaccinationPayload: any): Observable<any> {
    return this.http.post(`${environment.apiUrl}/osobavakcina/add`, vaccinationPayload)
      .pipe(
        catchError(error => {
          console.error('Error registering vaccination:', error);
          return throwError(() => error);
        })
      );
  }
  
  
}
