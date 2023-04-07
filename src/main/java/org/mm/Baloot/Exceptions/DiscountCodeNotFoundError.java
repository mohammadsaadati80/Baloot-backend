package org.mm.Baloot.Exceptions;

public class DiscountCodeNotFoundError extends Exception {
    public String getMessage() {
        return "Discount code not found";
    }
}
