import { RequestLevelEnum } from "../enums/request-level-enum";

export interface CustomerRequest {
  customerRequestId: number;
  title: string;
  description: string;
  level: RequestLevelEnum;
  baseReward: number;
  isActive: boolean;
  author: string;
}
