package org.mm.Exceptions;

public class CommodityNotFoundError extends Exception {
    public String getMessage() {
        return "Commodity not found";
    }
}
