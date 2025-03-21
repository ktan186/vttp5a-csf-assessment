import { Component } from '@angular/core';
import { Menu } from './models';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  selectedItems: Menu[] = [];
  
  onSelectedItems(items: Menu[]) {
    this.selectedItems = items;
    console.log('Received selected items: ' + this.selectedItems);
  }
}
