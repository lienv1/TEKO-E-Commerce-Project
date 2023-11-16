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
import { MainProfilComponent } from './profil/main-profil/main-profil.component';
import { OrderHistoryComponent } from './profil/order-history/order-history.component';
import { ProfilEditComponent } from './profil/profil-edit/profil-edit.component';

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
    path: 'profil',
    component: MainProfilComponent,
    children: [
      { path: 'edit', component: ProfilEditComponent },
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
