import { RegistrationStatus } from "../enums/registration-status";

export interface RegistrationData {
  registration: string;
  status: RegistrationStatus;
  isApothecaryAvailable: boolean;
  isHerbalistAvailable: boolean;
}
