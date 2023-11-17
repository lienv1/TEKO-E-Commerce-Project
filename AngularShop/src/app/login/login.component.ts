import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakLoginOptions } from 'keycloak-js';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  isLogged = false;
  username!: string

  constructor(
    private title:Title,
    private keycloakService: KeycloakService, 
    private router: Router) { }

  ngOnInit(): void {
    this.title.setTitle("Login");
  }

  ngAfterViewInit(): void {
    this.keycloakService.isLoggedIn().then(
      (logged) => {
        this.isLogged = logged;
        if (this.isLogged){
          this.getUsername();
        }
      }
    ).catch ((error) => {
      console.log("Error in keycloakService.isLoggedIn(): " + error)
    })

  }


  login() {
    this.keycloakService.login(this.keycloakRedirectOption());
  }

  register() {
    this.keycloakService.register(this.keycloakRedirectOption());
  }

  logout() {
    this.keycloakService.logout();
  }

  isLoggedIn() {
    this.keycloakService.isLoggedIn().then(
      (logged) => { this.isLogged = logged; }
    )
  }

  getUsername() {
    this.keycloakService.loadUserProfile().then(
      (user) => { this.username = user.username!; }
    );
  }

  keycloakRedirectOption(){
    const keycloakLoginOptions: KeycloakLoginOptions = {
      redirectUri:  window.location.origin + '/profile/edit'
    }
    return keycloakLoginOptions;
  }

}
