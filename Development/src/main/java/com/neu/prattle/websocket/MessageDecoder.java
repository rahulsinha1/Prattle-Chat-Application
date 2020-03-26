package com.neu.prattle.websocket;

/**
 * Decodes in-bound messages that come in as JSON structures into Message objects.
 * 
 * @author https://github.com/eugenp/tutorials/java-websocket/src/main/java/com/baeldung/websocket/MessageDecoder.java
 * @version dated 2017-03-05
 */

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import com.neu.prattle.model.Message;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * The Class MessageDecoder.
 */
public class MessageDecoder implements Decoder.Text<Message> {

    /** The logger. */
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private static Gson gson = new Gson();

    /**
     * Decode.
     * 
     * Extracts the text message from a JSON structure.  It's very bad if there's no message.
     *
     * @param s    the JSON structure that was sent in the channel
     * @return     a Message object
     */
    @Override
    public Message decode(String s) {
        Message message = null;
        try {
            JsonReader reader = new JsonReader(new StringReader(s));
            reader.setLenient(true);
            message = gson.fromJson(reader, Message.class);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return message;
    }

    /**
     * Will decode.
     * 
     * Tests if there's a string to decode.
     *
     * @param s the s
     * @return true, if successful
     */
    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }

    /**
     * Custom code if anything special is needed when establishing the session
     * with a particular endpoint (the websocket).  Not used at present. 
     *
     * @param endpointConfig the endpoint config
     */
    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    /**
     * Destroy.
     * 
     * Close the connection.  Nothing implemented in the prototype.
     * But then again, there's no disconnect message.
     * 
     */
    @Override
    public void destroy() {
        // Close resources
    }
}
