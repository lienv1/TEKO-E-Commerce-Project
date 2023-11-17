import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { FooterComponent } from './footer/footer.component';
import { NavbarComponent } from './navbar/navbar.component';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { ShoppingCart } from './service/shoppingCart';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { environment } from 'src/environments/environment';
import { AuthInterceptorService } from './service/auth-interceptor.service';
import { LoginComponent } from './login/login.component';
import { CartComponent } from './cart/cart.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { ProductPageComponent } from './product-page/product-page.component';
import { ThankYouPageComponent } from './thank-you-page/thank-you-page.component';
import { ShopComponent } from './shop/shop.component';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { CustomModalComponent } from './modal/custom-modal/custom-modal.component';
import { MainProfileComponent } from './profile/main-profile/main-profile.component';
import { OrderHistoryComponent } from './profile/order-history/order-history.component';
import { ProfileEditComponent } from './profile/profile-edit/profile-edit.component';


// Factory function required during AOT compilation
export function httpTranslateLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: environment.keycloakAPI,
        realm: environment.keycloakRealm,
        clientId: environment.keycloakClient
      },
      initOptions: {
        onLoad: 'check-sso',
        checkLoginIframe: false
      },
      enableBearerInterceptor: true,
      bearerPrefix: 'Bearer'
    });
}

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    FooterComponent,
    NavbarComponent,
    LoginComponent,
    CartComponent,
    CheckoutComponent,
    ProductPageComponent,
    ThankYouPageComponent,
    ShopComponent,
    AdminPageComponent,
    CustomModalComponent,
    MainProfileComponent,
    ProfileEditComponent,
    OrderHistoryComponent
  ],
  imports: [
    KeycloakAngularModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgbModule,
    NgxPaginationModule,
    ReactiveFormsModule,
    FormsModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: httpTranslateLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  providers: [ShoppingCart, CurrencyPipe, DatePipe, {
    provide: APP_INITIALIZER,
    useFactory: initializeKeycloak,
    multi: true,
    deps: [KeycloakService]
  }, /*{
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    }*/],
  bootstrap: [AppComponent]
})
export class AppModule { }
