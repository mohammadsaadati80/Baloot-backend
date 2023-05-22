package org.mm.Controllers;

import org.mm.Service.Baloot;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CreditController  {
    private Baloot baloot;

    public CreditController(){
        baloot = Baloot.getInstance();
    }

    @ResponseStatus(value = HttpStatus.OK,reason = "اعتبار کاربر با موفقیت افزایش یافت.")
    @RequestMapping(value = "/addcredit/",method = RequestMethod.POST)
    public void likeComment (@RequestBody Map<String, String> credit){
        try {
            baloot.addCredit(baloot.getLoginUsername(), Integer.valueOf(credit.get("creditValue")));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

}