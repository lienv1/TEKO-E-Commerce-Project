import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminProfileEditComponent } from './admin-profile-edit.component';

describe('AdminProfileEditComponent', () => {
  let component: AdminProfileEditComponent;
  let fixture: ComponentFixture<AdminProfileEditComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdminProfileEditComponent]
    });
    fixture = TestBed.createComponent(AdminProfileEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
