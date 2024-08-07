import { Component, ElementRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ShoppingCart } from '../service/shoppingCart';
import { TranslateService } from '@ngx-translate/core';
import { KeycloakService } from 'keycloak-angular';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {

  public isCollapsed = true;
  public isAdmin = false;

  public isLogged = false;
  public existInAPI = false;

  @ViewChild('languageSelector')
  languageSelector !: ElementRef<HTMLSelectElement>

  //THEME
  @ViewChild('themeToggle') themeToggle !: ElementRef<HTMLInputElement>

  constructor(private cart: ShoppingCart,
    private keycloakService: KeycloakService,
    private translate: TranslateService,
    private authService : AuthService,
    private router :Router
  ) {
    translate.addLangs(['en', 'de', 'fr', 'zh', 'vn']);
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    this.setLanguageFromStorage();
    this.isLoggedIn();
    this.getLanguage();
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
  public isLoggedIn(){
    this.authService.ensureTokenIsValid().then(logged => {
      this.isLogged = logged; 
      if (this.isLogged) this.getRole();
    })
  }

  public getUsername() {
    try { return this.keycloakService.getUsername(); }
    catch (error) {
      return false;
    }
  }

  //LOGIN SECTION END

  //ADMIN SECTION
  getRole() {
    let roles = this.keycloakService.getUserRoles();
    for (let i = 0; i < roles.length; i++) {
      if (roles[i] === "ADMIN") {
        this.isAdmin = true;
        return;
      }
    }
  }
  //ADMIN SECTION END

  //CART SECTION
  public countItemsInCart() {
    let quantity = this.cart.getCartItems().length
    if (quantity != null && quantity > 0) {
      return "(" + quantity + ")"
    }
    return ""
  }
  //CART SECTION END

  //THEME SECTION
  public getTheme(): string {
    let theme = localStorage.getItem("Theme");
    if (theme === null || theme === "light") { localStorage.setItem("Theme","light"); return "light"}
    return "dark";
  }

  onSwitchTheme() {
    let theme = localStorage.getItem("Theme");
    if (theme === null) {localStorage.setItem("Theme","light"); theme = "light";}

    if (theme.toLowerCase() === "light") localStorage.setItem("Theme", "dark")
    else localStorage.setItem("Theme", "light")
    location.reload();
  }

  //THEME SECTION END

  getLanguage() {
    let lang = this.translate.currentLang;
  }

}