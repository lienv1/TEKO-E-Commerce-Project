import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Order } from '../model/order';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  backendAPI: string = environment.backendAPI;
  keycloakAPI: string = environment.keycloakAPI;
  keycloakRealm : string = environment.keycloakRealm;

  constructor(private http: HttpClient) { }

  getAllOrdersByUsername(username:string,param:HttpParams):Observable<any>{
    return this.http.get<any>(`${this.backendAPI}/order/username/${username}`, {params:param})
  }

  postOrder(order:Order,username:string){
    return this.http.post<Order>(`${this.backendAPI}/order/username/${username}`, order);
  }

}
