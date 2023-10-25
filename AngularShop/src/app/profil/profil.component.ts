import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, ViewChild } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { KeycloakService } from 'keycloak-angular';
import { environment } from 'src/environments/environment';
import { CustomModalComponent } from '../modal/custom-modal/custom-modal.component';
import { CartItem } from '../model/cartItem';
import { FunctionModel } from '../model/functionModel';
import { Order } from '../model/order';
import { ShoppingCart } from '../service/shoppingCart';
import { UserService } from '../service/user.service';
import { OrderDetail } from '../model/orderDetail';

@Component({
  selector: 'app-profil',
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.scss']
})
export class ProfilComponent {

  @ViewChild("CustomModalComponent") customeModalComponent !: CustomModalComponent

  public orders: Order[] = []
  public keycloakAPI = environment.keycloakAPI
  public profilpage = environment.keycloakAPI + '/realms/' + environment.keycloakRealm + '/account/'
  public username : string = "";

  //Page
  public page = 1;
  public currentPage = 1;

  constructor(
    private title : Title,
    private cart:ShoppingCart, 
    private datePipe: DatePipe, 
    private userService: UserService, 
    private keycloakService: KeycloakService, 
    private router: Router,
    private modalService : NgbModal,
    private translateService:TranslateService
    ) { }

  ngOnInit(): void {
    this.title.setTitle("Profil");
  }

  ngAfterViewInit() {
    this.keycloakService.isLoggedIn().then(
      (isLogged) => {
        if (isLogged)
          this.getOrderByUsername();
        else {
          this.router.navigateByUrl("/login");
        }
      }
    ).catch(
      (error) => {
        alert("Error in keycloakService.isLoggedIn() " + error)
      })
  }

  scroll(el: HTMLElement) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

  private getOrderByUsername() {
    this.keycloakService.loadUserProfile().then(
      (user) => {
        if (user != null && user.username != null)
          this.userService.getOrdersByUsername(user.username).subscribe(
            (response: Order[]) => {
              this.orders = response;
            },
            (error: HttpErrorResponse) => {
              alert(error.message);
            }
          )
          else{
            alert("Error: can not load user")
          }
      }
    )
  }

  public convertTimestampToDate(timestamp ?: Date){
    if (timestamp == null){
      return "";
    }
    return this.datePipe.transform(timestamp, 'yyyy-MM-dd');
  }

  public loadItemsToCart(order:Order){

    if (!this.cart.hasItems()){
      this.loadItemsFunction(order);
      return;
    }

    let foo : () => void = () => this.loadItemsFunction(order);
    let functionModel :FunctionModel = {
      message: this.translateService.instant("REPLACE"),
      foo:foo
    }
    let message = this.translateService.instant("CART REPLACEMENT MESSAGE");
    let title = this.translateService.instant("WARNING");
    this.functionModal(message,title,functionModel)

  }

  public loadItemsFunction(order:Order){
    this.cart.clearCart();
    for (let i = 0; i< order.orderDetails.length ; i++){
      let orderProduct : OrderDetail = order.orderDetails[i];

      let product = orderProduct.product;
      let quantity = orderProduct.quantity;
      if (product == null){
        continue;
      }
      let cartItem :CartItem = {
       product:product,
       quantity:quantity
      }
      this.cart.addItem(cartItem);
    }
    this.router.navigateByUrl("/cart");
  }

  //Popup section
  public functionModal(message:string, title:string, foo: FunctionModel){
    let modal = this.customeModalComponent;
    modal.functionModels = [foo];
    modal.message = message;
    modal.title = title;
    modal.colorTitle = "red";
    this.openModal(modal,false);
  }

  //modal section
  public popupModal(message:string,title:string){
    let modal = this.customeModalComponent;
    modal.message = message;
    modal.title = title;
    this.openModal(modal,false)
  }

  public openModal(modal: any, autoclose: boolean) {
    let modalRef = this.modalService.open(modal.myModal);
    if (autoclose) {
      setTimeout(() => {
        modalRef.dismiss();
      }, 3000);
    }
  }
  //modal section ends
  //Popup section end

  public getTranslation(str:string){
    return this.translateService.instant(str)
  }

}
