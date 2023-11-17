import { HttpClient, HttpHeaders, HttpParams, HttpStatusCode } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { Observable, from, switchMap } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Order } from '../model/order';
import { Product } from '../model/product';
import { ProductCategory } from '../model/productCategory';
import { User } from '../model/user';
import { Filters } from '../model/filters';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  backendAPI: string = environment.backendAPI;

  constructor(private http: HttpClient, private keycloakService: KeycloakService) { }

  public getProducts(params : HttpParams): Observable<any> {
    return this.http.get<any>(`${this.backendAPI}/product/products`, {params:params})
  }

  public getProduct(id: string): Observable<Product> {
    return this.http.get<Product>(`${this.backendAPI}/product/id/${id}`);
  }

  public getProductByFilter(params:HttpParams):Observable<any>{
    console.log(params)
    return this.http.get<any>(`${this.backendAPI}/product/filter`, {params:params})
  }

  public getProductBySearch(params:HttpParams):Observable<any>{
    return this.http.get<any>(`${this.backendAPI}/product/search`, {params:params})
  }

  //CATEGORY AND FILTER
  public getCategories(): Observable<ProductCategory[]>{
    return this.http.get<ProductCategory[]>(`${this.backendAPI}/product/category`);
  }

  public getFiltersByCategory(params:HttpParams):Observable<Filters>{
    return this.http.get<Filters>(`${this.backendAPI}/product/filters`, {params:params});
  }

  public getFiltersBySearch(params:HttpParams):Observable<Filters>{
    return this.http.get<Filters>(`${this.backendAPI}/product/searchfilters`, {params:params})
  }


  //Favourite functions
  public isFavourite(username:string, id:string): Observable<boolean> {
    return this.http.get<boolean>(`${this.backendAPI}/favorite/product/${id}/username/${username}`);
   
  }

  public addFavourite(username:string, id:string):Observable<any>{  
    return this.http.post<any>(`${this.backendAPI}/favorite/product/${id}/username/${username}`,true);
  }

  public deleteFavourite(username:string, id:string):Observable<any>{
    return this.http.delete(`${this.backendAPI}/favorite/product/${id}/username/${username}`);
  }

  public getFavourites(username:string, params:HttpParams):Observable<Product[]>{
    return this.http.get<any[]>(`${this.backendAPI}/product/favorite/username/${username}`, {params:params});
  }
  
}
