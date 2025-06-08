import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { VaccinationRecord } from '../../models/interfaces';
import { Vaccine } from '../../models/interfaces';
import { Subject, debounceTime, distinctUntilChanged, takeUntil } from 'rxjs';
import { ApiService } from '../../services/api.service';

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

  constructor(private http: HttpClient, private apiService: ApiService) {}

  ngOnInit() {
    // Load all vaccines for the filter dropdown
    this.apiService.getAllVaccines().subscribe({
      next: (data) => {
        this.vaccines = data;
        console.log('Loaded vaccines:', data);
      },
      error: (err) => {
        console.error('Error loading vaccines:', err);
      }
    });
      
    // Load all vaccination records to enable quick filtering
    this.loadRecords();
      
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

  // Handle input changes properly
  onSearchInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchTerms.next(input.value);
  }

  loadRecords() {
    this.loading = true;
    this.error = '';
    
    this.apiService.getAllVaccinationRecords()
      .subscribe({
        next: (data: any[]) => {
          this.allRecords = data.map(record => ({
            ...record,
            // Mapovanie pre backward compatibility
            osobaMeno: record.menoOsoby,
            osobaPriezvisko: record.priezviskoOsoby,
            vakcinaNazov: record.nazovVakciny,
            vakcinaTyp: record.vakcinaTyp || 'N/A',
            datumAplikacie: record.datumVakciny,
            poradieDavky: record.aktualnaDavka
          }));
          this.results = [...this.allRecords];
          this.loading = false;
          console.log('Loaded vaccination records:', this.allRecords);
        },
        error: (err: any) => {
          this.error = `Error loading data: ${err.message}`;
          this.loading = false;
          console.error('Error loading vaccination records:', err);
        }
      });
  }

  search() {
    this.loading = true;
    this.error = '';
    
    if (!this.searchTerm.trim()) {
      // If no search term, show all records or apply just the vaccine filter
      if (this.vaccineFilter) {
        this.results = this.allRecords.filter(record => 
          record.nazovVakciny.toLowerCase() === this.vaccineFilter.toLowerCase()
        );
      } else {
        this.results = [...this.allRecords];
      }
      this.sortResults();
      this.loading = false;
      return;
    }
    
    // Filter locally based on the search term
    const lowercaseSearchTerm = this.searchTerm.toLowerCase();
    this.results = this.allRecords.filter(record => {
      const nameMatch = `${record.menoOsoby} ${record.priezviskoOsoby}`.toLowerCase().includes(lowercaseSearchTerm);
      const vaccineMatch = record.nazovVakciny.toLowerCase().includes(lowercaseSearchTerm);
      return nameMatch || vaccineMatch;
    });
    
    // Apply vaccine filter if needed
    if (this.vaccineFilter) {
      this.results = this.results.filter(record => 
        record.nazovVakciny.toLowerCase() === this.vaccineFilter.toLowerCase()
      );
    }
    
    this.sortResults();
    this.loading = false;
  }
  
  sortResults() {
    switch(this.sortBy) {
      case 'name':
        this.results.sort((a, b) => 
          (a.priezviskoOsoby + a.menoOsoby).localeCompare(b.priezviskoOsoby + b.menoOsoby)
        );
        break;
      case 'nameDesc':
        this.results.sort((a, b) => 
          (b.priezviskoOsoby + b.menoOsoby).localeCompare(a.priezviskoOsoby + a.menoOsoby)
        );
        break;
      case 'date':
        this.results.sort((a, b) => 
          new Date(b.datumVakciny).getTime() - new Date(a.datumVakciny).getTime()
        );
        break;
      case 'dateAsc':
        this.results.sort((a, b) => 
          new Date(a.datumVakciny).getTime() - new Date(b.datumVakciny).getTime()
        );
        break;
      case 'dose':
        this.results.sort((a, b) => b.aktualnaDavka - a.aktualnaDavka);
        break;
    }
  }

  getUniqueVaccines(): string[] {
    const vaccines = this.allRecords.map(record => record.nazovVakciny);
    return [...new Set(vaccines)].sort();
  }
}