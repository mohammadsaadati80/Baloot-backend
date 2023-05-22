package org.mm.Controllers;

import org.mm.Entity.*;

import org.mm.Service.Baloot;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ProviderController {

    private Baloot baloot;


    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/providers", method = RequestMethod.GET)
    public Map<Integer, Provider> getProviders(){
        try {
            return baloot.getProviders();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
