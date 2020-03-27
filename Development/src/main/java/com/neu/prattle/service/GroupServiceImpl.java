package com.neu.prattle.service;

import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.GroupDoesNotExistException;
import com.neu.prattle.exceptions.UserAlreadyPresentInGroupException;
import com.neu.prattle.exceptions.UserDoesNotExistException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.HEAD;


/**
 * GroupServiceImpl implements groupservice.
 */
public class GroupServiceImpl implements GroupService {

  private static GroupService groupService;
  private static UserService userService;

  private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
          .createEntityManagerFactory("fse");

  static {
    groupService = new GroupServiceImpl();
    userService = new UserServiceImpl();
  }

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
  public void createGroup(Group group) {
    create(group);
  }

  @Override
  public void addUser(Group group, User user) {

//    System.out.println(user);
//    if (group.getModerators().contains(user) || group.getMembers().contains(user)) {
//      throw new UserAlreadyPresentInGroupException("User is already present in the group.");
//    } else {
      EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
      EntityTransaction transaction = null;
      transaction = manager.getTransaction();
      transaction.begin();
      manager.createNativeQuery("INSERT INTO group_users (user_id, group_id) VALUES (?,?)")
              .setParameter(1, user.getUser_id())
              .setParameter(2, group.getId())
              .executeUpdate();


    Group groupObj = manager.find(Group.class, group.getId());
    User userObj = manager.find(User.class, user.getUser_id());

     List<User> groupUsers = groupObj.getMembers();
     groupUsers.add(user);
     groupObj.setMembers(groupUsers);
     manager.merge(groupObj);

    List<Group> groupPart = userObj.getGroupParticipant();
    groupPart.add(group);
    userObj.setGroupParticipant(groupPart);
    manager.merge(userObj);


      transaction.commit();
      manager.close();
    }


  @Override
  public void removeUser(Group group, User user) {

//    if (!group.getMembers().contains(user)) {
    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    EntityTransaction transaction = null;
    transaction = manager.getTransaction();
    transaction.begin();
    manager.createNativeQuery("DELETE FROM group_users u WHERE u.user_id = :1 AND u.group_id = :2")
            .setParameter(1, user.getUser_id())
            .setParameter(2, group.getId())
            .executeUpdate();

    Group groupObj = manager.find(Group.class, group.getId());
    User userObj = manager.find(User.class, user.getUser_id());

    List<User> groupUsers = groupObj.getMembers();

    for (User userEntry : groupUsers) {
      if (userEntry.equals(user)) {
        groupUsers.remove(user);
      }
    }
      groupObj.setMembers(groupUsers);
      manager.merge(groupObj);

      List<Group> groupPart = userObj.getGroupParticipant();

      for (Group groupEntry : groupPart) {
        if (groupEntry.equals(group)) {
          groupPart.remove(group);
        }
      }
        userObj.setGroupParticipant(groupPart);

        manager.merge(userObj);
        transaction.commit();
        manager.close();

//    } else {
//      throw new UserDoesNotExistException("User does not exist in the group.");
//    }
      }


  @Override
  public void addModerator(Group group, User moderator) {

//    if (!group.getModerators().contains(moderator)) {
      EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
      EntityTransaction transaction = null;
      transaction = manager.getTransaction();
      transaction.begin();
      manager.createNativeQuery("INSERT INTO group_mods (moderator_id, group_id) VALUES (?,?)")
              .setParameter(1, moderator.getUser_id())
              .setParameter(2, group.getId())
              .executeUpdate();

    Group groupObj = manager.find(Group.class, group.getId());

    List<User> groupMods = groupObj.getMembers();
    groupMods.add(moderator);
    groupObj.setModerators(groupMods);
    manager.merge(groupObj);

      transaction.commit();
      manager.close();
//    } else {
//      throw new UserAlreadyPresentInGroupException("Moderator is already present in the group.");
//    }
  }


  @Override
  public void removeModerator(Group group, User moderator) {

//    if (group.getModerators().contains(moderator)) {
      EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
      EntityTransaction transaction = null;
      transaction = manager.getTransaction();
      transaction.begin();
      manager.createNativeQuery("DELETE FROM group_mods WHERE moderator_id = ? AND u.group_id = ?")
              .setParameter(1, moderator.getUser_id())
              .setParameter(2, group.getId())
              .executeUpdate();

    Group groupObj = manager.find(Group.class, group.getId());

    List<User> groupMods = groupObj.getMembers();

    for (User modsEntry : groupMods) {
      if (modsEntry.equals(moderator)) {
        groupMods.remove(moderator);
      }
    }
    groupObj.setModerators(groupMods);
    manager.merge(groupObj);

      transaction.commit();
      manager.close();
//    } else {
//      throw new UserDoesNotExistException("Moderator does not exist.");
//    }

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
    transaction.commit();
    manager.close();
  }

  @Override
  public void updateGroup(Group group) {
    if (!isRecordExist(group.getName())) {
      throw new GroupDoesNotExistException("Group does not exist");
    }
    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    EntityTransaction transaction = null;
    transaction = manager.getTransaction();
    transaction.begin();

    manager.createNativeQuery("UPDATE groups SET group_description = ?, group_password = ?, is_private = ?  WHERE group_name= ?")
            .setParameter(1, group.getDescription())
            .setParameter(2, group.getPassword())
            .setParameter(3, group.getIsGroupPrivate())
            .setParameter(4, group.getName())
            .executeUpdate();

    transaction.commit();
    manager.close();

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

    if (isRecordExist(name)) {
      TypedQuery<Group> query = manager.createQuery(
              "SELECT g FROM Group g WHERE g.name = :name", Group.class);

      return (Group) query.setParameter("name", name).getSingleResult();
    } else
    throw new GroupDoesNotExistException("Group does not exist");
  }



  private void create(Group group) {

    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    EntityTransaction transaction = null;


    if (isRecordExist(group.getName())) {
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
    return (!count.equals(0L));
  }
}
