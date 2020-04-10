package com.neu.prattle.exceptions;

public class CannotRemoveModeratorException extends RuntimeException {
  public CannotRemoveModeratorException(String message) {
    super(message);
  }
}
