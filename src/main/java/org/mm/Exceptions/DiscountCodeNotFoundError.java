package org.mm.Exceptions;

public class DiscountCodeNotFoundError extends Exception {
    public String getMessage() {
        return "Discount code not found";
    }
}
