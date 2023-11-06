import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { Observable, from, switchMap } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Order } from '../model/order';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  backendAPI: string = environment.backendAPI;
  keycloakAPI: string = environment.keycloakAPI;
  keycloakRealm : string = environment.keycloakRealm;

  constructor(private http: HttpClient, private keycloakService: KeycloakService) { }

  public getUserdata(username:string): Observable<User> {
    return this.http.get<User>(`${this.backendAPI}/user/username/${username}`, { withCredentials: true });
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

  public registerUser(user:User):Observable<User>{
    return this.http.post<User>(`${this.backendAPI}/user/add`, user);
  }

  public updateUser(user:User, username:string):Observable<User>{
    return this.http.put<User>(`${this.backendAPI}/user/update/username/${username}`, user)
  }

  //ADMIN SECTION
  public getUserByUsername(username:string):Observable<any[]>{
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

  getAuthenticatedHeader(){
    const keycloakInstance = this.keycloakService.getKeycloakInstance();
    const accessToken = keycloakInstance.token;
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${accessToken}`
      })
    };
    console.log("accesstoken is   "+accessToken);
    return httpOptions;
  }

}
