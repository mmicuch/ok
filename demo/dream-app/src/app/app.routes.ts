import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { VaccinatedListComponent } from './pages/vaccinated-list/vaccinated-list.component';
import { VaccinatedSearchComponent } from './pages/vaccinated-search/vaccinated-search.component';
import { AddPersonComponent } from './pages/admin/add-person/add-person.component';
import { AddVaccineComponent } from './pages/admin/add-vaccine/add-vaccine.component';
import { AddVaccinationComponent } from './pages/admin/add-vaccination/add-vaccination.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'vaccinated', component: VaccinatedListComponent },
  { path: 'search', component: VaccinatedSearchComponent },
  { 
    path: 'admin', 
    canActivate: [AuthGuard],
    children: [
      { path: 'add-person', component: AddPersonComponent },
      { path: 'add-vaccine', component: AddVaccineComponent },
      { path: 'add-vaccination', component: AddVaccinationComponent }
    ]
  }
];