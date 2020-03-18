package com.neu.prattle.controllerTest;

import com.neu.prattle.controller.UserController;
import com.neu.prattle.model.User;

import org.junit.Before;
import org.junit.Test;

public class UserControllerTest {

  UserController userController ;
  User user;

  @Before
  public void setUp(){
      user = new User("Test","Test", "testtest", "password", "GMT");
  }

  @Test
  public void testCreateUserAccount(){

  }

}
