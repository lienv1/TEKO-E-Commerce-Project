import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { KeycloakService } from 'keycloak-angular';
import { environment } from 'src/environments/environment';
import { CartItem } from '../model/cartItem';
import { Order } from '../model/order';
import { User } from '../model/user';
import { ShoppingCart } from '../service/shoppingCart';
import { CustomModalComponent } from '../modal/custom-modal/custom-modal.component';
import { UserService } from '../service/user.service';
import { FunctionModel } from '../model/functionModel';
import { OrderDetail } from '../model/orderDetail';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.scss']
})
export class AdminPageComponent implements OnInit {

  @ViewChild("CustomModalComponent") customModalComponent !: CustomModalComponent
  @ViewChild('selectUser') selectUserElement !: ElementRef<HTMLSelectElement>

  public orders: Order[] = []
  public keycloakAPI = environment.keycloakAPI
  public profilpage = environment.keycloakAPI + '/realms/' + environment.keycloakRealm + '/account/'

  //Admin privilege
  public users: User[] = [];
  public selectedUser !: User;

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
    private translateService:TranslateService) { }

  ngOnInit(): void {

  }
/*
  public searchUsername(keyword:string){
    this.users = [];
    this.orders = [];
    if (keyword.length<3){
      this.popupModal("Please type at least 3 characters","WARNING","red");
      return;
    }
    this.userService.getUsersByUsername(keyword).subscribe (
      (response) => {
        for (let i = 0; i <response.length;i++){
          const username = response[i].username;
          const company = response[i].attributes.company != null ? response[i].attributes.company.toString() : "";
          this.users.push(
            {username:username,company:company}
          );
        }
        if (this.users.length>0 && this.users[0] != null){
          this.loadUserOrders(this.users[0].username);
        }
      },
      (error:HttpErrorResponse) => {
        this.popupModal(error.message,"Error!","red");
      }
    )
  }

  onOptionSelected(){
    const username = this.selectUserElement.nativeElement.value;
    this.loadUserOrders(username)
  }

  loadUserOrders(username ?: string){
    if (username == null)
      return;
    this.userService.getOrdersByUsername(username).subscribe(
      (response) => {
        this.orders = response;
      },
      (error:HttpErrorResponse) => {
        this.popupModal(error.message,"Error!","red");
      }
    )
  }

  scroll(el: HTMLElement) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' });
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
    let modal = this.customModalComponent;
    modal.functionModels = [foo];
    modal.message = message;
    modal.title = title;
    modal.colorTitle = "red";
    this.openModal(modal,false);
  }

  //modal section
  public popupModal(message:string,title:string, color:string){
    let modal = this.customModalComponent;
    modal.message = message;
    modal.title = title;
    modal.colorTitle = color;
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
  }*/
}
