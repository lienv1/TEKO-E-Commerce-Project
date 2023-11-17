import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { CartComponent } from './cart/cart.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { ShopComponent } from './shop/shop.component';
import { ThankYouPageComponent } from './thank-you-page/thank-you-page.component';
import { ProductPageComponent } from './product-page/product-page.component';
import { MainProfileComponent } from './profile/main-profile/main-profile.component';
import { OrderHistoryComponent } from './profile/order-history/order-history.component';
import { ProfileEditComponent } from './profile/profile-edit/profile-edit.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'shop', component: ShopComponent },
  { path: 'shop/category/:Cat/:Group', component: ShopComponent },
  { path: 'shop/search', component: ShopComponent },
  { path: 'shop/favourite', component: ShopComponent },
  { path: 'cart', component: CartComponent },
  { path: 'login', component: LoginComponent },
  {
    path: 'profile',
    component: MainProfileComponent,
    children: [
      { path: 'edit', component: ProfileEditComponent },
      { path: 'history', component: OrderHistoryComponent }
    ]
  },
  { path: 'checkout', component: CheckoutComponent},
  { path: 'product/:id',component:ProductPageComponent},
  { path: 'confirmation', component:ThankYouPageComponent},
  { path: 'admin', component:AdminPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
