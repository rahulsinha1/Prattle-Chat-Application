package com.neu.prattle.serviceTest;

import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Moderator;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GroupServiceImplTest {

  private GroupService groupService;
  private UserService userService;
  private static final String TESTUSER = "testuser";
  private static final String TESTMODERATOR = "testmoderator";
  private Group group = new Group();
  private Group group2;
  private static final String TESTGROUPNAME = "testgroupname";


  @Before
  public void setUp() {
      group2 = new Group("Test","This is a Test.","TestTest","Test", false);
    groupService = GroupServiceImpl.getInstance();
    userService = UserServiceImpl.getInstance();


  }

  @Test
  public void testCreateGroup() {
    setMocksForGroupCreation();
    groupService.createGroup(group);
  }

  @Test(expected = GroupAlreadyPresentException.class)
  public void testGroupCreation(){
      groupService.createGroup(group2);
      groupService.createGroup(group2);
      assertFalse(false);
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
    group.setName(generateString());
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
    List<Group> groupList = groupService.getAllGroups();
    for(Group obj : groupList)
    {
      System.out.println(obj.getName());
    }
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
    g.setName(generateString());
    g.setCreatedBy("rahul");
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
    Group group = groupService.getGroupByName(g.getName());
    assertEquals("rahul",group.getCreatedBy());

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
