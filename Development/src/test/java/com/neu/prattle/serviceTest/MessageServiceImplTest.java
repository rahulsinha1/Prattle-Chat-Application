package com.neu.prattle.serviceTest;

import com.neu.prattle.controller.MessageController;
import com.neu.prattle.service.MessageService;
import com.neu.prattle.service.MessageServiceImpl;

import org.junit.Test;

public class MessageServiceImplTest {

  MessageService messageService = MessageServiceImpl.getInstance();

  @Test
  public void testGetMessage() {
    //messageService.getMessages("GroupPersTest");
    new MessageController().getAllUserConversation("GroupPersTest");
  }
}
