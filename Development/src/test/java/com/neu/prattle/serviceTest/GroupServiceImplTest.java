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

import java.util.ArrayList;
import java.util.List;

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
    User u = new User("test1");
    userService.addUser(u);
    groupService.addUser(g,u);
  }

  @Test
  public void testAddMultipleUserToGroup () {
    Group g = new Group();
    g.setName("TESTGROUP1");
    User u = new User("test2");
    User u1 = new User("test3");
    userService.addUser(u);
    userService.addUser(u1);
    List<Moderator> moderators = new ArrayList<>();
    Moderator m = new Moderator("Moderator1");
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
    Moderator m = new Moderator("Moderator4");
    userService.addUser(m);
    List<Moderator> moderators = new ArrayList<>();
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
    User u = new User(TESTUSER);
    userService.addUser(u);
    Moderator m = new Moderator(TESTMODERATOR);
    userService.addUser(m);
    List<User> users = new ArrayList<>();
    users.add(u);
    List<Moderator> moderators = new ArrayList<>();
    moderators.add(m);
    group.setMembers(users);
    group.setName(TESTGROUPNAME);
    group.setModerators(moderators);
  }

  @Test
  public void testDeleteGroup() {
    Group g = new Group();
    g.setName("TESTGROUP5");
    Moderator m = new Moderator("Moderator5");
    userService.addUser(m);
    List<Moderator> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User("testuser5");
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
    Moderator m = new Moderator("Moderator6");
    userService.addUser(m);
    List<Moderator> moderators = new ArrayList<>();
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
    Moderator m = new Moderator("Moderator7");
    userService.addUser(m);
    List<Moderator> moderators = new ArrayList<>();
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
    Moderator m = new Moderator("Moderator10000000");
    userService.addUser(m);
    List<Moderator> moderators = new ArrayList<>();
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
}
