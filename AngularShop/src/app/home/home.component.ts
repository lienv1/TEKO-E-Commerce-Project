import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { TranslateService } from '@ngx-translate/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  content: any;

  constructor(private titleSerice: Title, private translationService: TranslateService, private router:Router) {
    
  }

  ngOnInit(): void {
    this.router.navigate(['/shop'])
  }

}