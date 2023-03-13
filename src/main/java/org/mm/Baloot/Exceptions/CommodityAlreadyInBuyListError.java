package org.mm.Baloot.Exceptions;

public class CommodityAlreadyInBuyListError extends Exception {
    public String getMessage() {
        return "Commodity already in BuyList";
    }
}
