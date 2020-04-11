package com.neu.prattle.controllerTest;

import com.neu.prattle.controller.UserController;
import com.neu.prattle.exceptions.FollowException;
import com.neu.prattle.exceptions.GroupDoesNotExistException;
import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import com.neu.prattle.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
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
        doThrow(new UserDoesNotExistException("User Does Not Exist")).when(userService).findUserByUsername("User");
        Response response = userController.getUser("DoNotExit");
        assertEquals(409, response.getStatus());
    }

    @Test
    public void testSearchResult(){
        User user1 = new User("User", "Test","User","pass1234","GMT");

        List<User> userList = new ArrayList<>();
        userList.add(user1);

        doReturn(userList).when(userService).searchUser("Te");

        Response response = userController.searchResult("Te");
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testSearchResultGroupDoesNotExistException(){
        doThrow(new UserDoesNotExistException("User Does Not Exist.")).when(userService).searchUser("Te");

        Response response = userController.searchResult("Te");
        assertEquals(409, response.getStatus());
    }

  @Test
  public void testFollowUser(){
    User user1 = new User("User", "Test","User","pass1234","GMT");
    User user2 = new User("User2", "Test2","User2","pass12342","GMT");

    doReturn(user1).when(userService).findUserByUsername(user1.getUsername());
    doReturn(user2).when(userService).findUserByUsername(user2.getUsername());
    doNothing().when(userService).followUser(user1,user2);

    Response response = userController.followUser(user1.getUsername(),user2.getUsername());
    assertEquals(200, response.getStatus());
  }


  @Test
  public void testFollowUserCannotFollow(){
    User user1 = new User("User", "Test","User","pass1234","GMT");
    User user2 = new User("User2", "Test2","User2","pass12342","GMT");

    doReturn(user1).when(userService).findUserByUsername(user1.getUsername());
    doReturn(user2).when(userService).findUserByUsername(user2.getUsername());
    doThrow(new FollowException("Already following this user")).when(userService).followUser(user1,user2);

    Response response = userController.followUser(user1.getUsername(),user2.getUsername());
    assertEquals(409, response.getStatus());
  }


  @Test
  public void testunFollowUser(){
    User user1 = new User("User", "Test","User","pass1234","GMT");
    User user2 = new User("User2", "Test2","User2","pass12342","GMT");

    doReturn(user1).when(userService).findUserByUsername(user1.getUsername());
    doReturn(user2).when(userService).findUserByUsername(user2.getUsername());
    doNothing().when(userService).unfollowUser(user1,user2);

    Response response = userController.unfollowUser(user1.getUsername(),user2.getUsername());
    assertEquals(200, response.getStatus());
  }


  @Test
  public void testunFollowUserCannotFollow(){
    User user1 = new User("User", "Test","User","pass1234","GMT");
    User user2 = new User("User2", "Test2","User2","pass12342","GMT");

    doReturn(user1).when(userService).findUserByUsername(user1.getUsername());
    doReturn(user2).when(userService).findUserByUsername(user2.getUsername());
    doThrow(new FollowException("Not following this user")).when(userService).unfollowUser(user1,user2);

    Response response = userController.unfollowUser(user1.getUsername(),user2.getUsername());
    assertEquals(409, response.getStatus());
  }


  @Test
  public void testGetFollowers(){
    User user1 = new User("User", "Test","User","pass1234","GMT");
    List<User> followerList = new ArrayList<>();
    followerList.add(user1);
    doReturn(user1).when(userService).findUserByUsername(user1.getUsername());
    doReturn(followerList).when(userService).getFollowers("User");
    Response response = userController.getFollowers("User");
    assertEquals(followerList, response.getEntity());
  }

  @Test
  public void testGetFollowersUserNotExist(){
    User user1 = new User("User", "Test","User","pass1234","GMT");
    List<User> followerList = new ArrayList<>();
    followerList.add(user1);
    doThrow(new UserDoesNotExistException("User Does Not Exist")).when(userService).getFollowers("User");
    Response response = userController.getFollowers("User");
    assertEquals(409, response.getStatus());
  }


  @Test
  public void testGetFollowing(){
    User user1 = new User("User", "Test","User","pass1234","GMT");
    List<User> followingList = new ArrayList<>();
    followingList.add(user1);
    doReturn(user1).when(userService).findUserByUsername(user1.getUsername());
    doReturn(followingList).when(userService).getFollowing("User");
    Response response = userController.getFollowing("User");
    assertEquals(followingList, response.getEntity());
  }

  @Test
  public void testGetFollowingUserNotExist(){
    User user1 = new User("User", "Test","User","pass1234","GMT");
    List<User> followingList = new ArrayList<>();
    followingList.add(user1);
    doThrow(new UserDoesNotExistException("User Does Not Exist")).when(userService).getFollowing("User");
    Response response = userController.getFollowing("User");
    assertEquals(409, response.getStatus());
  }


  @Test
  public void testSetStatus(){
    User user1 = new User("User", "Test","User","pass1234","GMT");

    doReturn(user1).when(userService).findUserByUsername("User");
    doNothing().when(userService).setStatus(user1.getUsername(),"Status new");
    Response response = userController.setStatus("User","Status new");
    assertEquals(200, response.getStatus());
  }

  @Test
  public void testSetStatusUserThatDoesNotExist(){
    doThrow(new UserDoesNotExistException("User Does Not Exist")).when(userService).setStatus("User","Status");
    Response response = userController.setStatus("User","Status");
    assertEquals(409, response.getStatus());
  }




}
