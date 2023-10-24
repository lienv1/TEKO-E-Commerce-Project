import { Component, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ShoppingCart } from '../service/shoppingCart';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {

  public isLogged = false;
  public isAdmin = false;

  @ViewChild('languageSelector')
  languageSelector !: ElementRef<HTMLSelectElement>

  @ViewChild('themeToggle')
  themeToggle !: ElementRef<HTMLInputElement>

  public theme: string = "light";

  constructor( private cart:ShoppingCart,
    private translate: TranslateService, 
    private route: ActivatedRoute, 
    private router: Router ){
      translate.addLangs(['en', 'de', 'fr', 'zh', 'vn']);
  }

  ngOnInit(): void {

  }

  ngAfterViewInit(): void {
    this.setLanguageFromStorage();
    this.isLoggedIn();
  }

  //On load
  public setLanguageFromStorage() {
    let language = localStorage.getItem('lang');
    if (language != null) {
      this.translateLanguageTo(language);
      this.languageSelector.nativeElement.value = language;
    }
    else {
      this.translateLanguageTo("de");
      this.languageSelector.nativeElement.value = "de";
    }
  }

  //LANGUAGE SECTION
  //Switch language
  public translateLanguageTo(lang: string) {
    this.translate.use(lang);
    this.updateLanguageInLocalStorage(lang);
  }
  //Update language in localStorage
  public updateLanguageInLocalStorage(lang: string) {
    localStorage.setItem('lang', lang);
  }
  //LANGUAGE SECTION END

  //LOGIN SECTION
  public isLoggedIn() {
    
  }

  public getUsername() {
    
  }
  //LOGIN SECTION END

}
