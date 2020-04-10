package com.neu.prattle.modelTest;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.Moderator;
import com.neu.prattle.model.User;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GroupModelTest {

  private Group group;

  @Before
  public void setUp() {
    group = new Group("Test","This is a test", "Test","password",true);
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
        //List<User> moderators = new ArrayList<>();
        Moderator m = new Moderator("Moderator800");
        //moderators.add(m);
        group.setModerators(m);

        assertEquals("Moderator800",group.getModerators().get(0).getUsername());
    }

    @Test
    public void testGetCreatedBy(){
      assertEquals("Test", group.getCreatedBy());
    }

    @Test
    public void testSetCreatedBy(){
      group.setCreatedBy("Jon");
        assertEquals("Jon", group.getCreatedBy());
    }

    @Test
    public void testSetGroupId(){
        group.setId(100);
        assertEquals(100,group.getId());
    }

    @Test
    public void testSetCreatedOn(){
      group.setCreatedOn("Monday");
        assertEquals("Monday",group.getCreatedOn());
    }

    @Test
    public void testSetDescription(){
        group.setDescription("This is Description");
        assertEquals("This is Description",group.getDescription());
    }

    @Test
    public void testSetIsGroupPrivate(){
        group.setIsGroupPrivate(true);
        assertTrue(group.getIsGroupPrivate());
    }

    @Test
    public void testSetMembers(){
        User user = new User("First", "Last", "firstlast",
            "pass1234","GMT");
      //List<User> members = new ArrayList<>();
      //members.add(user);
      group.setMembers(user);
      assertTrue(group.getMembers().size()!=0);
    }
}
