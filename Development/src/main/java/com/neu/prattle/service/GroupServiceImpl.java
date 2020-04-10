package com.neu.prattle.service;

import com.neu.prattle.exceptions.CannotRemoveModeratorException;
import com.neu.prattle.exceptions.CannotRemoveUserException;
import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.GroupDoesNotExistException;
import com.neu.prattle.exceptions.UserAlreadyModeratorException;
import com.neu.prattle.exceptions.UserAlreadyPresentInGroupException;
import com.neu.prattle.main.EntityManagerObject;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;


/**
 * GroupServiceImpl implements groupservice.
 */

public class GroupServiceImpl implements GroupService {

  private static GroupService groupService;
  private static UserService userService;
  private static final String GROUP_DOES_NOT_EXIST = "Group does not exist";

  private static final EntityManager manager = EntityManagerObject.getInstance();

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
    EntityTransaction transaction = null;

    if (isRecordExist(group.getName())) {
      throw new GroupAlreadyPresentException(String.format("Group already present with name: %s", group.getName()));
    }

    transaction = manager.getTransaction();

    transaction.begin();
    Optional<User> user = userService.findUserByName(group.getCreatedBy());

    user.ifPresent(group::setModerators);
    user.ifPresent(group::setMembers);

    manager.persist(group);
    transaction.commit();
  }

  @Override
  public void addUser(Group group, User user) {
    EntityTransaction transaction = null;
    transaction = manager.getTransaction();
    transaction.begin();

    Group groupObj = getGroupByName(group.getName());
    if (groupObj.getMembers().contains(user)) {
      throw new UserAlreadyPresentInGroupException("User already in this group");
    }
    groupObj.setMembers(user);
    manager.persist(group);
    transaction.commit();
  }

  @Override
  public void removeUser(Group group, User user) {
    Group groupObj = getGroupByName(group.getName());
    if (!groupObj.getMembers().contains(user)) {
      throw new CannotRemoveUserException("User not present in group");
    }
    User userObj = userService.findUserByUsername(user.getUsername());
    EntityTransaction transaction = null;
    transaction = manager.getTransaction();
    transaction.begin();

    if (group.getModerators().contains(user)) {
      throw new CannotRemoveUserException("Cannot remove . User is a moderator of the group");
    }

    groupObj.getMembers().remove(user);
    userObj.getGroupParticipant().remove(group);
    transaction.commit();
  }

  @Override
  public void addModerator(Group group, User moderator) {
    EntityTransaction transaction = null;
    transaction = manager.getTransaction();
    transaction.begin();

    Group groupObj = getGroupByName(group.getName());
    if (groupObj.getModerators().contains(moderator)) {
      throw new UserAlreadyModeratorException("User already a moderator of this group");
    }

    //Add as a user of the group before making a moderator
    if (!groupObj.getMembers().contains(moderator)) {
      groupObj.setMembers(moderator);
    }
    groupObj.setModerators(moderator);
    transaction.commit();
  }

  @Override
  public void removeModerator(Group group, User moderator) {
    EntityTransaction transaction = null;
    Group groupObj = getGroupByName(group.getName());
    if (!groupObj.getModerators().contains(moderator)) {
      throw new CannotRemoveModeratorException("User not a moderator of this group");
    }
    transaction = manager.getTransaction();
    transaction.begin();

    User mod = userService.findUserByUsername(moderator.getUsername());
    if (groupObj.getModerators().contains(moderator)) {
      if (groupObj.getModerators().size() == 1) {
        throw new CannotRemoveModeratorException("Unable to remove the only moderator");
      }
      groupObj.getModerators().remove(moderator);
      mod.getGroupModerator().remove(group);
    } else {
      throw new CannotRemoveModeratorException("User is not a moderator of this group");
    }
    transaction.commit();
  }


  @Override
  public void deleteGroup(String group) {

    if (!isRecordExist(group)) {
      throw new GroupDoesNotExistException(GROUP_DOES_NOT_EXIST);
    }
    EntityTransaction transaction = null;
    transaction = manager.getTransaction();
    transaction.begin();
    Group groupObj = getGroupByName(group);
    List<User> userList = groupObj.getMembers();

    for (User u : userList) {
      u.getGroupParticipant().removeIf(group1 -> group1.equals(groupObj));
      u.getGroupModerator().removeIf(group1 -> group1.equals(groupObj));
    }
    manager.remove(groupObj);
    transaction.commit();
  }

  @Override
  public void updateGroup(Group group) {
    if (!isRecordExist(group.getName())) {
      throw new GroupDoesNotExistException(GROUP_DOES_NOT_EXIST);
    }

    EntityTransaction transaction = null;
    transaction = manager.getTransaction();
    transaction.begin();

    Group groupObj = getGroupByName(group.getName());
    groupObj.setDescription(group.getDescription());
    groupObj.setPassword(group.getPassword());
    groupObj.setIsGroupPrivate(group.getIsGroupPrivate());

    transaction.commit();
  }

  @Override
  public List getAllGroups() {
    TypedQuery<Group> query = manager.createQuery("SELECT g FROM Group g", Group.class);
    return query.getResultList();
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
    if (isRecordExist(name)) {
      TypedQuery<Group> query = manager.createQuery(
              "SELECT g FROM Group g WHERE g.name = :name", Group.class);

      return query.setParameter("name", name).getSingleResult();
    } else {
      throw new GroupDoesNotExistException(GROUP_DOES_NOT_EXIST);
    }
  }

  @Override
  public List<Group> searchGroup(String keyword) {
    TypedQuery<Group> query = manager.createQuery("SELECT g FROM Group g WHERE g.name LIKE :name AND g.isGroupPrivate = false", Group.class);

    return query.setParameter("name", keyword+"%").getResultList();
  }

  private boolean isRecordExist(String groupName) {

    TypedQuery<Long> query = manager.createQuery(
            "SELECT count(g) FROM Group g WHERE g.name = :name", Long.class);

    Long count = query.setParameter("name", groupName).getSingleResult();

    return (!count.equals(0L));
  }
}
