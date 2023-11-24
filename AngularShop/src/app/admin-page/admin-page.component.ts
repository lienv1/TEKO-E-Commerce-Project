import { DatePipe } from '@angular/common';
import { HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
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
import { OrderService } from '../service/order.service';

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
  public selectedUsername !: string;
  userid !: number

  //page variables
  //Will be set by API
  page: number = 1;
  maxItems: number = 1;
  //Customizable
  maxOrdersPerPage = 10;

  constructor(   
    private title : Title,
    private cart:ShoppingCart, 
    private datePipe: DatePipe, 
    private userService: UserService, 
    private orderService : OrderService,
    private keycloakService: KeycloakService, 
    private router: Router,
    private modalService : NgbModal,
    private translateService:TranslateService,
    private route : ActivatedRoute
    ) {this.title.setTitle(translateService.instant('ADMIN MENU')) }

  ngOnInit(): void {
    //this.loadUserOrders();
    this.initParam();
  }

  //PARAM SECTION
  initParam() {
    this.route.queryParamMap.subscribe(queryParams => {
      const pageParam = queryParams.get('page');
      if (pageParam && pageParam !== "1"){
        this.page = Number.parseInt(pageParam);
      }
      else
        this.page=1;
      const userIdParam = queryParams.get('userid');
      if (!userIdParam)
        return
      
      this.userid = Number.parseInt(userIdParam);
      this.loadUserOrders();
    });
  }


  setUserIdParam(userid ?: number){
    if (userid)
      this.router.navigate([], {
        queryParams: { userid: userid },
        queryParamsHandling: 'merge'
      });
  }

  //PARAM SECTION END

  public searchUsername(keyword:string){
    this.users = [];
    this.orders = [];
    if (keyword.length<3){
      this.popupModal("Please type at least 3 characters","WARNING","red");
      return;
    }
    this.userService.getUsersByKeyword(keyword).subscribe (
      (response) => {
        this.users = response;
        if(this.users.length>0){
          //this.loadUserOrders(this.users[0].username) dont load here, just set the userid in parameter
          this.setUserIdParam(this.users[0].userId)
        }
      },
      (error:HttpErrorResponse) => {
        this.popupModal(error.message,"Error!","red");
      }
    )
  }

  onOptionSelected(){
    if (!this.selectUserElement)
      return;
    const userid = this.selectUserElement.nativeElement.value;
    if (!userid)
      return;  
    this.userid = Number.parseInt(userid)
    this.setUserIdParam(this.userid);
    this.loadUserOrders()
  }

  loadUserOrders(){
    if (!this.userid)
      return;
    
    this.orderService.getAllOrdersByUserId(this.userid,this.getParam()).subscribe(
      (response) => {
        this.orders = response.content;
        this.maxItems = response.totalElements;
      },
      (error:HttpErrorResponse) => {
        this.popupModal(error.message,"Error!","red");
      }
    )
  }

  getParam(){
    let params = new HttpParams();
    if (this.page)
      params = params.append("page", this.page-1);
    params = params.append("size",this.maxOrdersPerPage);
    params = params.append("sort", "orderId" + ",desc");
    return params;
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

    //PAGE SECTION
    public setPageParam() {
      this.router.navigate([], {
        queryParams: { page: this.page },
        queryParamsHandling: 'merge'
      })
    }
    //PAGE SECTION ENDS
  

  public getTranslation(str:string){
    return this.translateService.instant(str)
  }
}
