import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfilEditComponent } from './profil-edit.component';

describe('ProfilEditComponent', () => {
  let component: ProfilEditComponent;
  let fixture: ComponentFixture<ProfilEditComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProfilEditComponent]
    });
    fixture = TestBed.createComponent(ProfilEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
