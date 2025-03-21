import { Component, inject, OnInit, Output } from '@angular/core';
import { Menu } from '../models';
import { RestaurantService } from '../restaurant.service';
import { MenuStore } from '../menu.store';
import { Router } from '@angular/router';
import { BehaviorSubject, Subject } from 'rxjs';
import { MenuService } from '../service/menu.service';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit {
  // TODO: Task 2

  menus: Menu[] = [];
  count!: number;
  selected: Menu[] = [];

  @Output()
  sendSelectedItems = new BehaviorSubject<Menu[]>(this.selected);

  private restaurantSvc = inject(RestaurantService);
  private menuStore = inject(MenuStore);
  private router = inject(Router);
  private menuSvc = inject(MenuService);

  ngOnInit(): void {
    this.menuStore.clearSelectedMenu();
    this.restaurantSvc.getMenuItems().then(data => {
      console.log(data);
      this.menus = data;
    }).catch(error => {
      console.error("Error fetching menus: ", error);
    });
  }

  addQuantity(m: Menu) {
    console.log(m);
    const menu = this.selected.find(allMenu => allMenu.id === m.id);
    
    if (!menu) {
      m.quantity = 1;
      this.selected.push(m);
      console.log('Added item: ', m);
    } else {
      menu.quantity++;
      this.selected = this.selected.filter(m => m.id !== menu.id);
      this.selected.push(menu);
    }
    this.menuStore.addMenu(m);
  }
  
  reduceQuantity(m: Menu) {
    // this.selected = this.selected.filter(menu => m.id !== menu.id);
    const menu = this.selected.find(allMenu => allMenu.id === m.id);
    if (menu) {
      menu.quantity--;
      if (menu.quantity <= 0) {
        menu.quantity = 0;
        this.menuStore.removeMenu(m.id);
      }
      this.selected = this.selected.filter(sMenu => sMenu.id !== menu.id);
      this.menuStore.removeMenu(m.id);
      this.selected.push(menu);
      this.menuStore.addMenu(menu);
    }
  }

  getQuantity(m: Menu): number {
    const menu = this.selected.find(allMenu => allMenu.id === m.id);
    if (!menu) {
      return 0;
    } else {
      return menu.quantity;
    }
  }

  getCount(){
    if (!this.selected) {
      return 0;
    }
    // return this.selected.length;
    return this.selected.reduce((count, item) => count + item.quantity, 0);
  }

  getTotalPrice(): number {
    if (!this.selected) {
      return 0;
    }
    const total = this.selected.reduce((total, item) => total + (item.price * item.quantity), 0);
    if (total <= 0) {
      return 0;
    }
    return total;
  }

  placeOrder() {
    // this.sendSelectedItems.next(this.selected);
    this.menuSvc.setSelectedItems(this.selected);
    // console.log("store: ", this.menuStore.getSelectedMenus);
    this.router.navigate(['/place']);
  }
}
