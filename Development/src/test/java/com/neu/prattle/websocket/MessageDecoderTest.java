package com.neu.prattle.websocket;

import com.neu.prattle.model.Message;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.websocket.EncodeException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageDecoderTest {
  @Mock
  private ObjectMapper objectMapper;

  private MessageDecoder messsageDecoder = new MessageDecoder();

  private Message message = new Message();

  private String encodedString = "encodedString";

  @Before
  public void setUp() throws IllegalAccessException, NoSuchFieldException {
    Field f1 = messsageDecoder.getClass().getDeclaredField("objectMapper");
    f1.setAccessible(true);
    f1.set(messsageDecoder, objectMapper);
  }

  @Test
  public void testDecode() throws IOException, EncodeException {
    when(objectMapper.readValue(encodedString,Message.class)).thenReturn(message);
    messsageDecoder.decode(encodedString);
  }

  @Test
  public void testEncodeThrowsException() throws IOException, EncodeException {
    when(objectMapper.readValue(encodedString,Message.class)).thenThrow(new IOException());
    Message result = messsageDecoder.decode(encodedString);
    assertNull(result);
  }

  @Test
  public void testDestroy() {
    messsageDecoder.destroy();
  }

  @Test
  public void testWillDecode() {
    assertTrue(messsageDecoder.willDecode(encodedString));
  }

  @Test
  public void testWillDecodeNull() {
    assertFalse(messsageDecoder.willDecode(null));
  }
}
