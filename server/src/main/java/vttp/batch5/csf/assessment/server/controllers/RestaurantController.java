package vttp.batch5.csf.assessment.server.controllers;

import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
import vttp.batch5.csf.assessment.server.models.Item;
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
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) throws NoSuchAlgorithmException {
    JsonReader jReader = Json.createReader(new StringReader(payload));
    JsonObject jObject = jReader.readObject();

    String username = jObject.getString("username");
    String rawPassword = jObject.getString("password");

    MessageDigest md = MessageDigest.getInstance("SHA-224");
    md.update(rawPassword.getBytes());
    byte[] result = md.digest();
    String hash = new String(result);

    JsonArray items = jObject.getJsonArray("items");
    

    Double total = 0.00;
    List<Item> orderItems = new ArrayList<>();
    for (int i = 0; i < items.size(); i++) {
      JsonObject item = items.getJsonObject(i);

      Item mItem = new Item();
      mItem.setId(item.getString("id"));
      mItem.setPrice(Double.valueOf(item.getString("price")));
      mItem.setQuantity(item.getInt("quantity"));
      orderItems.add(mItem);

      Double subTot = mItem.getPrice() * mItem.getQuantity();
      total += subTot;
    }

    User u = restaurantSvc.getUserById(username);
    if (!u.getPassword().contentEquals(hash)) {
      JsonObject response = Json.createObjectBuilder().add("message", "Invalid username and/or password").build();
      return ResponseEntity.status(401).body(response.toString());
    }

    String orderId = UUID.randomUUID().toString().substring(0, 7);
    try {
      ResponseEntity<String> response = restaurantSvc.postPayment(orderId, username, total);

      JsonReader jReader2 = Json.createReader(new StringReader(response.getBody()));
      JsonObject jObject2 = jReader2.readObject();

      String rpaymentId = jObject2.getString("payment_id");
      String rorderId = jObject2.getString("order_id");
      Long rtimestamp = Long.getLong(jObject2.getString("timestamp"));

      try {
        restaurantSvc.insertOrderToDB(rorderId, rpaymentId, username, rtimestamp, total, orderItems);

        JsonObject successMessage = Json.createObjectBuilder()
          .add("orderId", rorderId)
          .add("paymentId", rpaymentId)
          .add("total", total)
          .add("timestamp", jObject2.getString("timestamp"))
          .build();
        return ResponseEntity.status(200).body(successMessage.toString());
      } catch (Exception e) {
        System.out.println("Failed to insert to DB.");
        JsonObject response1 = Json.createObjectBuilder().add("message", e.toString()).build();
        return ResponseEntity.status(500).body(response1.toString());
      }

    } catch (Exception e) {
      System.out.println("error with resttemplate: " + e);
      JsonObject response2 = Json.createObjectBuilder().add("message", e.toString()).build();
        return ResponseEntity.status(500).body(response2.toString());
    }
    
  }
}
