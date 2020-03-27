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
        group2 = new Group(generateString (),"This is a group","test4","", false);
    User m = new User(generateString());
    userService.addUser(m);
    List<User> moderators = new ArrayList<>();
    moderators.add(m);
    group2.setModerators(moderators);
    User u = new User(generateString());
    List<User> users = new ArrayList<>();
    users.add(u);
    group2.setMembers(users);
    userService.addUser(u);
        Response response = groupController.createGroup(group2);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testCreateGroupMessage(){
        group2 = new Group(generateString (),"This is a group","test4","", false);
      User m = new User(generateString());
      userService.addUser(m);
      List<User> moderators = new ArrayList<>();
      moderators.add(m);
      group2.setModerators(moderators);
      User u = new User(generateString());
      List<User> users = new ArrayList<>();
      users.add(u);
      group2.setMembers(users);
      userService.addUser(u);
        Response response = groupController.createGroup(group2);
        assertEquals("OK", response.getStatusInfo().getReasonPhrase());
    }

    @Test
    public void testCreateGroupAlreadyExist(){
        group2 = new Group(generateString (),"This is a group","test4","", false);
      User m = new User(generateString());
      userService.addUser(m);
      List<User> moderators = new ArrayList<>();
      moderators.add(m);
      group2.setModerators(moderators);
      User u = new User(generateString());
      List<User> users = new ArrayList<>();
      users.add(u);
      group2.setMembers(users);
      userService.addUser(u);
        groupController.createGroup(group2);
        Response response = groupController.createGroup(group2);
        assertEquals(409, response.getStatus());
    }

    @Test
    public void testCreateGroupAlreadyExistMessage(){
        group2 = new Group(generateString (),"This is a group","test4","", false);
      User m = new User(generateString());
      userService.addUser(m);
      List<User> moderators = new ArrayList<>();
      moderators.add(m);
      group2.setModerators(moderators);
      User u = new User(generateString());
      List<User> users = new ArrayList<>();
      users.add(u);
      group2.setMembers(users);
      userService.addUser(u);
        groupController.createGroup(group2);
        Response response = groupController.createGroup(group2);
        assertEquals("Conflict", response.getStatusInfo().getReasonPhrase());
    }

    @Test
    public void testGetGroup(){
    String groupname = generateString();
        Group group3 = new Group(groupname,"This is a group","test4","", false);
      User m = new User(generateString());
      userService.addUser(m);
      List<User> moderators = new ArrayList<>();
      moderators.add(m);
      group3.setModerators(moderators);
      String username = generateString();
      User u = new User(username);
      List<User> users = new ArrayList<>();
      users.add(u);
      group3.setMembers(users);
      userService.addUser(u);
        groupController.createGroup(group3);
        Response response = groupController.getGroup(username);
        ArrayList<Group> responseGroup = (ArrayList<Group>) response.getEntity();

        assertEquals(group3,responseGroup.get(0));
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
    String groupname = generateString();
        Group group3 = new Group(groupname,"This is a group","test4","", false);
      User m = new User(generateString());
      userService.addUser(m);
      List<User> moderators = new ArrayList<>();
      moderators.add(m);
      group3.setModerators(moderators);
      String username = generateString();
      User u = new User(username);
      List<User> users = new ArrayList<>();
      users.add(u);
      group3.setMembers(users);
      userService.addUser(u);
        groupController.createGroup(group3);

        groupController.addUser(groupname,username);
        Response response = groupController.addUser(groupname,username);
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
