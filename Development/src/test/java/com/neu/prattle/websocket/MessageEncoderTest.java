package com.neu.prattle.websocket;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.neu.prattle.model.Message;

//import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.websocket.EncodeException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageEncoderTest {

  @Mock
  private ObjectMapper objectMapper;

  private MessageEncoder messageEncoder = new MessageEncoder();

  private Message message = new Message();

  @Before
  public void setUp() throws IllegalAccessException, NoSuchFieldException {
    Field f1 = messageEncoder.getClass().getDeclaredField("objectMapper");
    f1.setAccessible(true);
    f1.set(messageEncoder, objectMapper);
  }

  @Test
  public void testEncode() throws IOException, EncodeException {
    when(objectMapper.writeValueAsString(message)).thenReturn("");
    messageEncoder.encode(message);
  }

  @Test
  public void testEncodeThrowsException() throws IOException, EncodeException {
    when(objectMapper.writeValueAsString(message)).thenThrow(new IOException());
    String result = messageEncoder.encode(message);
    assertEquals("{}",result);
  }

  @Test
  public void testDestroy() {
    messageEncoder.destroy();
  }
}
