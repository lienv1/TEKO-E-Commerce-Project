import { CurrencyPipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { KeycloakService } from 'keycloak-angular';
import { CartItem } from '../model/cartItem';
import { FunctionModel } from '../model/functionModel';
import { Product } from '../model/product';
import { ProductService } from '../service/product.service';
import { ShoppingCart } from '../service/shoppingCart';
import { CustomModalComponent } from '../modal/custom-modal/custom-modal.component';

@Component({
  selector: 'app-product-page',
  templateUrl: './product-page.component.html',
  styleUrls: ['./product-page.component.scss']
})
export class ProductPageComponent {

  @ViewChild('container') container !: ElementRef<HTMLElement>

  public productId?: string;
  public product?: Product;
  public favourite: boolean = false;
  public logged: boolean = false
  public cartMessage: string = "";
  username?: string;

  @ViewChild("quantityOption") quantityOption !: ElementRef<HTMLSelectElement>
  @ViewChild("quantityInput") quantityInput !: ElementRef<HTMLInputElement>

  @ViewChild("CustomModalComponent") customModalComponent !: CustomModalComponent

  constructor(
    private title : Title,
    private route: ActivatedRoute, 
    private router : Router,
    private translateService: TranslateService, 
    private productService: ProductService, 
    private keycloakService: KeycloakService, 
    private shoppingCart: ShoppingCart, 
    private modalService: NgbModal,
    private currencyPipe: CurrencyPipe
    ) { }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.productId = params.get('id')?.toString();
    });

  }

  ngAfterViewInit() {
    this.scroll(this.container.nativeElement)
    this.getProduct();
    this.isLoggedInAndLoadUser();
  }

  scroll(el: HTMLElement) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

  //IAM SECTION
  isLoggedInAndLoadUser() {
    this.keycloakService.isLoggedIn().then(
      (logged) => {
        this.logged = logged
        if (logged) {
          this.loadUser();
        }
      }
    )
  }
  //IAM SECTION END

  //API SECTION
  getProduct() {
    if (this.productId == null || this.productId == "")
      return
    this.productService.getProduct(this.productId).subscribe(
      (response: Product) => {
        this.product = response;
        this.title.setTitle(this.productId + " - " + this.product?.productName);
      },
      (error: HttpErrorResponse) => {
        alert(error.message)
      }
    )
  }

  loadUser() {
    this.keycloakService.loadUserProfile().then(
      (user) => {
        this.username = user.username;
        this.isFavorite();
      }
    )
  }

  isFavorite() {
    if (this.productId == null)
      return;
    if (this.username == null)
      return;

    this.productService.isFavourite(this.username, this.productId).subscribe({
     next : (response: boolean) => {
        this.favourite = response
      },
      error : (error: HttpErrorResponse) => {
        if (error.status !== 404) this.popup("ERROR " + error.status, error.message, "red")
      }
    })
  }

  public addFavourite() {
    if (this.username == null)
      return;
    if (this.productId == null)
      return;
    this.productService.addFavourite(this.username, this.productId).subscribe(
      (response) => {
        this.favourite = true;
      },
      (error: HttpErrorResponse) => {
        this.handleError(error);
      }
    )
  }

  public removeFavourite() {
    if (this.username == null)
      return;
    if (this.productId == null)
      return;
    this.productService.deleteFavourite(this.username, this.productId).subscribe(
      (response) => {
        this.favourite = false;
      },
      (error: HttpErrorResponse) => {
        this.handleError(error)
      }
    )
  }

  handleError(error: HttpErrorResponse){
    if (error.status !== 404) this.popup("ERROR " + error.status, error.message, "red")
    else {this.router.navigateByUrl("/profile/edit"); this.popup("Profile unfinished", "Please setup your profile first", "black")}
  }

  //API SECTION END


  //CART SECTION
  public addToCart() {
    if (this.product == null) {
      console.log("Error, no productId")
      return
    }
    let quantity = parseInt(this.quantityInput.nativeElement.value)
    if (quantity == null) {
      console.log("No quantity")
      return
    }
    let option = this.quantityOption.nativeElement.value
    if (option == null) {
      console.log("no option")
      return
    }
    if (option === "carton") {
      quantity *= (this.product.pack === undefined || this.product.pack == null)  ? 1 : this.product.pack
    }
    console.log("option is " + option + ", quantity is " + quantity);

    const cartItem: CartItem = {
      product: this.product,
      quantity: quantity
    }

    if (this.shoppingCart.findItemInCart(cartItem)) {
      //Increase function
      let increaseFunction: () => void = () => this.increaseQuantity(cartItem.product, quantity)
      const increaseName = this.translateService.instant("INCREASE");
      const increaseModel: FunctionModel = {
        buttonText: increaseName,
        foo: increaseFunction,
      }
      //Replace function
      let replaceFunction: () => void = () => this.replaceQuantity(cartItem.product, quantity);
      const replaceName: string = this.translateService.instant("REPLACE")
      const replaceModel: FunctionModel = {
        buttonText: replaceName,
        foo: replaceFunction
      }
      //Modal setting
      const message: string = cartItem.product.productId + " " + cartItem.product.productName + " - " + this.translateService.instant("ITEM EXISTS");
      const title: string = this.translateService.instant("WARNING");
      const color: string = "red";
      let modal = this.customModalComponent;
      modal.title = title;
      modal.message = message;
      modal.colorTitle = color;
      modal.functionModels = [replaceModel, increaseModel];
      this.openModal(modal, false);
      return;
    }

    this.shoppingCart.addItem(cartItem)
    this.cartMessage = this.translateService.instant("ITEM ADDED SUCCESS");
    setTimeout(() => {
      this.cartMessage = '';
    }, 3000);
  }

  public increaseQuantity(product: Product, quantity: number) {
    if (quantity == null) {
      console.log("No quantity")
      return
    }

    if (quantity > 0) {
      if (product != null) {
        this.shoppingCart.increaseQuantityById(product.productId, quantity)
        console.log("Quantity increased")
      }
    }
  }

  public replaceQuantity(product: Product, quantity: number) {
    if (quantity == null) {
      console.log("No quantity")
      return
    }

    if (quantity > 0) {
      if (product != null)
        this.shoppingCart.replaceQuantityById(product.productId, quantity)
    }
  }

  //CART SECTION END

  //MODAL SECTION
  public openModal(modal: any, autoclose: boolean) {
    let modalRef = this.modalService.open(modal.myModal);
    if (autoclose) {
      setTimeout(() => {
        modalRef.dismiss();
      }, 3000);
    }
  }

  public popup(title:string, message:string, color:string){
    let modal = this.customModalComponent;
    modal.message = message;
    modal.title = title;
    modal.colorTitle = color;
    this.openModal(modal,false);
  }
  //MODAL SECTION END

  public hasCarton(): boolean {
    return this.product?.pack !== 1
  }

  public formatPrice(priceNumber ?: number): string {

    if (priceNumber == null){
      return "";
    }
   
    // Format the price as Swiss Franc (CHF)
    let formattedPrice = this.currencyPipe.transform(priceNumber, 'CHF');
  
  if (formattedPrice != null)
    formattedPrice = formattedPrice?.replace('CHF','CHF ')

  // Return the formatted price
  return formattedPrice || '';
}

}
