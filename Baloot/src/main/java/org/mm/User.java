package org.mm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

public class User {
    private String username;
    private String password;
    private String email;
    private String birthDate;
    private String address;
    private Integer credit;

    public void initialValues() {
        username = "";
        password = "";
        email = "";
//        birthDate = new Date(1000, 01 , 01);
        birthDate = "";
        address = "";
        credit = 0;
    }

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

    public Integer getCredit() {
        return credit;
    }

    public String getAddress() {
        return password;
    }

    public String getBirthDate() {
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
