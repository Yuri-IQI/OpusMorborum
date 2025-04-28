import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApothecaryPageComponent } from './apothecary-page.component';

describe('ApothecaryPageComponent', () => {
  let component: ApothecaryPageComponent;
  let fixture: ComponentFixture<ApothecaryPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApothecaryPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApothecaryPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
