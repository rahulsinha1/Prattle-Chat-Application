package com.neu.prattle.serviceTest;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.Group;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;

import com.neu.prattle.model.User;

public class UserServiceImplTest {

	private UserService as;
	private GroupService groupServices;
	private static final String MIKE4 = "MIKE4";
	private static final String MIKE1 = "Mike1";

	@Before
	public void setUp() {
		as = UserServiceImpl.getInstance();
		groupServices = GroupServiceImpl.getInstance();
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
		for(int i= 0; i<4;i++)
		{
			User user = new User(generateString());
			as.addUser(user);
			Optional<User> user1 = as.findUserByName(user.getUsername());
			assertTrue(user1.isPresent());
		}


	}


	// This method just tries to add
	@Test
	public void getUserTest() {
		Optional<User> user = as.findUserByName(MIKE1);
		assertTrue(user.isPresent());
	}


	/*
	Finding user in the databse according to the username
	 */
	@Test
	public void testFindUserByUsername() {
		setMocksForUserService(generateString());
		User user = as.findUserByUsername(MIKE4);
		assertEquals(MIKE4, user.getUsername());
	}

@Test
	public void testFindUserByUsernameNotExisting() {
		User user = as.findUserByUsername("nouser");
	  assertNull(user);
	}

	/*
	Test if the user is part of one group
	 */
	@Test
	public void findGroupsByName() {
		String userName= generateString();
		String groupName= generateString();
		User groupUser = new User(userName);
		Group group = new Group(groupName);
		List<User> listUsers = new ArrayList<>();
		List<Group> listGroups = new ArrayList<>();
		listUsers.add(groupUser);
		listGroups.add(group);
		groupUser.setGroupParticipant(listGroups);
		group.setMembers(listUsers);
		as.addUser(groupUser);
		List<Group> g =  as.findGroupsByName(groupUser.getUsername());
		assertEquals(groupName, g.get(0).getName());
	}


	/*
	Test if the user is part of multiple groups
	 */
	@Test
	public void findMultipleGroupsByName()
	{
		String userName= generateString();
		String groupName= generateString();
		String groupName1= generateString();
		User groupUser = new User(userName);
		Group group = new Group(groupName);
		Group group1 = new Group(groupName1);
		List<User> listUsers = new ArrayList<>();
		List<Group> listGroups = new ArrayList<>();
		listUsers.add(groupUser);
		listGroups.add(group);
		listGroups.add(group1);
		groupUser.setGroupParticipant(listGroups);
		group.setMembers(listUsers);
		group1.setMembers(listUsers);
		as.addUser(groupUser);
		List<Group> g =  as.findGroupsByName(groupUser.getUsername());
		assertEquals(groupName, g.get(0).getName());
		assertEquals(groupName1, g.get(1).getName());
	}



	@Test
	public void updateUser() {
		Optional<User> user = as.findUserByName("MIKE1");
		if (user.isPresent()) {
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

	@Test
	public void testEmptyFindUserByName() {
		User user = as.findUserByUsername("Test94");
		assertNull(user);
	}


	@Test(expected = UserAlreadyPresentException.class)
	public void testUserAlreadyExistTwo() {
		as.addUser(new User("Test", "Test", "Test", "Test", "Test"));
		as.addUser(new User("Test", "Test", "Test", "Test", "Test"));
		assertFalse(false);
	}

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

	private String generateString() {
		byte[] array = new byte[7]; // length is bounded by 7
		new Random().nextBytes(array);
		String generatedString = new String(array, Charset.forName("UTF-8"));
		return generatedString;
	}

}
