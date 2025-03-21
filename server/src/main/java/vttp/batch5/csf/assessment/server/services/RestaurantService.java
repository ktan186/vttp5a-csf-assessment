package vttp.batch5.csf.assessment.server.services;

import jakarta.json.JsonObject;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import vttp.batch5.csf.assessment.server.models.Menu;
import vttp.batch5.csf.assessment.server.models.User;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

  @Autowired
  private OrdersRepository ordersRepo ;

  @Autowired
  private RestaurantRepository restaurantRepo;

  // TODO: Task 2.2
  // You may change the method's signature
  public List<Menu> getMenu() {
    return ordersRepo.getMenu();
  }
  
  // TODO: Task 4
  public User getUserById(String username) {
    return restaurantRepo.getUser(username);
  }

  public ResponseEntity<String> postPayment(String orderId, String payer, Integer amount) {
    String url = "https://payment-service-production-a75a.up.railway.app/";

    JsonObject json = Json.createObjectBuilder()
      .add("order_id", orderId)
      .add("payer", payer)
      .add("payee", "Tan Kang Hui")
      .add("payment", amount)
      .build();

    HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    String payload = json.toString();

		HttpEntity<String> request = new HttpEntity<>(payload, headers);

    RestTemplate restTemplate = new RestTemplate();

    try {
			ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
			System.out.println(response.getBody());
			return response;
		} catch (Exception e) {
			System.out.println(e);
			throw e;
		}
  }

}
