package com.neu.prattle.serviceTest;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.model.Group;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;

import com.neu.prattle.model.User;

public class UserServiceImplTest {

	private UserService as;
	private static final String MIKE4 = "Mike4";
	private static final String MIKE1 = "Mike1";

	@Before
	public void setUp() {
		as = UserServiceImpl.getInstance();
	}


	// This method just tries to add
	@Test
	public void setUserTest(){
		setMocksForUserService("Mike5");
	}

	// This method just tries to add
	@Test
	public void getUserTest(){
		Optional<User> user = as.findUserByName(MIKE1);
		assertTrue(user.isPresent());
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

	@Test
	public void findGroupsByName() {
		setMocksForUserService("Mike3");
		List<String> g = as.findGroupsByName("Mike3");
		assertEquals("test",g.get(0));
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

	@Test
	public void testEmptyFindUserByName() {
		User user = as.findUserByUsername("Test94");
		assertNull(user);
	}

	@Test(expected = UserAlreadyPresentException.class)
	public void testUserAlreadyExist() {
		as.addUser(new User("Test1"));
		as.addUser(new User("Test1"));
	}

    @Test(expected = UserAlreadyPresentException.class)
    public void testUserAlreadyExistTwo() {
        as.addUser(new User("Test", "Test","Test", "Test","Test"));
        as.addUser(new User("Test", "Test","Test", "Test","Test"));
        assertFalse(false);
    }

	@Test
	public void testEmptyGroup() {
		List g = as.findGroupsByName("Test93");
		assertEquals(0,g.size());
	}


	private void setMocksForUserService(String name){
		List<String> groups = new ArrayList<>();
		Group g = new Group();
		g.setName("test");
		groups.add(g.getName());
		User u = new User(name);
		u.setGroupParticipant(groups);
		as.addUser(u);
	}
}
