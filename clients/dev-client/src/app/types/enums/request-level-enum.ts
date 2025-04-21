export enum RequestLevelEnum {
  LOW = 1,
  LOW_TO_MEDIUM = 2,
  MEDIUM = 3,
  MEDIUM_TO_HIGH = 4,
  HIGH = 5
}

export const RequestLevelEnumNameMap: Record<RequestLevelEnum, string> = {
  [RequestLevelEnum.LOW]: 'LOW',
  [RequestLevelEnum.LOW_TO_MEDIUM]: 'LOW_TO_MEDIUM',
  [RequestLevelEnum.MEDIUM]: 'MEDIUM',
  [RequestLevelEnum.MEDIUM_TO_HIGH]: 'MEDIUM_TO_HIGH',
  [RequestLevelEnum.HIGH]: 'HIGH',
};
