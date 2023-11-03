import { Component } from '@angular/core';

@Component({
  selector: 'app-main-profil',
  templateUrl: './main-profil.component.html',
  styleUrls: ['./main-profil.component.scss']
})
export class MainProfilComponent {
  selectedComponent: string = "subcomponent1";

  selectComponent(componentName: string) {
    this.selectedComponent = componentName;
  }
}
