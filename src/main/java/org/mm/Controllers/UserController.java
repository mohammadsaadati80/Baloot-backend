package org.mm.Controllers;

import org.mm.Baloot.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UserController {

    private Baloot baloot;

    public UserController(){
        baloot = Baloot.getInstance();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public Map<String, User> getUsers(){
        try {
            return baloot.getUsers();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
