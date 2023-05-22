package org.mm.Exceptions;

public class UserPasswordIncorrect extends Exception {
    public String getMessage() {
        return "User password is incorrect";
    }
}