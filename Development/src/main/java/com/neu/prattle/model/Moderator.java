package com.neu.prattle.model;


import javax.persistence.Entity;
import javax.persistence.Table;

public class Moderator extends User {

  public Moderator() {}

  public Moderator (String username) {
    super(username);
  }
}
