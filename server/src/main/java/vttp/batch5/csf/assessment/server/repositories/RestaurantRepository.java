package vttp.batch5.csf.assessment.server.repositories;

import javax.sql.RowSet;

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
}
