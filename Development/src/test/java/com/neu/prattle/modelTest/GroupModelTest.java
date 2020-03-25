package com.neu.prattle.modelTest;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.Moderator;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;



public class GroupModelTest {

  private GroupService groupService;
  private UserService userService;
  private User user;

  private Group group;

  @Before
  public void setUp() {
    groupService = GroupServiceImpl.getInstance();
    userService = UserServiceImpl.getInstance();

    user = new User("test","test","Test","Test","Test");
    group = new Group("Test","This is a test", "Test","password",true);
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
    g.setMembers(users);
    userService.addUser(u);
    g.setDescription("test");
    g.setIsGroupPrivate(false);
    Long currentTimeStamp = new Date().getTime();
    g.setCreatedOn(currentTimeStamp.toString());
    groupService.createGroup(g);
    assertEquals("test",g.getDescription());
    assertFalse(g.getIsGroupPrivate());
    assertFalse(g.equals(new Group("GROUP2")));
  }

  @Test
    public void testGetName(){
    assertEquals("Test",group.getName());
  }

    @Test
    public void testGetDescription(){
        assertEquals("This is a test",group.getDescription());
    }

    @Test
    public void testIsPrivate(){
        assertTrue(group.getIsGroupPrivate());
    }

    @Test
    public void testGetPassword(){
        assertEquals("password",group.getPassword());
    }

    @Test
    public void testGetCreatedOn(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        assertEquals(strDate,group.getCreatedOn());
    }

    @Test
    public void testGetId(){
        assertEquals(0,group.getId());
    }

    @Test
    public void testModerators(){
        List<Moderator> moderators = new ArrayList<>();
        Moderator m = new Moderator("Moderator800");
        moderators.add(m);
        group.setModerators(moderators);

        assertEquals("Moderator800",group.getModerators().get(0).getUsername());
    }

    @Test
    public void getCreatedBy(){
      assertEquals("Test", group.getCreatedBy());
    }

    @Test
    public void setCreatedBy(){
      group.setCreatedBy("Jon");
        assertEquals("Jon", group.getCreatedBy());
    }

    @Test
    public void setGroupId(){
        group.setId(100);
        assertEquals(100,group.getId());
    }
}
