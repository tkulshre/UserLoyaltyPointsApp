package com.user;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Utils {
  public static boolean validateFirstName(String firstName) {
    return firstName.matches( "[A-Z][a-zA-Z]*" );
  }

  public static boolean validateEmail(String email) {
    boolean result = true;
    try {
      InternetAddress emailAddr = new InternetAddress(email);
      emailAddr.validate();
    } catch (AddressException ex) {
      result = false;
    }
    return result;
  }

  public static boolean validateAction(String action) {
    return (action.toLowerCase().equals("remove") || action.toLowerCase().equals("add"));
  }

  public static boolean  validateAmount(String amount) {
    try
    {
      double d = Double.parseDouble(amount);
    }
    catch(NumberFormatException nfe)
    {
      return false;
    }
    return true;
  }
}
