import { HttpClient } from "@angular/common/http";
import { inject } from "@angular/core";
import { lastValueFrom } from "rxjs";
import { Menu, Order, User } from "./models";

export class RestaurantService {

  private httpClient = inject(HttpClient);

  // TODO: Task 2.2
  // You change the method's signature but not the name
  getMenuItems() {
    return lastValueFrom(this.httpClient.get<Menu[]>(`/api/menu`));
  }

  // TODO: Task 3.2
  public postOrder(user: User, items: Order[]): Promise<any> {
    const body = {
      username: user.username, 
      password: user.password, 
      items: Array(items)
    }
    return lastValueFrom(this.httpClient.post(`/api/food_order`, body));
  }

}
