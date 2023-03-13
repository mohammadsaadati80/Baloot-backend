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

    private Set<Integer> buyList = new HashSet<>();

    public void update(User updatedUser) {
        username = updatedUser.getUsername();
        password = updatedUser.getPassword();
        email = updatedUser.getEmail();
        birthDate = updatedUser.getBirthDate();
        address = updatedUser.getAddress();
        credit = updatedUser.getCredit();
    }

    public boolean isValidCommand() {
        if (username==null || password==null || email==null || birthDate==null || address==null || credit==0)
            return false;
        else
            return true;
    }

    public boolean isInBuyList(Integer commodityId) {
        if (buyList.contains(commodityId))
            return true;
        else
            return false;
    }

    public boolean haveSpecialCharacter() {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(username);
        return m.find();
    }

    public void addToBuyList(Integer commodityId) {
        buyList.add(commodityId);
    }

    public Set<Integer> getBuyList() {
        return buyList;
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