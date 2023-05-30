package org.mm.Exceptions;

public class UserAlreadyExistsError extends Exception {
    public String getMessage() {
        return "User already exists";
    }
}