package com.neu.prattle.service;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserDoesNotExistException;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
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
  private static final String selectQuery = "SELECT u FROM User u WHERE u.username = :name";

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

    if (isRecordExist(username)) {
      TypedQuery<User> query = manager.createQuery(
              selectQuery, User.class);

      User user = (User) query.setParameter("name", username).getSingleResult();
      return Optional.of(user);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public synchronized void addUser(User user) {
    create(user);
  }

  @Override
  public User findUserByUsername(String name) {
    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();

    if (isRecordExist(name)) {
      TypedQuery<User> query = manager.createQuery(
              selectQuery, User.class);

      return (User) query.setParameter("name", name).getSingleResult();

    } else {
      throw new UserDoesNotExistException("User does not exist.");
    }
  }

  @Override
  public List findGroupsByName(String name) {
    if (isRecordExist(name)) {
      EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
      TypedQuery<User> query = manager.createQuery(
              selectQuery, User.class);


      User user = (User) query.setParameter("name", name).getSingleResult();

      Query query1 = manager.createNativeQuery("Select * from groups where group_id in ( select group_id from group_users where user_id =?)", Group.class)
              .setParameter(1, user.getUser_id());

      return (List<Group>) query1.getResultList();
    }
    return Collections.emptyList();

  }

  @Override
  public void updateUser(User user) {
    if (!isRecordExist(user.getUsername())) {
      throw new UserDoesNotExistException("User does not exist");
    }
    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    EntityTransaction transaction = null;
    transaction = manager.getTransaction();
    transaction.begin();

    manager.createNativeQuery("UPDATE users SET timezone = ?, first_name = ?, last_name = ?, user_password =? WHERE username= ?")
            .setParameter(1, user.getTimezone())
            .setParameter(2, user.getFirstName())
            .setParameter(3, user.getLastName())
            .setParameter(4, user.getPassword())
            .setParameter(5, user.getUsername()).executeUpdate();

    transaction.commit();
    manager.close();
  }

  private void create(User user) {

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

  private boolean isRecordExist(String username) {
    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();

    TypedQuery<Long> query = manager.createQuery(
            "SELECT count(u) FROM User u WHERE u.username = :name", Long.class);


    Long count = (Long) query.setParameter("name", username).getSingleResult();
    return (!count.equals(0L));
  }
}
