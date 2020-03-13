package com.neu.prattle;

import com.neu.prattle.main.PrattleApplication;

import org.junit.Test;

public class PrattleApplicationTest {

  PrattleApplication prattleApplication = new PrattleApplication();

  @Test
  public void testGetClasses(){
    prattleApplication.getClasses();
  }
}
