import { HttpClient, HttpClientModule, provideHttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { environment } from '../../enviroment/environment';
import { Observable } from 'rxjs';
import { CustomerRequest } from '../../types/interfaces/customer-request';
import { CommonModule } from '@angular/common';
import { RequestsCreationComponent } from "../../components/requests-creation/requests-creation.component";
import { MenuType } from '../../types/enums/menu-type';
import { RequestsGalleryComponent } from '../../components/requests-gallery/requests-gallery.component';

@Component({
  selector: 'app-customer-request-management',
  standalone: true,
  imports: [CommonModule, RequestsCreationComponent, RequestsGalleryComponent],
  templateUrl: './customer-request-management.component.html',
  styleUrls: ['./customer-request-management.component.css']
})

export class CustomerRequestManagementComponent implements OnInit {
  voxAegriUrl = environment.VOX_AEGRI_URL;
  customerRequestList!: Observable<CustomerRequest[]>;
  MenuTypes = MenuType;

  menuStatusMap = new Map<MenuType, boolean>([
    [MenuType.QUEST, false],
    [MenuType.QUESTLINE, false]
  ]);

  menuList: MenuType[] = [...this.menuStatusMap.keys()];
  isVisualizerActive = false;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadCustomerRequests();
  }

  private loadCustomerRequests(): void {
    this.customerRequestList = this.http.get<CustomerRequest[]>(`${this.voxAegriUrl}/customer-request`);
  }

  toggleMenu(menu: MenuType): void {
    const currentStatus = this.menuStatusMap.get(menu) ?? false;
    this.menuStatusMap.set(menu, !currentStatus);
  }

  openGallery(): void {
    this.isVisualizerActive = !this.isVisualizerActive;
  }  
}