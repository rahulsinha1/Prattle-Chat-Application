package com.neu.prattle;

import com.neu.prattle.controller.GroupController;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Moderator;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.ArrayList;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GroupControllerTest {

  GroupController groupController;
  private UserService userService;
  private GroupService groupService;

  @Before
  public void setUp() {
    groupService = GroupServiceImpl.getInstance();
    userService = UserServiceImpl.getInstance();
    groupController = new GroupController();
  }

  @Test
  public void testCreateGroup(){
    Group g = new Group();
    g.setName("TESTGROUP10");
    Moderator m = new Moderator("Moderator10");
    userService.addUser(m);
    List<Moderator> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User("testuser10");
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setUsers(users);
    userService.addUser(u);
    groupController.createGroup(g);
    groupController.createGroup(g);
    groupController.addModerator("TESTGROUP10",m);
  }

  @Test
  public void testAddUser(){
    Group g = new Group();
    g.setName("TESTGROUP1000");
    Moderator m = new Moderator("Moderator1000");
    userService.addUser(m);
    List<Moderator> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User("testuser1000");
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setUsers(users);
    userService.addUser(u);
    groupController.createGroup(g);
    groupController.addUser("TESTGROUP1000",m);
  }

  @Test
  public void testRemoveUser(){
    Group g = new Group();
    g.setName("TESTGROUP10001");
    Moderator m = new Moderator("Moderator10001");
    userService.addUser(m);
    List<Moderator> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User("testuser10001");
    userService.addUser(u);
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setUsers(users);
    groupService.createGroup(g);
    groupController.createGroup(g);
    groupController.removeUser(g,u);
  }

  @Test
  public void testAddModerator() {
    Group g = new Group();
    g.setName("TESTGROUP10002");
    Moderator m = new Moderator("Moderator10002");
    userService.addUser(m);
    List<Moderator> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User("testuser10002");
    userService.addUser(u);
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setUsers(users);
    groupService.createGroup(g);
    groupController.createGroup(g);
    groupController.addModerator("TESTGROUP10002",m);
  }

  @Test
  public void testRemoveModerator() {
    Group g = new Group();
    g.setName("TESTGROUP10003");
    Moderator m = new Moderator("Moderator10003");
    userService.addUser(m);
    List<Moderator> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User("testuser10003");
    userService.addUser(u);
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setUsers(users);
    groupService.createGroup(g);
    groupController.createGroup(g);
    groupController.removeModerator(g,m);
  }

  @Test
  public void testUpdateGroup() {
    Group g = new Group();
    g.setName("TESTGROUP10004");
    Moderator m = new Moderator("Moderator10004");
    userService.addUser(m);
    List<Moderator> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User("testuser10004");
    userService.addUser(u);
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setUsers(users);
    groupService.createGroup(g);
    groupController.createGroup(g);
    groupController.updateGroup(g);
  }

  @Test
  public void testDeleteGroup() {
    Group g = new Group();
    g.setName("TESTGROUP10005");
    Moderator m = new Moderator("Moderator10005");
    userService.addUser(m);
    List<Moderator> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User("testuser10005");
    userService.addUser(u);
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setUsers(users);
    groupService.createGroup(g);
    groupController.createGroup(g);
    groupController.deleteGroup(g);
  }

  @Test
  public void testGetAllGroups() {
    Group g = new Group();
    g.setName("TESTGROUP10006");
    Moderator m = new Moderator("Moderator10006");
    userService.addUser(m);
    List<Moderator> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User("testuser10006");
    userService.addUser(u);
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setUsers(users);
    groupService.createGroup(g);
    groupController.createGroup(g);
    groupController.getAllGroups();
  }

  @Test
  public void testGetAllUserGroups() {
    Group g = new Group();
    g.setName("TESTGROUP10007");
    Moderator m = new Moderator("Moderator10007");
    userService.addUser(m);
    List<Moderator> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User("testuser10007");
    userService.addUser(u);
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setUsers(users);
    groupService.createGroup(g);
    groupController.createGroup(g);
    groupController.getAllUserGroups("testuser10007");
  }

  @Test
  public void testGetGroupDetails() {
    Group g = new Group();
    g.setName("TESTGROUP10008");
    Moderator m = new Moderator("Moderator10008");
    userService.addUser(m);
    List<Moderator> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User("testuser10008");
    userService.addUser(u);
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setUsers(users);
    groupService.createGroup(g);
    groupController.createGroup(g);
    groupController.getGroupDetails("TESTGROUP10008");
  }
}
