package org.mm.Exceptions;

public class InvalidDiscountError extends Exception {
    public String getMessage() {
        return "Invalid discount value";
    }
}
