import { Component, Input } from '@angular/core';
import { CustomerRequest } from '../../types/interfaces/customer-request';
import { Observable } from 'rxjs';
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
}
