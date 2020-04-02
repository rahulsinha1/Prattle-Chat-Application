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
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;


/**
 * GroupServiceImpl implements groupservice.
 */

public class GroupServiceImpl implements GroupService {

  private static GroupService groupService;
  private static UserService userService;

  //private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("fse");
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
    create(group);
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
      /*manager.createNativeQuery("INSERT INTO group_users (user_id, group_id) VALUES (?,?)")
              .setParameter(1, user.getUserId())
              .setParameter(2, group.getId())
              .executeUpdate();*/
        manager.persist(group);
        transaction.commit();
      }


  @Override
  public void removeUser(Group group, User user) {
    EntityTransaction transaction = null;
    transaction = manager.getTransaction();
        transaction.begin();
        Group groupObj = getGroupByName(group.getName());
        if (group.getModerators().contains(user)) {
          throw new CannotRemoveUserException("Cannot remove . User is a moderator of the group");
        }
        groupObj.getMembers().remove(user);

     /* manager.createNativeQuery("DELETE FROM group_users WHERE user_id = ? AND group_id = ?")
              .setParameter(1, user.getUserId())
              .setParameter(2, group.getId())
              .executeUpdate();*/
        transaction.commit();
      }




      @Override
      public void addModerator (Group group, User moderator){
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
      /*manager.createNativeQuery("INSERT INTO group_mods (moderator_id, group_id) VALUES (?,?)")
              .setParameter(1, moderator.getUserId())
              .setParameter(2, group.getId())
              .executeUpdate();*/

            groupObj.setModerators(moderator);
            transaction.commit();
          }





      @Override
      public void removeModerator (Group group, User moderator){
        EntityTransaction transaction = null;
        transaction = manager.getTransaction();
        transaction.begin();

    /*  manager.createNativeQuery("DELETE FROM group_mods WHERE moderator_id = ? AND group_id = ?")
              .setParameter(1, moderator.getUserId())
              .setParameter(2, group.getId())
              .executeUpdate();*/
            Group groupObj = getGroupByName(group.getName());
            if (groupObj.getModerators().contains(moderator)) {
              if (groupObj.getModerators().size() == 1) {
                throw new CannotRemoveModeratorException("Unable to remove the only moderator");
              }
              groupObj.getModerators().remove(moderator);
            } else {
              throw new CannotRemoveModeratorException("User is not a moderator of this group");
            }
            transaction.commit();
          }




      @Override
      public void deleteGroup (String group){

        if (!isRecordExist(group)) {
          throw new GroupDoesNotExistException("Group does not exist");
        }
        EntityTransaction transaction = null;
        transaction = manager.getTransaction();
            transaction.begin();
            Group groupObj = getGroupByName(group);
            List<User> userList = groupObj.getMembers();
            //List<User> moderatorList = groupObj.getModerators();

            for(User u : userList)
            {
              u.getGroupParticipant().removeIf(group1 -> group1.equals(groupObj));
              u.getGroupModerator().removeIf(group1 -> group1.equals(groupObj));
            }
            manager.remove(groupObj);
   /* manager.createNativeQuery("DELETE FROM groups WHERE group_name =? ")
            .setParameter(1, group.getName())
            .executeUpdate();*/
            transaction.commit();
          }


      @Override
      public void updateGroup (Group group){
        if (!isRecordExist(group.getName())) {
          throw new GroupDoesNotExistException("Group does not exist");
        }

        EntityTransaction transaction = null;
        transaction = manager.getTransaction();
            transaction.begin();

            Group groupObj = getGroupByName(group.getName());
            groupObj.setDescription(group.getDescription());
            groupObj.setPassword(group.getPassword());
            groupObj.setIsGroupPrivate(group.getIsGroupPrivate());

    /*manager.createNativeQuery("UPDATE groups SET group_description = ?, group_password = ?, is_private = ?  WHERE group_name= ?")
            .setParameter(1, group.getDescription())
            .setParameter(2, group.getPassword())
            .setParameter(3, group.getIsGroupPrivate())
            .setParameter(4, group.getName())
            .executeUpdate();*/

            transaction.commit();

          }



      @Override
      public List getAllGroups () {
        TypedQuery<Group> query = manager.createQuery("SELECT g FROM Group g", Group.class);
        return query.getResultList();
      }

      @Override
      public List getAllGroupsByUsername (String username){
        return userService.findGroupsByName(username);
      }

      @Override
      public void notifyGroup () {
        throw new UnsupportedOperationException("to be implemented");
      }

      @Override
      public Group getGroupByName (String name){
        if (isRecordExist(name)) {
          TypedQuery<Group> query = manager.createQuery(
                  "SELECT g FROM Group g WHERE g.name = :name", Group.class);

          return query.setParameter("name", name).getSingleResult();
        } else {
          throw new GroupDoesNotExistException("Group does not exist");
        }
      }


      private void create (Group group){
        EntityTransaction transaction = null;


        if (isRecordExist(group.getName())) {
          throw new GroupAlreadyPresentException(String.format("Group already present with name: %s", group.getName()));
        }

        transaction = manager.getTransaction();

            transaction.begin();
            Optional<User> user = userService.findUserByName(group.getCreatedBy());

            user.ifPresent(group::setModerators);
            user.ifPresent(group::setMembers);

            //group.setModerators(userService.findUserByName(group.getCreatedBy()).get());
            //group.setMembers(userService.findUserByName(group.getCreatedBy()).get());
            manager.persist(group);
            transaction.commit();
          }



      private boolean isRecordExist (String groupName){

        TypedQuery<Long> query = manager.createQuery(
                "SELECT count(g) FROM Group g WHERE g.name = :name", Long.class);


        Long count = query.setParameter("name", groupName).getSingleResult();

        return (!count.equals(0L));
      }
    }
