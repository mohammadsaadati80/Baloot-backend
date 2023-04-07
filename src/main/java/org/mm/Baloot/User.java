package org.mm.Baloot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

public class User {
    private String username;
    private String password;
    private String email;
    private Date birthDate;
    private String address;
    private Integer credit;

    private HashMap<Integer, Commodity> buyList = new HashMap<>();

    private HashMap<Integer, Commodity> purchasedList = new HashMap<>();

    private Map<String, Discount> usedDiscounts = new HashMap<>();

    private Discount currentDiscount = null;

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

    public boolean isValidCommand() {
        if (username==null || password==null || email==null || birthDate==null || address==null || credit==null)
            return false;
        else
            return true;
    }

    public boolean isInBuyList(Integer commodityId) {
        if (buyList.containsKey(commodityId))
            return true;
        return false;
    }

    public boolean haveSpecialCharacter() {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(username);
        return m.find();
    }

    public boolean haveEnoughCredit() {
        Integer totalPrice = 0;
        for (Map.Entry<Integer, Commodity> entry : buyList.entrySet())
            totalPrice += entry.getValue().getPrice();
        return (credit >= totalPrice);
    }

    public void buyListPayment() {
        Integer totalPrice = applyDiscountOnBuyListPrice();
        for (Map.Entry<Integer, Commodity> entry : buyList.entrySet()) {
            purchasedList.put(entry.getKey(), entry.getValue());
        }
        credit -= totalPrice;
        if(currentDiscount != null) {
            usedDiscounts.put(currentDiscount.getDiscountCode(), currentDiscount);
            currentDiscount = null;
        }
        buyList.clear();
        buyList = new HashMap<>();
    }

    public void addToBuyList(Commodity commodity) {
        buyList.put(commodity.getId(), commodity);
    }

    public void addCredit(Integer newCredit) { credit += newCredit;}

    public void addDiscountCode(Discount discount) {currentDiscount = discount;}

    public HashMap<Integer, Commodity> getBuyList() {
        return buyList;
    }

    public HashMap<Integer, Commodity> getPurchasedList() {
        return purchasedList;
    }

    public void removeFromBuyList(Integer commodityId) {
        buyList.remove(commodityId);
    }

    public Integer getCurrentBuyListPrice() {
        Integer totalPrice = 0;
        for (Map.Entry<Integer, Commodity> entry : buyList.entrySet())
            totalPrice += entry.getValue().getPrice();
        return totalPrice;
    }

    public Integer applyDiscountOnBuyListPrice() {
        Integer totalPrice = getCurrentBuyListPrice();
        if(currentDiscount != null) {
            totalPrice *= (100 - currentDiscount.getDiscount())/100;
        }
        return totalPrice;
    }

    public boolean isUsedDiscountCode(String discountCode) {
        if (usedDiscounts.containsKey(discountCode))
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
