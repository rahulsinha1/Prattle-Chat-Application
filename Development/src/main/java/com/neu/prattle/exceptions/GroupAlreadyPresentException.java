package com.neu.prattle.exceptions;

/***
 * An representation of an error which is thrown where a request has been made
 * for creation of a group object that already exists in the system.
 * Refer {@link com.neu.prattle.model.Group#equals}
 * Refer {@link com.neu.prattle.service.GroupService#createGroup(com.neu.prattle.model.Group)}
 *
 */
public class GroupAlreadyPresentException extends RuntimeException {
  public GroupAlreadyPresentException(String message) {
    super(message);
  }
}