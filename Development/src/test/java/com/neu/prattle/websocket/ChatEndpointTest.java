package com.neu.prattle.websocket;

import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Optional;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import static org.mockito.Matchers.any;
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

  private String username = "username";

  private User user = new User();

  private ChatEndpoint chatEndpoint = new ChatEndpoint();

  private RemoteEndpoint.Basic basic = mock(RemoteEndpoint.Basic.class);

  private Message message;

  @Before
  public void setUp() throws IllegalAccessException, NoSuchFieldException {
    Field f1 = chatEndpoint.getClass().getDeclaredField("accountService");
    f1.setAccessible(true);
    f1.set(chatEndpoint, userService);
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
  public void testOnClose() {
    chatEndpoint.onClose(session);
  }

  @Test
  public void testOnMessageBroadcastUserPresent() throws IOException, EncodeException {
    message = new Message();
    message.setFrom(username);
    message.setContent("What is the time?");
    when(userService.findUserByName(username)).thenReturn(Optional.of(user));
    chatEndpoint.onMessage(session,message);
  }
}
