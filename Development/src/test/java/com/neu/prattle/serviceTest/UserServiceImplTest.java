
package com.neu.prattle.serviceTest;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.Group;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.neu.prattle.model.User;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

  @Mock
  private UserService mockUserService;
  private UserService as;
  private static final String MIKE4 = "MIKE4";
  private static final String MIKE1 = "Mike1";

  @Before
  public void setUp() {
    as = UserServiceImpl.getInstance();
    mockUserService = UserServiceImpl.getInstance();
    MockitoAnnotations.initMocks(this);
  }


  // This method just tries to add
  @Test
  public void setUserTest() {
    setMocksForUserService(generateString());
  }


  /*
  Adding a single user to the databse
   */
  @Test
  public void createUserTest() {

    String userName = generateString();
    User mockUser = new User(userName);
    mockUser.setFirstName("john");
    mockUserService.addUser(mockUser);

    when(mockUserService.findUserByName(userName)).thenReturn(Optional.of(mockUser));

    User result = mockUserService.findUserByName(userName).get();
    assertEquals(mockUser.getFirstName(), result.getFirstName());
  }

  /* *//*
  Adding multiple users to the databse
   *//*
  @Test
  public void addMultipleUsersTest() {
    for (int i = 0; i < 4; i++) {
      User user = new User(generateString());
      as.addUser(user);
      Optional<User> user1 = as.findUserByName(user.getUsername());
      assertTrue(user1.isPresent());
    }


  }
*/

  // This method tests finding and getting user information from the database
  @Test
  public void getUserTest() {
    String userName = generateString();
    User mockUser = new User(userName);
    mockUser.setFirstName("john");
    mockUser.setLastName("doe");
    mockUserService.addUser(mockUser);

    when(mockUserService.findUserByName(userName)).thenReturn(Optional.of(mockUser));

    User result = mockUserService.findUserByName(userName).get();
    assertEquals(mockUser.getFirstName() + mockUser.getLastName(), result.getFirstName() + result.getLastName());

  }


  /*
  Finding user in the databse according to the username
   */
  @Test
  public void testFindUserByUsername() {
    String userName = generateString();
    User mockUser = new User(userName);
    mockUser.setFirstName("john");
    mockUserService.addUser(mockUser);

    when(mockUserService.findUserByName(userName)).thenReturn(Optional.of(mockUser));

    Optional<User> userObj = mockUserService.findUserByName(userName);
    assertTrue(userObj.isPresent());
  }

  /*
  Find a user which does not exist
   */
  @Test(expected = UserDoesNotExistException.class)
  public void testFindUserByUsernameNotExisting() {
    doThrow(new UserDoesNotExistException("User does not exists")).when(mockUserService).findUserByName("nouser");
    Optional<User> userObj = mockUserService.findUserByName("nouser");
    assertNull(userObj);
  }

  /*
  Test if the user is part of one group
   */
  @Test
  public void findGroupsByName() {
    String userName = generateString();
    String groupName = generateString();
    User groupUser = new User(userName);
    Group group = new Group(groupName);
    List<User> listUsers = new ArrayList<>();
    List<Group> listGroups = new ArrayList<>();
    listUsers.add(groupUser);
    listGroups.add(group);
    groupUser.setGroupParticipant(listGroups);
    group.setMembers(listUsers);
    mockUserService.addUser(groupUser);

    List<Group> mockGroup = new ArrayList<>();
    mockGroup.add(group);

    when(mockUserService.findGroupsByName(groupUser.getUsername())).thenReturn(mockGroup);
    List<Group> g = mockUserService.findGroupsByName(groupUser.getUsername());


    assertEquals(groupName, g.get(0).getName());
  }


  /*
  Test for updating the user entity which exists in the system
   */
  @Test
  public void updateUser() {

    String userName = generateString();
    User mockUser = new User(userName);
    mockUser.setFirstName("john");
    mockUserService.addUser(mockUser);

    when(mockUserService.findUserByName(userName)).thenReturn(Optional.of(mockUser));
    Optional<User> user = mockUserService.findUserByName(userName);
    if (user.isPresent()) {
      user.get().setFirstName("Mikeupdate");
      mockUserService.updateUser(user.get());
    }

    when(mockUserService.findUserByUsername(userName)).thenReturn(mockUser);
    User user2 = mockUserService.findUserByUsername(userName);

    user.ifPresent(user1 -> assertEquals("Mikeupdate", user2.getFirstName()));
  }


  /*
  Update a non existing user Entity
   */
  @Test(expected = UserDoesNotExistException.class)
  public void updateUserNotExists() {
    User user = new User(generateString());
    user.setFirstName("update");
    as.updateUser(user);
  }


  /*
  Find user in the databse which does not exist
   */
  @Test
  public void testEmptyFindUser() {
    Optional<User> user = as.findUserByName("Test94");
    assertFalse(user.isPresent());
  }

  @Test(expected = UserDoesNotExistException.class)
  public void testEmptyFindUserByName() {
    User user = as.findUserByUsername("Test94");
    assertNull(user);
  }


  /*
    Adding user which already exists in the databse
   */
  @Test(expected = UserAlreadyPresentException.class)
  public void testUserAlreadyExistTwo() {
    as.addUser(new User("Test", "Test", "Test", "Test", "Test"));
    as.addUser(new User("Test", "Test", "Test", "Test", "Test"));
    assertFalse(false);
  }

  /*
    Check the groups for user who is not a part of any group
   */
  @Test
  public void testEmptyGroup() {
    List g = as.findGroupsByName("Test93");
    assertEquals(0, g.size());
  }

  private void setMocksForUserService(String name) {
    List<Group> groups = new ArrayList<>();
    Group g = new Group();
    g.setName(generateString());
    groups.add(g);
    User u = new User(name);
    u.setFirstName(generateString());
    u.setLastName(generateString());
    u.setGroupParticipant(groups);
    as.addUser(u);
  }

  /*
  This is a helper method using to generate random strings rto populate group and user names.
   */
  private String generateString() {
    int n = 8;
    {
      // chose a Character random from this String
      String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
              + "0123456789"
              + "abcdefghijklmnopqrstuvxyz";


      StringBuilder sb = new StringBuilder(n);

      for (int i = 0; i < n; i++) {

        int index = (int) (AlphaNumericString.length()
                * Math.random());

        // add Character one by one in end of sb
        sb.append(AlphaNumericString
                .charAt(index));
      }

      return sb.toString();
    }

  }

}


