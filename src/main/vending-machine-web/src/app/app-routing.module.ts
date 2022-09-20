import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {LoginComponent} from "./login/login.component";
import {CreateComponent} from "./create/create.component";
import {BuyerComponent} from "./buyer/buyer.component";
import {SellerComponent} from "./seller/seller.component";

const routes: Routes = [
  {path:'login', component: LoginComponent},
  {path: 'create', component: CreateComponent},
  {path: 'buyer', component: BuyerComponent},
  {path: 'seller', component: SellerComponent},
  {path: '', component: HomeComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
