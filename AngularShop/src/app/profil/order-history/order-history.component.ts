import { DatePipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { KeycloakService } from 'keycloak-angular';
import { CustomModalComponent } from 'src/app/modal/custom-modal/custom-modal.component';
import { CartItem } from 'src/app/model/cartItem';
import { FunctionModel } from 'src/app/model/functionModel';
import { Order } from 'src/app/model/order';
import { OrderDetail } from 'src/app/model/orderDetail';
import { OrderService } from 'src/app/service/order.service';
import { ShoppingCart } from 'src/app/service/shoppingCart';

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  styleUrls: ['./order-history.component.scss']
})
export class OrderHistoryComponent implements OnInit {

  @ViewChild("CustomModalComponent") customModalComponent !: CustomModalComponent
  orders:Order[] = []

  username !: string;
  page = 1;

  constructor(private translateService:TranslateService, private keycloakService:KeycloakService, private orderService:OrderService, private cart:ShoppingCart,private router:Router, private modalService:NgbModal, private datePipe: DatePipe){}

  ngOnInit(): void {
    this.initUser();
  }

  initUser(){
    this.keycloakService.loadUserProfile().then(
      (user) => { let username = user.username; if (username) this.initOrder(username)}
    )
  }

  initOrder(username:string){
    this.orderService.getAllOrdersByUsername(username).subscribe({
      next: (response) => this.orders = response,
      error: (error) => alert(error.message)
    })
  }

  public loadItemsToCart(order:Order){

    if (!this.cart.hasItems()){
      this.loadItemsFunction(order);
      return;
    }

    let foo : () => void = () => this.loadItemsFunction(order);
    let functionModel :FunctionModel = {
      buttonText: this.translateService.instant("REPLACE"),
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


  scroll(el: HTMLElement) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

  public getTranslation(str:string){
    return this.translateService.instant(str)
  }

  public convertTimestampToDate(timestamp ?: Date){
    if (timestamp == null){
      return "";
    }
    return this.datePipe.transform(timestamp, 'yyyy-MM-dd');
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
    public popupModal(message:string,title:string){
      let modal = this.customModalComponent;
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

}
