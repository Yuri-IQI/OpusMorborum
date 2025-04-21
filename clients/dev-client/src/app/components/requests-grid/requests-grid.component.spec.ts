import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestsGridComponent } from './requests-grid.component';

describe('RequestsGridComponent', () => {
  let component: RequestsGridComponent;
  let fixture: ComponentFixture<RequestsGridComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RequestsGridComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RequestsGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
