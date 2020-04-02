
package com.neu.prattle.serviceTest;

import com.neu.prattle.exceptions.CannotRemoveModeratorException;
import com.neu.prattle.exceptions.CannotRemoveUserException;
import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.exceptions.GroupDoesNotExistException;
import com.neu.prattle.exceptions.UserAlreadyModeratorException;
import com.neu.prattle.exceptions.UserAlreadyPresentInGroupException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.GroupServiceImpl;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import javax.transaction.Transactional;

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
    group2 = new Group("Test", "This is a Test.", "TestTest", "Test", false);
    groupService = GroupServiceImpl.getInstance();
    userService = UserServiceImpl.getInstance();
    mockGroupService = GroupServiceImpl.getInstance();
    mockUserService = UserServiceImpl.getInstance();
    MockitoAnnotations.initMocks(this);


  }

  /*
    Test the creation of a group with name which already exists
   */

  @Test(expected = GroupAlreadyPresentException.class)
  public void testGroupCreationAlreadyExists() {
    String groupName = generateString();
    Group g = new Group(groupName);
    String userName = generateString();
    g.setCreatedBy(userName);
    g.setName(groupName);
    User u = new User(userName);
    u.setFirstName(generateString());
    userService.addUser(u);
    Group group1 = new Group(groupName);
    groupService.createGroup(g);
    group1.setCreatedBy(userName);
    groupService.createGroup(group1);
    assertFalse(false);
  }



  /*

  test for getting a group by name and successful creation of a group
   */

  @Test
  public void getGroupByName() {
    String groupName = generateString();
    String userName = generateString();
    User u = new User(userName);
    u.setFirstName("John");
    userService.addUser(u);
    Group g = new Group(groupName);
    g.setCreatedBy(userName);
    g.setName(groupName);
    groupService.createGroup(g);
    Group createdGroup = groupService.getGroupByName(groupName);
    assertEquals(createdGroup.getName(), groupName);
  }

   /*

  test for getting a group by name which does not exist
   */


  @Test(expected = GroupDoesNotExistException.class)
  public void getGroupByNameNotExisting() {
    Group createdGroup = groupService.getGroupByName("NoGroup");
  }


   /*
    Test to check if user is added to the group
   */


  @Test
  public void testAddUserToGroup() {
    String groupName = generateString();
    String userName = generateString();
    String userName1 = generateString();

    User user = new User(userName);
    user.setFirstName("rahulsinha");
    userService.addUser(user);

    Group g = new Group();
    g.setCreatedBy(userName);
    g.setName(groupName);
    groupService.createGroup(g);

    User u = new User(userName1);
    u.setFirstName(generateString());
    userService.addUser(u);
    groupService.addUser(g, u);
    Group group = groupService.getGroupByName(groupName);


    //verify(mockGroupService).addUser(g,u);

    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    Query query1 = manager.createNativeQuery("Select * from group_users where group_id= ?")
            .setParameter(1, group.getId());

    List list = query1.getResultList();
    manager.close();
    assertEquals(2, list.size());
  }

  /*
  Tests to verify that multiple users are added to the group
   */
  @Test
  public void testAddMultipleUserToGroup() {
    String groupName = generateString();
    String userName = generateString();
    Group g = new Group();
    g.setCreatedBy(userName);
    g.setName(groupName);
    User u = new User(userName);
    u.setFirstName(generateString());
    userService.addUser(u);
    groupService.createGroup(g);
    //Adding second user to group
    User user2 = new User(generateString());
    userService.addUser(user2);
    groupService.addUser(g, user2);
    Group group = groupService.getGroupByName(groupName);
    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    Query query1 = manager.createNativeQuery("Select * from group_users where group_id= ?")
            .setParameter(1, group.getId());
    List list = query1.getResultList();
    manager.close();
  }


  /*
    Try to add user which already exists in group
   */
  @Test(expected = UserAlreadyPresentInGroupException.class)
  public void testAddUserWhichAlreadyExistsInGroup() {
    String groupName = generateString();
    String userName = generateString();
    User user = new User(userName);
    user.setFirstName("rahulsinha");
    userService.addUser(user);
    Group g = new Group();
    g.setCreatedBy(userName);
    g.setName(groupName);
    groupService.createGroup(g);

    // User already exists since he created the group
    groupService.addUser(g, user);

  }


  /*
   Test to check if user is removed to the group
  */
  @Test
  public void testRemoveUserFromGroup() {
    String groupName = generateString();
    String userName = generateString();
    User u = new User(userName);
    userService.addUser(u);

    Group g = new Group();
    g.setCreatedBy(userName);
    g.setName(groupName);

    u.setFirstName(generateString());

    groupService.createGroup(g);


    //Adding second user to group
    User user2 = new User(generateString());
    userService.addUser(user2);

    groupService.addUser(g, user2);

    //TRemoving second user
    groupService.removeUser(g, user2);
    Group groupObj = groupService.getGroupByName(groupName);

    assertEquals(1, groupObj.getMembers().size());

  }


  @Test(expected = CannotRemoveUserException.class)
  public void testRemoveUserFromGroupNotExist() {
    String groupName = generateString();
    String userName = generateString();
    User u = new User(userName);
    userService.addUser(u);

    Group g = new Group();
    g.setCreatedBy(userName);
    g.setName(groupName);

    u.setFirstName(generateString());

    groupService.createGroup(g);


    //Adding second user to group
    User user2 = new User(generateString());
    //userService.addUser(user2);

    //groupService.addUser(g, user2);

    //TRemoving second user
    groupService.removeUser(g, user2);
    Group groupObj = groupService.getGroupByName(groupName);

    assertEquals(1, groupObj.getMembers().size());

  }



  /*
   Test to check moderator cannot be removed from group
  */
  @Test(expected = CannotRemoveUserException.class)
  public void testRemoveUserFromGroupWhoIsModerator() {
    String groupName = generateString();
    String userName = generateString();
    User u = new User(userName);
    userService.addUser(u);

    Group g = new Group();
    g.setCreatedBy(userName);
    g.setName(groupName);
    groupService.createGroup(g);

    //Trying to remove moderator
    groupService.removeUser(g, u);
  }


  /*
    Verify adding additional moderator to a group
   */
  @Test
  public void testAddModeratorToGroup() {
    String groupName = generateString();
    String creatorName = generateString();
    User creator = new User(creatorName);
    userService.addUser(creator);
    Group g = new Group();
    g.setName(groupName);
    g.setCreatedBy(creatorName);

    groupService.createGroup(g);


    String modName = generateString();
    User mod = new User(modName);
    userService.addUser(mod);
    groupService.addModerator(g, mod);

    Group group = groupService.getGroupByName(groupName);

    assertEquals(2, group.getModerators().size());
    assertEquals(2, group.getMembers().size());
  }

  /*
    Test adding same moderator twice to a group
   */
  @Test(expected = UserAlreadyModeratorException.class)
  public void testAddModeratorToGroupWhoIsAlreadyModerator() {
    String groupName = generateString();
    String creatorName = generateString();
    User creator = new User(creatorName);
    userService.addUser(creator);
    Group g = new Group();
    g.setName(groupName);
    g.setCreatedBy(creatorName);

    groupService.createGroup(g);

    groupService.addModerator(g, creator);

  }


  /*
   Test to check if moderator is removed from the group
  */
  @Test
  public void testRemoveModeratorFromGroup() {
    String groupName = generateString();
    String creatorName = generateString();
    User creator = new User(creatorName);
    userService.addUser(creator);
    Group g = new Group();
    g.setName(groupName);
    g.setCreatedBy(creatorName);

    groupService.createGroup(g);


    String modName = generateString();
    User mod = new User(modName);
    userService.addUser(mod);
    groupService.addModerator(g, mod);

    Group group = groupService.getGroupByName(groupName);

    assertEquals(2, group.getModerators().size());
    assertEquals(2, group.getMembers().size());

    groupService.removeModerator(group, mod);
    assertEquals(1, group.getModerators().size());
    assertEquals(2, group.getMembers().size());
  }

  /*
   Test to verify that only moderator cannot removed from the group
  */
  @Test(expected = CannotRemoveModeratorException.class)
  public void testRemoveOnlyModeratorFromGroup() {
    String groupName = generateString();
    String creatorName = generateString();
    User creator = new User(creatorName);
    userService.addUser(creator);
    Group g = new Group();
    g.setName(groupName);
    g.setCreatedBy(creatorName);

    groupService.createGroup(g);

    groupService.removeModerator(g, creator);
  }


  /*
   Test to verify that user who is not a moderator cannot be removed as moderator
  */
  @Test(expected = CannotRemoveModeratorException.class)
  public void testRemoveModeratorNotExistsFromGroup() {
    String groupName = generateString();
    String creatorName = generateString();
    User creator = new User(creatorName);
    userService.addUser(creator);
    Group g = new Group();
    g.setName(groupName);
    g.setCreatedBy(creatorName);

    groupService.createGroup(g);
    User testUser = new User(generateString());
    userService.addUser(testUser);


    groupService.removeModerator(g, testUser);
  }


  /*
    Test if a group is successfully deleted
   */
  @Test
  public void testDeleteGroup() {
    String username = generateString();
    User m = new User(username);
    userService.addUser(m);

    Group g = new Group();
    String name = generateString();
    g.setName(name);
    g.setCreatedBy(username);


    groupService.createGroup(g);
    Group group = groupService.getGroupByName(name);
    assertEquals(true, groupService.getAllGroups().contains(group));
    groupService.deleteGroup(name);
    assertEquals(false, groupService.getAllGroups().contains(group));

  }


  @Test
  public void testDeleteGrouptest() {

    Group group = groupService.getGroupByName("grouptest47");
    assertEquals(true, groupService.getAllGroups().contains(group));
    groupService.deleteGroup("grouptest47");
    assertEquals(false, groupService.getAllGroups().contains(group));

  }


  /*
    Test if a group is successfully deleted
   */
  @Test(expected = GroupDoesNotExistException.class)
  public void testDeleteGroupDoesNotExist() {
    String username = generateString();
    User m = new User(username);
    userService.addUser(m);

    Group g = new Group();
    String name = generateString();
    g.setName(name);
    g.setCreatedBy(username);


    //groupService.createGroup(g);
    //Group group = groupService.getGroupByName(name);
    //assertEquals(true,groupService.getAllGroups().contains(group));
    groupService.deleteGroup(g.getName());
    //assertEquals(false,groupService.getAllGroups().contains(group));

  }


  /*
  Updating a group. Adding a description and verifying its been updated
   */
  @Test
  public void testUpdateGroup() {
    String groupName = generateString();
    String moderator = "Moderator" + generateString();
    User m = new User(moderator);
    userService.addUser(m);
    String username = "User" + generateString();
    User u = new User(username);
    userService.addUser(u);

    Group g = new Group();
    g.setName(groupName);
    g.setCreatedBy(moderator);
    g.setMembers(u);

    g.setDescription("No description provided");
    groupService.createGroup(g);
    g.setDescription("This is a test group");
    groupService.updateGroup(g);
    assertEquals("This is a test group", groupService.getGroupByName(groupName).getDescription());
  }


  /*
  Updating a group which does not exist
  */
  @Test(expected = GroupDoesNotExistException.class)
  public void updateGroupNotExisting() {
    Group group = new Group(generateString());
    groupService.updateGroup(group);
  }


  private void setMocksForGroupCreation() {
    User u = new User(generateString());
    u.setFirstName(generateString());
    userService.addUser(u);
    User m = new User(TESTMODERATOR + generateString());
    m.setFirstName(generateString());
    userService.addUser(m);
    //List<User> users = new ArrayList<>();
    //users.add(u);
    //List<User> moderators = new ArrayList<>();
    //moderators.add(m);
    group.setMembers(u);
    group.setName(generateString());
    group.setModerators(m);
  }


  /*
    Test to get all groups in the system
     */
  @Test
  public void testGetAllGroups() {
    String username = generateString();
    User m = new User(username);
    userService.addUser(m);

    Group g = new Group();
    g.setName(generateString());
    g.setCreatedBy(username);
    groupService.createGroup(g);

    Group g1 = new Group();
    g1.setName(generateString());
    g1.setCreatedBy(username);
    groupService.createGroup(g1);


    List<Group> groupList = groupService.getAllGroups();

    EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
    Query query = manager.createNativeQuery("SELECT COUNT(*) FROM groups");
    int count = ((BigInteger) query.getSingleResult()).intValue();
    manager.close();
    assertEquals(count, groupList.size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNotifyGroup() {
    groupService.notifyGroup();
  }


  /*
    Test to retrieve group details by group name
   */
  @Test
  public void testGetGroupByName() {

    String username = generateString();
    User m = new User(generateString());
    userService.addUser(m);

    Group g = new Group();
    g.setName(generateString());
    g.setCreatedBy(username);
    g.setDescription("test group");
    groupService.createGroup(g);

    Group group = groupService.getGroupByName(g.getName());

    assertEquals(username, group.getCreatedBy());
    assertEquals("test group", group.getDescription());

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
