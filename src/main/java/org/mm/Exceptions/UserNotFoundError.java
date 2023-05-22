package org.mm.Exceptions;

public class UserNotFoundError extends Exception {
    public String getMessage() {
        return "User not found";
    }
}
