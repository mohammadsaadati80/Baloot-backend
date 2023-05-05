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
    @RequestMapping(value = "/buylist/suggestion", method = RequestMethod.POST)
    public List<Commodity> getSuggestedCommodities(@RequestBody Map<String, String> body){
        int commodity_id = Integer.parseInt(body.get("commodityId"));
        try {
            return baloot.getSuggestedCommodities(baloot.getCommodityById(commodity_id));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/purchasedlist", method = RequestMethod.GET)
    public List<Commodity> getPurchasedList(){
        try {
            return baloot.getUserById(baloot.getLoginUsername()).getPurchasedList();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @ResponseStatus(value = HttpStatus.OK,reason = "کالا با موفقیت به لیست خرید اضافه شد.")
    @RequestMapping(value = "/buylist/remove_from_buylist",method = RequestMethod.POST)
    public void removeFromBuyList(@RequestBody Map<String, String> body){
        int commodity_id = Integer.parseInt(body.get("commodityId"));
        try {
            baloot.removeFromBuyList(baloot.getLoginUsername(), commodity_id);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/buylist/discount",method = RequestMethod.POST)
    public void applyDiscount(@RequestBody Map<String, String> body){
        try {
            baloot.applyDiscountCode(baloot.getLoginUsername(), body.get("discount"));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/buylist/payment",method = RequestMethod.POST)
    public void buyListPayment(){
        try {
            baloot.userBuyListPayment(baloot.getLoginUsername());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/buylist/total_price",method = RequestMethod.GET)
    public Integer buyListTotalPrice(){
        try {
           return baloot.getUserById(baloot.getLoginUsername()).getCurrentBuyListPrice();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/buylist/total_price_with_discount",method = RequestMethod.GET)
    public Integer buyListTotalPriceAfterDiscount(){
        try {
           return baloot.getUserById(baloot.getLoginUsername()).applyDiscountOnBuyListPrice();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}