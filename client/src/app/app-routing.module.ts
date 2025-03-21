import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MenuComponent } from './components/menu.component';
import { PlaceOrderComponent } from './components/place-order.component';

const routes: Routes = [
  {path: "", component: MenuComponent}, 
  {path: "place", component: PlaceOrderComponent}, 
  {path: "**", redirectTo:"/", pathMatch: "full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)], 
  exports: [RouterModule]
})
export class AppRoutingModule { }