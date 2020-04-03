package com.neu.prattle.controllerTest;

import com.neu.prattle.controller.UserController;
import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.User;

import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


import javax.ws.rs.core.Response;
import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    UserController userController = new UserController();

    @Mock
    private UserService userService;


    @Before
  public void setUp() throws IllegalAccessException, NoSuchFieldException  {
        Field f1 = userController.getClass().getDeclaredField("accountService");
        f1.setAccessible(true);
        f1.set(userController, userService);
    }


  @Test
  public void testCreateUser() {
      User user = new User("User", "Test", "User","pass1234","GMT");

      doNothing().when(userService).addUser(user);
      Response response = userController.createUserAccount(user);
      assertEquals(200, response.getStatus());
  }

    @Test
    public void testCreateUserSameUser() {
      User user = new User("User", "Test", "User","pass1234","GMT");

      doThrow(new UserAlreadyPresentException("User Already exist")).when(userService).addUser(user);
        Response response = userController.createUserAccount(user);
        assertEquals(409, response.getStatus());
    }

  @Test
  public void testGetUser(){
        User user1 = new User("User", "Test","User","pass1234","GMT");

        doReturn(user1).when(userService).findUserByUsername("User");
        Response response = userController.getUser("User");
     assertEquals(user1, response.getEntity());
  }

    @Test
    public void testGetUserThatDoesNotExist(){
        User user = new User("User", "Test", "User","pass1234","GMT");

        doThrow(new UserDoesNotExistException("User Does Not Exist")).when(userService).findUserByUsername("User");
        Response response = userController.getUser("DoNotExit");
        assertEquals(409, response.getStatus());
    }

}
