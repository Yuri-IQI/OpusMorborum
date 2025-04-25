import { Component, Input } from '@angular/core';
import { CustomerRequest } from '../../types/interfaces/customer-request';
import { map, Observable } from 'rxjs';
import { CommonModule } from '@angular/common';
import { QuestCardComponent } from "../quest-card/quest-card.component";
import { VisibilityEnum } from '../../types/enums/visibility-enum';

@Component({
  selector: 'app-requests-grid',
  standalone: true,
  imports: [CommonModule, QuestCardComponent],
  templateUrl: './requests-grid.component.html',
  styleUrls: ['./requests-grid.component.css']
})
export class RequestsGridComponent {
  @Input() customerRequestList!: Observable<CustomerRequest[]>;
  visibilityEnum: VisibilityEnum = VisibilityEnum.UNASSIGNED;
  gridLimit: number = 12;
  currentPage: number = 1;

  pagedRequests$: Observable<CustomerRequest[]> = new Observable();
  totalPages$: Observable<number> = new Observable();
  
  ngOnChanges() {
    this.updatePagedData();
  }

  goToPage(page: number) {
    this.currentPage = page;
    this.updatePagedData();
  }

  private updatePagedData() {
    if (this.customerRequestList) {
      this.pagedRequests$ = this.customerRequestList.pipe(
        map(requests => {
          const start = (this.currentPage - 1) * this.gridLimit;
          const end = start + this.gridLimit;
          return requests.slice(start, end);
        })
      );

      this.totalPages$ = this.customerRequestList.pipe(
        map(requests => Math.ceil(requests.length / this.gridLimit))
      );
    }
  }
}
