import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { Observable, from, switchMap } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Order } from '../model/order';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  backendAPI: string = environment.backendAPI;
  keycloakAPI: string = environment.keycloakAPI;
  keycloakRealm : string = environment.keycloakRealm;

  constructor(private http: HttpClient, private keycloakService: KeycloakService) { }

  public getUserdata(): Observable<string> {
    return this.http.get<string>(`${this.backendAPI}/api/user`, { withCredentials: true });
  }

  public getOrdersByUsername(username:string):Observable<Order[]>{
    return from(this.keycloakService.getToken()).pipe(
      switchMap((token: string) => {
        const headers = new HttpHeaders({
          Authorization: `Bearer ${token}`,
        });
        return this.http.get<Order[]>(`${this.backendAPI}/order/user/${username}`,{ headers});
      })
    );
  }

  //ADMIN SECTION
  public getUsersByUsername(username:string):Observable<any[]>{
    return from(this.keycloakService.getToken()).pipe(
      switchMap((token: string) => {
        const headers = new HttpHeaders({
          Authorization: `Bearer ${token}`,
        });
        return this.http.get<any>(`${this.keycloakAPI}/admin/realms/${this.keycloakRealm}/users?username=${username}`,{ headers});
      })
    );
  }
  public getUsersByFullCompanyName(company:string):Observable<any[]>{
    return from(this.keycloakService.getToken()).pipe(
      switchMap((token: string) => {
        const headers = new HttpHeaders({
          Authorization: `Bearer ${token}`,
        });
        return this.http.get<any>(`${this.keycloakAPI}/admin/realms/${this.keycloakRealm}/users?q=company:${company}`,{ headers});
      })
    );
  }
  //ADMIN SECTION END

}