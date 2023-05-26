package org.mm.Controllers;

import org.mm.Entity.*;

import org.mm.Service.Baloot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UserController {
    @Autowired
    private Baloot baloot;


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<User> getUsers(){
        try {
            return baloot.getUsers();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
