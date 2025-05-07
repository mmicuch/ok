import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { VaccinationRecord } from '../../models/interfaces';

@Component({
  selector: 'app-vaccinated-list',
  templateUrl: './vaccinated-list.component.html',
  styleUrls: ['./vaccinated-list.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class VaccinatedListComponent implements OnInit {
  records: VaccinationRecord[] = [];
  filteredRecords: VaccinationRecord[] = [];
  loading = false;
  error = '';
  sortBy = 'name'; // Default sort
  sortDirection = 'asc'; // Default direction
  vaccineFilter = ''; // For filtering by vaccine type

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.loadRecords();
  }
  
  loadRecords() {
    this.loading = true;
    this.error = '';
    
    // Use the correct API endpoint
    this.http.get<VaccinationRecord[]>(`${environment.apiUrl}/osobavakcina`)
      .subscribe({
        next: (data) => {
          this.records = data;
          this.filteredRecords = [...this.records]; // Initialize filtered records
          this.applySort(); // Apply default sorting
          this.loading = false;
          console.log('Loaded vaccination records:', data);
        },
        error: (err) => {
          this.error = `Error loading data: ${err.message}`;
          this.loading = false;
          console.error('Error loading vaccination records:', err);
        }
      });
  }
  
  // Change sorting
  changeSorting(column: string) {
    if (this.sortBy === column) {
      // Toggle direction if same column
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      // New column, default to ascending
      this.sortBy = column;
      this.sortDirection = 'asc';
    }
    
    this.applySort();
  }
  
  // Filter by vaccine
  applyVaccineFilter() {
    if (!this.vaccineFilter) {
      this.filteredRecords = [...this.records];
    } else {
      this.filteredRecords = this.records.filter(record => 
        record.vakcinaNazov.toLowerCase().includes(this.vaccineFilter.toLowerCase()) ||
        record.vakcinaTyp.toLowerCase().includes(this.vaccineFilter.toLowerCase())
      );
    }
    this.applySort();
  }
  
  // Reset filter
  resetFilter() {
    this.vaccineFilter = '';
    this.filteredRecords = [...this.records];
    this.applySort();
  }
  
  // Apply current sorting
  applySort() {
    const direction = this.sortDirection === 'asc' ? 1 : -1;
    
    this.filteredRecords.sort((a, b) => {
      switch(this.sortBy) {
        case 'name':
          return direction * (`${a.osobaPriezvisko} ${a.osobaMeno}`).localeCompare(`${b.osobaPriezvisko} ${b.osobaMeno}`);
        case 'vaccine':
          return direction * a.vakcinaNazov.localeCompare(b.vakcinaNazov);
        case 'date':
          return direction * (new Date(a.datumAplikacie).getTime() - new Date(b.datumAplikacie).getTime());
        case 'dose':
          return direction * (a.poradieDavky - b.poradieDavky);
        default:
          return 0;
      }
    });
  }
  
  // Get sort indicator for columns
  getSortIndicator(column: string): string {
    if (this.sortBy === column) {
      return this.sortDirection === 'asc' ? '↑' : '↓';
    }
    return '';
  }
}