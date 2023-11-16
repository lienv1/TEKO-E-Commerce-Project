import { Component } from '@angular/core';
import { contactInfo } from '../model/contact-info';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent {

  contactInfo = contactInfo;

}
