package com.neu.prattle.service;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.Moderator;
import com.neu.prattle.model.User;

import java.util.List;

/***
 * Acts as an interface between the data layer and the
 * servlet controller.
 *
 * The controller is responsible for interfacing with this instance
 * to perform all the CRUD operations on groups.
 *
 *
 */
public interface GroupService {

  /**
   * create a group based on group model. Group name based be unique.
   *
   * @throws com.neu.prattle.exceptions.GroupAlreadyPresentException if group already present
   */
  void createGroup(Group group);

  /**
   * adds user to existing group
   *
   * @throws com.neu.prattle.exceptions.UserDoesNotExist if user is not found in system
   */
  void addUser(Group group, User user);

  /**
   * removes user from existing group
   *
   * @throws com.neu.prattle.exceptions.UserDoesNotExist if user is not found in system
   */
  void removeUser(Group group, User user);

  /**
   * adds moderator to existing group
   *
   * @throws com.neu.prattle.exceptions.UserDoesNotExist if user is not found in system
   */
  void addModerator(Group group, Moderator moderator);

  /**
   * removes moderator from existing group
   *
   * @throws com.neu.prattle.exceptions.UserDoesNotExist if user is not found in system
   */
  void removeModerator(Group group, Moderator moderator);

  /**
   * updates group information
   */
  void updateGroup(Group group);

  /**
   * deletes group from system
   */
  void deleteGroup(Group group);

  /**
   * gives list of all groups present in the system
   *
   * @return list of groups
   */
  List getAllGroups();

  /**
   * gives list of groups of which user is a part
   *
   * @return list of groups
   */
  List getAllGroupsByUsername(String username);

  /**
   * notifies all members of group about changes to group
   */
  void notifyGroup();

  /**
   * gives group details based on group name
   */
  Group getGroupByName(String name);
}
