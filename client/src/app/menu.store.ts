
import { ComponentStore } from "@ngrx/component-store";
import { Observable } from "rxjs";
import { Injectable } from "@angular/core";
import { Menu } from "./models";

interface MenuState {
    selectedMenu: Menu[];
    loading: boolean;
    error: string | null;
}

@Injectable({
    providedIn: 'root', // Make the store available globally
  })
// Use the following class to implement your store
export class MenuStore extends ComponentStore<MenuState> {
    constructor() {
        super({
            selectedMenu: [], 
            loading: false, 
            error: null
        });
    }

    // Selectors
    readonly selectedMenu$: Observable<Menu[]> = this.select(state => state.selectedMenu);
    readonly loading$: Observable<boolean> = this.select(state => state.loading);
    readonly error$: Observable<string | null> = this.select(state => state.error);
    readonly selectedMenuCount$: Observable<number> = this.select(state => state.selectedMenu.length);
    
    readonly addMenu = this.updater((state, selectedMenu: Menu[]) => ({
        ...state, lineItems: [...state.selectedMenu, selectedMenu]
    }));

    getSelectedMenus(): Menu[] {
        return this.get().selectedMenu;
    }

    readonly clearSelectedMenu = this.updater((state) => ({
        ...state, selectedMenu: []
    }))
}