import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';
import { CustomModalComponent } from 'src/app/modal/custom-modal/custom-modal.component';
import { Address } from 'src/app/model/address';
import { FunctionModel } from 'src/app/model/functionModel';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-admin-profile-edit',
  templateUrl: './admin-profile-edit.component.html',
  styleUrls: ['./admin-profile-edit.component.scss']
})
export class AdminProfileEditComponent {

  @ViewChild("CustomModalComponent") customModalComponent !: CustomModalComponent
  @ViewChild('selectUser') selectUserElement !: ElementRef<HTMLSelectElement>

  isSameAddress: boolean = false;
  user !: User
  public addressForm !: FormGroup;
  public billingAddress !: FormGroup;
  public deliveryAddress !: FormGroup;
  
  newlyRegistered: boolean = false;

  users:User[] = []
  username !: string

  constructor(private route:ActivatedRoute, private keycloakService: KeycloakService, private userService: UserService, private modalService: NgbModal, private router: Router, private title:Title, private translationService:TranslateService) {
  }

  ngOnInit(): void {
    this.title.setTitle("Profile Edit (Admin)")
    this.getLoginStatus();
    this.initAddressForm();
    this.initParam();
  }

  ngAfterViewInit(): void {
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
      erpInput : new FormControl(''),
      companyInput: new FormControl(''),
      firstnameInput: new FormControl(''),
      lastnameInput: new FormControl(''),
      emailInput: new FormControl(''),
      phoneInput: new FormControl(''),
      billingAddress: this.billingAddress,
      deliveryAddress: this.deliveryAddress
    });
  }
  
  initParam() {
    this.route.queryParamMap.subscribe(queryParams => {
      const usernameParam = queryParams.get('username');
      if (!usernameParam)
        return
      this.username = usernameParam;
      this.userService.getUserdata(this.username).subscribe({
        next: (response) => {this.fillFormBackend(response)},
        error: (error:HttpErrorResponse) => {this.popupModal(error.message,"ERROR","red")}
      })
    });
  }

  fillFormBackend(user: User) {
    // Set value for the main address form controls
    this.addressForm.get('erpInput')?.setValue(user.erpId);
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
  }

  toggleInput(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    this.isSameAddress = checkbox.checked;
    if (this.isSameAddress) {
      this.deliveryAddress.disable();
      return;
    }
    this.deliveryAddress.enable();
  }

  //KEYCLOAK API
  getLoginStatus() {
    this.keycloakService.isLoggedIn().then(
      (response) => {
        if (response === false) {
          this.modalRedirectToLogin();
          return;
        }
        else{
         
        }
      }
    )
  }
  //KEYCLOAK API END

  //BACKEND API

  updateProfil() {

    if (!this.addressForm.valid) {
      alert("INVALID Addressform")
      return;
    }
    const erpId = this.addressForm.value.erpInput;
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
      erpId: erpId,
      firstname: firstname,
      lastname: lastname,
      company: company,
      email: email,
      phone: phone,
      deliveryAddress: deliveryAddress,
      billingAddress: billingAddress,
      username: this.username
    }

    this.updateUser(user);
  }

  updateUser(user: User) {
      this.userService.updateUserAsAdmin(user, this.username).subscribe({
        next: (response) => {this.modalSuccess(); this.newlyRegistered = false; },
        error: (error: HttpErrorResponse) => { this.modalFail(error.message) }
      });
  }
  //BACKEND API END

  //MODAL SECTION
  modalSuccess() {
    let modal = this.customModalComponent
    modal.message = this.translationService.instant("ACCOUNT UPDATE TEXT");
    modal.title = this.translationService.instant("ACCOUNT UPDATE");;
    this.openModal(modal, true);
  }
  modalFail(message: string) {
    let modal = this.customModalComponent
    modal.message = `${this.translationService.instant('ACCOUNT UPDATE ERROR')} \n ${message}`;
    modal.title = this.translationService.instant("WARNING");
    modal.title = "Red"
    this.openModal(modal, true);
  }

  modalRedirectToLogin() {
    const redirectModal: FunctionModel = {
      buttonText: "Login",
      foo: () => this.redirectToLogin()
    }
    let modal = this.customModalComponent;
    modal.message = "You are not logged in. Please login to continue";
    modal.title = "Please login";
    modal.colorTitle = "red";
    modal.functionModels = [redirectModal];
    this.modalService.open(modal.myModal);
  }
  redirectToLogin() {
    this.router.navigateByUrl("/login");
  }
  //MODAL SECTION END

  public searchUsername(keyword: string) {
    if (keyword.length < 3) {
      this.popupModal("Please type at least 3 characters", "WARNING", "red");
      return;
    }
    this.users = [];
    const regex = /\s+/g;
    keyword = keyword.replace(regex, '¿');
    this.userService.getUsersByKeyword(keyword).subscribe(
      (response) => {
        this.users = response;
        if (this.users.length > 0) {
          //this.loadUserOrders(this.users[0].username) dont load here, just set the userid in parameter
          this.setUsernameParam(this.users[0].username)
          this.fillFormBackend(this.users[0])
        }
      },
      (error: HttpErrorResponse) => {
        this.popupModal(error.message, "Error!", "red");
      }
    )
  }

  getTranslation(str:string){

  }

  onOptionSelected() {
    if (!this.selectUserElement)
      return;
    const username = this.selectUserElement.nativeElement.value;
    if (!username)
      return;
    this.username = username
    this.setUsernameParam(this.username);
  }

  setUsernameParam(username:string) {
    if (username)
      this.router.navigate([], {
        queryParams: { username: username },
        queryParamsHandling: 'merge'
      });
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
    public popupModal(message: string, title: string, color: string) {
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
    //modal section ends
    //Popup section end

}
