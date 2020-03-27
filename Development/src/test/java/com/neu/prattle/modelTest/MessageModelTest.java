package com.neu.prattle.modelTest;

import com.neu.prattle.model.Message;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageModelTest {

  @Test
  public void testBuilder(){
    Message m = new Message.MessageBuilder()
            .setFrom("Test1")
            .setMessageContent("Hello")
            .setTo("test2").setMessageTimestamp("1234567890").build();
    assertEquals("Hello",m.getContent());
    assertEquals("Test1",m.getFrom());
    assertEquals("test2",m.getTo());
    assertEquals("1234567890",m.getTimestamp());
    assertEquals("From: Test1To: test2Content: HelloTimestamp: 1234567890",m.toString());
  }
}
