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

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent {

  @ViewChild("CustomModalComponent") customModalComponent !: CustomModalComponent
  @ViewChild('container') container !: ElementRef<HTMLElement>;

  public cartItems: CartItem[] = [];
  public page : number = 1;
  public logged = false;

  showAlert = false;

  public selectedProduct !: Product

  constructor(
    private title:Title, 
    private shoppingCart:ShoppingCart,
    private currencyPipe: CurrencyPipe, 
    private translate:TranslateService,
    private modalService: NgbModal,
    private keycloakService : KeycloakService
    ) { }

  ngOnInit(): void {
    this.initCartItems();
    this.title.setTitle("Cart");
  }

  ngAfterViewInit(){
    this.isLogged();
    this.scroll(this.container.nativeElement);
  }

  isLogged() {
    this.keycloakService.isLoggedIn().then(
      (logged) => {
        this.logged = logged
      }
    )
  }

  initCartItems(){
    this.cartItems = this.shoppingCart.getCartItems();
    this.sortItemsById();
  }

  scroll(el: HTMLElement) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

  public hasCarton(product:Product){
    return product.pack !== 1
  }

  public formatPrice(priceNumber: number): string {
      // Format the price as Swiss Franc (CHF)
    let formattedPrice = this.currencyPipe.transform(priceNumber, 'CHF');
    
    if (formattedPrice != null)
      formattedPrice = formattedPrice?.replace('CHF','CHF ')

    // Return the formatted price
    return formattedPrice || '';
  }

  public calculatedPrice(price:number,quantity:number){
    return (price*quantity)
  }

  public getTranslation(str: string) {
    return this.translate.instant(str);
  }

  public getCarton(item:CartItem){
    let quantity = item.quantity
    let perCarton = item.product.pack === null ? item.product.pack : 1
    return Math.round(quantity/perCarton)
  }

  public updateCartItem(cartItem:CartItem, quantityStr:string){
      const quantity = parseFloat(quantityStr)
      this.shoppingCart.removeItem(cartItem);
      if (quantity >0){
        let item = cartItem;
        item.quantity = quantity;
        this.shoppingCart.addItem(item)
      }
      this.sortItemsById();
      this.showMessageForItem(cartItem.product.productId);
  }
  
  sortItemsById(): void {
    this.cartItems = this.cartItems.sort((a, b) => a.product.productId - b.product.productId);
  }

  //Popup section
  public popupDelete(cartItem:CartItem){
    this.selectedProduct = cartItem.product;
    const deleteFunction : () => void = () => this.deleteItem(cartItem.product);
    const deleteText : string = this.translate.instant("REMOVE");
    const message : string = this.translate.instant("REMOVE ITEM MESSAGE");
    const title : string = this.translate.instant("WARNING");
    const deleteModel : FunctionModel = {
      message:deleteText,
      foo:deleteFunction
    }
    let modal = this.customModalComponent;
    modal.message = message;
    modal.title = title;
    modal.functionModels = [deleteModel];
    modal.colorTitle = "red";
    this.modalService.open(modal.myModal);
  } 

  public deleteItem(product:Product){
    this.shoppingCart.removeItemById(product.productId)
  }

  showMessageForItem(id:number) {
    let messageDiv = document.getElementById(`msg-${id}`);
    if (messageDiv == null)
      return

    messageDiv.style.display="block"

    setTimeout(() => {
      if (messageDiv != null)
      messageDiv.style.display = "none"
    }, 2000);
  }
  //Popup section ends

  public getTotal(){
    return this.shoppingCart.getTotal();
  }

  public getTotalTax(){
    return this.shoppingCart.getTotalTax();
  }

  public getSubtotal(){
    return this.shoppingCart.getSubtotal();
  }

}
