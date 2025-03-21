package vttp.batch5.csf.assessment.server.repositories;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.batch5.csf.assessment.server.models.User;

// Use the following class for MySQL database
@Repository
public class RestaurantRepository {
    
    @Autowired
    private JdbcTemplate template;

    public static final String SQL_GET_USER = "select * from customers where username = ?";
    public static final String SQL_ADD_ORDER = "insert place_orders(order_id, payment_id, order_date, total, username) values (?, ?, ?, ?, ?)";

    public User getUser(String username) {
        SqlRowSet rs = template.queryForRowSet(SQL_GET_USER, username);
        if (!rs.next()) {
            System.out.println("User not found!");
            return null;
        }
        User u = new User();
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        return u;
    }

    public Boolean addOrderDetails(String orderId, String paymentId, Long timestamp, Double total, String username) {
        Date date = new Date(timestamp);
        int addedOrder = template.update(SQL_ADD_ORDER, orderId, paymentId, date, total, username);
        System.out.println("added order: " + addedOrder);
        return addedOrder > 0;
    }

}
