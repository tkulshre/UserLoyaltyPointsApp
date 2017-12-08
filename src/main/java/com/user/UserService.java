package com.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.dao.EmptyResultDataAccessException;

@Service
public class UserService {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public int addUserInDb(User user){
    String sql = "INSERT INTO USER(first_name, last_name, email) VALUES(?,?,?)";
    return jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getEmail());
  }

  public int updateTransferInDb(String email, String data){
    String sql = "UPDATE USER_TRANSFER SET action_list = action_list + \'" + data + "\' WHERE email = \'" + email + "\'";
    return jdbcTemplate.update(sql);
  }

  public int containUserInDb(String email){
    try {
      String sql = "SELECT points FROM USER WHERE email = \'" + email + "\'";
      Integer points = (Integer) jdbcTemplate.queryForObject(sql, Integer.class);
      return points.intValue();
    } catch (EmptyResultDataAccessException e){
      return -1;
    }
  }

  public String getUserTransferDataFromDb(String email){
    try {
      String sql = "SELECT action_list FROM USER_TRANSFER WHERE email = \'" + email + "\'";
      String actionList = (String) jdbcTemplate.queryForObject(sql, String.class);
      return actionList;
    } catch (EmptyResultDataAccessException e){
      return "";
    }
  }

  public int updateUserPointsInDb(String email, Integer points){
    String sql = "UPDATE USER SET points =" + points + " WHERE email = \'" + email + "\'";
    return jdbcTemplate.update(sql);
  }

  public List<User> getAllUsersFromDb(){
    return jdbcTemplate.query("SELECT * FROM USER", new RowMapper<User>(){

      public User mapRow(ResultSet rs, int arg1) throws SQLException {
        User user = new User();
        user.setPoints(rs.getInt("points"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        return user;
      }
    });
  }
}
