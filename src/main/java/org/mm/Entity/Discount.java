package org.mm.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "discount")
public class Discount {
    @Id
    private Integer id;
    private String discountCode;
    private Integer discount;
    public Discount (String _discountCode, Integer _discount) {
        discountCode = _discountCode;
        discount = _discount;
    }

    public boolean isValidCommand() {
        if (discountCode==null || discount==null)
            return false;
        else
            return true;
    }
    public void update(Discount _discount) {
        discountCode = _discount.getDiscountCode();
        discount = _discount.getDiscount();
    }
    public  boolean isValidDiscount() {
        if (discount > 0 && discount <= 100)
            return true;
        else
            return false;
    }
    public Integer getDiscount() {
        return discount;
    }
    public String getDiscountCode() {
        return discountCode;
    }

    public Integer getId() { return  id;}
}
