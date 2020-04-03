package com.neu.prattle.exceptions;

public class UserAlreadyPresentInGroupException extends RuntimeException {
    public UserAlreadyPresentInGroupException(String message)  {
        super(message);
    }
}
