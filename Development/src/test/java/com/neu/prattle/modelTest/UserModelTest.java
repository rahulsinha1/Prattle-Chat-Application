package com.neu.prattle.modelTest;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class UserModelTest {

    private User user;
    private ArrayList<Group> group;
    private ArrayList<User> follower;

    @Before
    public void setUp() {
        user = new User();
        //group = new ArrayList<>();
        Group group = new Group();
        follower = new ArrayList<>();

        //group.add(group1);
        //follower.add(user);

        user.setFirstName("User");
        user.setLastName("One");
        user.setPassword("TestOne");
        user.setUsername("user1");
        user.setTimezone("GMT");

        user.setGroupParticipant(group);
    }

  @Test
  public void testUserModel() {
    new User();
  }

    @Test
    public void testUserFirstName(){
        assertEquals("User",user.getFirstName());
    }

    @Test
    public void testUserLastName(){
        assertEquals("One",user.getLastName());
    }

    @Test
    public void testUserUsername(){
        assertEquals("user1",user.getUsername());
    }

    @Test
    public void testUserTimezone(){
        assertEquals("GMT",user.getTimezone());
    }

    @Test
    public void testUserPassword(){
        assertEquals("TestOne",user.getPassword());
    }

    @Test
    public void testUserGroup(){
        assertEquals(1,user.getGroupParticipant().size());
    }

    @Test
    public void testCreateUserAccount(){
        User user = new User("First", "Last", "firstlast",
            "pass1234","GMT");

        ArrayList<String> emptyGroup = new ArrayList<>();
        ArrayList<String> emptyFollow = new ArrayList<>();

        assertEquals("First", user.getFirstName());
        assertEquals("Last", user.getLastName());
        assertEquals("firstlast", user.getUsername());
        assertEquals("ssap", user.getPassword());
        assertEquals("GMT", user.getTimezone());
        assertEquals(emptyGroup, user.getGroupParticipant());
    }

    @Test
    public void testEqual(){
        User user = new User("First", "Last", "firstlast",
            "pass1234","GMT");

        User user1 = new User("First", "Last", "firstlast",
            "pass1234","GMT");

        assertTrue(user.equals(user1));
    }

    @Test
    public void testNotEqual(){
        User user = new User("First", "Last", "firstlast",
            "pass1234","GMT");

        User user1 = new User("hello");

        assertFalse(user.equals(user1));
    }
}
