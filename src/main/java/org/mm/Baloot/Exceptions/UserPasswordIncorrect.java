package org.mm.Baloot.Exceptions;

public class UserPasswordIncorrect extends Exception {
    public String getMessage() {
        return "User password is incorrect";
    }
}