package com.neu.prattle.service;

import com.google.common.base.Joiner;

import com.neu.prattle.main.EntityManagerObject;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Message;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class MessageServiceImpl implements MessageService {

  private static MessageService messageService;
  private static UserService userService = UserServiceImpl.getInstance();

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
    List<Group> groups = userService.findGroupsByName(username);
    List<String> groupnames = new ArrayList();
    for(Group g : groups) {
      groupnames.add(g.getName());
    }
    groupnames.add(username);
    String delim = "\",\"";
    String resSet = Joiner.on(delim).join(groupnames);
    Query q = manager.createNativeQuery("select * from message where sender = ? or receiver in (\""
            + resSet + "\")" ,Message.class)
            .setParameter(1, username);
    return q.getResultList();
  }

  @Override
  public Message findMessageById(int id) {
    if (isRecordExist(id)) {
      Query q = manager.createNativeQuery("select * from message where id = ?",Message.class)
              .setParameter(1, id);
      return (Message) q.getSingleResult();
    } else {
      throw new RuntimeException("Message does not exist.");
    }
  }

  @Override
  public void deleteMessageById(int id) {
    EntityTransaction transaction;
    transaction = manager.getTransaction();
    transaction.begin();
    Message message = findMessageById(id);
    message.setDeleted(true);
    transaction.commit();
  }

  private boolean isRecordExist(int id) {
    Query q = manager.createNativeQuery("select count(*) from message where id = ?")
            .setParameter(1, id);
    int count = q.getResultList().size();
    return count!=0;
  }
}
