import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestsCreationComponent } from './requests-creation.component';

describe('RequestsCreationComponent', () => {
  let component: RequestsCreationComponent;
  let fixture: ComponentFixture<RequestsCreationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RequestsCreationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RequestsCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
