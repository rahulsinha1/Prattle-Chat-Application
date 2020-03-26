package com.neu.prattle.serviceTest;

import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.GroupDoesNotExistException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Moderator;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GroupServiceImplTest {

  private GroupService groupService;
  private User user;
  private Group group2;


  @Before
  public void setUp() {
        groupService = GroupServiceImpl.getInstance();
        user = new User("TestTest","TestTest","TestTest","TestTest","GMT");
        group2 = new Group("Test","This is a Test.","TestTest","Test", false);
  }

  @Test
    public void testCreateGroup() {
      Group group3 = new Group("Hello","This is a Test.","TestTest","Test", false);

      groupService.createGroup(group3);
        Group group = (Group) groupService.getAllGroups().get(0);
        assertEquals(group2,group);
    }

    @Test
    public void testGetAllGroup() {
        GroupService groupService2 = GroupServiceImpl.getInstance();
        assertEquals(5,groupService2.getAllGroups().size());
    }

    @Test
    public void testDeleteGroup() {
        Group group3 = new Group("Tesst","This is a Test.","TestTest1","Test", false);
        groupService.createGroup(group2);
        groupService.createGroup(group3);
        groupService.deleteGroup(group3);
        assertEquals(2,groupService.getAllGroups().size());
    }

    @Test
    public void testAddUserToGroup() {
        Group group3 = new Group("Tessst","This is a Test.","TestTest1","Test", false);
        groupService.createGroup(group3);
        groupService.addUser(group3,user);
        assertEquals(user.getUsername(),groupService.getGroupByName("Tessst").getMembers().get(0).getUsername());
    }

    @Test
    public void testRemoveUser(){
        Group group3 = new Group("Tesssst","This is a Test.","TestTest1","Test", false);
        groupService.createGroup(group3);
        groupService.removeUser(group3,user);
        assertEquals(0,groupService.getGroupByName("Tesssst").getMembers().size());
    }

    @Test
    public void testAddModerator(){
        Group group3 = new Group("Tessts","This is a Test.","TestTest1","Test", false);

        Moderator moderator = new Moderator("TestTest");

        groupService.createGroup(group3);
        groupService.addModerator(group3,moderator);

        assertEquals(moderator.getUsername(),groupService.getGroupByName("Tessts").getModerators().get(1).getUsername());
    }

    @Test
    public void testRemoveModerator(){
        Group group3 = new Group("group1","This is a Test.","TestTest1","Test", false);

        Moderator moderator = new Moderator("TestTestTest");

        groupService.createGroup(group3);
        groupService.addModerator(group3,moderator);
        groupService.removeModerator(group3,moderator);

        assertEquals(1,groupService.getGroupByName("group1").getModerators().size());
    }

    @Test
    public void testUpdateGroup(){
        Group group3 = new Group("Tesst","This is a Test.","TestTest1","Test", false);
        groupService.createGroup(group3);
        group3.setDescription("New description");
        groupService.updateGroup(group3);

        assertEquals(group3.getDescription(),groupService.getGroupByName("Tesst").getDescription());
    }

    @Test
    public void testGetAllGroupsByUsername(){
      Group newGroup = new Group("ThisIsIt","This is a Test.","Yes","Test", false);
      groupService.createGroup(newGroup);
       assertEquals(1, groupService.getAllGroupsByUsername("Yes").size());
    }


  @Test(expected = GroupAlreadyPresentException.class)
  public void testGroupAlreadyPresentException(){
      groupService.createGroup(group2);
      groupService.createGroup(group2);
      assertFalse(true);
  }

    @Test(expected = GroupDoesNotExistException.class)
    public void testGroupDoesNotExistException(){
        groupService.getGroupByName("falseGroup");
        assertFalse(true);
    }

    @Test(expected = GroupDoesNotExistException.class)
    public void testGroupDoesNotExistExceptionTwo(){
        groupService.getAllGroupsByUsername("hello");
        assertFalse(true);
    }
}
