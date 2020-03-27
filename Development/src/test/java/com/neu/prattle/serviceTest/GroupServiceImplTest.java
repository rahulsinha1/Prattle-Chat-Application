package com.neu.prattle.serviceTest;

import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.GroupDoesNotExistException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Moderator;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;

import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertFalse;

public class GroupServiceImplTest {

    private GroupService groupService;
    private UserService userService;
    private static final String TESTUSER = "testuser";
    private static final String TESTMODERATOR = "testmoderator";
    private static final String TESTGROUPNAME = "testgroupname";
    private Group group = new Group();
    private User user;
    private Group group2;


  @Before
  public void setUp() {
        groupService = GroupServiceImpl.getInstance();
        userService = UserServiceImpl.getInstance();
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
    public void testDeleteGroup2() {
        Group group3 = new Group("Tesst","This is a Test.","TestTest1","Test", false);
        groupService.createGroup(group2);
        groupService.createGroup(group3);
        groupService.deleteGroup(group3);
        assertEquals(2,groupService.getAllGroups().size());
    }

    @Test
    public void testAddUserToGroup2() {
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
    public void testUpdateGroup2(){
        Group group3 = new Group("Tesst","This is a Test.","TestTest1","Test", false);
        groupService.createGroup(group3);
        group3.setDescription("New description");
        groupService.updateGroup(group3);

        assertEquals(group3.getDescription(),groupService.getGroupByName("Tesst").getDescription());
    }

    @Test
    public void testGetAllGroupsByUsername2(){
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

  @Test
  public void testAddUserToGroup () {
    Group g = new Group();
    g.setName(generateString());
    User u = new User(generateString());
    u.setFirstName(generateString());
    userService.addUser(u);
    groupService.createGroup(g);
    groupService.addUser(g,u);
  }

  @Test
  public void testAddMultipleUserToGroup () {
    Group g = new Group();
    g.setName("TESTGROUP2");
    User u = new User(generateString());
    u.setFirstName("groupuser3");
    User u1 = new User(generateString());
    u1.setFirstName("groupuser4");

    userService.addUser(u);
    userService.addUser(u1);
    List<User> moderators = new ArrayList<>();
    User m = new User("Moderator149");
    m.setFirstName("ModeratorGroup");
    moderators.add(m);
    userService.addUser(m);
    g.setModerators(moderators);
    groupService.createGroup(g);
    groupService.addUser(g,u);
    groupService.addUser(g,u1);
    groupService.addUser(g,u1);
  }

  @Test
  public void testUpdateGroup () {
    Group g = new Group();
    g.setName("TESTGROUP4");
    User m = new User("Moderator4");
    userService.addUser(m);
    List<User> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User("testuser4");
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setMembers(users);
    userService.addUser(u);
    groupService.createGroup(g);
    groupService.updateGroup(g);
  }

  private void setMocksForGroupCreation() {
    User u = new User(generateString());
    u.setFirstName(generateString());
    userService.addUser(u);
    User m = new User(TESTMODERATOR+generateString());
    m.setFirstName(generateString());
    userService.addUser(m);
    List<User> users = new ArrayList<>();
    users.add(u);
    List<User> moderators = new ArrayList<>();
    moderators.add(m);
    group.setMembers(users);
    group.setName(TESTGROUPNAME);
    group.setModerators(moderators);
  }

  @Test
  public void testDeleteGroup() {
    Group g = new Group();
    g.setName(generateString());
    User m = new User(generateString());
    userService.addUser(m);
    List<User> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User(generateString());
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setMembers(users);
    userService.addUser(u);
    groupService.createGroup(g);
    groupService.deleteGroup(g);
  }

  @Test
  public void testGetAllGroups(){
    Group g = new Group();
    g.setName("TESTGROUP6");
    User m = new User("Moderator6");
    userService.addUser(m);
    List<User> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User("testuser6");
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setMembers(users);
    userService.addUser(u);
    groupService.createGroup(g);
    groupService.getAllGroups();
  }

  @Test
  public void testGetAllGroupsByUsername(){
    Group g = new Group();
    g.setName("TESTGROUP7");
    User m = new User("Moderator7");
    userService.addUser(m);
    List<User> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User("testuser7");
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setMembers(users);
    userService.addUser(u);
    groupService.createGroup(g);
    groupService.getAllGroupsByUsername("testuser7");
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNotifyGroup(){
    groupService.notifyGroup();
  }

  @Test
  public void testGetGroupByName(){
    Group g = new Group();
    g.setName("TESTGROUP10");
    User m = new User("Moderator10000000");
    userService.addUser(m);
    List<User> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User("testuser10000000");
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setMembers(users);
    userService.addUser(u);
    groupService.createGroup(g);
    groupService.getGroupByName("TESTGROUP10");
  }

  @Test
  public void testGetGroupByNameNull() {
    groupService.getGroupByName("TESTGROUP9");
  }



  private String generateString() {
    byte[] array = new byte[7]; // length is bounded by 7
    new Random().nextBytes(array);
    String generatedString = new String(array, Charset.forName("UTF-8"));
    return generatedString;
  }
}
