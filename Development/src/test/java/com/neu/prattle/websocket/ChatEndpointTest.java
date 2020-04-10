package com.neu.prattle.websocket;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.service.GroupService;
import com.neu.prattle.service.UserService;
import com.neu.prattle.serviceTest.UserServiceImplTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChatEndpointTest {

  @Mock
  private Session session = mock(Session.class, RETURNS_DEEP_STUBS);

  @Mock
  private UserService userService;

  @Mock
  private GroupService groupService;

  @Mock
  private EntityManager manager;

  @Mock
  private Query query;

  @Mock
  private EntityTransaction transaction;

  @Mock
  private static HashMap<String, String> users;

  private String username = "username";

  private String groupname = "groupname";

  private User user = new User("firstname","lastname","username","password","America/New_York");

  private User user2 = new User("firstname","lastname","username2","password","America/New_York");

  private Group group = new Group("groupname","groupDescription","username","groupPassword",false);

  private ChatEndpoint chatEndpoint = new ChatEndpoint();

  private RemoteEndpoint.Basic basic = mock(RemoteEndpoint.Basic.class);

  private Message message;

  @Before
  public void setUp() throws ReflectiveOperationException {
    Field f1 = chatEndpoint.getClass().getDeclaredField("accountService");
    f1.setAccessible(true);
    f1.set(chatEndpoint, userService);

    Field f2 = chatEndpoint.getClass().getDeclaredField("groupService");
    f2.setAccessible(true);
    f2.set(chatEndpoint, groupService);

    group.setMembers(user);
    group.setMembers(user2);
    UserServiceImplTest.setFinalStaticField(ChatEndpoint.class, "manager", manager);
    persistMessage();
  }

  @Test
  public void testOnOpenUserPresent() throws IOException, EncodeException {
    when(userService.findUserByName(username)).thenReturn(Optional.of(user));
    when(session.getBasicRemote()).thenReturn(basic);
    doNothing().when(basic).sendObject(any());
    chatEndpoint.onOpen(session,username);
  }

  @Test
  public void testOnOpenUserNotPresent() throws IOException, EncodeException {
    when(userService.findUserByName(username)).thenReturn(Optional.ofNullable(null));
    when(session.getBasicRemote()).thenReturn(basic);
    doNothing().when(basic).sendObject(any());
    chatEndpoint.onOpen(session,username);
  }

  @Test
  public void testOnClose() throws IOException, EncodeException{
    when(userService.findUserByName(username)).thenReturn(Optional.of(user));
    when(session.getBasicRemote()).thenReturn(basic);
    doNothing().when(basic).sendObject(any());
    chatEndpoint.onOpen(session,username);
    chatEndpoint.onClose(session);
  }

  @Test
  public void testOnMessageBroadcastUserPresent() throws IOException, EncodeException {
    message = new Message();
    message.setFrom(username);
    message.setContent("What is the time?");
    message.setTimestamp(username + ":1234567890");
    when(userService.findUserByName(username)).thenReturn(Optional.of(user));
    when(session.getBasicRemote()).thenReturn(basic);
    when(session.getId()).thenReturn("1234");
    doNothing().when(basic).sendObject(any());
    chatEndpoint.onOpen(session,username);
    chatEndpoint.onMessage(session,message);
  }

  @Test
  public void testOnMessageDirect() throws IOException, EncodeException {
    when(userService.findUserByName(username)).thenReturn(Optional.of(user));
    when(session.getBasicRemote()).thenReturn(basic);
    when(session.getId()).thenReturn("1234");
    doNothing().when(basic).sendObject(any());
    chatEndpoint.onOpen(session,username);
    message = new Message();
    message.setFrom(username);
    message.setTo(username);
    message.setContent(username + ":What is the time?");
    message.setTimestamp(username + ":1234567890");
    when(userService.findUserByName(username)).thenReturn(Optional.of(user));
    chatEndpoint.onMessage(session,message);
  }

  @Test
  public void testOnMessageDirectNotPresent() throws IOException, EncodeException, NoSuchFieldException, IllegalAccessException {
    Field f3 = chatEndpoint.getClass().getDeclaredField("users");
    f3.setAccessible(true);
    f3.set(chatEndpoint, users);
    message = new Message();
    message.setFrom(username);
    message.setTo(username);
    message.setContent(username + ":What is the time?");
    message.setTimestamp(username + ":1234567890");
    when(session.getBasicRemote()).thenReturn(basic);
    when(session.getId()).thenReturn("1234");
    when(users.get(anyString())).thenReturn(username);
    doNothing().when(basic).sendObject(any());
    when(userService.findUserByName(username)).thenReturn(Optional.ofNullable(null));
    chatEndpoint.onMessage(session,message);
  }

  @Test
  public void testOnMessageGroup() throws IOException, EncodeException {
    when(groupService.getGroupByName(groupname)).thenReturn(group);
    when(userService.findUserByName(groupname)).thenReturn(Optional.ofNullable(null));
    when(userService.findUserByName(username)).thenReturn(Optional.of(user));
    when(userService.findUserByName("username2")).thenReturn(Optional.of(user));
    when(session.getBasicRemote()).thenReturn(basic);
    when(session.getId()).thenReturn("1234");
    doNothing().when(basic).sendObject(any());
    chatEndpoint.onOpen(session,username);
    chatEndpoint.onOpen(session,"username2");
    message = new Message();
    message.setFrom(username);
    message.setTo(groupname);
    message.setContent(username + ":What is the time?");
    message.setTimestamp(username + ":1234567890");
    chatEndpoint.onMessage(session,message);
  }

  @Test
  public void testOnMessageGroupNotPresent() throws IOException, EncodeException {
    when(groupService.getGroupByName(groupname)).thenReturn(null);
    when(userService.findUserByName(groupname)).thenReturn(Optional.ofNullable(null));
    when(userService.findUserByName(username)).thenReturn(Optional.of(user));
    when(userService.findUserByName("username2")).thenReturn(Optional.of(user));
    when(session.getBasicRemote()).thenReturn(basic);
    when(session.getId()).thenReturn("1234");
    doNothing().when(basic).sendObject(any());
    chatEndpoint.onOpen(session,username);
    chatEndpoint.onOpen(session,"username2");
    message = new Message();
    message.setFrom(username);
    message.setTo(groupname);
    message.setContent(username + ":What is the time?");
    message.setTimestamp(username + ":1234567890");
    chatEndpoint.onMessage(session,message);
  }

  private void persistMessage() {
      when(manager.getTransaction()).thenReturn(transaction);
      doNothing().when(transaction).begin();
      when(manager.createNativeQuery(anyString())).thenReturn(query);
      when(query.setParameter(anyInt(),anyString())).thenReturn(query);
      when(query.executeUpdate()).thenReturn(0);
      doNothing().when(transaction).commit();
  }

  @After
  public void cleanup() {
    chatEndpoint.onClose(session);
  }
}

