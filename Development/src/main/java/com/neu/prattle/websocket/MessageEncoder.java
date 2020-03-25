package com.neu.prattle.websocket;

import com.google.gson.Gson;

import javax.crypto.NoSuchPaddingException;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.neu.prattle.model.Message;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class MessageEncoder.
 * 
 * @author https://github.com/eugenp/tutorials/java-websocket/src/main/java/com/baeldung/websocket
 * @version dated 2017-03-05
 */
public class MessageEncoder implements Encoder.Text<Message> {

	/** @see org.codehaus.jackson.map.ObjectMapper */
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final String KEY = "123";
    private static Gson gson = new Gson();
    
    /** The logger. */
    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * Encode.
     *
     * Constucts a JSON structure from a Message object, in effect
     * serializing the Message by converting it into a String.
     *  
     * @param message     What needs to be serialized
     * @return            the resulting JSON (String)
     * @throws EncodeException   see javax.websocket.EncodeException
     */
    @Override
    public String encode(Message message) throws EncodeException {
        try {
            String content = message.getContent();
            byte[] keyData = (KEY).getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyData,"Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);
            byte[] hasil = cipher.doFinal(content.getBytes());
            message.setContent(new String(Base64.getEncoder().encode(hasil)));
            return gson.toJson(message);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            return "{}";
        }
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
     * Close the connection.  Nothing implemented in the prototype.
     * But then again, there's no disconnect message.
     */
    @Override
    public void destroy() {
        // Close resources
    }
}
