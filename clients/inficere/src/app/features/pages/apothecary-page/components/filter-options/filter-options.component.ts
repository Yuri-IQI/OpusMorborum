import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatTooltipModule } from '@angular/material/tooltip';
import { FilterTypes } from '../../../../../shared/types/enums/filter-types';
import { FilterRequest } from '../../../../../shared/types/interfaces/filter-request';
import { RequestStatusEnum } from '../../../../../shared/types/enums/request-status-enum';

@Component({
  selector: 'app-filter-options',
  standalone: true,
  imports: [CommonModule, MatTooltipModule],
  templateUrl: './filter-options.component.html',
  styleUrl: './filter-options.component.css'
})
export class FilterOptionsComponent {
  @Output() selectedFilter = new EventEmitter<FilterTypes>();
  @Input() currentFilter: FilterRequest | null = null;
  filterMap: Map<FilterTypes, string> = new Map([
    [FilterTypes.TITLE, 'Title'],
    [FilterTypes.LEVEL, 'Level'],
    [FilterTypes.BASE_REWARD, 'Reward'],
    [FilterTypes.AUTHOR, 'Author'],
    [FilterTypes.STATUS, 'Status']
  ]);
  filterList: FilterTypes[] = [...this.filterMap.keys()];

  ngOnInit() {
    if (!this.currentFilter) {
      this.currentFilter = {
        filterType: FilterTypes.STATUS,
        filterValue: RequestStatusEnum.AVAILABLE
      }
    }
  }

  selectFilter(filter: FilterTypes) {
    this.selectedFilter.emit(filter);
  }
}
