import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
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
    return this.http.get<User>(`${this.backendAPI}/user/username/${username}`);
  }

  public getUsersByKeyword(keyword:string): Observable<User[]> {
    let param = new HttpParams();
    param = param.append('keyword',keyword);
    return this.http.get<User[]>(`${this.backendAPI}/user/search`, { params:param });
  }

  public registerUser(user:User):Observable<User>{
    return this.http.post<User>(`${this.backendAPI}/user/add`, user);
  }

  public updateUser(user:User, username:string):Observable<User>{
    return this.http.put<User>(`${this.backendAPI}/user/update/username/${username}`, user)
  }

  public updateUserAsAdmin(user:User,username:string):Observable<User>{
    return this.http.put<User>(`${this.backendAPI}/user/update/admin/username/${username}`, user)
  }


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
