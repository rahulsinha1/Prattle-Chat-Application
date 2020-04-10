package com.neu.prattle.serviceTest;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.User;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

  @Mock
  private EntityManager manager;

  @Mock
  private EntityTransaction transaction;

  @Mock
  private TypedQuery<User> query;

  @Mock
  private TypedQuery<Long> queryLong;

  private static final String SELECT_QUERY = "SELECT u FROM User u WHERE u.username = :name";
  private static final String SELECT_QUERY_COUNT = "SELECT count(u) FROM User u WHERE u.username = :name";
  private UserService userService = UserServiceImpl.getInstance();

  @Before
  public void setup() throws ReflectiveOperationException {
    setFinalStaticField(UserServiceImpl.class, "manager", manager);
  }

  @Test
  public void testFindUserByName() {
    setMocksForIsRecordExists(1L);
    setMocksForFindByUser();
    assertEquals("User", userService.findUserByName("User").get().getUsername());
  }

  @Test
  public void testFindUserByNameNoRecordExists() {
    setMocksForIsRecordExists(0L);
    assertFalse(userService.findUserByName("User").isPresent());
  }

  @Test
  public void testCreateUser() {
    User user = new User("User", "Test", "User", "pass1234", "GMT");
    setMocksForIsRecordExists(0L);
    setMocksForEntityPersist();
    userService.addUser(user);
  }

  @Test(expected = UserAlreadyPresentException.class)
  public void tesCreateUserThrowsException() {
    User user = new User("User", "Test", "User", "pass1234", "GMT");
    setMocksForIsRecordExists(1L);
    userService.addUser(user);
  }

  @Test
  public void testFindUserByUsername() {
    setMocksForIsRecordExists(1L);
    setMocksForFindByUser();
    assertEquals("User", userService.findUserByUsername("User").getUsername());
  }

  @Test(expected = UserDoesNotExistException.class)
  public void testFindUserByUsernameRecordDoesNotExist() {
    setMocksForIsRecordExists(0L);
    userService.findUserByUsername("User");
  }

  @Test
  public void testFindGroupsByName() {
    setMocksForIsRecordExists(1L);
    setMocksForFindByUser();
    assertEquals(0, userService.findGroupsByName("User").size());
  }

  @Test
  public void testFindGroupsByNameNoRecordExists() {
    setMocksForIsRecordExists(0L);
    assertEquals(0, userService.findGroupsByName("User").size());
  }

  @Test
  public void testUpdateUser() {
    userService.updateUser(setMocksForUpdateDeleteUser());
  }

  @Test(expected = UserDoesNotExistException.class)
  public void testUpdateUserRecordDoesNotExist() {
    setMocksForIsRecordExists(0L);
    userService.updateUser(new User());
  }

  @Test
  public void testDeleteUser() {
    userService.deleteUser(setMocksForUpdateDeleteUser());
  }

  @Test(expected = UserDoesNotExistException.class)
  public void testDeleteUserRecordDoesNotExist() {
    setMocksForIsRecordExists(0L);
    userService.deleteUser(new User());
  }

  private void setMocksForIsRecordExists(Long count) {
    when(manager.createQuery(SELECT_QUERY_COUNT, Long.class)).thenReturn(queryLong);
    when(queryLong.setParameter(anyString(), anyString())).thenReturn(queryLong);
    when(queryLong.getSingleResult()).thenReturn(count);
  }

  private void setMocksForFindByUser() {
    User user = new User("User", "Test", "User", "pass1234", "GMT");
    when(manager.createQuery(SELECT_QUERY, User.class)).thenReturn(query);
    when(query.setParameter(anyString(), anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(user);
  }

  public static void setFinalStaticField(Class<?> clazz, String fieldName, Object value)
          throws ReflectiveOperationException {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    Field modifiers = Field.class.getDeclaredField("modifiers");
    modifiers.setAccessible(true);
    modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    field.set(null, value);
  }

  @Test
  public void testSearchUser()
  {
    setMocksForSearchUser();
    assertEquals(2,userService.searchUser("te").size());
  }


  private void setMocksForSearchUser() {
    User user = new User("User", "Test", "User", "pass1234", "GMT");
    User user2 = new User("User2", "Test2", "User2", "pass12342", "GMT");
    List<User> userList = new ArrayList<>(Arrays.asList(user,user2));
    when(manager.createQuery("SELECT u FROM User u WHERE u.username LIKE :name", User.class)).thenReturn(query);
    when(query.setParameter(anyString(), anyString())).thenReturn(query);
    when(query.getResultList()).thenReturn(userList);
  }

  private void setMocksForEntityPersist() {
    User user = new User("User", "Test", "User", "pass1234", "GMT");
    when(manager.getTransaction()).thenReturn(transaction);
    doNothing().when(transaction).begin();
    doNothing().when(manager).persist(user);
    doNothing().when(transaction).commit();
  }

  private User setMocksForUpdateDeleteUser() {
    setMocksForIsRecordExists(1L);
    setMocksForFindByUser();
    User user = userService.findUserByUsername("User");
    setMocksForEntityPersist();
    return user;
  }
}