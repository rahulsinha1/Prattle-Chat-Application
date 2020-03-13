package com.neu.prattle.service;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.model.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/***
 * Implementation of {@link UserService}
 *
 * It stores the user accounts in-memory, which means any user accounts
 * created will be deleted once the application has been restarted.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 */
public class UserServiceImpl implements UserService {

  private static UserService accountService;

  static {
    accountService = new UserServiceImpl();
  }

  private Set<User> userSet = new HashSet<>();

  /***
   * UserServiceImpl is a Singleton class.
   */
  protected UserServiceImpl() {

  }

  /**
   * Call this method to return an instance of this service.
   *
   * @return this
   */
  public static UserService getInstance() {
    return accountService;
  }

  /***
     *
     * @param name -> The name of the user.
     * @return An optional wrapper supplying the user.
     */
    @Override
    public Optional<User> findUserByName(String username) {
        final User user = new User(username);
        if (userSet.contains(user))
            return Optional.of(user);
        else
            return Optional.empty();
    }

    @Override
    public synchronized void addUser(User user) {
        if (userSet.contains(user))
            throw new UserAlreadyPresentException(String.format("User already present with name: %s", user.getUsername()));
    userSet.add(user);
  }

  @Override
  public User findUserByUsername(String name) {
    for (User user : userSet) {
      if (user.getUsername().equals(name)) {
        return user;
      }
    }
    return null;
  }

  @Override
  public List findGroupsByName(String name) {
    for (User user : userSet) {
      if (user.getUsername().equals(name)) {
        return user.getGroupParticipant();
      }
    }
    return Collections.emptyList();
  }

  @Override
  public void updateUser(User user) {
    userSet.add(user);
  }
}
