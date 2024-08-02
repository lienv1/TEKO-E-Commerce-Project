import { CurrencyPipe } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { KeycloakService } from 'keycloak-angular';
import { CartItem } from '../model/cartItem';
import { FunctionModel } from '../model/functionModel';
import { Product } from '../model/product';
import { ShoppingCart } from '../service/shoppingCart';
import { CustomModalComponent } from '../modal/custom-modal/custom-modal.component';
import { environment } from 'src/environments/environment';
import { ExtendedModalService } from '../service/extendedModalService';
import { UserService } from '../service/user.service';
import { User } from '../model/user';
import { HttpErrorResponse } from '@angular/common/http';
import { PriceService } from '../service/price.service';
import { ProductService } from '../service/product.service';
import { PriceCategoryDTO } from '../model/priceCategoryDTO';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent {

  @ViewChild("CustomModalComponent") customModalComponent !: CustomModalComponent
  @ViewChild('container') container !: ElementRef<HTMLElement>;

  cartItems: CartItem[] = [];
  page: number = 1;
  logged = false;
  fileServer = environment.fileServerAPI;
  erpId?: number;

  showAlert = false;

  selectedProduct !: Product

  private extendedModalService: ExtendedModalService

  constructor(
    private title: Title,
    private shoppingCart: ShoppingCart,
    private currencyPipe: CurrencyPipe,
    private translate: TranslateService,
    private modalService: NgbModal,
    private keycloakService: KeycloakService,
    private userService: UserService,
    private productService: ProductService,
    private priceService: PriceService,
    private authService :AuthService
  ) {
    this.extendedModalService = new ExtendedModalService(modalService)
  }

  ngOnInit(): void {
    this.translate.get('CART').subscribe(element => {this.title.setTitle(element)})
    this.initCartItems();
  }

  ngAfterViewInit() {
    this.isLogged();
    this.scroll(this.container.nativeElement);
  }

  isLogged() {
    this.authService.ensureTokenIsValid().then(logged => {this.logged=logged;if (this.logged) this.loadUser();}  )
  }

  initCartItems() {
    this.cartItems = this.shoppingCart.getCartItems();
    this.sortItemsById();
  }

  scroll(el: HTMLElement) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

  public hasCarton(product: Product) {
    return product.pack !== 1
  }

  //PRICE SECTION
  public formatPrice(priceNumber: number): string {
    // Format the price as Swiss Franc (CHF)
    let formattedPrice = this.currencyPipe.transform(priceNumber, 'CHF');

    if (formattedPrice != null)
      formattedPrice = formattedPrice?.replace('CHF', 'CHF ')

    // Return the formatted price
    return formattedPrice || '';
  }

  public calculatedPrice(price: number, quantity: number) {
    return (price * quantity)
  }

  //PRICE SECTION END

  loadUser() {
    this.keycloakService.loadUserProfile().then(
      (user) => {
        if (user.username) this.loadAPIUser(user.username);
      },
      (error) => {
        console.log(error.message)
        return;
      }
    )
  }

  loadAPIUser(username: string) {
    this.userService.getUserdata(username).subscribe({
      next: (response) => { this.erpId = response.erpId; this.setDiscountForAllProducts() },
      error: (error: HttpErrorResponse) => { console.log(error.message); this.setDiscountForAllProducts() }
    })
  }

  setDiscountForAllProducts() {
    if (!this.erpId)
      for (let i = 0; i < this.cartItems.length; i++)
        this.cartItems[i].discountedPrice = this.cartItems[i].product.price;

    else {
      this.priceService.getPrices(this.erpId, this.cartItems).subscribe({
        next: (response: PriceCategoryDTO[]) => {
          this.cartItems.map(cartItem => {
            const priceCategory = response.find(element => element.productId === cartItem.product.productId);
            if (priceCategory) {
              cartItem.discountedPrice = priceCategory.price
              return cartItem
            } else {
              return cartItem;
            }
          })

        },
        error: (error) => { console.log(error.message) }
      }
      )
      /*for (let i = 0; i<this.cartItems.length;i++){
            this.priceService.getPrice(this.erpId,this.cartItems[i].product,this.cartItems[i].quantity).subscribe({
              next: (response) => {this.cartItems[i].discountedPrice = response;},
              error:(error:HttpErrorResponse) => {console.log(error.message)}
            })
          }*/
    }
  }

  /*Deprecrated*/
  setDiscountForProduct(cartItem: CartItem) {
    if (!this.erpId) {
      cartItem.discountedPrice = cartItem.product.price
      return;
    }
    this.priceService.getPrice(this.erpId, cartItem.product, cartItem.quantity).subscribe({
      next: (response) => {
        cartItem.discountedPrice = response;
      },
      error: (error: HttpErrorResponse) => { console.log(error.message) }
    })
  }

  displayPrice(cartItem: CartItem): number {
    if (!this.erpId || !cartItem.discountedPrice)
      return cartItem.quantity * cartItem.product.price
    return cartItem.quantity * cartItem.discountedPrice;
  }

  displayUnitPrice(cartItem: CartItem): number {
    if (this.erpId && cartItem.discountedPrice) {
      return cartItem.discountedPrice
    }
    return cartItem.product.price
  }

  equalPrice(productId: number): boolean {
    let item = this.cartItems.find(element => element.product.productId === productId)
    if (item) {
      return !item.discountedPrice || !item.product.price || item.discountedPrice === item.product.price
    }
    return false;
  }

  //PRICE SECTION END
  public getTranslation(str: string) {
    return this.translate.instant(str);
  }

  public getCarton(item: CartItem) {
    let quantity = item.quantity
    let perCarton = item.product.pack === null ? 1 : item.product.pack
    if (perCarton === undefined) {
      perCarton = 1
    }
    return Math.round(quantity / perCarton)
  }

  public updateCartItem(cartItem: CartItem, quantityStr: string) {
    const quantity = parseFloat(quantityStr)

    /*if (cartItem.product.stock && quantity > cartItem.product.stock){
      this.extendedModalService.popup(this.customModalComponent,this.translate.instant('STOCK LIMIT EXCEED'),this.translate.instant('STOCK LIMIT EXCEED TEXT'),"red");
      return;
    }*/

    this.shoppingCart.removeItem(cartItem);
    if (quantity > 0) {
      let item = cartItem;
      item.quantity = quantity;
      this.shoppingCart.addItem(item)
    }
    this.setDiscountForProduct(cartItem)
    this.sortItemsById();
    this.showMessageForItem(cartItem.product.productId);
  }

  sortItemsById(): void {
    this.cartItems = this.cartItems.sort((a, b) => a.product.productId - b.product.productId);
  }

  //Popup section

  public popupDelete(cartItem: CartItem) {
    this.selectedProduct = cartItem.product;
    const deleteFunction: () => void = () => this.deleteItem(cartItem.product);
    const deleteText: string = this.translate.instant("REMOVE");
    const message: string = this.translate.instant("REMOVE ITEM MESSAGE");
    const title: string = this.translate.instant("WARNING");
    const deleteModel: FunctionModel = {
      buttonText: deleteText,
      foo: deleteFunction
    }
    let modal = this.customModalComponent;
    modal.message = message;
    modal.title = title;
    modal.functionModels = [deleteModel];
    modal.colorTitle = "red";
    this.modalService.open(modal.myModal);
  }

  public deleteItem(product: Product) {
    this.shoppingCart.removeItemById(product.productId)
  }

  showMessageForItem(id: number) {
    let messageDiv = document.getElementById(`msg-${id}`);
    if (messageDiv == null)
      return

    messageDiv.style.display = "block"

    setTimeout(() => {
      if (messageDiv != null)
        messageDiv.style.display = "none"
    }, 2000);
  }
  //Popup section ends

  public getTotal() {
    let hasErp = this.erpId != null;
    return this.shoppingCart.getTotal(hasErp);
  }

  public getTotalTax() {
    let hasErp = this.erpId != null;
    return this.shoppingCart.getTotalTax(hasErp);
  }

  public getSubtotal() {
    let hasErp = this.erpId != null;
    return this.shoppingCart.getSubtotal(hasErp);
  }

}
