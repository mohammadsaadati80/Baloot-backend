package org.mm.Exceptions;

public class InvalidDiscount extends Exception {
    public String getMessage() {
        return "Invalid discount value";
    }
}
