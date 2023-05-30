package org.mm.Exceptions;

public class UserPasswordIncorrectError extends Exception {
    public String getMessage() {
        return "User password is incorrect";
    }
}