package com.neu.prattle;

import com.neu.prattle.controller.UserController;
import com.neu.prattle.model.User;

import org.junit.Test;

public class UserControllerTest {

  UserController userController = new UserController();

  @Test
  public void testCreateUserAccount(){
    User u = new User("TestUser234");
    userController.createUserAccount(u);
    userController.createUserAccount(u);
  }
}
