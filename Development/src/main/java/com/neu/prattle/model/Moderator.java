package com.neu.prattle.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;


@Entity
@Table(name = "moderator")

public class Moderator extends User {

  public Moderator() {}

  public Moderator (String username) {
    super(username);
  }
}
