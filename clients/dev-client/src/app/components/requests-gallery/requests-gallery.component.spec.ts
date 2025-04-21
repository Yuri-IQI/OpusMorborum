import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestsGalleryComponent } from './requests-gallery.component';

describe('RequestsGalleryComponent', () => {
  let component: RequestsGalleryComponent;
  let fixture: ComponentFixture<RequestsGalleryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RequestsGalleryComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RequestsGalleryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
