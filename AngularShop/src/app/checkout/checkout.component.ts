import { CurrencyPipe } from '@angular/common';
import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';
import { CartItem } from '../model/cartItem';
import { Order } from '../model/order';
import { User } from '../model/user';
import { ProductService } from '../service/product.service';
import { ShoppingCart } from '../service/shoppingCart';
import { UserService } from '../service/user.service';
import { CustomModalComponent } from '../modal/custom-modal/custom-modal.component';
import { OrderDetail } from '../model/orderDetail';
import { Address } from '../model/address';
import { OrderService } from '../service/order.service';
import { ExtendedModalService } from '../service/extendedModalService';
import { FunctionModel } from '../model/functionModel';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent {

  @ViewChild("CustomModalComponent") customeModalComponent !: CustomModalComponent
  @ViewChild("submitBT") submitBT !: ElementRef<HTMLButtonElement>;
  @ViewChild("container") container !: ElementRef<HTMLDivElement>;

  public keycloakProfile !: KeycloakProfile
  public submitButtonDisabled = false;
  public minDate !: string;
  private username !: string;
  private userId !: number;
  private erpId ?: number;

  //FORM
  isSameAddress: boolean = false;
  addressForm !: FormGroup;
  billingAddress !: FormGroup;
  deliveryAddress !: FormGroup;
  //ADMIN variable
  public isAdmin: boolean = false;
  public users: User[] = [];
  @ViewChild('customerSelect', { static: false }) customerSelectElement!: ElementRef;
  //ADMIN variable end

  //Popup
  private extendedModalService:ExtendedModalService

  constructor(
    private title: Title,
    private translateService: TranslateService,
    private keycloakService: KeycloakService,
    private productService: ProductService,
    private orderService : OrderService,
    private userService: UserService,
    private cart: ShoppingCart,
    private modalService: NgbModal,
    private router: Router,
    private currencyPipe: CurrencyPipe,
  ) { this.extendedModalService = new ExtendedModalService(modalService);
    
  }

  ngOnInit(): void {
    this.translateService.get('CHECKOUT').subscribe(element => {this.title.setTitle(element)})

    this.initAddressForm();
    //Check if it's logged in, then load profile
    this.checkAndProceedLoadingProfile()
    this.getRole();
    this.handleDate();
  }

  ngAfterViewInit() {
    this.scroll(this.container.nativeElement);
  }

  initAddressForm() {
    this.billingAddress = new FormGroup({
      billingStreetInput: new FormControl(''),
      billingCityInput: new FormControl(''),
      billingZipInput: new FormControl(''),
      billingCountryInput: new FormControl('')
    });

    this.deliveryAddress = new FormGroup({
      deliveryStreetInput: new FormControl(''),
      deliveryCityInput: new FormControl(''),
      deliveryZipInput: new FormControl(''),
      deliveryCountryInput: new FormControl('')
    });

    this.addressForm = new FormGroup({
      companyInput: new FormControl(''),
      firstnameInput: new FormControl(''),
      lastnameInput: new FormControl(''),
      emailInput: new FormControl(''),
      phoneInput: new FormControl(''),
      deliveryDateInput : new FormControl(''),
      commentInput : new FormControl(''),
      billingAddress: this.billingAddress,
      deliveryAddress: this.deliveryAddress
    });
  }

  scroll(el: HTMLElement) {
    el.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

  handleDate() {
    const today = new Date();
    const twoDaysFromNow = new Date(today);
    twoDaysFromNow.setDate(today.getDate() + 2);
    // Format the date to the desired format (e.g., yyyy-MM-dd)
    this.minDate = twoDaysFromNow.toISOString().slice(0, 10);
  }

  public checkAndProceedLoadingProfile() {
    this.keycloakService.isLoggedIn().then(
      (isLogged) => {
        if (isLogged) {
          this.keycloakService.loadUserProfile().then(
            (user) => {
            const username = user.username;
            if (username){
              this.username = username;
              this.loadUser(username);
            }
            }
          )
        }
        else {
          this.router.navigate(['/login'])
        }
      },
      (error: HttpErrorResponse) => {
        alert(error.message)
      }
    )
  }

  public loadUser(username:string) {
    this.userService.getUserdata(username).subscribe({
      next: (response) => {this.fillFormBackend(response); if(response.userId) this.userId = response.userId; if(response.erpId) this.erpId = response.erpId},
      error: (error) => {if(error.status === 404) this.redirectToProfilEdit(); 
        else 
        this.extendedModalService.popup(this.customeModalComponent,"Error " + error.status,error.message,"red")
      }
    })
  }

  //Form section
  fillFormBackend(user: User) {
     // Set value for the main address form controls
     this.addressForm.get('companyInput')?.setValue(user.company);
     this.addressForm.get('firstnameInput')?.setValue(user.firstname);
     this.addressForm.get('lastnameInput')?.setValue(user.lastname);
     this.addressForm.get('emailInput')?.setValue(user.email);
     this.addressForm.get('phoneInput')?.setValue(user.phone);
 
     // Set value for the nested billing address form group controls
     this.addressForm.get('billingAddress.billingStreetInput')?.setValue(user.billingAddress.street);
     this.addressForm.get('billingAddress.billingCityInput')?.setValue(user.billingAddress.city);
     this.addressForm.get('billingAddress.billingZipInput')?.setValue(user.billingAddress.postalCode);
     this.addressForm.get('billingAddress.billingCountryInput')?.setValue(user.billingAddress.country);
 
     // Set value for the nested delivery address form group controls
     this.addressForm.get('deliveryAddress.deliveryStreetInput')?.setValue(user.deliveryAddress.street);
     this.addressForm.get('deliveryAddress.deliveryCityInput')?.setValue(user.deliveryAddress.city);
     this.addressForm.get('deliveryAddress.deliveryZipInput')?.setValue(user.deliveryAddress.postalCode);
     this.addressForm.get('deliveryAddress.deliveryCountryInput')?.setValue(user.deliveryAddress.country);

     //Detail
     this.addressForm.get('deliveryDateInput')?.setValue(new Date());

  }

  getUserfromForm(): User {
    const company = this.addressForm.value.companyInput;
    const firstname = this.addressForm.value.firstnameInput;
    const lastname = this.addressForm.value.lastnameInput;
    const email = this.addressForm.value.emailInput;
    const phone = this.addressForm.value.phoneInput;
    const billingStreet = this.addressForm.value.billingAddress.billingStreetInput;
    const billingCity = this.addressForm.value.billingAddress.billingCityInput;
    const billingZip = this.addressForm.value.billingAddress.billingZipInput;
    const billingCountry = this.addressForm.value.billingAddress.billingCountryInput;

    const billingAddress: Address = {
      street: billingStreet,
      city: billingCity,
      postalCode: billingZip,
      country: billingCountry
    };

    let deliveryAddress: Address

    if (!this.isSameAddress) {
      const deliveryStreet = this.addressForm.value.deliveryAddress.deliveryStreetInput;
      const deliveryCity = this.addressForm.value.deliveryAddress.deliveryCityInput;
      const deliveryZip = this.addressForm.value.deliveryAddress.deliveryZipInput;
      const deliveryCountry = this.addressForm.value.deliveryAddress.deliveryCountryInput;
      deliveryAddress = {
        street: deliveryStreet,
        city: deliveryCity,
        postalCode: deliveryZip,
        country: deliveryCountry
      };
    }
    else
      deliveryAddress = billingAddress

    const user: User = {
      userId: this.userId,
      firstname: firstname,
      lastname: lastname,
      company: company,
      email: email,
      phone: phone,
      deliveryAddress: deliveryAddress,
      billingAddress: billingAddress,
      username: this.username
    }
    return user;
  }

  //Form section end
  toggleInput(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    this.isSameAddress = checkbox.checked;
    if (this.isSameAddress) {
      this.deliveryAddress.disable();
      return;
    }
    this.deliveryAddress.enable();
  }
  //Submit section
  onSubmit() {
    this.disableSubmit();

    if (this.addressForm.invalid) {
      let title = this.translateService.instant("WARNING");
      this.extendedModalService.popup(this.customeModalComponent,title,"FORM NOT VALID", "red");
      this.enableSubmit();
      return;
    }

    if (this.addressForm.value.deliveryDateInput == null || this.addressForm.value.deliveryDateInput <= new Date()) {
      let title = this.translateService.instant("WARNING");
      this.extendedModalService.popup(this.customeModalComponent, title,"DELIVERY DATE MISSING OR INVALID", "red");
      this.enableSubmit();
      return
    }

    if (this.username == null || this.username == "")
    {
      let title = this.translateService.instant("WARNING");
      this.extendedModalService.popup(this.customeModalComponent,title,"Username is empty!", "red");
      this.enableSubmit();
      return;
    }

    let potentialError : CartItem = this.cart.getItemWithoutQuantity()
    if (potentialError != null){
      let message = "The product "+potentialError.product.productId + " " + potentialError.product.productName + " doesn't have valid quantity";
      let title = this.translateService.instant("WARNING");
      let redirectFunction: () => void = () => this.redirect(potentialError.product.productId)
      let correction : FunctionModel = {
        buttonText: "See "+potentialError.product.productId,
        foo: redirectFunction
      }
      this.extendedModalService.popup(this.customeModalComponent,title,message,"red",[correction])
      return;
    }

    //Remove all products without quantity
    this.cart.removeAllItemsWithoutQuantity();
    //To resolve conflicts in case of duplicates
    this.cart.mergeItems();

    if (this.getTotal() < 300) {
      let message = this.translateService.instant("MINIMUM ORDER MESSAGE");
      let title = this.translateService.instant("WARNING");
      this.extendedModalService.popup(this.customeModalComponent,title, message, "red")
      this.enableSubmit();
      return;
    }

    const comment = this.addressForm.value.commentInput;

    const items: CartItem[] = this.cart.getOrder();
    const orderedProducts: OrderDetail[] = items.map(item => {
      return {
        quantity: item.quantity,
        product: item.product
      }
    })

    if (orderedProducts.length < 1) {
      this.extendedModalService.popup(this.customeModalComponent,"WARNING","No items", "red");
      this.enableSubmit();
      return;
    }

    //Make modal not closeable
    let title = this.translateService.instant('PLEASE WAIT')
    let ngbModalOptions: NgbModalOptions = {
      backdrop : 'static',
      keyboard : false
    };
    this.extendedModalService.popup(this.customeModalComponent,title,"wait","inherit",[], false,ngbModalOptions,true)

    const deliveryDate = this.addressForm.value.deliveryDateInput;
    const user = this.getUserfromForm();
    const order: Order = {
      user : user,
      orderDetails: orderedProducts,
      comment: comment,
      orderDate: deliveryDate
    }

    //Get current language
    let currentLang = this.translateService.currentLang.toUpperCase();

    this.orderService.postOrder(order,this.username,currentLang).subscribe({
      next: (response) => {this.processingCompletedOrder(); this.modalService.dismissAll();},
      error: (error:HttpErrorResponse) => {this.modalService.dismissAll();  
        let title = this.translateService.instant("WARNING");
        if (error.status == 429)
          this.extendedModalService.popup(this.customeModalComponent,title,"Too many requests. Please try again tomorrow", "red");
        else
          this.extendedModalService.popup(this.customeModalComponent,title,error.message, "red");
        this.enableSubmit();
      }
    })
  }

  redirect(productId:number){
    this.router.navigateByUrl("/product/"+productId)
  }

  public disableSubmit() {
    this.submitBT.nativeElement.textContent = this.translateService.instant("PLEASE WAIT");
    this.submitButtonDisabled = true;
  }

  public enableSubmit() {
    this.submitBT.nativeElement.textContent = this.translateService.instant("SUBMIT");
    this.submitButtonDisabled = false;
  }
  //Submit section end

  //modal section
  public redirectToProfilEdit(){
    this.router.navigateByUrl("/profile/edit");
    let message = this.translateService.instant('COMPLETE PROFILE');
    let title = this.translateService.instant('PROFILE UNFINISHED');
    let colorTitle = "black";
    this.extendedModalService.popup(this.customeModalComponent,title,message,colorTitle)
  }
  processingCompletedOrder() {
    let message = this.translateService.instant("ORDER CONFIRMATION MESSAGE");
    let title = this.translateService.instant("ORDER CONFIRMED");
    this.extendedModalService.popup(this.customeModalComponent,title,message,"black")
    this.enableSubmit();
    this.cart.clearCart();
    this.router.navigateByUrl("/confirmation")
  }
  //modal section ends

  //ADMIN SECTION
  private getRole() {
    let roles = this.keycloakService.getUserRoles()
    for (let i = 0; i < roles.length; i++) {
      if (roles[i] === "ADMIN") {
        this.isAdmin = true;
        return;
      }
    }
  }

  public getUsers(keyword: string) {
    if (keyword == null || keyword.length < 3) {
      this.extendedModalService.popup(this.customeModalComponent,"WARNING","Please type at least 3 characters", "red");
      return;
    }
    this.users = [];
    const regex = /\s+/g;
    keyword = keyword.replace(regex, '¿');
    this.userService.getUsersByKeyword(keyword).subscribe(
      (response) => {
        this.users = response;
        if (this.users.length > 0) {
          this.fillFormBackend(this.users[0]);
          if (this.users[0].username != null){
            this.username = this.users[0].username;
            let userId = this.users[0].userId;
            if (userId)
              this.userId = userId;
          }
        }
      }
    );
  }

  public stringContainAllOfArray(str: string, keywords: string[]) {
    for (let keyword of keywords) {
      if (!str.toLowerCase().includes(keyword.toLowerCase())) {
        return false;
      }
    }
    return true;
  }

  onOptionSelected() {
    let username = this.customerSelectElement.nativeElement.value;
    for (let i = 0; i < this.users.length; i++) {
      if (this.users[i].username === username) {
        this.fillFormBackend(this.users[i])
        this.username = this.users[i].username;
        let userid = this.users[i].userId;
        if (userid)
          this.userId=userid
        return;
      }
    }
  }

  //ADMIN SECTION END

  public getTranslation(str:string){
    return this.translateService.instant(str)
  }

  public formatPrice(priceNumber: number): string {
    // Format the price as Swiss Franc (CHF)
    let formattedPrice = this.currencyPipe.transform(priceNumber, 'CHF');

    if (formattedPrice != null)
      formattedPrice = formattedPrice?.replace('CHF', 'CHF ')

    // Return the formatted price
    return formattedPrice || '';
  }

  public getTotal() {
    if (this.erpId)
      return this.cart.getTotal(true);
    return this.cart.getTotal();
  }

}
