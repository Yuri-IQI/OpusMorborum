import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CustomerRequest } from '../../types/interfaces/customer-request';
import { CommonModule } from '@angular/common';
import { VisibilityEnum } from '../../types/enums/visibility-enum';
import { RequestStatus } from '../../types/interfaces/request-status';
import { RequestLevelEnum, RequestLevelEnumNameMap } from '../../types/enums/request-level-enum';
import { EventBusService } from '../../services/event-bus.service';

@Component({
  selector: 'app-quest-card',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './quest-card.component.html',
  styleUrl: './quest-card.component.css'
})
export class QuestCardComponent {
  @Input() quest: CustomerRequest | null = null;
  @Input() questStatus: RequestStatus | null = null;
  @Input() visibility: VisibilityEnum | null = 0;
  @Output() editQuest = new EventEmitter<CustomerRequest>();
  questLevelIconMap: Map<string, string> = new Map([
    ['LOW', '/one.svg'],
    ['LOW_TO_MEDIUM', '/two.svg'],
    ['MEDIUM', '/three.svg'],
    ['MEDIUM_TO_HIGH', '/four.svg'],
    ['HIGH', '/five.svg']
  ]);
  RequestLevelEnumNameMap = RequestLevelEnumNameMap;

  constructor(private eventBus: EventBusService) {}

  send() {
    this.eventBus.emitEvent();
  }
}