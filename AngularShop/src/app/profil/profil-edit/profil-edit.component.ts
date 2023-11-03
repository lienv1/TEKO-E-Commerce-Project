import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';
import { CustomModalComponent } from 'src/app/modal/custom-modal/custom-modal.component';
import { Address } from 'src/app/model/address';
import { FunctionModel } from 'src/app/model/functionModel';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/service/user.service';


@Component({
  selector: 'app-profil-edit',
  templateUrl: './profil-edit.component.html',
  styleUrls: ['./profil-edit.component.scss']
})
export class ProfilEditComponent implements OnInit {

  @ViewChild("CustomModalComponent") customModalComponent !: CustomModalComponent
  
  isSameAddress: boolean = false;
  user !: User
  public addressForm !: FormGroup;
  public billingAddress !: FormGroup;
  public deliveryAddress !: FormGroup;
  username !: string

  constructor(private keycloakService: KeycloakService, private userService: UserService, private modalService:NgbModal, private router:Router) {
  }

  ngOnInit(): void {
    this.getLoginStatus();
    this.initAddressForm();
  }
  ngAfterViewInit(): void {
  }


  initAddressForm() {
    this.billingAddress = new FormGroup({
      billingStreetInput: new FormControl(''),
      billingCityInput: new FormControl(''),
      billingStateInput: new FormControl(''),
      billingZipInput: new FormControl(''),
      billingCountryInput: new FormControl('')
    });

    this.deliveryAddress = new FormGroup({
      deliveryStreetInput: new FormControl(''),
      deliveryCityInput: new FormControl(''),
      deliveryStateInput: new FormControl(''),
      deliveryZipInput: new FormControl(''),
      deliveryCountryInput: new FormControl('')
    });

    this.addressForm = new FormGroup({
      companyInput: new FormControl(''),
      firstnameInput: new FormControl(''),
      lastnameInput: new FormControl(''),
      emailInput: new FormControl(''),
      phoneInput: new FormControl(''),
      billingAddress: this.billingAddress,
      deliveryAddress: this.deliveryAddress
    });
  }

  fillFormKeycloak(user: KeycloakProfile) {
    this.addressForm.get('firstnameInput')?.setValue(user.firstName)
    this.addressForm.get('lastnameInput')?.setValue(user.lastName)
    this.addressForm.get('emailInput')?.setValue(user.email)
  }

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
    this.addressForm.get('billingAddress.billingStateInput')?.setValue(user.billingAddress.state);
    this.addressForm.get('billingAddress.billingZipInput')?.setValue(user.billingAddress.postalCode);
    this.addressForm.get('billingAddress.billingCountryInput')?.setValue(user.billingAddress.country);

    // Set value for the nested delivery address form group controls
    this.addressForm.get('deliveryAddress.deliveryStreetInput')?.setValue(user.deliveryAddress.street);
    this.addressForm.get('deliveryAddress.deliveryCityInput')?.setValue(user.deliveryAddress.city);
    this.addressForm.get('deliveryAddress.deliveryStateInput')?.setValue(user.deliveryAddress.state);
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
  getLoginStatus(){
    this.keycloakService.isLoggedIn().then(
      (response) => {
        if (response === false){
          this.modalRedirectToLogin();
          return;
        }
        this.getKeycloakUser();
      }
    )
  }
  getKeycloakUser() {
    this.keycloakService.loadUserProfile().then(
      (user) => {
        if (user.username)
          this.username = user.username;
        let fillFormKeycloak: () => void = () => this.fillFormKeycloak(user);
        this.getBackendUser(fillFormKeycloak)
      }
    )
  }
  //KEYCLOAK API END

  //BACKEND API
  getBackendUser(fillFormKeycloak: () => void) {
    this.userService.getUserdata(this.username).subscribe(
      (response: User) => {
        this.fillFormBackend(response);
      },
      (error: HttpErrorResponse) => {
        fillFormKeycloak();
      }
    )
  }

  updateProfil() {
    if (!this.addressForm.valid) {
      alert("INVALID Addressform")
      return;
    }
      const company = this.addressForm.value.companyInput;
      const firstname = this.addressForm.value.firstnameInput;
      const lastname = this.addressForm.value.lastnameInput;
      const email = this.addressForm.value.emailInput;
      const phone = this.addressForm.value.phoneInput;

      const billingStreet = this.addressForm.value.billingAddress.billingStreetInput;
      const billingCity = this.addressForm.value.billingAddress.billingCityInput;
      const billingState = this.addressForm.value.billingAddress.billingStateInput;
      const billingZip = this.addressForm.value.billingAddress.billingZipInput;
      const billingCountry = this.addressForm.value.billingAddress.billingCountryInput;

      const billingAddress : Address = {
        street : billingStreet,
        city : billingCity,
        state : billingState,
        postalCode : billingZip,
        country : billingCountry
      };

      let deliveryAddress : Address

      if (!this.isSameAddress) {
        const deliveryStreet = this.addressForm.value.deliveryAddress.deliveryStreetInput;
        const deliveryCity = this.addressForm.value.deliveryAddress.deliveryCityInput;
        const deliveryState = this.addressForm.value.deliveryAddress.deliveryStateInput;
        const deliveryZip = this.addressForm.value.deliveryAddress.deliveryZipInput;
        const deliveryCountry = this.addressForm.value.deliveryAddress.deliveryCountryInput;
        deliveryAddress = {
          street : deliveryStreet,
          city : deliveryCity,
          state : deliveryState,
          postalCode : deliveryZip,
          country : deliveryCountry
        };
      }
      else
        deliveryAddress = billingAddress

      const user : User = {
        firstname: firstname,
        lastname: lastname,
        company: company,
        email: email,
        phone: phone,
        deliveryAddress: deliveryAddress,
        billingAddress: billingAddress,
        username: this.username
      }
    console.log(user);
  }
  //BACKEND API END

  //MODAL SECTION
  modalRedirectToLogin() {
    
    const redirectModal : FunctionModel = {
      message:"Login",
      foo : () => this.redirectToLogin()
    }
    
    let modal = this.customModalComponent;
    modal.message = "You are not logged in. Please login to continue";
    modal.title = "Please login";
    modal.colorTitle = "red";
    modal.functionModels = [redirectModal];
    this.modalService.open(modal.myModal);
  }
  redirectToLogin(){
    this.router.navigateByUrl("/login");
  }
  //MODAL SECTION END

}
