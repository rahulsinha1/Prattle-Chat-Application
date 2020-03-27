
package com.neu.prattle.serviceTest;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.Group;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.Test;

import com.neu.prattle.model.User;

public class UserServiceImplTest {

    private UserService as;
    private static final String MIKE4 = "MIKE4";
    private static final String MIKE1 = "Mike1";

    @Before
    public void setUp() {
        as = UserServiceImpl.getInstance();
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
    public void addUserTest() {
        User user = new User(generateString());
        as.addUser(user);
        Optional<User> user1 = as.findUserByName(user.getUsername());
        assertTrue(user1.isPresent());
    }

    /*
Adding multiple users to the databse
 */
    @Test
    public void addMultipleUsersTest() {
        for (int i = 0; i < 4; i++) {
            User user = new User(generateString());
            as.addUser(user);
            Optional<User> user1 = as.findUserByName(user.getUsername());
            assertTrue(user1.isPresent());
        }
    }

    // Performance testing to benchmark our number of users that can be added
    // in 1 sec
    @Test(timeout = 1000)
    public void checkPrefTest(){
        for(int i=1000; i < 2000; i++) {
            as.addUser(new User("Mike"+i));
        }
    }

    @Test
    public void testFindUserByUsername() {
        setMocksForUserService(MIKE4);
        User user = as.findUserByUsername(MIKE4);
        assertEquals(MIKE4,user.getUsername());
    }

    /*
    Find a user which does not exist
     */
    @Test(expected = UserDoesNotExistException.class)
    public void testFindUserByUsernameNotExisting() {
        User user = as.findUserByUsername("nouser");
        assertNull(user);
    }

    @Test
    public void updateUser() {
        setMocksForUserService(MIKE1);
        Optional<User> user = as.findUserByName(MIKE1);
        if(user.isPresent()){
            user.get().setUsername("Mikeupdate");
            as.updateUser(user.get());
        }
        user.ifPresent(user1 -> assertEquals("Mikeupdate", user1.getUsername()));
    }

    @Test
    public void testEmptyFindUser() {
        Optional<User> user = as.findUserByName("Test94");
        assertFalse(user.isPresent());
    }

    @Test(expected = UserAlreadyPresentException.class)
    public void testUserAlreadyExist() {
        as.addUser(new User("Test", "Test","Test", "Test","Test"));
        as.addUser(new User("Test", "Test","Test", "Test","Test"));
        assertFalse(false);
    }


    // This method tests finding and getting user information from the database
    @Test
    public void getUserTest() {
        String username = generateString();
        String first_name = generateString();
        User user = new User(username);
        user.setFirstName(first_name);
        as.addUser(user);
        Optional<User> userObj = as.findUserByName(username);
        assertTrue(userObj.isPresent());
        assertEquals(first_name, userObj.get().getFirstName());

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
        as.addUser(groupUser);
        List<Group> g = as.findGroupsByName(groupUser.getUsername());
        assertEquals(groupName, g.get(0).getName());
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






















