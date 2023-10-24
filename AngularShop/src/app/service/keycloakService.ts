import { environment } from "src/environments/environment";

export class KeycloakService {

    keycloakAPI = `${environment.keycloakAPI}/realms/${environment.keycloakRealm}/protocol/openid-connect/userinfo`

    constructor() {
    }

    public isLoggedIn(): boolean {
        return false;
    }

    public getUsername(): string {
        return "";
    }

    

}