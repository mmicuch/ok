import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs';
import { catchError, tap, map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { Person, Vaccine, Vaccination } from '../models/interfaces';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = environment.apiUrl;
  
  constructor(private http: HttpClient) {}
  
  // Metóda na získanie token z localStorage
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token'); // Zmenené z 'authToken' na 'token'
    console.log('Retrieved token from localStorage:', token ? 'Token exists' : 'No token found');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  // GET request s autentifikáciou
  get<T>(endpoint: string): Observable<T> {
    return this.http.get<T>(`${environment.apiUrl}/${endpoint}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      tap((response: T) => console.log(`GET ${endpoint}:`, response)),
      catchError((error: any) => {
        console.error(`Error with endpoint ${endpoint}:`, error);
        return throwError(() => error);
      })
    );
  }

  // POST request s autentifikáciou
  post<T>(endpoint: string, data: any): Observable<T> {
    return this.http.post<T>(`${environment.apiUrl}/${endpoint}`, data, {
      headers: this.getAuthHeaders()
    }).pipe(
      tap((response: T) => console.log(`POST ${endpoint}:`, response)),
      catchError((error: any) => {
        console.error(`Error with endpoint ${endpoint}:`, error);
        return throwError(() => error);
      })
    );
  }
  
  // Get all persons
  getAllPersons(): Observable<Person[]> {
    return this.http.get<Person[]>(`${this.apiUrl}/osoby/all`, {
      headers: this.getAuthHeaders()
    });
  }
  
  // Add new person
  addPerson(person: Person): Observable<any> {
    return this.http.post<Person>(`${this.apiUrl}/osoby/add`, person, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((response: any) => ({ error: null, data: response })),
      catchError((error: any) => of({ error: error, endpoint: 'osoby/add' }))
    );
  }
  
  // Get all vaccines
  getAllVaccines(): Observable<Vaccine[]> {
    return this.get<Vaccine[]>('vakcina/all');
  }
  
  // Add new vaccine
  addVaccine(vaccine: Vaccine): Observable<any> {
    return this.post<Vaccine>('vakcina/add', vaccine);
  }

  // Pridajte chýbajúcu metódu
  getAllVaccinationRecords(): Observable<any[]> {
    return this.get<any[]>('osobavakcina');
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
  
  // Get notifications
  getNotifications(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/notifikacie/nadchadzajuce`);
  }
  
  // Get notifications for next N days
  getNotificationsForDays(days: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/notifikacie/nasledujucich-dni/${days}`);
  }
  
  // Get overdue notifications
  getOverdueNotifications(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/notifikacie/prekrocene`);
  }
  
  // Get notification statistics
  getNotificationStats(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/notifikacie/statistiky`);
  }

  // Register advanced vaccination
  registerAdvancedVaccination(vaccination: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/osobavakcina/advanced`, null, {
      params: vaccination
    });
  }
  
  // Add next dose
  addNextDose(doseData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/osobavakcina/dalsia-davka`, null, {
      params: doseData
    });
  }
  
  // Get incomplete vaccinations
  getIncompleteVaccinations(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/osobavakcina/nedokoncene`);
  }

  // Delete person
  deletePerson(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/osoby/remove/${id}`);
  }
  
  // Delete vaccine
  deleteVaccine(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/vakcina/${id}`);
  }
  
  // Update person
  updatePerson(id: number, person: Person): Observable<any> {
    return this.http.put(`${this.apiUrl}/osoby/${id}`, person);
  }
  
  // Update vaccine
  updateVaccine(id: number, vaccine: Vaccine): Observable<any> {
    return this.http.put(`${this.apiUrl}/vakcina/${id}`, vaccine);
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