package com.neu.prattle;

import com.neu.prattle.model.Message;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageModelTest {

  @Test
  public void testBuilder(){
    Message m = new Message.MessageBuilder()
            .setFrom("Test1")
            .setMessageContent("Hello")
            .setTo("test2").build();
    assertEquals("Hello",m.getContent());
    assertEquals("Test1",m.getFrom());
    assertEquals("test2",m.getTo());
    assertEquals("From: Test1To: test2Content: Hello",m.toString());
  }
}
