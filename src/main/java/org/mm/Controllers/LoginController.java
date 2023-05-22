package org.mm.Controllers;

import org.mm.Service.Baloot;
import org.mm.Entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class LoginController {
    private Baloot baloot;

    @ResponseStatus(value = HttpStatus.OK,reason = "کاربر با موفقیت لاگین شد.")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    protected void login(@RequestBody Map<String, String> user_info){
        try {
            baloot.login(user_info.get("username"), user_info.get("password"));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @ResponseStatus(value = HttpStatus.OK,reason = "کاربر با موفقیت از سامانه خارج شد.")
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    protected void logout(){
        baloot.logout();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/login/user",method = RequestMethod.GET)
    protected User getLoggedInUser(){
        try {
            return baloot.getUserById(baloot.getLoginUsername());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @ResponseStatus(value = HttpStatus.OK,reason = "کاربر با موفقیت ثبت نام شد.")
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    protected void registerUser(@RequestBody Map<String, String> user_info){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

//        Date date = new Date(user_info.get("birthDate"));
        try {
            Date date = format.parse(user_info.get("birthDate"));
            baloot.registerUser(user_info.get("username"), user_info.get("password"), user_info.get("email"), date, user_info.get("address"));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
