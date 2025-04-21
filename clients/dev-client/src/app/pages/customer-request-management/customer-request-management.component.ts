import { HttpClient, HttpClientModule, provideHttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { environment } from '../../enviroment/environment';
import { Observable } from 'rxjs';
import { CustomerRequest } from '../../types/interfaces/customer-request';
import { CommonModule } from '@angular/common';
import { RequestsCreationComponent } from "../../components/requests-creation/requests-creation.component";

@Component({
  selector: 'app-customer-request-management',
  standalone: true,
  imports: [CommonModule, RequestsCreationComponent],
  templateUrl: './customer-request-management.component.html',
  styleUrls: ['./customer-request-management.component.css']
})
export class CustomerRequestManagementComponent {
  voxAegriUrl = environment.VOX_AEGRI_URL;
  customerRequestList!: Observable<CustomerRequest[]>;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.getCustomerRequestList();
  }

  private getCustomerRequestList() {
    this.customerRequestList = this.http.get<CustomerRequest[]>(`${this.voxAegriUrl}/customer-request`);
  }
}
