package com.neu.prattle.service;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

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
  private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
          .createEntityManagerFactory("fse");

  static {
    accountService = new UserServiceImpl();
  }

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
   * @param username -> The name of the user.
   * @return An optional wrapper supplying the user.
   */
  @Override
  public Optional<User> findUserByName(String username) {
    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    EntityTransaction transaction = null;

    if(isRecordExist(username)) {
      TypedQuery<User> query = manager.createQuery(
              "SELECT u FROM User u WHERE u.username = :name", User.class);

      User user = (User) query.setParameter("name", username).getSingleResult();
      return Optional.of(user);
    }
    else
      return Optional.empty();
  }

  @Override
  public synchronized void addUser(User user) {
    create(user);
  }

  @Override
  public User findUserByUsername(String name) {
    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();

    if(isRecordExist(name)) {
      TypedQuery<User> query = manager.createQuery(
              "SELECT u FROM User u WHERE u.username = :name", User.class);

      return (User) query.setParameter("name", name).getSingleResult();
    }
    else
        throw new UserDoesNotExistException("User does not exist.");
  }

  @Override
  public List findGroupsByName (String name){
      if (isRecordExist(name)) {
          EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
          TypedQuery<User> query = manager.createQuery(
              "SELECT u FROM User u WHERE u.username = :name", User.class);

          User user = (User) query.setParameter("name", name).getSingleResult();
          return user.getGroupParticipant();

      }
      return Collections.emptyList();
  }

  @Override
  public void updateUser (User updatedUser){


  }

  private void create (User user){

      EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
      EntityTransaction transaction = null;

      if (isRecordExist(user.getUsername())) {
          throw new UserAlreadyPresentException(String.format("User already present with name: %s", user.getUsername()));
      }
      transaction = manager.getTransaction();
      transaction.begin();


      manager.persist(user);

      transaction.commit();
      manager.close();
  }

  private boolean isRecordExist (String username){
      EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();

      TypedQuery<Long> query = manager.createQuery(
          "SELECT count(u) FROM User u WHERE u.username = :name", Long.class);


      Long count = (Long) query.setParameter("name", username).getSingleResult();
      return (!count.equals(0L));
  }
}
