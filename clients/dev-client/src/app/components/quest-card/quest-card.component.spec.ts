import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestCardComponent } from './quest-card.component';

describe('QuestCardComponent', () => {
  let component: QuestCardComponent;
  let fixture: ComponentFixture<QuestCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuestCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QuestCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
