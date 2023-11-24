import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  constructor(private titleSerice : Title, private translationService:TranslateService ) { 
    this.titleSerice.setTitle(this.translationService.instant('HOME'));
  }

  ngOnInit(): void {
  }

}