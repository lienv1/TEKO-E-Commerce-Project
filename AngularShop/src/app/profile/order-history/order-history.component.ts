import { DatePipe } from '@angular/common';
import { HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
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
  orders: Order[] = []

  username !: string;

  //page variables
  //Will be set by API
  page: number = 1;
  maxItems: number = 1;
  //Customizable
  maxOrdersPerPage = 10; 

  constructor(private route: ActivatedRoute, private translateService: TranslateService, private keycloakService: KeycloakService, private orderService: OrderService, private cart: ShoppingCart, private router: Router, private modalService: NgbModal, private datePipe: DatePipe) { }

  ngOnInit(): void {
    this.initParam();
  }

  initParam() {
    this.route.queryParamMap.subscribe(queryParams => {
      const pageParam = queryParams.get('page');
      if (pageParam && pageParam !== "1"){
        this.page = Number.parseInt(pageParam);
      }
      else
        this.page=1;
      this.initUser();
    });
  }

  initUser() {
    this.keycloakService.loadUserProfile().then(
      (user) => { let username = user.username; if (username) this.initOrder(username) }
    )
  }

  initOrder(username: string) {
    let params = new HttpParams();
    params = this.appendPageParam(params);
    params = this.appendMaxOrderParam(params);
    this.orderService.getAllOrdersByUsername(username,params).subscribe({
      next: (response) => this.handleResponse(response),
      error: (error) => this.handleError(error)
    })
  }

  //API SECTION
  appendPageParam(params : HttpParams){
    if (this.page !== 1){
      params = params.append("page", this.page-1);
    }
    return params;
  }

  appendMaxOrderParam(params : HttpParams){
    params = params.append("size", this.maxOrdersPerPage);
    return params;
  }

  handleResponse(response:any){
    this.orders = response.content;
    this.maxItems = response.totalElements;
    console.log(this.maxItems);
  }
  handleError(error:HttpErrorResponse){
    if (error.status === 404){
      this.redirectToProfilEdit();
      return;
    }
    this.popupModal("ERROR", error.message,"red");
  }

  //API SECTION END

  public loadItemsToCart(order: Order) {

    if (!this.cart.hasItems()) {
      this.loadItemsFunction(order);
      return;
    }

    let foo: () => void = () => this.loadItemsFunction(order);
    let functionModel: FunctionModel = {
      buttonText: this.translateService.instant("REPLACE"),
      foo: foo
    }
    let message = this.translateService.instant("CART REPLACEMENT MESSAGE");
    let title = this.translateService.instant("WARNING");
    this.functionModal(message, title, functionModel)

  }

  public loadItemsFunction(order: Order) {
    this.cart.clearCart();
    for (let i = 0; i < order.orderDetails.length; i++) {
      let orderProduct: OrderDetail = order.orderDetails[i];

      let product = orderProduct.product;
      let quantity = orderProduct.quantity;
      if (product == null) {
        continue;
      }
      let cartItem: CartItem = {
        product: product,
        quantity: quantity
      }
      this.cart.addItem(cartItem);
    }
    this.router.navigateByUrl("/cart");
  }


  scroll(el: HTMLElement) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

  public getTranslation(str: string) {
    return this.translateService.instant(str)
  }

  public convertTimestampToDate(timestamp?: Date) {
    if (timestamp == null) {
      return "";
    }
    return this.datePipe.transform(timestamp, 'yyyy-MM-dd');
  }

  //Popup section
  public functionModal(message: string, title: string, foo: FunctionModel) {
    let modal = this.customModalComponent;
    modal.functionModels = [foo];
    modal.message = message;
    modal.title = title;
    modal.colorTitle = "red";
    this.openModal(modal, false);
  }

  //modal section
  public popupModal(message: string, title: string,color:string) {
    let modal = this.customModalComponent;
    modal.message = message;
    modal.title = title;
    modal.colorTitle = color;
    this.openModal(modal, false)
  }

  public openModal(modal: any, autoclose: boolean) {
    let modalRef = this.modalService.open(modal.myModal);
    if (autoclose) {
      setTimeout(() => {
        modalRef.dismiss();
      }, 3000);
    }
  }
  public redirectToProfilEdit(){
    this.router.navigateByUrl("/profile/edit");
    let modal = this.customModalComponent;
    modal.message = "Please setup your profile first";
    modal.title = "Profile unfinished";
    modal.colorTitle = "black";
    this.openModal(modal,false);
  }
  //modal section ends
  //Popup section end

  //PAGE SECTION
  public setPageParam() {
    this.router.navigate([], {
      queryParams: { page: this.page },
      queryParamsHandling: 'merge'
    })
  }
  //PAGE SECTION ENDS

}
