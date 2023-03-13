package org.mm.Baloot.Exceptions;

public class CommodityIsNotInBuyListError extends Exception {
    public String getMessage() {
        return "Commodity is not in BuyList";
    }
}
