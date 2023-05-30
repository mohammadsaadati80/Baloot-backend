package org.mm.Exceptions;

public class InvalidCreditValueError extends Exception {
    public String getMessage() {
        return "Invalid credit value";
    }
}
