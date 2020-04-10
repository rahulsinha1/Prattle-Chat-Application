package com.neu.prattle.controllerTest;

import com.neu.prattle.controller.MessageController;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.service.MessageService;
import com.neu.prattle.service.UserService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageControllerTest {

  private MessageController messageController = new MessageController();

  @Mock
  private MessageService messageService;

  @Mock
  private UserService userService;

  @Before
  public void setUp() throws IllegalAccessException, NoSuchFieldException  {
    Field f1 = messageController.getClass().getDeclaredField("messageService");
    f1.setAccessible(true);
    f1.set(messageController, messageService);

    f1 = messageController.getClass().getDeclaredField("userService");
    f1.setAccessible(true);
    f1.set(messageController, userService);
  }

  @Test
  public void testGetAllUserConversation() {
    User u = new User();
    List messages = null;
    when(userService.findUserByUsername(anyString())).thenReturn(u);
    when(messageService.getMessages(anyString())).thenReturn(messages);
    assertEquals(200,messageController.getAllUserConversation("test").getStatus());
  }

  @Test
  public void testGetAllUserConversationThrowException() {
    when(userService.findUserByUsername(anyString())).thenReturn(null);
    assertEquals(200,messageController.getAllUserConversation("test").getStatus());
  }

  @Test
  public void testGetMessageById(){
    Message m = new Message();
    when(messageService.findMessageById(anyInt())).thenReturn(m);
    assertEquals(200,messageController.getMessageById("1").getStatus());
  }

  @Test
  public void testGetMessageByIdThrowException(){
    Message m = new Message();
    when(messageService.findMessageById(anyInt())).thenThrow(RuntimeException.class);
    assertEquals(409,messageController.getMessageById("1").getStatus());
  }

  @Test
  public void testDeleteMessageById(){
    Message m = new Message();
    doNothing().when(messageService).deleteMessageById(anyInt());
    assertEquals(200,messageController.deleteMessage("1").getStatus());
  }

  @Test
  public void testDeleteMessageByIdThrowException(){
    Message m = new Message();
    doThrow(RuntimeException.class).when(messageService).deleteMessageById(anyInt());
    assertEquals(409,messageController.deleteMessage("1").getStatus());
  }
}
