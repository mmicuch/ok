import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { VaccinationRecord } from '../../models/interfaces';
import { Vaccine } from '../../models/interfaces';
import { Subject, debounceTime, distinctUntilChanged, takeUntil } from 'rxjs';

@Component({
  selector: 'app-vaccinated-search',
  templateUrl: './vaccinated-search.component.html',
  styleUrls: ['./vaccinated-search.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class VaccinatedSearchComponent implements OnInit, OnDestroy {
  searchTerm = '';
  vaccineFilter = '';
  sortBy = 'name';
  results: VaccinationRecord[] = [];
  filteredResults: VaccinationRecord[] = [];
  allRecords: VaccinationRecord[] = [];
  loading = false;
  error = '';
  vaccines: Vaccine[] = [];
  
  // Add these for reactive search
  private searchTerms = new Subject<string>();
  private destroy$ = new Subject<void>();

  constructor(private http: HttpClient) {}

  ngOnInit() {
    // Load all vaccines for the filter dropdown
    this.http.get<Vaccine[]>(`${environment.apiUrl}/vakcina/all`)
      .subscribe({
        next: (data) => {
          this.vaccines = data;
        },
        error: (err) => {
          console.error('Error loading vaccines:', err);
        }
      });
      
    // Load all vaccination records to enable quick filtering
    this.http.get<VaccinationRecord[]>(`${environment.apiUrl}/osobavakcina`)
      .subscribe({
        next: (data) => {
          this.allRecords = data;
        },
        error: (err) => {
          console.error('Error loading vaccination records:', err);
        }
      });
      
    // Set up reactive search
    this.searchTerms.pipe(
      takeUntil(this.destroy$),
      debounceTime(300), // Wait for 300ms pause in events
      distinctUntilChanged() // Only emit if value is different from previous
    ).subscribe(term => {
      this.searchTerm = term;
      this.search();
    });
  }
  
  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // Update this method to handle input changes
  onSearchInput(term: string) {
    this.searchTerms.next(term);
  }

  search() {
    this.loading = true;
    this.error = '';
    
    if (!this.searchTerm.trim() && !this.vaccineFilter) {
      // If no search term and no filter, show all records
      this.results = [...this.allRecords];
      this.sortResults();
      this.loading = false;
      return;
    }
    
    if (this.searchTerm.trim() === '' && this.vaccineFilter) {
      // If only vaccine filter is specified, filter locally
      this.results = this.allRecords.filter(record => 
        record.vakcinaNazov.toLowerCase() === this.vaccineFilter.toLowerCase()
      );
      this.sortResults();
      this.loading = false;
      return;
    }
    
    // Use the search endpoint with the query
    this.http.get<VaccinationRecord[]>(`${environment.apiUrl}/osobavakcina/search?query=${encodeURIComponent(this.searchTerm)}`)
      .subscribe({
        next: (data) => {
          // Apply vaccine filter if needed
          if (this.vaccineFilter) {
            this.results = data.filter(record => 
              record.vakcinaNazov.toLowerCase() === this.vaccineFilter.toLowerCase()
            );
          } else {
            this.results = data;
          }
          
          this.sortResults();
          this.loading = false;
        },
        error: (err) => {
          this.error = `Error searching: ${err.message}`;
          this.loading = false;
          console.error('Error while searching:', err);
        }
      });
  }
  
  sortResults() {
    switch(this.sortBy) {
      case 'name':
        this.results.sort((a, b) => 
          (a.osobaPriezvisko + a.osobaMeno).localeCompare(b.osobaPriezvisko + b.osobaMeno)
        );
        break;
      case 'nameDesc':
        this.results.sort((a, b) => 
          (b.osobaPriezvisko + b.osobaMeno).localeCompare(a.osobaPriezvisko + a.osobaMeno)
        );
        break;
      case 'date':
        this.results.sort((a, b) => 
          new Date(b.datumAplikacie).getTime() - new Date(a.datumAplikacie).getTime()
        );
        break;
      case 'dateAsc':
        this.results.sort((a, b) => 
          new Date(a.datumAplikacie).getTime() - new Date(b.datumAplikacie).getTime()
        );
        break;
      case 'dose':
        this.results.sort((a, b) => b.poradieDavky - a.poradieDavky);
        break;
    }
  }
}