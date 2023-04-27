package org.mm.Controllers;

import org.mm.Baloot.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class BuyListController  {
    private Baloot baloot;

    public BuyListController(){
        baloot = Baloot.getInstance();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/buylist", method = RequestMethod.GET)
    public List<Commodity> getBuyList(){
        try {
            return baloot.getBuyList(baloot.getLoginUsername());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/buylist/suggestion", method = RequestMethod.GET)
    public List<Commodity> getSuggestedCommodities(@RequestBody Map<String, String> body){
        int commodity_id = Integer.parseInt(body.get("commodityId"));
        try {
            return baloot.getSuggestedCommodities(baloot.getCommodityById(commodity_id));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @ResponseStatus(value = HttpStatus.OK,reason = "کالا با موفقیت به لیست خرید اضافه شد.")
    @RequestMapping(value = "/buylist/remove_from_buylist",method = RequestMethod.POST)
    public void removeFromWatchList(@RequestBody Map<String, String> body){
        int commodity_id = Integer.parseInt(body.get("commodityId"));
        try {
            baloot.removeFromBuyList(baloot.getLoginUsername(), commodity_id);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

}