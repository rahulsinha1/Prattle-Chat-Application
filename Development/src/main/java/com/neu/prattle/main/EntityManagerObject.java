package com.neu.prattle.main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerObject {
  private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence
          .createEntityManagerFactory("fse");
  private static final EntityManager manager;


  static {
    manager = ENTITY_MANAGER_FACTORY.createEntityManager();
  }
  public static EntityManager getInstance() {
    return manager;
  }

  private EntityManagerObject(){}
}
