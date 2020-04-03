package com.neu.prattle.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/***
 * A Basic POJO for Message.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 */
@Entity
@Table(name = "messages")
public class Message {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    /***
     * The name of the user who sent this message.
     */
    @Column(name = "sender")
    private String from;
    /***
     * The name of the user to whom the message is sent.
     */
    @Column(name = "receiver")
    private String to;
    /***
     * It represents the contents of the message.
     */
    @Column(name = "content")
    private String content;

    @Column(name = "time_stamp")
    private String timestamp;

    @Column(name = "isDeleted")
    private boolean isDeleted;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("From: ").append(from)
                .append("To: ").append(to)
                .append("Content: ").append(content)
                .append("Timestamp: ").append(timestamp)
                .toString();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static MessageBuilder messageBuilder()   {
        return new MessageBuilder();
    }

    /***
     * A Builder helper class to create instances of {@link Message}
     */
    public static class MessageBuilder    {
        /***
         * Invoking the build method will return this message object.
         */
        Message message;

        public MessageBuilder()    {
            message = new Message();
            message.setFrom("Not set");
        }

        public MessageBuilder setFrom(String from)    {
            message.setFrom(from);
            return this;
        }

        public MessageBuilder setTo(String to)    {
            message.setTo(to);
            return this;
        }

        public MessageBuilder setMessageContent(String content)   {
            message.setContent(content);
            return this;
        }

        public MessageBuilder setMessageTimestamp(String timestamp)   {
            message.setTimestamp(timestamp);
            return this;
        }

        public MessageBuilder setId(int id)   {
            message.setId(id);
            return this;
        }

        public MessageBuilder setIsDeleted(boolean isDeleted) {
            message.setDeleted(isDeleted);
            return this;
        }

        public Message build()  {
            return message;
        }
    }
}
