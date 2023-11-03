import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup} from '@angular/forms';
import { KeycloakService } from 'keycloak-angular';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/service/user.service';


@Component({
  selector: 'app-profil-edit',
  templateUrl: './profil-edit.component.html',
  styleUrls: ['./profil-edit.component.scss']
})
export class ProfilEditComponent implements OnInit {

  isSameAddress: boolean = false;
  user !: User
  public addressForm !: FormGroup;
  public billingAddress !: FormGroup;
  public deliveryAddress !: FormGroup;
  username !: string

  constructor(private keycloakService: KeycloakService, private userService:UserService) {
  }

  ngOnInit(): void {
    this.initAddressForm();
  }
  ngAfterViewInit(): void {
    this.getUserKeycloak();
  }


  initAddressForm() {
    this.billingAddress = new FormGroup({
      billingAddressInput: new FormControl(''),
      billingCityInput: new FormControl(''),
      billingStateInput: new FormControl(''),
      billingZipInput: new FormControl(''),
      billingCountryInput: new FormControl('')
    });

    this.deliveryAddress = new FormGroup({
      deliveryAddressInput: new FormControl(''),
      deliveryCityInput: new FormControl(''),
      deliveryStateInput: new FormControl(''),
      deliveryZipInput: new FormControl(''),
      deliveryCountryInput: new FormControl('')
    });

    this.addressForm = new FormGroup({
      companyInput:new FormControl(''), 
      firstnameInput: new FormControl(''),
      lastnameInput: new FormControl(''),
      emailInput: new FormControl(''),
      phoneInput: new FormControl(''),
      billingAddress: this.billingAddress,
      deliveryAddress: this.deliveryAddress
    });
  }

  toggleInput(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    this.isSameAddress = checkbox.checked;
    if (this.isSameAddress){
      this.deliveryAddress.disable();
      return;
    }
    this.deliveryAddress.enable();
  }

  //KEYCLOAK API
  getUserKeycloak() {
    this.keycloakService.loadUserProfile().then(
      (user) => {
        if (user.username)
          this.username = user.username;
        this.addressForm = new FormGroup({
          firstnameInput : new FormControl(user.firstName),
          lastnameInput : new FormControl(user.lastName),
          emailInput : new FormControl(user.email)
        })
      }
    ),
    (error:HttpErrorResponse) => {
      alert("Failed to load user");
    }
  }
  //KEYCLOAK API END

  //BACKEND API
  getUser() {
    this.userService.getUserdata(this.username).subscribe(
      (response:User) => {
        console.log(response)
      }
    )
  }

  updateProfil() {
    if (this.addressForm.valid) {
      console.log(this.addressForm.value);
      // Handle your form submission, e.g., send it to a server
    } else {
      console.log('Form is not valid');
    }
  }
  //BACKEND API END

}
