package com.neu.prattle.exceptions;

public class CannotRemoveUserException extends RuntimeException{
  public CannotRemoveUserException(String message)  {
    super(message);
  }
}
