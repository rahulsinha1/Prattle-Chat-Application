package com.neu.prattle.service;

import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.main.EntityManagerObject;
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
  private static final String SELECT_QUERY = "SELECT u FROM User u WHERE u.username = :name";
  //private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("fse");
  private static final EntityManager manager = EntityManagerObject.getInstance();


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
    if (isRecordExist(username)) {
      TypedQuery<User> query = manager.createQuery(
              SELECT_QUERY, User.class);

      User user = query.setParameter("name", username).getSingleResult();
      return Optional.of(user);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void addUser(User user) {
    create(user);
  }

  @Override
  public User findUserByUsername(String name) {
    if (isRecordExist(name)) {
      TypedQuery<User> query = manager.createQuery(
              SELECT_QUERY, User.class);
      return query.setParameter("name", name).getSingleResult();

    } else {
      throw new UserDoesNotExistException("User does not exist.");
    }
  }

  @Override
  public List<Group> findGroupsByName(String name) {
    if (isRecordExist(name)) {
      //TypedQuery<User> query = manager.createQuery(
              //SELECT_QUERY, User.class);


      User user = findUserByUsername(name);
      return user.getGroupParticipant();


     // User user = query.setParameter("name", name).getSingleResult();

      //Query query1 = manager.createNativeQuery("Select * from groups where group_id in ( select group_id from group_users where user_id =?)", Group.class)
      //        .setParameter(1, user.getUserId());

      //return query1.getResultList();
    }
    return Collections.emptyList();

  }

  @Override
  public void updateUser(User user) {
    if (!isRecordExist(user.getUsername())) {
      throw new UserDoesNotExistException("User does not exist");
    }
    EntityTransaction transaction = null;
    transaction = manager.getTransaction();
    transaction.begin();

    User userObj = findUserByUsername(user.getUsername());
    userObj.setTimezone(user.getTimezone());
    userObj.setFirstName(user.getFirstName());
    userObj.setLastName(user.getLastName());
    userObj.setPassword(user.getPassword());

    /*manager.createNativeQuery("UPDATE users SET timezone = ?, first_name = ?, last_name = ?, user_password =? WHERE username= ?")
            .setParameter(1, user.getTimezone())
            .setParameter(2, user.getFirstName())
            .setParameter(3, user.getLastName())
            .setParameter(4, user.getPassword())
            .setParameter(5, user.getUsername()).executeUpdate();*/

    transaction.commit();
  }

  @Override
  public void deleteUser(User user) {
    if (!isRecordExist(user.getUsername())) {
      throw new UserDoesNotExistException("User does not exist");
    }
    EntityTransaction transaction = null;
    transaction = manager.getTransaction();
        transaction.begin();
        User userObj = findUserByUsername(user.getUsername());
        manager.remove(userObj);
   /* manager.createNativeQuery("DELETE FROM groups WHERE group_name =? ")
            .setParameter(1, group.getName())
            .executeUpdate();*/
        transaction.commit();
      }



  private void create(User user) {
    EntityTransaction transaction = null;
    if (isRecordExist(user.getUsername())) {
      throw new UserAlreadyPresentException(String.format("User already present with name: %s", user.getUsername()));
    }
    transaction = manager.getTransaction();
    transaction.begin();
    manager.persist(user);
    transaction.commit();
  }

  private boolean isRecordExist(String username) {

    TypedQuery<Long> query = manager.createQuery(
            "SELECT count(u) FROM User u WHERE u.username = :name", Long.class);


    Long count = query.setParameter("name", username).getSingleResult();
    return (!count.equals(0L));
  }
}
