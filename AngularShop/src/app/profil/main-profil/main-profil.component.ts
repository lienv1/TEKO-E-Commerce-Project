import { Component } from '@angular/core';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-main-profil',
  templateUrl: './main-profil.component.html',
  styleUrls: ['./main-profil.component.scss']
})
export class MainProfilComponent {
  selectedComponent: string = "subcomponent1";
  public profilpage = environment.keycloakAPI +'/realms/'+environment.keycloakRealm+'/account/'

  selectComponent(componentName: string) {
    this.selectedComponent = componentName;
  }
}
