package com.neu.prattle.serviceTest;

import com.neu.prattle.model.Message;
import com.neu.prattle.service.MessageService;
import com.neu.prattle.service.MessageServiceImpl;
import com.neu.prattle.websocket.ChatEndpoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceImplTest {

  MessageService messageService = MessageServiceImpl.getInstance();

  @Mock
  private Session session = mock(Session.class, RETURNS_DEEP_STUBS);

  @Test
  public void testGetMessage() throws IOException, EncodeException {
    /*//messageService.getMessages("Test");
    Message message = new Message();
    message.setFrom("Test");
    message.setTo("test");
    message.setContent("test" + ":What is the time?");
    message.setTimestamp("test" + ":1234567890");
    ChatEndpoint chatEndpoint = new ChatEndpoint();
    chatEndpoint.onOpen(session,"Test");
    chatEndpoint.onMessage(session,message);*/
  }
}
