package org.mm.Baloot.Exceptions;

public class InvalidCommandError extends Exception {
    public String getMessage() {
        return "Invalid command";
    }
}
