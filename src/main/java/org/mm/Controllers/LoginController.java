package org.mm.Controllers;

import org.mm.Baloot.Baloot;
import org.mm.Baloot.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class LoginController {
    @ResponseStatus(value = HttpStatus.OK,reason = "کاربر با موفقیت لاگین شد.")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    protected void login(@RequestBody Map<String, String> user_info){
        Baloot baloot = Baloot.getInstance();
        try {
            baloot.login(user_info.get("username"), user_info.get("password"));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @ResponseStatus(value = HttpStatus.OK,reason = "کاربر با موفقیت از سامانه خارج شد.")
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    protected void logout(){
        Baloot baloot = Baloot.getInstance();
        baloot.logout();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/login/user",method = RequestMethod.GET)
    protected User getLoggedInUser(){
        Baloot baloot = Baloot.getInstance();
        try {
            return baloot.getUserById(baloot.getLoginUsername());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
