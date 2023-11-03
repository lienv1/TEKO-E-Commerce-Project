import { CurrencyPipe } from '@angular/common';
import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { UntypedFormGroup, UntypedFormBuilder, UntypedFormControl, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
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
  public addressForm !: UntypedFormGroup;
  public submitButtonDisabled = false;
  public minDate !: string;
  private username !: string;

  //ADMIN variable
  public isAdmin: boolean = false;
  public users: any[] = [];
  @ViewChild('customerSelect', { static: false }) customerSelectElement!: ElementRef;
  //ADMIN variable end

  constructor(
    private title: Title,
    private translateService: TranslateService,
    private keycloakService: KeycloakService,
    private productService: ProductService,
    private userService: UserService,
    private cart: ShoppingCart,
    private modalService: NgbModal,
    private router: Router,
    private currencyPipe: CurrencyPipe,
    private formBuilder: UntypedFormBuilder
  ) {
  }
/*
  ngOnInit(): void {
    this.initForm();
    //Check if it's logged in, then load profile
    this.checkAndProceedLoadingProfile()
    this.title.setTitle("Checkout");
    this.getRole();
    this.handleDate();
  }

  ngAfterViewInit() {
    this.scroll(this.container.nativeElement);
  }


  initForm() {
    this.addressForm = this.formBuilder.group({
      company: new UntypedFormControl('', Validators.required),
      firstName: new UntypedFormControl('', Validators.required),
      lastName: new UntypedFormControl('', Validators.required),
      phone: new UntypedFormControl('', Validators.required),
      email: new UntypedFormControl('', [Validators.required, Validators.email, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')]),
      street: new UntypedFormControl('', Validators.required),
      city: new UntypedFormControl('', Validators.required),
      postalCode: new UntypedFormControl('', Validators.required),
      delivery: new UntypedFormControl(new Date()),
      comment: new UntypedFormControl('')
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
          this.loadUser();
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

  public loadUser() {
    this.keycloakService.loadUserProfile().then(
      (user) => {
        this.keycloakProfile = user;
        this.fillForm(user);
        if (user.username != null)
          this.username = user.username
      }
    )
  }

  //Form section
  fillForm(user: any) {
    const company = user.attributes.company != null ? user.attributes.company.toString() : ""; //toString() needed, otherwise it returns an array
    const phone = user.attributes.phone != null ? user.attributes.phone.toString() : "";
    const street = user.attributes.street != null ? user.attributes.street.toString() : "";
    const zip = user.attributes.zip != null ? user.attributes.zip.toString() : "";
    const city = user.attributes.city != null ? user.attributes.city.toString() : "";

    this.addressForm = new UntypedFormGroup({
      company: new UntypedFormControl(company), 
      firstName: new UntypedFormControl(user.firstName),
      lastName: new UntypedFormControl(user.lastName),
      phone: new UntypedFormControl(phone),
      email: new UntypedFormControl(user.email),
      street: new UntypedFormControl(street),
      postalCode: new UntypedFormControl(zip),
      city: new UntypedFormControl(city),
      delivery: new UntypedFormControl(new Date()),
      comment: new UntypedFormControl(' ')
    });
  }

  getUserfromForm(): User {

    let username: string | undefined = this.username;
    let company: string = this.addressForm.value.company;
    let phone: string = this.addressForm.value.phone;
    let email: string = this.addressForm.value.email;
    let firstname: string = this.addressForm.value.firstName;
    let lastname: string = this.addressForm.value.lastName;
    let street: string = this.addressForm.value.street;
    let postalCode: string = this.addressForm.value.postalCode;
    let city: string = this.addressForm.value.city;

    const user: User = {
      username: username,
      company: company,
      phone: phone,
      email: email,
      firstname: firstname,
      lastname: lastname,
      street: street,
      postalCode: postalCode,
      city: city
    }
    return user
  }

  //Form section end

  //Submit section
  onSubmit() {

    this.disableSubmit();

    if (this.addressForm.invalid) {
      alert("FORM NOT VALID");
      this.enableSubmit();
      return;
    }

    if (this.addressForm.value.delivery == null || this.addressForm.value.delivery <= new Date()) {
      this.popupModal("DELIVERY DATE MISSING OR INVALID", "WARNING", "red");
      this.enableSubmit();
      return
    }

    if (this.username == null || this.username == "")
    {
      this.popupModal("Username is empty!","WARNING","red");
      this.enableSubmit();
      return;
    }

    //To resolve conflicts in case of duplicates
    this.cart.mergeItems();

    if (this.getTotal() < 300) {
      let message = this.translateService.instant("MINIMUM ORDER MESSAGE");
      let title = this.translateService.instant("WARNING");
      this.popupModal(message, title, "red");
      this.enableSubmit();
      return;
    }

    const comment = this.addressForm.value.comment;

    const items: CartItem[] = this.cart.getOrder();
    const orderedProducts: OrderDetail[] = items.map(item => {
      return {
        id: item.product.productId,
        quantity: item.quantity,
        product: item.product
      }
    })

    if (orderedProducts.length < 1) {
      this.popupModal("No items", "Error", "red");
      this.enableSubmit();
      return;
    }

    const deliveryDate = this.addressForm.value.delivery;
    const user = this.getUserfromForm();
    const order: Order = {
      orderDetails: orderedProducts,
      orderDate: deliveryDate,
      comment: comment
    }

    this.productService.sendOrder(user, order).subscribe(
      (response: HttpStatusCode) => {
        this.processingCompletedOrder();
      },
      (error: HttpErrorResponse) => {
        this.popupModal(error.message, "ERROR", "red")
        this.enableSubmit();
      }
    )
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
  public popupModal(message: string, title: string, color: string) {
    let modal = this.customeModalComponent;
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
      }, 1000);
    }
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
      this.popupModal("Please type at least 3 characters", "WARNING", "red");
      return;
    }
    this.users = [];

    this.userService.getUsersByUsername(keyword).subscribe(
      (response) => {
        let data = response;
        for (let i = 0; i < data.length; i++) {

          let searchIndex = data[i].username + " " + data[i].attributes.company.toString();
          if (!this.stringContainAllOfArray(searchIndex, keyword.split(" "))) {
            continue;
          }
          this.users.push(data[i]);
        }
        if (this.users.length > 0) {
          this.fillForm(this.users[0]);
          if (this.users[0].username != null){
            this.username = this.users[0].username;
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
        this.fillForm(this.users[i])
        this.username = this.users[i].username;
        return;
      }
    }
  }

  public getTranslation(str:string){
    return this.translateService.instant(str)
  }

  //ADMIN SECTION END

  processingCompletedOrder() {
    let modal = this.customeModalComponent;
    modal.message = this.translateService.instant("ORDER CONFIRMATION MESSAGE");
    modal.title = this.translateService.instant("ORDER CONFIRMED");
    this.openModal(modal, false);
    this.enableSubmit();
    this.cart.clearCart();
    this.router.navigateByUrl("/confirmation")
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
    return this.cart.getSubtotal();
  }
*/
}
