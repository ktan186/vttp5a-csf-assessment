package vttp.batch5.csf.assessment.server.repositories;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp.batch5.csf.assessment.server.models.Item;
import vttp.batch5.csf.assessment.server.models.Menu;


@Repository
public class OrdersRepository {

  @Autowired
  private MongoTemplate mongoTemplate;

  // TODO: Task 2.2
  // You may change the method's signature
  // Write the native MongoDB query in the comment below
  //
  //  Native MongoDB query here
  //
    // db.menus.find().sort({name: 1})
  public List<Menu> getMenu() {
    Query query = new Query().with(Sort.by(Sort.Direction.ASC, "name"));
    List<Document> results = mongoTemplate.find(query, Document.class, "menus");

    List<Menu> menus = new LinkedList<>();
    for (Document d: results) {
      Menu m = new Menu();
      m.setId(d.getString("_id"));
      m.setName(d.getString("name"));
      m.setDescription(d.getString("description"));
      m.setPrice(d.getDouble("price"));
      menus.add(m);
    }
    return menus;
  }

  // TODO: Task 4
  // Write the native MongoDB query for your access methods in the comment below
  //
  //  Native MongoDB query here
    // db.orders.insert({
    //     _id: orderId, 
    //     order_id: orderId, 
    //     payment_id: paymentId, 
    //     username: username, 
    //     timestamp: date, 
    //     items: items
    // })
  public void addOrder(String orderId, String paymentId, String username, Long timestamp, List<Item> items) {
    Date date = new Date(timestamp);
    Document d = new Document();
    d.put("_id", orderId);
    d.put("order_id", orderId);
    d.put("payment_id", paymentId);
    d.put("username", username);
    d.put("timestamp", date);
    d.put("items", items);

    Document insertedDoc = mongoTemplate.insert(d, "orders");
		ObjectId id = insertedDoc.getObjectId("_id");

		System.out.printf(">>>>> after insert: %s\n", insertedDoc.toJson());
    System.out.printf(">>>>> id after insert: %s\n", id.toHexString());
  }
}
