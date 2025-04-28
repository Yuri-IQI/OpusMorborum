import { Component } from '@angular/core';
import { Role } from '../../../shared/types/enums/role';
import { RegistrationData } from '../../../shared/types/interfaces/registration-data';
import { RegistrationStatus } from '../../../shared/types/enums/registration-status';
import { environment } from '../../../shared/environment/environment';
import { HttpClient, provideHttpClient } from '@angular/common/http';
import { RegistrationService } from '../../services/registration-service.service';
import { RoleAssignment } from '../../../shared/types/interfaces/role-assignment';
import { Router } from '@angular/router';

@Component({
  selector: 'app-start-page',
  imports: [],
  standalone: true,
  templateUrl: './start-page.component.html',
  styleUrl: './start-page.component.css'
})
export class StartPageComponent {
  Role = Role;
  RegistrationStatus = RegistrationStatus;
  registrationData: RegistrationData | null = null;
  apiUrl: string = environment.apiUrl;

  constructor(private http: HttpClient, private registrationService: RegistrationService, private router: Router) {}

  registerCodes(apothecaryCode: string, herbalistCode: string) {
    const accessCodes = { apothecaryCode, herbalistCode };

    if (!apothecaryCode.trim() || !herbalistCode.trim()) {
      let emptyCode = !apothecaryCode.trim() ? 'Apothecary' : 'Herbalist';
      alert(emptyCode + ' code was not supplied');
      return;
    } else {
      this.http.post<RegistrationData>(this.apiUrl + '/sole/access', accessCodes)
        .subscribe({
          next: (response) => {
            this.registrationData = response;
            this.registrationService.setRegistrationData(response);
          },
          error: (error) => {
            console.error('Error creating registration:', error);
          }
        })
    }
  }

  start(selectedRole: Role) {
    let roleSelection = {
      selectedRole: selectedRole,
      registration: this.registrationService.registrationData.registration
    };

    this.http.post<RoleAssignment>(this.apiUrl + '/sole/access/roles', roleSelection)
    .subscribe({
      next: (response) => {
        if (selectedRole.match("APOTHECARY")) {
          this.router.navigate(['/apothecary'], {
            state: { registrationData: this.registrationData }
          });

        } else if (selectedRole.match("HERBALIST")) {
          this.router.navigate(['/herbalist'])
        }
      }
    });
  }
}
