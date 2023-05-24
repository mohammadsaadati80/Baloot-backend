package org.mm.Entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;

//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

@Entity
@Table(name = "user")
public class User {
    @Id
    private String username;
    private String password;
    private String email;
    private Date birthDate;
    private String address;
    private Integer credit;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "buyList", joinColumns = @JoinColumn(name = "USER_ID"),inverseJoinColumns = @JoinColumn(name = "COMMODITY_ID"))
    private Set<Commodity> buyList = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "purchasedList", joinColumns = @JoinColumn(name = "USER_ID"),inverseJoinColumns = @JoinColumn(name = "COMMODITY_ID"))
    private Set<Commodity> purchasedList = new HashSet<>();

    @ManyToMany
    @JoinTable(name="used_discount", joinColumns = @JoinColumn(name = "DISCOUNT_ID"), inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Discount> usedDiscounts = new HashSet<>();

    @ManyToOne
    @JoinTable(name="current_discount", joinColumns = @JoinColumn(name = "DISCOUNT_ID"), inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    @LazyCollection(LazyCollectionOption.FALSE)
    private Discount currentDiscount;

//    public User(String _username, String _password, String _email, Date _birthDate, String _address, Integer _credit) {
//        username = _username;
//        password = _password;
//        email = _email;
//        birthDate = _birthDate;
//        address = _address;
//        credit = _credit;
//    }

    public void update(User updatedUser) {
        username = updatedUser.getUsername();
        password = updatedUser.getPassword();
        email = updatedUser.getEmail();
        birthDate = updatedUser.getBirthDate();
        address = updatedUser.getAddress();
        credit = updatedUser.getCredit();
    }

    public void updateValue(String _username, String _password, String _email, Date _birthDate, String _address) {
        username = _username;
        password = _password;
        email = _email;
        birthDate = _birthDate;
        address = _address;
        credit = 0;
    }

    public boolean isValidCommand() {
        if (username==null || password==null || email==null || birthDate==null || address==null || credit==null)
            return false;
        else
            return true;
    }

    public boolean isInBuyList(Integer commodityId) {
        for(Commodity entry : buyList) {
            if (entry.getId().equals(commodityId))
                return true;
        }
        return false;
    }

    public boolean haveSpecialCharacter() {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(username);
        return m.find();
    }

    public boolean haveEnoughCredit() {
        return (credit >= applyDiscountOnBuyListPrice());
    }

    public void buyListPayment() {
        Integer totalPrice = applyDiscountOnBuyListPrice();
        for (Commodity entry : buyList) {
            purchasedList.add(entry);
        }
        credit -= totalPrice;
        if(currentDiscount != null) {
            usedDiscounts.add(currentDiscount);
            currentDiscount = null;
        }
        buyList.clear();
        buyList = new HashSet<Commodity>();
    }

    public void addToBuyList(Commodity commodity) {
        buyList.add(commodity);
    }

    public void addCredit(Integer newCredit) { credit += newCredit;}

    public void addDiscountCode(Discount discount) {currentDiscount = discount;}

    public Set<Commodity> getBuyList() {
        return buyList;
    }

    public Set<Commodity> getPurchasedList() {
        return purchasedList;
    }

    public void removeFromBuyList(Commodity commodity) {
        for(Commodity entry: buyList) {
            if (entry.getId().equals(commodity.getId())) {
                buyList.remove(entry);
                break;
            }
        }
    }

    public Integer getCurrentBuyListPrice() {
        Integer totalPrice = 0;
        for (Commodity entry : buyList)
            totalPrice += entry.getPrice();
        return totalPrice;
    }

    public Integer applyDiscountOnBuyListPrice() {
        float totalPrice = getCurrentBuyListPrice();
        if(currentDiscount != null) {
            float x = (100 - currentDiscount.getDiscount());
            float y = 100;
            totalPrice *= (x/y);
        }
        return (int) totalPrice;
    }

    public boolean isUsedDiscountCode(String discountCode) {
        if (usedDiscounts.contains(discountCode))
            return true;
        return false;
    }

    public Integer getDiscount() {
        if(currentDiscount != null)
            return currentDiscount.getDiscount();
        else
            return 0;
    }

    public Integer getCredit() {
        return credit;
    }

    public String getAddress() {
        return password;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

}
