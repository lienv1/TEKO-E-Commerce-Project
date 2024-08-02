// src/app/auth.guard.ts
import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private keycloakService: KeycloakService, private router: Router) {}

  async canActivate(): Promise<boolean> {
    if (await this.keycloakService.isLoggedIn()) {
      return true;  // User is logged in, allow access to the route
    } else {
      await this.keycloakService.login();  // Redirect to Keycloak login
      return false;  // Prevent navigation until login is successful
    }
  }
}
