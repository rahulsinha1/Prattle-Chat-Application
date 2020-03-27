package com.neu.prattle.service;

import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Moderator;
import com.neu.prattle.model.User;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.EntityManagerFactory;

/**
 * GroupServiceImpl implements groupservice.
 */
public class GroupServiceImpl implements GroupService {

  private static GroupService groupService;
  private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
          .createEntityManagerFactory("fse");

  static {
    groupService = new GroupServiceImpl();
  }

  private UserService userService = UserServiceImpl.getInstance();
  private Set<Group> groupSet = new HashSet<>();
  private Map<String, List<User>> userGroupList = new HashMap<>();
  private Map<String, List<Moderator>> moderatorGroupList = new HashMap<>();
  /***
   * GroupServiceImpl is a Singleton class.
   */
  private GroupServiceImpl() {

  }

  /**
   * Call this method to return an instance of this service.
   *
   * @return this
   */
  public static GroupService getInstance() {
    return groupService;
  }

  @Override
  public void createGroup(Group group){
      create(group);
  }

  @Override
  public void addUser(Group group, User user) {

    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    EntityTransaction transaction = null;
    transaction = manager.getTransaction();
    transaction.begin();
    manager.createNativeQuery("INSERT INTO group_users (user_id, group_id) VALUES (?,?)")
            .setParameter(1, user.getUser_id())
            .setParameter(2, group.getId())
            .executeUpdate();
  }


  @Override
  public void removeUser(Group group, User user) {
    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    EntityTransaction transaction = null;
    transaction = manager.getTransaction();
    transaction.begin();
    manager.createNativeQuery("DELETE FROM group_users u WHERE u.user_id = :1 AND u.group_id = :2")
            .setParameter(1, user.getUser_id())
            .setParameter(2, group.getId())
            .executeUpdate();
  }

  @Override
  public void addModerator(Group group, User moderator) {
    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    EntityTransaction transaction = null;
    transaction = manager.getTransaction();
    transaction.begin();
    manager.createNativeQuery("INSERT INTO group_mods (user_id, group_id) VALUES (?,?)")
            .setParameter(1, moderator.getUser_id())
            .setParameter(2, group.getId())
            .executeUpdate();
  }


  @Override
  public void removeModerator(Group group, User moderator) {
    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    EntityTransaction transaction = null;
    transaction = manager.getTransaction();
    transaction.begin();
    manager.createNativeQuery("DELETE FROM group_mods WHERE moderator_id = ? AND u.group_id = ?")
            .setParameter(1, moderator.getUser_id())
            .setParameter(2, group.getId())
            .executeUpdate();
  }


  @Override
  public void deleteGroup(Group group) {

    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    EntityTransaction transaction = null;
    transaction = manager.getTransaction();
    transaction.begin();
    manager.createNativeQuery("DELETE FROM groups WHERE group_name =? ")
            .setParameter(1, group.getName())
            .executeUpdate();
  }

  @Override
  public void updateGroup(Group group) {
    groupSet.add(group);
    List<User> usersInGroup = group.getMembers();
    for (User user : usersInGroup) {
      checkUserAndAddToGroup(group, user);
    }
  }

  @Override
  public List getAllGroups() {
    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    TypedQuery<Group> query = manager.createQuery("SELECT g FROM Group g", Group.class);
    List<Group> results = query.getResultList();
    return results;
  }

  @Override
  public List getAllGroupsByUsername(String username) {

    return userService.findGroupsByName(username);
  }

  @Override
  public void notifyGroup() {
    throw new UnsupportedOperationException("to be implemented");
  }

  @Override
  public Group getGroupByName(String name) {
    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();

    if(isRecordExist(name)) {
      TypedQuery<Group> query = manager.createQuery(
              "SELECT g FROM Group g WHERE g.name = :name", Group.class);

      Group group = (Group) query.setParameter("name", name).getSingleResult();
      return group;
    }
    else
      return null;
  }

  private void checkUserAndAddToGroup(Group group, User user) {
    if (userService.findUserByName(user.getUsername()).isPresent()) {
      List groupParticipant = user.getGroupParticipant();
      groupParticipant.add(group.getName());
      User currentUser = userService.findUserByUsername(user.getUsername());
      currentUser.setGroupParticipant(user.getGroupParticipant());
    } else {
      throw new UserDoesNotExistException(String.format("User %s does not exist in system.", user.getUsername()));
    }
  }

  private void create(Group group) {

    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    EntityTransaction transaction = null;


    if(isRecordExist(group.getName()))
    {
      throw new GroupAlreadyPresentException(String.format("Group already present with name: %s", group.getName()));
    }
    transaction = manager.getTransaction();
    transaction.begin();



    manager.persist(group);

    transaction.commit();
    manager.close();
  }

  private boolean isRecordExist(String groupName) {
    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();

    TypedQuery<Long> query = manager.createQuery(
            "SELECT count(g) FROM Group g WHERE g.name = :name", Long.class);


    Long count = (Long) query.setParameter("name", groupName).getSingleResult();
    return ( ( count.equals( 0L ) ) ? false : true );
  }
}
