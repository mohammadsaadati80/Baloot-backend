package org.mm.Baloot.Exceptions;

public class CommodityNotInStuckError extends Exception {
    public String getMessage() {
        return "Commodity is not available in stock";
    }
}
