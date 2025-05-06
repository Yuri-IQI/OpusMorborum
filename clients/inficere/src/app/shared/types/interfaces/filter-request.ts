import { FilterTypes } from "../enums/filter-types";

export interface FilterRequest {
    filterType: FilterTypes;
    filterValue: string
}