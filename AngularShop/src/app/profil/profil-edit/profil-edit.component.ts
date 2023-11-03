import { Component } from '@angular/core';


@Component({
  selector: 'app-profil-edit',
  templateUrl: './profil-edit.component.html',
  styleUrls: ['./profil-edit.component.scss']
})
export class ProfilEditComponent {
  isSameAddress: boolean = false;

  toggleInput(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    this.isSameAddress = checkbox.checked;
  }
  

}
