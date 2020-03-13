package com.neu.prattle;

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
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GroupModelTest {

  private GroupService groupService;
  private UserService userService;

  @Before
  public void setUp() {
    groupService = GroupServiceImpl.getInstance();
    userService = UserServiceImpl.getInstance();
  }

  @Test
  public void testGroupModel(){
    Group g = new Group ("GROUP1");
    Moderator m = new Moderator("Moderator800");
    userService.addUser(m);
    List<Moderator> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User("testuser800");
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setUsers(users);
    userService.addUser(u);
    g.setDescription("test");
    g.setPrivate(false);
    Long currentTimeStamp = new Date().getTime();
    g.setCreatedOn(currentTimeStamp.toString());
    groupService.createGroup(g);
    assertEquals("test",g.getDescription());
    assertFalse(g.isPrivate());
    assertFalse(g.equals(new Group("GROUP2")));
  }
}
