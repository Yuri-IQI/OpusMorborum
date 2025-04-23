import { Component, Input } from '@angular/core';
import { CustomerRequest } from '../../types/interfaces/customer-request';
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-requests-grid',
  imports: [CommonModule],
  templateUrl: './requests-grid.component.html',
  styleUrl: './requests-grid.component.css'
})
export class RequestsGridComponent {
  @Input() customerRequestList!: Observable<CustomerRequest[]>;
}
