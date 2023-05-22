package org.mm.Exceptions;

public class CommodityAlreadyInBuyListError extends Exception {
    public String getMessage() {
        return "Commodity already in BuyList";
    }
}
