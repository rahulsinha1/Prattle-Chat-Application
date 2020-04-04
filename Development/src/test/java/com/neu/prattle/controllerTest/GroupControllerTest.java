package com.neu.prattle.controllerTest;

import com.neu.prattle.controller.GroupController;
import com.neu.prattle.exceptions.*;
import com.neu.prattle.model.Group;

import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
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
public class GroupControllerTest {
    GroupController groupController = new GroupController();

    @Mock
    private GroupService groupService;

    @Mock
    private UserService userService;

  @Before
  public void setUp()throws IllegalAccessException, NoSuchFieldException  {
    Field f1 = groupController.getClass().getDeclaredField("userService");
    f1.setAccessible(true);
    f1.set(groupController, userService);


    Field f2 = groupController.getClass().getDeclaredField("groupService");
    f2.setAccessible(true);
    f2.set(groupController, groupService);
  }

  @Test
    public void testCreateGroup(){
      Group group = new Group("group1","This is group","user1","pass1234",false);

      doNothing().when(groupService).createGroup(group);
      Response response = groupController.createGroup(group);
      assertEquals(200, response.getStatus());
    }

    @Test
    public void testGroupAlreadyExist(){
      Group group = new Group("group1","This is group","user1","pass1234",false);

      doThrow(new GroupAlreadyPresentException("Group Already exist")).when(groupService).createGroup(group);
      Response response = groupController.createGroup(group);
      assertEquals(409, response.getStatus());
    }

    @Test
    public void testGetGroup(){
      Group group = new Group("group1","This is group","user1","pass1234",false);

      List<Group> groupList = new ArrayList<>();
      groupList.add(group);

      doReturn(groupList).when(groupService).getAllGroupsByUsername("user1");
      Response response = groupController.getGroup("user1");
      assertEquals(200, response.getStatus());
    }

    @Test
    public void testGroupDoesNotExistException(){
      doThrow(new UserDoesNotHaveAnyGroup("User Is Not Apart Of Any Groups")).when(groupService).getAllGroupsByUsername("user1");
      Response response = groupController.getGroup("user1");
      assertEquals(409, response.getStatus());
    }

    @Test
    public void testAddUser(){
      User user = new User("test","test","Test","Test","GMT");
      Group group = new Group("group1","This is group","user1","pass1234",false);

      doReturn(group).when(groupService).getGroupByName("group1");
      doReturn(user).when(userService).findUserByUsername("Test");

      Response response = groupController.addUser("group1","Test");
      assertEquals(200, response.getStatus());
    }

    @Test
    public void testUserAlreadyPresentInGroupException(){
      User user = new User("test","test","Test","Test","GMT");
      Group group = new Group("group1","This is group","user1","pass1234",false);

      doReturn(group).when(groupService).getGroupByName("group1");
      doThrow(new UserAlreadyPresentInGroupException("User Is Already PResent In The Group.")).when(userService).findUserByUsername("Test");

      Response response = groupController.addUser("group1","Test");
      assertEquals(409, response.getStatus());
    }

  @Test
  public void testRemoveUser(){
    User user = new User("test","test","Test","Test","GMT");
    Group group = new Group("group1","This is group","user1","pass1234",false);

    doReturn(group).when(groupService).getGroupByName("group1");
    doReturn(user).when(userService).findUserByUsername("Test");

    Response response = groupController.removeUser("group1","Test");
    assertEquals(200, response.getStatus());
  }

  @Test
  public void testUserDoesNotExistException(){
    User user = new User("test","test","Test","Test","GMT");
    Group group = new Group("group1","This is group","user1","pass1234",false);

    doReturn(group).when(groupService).getGroupByName("group1");
    doThrow(new UserDoesNotExistException("User Does Not Exist In Group")).when(userService).findUserByUsername("Test");

    Response response = groupController.removeUser("group1","Test");
    assertEquals(409, response.getStatus());
  }

  @Test
  public void testAddModerator(){
    User user = new User("test","test","Test","Test","GMT");
    Group group = new Group("group1","This is group","user1","pass1234",false);

    doReturn(group).when(groupService).getGroupByName("group1");
    doReturn(user).when(userService).findUserByUsername("Test");

    Response response = groupController.addModerator("group1","Test");
    assertEquals(200, response.getStatus());
  }

  @Test
  public void testModeratorAlreadyPresentInGroupException(){
    User user = new User("test","test","Test","Test","GMT");
    Group group = new Group("group1","This is group","user1","pass1234",false);

    doReturn(group).when(groupService).getGroupByName("group1");
    doThrow(new UserAlreadyPresentInGroupException("Moderator Already Present In Group")).when(userService).findUserByUsername("Test");

    Response response = groupController.addModerator("group1","Test");
    assertEquals(409, response.getStatus());
  }

  @Test
  public void testRemoveModerator(){
    User user = new User("test","test","Test","Test","GMT");
    Group group = new Group("group1","This is group","user1","pass1234",false);

    doReturn(group).when(groupService).getGroupByName("group1");
    doReturn(user).when(userService).findUserByUsername("Test");

    Response response = groupController.removeModerator("group1","Test");
    assertEquals(200, response.getStatus());
  }

  @Test
  public void testModeratorDoesNotExistException(){
    User user = new User("test","test","Test","Test","GMT");
    Group group = new Group("group1","This is group","user1","pass1234",false);

    doReturn(group).when(groupService).getGroupByName("group1");
    doThrow(new UserDoesNotExistException("Moderator Does Not Exist In Group")).when(userService).findUserByUsername("Test");

    Response response = groupController.removeModerator("group1","Test");
    assertEquals(409, response.getStatus());
  }

  @Test
  public void testUpdateGroup(){
    Group group = new Group("group1","This is group","user1","pass1234",false);

    doReturn(group).when(groupService).getGroupByName("group1");
    doNothing().when(groupService).updateGroup(group);

    Response response = groupController.updateGroup("group1");
    assertEquals(200, response.getStatus());
  }

  @Test
  public void testUpdateGroupDoesNotExistException(){
    Group group = new Group("group1","This is group","user1","pass1234",false);

    doReturn(group).when(groupService).getGroupByName("group1");
    doThrow(new GroupDoesNotExistException("Group does not exist.")).when(groupService).updateGroup(group);

    Response response = groupController.updateGroup("group1");
    assertEquals(409, response.getStatus());
  }

  @Test
  public void testDeleteGroup(){
    Group group = new Group("group1","This is group","user1","pass1234",false);

    doReturn(group).when(groupService).getGroupByName("group1");
    doNothing().when(groupService).deleteGroup("group1");

    Response response = groupController.deleteGroup("group1");
    assertEquals(200, response.getStatus());
  }

  @Test
  public void testDeleteGroupDoesNotExistException(){
    Group group = new Group("group1","This is group","user1","pass1234",false);

    doReturn(group).when(groupService).getGroupByName("group1");
    doThrow(new GroupDoesNotExistException("Group does not exist.")).when(groupService).deleteGroup("group1");

    Response response = groupController.deleteGroup("group1");
    assertEquals(409, response.getStatus());
  }

  @Test
  public void testGetAll(){
    Group group = new Group("group1","This is group","user1","pass1234",false);

    List<Group> groupList = new ArrayList<>();
    groupList.add(group);

    doReturn(groupList).when(groupService).getAllGroups();
    Response response = groupController.getAllGroups();
    assertEquals(200, response.getStatus());
  }

  @Test
  public void testGetAllUserGroups(){
    Group group = new Group("group1","This is group","user1","pass1234",false);

    List<Group> groupList = new ArrayList<>();
    groupList.add(group);

    doReturn(groupList).when(groupService).getAllGroupsByUsername("user1");
    Response response = groupController.getAllUserGroups("user1");
    assertEquals(200, response.getStatus());
  }

  @Test
  public void testUserDoesNotExistExceptionForGetAllUserGroups(){
    doThrow(new UserDoesNotExistException("User Does Not Exist.")).when(groupService).getAllGroupsByUsername("user1");
    Response response = groupController.getAllUserGroups("user1");
    assertEquals(409, response.getStatus());
  }

  @Test
  public void testGetGroupDetails(){
    Group group = new Group("group1","This is group","user1","pass1234",false);

    doReturn(group).when(groupService).getGroupByName("group1");

    Response response = groupController.getGroupDetails("group1");
    assertEquals(200, response.getStatus());
  }

  @Test
  public void testGroupDoesNotExistExceptionGetGroupDetails(){
    doThrow(new GroupDoesNotExistException("Group Does Not Exist.")).when(groupService).getGroupByName("group1");

    Response response = groupController.getGroupDetails("group1");
    assertEquals(409, response.getStatus());
  }
}
