package org.mm.Baloot.Exceptions;

public class UserNotHaveEnoughCreditError extends Exception {
    public String getMessage() {
        return "User not have enough credit";
    }
}
