package com.neu.prattle.exceptions;

public class UserDoesNotHaveAnyGroup extends RuntimeException {

    public UserDoesNotHaveAnyGroup(String message) {
        super(message);
    }
}
