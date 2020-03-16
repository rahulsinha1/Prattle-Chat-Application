package com.neu.prattle.modelTest;

import com.neu.prattle.model.User;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserModelTest {

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("User");
        user.setLastName("One");
        user.setPassword("TestOne");
        user.setUsername("user1");
        user.setTimezone("GMT");
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
}
