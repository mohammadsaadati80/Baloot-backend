package org.mm.Baloot.Exceptions;

public class UserNotFoundError extends Exception {
    public String getMessage() {
        return "User not found";
    }
}
