import { Component, inject, Input, OnInit } from '@angular/core';
import { Menu, Order, User } from '../models';
import { Router } from '@angular/router';
import { MenuStore } from '../menu.store';
import { RestaurantService } from '../restaurant.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit {

  // TODO: Task 3
  
  // @Input({ required: true })
  selectedItems!: Menu[];
  orders!: Order[];

  private restaurantSvc = inject(RestaurantService);
  private menuStore = inject(MenuStore);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  protected form!: FormGroup;

  ngOnInit(): void {
    this.selectedItems = this.menuStore.getSelectedMenus();
    this.form = this.createForm();
  }

  private createForm(): FormGroup {
    return this.fb.group({
      username: this.fb.control<string>('', [ Validators.required ]), 
      password: this.fb.control<string>('', [ Validators.required ])
    })
  }

  protected invalid() {
    return this.form.invalid;
  }

  protected processForm(): void {
    const user: User = {
      ...this.form.value
    };
    for (let i = 0; i < this.selectedItems.length; i++) {
      const menu: Menu = this.selectedItems[i];
      const order: Order = {
        id: menu.id, 
        price: menu.price, 
        quantity: menu.quantity
      }
      this.orders.push(order);
    }

    const finalOrder: Order[] = {
      ...this.orders
    }
    
    this.restaurantSvc.postOrder(user, finalOrder).then(data => {
      console.log(data);
    }).catch(err => {
      console.error(err);
    })
  }

  // addOrders(menus: Menu[]) {
  //   this.selectedItems = menus;
  // }

  getTotalPrice(): number {
    if (!this.selectedItems) {
      return 0;
    }
    const total = this.selectedItems.reduce((total, item) => total + (item.price * item.quantity), 0);
    if (total <= 0) {
      return 0;
    }
    return total;
  }

  startOver() {
    this.router.navigate(['/']);
  }

}
