package com.neu.prattle.service;

import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.GroupDoesNotExistException;
import com.neu.prattle.exceptions.UserAlreadyPresentInGroupException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Moderator;
import com.neu.prattle.model.User;

import java.util.*;

/**
 * GroupServiceImpl implements groupservice.
 */
public class GroupServiceImpl implements GroupService {

  private static GroupService groupService;

  static {
    groupService = new GroupServiceImpl();
  }

  private Set<Group> groupSet = new HashSet<>();

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
      for (Group groupInSet : groupSet){
          if(groupInSet.getName().equals(group.getName())){
              throw new GroupAlreadyPresentException(String.format("Group already present with name: %s", group.getName()));
          }
      }
      groupSet.add(group);
  }

    @Override
    public List<Group> getAllGroups() {
      return new ArrayList<>(groupSet);
    }

    @Override
    public List getAllGroupsByUsername(String username) {
        List<Group> groups = new ArrayList<>();

                for(Group groupInSet : groupSet){
                    for(Moderator moderator:groupInSet.getModerators()){
                        if(moderator.getUsername().equals(username)){
                            groups.add(groupInSet);
                            return groups;
                        }
                    }
                }
        throw new GroupDoesNotExistException("User is not apart of any group.");
    }

    @Override
    public void deleteGroup(Group group) {
        groupSet.remove(group);
    }



  @Override
  public void addUser(Group group, User user) {
      if(group.getModerators().contains(user) || group.getMembers().contains(user)){
          throw new UserAlreadyPresentInGroupException("User is already present in the group.");
      } else {
          group.getMembers().add(user);
      }
  }


  @Override
  public void removeUser(Group group, User user) {
      group.getMembers().remove(user);
  }

  @Override
  public void addModerator(Group group, Moderator moderator) {
        group.getModerators().add(moderator);
  }

  @Override
  public void removeModerator(Group group, Moderator moderator) {
      group.getModerators().remove(moderator);
  }

  @Override
  public void updateGroup(Group group) {
      this.getGroupByName(group.getName()).setMembers(group.getMembers());
      this.getGroupByName(group.getName()).setIsGroupPrivate(group.getIsGroupPrivate());
      this.getGroupByName(group.getName()).setModerators(group.getModerators());
      this.getGroupByName(group.getName()).setDescription(group.getDescription());
      this.getGroupByName(group.getName()).setPassword(group.getPassword());

  }


  @Override
  public void notifyGroup() {
    throw new UnsupportedOperationException("to be implemented");
  }

  @Override
  public Group getGroupByName(String name) {
    for (Group group : groupSet) {
      if (group.getName().equals(name)){
          return group;
      }
    }

    throw new GroupDoesNotExistException(String.format("Group does not exist with name: %s", name));
  }

}
