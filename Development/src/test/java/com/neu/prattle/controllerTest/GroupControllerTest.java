package com.neu.prattle.controllerTest;

import com.neu.prattle.controller.GroupController;
import com.neu.prattle.model.Group;

import com.neu.prattle.model.Moderator;
import com.neu.prattle.model.User;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class GroupControllerTest {
    GroupController groupController;
    UserService userService;
    private Group group2;

  @Before
  public void setUp() {
     userService = UserServiceImpl.getInstance();
     groupController = new GroupController();
  }

  @Test
    public void testCreateGroup(){
    String groupName = generateString();
    Group g = new Group(groupName);
    String userName = generateString();
    g.setCreatedBy(userName);
    g.setName(groupName);
    User u = new User(userName);
    u.setFirstName(generateString());
    userService.addUser(u);
        Response response = groupController.createGroup(g);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testCreateGroupMessage(){
      String groupName = generateString();
      Group g = new Group(groupName);
      String userName = generateString();
      g.setCreatedBy(userName);
      g.setName(groupName);
      User u = new User(userName);
      u.setFirstName(generateString());
      userService.addUser(u);
        Response response = groupController.createGroup(g);
        assertEquals("OK", response.getStatusInfo().getReasonPhrase());
    }

    @Test
    public void testCreateGroupAlreadyExist(){
      String groupName = generateString();
      Group g = new Group(groupName);
      String userName = generateString();
      g.setCreatedBy(userName);
      g.setName(groupName);
      User u = new User(userName);
      u.setFirstName(generateString());
      userService.addUser(u);
      groupController.createGroup(g);
        Response response = groupController.createGroup(g);
        assertEquals(409, response.getStatus());
    }

    @Test
    public void testCreateGroupAlreadyExistMessage(){
      String groupName = generateString();
      Group g = new Group(groupName);
      String userName = generateString();
      g.setCreatedBy(userName);
      g.setName(groupName);
      User u = new User(userName);
      u.setFirstName(generateString());
      userService.addUser(u);
        groupController.createGroup(g);
        Response response = groupController.createGroup(g);
        assertEquals("Conflict", response.getStatusInfo().getReasonPhrase());
    }

    @Test
    public void testGetGroup(){
      String groupName = generateString();
      Group g = new Group(groupName);
      String userName = generateString();
      g.setCreatedBy(userName);
      g.setName(groupName);
      User u = new User(userName);
      u.setFirstName(generateString());
      userService.addUser(u);
      groupController.createGroup(g);
        Response response = groupController.getGroup(userName);
        ArrayList<Group> responseGroup = (ArrayList<Group>) response.getEntity();

        assertEquals(g,responseGroup.get(0));
    }

  @Test
  public void testGetAllGroup(){
    String userName = generateString();
    String groupName = generateString();
    User u = new User(userName);
    u.setFirstName(generateString());
    userService.addUser(u);
    Group g = new Group(groupName);
    g.setCreatedBy(userName);
    groupController.createGroup(g);

    Group g1 = new Group(generateString());
    g1.setCreatedBy(userName);
    groupController.createGroup(g1);

    Response response = groupController.getAllUserGroups(userName);
    List<Group> responseGroup = (List<Group>) response.getEntity();

    assertEquals(g,responseGroup.get(0));
    assertEquals(g1,responseGroup.get(1));
  }



    @Test
    public void testGroupDoesNotExist(){
        Response response = groupController.getGroup(generateString());
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testGroupDoesNotExistMessage(){
        Response response = groupController.getGroup(generateString());
        assertEquals("OK", response.getStatusInfo().getReasonPhrase());
    }

    @Test
    public void testUserAlreadyPresentInGroupException(){
      String groupName = generateString();
      Group g = new Group(groupName);
      String userName = generateString();
      g.setCreatedBy(userName);
      g.setName(groupName);
      User u = new User(userName);
      u.setFirstName(generateString());
      userService.addUser(u);
      groupController.createGroup(g);

        groupController.addUser(groupName,userName);
        Response response = groupController.addUser(groupName,userName);
        assertEquals("OK",response.getStatusInfo().getReasonPhrase());
    }

  @Test
  public void testRemoveUser(){
    String groupName = generateString();
    Group g = new Group(groupName);
    String userName = generateString();
    g.setCreatedBy(userName);
    g.setName(groupName);
    User u = new User(userName);
    u.setFirstName(generateString());
    userService.addUser(u);
    groupController.createGroup(g);
    Response response = groupController.removeUser(groupName,userName);
    assertEquals("OK",response.getStatusInfo().getReasonPhrase());
  }

  @Test
  public void testAddModerator(){
    String groupName = generateString();
    Group g = new Group(groupName);
    String userName = generateString();
    g.setCreatedBy(userName);
    g.setName(groupName);
    User u = new User(userName);
    u.setFirstName(generateString());
    userService.addUser(u);
    groupController.createGroup(g);
    Response response = groupController.addModerator(groupName,userName);
    assertEquals("OK",response.getStatusInfo().getReasonPhrase());
  }

  @Test
  public void testRemoveModerator(){
    String groupName = generateString();
    Group g = new Group(groupName);
    String userName = generateString();
    g.setCreatedBy(userName);
    g.setName(groupName);
    User u = new User(userName);
    u.setFirstName(generateString());
    userService.addUser(u);
    groupController.createGroup(g);
    Response response = groupController.removeModerator(groupName,userName);
    assertEquals("OK",response.getStatusInfo().getReasonPhrase());
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
