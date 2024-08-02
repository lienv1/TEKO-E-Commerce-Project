import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-main-profile',
  templateUrl: './main-profile.component.html',
  styleUrls: ['./main-profile.component.scss']
})
export class MainProfileComponent implements OnInit{
  

  public profilpage = environment.keycloakAPI +'/realms/'+environment.keycloakRealm+'/account/'

  constructor(private title:Title, private translate:TranslateService ){}

  ngOnInit(): void {
    this.translate.get('PROFILE').subscribe(element => {this.title.setTitle(element)})
  }


}
