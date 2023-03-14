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

    public void buyListPayment() {
        for (Map.Entry<Integer, Commodity> entry : buyList.entrySet()) {
            purchasedList.put(entry.getKey(), entry.getValue());
            credit -= entry.getValue().getPrice();
        }
        buyList.clear();
        buyList = new HashMap<>();
    }

    public void addToBuyList(Commodity commodity) {
        buyList.put(commodity.getId(), commodity);
    }

    public void addCredit(Integer newCredit) { credit += newCredit;}

    public HashMap<Integer, Commodity> getBuyList() {
        return buyList;
    }

    public HashMap<Integer, Commodity> getPurchasedListList() {
        return purchasedList;
    }

    public void removeFromBuyList(Integer commodityId) {
        buyList.remove(commodityId);
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
