import { Component, inject, Input, OnInit } from '@angular/core';
import { Menu, Order, User } from '../models';
import { Router } from '@angular/router';
import { MenuStore } from '../menu.store';
import { RestaurantService } from '../restaurant.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MenuService } from '../service/menu.service';
import { catchError, lastValueFrom, Observable } from 'rxjs';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit {

  // TODO: Task 3
  
  // @Input({ required: true })
  selectedItems: Menu[] = [];

  orders: Order[] = [];
  allItems!: any[];

  private restaurantSvc = inject(RestaurantService);
  private menuStore = inject(MenuStore);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private menuSvc = inject(MenuService);

  protected form!: FormGroup;
  protected items$!: Observable<Menu[]>;

  ngOnInit(): void {
    // this.selectedItems = this.menuStore.getSelectedMenus();
    this.items$ = this.menuStore.selectedMenu$;
    console.log('store items: ' + this.items$);
    this.items$.subscribe(data => {
      this.selectedItems = data;
    })
    console.log('selected: ' + JSON.stringify(this.selectedItems));

    // this.menuSvc.selectedItems$.subscribe(data => {
    //   this.selectedItems = data;
    //   console.log('selected: ' + JSON.stringify(this.selectedItems));
    //   for (var i = 0; i < data.length; i++) {
    //     console.log('data: ' + JSON.stringify(data[i]));
    //     const item: Menu = {
    //       id: data[i].id, 
    //       name: data[i].name, 
    //       description: data[i].description, 
    //       price: data[i].price, 
    //       quantity: data[i].quantity
    //     }
    //     console.log('item: ' + item);
    //     this.allItems.push(item);
    //   }
    // })
    // console.log('allItems: ' + JSON.stringify(this.allItems));
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
      const date = data.timestamp;
      const orderId = data.orderId;
      const paymentId = data.paymentId;
      const total = data.total;
      this.router.navigate(['/confirm', date, '/', orderId, '/', paymentId, '/', total]);
    }).catch(err => {
      console.error(err);
      alert(err.message);
    })
  }

  // addOrders(menus: Menu[]) {
  //   this.selectedItems = menus;
  // }

  getTotalPrice(): number {
    if (!this.items$) {
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
