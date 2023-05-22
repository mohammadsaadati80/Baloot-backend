package org.mm.Exceptions;

public class InvalidPriceRangeError extends Exception {
    public String getMessage() {
        return "Invalid price range";
    }
}
