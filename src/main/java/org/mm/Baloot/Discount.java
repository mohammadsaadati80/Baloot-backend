package org.mm.Baloot;

public class Discount {
    private String discountCode;
    private Integer discount;
    public Discount (String _discountCode, Integer _discount) {
        discountCode = _discountCode;
        discount = _discount;
    }
    public  boolean isValidDiscount() {
        if (discount > 0 && discount <= 100)
            return true;
        else return false;
    }
    public Integer getDiscount() {
        return discount;
    }
    public String getDiscountCode() {
        return discountCode;
    }
}
