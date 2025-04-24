import { RequestStatusEnum } from "../enums/request-status-enum";
import { CustomerRequest } from "./customer-request";

export interface RequestStatus {
    id: number;
    quest: CustomerRequest;
    requestStatus: RequestStatusEnum;
}