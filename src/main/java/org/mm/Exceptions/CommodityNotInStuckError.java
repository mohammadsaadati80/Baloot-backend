package org.mm.Exceptions;

public class CommodityNotInStuckError extends Exception {
    public String getMessage() {
        return "Commodity is not available in stock";
    }
}
