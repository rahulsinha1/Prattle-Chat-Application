package com.neu.prattle.controllerTest;

import com.neu.prattle.controller.GroupController;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;


import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class GroupControllerTest {
    GroupController groupController;
    private GroupService groupService;
    private UserService userService;
    private Group group1;
    private User user;
    private Group group2;

  @Before
  public void setUp() {
     groupService = GroupServiceImpl.getInstance();
     userService = UserServiceImpl.getInstance();
     groupController = new GroupController();
  }

  @Test
  public void testCreateGroup(){
      group2 = new Group("Test1","This is a group","test4","", false);
      Response response = groupController.createGroup(group2);
      assertEquals(200, response.getStatus());
  }

    @Test
    public void testCreateGroupWithSameName(){
        group1 = new Group("Test","This is a group","test1","", false);
        groupController.createGroup(group1);
        Response response = groupController.createGroup(group1);
        assertEquals(409, response.getStatus());
    }

}
