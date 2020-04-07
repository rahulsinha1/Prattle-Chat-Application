package com.neu.prattle.service;

import com.neu.prattle.main.EntityManagerObject;
import com.neu.prattle.model.Message;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class MessageServiceImpl implements MessageService {

  private static MessageService messageService;

  private static final EntityManager manager = EntityManagerObject.getInstance();

  static {
    messageService = new MessageServiceImpl();
  }

  private MessageServiceImpl() {

  }

  public static MessageService getInstance() {
    return messageService;
  }

  @Override
  public List getMessages(String username) {
    TypedQuery<Message> query = manager.createQuery("SELECT m FROM Message m" +
            " WHERE m.from = :username or m.to = :username", Message.class);
    return query.setParameter("username", username).getResultList();
  }
}
