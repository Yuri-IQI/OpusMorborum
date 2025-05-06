import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { DisplayEnum } from '../../../../../shared/types/enums/display-enum';
import { FilterTypes } from '../../../../../shared/types/enums/filter-types';
import { FilterRequest } from '../../../../../shared/types/interfaces/filter-request';
import { RequestStatusEnum } from '../../../../../shared/types/enums/request-status-enum';
import { FilterOptionsComponent } from '../filter-options/filter-options.component';

@Component({
  selector: 'app-quest-menu',
  standalone: true,
  imports: [CommonModule, FilterOptionsComponent],
  templateUrl: './quest-menu.component.html',
  styleUrl: './quest-menu.component.css'
})
export class QuestMenuComponent {
  DisplayEnum = DisplayEnum;
  inMenuDisplay: DisplayEnum = DisplayEnum.DEFAULT;
  collapsableCallerClass: string = 'collapsable-caller';
  questMenuExpanded: boolean = false;
  currentFilter: FilterRequest = {filterType: FilterTypes.STATUS, filterValue: RequestStatusEnum.AVAILABLE};

  toggleRequestsMenu() {
    [this.questMenuExpanded, this.collapsableCallerClass] = [
      !this.questMenuExpanded, 
      this.questMenuExpanded ? 'inverted-collapsable-caller' : 'collapsable-caller'
    ];
  }

  selectFilter(filter: FilterTypes) {
    this.inMenuDisplay = DisplayEnum.FILTER;
    this.currentFilter.filterType = filter;
    this.currentFilter.filterValue = '';
  }
}
