package com.neu.prattle.exceptions;

public class GroupAlreadyPresentException extends RuntimeException{
    /**
     *
     */
    private static final long serialVersionUID = -4845176561270017896L;

    public GroupAlreadyPresentException(String message)  {
        super(message);
    }
}
