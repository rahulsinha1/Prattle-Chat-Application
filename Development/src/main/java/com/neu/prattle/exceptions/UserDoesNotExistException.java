package com.neu.prattle.exceptions;

/***
 * An representation of an error which is thrown where a request has been made
 * for creation of a user object does not exists in the system.
 * Refer {@link com.neu.prattle.model.User#equals}
 * Refer {@link com.neu.prattle.service.UserService#findGroupsByName(String)}}
 *
 */

public class UserDoesNotExistException extends RuntimeException {

  public UserDoesNotExistException(String message) {
    super(message);
  }
}
