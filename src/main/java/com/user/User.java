package com.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

public class User {
  private String firstName;
  private String lastName;
  private int points;
  private String email;

  public void User(String firstName, String lastName, String email){
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.points = 0;
  }

  public void User(String firstName, String email){
    this.firstName = firstName;
    this.email = email;
    this.points = 0;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    if (lastName == null) {
      return "";
    }
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String toString(){
    StringBuilder builder = new StringBuilder();
    builder.append(this.getFirstName())
            .append(", ")
            .append(this.getLastName())
            .append(", ")
            .append(this.getEmail())
            .append(", ")
            .append(this.getPoints());

    return builder.toString();
  }

}
