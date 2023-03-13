package org.mm.Baloot.Exceptions;

public class UsernameShouldNotContainSpecialCharactersError extends Exception {
    public String getMessage() {
        return "Username should not contain special characters";
    }
}
