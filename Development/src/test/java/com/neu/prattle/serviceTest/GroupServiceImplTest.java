
package com.neu.prattle.serviceTest;

import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.GroupDoesNotExistException;
import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Moderator;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;


import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceImplTest {

  private GroupService groupService;
  private UserService userService;
  private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
          .createEntityManagerFactory("fse");

  @Mock
  private GroupService mockGroupService;
  @Mock
  private UserService mockUserService;
  private static final String TESTUSER = "testuser";
  private static final String TESTMODERATOR = "testmoderator";
  private Group group = new Group();
  private Group group2;
  private static final String TESTGROUPNAME = "testgroupname";


  @Before
  public void setUp() {
    group2 = new Group("Test","This is a Test.","TestTest","Test", false);
    groupService = GroupServiceImpl.getInstance();
    userService = UserServiceImpl.getInstance();
    mockGroupService = GroupServiceImpl.getInstance();
    mockUserService = UserServiceImpl.getInstance();
    MockitoAnnotations.initMocks(this);


  }

  /*
    Test the creation of a group
   */
  @Test
  public void testCreateGroup() {
    String groupName = generateString();
    Group group = new Group(groupName);
    groupService.createGroup(group);
    Group createdGroup = groupService.getGroupByName(groupName);
    assertEquals(createdGroup.getName(),groupName);
  }

  /*
    Test the creation of a group with name which already exists
   */

  @Test(expected = GroupAlreadyPresentException.class)
  public void testGroupCreation(){
    String groupName = generateString();
    Group group = new Group(groupName);
    Group group1 = new Group(groupName);
    groupService.createGroup(group);
    groupService.createGroup(group1);
    assertFalse(false);
  }



  /*

  test for getting a group by name
   */

  @Test
  public void getGroupByName()
  {
    String groupName = generateString();
    Group group = new Group(groupName);
    groupService.createGroup(group);
    Group createdGroup = groupService.getGroupByName(groupName);
    assertEquals(createdGroup.getName(),groupName);
  }

   /*

  test for getting a group by name which does not exist
   */


  @Test(expected =  GroupDoesNotExistException.class)
  public void getGroupByNameNotExisting()
  {
    Group createdGroup = groupService.getGroupByName("NoGroup");
  }


   /*
    Test to check if user is added to the group
   */


  @Test
  public void testAddUserToGroup () {
    String groupName = generateString();
    String userName = generateString();
    Group g = new Group();
    g.setName(groupName);
    User u = new User(userName);
    u.setFirstName(generateString());
    userService.addUser(u);
    groupService.createGroup(g);
    groupService.addUser(g,u);
    Group group = groupService.getGroupByName(groupName);


    //verify(mockGroupService).addUser(g,u);

   EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
   Query query1 = manager.createNativeQuery("Select * from group_users where group_id= ?")
                   .setParameter(1, group.getId());

   List list =  query1.getResultList();
   assertEquals(list.size(),1);
  }

  /*
  Tests to verify that multiple users are added to the group
   */
  @Test
  public void testAddMultipleUserToGroup () {
    String groupName = generateString();
    String userName = generateString();
    Group g = new Group();
    g.setName(groupName);
    User u = new User(userName);
    u.setFirstName(generateString());
    userService.addUser(u);
    groupService.createGroup(g);
    //Adding first user
    groupService.addUser(g,u);
    //Adding second user to group
    User user2 = new User(generateString());
    userService.addUser(user2);
    groupService.addUser(g,user2);
    Group group = groupService.getGroupByName(groupName);

    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    Query query1 = manager.createNativeQuery("Select * from group_users where group_id= ?")
            .setParameter(1, group.getId());

    List list =  query1.getResultList();
    assertEquals(list.size(),2);
  }


  /*
   Test to check if user is removed to the group
  */
  @Test
  public void testRemoveUserFromGroup () {
    String groupName = generateString();
    String userName = generateString();
    Group g = new Group();
    g.setName(groupName);
    User u = new User(userName);
    u.setFirstName(generateString());
    userService.addUser(u);
    groupService.createGroup(g);
    groupService.addUser(g,u);
    Group group = groupService.getGroupByName(groupName);

    //Adding second user to group
    User user2 = new User(generateString());
    userService.addUser(user2);
    groupService.addUser(g,user2);

    //TRemoving second user
    mockGroupService.removeUser(g,user2);
    verify(mockGroupService).removeUser(g,user2);
  }


  /*
    Verify adding a single moderator to a group
   */
  @Test
  public void testAddModeratorToGroup () {
    String groupName = generateString();
    String moderatorName = generateString();
    Group g = new Group();
    g.setName(groupName);
    User mod = new User(moderatorName);
    userService.addUser(mod);
    groupService.createGroup(g);
    groupService.addModerator(g,mod);
    Group group = groupService.getGroupByName(groupName);


    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    Query query1 = manager.createNativeQuery("Select * from group_mods where group_id= ?")
            .setParameter(1, group.getId());

    List list =  query1.getResultList();
    assertEquals(list.size(),1);
  }


  /*
   Test to check if moderator is removed from the group
  */
  @Test
  public void testRemoveModeratorFromGroup () {
    String groupName = generateString();
    String modName = generateString();
    Group g = new Group();
    g.setName(groupName);
    User mod = new User(modName);
    userService.addUser(mod);
    groupService.createGroup(g);
    Group group = groupService.getGroupByName(groupName);
    groupService.addModerator(group,mod);

    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    Query query1 = manager.createNativeQuery("Select * from group_mods where group_id= ?")
            .setParameter(1, group.getId());

    List list =  query1.getResultList();
    assertEquals(1, list.size());

    User mod2 = new User(generateString());
    userService.addUser(mod2);
    groupService.addModerator(group,mod2);

    mockGroupService.removeModerator(group, mod2);
    verify(mockGroupService).removeModerator(group,mod2);
  }


  /*
  Updating a group. Adding a description and verifying itys been updated
   */
  @Test
  public void testUpdateGroup () {
    String groupName = generateString();
    String moderator = "Moderator"+generateString();
    String username = "User"+generateString();
    Group g = new Group();
    g.setName(groupName);
    User m = new User(moderator);
    userService.addUser(m);
    List<User> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User(username);
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setMembers(users);
    userService.addUser(u);
    //Creating a group without description
    groupService.createGroup(g);
    g.setDescription("This is a test group");
    groupService.updateGroup(g);
    assertEquals("This is a test group",groupService.getGroupByName(groupName).getDescription());
  }

  private void setMocksForGroupCreation() {
    User u = new User(generateString());
    u.setFirstName(generateString());
    userService.addUser(u);
    User m = new User(TESTMODERATOR+generateString());
    m.setFirstName(generateString());
    userService.addUser(m);
    List<User> users = new ArrayList<>();
    users.add(u);
    List<User> moderators = new ArrayList<>();
    moderators.add(m);
    group.setMembers(users);
    group.setName(generateString());
    group.setModerators(moderators);
  }

  /*
    Test if a group is successfully deleted
   */
  @Test(expected = GroupDoesNotExistException.class)
  public void testDeleteGroup() {
    Group g = new Group();
    String name = generateString();
    g.setName(name);
    User m = new User(generateString());
    userService.addUser(m);
    List<User> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User(generateString());
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setMembers(users);
    userService.addUser(u);
    groupService.createGroup(g);
    Group group = groupService.getGroupByName(name);
    groupService.deleteGroup(group);

    groupService.getGroupByName(name);;
  }

  @Test
  public void testGetAllGroups(){
    Group g = new Group();
    g.setName(generateString());
    User m = new User(generateString());
    userService.addUser(m);
    List<User> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User(generateString());
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setMembers(users);
    userService.addUser(u);
    groupService.createGroup(g);
    List<Group> groupList = groupService.getAllGroups();
    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    Query query = manager.createNativeQuery("SELECT COUNT(*) FROM groups");
    int count = ((BigInteger) query.getSingleResult()).intValue();
    manager.close();
    assertEquals(count,groupList.size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNotifyGroup(){
    groupService.notifyGroup();
  }

  @Test
  public void testGetGroupByName(){
    Group g = new Group();
    g.setName(generateString());
    g.setCreatedBy("rahul");
    User m = new User(generateString());
    userService.addUser(m);
    List<User> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User(generateString());
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setMembers(users);
    userService.addUser(u);
    groupService.createGroup(g);
    Group group = groupService.getGroupByName(g.getName());
    assertEquals("rahul",group.getCreatedBy());

  }



  private String generateString() {
    int n = 8;
    {
      // chose a Character random from this String
      String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
              + "0123456789"
              + "abcdefghijklmnopqrstuvxyz";


      StringBuilder sb = new StringBuilder(n);

      for (int i = 0; i < n; i++) {

        int index = (int) (AlphaNumericString.length()
                * Math.random());

        // add Character one by one in end of sb
        sb.append(AlphaNumericString
                .charAt(index));
      }

      return sb.toString();
    }

  }
}
