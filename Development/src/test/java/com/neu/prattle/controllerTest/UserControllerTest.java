package com.neu.prattle.controllerTest;

import com.neu.prattle.controller.UserController;
import com.neu.prattle.model.User;

import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


import javax.ws.rs.core.Response;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    UserController userController;
    private UserService userService;
    //private User user;
    //private User user1;


    @Before
  public void setUp(){
      userService = UserServiceImpl.getInstance();
      userController = new UserController();
      //user = new User("User", "Test", "testtest","pass1234","GMT");
      //user1 = new User("User", "Test", "anotherTest","pass1234","GMT");
  }


  @Test
  public void testCreateUser() {
      User user = new User("User", "Test", generateString(),"pass1234","GMT");
      Response response = userController.createUserAccount(user);
      assertEquals(200, response.getStatus());
  }

    @Test
    public void testCreateUserSameUser() {
      User user = new User("User", "Test", generateString(),"pass1234","GMT");
      userController.createUserAccount(user);
        Response response = userController.createUserAccount(user);
        assertEquals(409, response.getStatus());
    }

  @Test
  public void testGetUser(){
      String username = generateString();
    User user1 = new User("User", "Test",username,"pass1234","GMT");

    userController.createUserAccount(user1);
     Response response = userController.getUser(username);
     assertTrue(response.hasEntity());
  }

    @Test
    public void testGetUserThatDoesNotExist(){
        Response response = userController.getUser("DoNotExit");
        assertEquals(409, response.getStatus());
    }

  private String generateString () {
    int n = 8;
    {
      // chose a Character random from this String
      String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
              + "0123456789"
              + "abcdefghijklmnopqrstuvxyz";


      StringBuilder sb = new StringBuilder(n);

      for (int i = 0; i < n; i++) {

        int index = (int) (AlphaNumericString.length()
                * Math.random());

        // add Character one by one in end of sb
        sb.append(AlphaNumericString
                .charAt(index));
      }

      return sb.toString();
    }
  }
}
