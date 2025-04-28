import { Injectable } from '@angular/core';
import { RegistrationData } from '../../shared/types/interfaces/registration-data';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {
  registrationData!: RegistrationData;

  setRegistrationData(data: RegistrationData): void {
    this.registrationData = data;
  }

  hasRegistration(): boolean {
    return this.registrationData.registration.length > 0;
  }
}
