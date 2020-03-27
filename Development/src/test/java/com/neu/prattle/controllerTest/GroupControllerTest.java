package com.neu.prattle.controllerTest;

import com.neu.prattle.controller.GroupController;
import com.neu.prattle.model.Group;

import com.neu.prattle.model.User;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;


import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class GroupControllerTest {
    GroupController groupController;
  //  UserService userService;
    private Group group2;

  @Before
  public void setUp() {
    //  userService = UserServiceImpl.getInstance();
     groupController = new GroupController();
  }

  @Test
    public void testCreateGroup(){
        group2 = new Group("Test1","This is a group","test4","", false);
        Response response = groupController.createGroup(group2);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testCreateGroupMessage(){
        group2 = new Group("Test2","This is a group","test4","", false);
        Response response = groupController.createGroup(group2);
        assertEquals("Group Successfully Created.", response.getStatusInfo().getReasonPhrase());
    }

    @Test
    public void testCreateGroupAlreadyExist(){
        group2 = new Group("Test1","This is a group","test4","", false);
        groupController.createGroup(group2);
        Response response = groupController.createGroup(group2);
        assertEquals(409, response.getStatus());
    }

    @Test
    public void testCreateGroupAlreadyExistMessage(){
        group2 = new Group("Test1","This is a group","test4","", false);
        groupController.createGroup(group2);
        Response response = groupController.createGroup(group2);
        assertEquals("Group already present with name: Test1", response.getStatusInfo().getReasonPhrase());
    }

    @Test
    public void testGetGroup(){
        Group group3 = new Group("group3","This is a group","test4","", false);
        groupController.createGroup(group3);
        Response response = groupController.getGroup("test4");
        ArrayList<Group> responseGroup = (ArrayList<Group>) response.getEntity();

        assertEquals(group3,responseGroup.get(0));
    }

    @Test
    public void testGroupDoesNotExist(){
        Response response = groupController.getGroup("test1");
        assertEquals(409, response.getStatus());
    }

    @Test
    public void testGroupDoesNotExistMessage(){
        Response response = groupController.getGroup("test1");
        assertEquals("User is not apart of any group.", response.getStatusInfo().getReasonPhrase());
    }

    @Test
    public void testAddUser(){
//        User user = new User("First", "Last", "firstlast",
//            "pass1234","GMT");
//        userService.addUser(user);
//        userService.findUserByUsername(user.getUsername());
//
//        Group group3 = new Group("group3","This is a group","test4","", false);
//        groupController.createGroup(group3);
//
//      Response response = groupController.addUser("group3","firstlast");
//      assertEquals("User Successfully Added In Group.",response.getStatusInfo().getReasonPhrase());
    }

    @Test
    public void testUserAlreadyPresentInGroupException(){
        Group group3 = new Group("group3","This is a group","test4","", false);
        groupController.createGroup(group3);

        groupController.addUser("group3","test1");
        Response response = groupController.addUser("group3","test1");
        assertEquals("User is already present in the group.",response.getStatusInfo().getReasonPhrase());
    }
//
//    @Test
//    public void testCreateGroupWithSameName(){
//        group1 = new Group("Test","This is a group","test1","", false);
//        groupController.createGroup(group1);
//        Response response = groupController.createGroup(group1);
//        assertEquals(409, response.getStatus());
//    }
//
//    @Test
//    public void testGetGroup(){
//      User user = new User("test2","test2","test2","test2","GMT");
//      userService.addUser(user);
//
//      Group group3 = new Group("Test3","This is a group","test2","", false);
//      groupController.createGroup(group3);
//       Response response = groupController.getGroup(user.getUsername());
//        assertEquals(200, response.getStatus());
//    }
//
//    @Test(expected = UserDoesNotExistException.class)
//    public void testGetGroup2(){
//        User user = new User("test2","test2","test2","test2","GMT");
//        userService.addUser(user);
//
//        Group group3 = new Group("Test3","This is a group","test","", false);
//        groupController.createGroup(group3);
//        Response response = groupController.getGroup(user.getUsername());
//        assertFalse(true);
//  }

}
