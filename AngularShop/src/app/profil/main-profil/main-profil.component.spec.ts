import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MainProfilComponent } from './main-profil.component';

describe('MainProfilComponent', () => {
  let component: MainProfilComponent;
  let fixture: ComponentFixture<MainProfilComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MainProfilComponent]
    });
    fixture = TestBed.createComponent(MainProfilComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
