import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { VaccinationRecord } from '../../models/interfaces';

@Component({
  selector: 'app-vaccinated-search',
  templateUrl: './vaccinated-search.component.html',
  styleUrls: ['./vaccinated-search.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class VaccinatedSearchComponent {
  searchTerm = '';
  results: VaccinationRecord[] = [];
  loading = false;
  error = '';

  constructor(private http: HttpClient) {}

  search() {
    if (!this.searchTerm.trim()) return;
    
    this.loading = true;
    this.error = '';
    
    // Use the correct endpoint and add error handling
    this.http.get<VaccinationRecord[]>(`${environment.apiUrl}/osobavakcina/search?query=${encodeURIComponent(this.searchTerm)}`)
      .subscribe({
        next: (data) => {
          this.results = data;
          this.loading = false;
          console.log('Search results:', data);
        },
        error: (err) => {
          this.error = `Error searching: ${err.message}`;
          this.loading = false;
          console.error('Error while searching:', err);
        }
      });
  }
}