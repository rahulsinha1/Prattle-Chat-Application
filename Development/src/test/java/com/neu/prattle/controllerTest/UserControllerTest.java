package com.neu.prattle.controllerTest;

import com.neu.prattle.controller.UserController;
import com.neu.prattle.model.User;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;


public class UserControllerTest {
  private UserController userController;
  User user;

  @Before
  public void setUp(){
      user = new User("Test","Test", "testtest", "password", "GMT");
  }

  @Test
  public void testCreateUserAccount() throws Exception {

  }

}
