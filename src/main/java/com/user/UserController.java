package com.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorController;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;

@RestController
public class UserController implements ErrorController {
  private static final String PATH = "/error";
  private final static int CACHE_MAX_SIZE = 10000;
  UserCache cache = new UserCache(CACHE_MAX_SIZE);

  Logger logger = LoggerFactory.getLogger(UserApplication.class);

  @Autowired
  UserService userService;

  @RequestMapping(value = PATH)
  public String error() {
    return "Error handling: Either First name or Email not provided, Please send both First name and Email in request";
  }

  @Override
  public String getErrorPath() {
    return PATH;
  }

  @RequestMapping("/add")
  public String addUserInfo(@RequestParam(value = "firstName") String firstName,
                        @RequestParam(value = "lastName", required = false) String lastName,
                        @RequestParam(value = "email") String email) {

    if (cache.getCache().containsKey(email)){
      return "User already exist in DB.";
    }

    int pointIfUserexistInDb = userService.containUserInDb(email);
    if (pointIfUserexistInDb != -1) {
      cache.getCache().put(email, pointIfUserexistInDb);
      return "User already exist in DB.";
    }

    User user = new User();

    if (Utils.validateFirstName(firstName)) {
      user.setFirstName(firstName);
    } else {
      return "Some problem in FirstName. Make sure you enter a proper name";
    }

    if (Utils.validateEmail(email)) {
      user.setEmail(email);
    } else {
      return "Some problem in Email. Make sure you enter a proper email id";
    }

    if (lastName != null) {
      user.setLastName(lastName);
    }

    if ( userService.addUserInDb(user) > 0){
      cache.getCache().put(user.getEmail(), user.getPoints());
      cache.getTransferCache().put(user.getEmail(), new ArrayList<Integer>());
      logger.info("User saved successfully");
    }

    return "User added succesfully";
  }

  @RequestMapping("/listAllUsers")
  public String listAllUsers() {
    StringBuilder sb = new StringBuilder();

    for(User user : userService.getAllUsersFromDb()){
      logger.info(user.toString());
      sb.append(user.toString());
    }

    return sb.toString();
  }

  @RequestMapping("/listTransfer")
  public String listTransfer(@RequestParam(value = "email") String email) {
    if (cache.getTransferCache().containsKey(email)){
      String result = makeTransferInformation(cache.getTransferCache().get(email));
      return result;
    }

    String actionList = userService.getUserTransferDataFromDb(email);
    if (actionList.equals("")){
      cache.getTransferCache().put(email, new ArrayList<Integer>());
      return "No Transfer Activity";
    }

    String[] actionsItems = actionList.split(",");
    ArrayList<Integer> actions = new ArrayList();
    for (String item : actionsItems){
      actions.add(Integer.parseInt(item));
    }
    cache.getTransferCache().put(email, actions);
    String result = makeTransferInformation(actions);
    return result;
  }

  public String makeTransferInformation(ArrayList<Integer> actionList){
    StringBuilder sb = new StringBuilder();
    int totalPoints = 0;

    for (int i = 0; i < actionList.size()-1; i+=2){
      if (actionList.get(i) == 0){
        totalPoints -= actionList.get(i+1);
        sb.append("POINTS REMOVED: " + actionList.get(i+1) + ", Total Points: " + totalPoints + "\n");
      } else {
        totalPoints += actionList.get(i+1);
        sb.append("POINTS ADDED: " + actionList.get(i+1) + ", Total Points: " + totalPoints + "\n");
      }
    }
    sb.append("CURRENT TOTAL POINTS: " + totalPoints);
    return sb.toString();
  }

  @RequestMapping("/transfer")
  public String transfer(@RequestParam(value = "email") String email,
                        @RequestParam(value = "action") String action,
                        @RequestParam(value = "amount") String amount) {
    int points = 0;
    points = validateCacheItemExist(cache.getCache(), email);
    if (points == -1) {
      return "User does not exist in DB, please add first";
    }

    validateTransferCacheItemExist(cache.getTransferCache(), email);

    if (action.equals("remove")) {
      if (Integer.parseInt(amount) > points) {
        return "User does not have sufficient points";
      }
      points -= Integer.parseInt(amount);
      updateDbAndTransferCacheData(email, points, amount, "0");
    } else { //add points
      points += Integer.parseInt(amount);
      updateDbAndTransferCacheData(email, points, amount, "1");
    }

    return "Action completed for User " + email + ", current points:" + points;
  }

  public void updateDbAndTransferCacheData(String email, int points, String amount, String action){
    userService.updateUserPointsInDb(email, points);
    userService.updateTransferInDb(email, action + "," + amount);
    cache.getCache().put(email, points);
    ArrayList actionList = cache.getTransferCache().get(email);
    actionList.add(Integer.parseInt(action));
    actionList.add(Integer.parseInt(amount));
  }

  public int validateCacheItemExist(LinkedHashMap<String, Integer> cache, String email) {
    int pointIfUserExistInDb = 0;

    if (!cache.containsKey(email)){
      pointIfUserExistInDb = userService.containUserInDb(email);
      if (pointIfUserExistInDb != -1) {
        cache.put(email, pointIfUserExistInDb);
      }
    } else {
      pointIfUserExistInDb = cache.get(email);
    }

    return pointIfUserExistInDb;
  }

  public void validateTransferCacheItemExist(LinkedHashMap<String, ArrayList<Integer>> cache, String email) {
    if (!cache.containsKey(email)){
      String actionList = userService.getUserTransferDataFromDb(email);
      String[] actionsItems = actionList.split(",");
      ArrayList<Integer> actions = new ArrayList();
      for (String item : actionsItems){
        actions.add(Integer.parseInt(item));
      }
      cache.put(email, actions);
    }
  }
}
