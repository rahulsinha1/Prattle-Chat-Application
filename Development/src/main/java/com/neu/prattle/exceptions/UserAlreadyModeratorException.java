package com.neu.prattle.exceptions;

public class UserAlreadyModeratorException extends RuntimeException {

  public UserAlreadyModeratorException(String message) {
    super(message);
  }
}
