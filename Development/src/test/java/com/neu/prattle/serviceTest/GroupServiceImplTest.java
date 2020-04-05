
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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceImplTest {

  private GroupService groupService = GroupServiceImpl.getInstance();

  @Mock
  private UserService mockUserService;

  @Mock
  private EntityManager mockEntityManager;

  @Mock
  private EntityTransaction mockTransaction;

  @Mock
  private TypedQuery<Group> query;

  @Mock
  private TypedQuery<Long> queryLong;

  private static final String SELECT_QUERY = "SELECT count(g) FROM Group g WHERE g.name = :name";

  private static final String SELECT_GROUP_QUERY = "SELECT g FROM Group g WHERE g.name = :name";

  @Before
  public void setUp() throws ReflectiveOperationException {
    Field f1 = groupService.getClass().getDeclaredField("userService");
    f1.setAccessible(true);
    f1.set(groupService, mockUserService);

    UserServiceImplTest.setFinalStaticField(GroupServiceImpl.class,"manager",mockEntityManager);
  }

  /**
   * Test the creation of a group with name which already exists
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
    doNothing().when(mockUserService).addUser(u);

    //Record Exists Mock
    setMocksForIsRecordExists(1L);

    groupService.createGroup(g);
  }

  /**
   * Test for getting a group by name and successful creation of a group
   */
  @Test
  public void getGroupByName() {
    String groupName = generateString();
    String userName = generateString();
    User u = new User(userName);
    u.setFirstName("John");
    doNothing().when(mockUserService).addUser(u);
    Group g = new Group(groupName);
    g.setCreatedBy(userName);
    g.setName(groupName);

    //Record Exists Mock
    setMocksForIsRecordExists(1L);

    //Return group Mock
    when(mockEntityManager.createQuery(SELECT_GROUP_QUERY,Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(g);

    Group createdGroup = groupService.getGroupByName(groupName);
    assertEquals(createdGroup.getName(), groupName);
  }

  /**
   * Test for getting a group by name which does not exist
   */
  @Test(expected = GroupDoesNotExistException.class)
  public void getGroupByNameNotExisting() {
    //Record Not Exists Mock
    setMocksForIsRecordExists(0L);
    Group createdGroup = groupService.getGroupByName("NoGroup");
  }

  /**
   * Test to check if user is added to the group
   */
  @Test
  public void testAddUserToGroup() {
    String groupName = generateString();
    String userName = generateString();
    String userName1 = generateString();

    User user = new User(userName);
    user.setFirstName("rahulsinha");

    Group g = new Group();
    g.setCreatedBy(userName);
    g.setName(groupName);

    User u = new User(userName1);
    u.setFirstName(generateString());
    g.setMembers(user);
    g.setModerators(user);

    //Record Exists Mock
    setMocksForIsRecordExists(1L);

    //Return group Mock
    when(mockEntityManager.createQuery(SELECT_GROUP_QUERY,Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(g);

    setMocksForEntityPersist();

    groupService.addUser(g, u);

    Group group = groupService.getGroupByName(groupName);
    assertEquals(2,group.getMembers().size());
  }

  /**
   * Try to add user which already exists in group
   */
  @Test(expected = UserAlreadyPresentInGroupException.class)
  public void testAddUserWhichAlreadyExistsInGroup() {
    String groupName = generateString();
    String userName = generateString();
    User user = new User(userName);
    user.setFirstName("rahulsinha");
    Group g = new Group();
    g.setCreatedBy(userName);
    g.setName(groupName);
    g.setMembers(user);
    g.setModerators(user);
    //Record Exists Mock
    setMocksForIsRecordExists(1L);
    //Return group Mock
    when(mockEntityManager.createQuery(SELECT_GROUP_QUERY,Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(g);

    setMocksForEntityPersist();
    // User already exists since he created the group
    groupService.addUser(g, user);
  }

  /**
   * Test to check if user is removed to the group
   */
  @Test
  public void testRemoveUserFromGroup() {
    String groupName = generateString();
    String userName = generateString();
    User u = new User(userName);
    Group g = new Group();
    g.setCreatedBy(userName);
    g.setName(groupName);
    u.setFirstName(generateString());
    g.setMembers(u);
    g.setModerators(u);

    //Adding second user to group
    String username2 = generateString();
    User user2 = new User(username2);
    g.setMembers(user2);

    //Record Exists Mock
    setMocksForIsRecordExists(1L);

    //Return group Mock
    when(mockEntityManager.createQuery(SELECT_GROUP_QUERY,Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(g);

    setMocksForEntityPersist();

    Group groupObj;
    groupObj = groupService.getGroupByName(groupName);
    assertEquals(2,groupObj.getMembers().size());

    when(mockUserService.findUserByUsername(username2)).thenReturn(user2);
    //TRemoving second user
    groupService.removeUser(g, user2);
    groupObj = groupService.getGroupByName(groupName);

    assertEquals(1,groupObj.getMembers().size());
  }

  @Test(expected = CannotRemoveUserException.class)
  public void testRemoveUserFromGroupNotExist() {
    String groupName = generateString();
    String userName = generateString();
    User u = new User(userName);

    Group g = new Group();
    g.setCreatedBy(userName);
    g.setName(groupName);
    g.setModerators(u);
    g.setMembers(u);
    u.setFirstName(generateString());

    //Adding second user to group
    String username2 = generateString();
    User user2 = new User(username2);

    //Record Exists Mock
    setMocksForIsRecordExists(1L);

    //Return group Mock
    when(mockEntityManager.createQuery(SELECT_GROUP_QUERY,Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(g);

    setMocksForEntityPersist();

    Group groupObj;
    groupObj = groupService.getGroupByName(groupName);
    assertEquals(1,groupObj.getMembers().size());

    //TRemoving second user
    groupService.removeUser(g, user2);
  }

  /**
   * Test to check moderator cannot be removed from group
   */
  @Test(expected = CannotRemoveUserException.class)
  public void testRemoveUserFromGroupWhoIsModerator() {
    String groupName = generateString();
    String userName = generateString();
    User u = new User(userName);

    Group g = new Group();
    g.setCreatedBy(userName);
    g.setName(groupName);
    g.setMembers(u);
    g.setModerators(u);

    //Record Exists Mock
    setMocksForIsRecordExists(1L);

    //Return group Mock
    when(mockEntityManager.createQuery(SELECT_GROUP_QUERY,Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(g);

    setMocksForEntityPersist();

    Group groupObj;
    groupObj = groupService.getGroupByName(groupName);
    assertEquals(1,groupObj.getMembers().size());

    when(mockUserService.findUserByUsername(userName)).thenReturn(u);
    //Trying to remove moderator
    groupService.removeUser(g, u);
  }

  /**
   * Verify adding additional moderator to a group
   */
  @Test
  public void testAddModeratorToGroup() {
    String groupName = generateString();
    String creatorName = generateString();
    User creator = new User(creatorName);
    Group g = new Group();
    g.setName(groupName);
    g.setCreatedBy(creatorName);
    g.setMembers(creator);
    g.setModerators(creator);

    String modName = generateString();
    User mod = new User(modName);

    //Record Exists Mock
    setMocksForIsRecordExists(1L);

    //Return group Mock
    when(mockEntityManager.createQuery(SELECT_GROUP_QUERY,Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(g);

    setMocksForEntityPersist();

    groupService.addModerator(g, mod);

    Group group = groupService.getGroupByName(groupName);

    assertEquals(2, group.getModerators().size());
    assertEquals(2, group.getMembers().size());
  }

  /**
   * Test adding same moderator twice to a group
   */
  @Test(expected = UserAlreadyModeratorException.class)
  public void testAddModeratorToGroupWhoIsAlreadyModerator() {
    String groupName = generateString();
    String creatorName = generateString();
    User creator = new User(creatorName);
    Group g = new Group();
    g.setName(groupName);
    g.setCreatedBy(creatorName);
    g.setMembers(creator);
    g.setModerators(creator);

    //Record Exists Mock
    setMocksForIsRecordExists(1L);

    //Return group Mock
    when(mockEntityManager.createQuery(SELECT_GROUP_QUERY,Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(g);

    setMocksForEntityPersist();

    groupService.addModerator(g, creator);
  }

  /**
   * Test to remove moderator from group
   */
  @Test
  public void testRemoveModeratorFromGroup() {
    String groupName = generateString();
    String creatorName = generateString();
    User creator = new User(creatorName);
    Group g = new Group();
    g.setName(groupName);
    g.setCreatedBy(creatorName);
    g.setMembers(creator);
    g.setModerators(creator);

    String modName = generateString();
    User mod = new User(modName);

    g.setMembers(mod);
    g.setModerators(mod);

    //Record Exists Mock
    setMocksForIsRecordExists(1L);

    //Return group Mock
    when(mockEntityManager.createQuery(SELECT_GROUP_QUERY,Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(g);

    setMocksForEntityPersist();

    Group group = groupService.getGroupByName(groupName);

    assertEquals(2, group.getModerators().size());
    assertEquals(2, group.getMembers().size());

    when(mockUserService.findUserByUsername(modName)).thenReturn(mod);
    groupService.removeModerator(group, mod);
    assertEquals(1, group.getModerators().size());
    assertEquals(2, group.getMembers().size());
  }

  /**
   * Test to verify that only moderator cannot removed from the group
   */
  @Test(expected = CannotRemoveModeratorException.class)
  public void testRemoveOnlyModeratorFromGroup() {
    String groupName = generateString();
    String creatorName = generateString();
    User creator = new User(creatorName);
    Group g = new Group();
    g.setName(groupName);
    g.setCreatedBy(creatorName);
    g.setMembers(creator);
    g.setModerators(creator);

    //Record Exists Mock
    setMocksForIsRecordExists(1L);

    //Return group Mock
    when(mockEntityManager.createQuery(SELECT_GROUP_QUERY,Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(g);

    setMocksForEntityPersist();

    Group group = groupService.getGroupByName(groupName);

    assertEquals(1, group.getModerators().size());
    assertEquals(1, group.getMembers().size());

    when(mockUserService.findUserByUsername(creatorName)).thenReturn(creator);
    groupService.removeModerator(g, creator);
  }

  /**
   * Test to verify that user who is not a moderator cannot be removed as moderator
   */
  @Test(expected = CannotRemoveModeratorException.class)
  public void testRemoveModeratorNotExistsFromGroup() {
    String groupName = generateString();
    String creatorName = generateString();
    User creator = new User(creatorName);
    Group g = new Group();
    g.setName(groupName);
    g.setCreatedBy(creatorName);
    g.setMembers(creator);
    g.setModerators(creator);

    String testUserName = generateString();
    User testUser = new User(testUserName);

    //Record Exists Mock
    setMocksForIsRecordExists(1L);

    //Return group Mock
    when(mockEntityManager.createQuery(SELECT_GROUP_QUERY,Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(g);

    setMocksForEntityPersist();
    when(mockUserService.findUserByUsername(testUserName)).thenReturn(testUser);
    groupService.removeModerator(g, testUser);
  }

  /**
   * Test if a group is successfully deleted
   */
  @Test
  public void testDeleteGroup() {
    String username = generateString();
    User m = new User(username);

    Group g = new Group();
    String name = generateString();
    g.setName(name);
    g.setCreatedBy(username);
    g.setMembers(m);
    g.setModerators(m);

    List<Group> groupList = new ArrayList<>();
    groupList.add(g);
    //Record Exists Mock
    setMocksForIsRecordExists(1L);

    //Return group Mock
    when(mockEntityManager.createQuery(SELECT_GROUP_QUERY,Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(g);

    when(mockEntityManager.createQuery("SELECT g FROM Group g",Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getResultList()).thenReturn(groupList);

    setMocksForEntityPersist();

    doNothing().when(mockEntityManager).persist(g);

    Group group = groupService.getGroupByName(name);
    assertEquals(true, groupService.getAllGroups().contains(group));
    groupService.deleteGroup(name);
    when(mockEntityManager.createQuery("SELECT g FROM Group g",Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getResultList()).thenReturn(new ArrayList<>());
    assertEquals(false, groupService.getAllGroups().contains(group));
  }

  /**
   * Updating a group. Adding a description and verifying its been updated
   */
  @Test
  public void testUpdateGroup() {
    String groupName = generateString();
    String moderator = "Moderator" + generateString();
    User m = new User(moderator);
    String username = "User" + generateString();
    User u = new User(username);

    Group g = new Group();
    g.setName(groupName);
    g.setCreatedBy(moderator);
    g.setMembers(u);
    g.setMembers(m);
    g.setModerators(m);
    g.setDescription("No description provided");

    //Record Exists Mock
    setMocksForIsRecordExists(1L);

    //Return group Mock
    when(mockEntityManager.createQuery(SELECT_GROUP_QUERY,Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(g);

    setMocksForEntityPersist();

    Group group;
    group = groupService.getGroupByName(groupName);

    assertEquals("No description provided",group.getDescription());
    g.setDescription("This is a test group");
    groupService.updateGroup(g);
    group = groupService.getGroupByName(groupName);
    assertEquals("This is a test group", group.getDescription());
  }

  /**
   * Updating a group which does not exist
   */
  @Test(expected = GroupDoesNotExistException.class)
  public void updateGroupNotExisting() {
    Group group = new Group(generateString());
    setMocksForIsRecordExists(0L);
    groupService.updateGroup(group);
  }

  /**
   * Test to get all groups in the system
   */
  @Test
  public void testGetAllGroups() {
    String username = generateString();
    User m = new User(username);

    Group g = new Group();
    g.setName(generateString());
    g.setCreatedBy(username);

    Group g1 = new Group();
    g1.setName(generateString());
    g1.setCreatedBy(username);

    List<Group> groupList = new ArrayList<>();
    groupList.add(g);
    groupList.add(g1);
    //Record Exists Mock
    setMocksForIsRecordExists(1L);

    //Return group Mock
    when(mockEntityManager.createQuery(SELECT_GROUP_QUERY,Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(g);

    when(mockEntityManager.createQuery("SELECT g FROM Group g",Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getResultList()).thenReturn(groupList);

    List<Group> resultGroupList = groupService.getAllGroups();

    assertEquals(resultGroupList.size(), groupList.size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNotifyGroup() {
    groupService.notifyGroup();
  }

  @Test(expected = GroupDoesNotExistException.class)
  public void testDeleteGroupDoesNotExist() {
    String groupName = generateString();
    String username = generateString();
    User m = new User(username);

    Group g = new Group();
    g.setName(groupName);
    g.setCreatedBy(username);

    setMocksForIsRecordExists(0L);

    groupService.deleteGroup(groupName);
  }

  @Test
  public void testGetAllGroupsByUsername() {
    String groupName = generateString();
    String username = generateString();
    User m = new User(username);

    Group g = new Group();
    g.setName(groupName);
    g.setCreatedBy(username);
    g.setModerators(m);
    g.setMembers(m);
    List<Group> groupList = new ArrayList<>();
    groupList.add(g);
    when(mockUserService.findGroupsByName(username)).thenReturn(groupList);
    List groups = groupService.getAllGroupsByUsername(username);
    assertEquals(groupList.size(),groups.size());
  }

  @Test
  public void testSuccessfulCreateGroup() {
    String groupName = generateString();
    String userName = generateString();
    User u = new User(userName);

    Group g = new Group();
    g.setCreatedBy(userName);
    g.setName(groupName);
    g.setMembers(u);
    g.setModerators(u);

    //Record Exists Mock
    setMocksForIsRecordExists(0L);

    //Return group Mock
    when(mockEntityManager.createQuery(SELECT_GROUP_QUERY,Group.class)).thenReturn(query);
    when(query.setParameter(anyString(),anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(g);

    when(mockUserService.findUserByName(userName)).thenReturn(Optional.of(u));
    setMocksForEntityPersist();

    Group groupObj;
    //Trying to create group
    groupService.createGroup(g);

    setMocksForIsRecordExists(1L);

    groupObj = groupService.getGroupByName(groupName);
    assertEquals(groupObj.getCreatedBy(),userName);
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

  private void setMocksForIsRecordExists(Long count) {
    when(mockEntityManager.createQuery(SELECT_QUERY, Long.class)).thenReturn(queryLong);
    when(queryLong.setParameter(anyString(), anyString())).thenReturn(queryLong);
    when(queryLong.getSingleResult()).thenReturn(count);
  }

  private void setMocksForEntityPersist() {
    Group group = new Group("Test", "This is a Test.", "TestTest", "Test", false);
    when(mockEntityManager.getTransaction()).thenReturn(mockTransaction);
    doNothing().when(mockTransaction).begin();
    doNothing().when(mockEntityManager).persist(group);
    doNothing().when(mockTransaction).commit();
  }
}
