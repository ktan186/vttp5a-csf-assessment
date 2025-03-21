package vttp.batch5.csf.assessment.server.controllers;

import java.io.StringReader;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.csf.assessment.server.models.Menu;
import vttp.batch5.csf.assessment.server.models.User;
import vttp.batch5.csf.assessment.server.services.RestaurantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

  @Autowired
  private RestaurantService restaurantSvc;

  // TODO: Task 2.2
  // You may change the method's signature
  @GetMapping("/menu")
  public ResponseEntity<String> getMenus() {
    List<Menu> menus = restaurantSvc.getMenu();
    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Menu m : menus) {
      JsonObject jObject = Json.createObjectBuilder()
        .add("id", m.getId())
        .add("name", m.getName())
        .add("description", m.getDescription())
        .add("price", m.getPrice())
        .build();
      arrayBuilder.add(jObject);
    }
    JsonArray jArray = arrayBuilder.build();
    return ResponseEntity.ok(jArray.toString());
  }

  // TODO: Task 4
  // Do not change the method's signature
  @PostMapping(path="/food_order", consumes=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
    JsonReader jReader = Json.createReader(new StringReader(payload));
    JsonObject jObject = jReader.readObject();

    String username = jObject.getString("username");
    String password = jObject.getString("password");
    JsonArray items = jObject.getJsonArray("items");

    for (JsonObject j: items) {
      
    }

    User u = restaurantSvc.getUserById(username);
    if (!u.getPassword().contentEquals(password)) {
      JsonObject response = Json.createObjectBuilder().add("message", "Invalid username and/or password").build();
      return ResponseEntity.status(401).body(response.toString());
    }

    String orderId = UUID.randomUUID().toString().substring(0, 7);
    restaurantSvc.postPayment(orderId, username, null)
    return ResponseEntity.ok("{}");
  }
}
