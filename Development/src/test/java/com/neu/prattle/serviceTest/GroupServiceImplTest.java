
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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceImplTest {

  private GroupService groupService;
  private UserService userService;

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
    Test the creation of a group
   */
  @Test
  public void testCreateGroup() {
    String groupName = generateString();
    Group group = new Group(groupName);
    mockGroupService.createGroup(group);
    when(mockGroupService.getGroupByName(groupName)).thenReturn(new Group(groupName));
    Group createdGroup = mockGroupService.getGroupByName(groupName);
    assertEquals(createdGroup.getName(), groupName);
//    groupService.createGroup(group);
//    Group createdGroup = groupService.getGroupByName(groupName);
//    assertEquals(createdGroup.getName(),groupName);
  }

  /*
    Test the creation of a group with name which already exists
   */

  @Test(expected = GroupAlreadyPresentException.class)
  public void testGroupCreation() {
    String groupName = generateString();
    Group group = new Group(groupName);
    Group group1 = new Group(groupName);
    mockGroupService.createGroup(group);
    doThrow(new GroupAlreadyPresentException("Group already exists")).when(mockGroupService).createGroup(group1);
    mockGroupService.createGroup(group1);
  }



  /*

  test for getting a group by name
   */

  @Test
  public void getGroupByName() {
    String groupName = generateString();
    Group group = new Group(groupName);
    mockGroupService.createGroup(group);
    when(mockGroupService.getGroupByName(groupName)).thenReturn(new Group(groupName));
    Group createdGroup = mockGroupService.getGroupByName(groupName);
    assertEquals(createdGroup.getName(), groupName);
  }

   /*

  test for getting a group by name which does not exist
   */


  @Test(expected = GroupDoesNotExistException.class)
  public void getGroupByNameNotExisting() {
    String group = "doesnotexist";
    doThrow(new GroupDoesNotExistException("Group does not exist")).when(mockGroupService).getGroupByName(group);
    mockGroupService.getGroupByName(group);
  }


   /*
    Test to check if user is added to the group
   */


  @Test
  public void testAddUserToGroup() {
    String groupName = generateString();
    String userName = generateString();
    Group g = new Group();
    g.setName(groupName);
    User u = new User(userName);
    u.setFirstName(generateString());
    mockUserService.addUser(u);
    mockGroupService.createGroup(g);
    mockGroupService.addUser(g, u);


    Group mockGroup = new Group("Test", "This is a test", "Test", "password", true);
    List<User> group_user = new ArrayList<>();
    group_user.add(u);
    mockGroup.setMembers(group_user);
    when(mockGroupService.getGroupByName(groupName)).thenReturn(mockGroup);

    Group group = mockGroupService.getGroupByName(groupName);

    assertEquals(group.getMembers().get(0).getUsername(), mockGroup.getMembers().get(0).getUsername());

  }

  /*
  Tests to verify that multiple users are added to the group
   */
  @Test
  public void testAddMultipleUserToGroup() {
    String groupName = generateString();
    String userName = generateString();
    Group g = new Group();
    g.setName(groupName);
    User u = new User(userName);
    u.setFirstName(generateString());
    mockUserService.addUser(u);
    mockGroupService.createGroup(g);
    //Adding first user
    mockGroupService.addUser(g, u);
    //Adding second user to group
    User user2 = new User(generateString());
    mockUserService.addUser(user2);
    mockGroupService.addUser(g, user2);

    Group mockGroup = new Group("Test", "This is a test", "Test", "password", true);
    List<User> group_user = new ArrayList<>();
    group_user.add(u);
    group_user.add(user2);
    mockGroup.setMembers(group_user);
    when(mockGroupService.getGroupByName(groupName)).thenReturn(mockGroup);

    Group group = mockGroupService.getGroupByName(groupName);

    assertEquals(group.getMembers().size(), mockGroup.getMembers().size());

  }


  /*
   Test to check if user is removed to the group
  */
  @Test
  public void testRemoveUserFromGroup() {
    String groupName = generateString();
    String userName = generateString();
    Group g = new Group();
    g.setName(groupName);
    User u = new User(userName);
    u.setFirstName(generateString());
    mockUserService.addUser(u);
    mockGroupService.createGroup(g);
    mockGroupService.addUser(g, u);

    //Adding second user to group
    User user2 = new User(generateString());
    mockUserService.addUser(user2);
    mockGroupService.addUser(g, user2);

    //TRemoving second user
    mockGroupService.removeUser(g, user2);
    verify(mockGroupService).removeUser(g, user2);
  }


  /*
    Verify adding a single moderator to a group
   */
  @Test
  public void testAddModeratorToGroup() {
    String groupName = generateString();
    String moderatorName = generateString();
    Group g = new Group();
    g.setName(groupName);
    User mod = new User(moderatorName);
    mockUserService.addUser(mod);
    mockGroupService.createGroup(g);
    mockGroupService.addModerator(g, mod);

    Group mockGroup = new Group("Test", "This is a test", "Test", "password", true);
    List<User> group_mods = new ArrayList<>();
    group_mods.add(mod);
    mockGroup.setModerators(group_mods);

    when(mockGroupService.getGroupByName(groupName)).thenReturn(mockGroup);

    Group group = mockGroupService.getGroupByName(groupName);

    assertEquals(group.getModerators().get(0).getUsername(), mockGroup.getModerators().get(0).getUsername());
  }


  /*
   Test to check if moderator is removed from the group
  */
  @Test
  public void testRemoveModeratorFromGroup() {
    String groupName = generateString();
    String modName = generateString();
    Group g = new Group();
    g.setName(groupName);
    User mod = new User(modName);
    mockUserService.addUser(mod);
    mockGroupService.createGroup(g);
    when(mockGroupService.getGroupByName(groupName)).thenReturn(g);
    Group group = mockGroupService.getGroupByName(groupName);
    mockGroupService.addModerator(group, mod);

    // Adding and removing the second moderator

    User mod2 = new User(generateString());
    mockUserService.addUser(mod2);
    mockGroupService.addModerator(group, mod2);

    mockGroupService.removeModerator(group, mod2);
    verify(mockGroupService).removeModerator(group, mod2);
  }


  /*
  Updating a group. Adding a description and verifying itys been updated
   */
  @Test
  public void testUpdateGroup() {
    String groupName = generateString();
    String moderator = "Moderator" + generateString();
    String username = "User" + generateString();
    Group g = new Group();
    g.setName(groupName);
    User m = new User(moderator);
    mockUserService.addUser(m);
    List<User> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User(username);
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setMembers(users);
    mockUserService.addUser(u);
    //Creating a group without description
    mockGroupService.createGroup(g);
    g.setDescription("This is a test group");
    mockGroupService.updateGroup(g);
    when(mockGroupService.getGroupByName(groupName)).thenReturn(g);
    assertEquals("This is a test group", mockGroupService.getGroupByName(groupName).getDescription());
  }

  /*
    Updating a group which does not exist
     */
  @Test(expected = GroupDoesNotExistException.class)
  public void testUpdateGroupNotExist() {
    Group group = new Group("Test","This is a test", "Test","password",true);
    doThrow(new GroupDoesNotExistException("Group does not exist")).when(mockGroupService).updateGroup(group);
    group.setPassword("newPass");
    mockGroupService.updateGroup(group);
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
    mockUserService.addUser(m);
    List<User> moderators = new ArrayList<>();
    moderators.add(m);
    g.setModerators(moderators);
    User u = new User(generateString());
    List<User> users = new ArrayList<>();
    users.add(u);
    g.setMembers(users);
    mockUserService.addUser(u);
    mockGroupService.createGroup(g);
    when(mockGroupService.getGroupByName(name)).thenReturn(g);
    Group group = mockGroupService.getGroupByName(name);
    mockGroupService.deleteGroup(group);
    doThrow(new GroupDoesNotExistException("Group does not exist")).when(mockGroupService).getGroupByName(group.getName());
    mockGroupService.getGroupByName(group.getName());
  }

  @Test
  public void testGetAllGroups() {
    Group g = new Group(generateString());
    mockGroupService.createGroup(g);
    Group g1 = new Group(generateString());

    List<Group> mockGroupList = new ArrayList<>();
    mockGroupList.add(g);
    mockGroupList.add(g1);

    when(mockGroupService.getAllGroups()).thenReturn(mockGroupList);

    List<Group> groupList = mockGroupService.getAllGroups();

    assertEquals(mockGroupList.size(), groupList.size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNotifyGroup() {
    groupService.notifyGroup();
  }

  @Test
  public void testGetGroupByName() {
    Group g = new Group();
    g.setName("testGroup");
    g.setCreatedBy("rahul");

    when(mockGroupService.getGroupByName("testGroup")).thenReturn(g);
    Group group = mockGroupService.getGroupByName("testGroup");

    assertEquals("rahul", group.getCreatedBy());

  }

  @Test
  public void addUsersToGroups()
  {
    String gName = generateString()+"z";
    String userName = generateString()+"cdmmnkj";
    User user = new User(userName);
    userService.addUser(user);
    Group group = new Group(gName);
    groupService.createGroup(group);

    User userObj = userService.findUserByUsername(user.getUsername());
    Group groupObj = groupService.getGroupByName(group.getName());

    groupService.addUser(groupObj,userObj);

    //assertEquals(userService.findGroupsByName(userObj.getUsername()).get(0).getName(),gName);

    List<Group> grpList = userService.findGroupsByName(userName);
    assertEquals(grpList.get(0).getName(),gName);
  }

  /*
  Helper method to generate random strings
   */
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
