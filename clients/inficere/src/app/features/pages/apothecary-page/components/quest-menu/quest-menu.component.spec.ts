import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestMenuComponent } from './quest-menu.component';

describe('QuestMenuComponent', () => {
  let component: QuestMenuComponent;
  let fixture: ComponentFixture<QuestMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuestMenuComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QuestMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
