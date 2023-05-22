package org.mm.Exceptions;

public class DiscountCodeUsedError extends Exception {
    public String getMessage() {
        return "The discount code has already been used";
    }
}
