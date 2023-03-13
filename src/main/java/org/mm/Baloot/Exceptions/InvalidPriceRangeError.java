package org.mm.Baloot.Exceptions;

public class InvalidPriceRangeError extends Exception {
    public String getMessage() {
        return "Invalid price range";
    }
}
