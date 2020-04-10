package com.neu.prattle.exceptions;

public class GroupDoesNotExistException extends RuntimeException{
    public GroupDoesNotExistException(String message)  {
        super(message);
    }
}
