import { HttpClient, HttpHeaders, HttpParams, HttpStatusCode } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { Observable, from, switchMap } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Order } from '../model/order';
import { Product } from '../model/product';
import { ProductCategory } from '../model/productCategory';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  backendAPI: string = environment.backendAPI;

  constructor(private http: HttpClient, private keycloakService: KeycloakService) { }

  public getProducts(): Observable<any> {
    return this.http.get<any>(`${this.backendAPI}/product/products`)
  }

  public getProduct(id: string): Observable<Product> {
    return this.http.get<Product>(`${this.backendAPI}/product/id/${id}`);
  }

  public getProductByFilter(params:HttpParams):Observable<any>{
    return this.http.get<any>(`${this.backendAPI}/product/filter`, {params:params})
  }

  public getProductBySearch(params:HttpParams):Observable<any>{
    return this.http.get<any>(`${this.backendAPI}/product/search`, {params:params})
  }


  //CATEGORY
  public getCategories(): Observable<ProductCategory[]>{
    return this.http.get<ProductCategory[]>(`${this.backendAPI}/product/category`);
  }


  //Favourite functions
  public isFavourite(username:string, id:string): Observable<boolean> {
    return from(this.keycloakService.getToken()).pipe(
      switchMap((token: string) => {
        const headers = new HttpHeaders({
          Authorization: `Bearer ${token}`,
        });
        return this.http.get<boolean>(`${this.backendAPI}/product/favourite/user/${username}/product/${id}`, { headers });
      })
    );
  }

  public addFavourite(username:string, id:string):Observable<any>{
    return from(this.keycloakService.getToken()).pipe(
      switchMap((token: string) => {
        const headers = new HttpHeaders({
          Authorization: `Bearer ${token}`,
        });
        return this.http.post(`${this.backendAPI}/product/favourite/user/${username}/product/${id}`, { headers });
      })
    );
  }

  public deleteFavourite(username:string, id:string):Observable<any>{
    return from(this.keycloakService.getToken()).pipe(
      switchMap((token: string) => {
        const headers = new HttpHeaders({
          Authorization: `Bearer ${token}`,
        });
        return this.http.delete(`${this.backendAPI}/product/favourite/user/${username}/product/${id}`, { headers });
      })
    );
  }

  public getFavourites(username:string):Observable<Product[]>{
    return from(this.keycloakService.getToken()).pipe(
      switchMap((token: string) => {
        const headers = new HttpHeaders({
          Authorization: `Bearer ${token}`,
        });
        return this.http.get<Product[]>(`${this.backendAPI}/product/favourite/user/${username}/all`, { headers });
      })
    );
  }
  
}
