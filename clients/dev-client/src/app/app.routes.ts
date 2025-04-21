import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerRequestManagementComponent } from './pages/customer-request-management/customer-request-management.component';

export const routes: Routes = [
  { path: 'customer-request-management', component: CustomerRequestManagementComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutes { }
