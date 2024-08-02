import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { CartComponent } from './cart/cart.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { ShopComponent } from './shop/shop.component';
import { ThankYouPageComponent } from './thank-you-page/thank-you-page.component';
import { ProductPageComponent } from './product-page/product-page.component';
import { MainProfileComponent } from './profile/main-profile/main-profile.component';
import { OrderHistoryComponent } from './profile/order-history/order-history.component';
import { ProfileEditComponent } from './profile/profile-edit/profile-edit.component';
import { AdminMainComponent } from './admin/admin-main/admin-main.component';
import { AdminProfileEditComponent } from './admin/admin-profile-edit/admin-profile-edit.component';
import { AdminOrderHistoryComponent } from './admin/admin-order-history/admin-order-history.component';
import { AuthGuard } from './service/auth.guard';

const routes: Routes = [
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard]  },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'shop', component: ShopComponent, canActivate: [AuthGuard] },
  { path: 'shop/category/:Cat/:Group', component: ShopComponent },
  { path: 'shop/search', component: ShopComponent },
  { path: 'shop/favourite', component: ShopComponent },
  { path: 'cart', component: CartComponent, canActivate: [AuthGuard]  },
  { path: 'login', component: LoginComponent },
  {
    path: 'profile',
    component: MainProfileComponent,
    children: [
      { path: 'edit', component: ProfileEditComponent },
      { path: 'history', component: OrderHistoryComponent }
    ]
  },
  { path: 'checkout', component: CheckoutComponent, canActivate: [AuthGuard]},
  { path: 'product/:id',component:ProductPageComponent, canActivate: [AuthGuard]},
  { path: 'confirmation', component:ThankYouPageComponent},
  { path: 'admin', component:AdminMainComponent,
    children: [
      {path : "edit", component:AdminProfileEditComponent},
      {path : "history", component:AdminOrderHistoryComponent}
    ]
}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {scrollPositionRestoration: 'enabled'})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
