import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Menu } from '../models';

@Injectable({
  providedIn: 'root'
})
export class MenuService {

  private selectedItemsSource = new Subject<Menu[]>();
  
  selectedItems$ = this.selectedItemsSource.asObservable();

  setSelectedItems(items: Menu[]) {
    this.selectedItemsSource.next(items);
  }
}
