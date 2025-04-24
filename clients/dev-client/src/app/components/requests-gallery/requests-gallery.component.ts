import { Component, EventEmitter, Input, Output } from '@angular/core';
import { RequestsGridComponent } from '../requests-grid/requests-grid.component';
import { CustomerRequest } from '../../types/interfaces/customer-request';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-requests-gallery',
  imports: [RequestsGridComponent],
  standalone: true,
  templateUrl: './requests-gallery.component.html',
  styleUrl: './requests-gallery.component.css'
})

export class RequestsGalleryComponent {
  @Output() close = new EventEmitter<void>();
  @Input() customerRequestList!: Observable<CustomerRequest[]>;

  putDownCover(): void {
    this.close.emit();
  }
}