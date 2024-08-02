import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Product } from '../model/product';
import { PriceCategoryDTO } from '../model/priceCategoryDTO';
import { CartItem } from '../model/cartItem';

@Injectable({
  providedIn: 'root'
})
export class PriceService {

  backendAPI: string = environment.backendAPI;

  constructor(private http: HttpClient) { }

  public getPrice(erp:number,product:Product, quantity:number):Observable<number>{
    return this.http.post<number>(`${this.backendAPI}/price/quantity/${quantity}/erp/${erp}`, product);
  }

  public getPrices(erp:number,products:CartItem[]):Observable<PriceCategoryDTO[]>{
    return this.http.post<PriceCategoryDTO[]>(`${this.backendAPI}/price/erp/${erp}`, products);
  }

}
