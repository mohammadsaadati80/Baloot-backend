package org.mm.Exceptions;

public class UserBuyListIsEmptyError extends Exception {
    public String getMessage() {
        return "User buy list is empty";
    }
}