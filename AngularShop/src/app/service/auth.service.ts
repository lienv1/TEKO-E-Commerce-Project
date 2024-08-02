import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private keycloakService: KeycloakService) { }

  async ensureTokenIsValid(): Promise<boolean> {
    const minValidity = 50; // seconds
    try {
      const logged = await this.isLoggedIn();
      if (logged) {
        console.log("Is still logged in")
        return true
      }
      else {
        const isTokenUpdated = await this.keycloakService.updateToken(minValidity);
        if (isTokenUpdated) {
          console.log("Token was successfully refreshed.");
          return true;
        } else {
          return false;
        }
      }

    } catch (error) {
      console.error("Could not refresh the token, or the session has expired", error);
      return false; // Token is invalid and could not be refreshed
    }
  }

  async isLoggedIn(): Promise<boolean> {
    try {
      const isLogged = await this.keycloakService.isLoggedIn()
      return isLogged;
    } catch (error) {
      return false;
    }
  }


  async ngOnInit() {
    try {
      // Setup automatic token refresh
      this.setupTokenRefresh();
    } catch (error) {
      console.error('Keycloak init failed', error);
    }
  }

  setupTokenRefresh() {
    // Check token validity every 60 seconds
    console.log("Check token validity every 60 seconds")
    setInterval(async () => {
      // Await the resolution of isLoggedIn() before checking its value
      const loggedIn = await this.keycloakService.isLoggedIn();
      if (loggedIn) {
        // Attempt to refresh the token
        await this.keycloakService.updateToken(60).catch(() => {
          this.keycloakService.logout();
        });
      }
    }, 60000);
  }

}