import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerRequestManagementComponent } from './customer-request-management.component';

describe('CustomerRequestManagementComponent', () => {
  let component: CustomerRequestManagementComponent;
  let fixture: ComponentFixture<CustomerRequestManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomerRequestManagementComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerRequestManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
