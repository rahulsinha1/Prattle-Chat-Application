package com.neu.prattle.service;

import com.neu.prattle.model.Message;

import java.util.List;

public interface MessageService {

  List getMessages(String username);

  Message findMessageById(int id);

  void deleteMessageById(int id);

}
