package com.neu.prattle.service;

import com.neu.prattle.exceptions.UserDoesNotExist;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Moderator;
import com.neu.prattle.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * GroupServiceImpl implements groupservice.
 */
public class GroupServiceImpl implements GroupService {

  private static GroupService groupService;

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
  public void createGroup(Group group) {
    for (User user : group.getUsers()) {
      checkUserAndAddToGroup(group, user);
    }
    group.setId("" + groupSet.size() + 1);
    addModerator(group, group.getModerators().get(0));
    Long currentTimeStamp = new Date().getTime();
    group.setCreatedOn(currentTimeStamp.toString());
    groupSet.add(group);
  }

  @Override
  public void addUser(Group group, User user) {
    if (userGroupList.containsKey(group)) {
      List<User> userList = userGroupList.get(group.getId());
      userList.add(user);
      userGroupList.put(group.getId(), userList);
    } else {
      List<User> userList = new ArrayList();
      userList.add(user);
      userGroupList.put(group.getId(), userList);
    }
    checkUserAndAddToGroup(group, user);
  }


  @Override
  public void removeUser(Group group, User user) {
    if (userGroupList.containsKey(group)) {
      List<User> userList = userGroupList.get(group.getId());
      userList.remove(user);
      userGroupList.put(group.getId(), userList);
    }
  }

  @Override
  public void addModerator(Group group, Moderator moderator) {
    if (moderatorGroupList.containsKey(group)) {
      List<Moderator> moderatorList = moderatorGroupList.get(group.getId());
      moderatorList.add(moderator);
      moderatorGroupList.put(group.getId(), moderatorList);
    } else {
      List<Moderator> moderatorList = new ArrayList();
      moderatorList.add(moderator);
      moderatorGroupList.put(group.getId(), moderatorList);
    }
    checkUserAndAddToGroup(group, moderator);
  }

  @Override
  public void removeModerator(Group group, Moderator moderator) {
    if (moderatorGroupList.containsKey(group)) {
      List<Moderator> moderatorList = moderatorGroupList.get(group.getId());
      moderatorList.remove(moderator);
      moderatorGroupList.put(group.getId(), moderatorList);
    }
  }

  @Override
  public void updateGroup(Group group) {
    groupSet.add(group);
    List<User> usersInGroup = group.getUsers();
    for (User user : usersInGroup) {
      checkUserAndAddToGroup(group, user);
    }
  }

  @Override
  public void deleteGroup(Group group) {
    groupSet.remove(group);
  }

  @Override
  public List getAllGroups() {
    return Arrays.asList(groupSet.toArray());
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
    for (Group group : groupSet) {
      if (group.getName().equals(name))
        return group;
    }
    return null;
  }

  private void checkUserAndAddToGroup(Group group, User user) {
    if (userService.findUserByName(user.getUsername()).isPresent()) {
      List groupParticipant = user.getGroupParticipant();
      groupParticipant.add(group.getName());
      User currentUser = userService.findUserByUsername(user.getUsername());
      currentUser.setGroupParticipant(user.getGroupParticipant());
    } else {
      throw new UserDoesNotExist("User " + user.getUsername() + " does not exist in system.");
    }
  }
}
