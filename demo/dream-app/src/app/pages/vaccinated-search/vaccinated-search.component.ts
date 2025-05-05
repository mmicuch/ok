import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-vaccinated-search',
  templateUrl: './vaccinated-search.component.html',
  styleUrls: ['./vaccinated-search.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class VaccinatedSearchComponent {
  searchTerm = '';
  results: any[] = [];

  constructor(private http: HttpClient) {}

  search() {
    if (!this.searchTerm.trim()) return;
    this.http.get<any[]>(`${environment.apiUrl}/osobavakciny/search?query=${encodeURIComponent(this.searchTerm)}`)
      .subscribe(data => this.results = data);
  }
}
