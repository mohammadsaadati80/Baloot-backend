package org.mm.Controllers;

import org.mm.Baloot.Baloot;
import org.mm.Baloot.Commodity;
import org.mm.Baloot.Comment;
import org.mm.Baloot.Rate;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.*;
import java.util.Objects;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CommodityController {
    private Baloot baloot;

    public CommodityController(){
        baloot = Baloot.getInstance();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/commodity/{id}", method = RequestMethod.GET)
    public Commodity getCommodity(@PathVariable("id") String id){
        int commodity_id = Integer.parseInt(id);
        try {
            return baloot.getCommodityById(commodity_id);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @ResponseStatus(value = HttpStatus.OK,reason = "کامنت با موفقیت به کامنت ها اضافه شد.")
    @RequestMapping(value = "/commodity/add_comment",method = RequestMethod.POST)
    public void addComment(@RequestBody Map<String, String> comment_info) {
        int commodity_id = Integer.parseInt(comment_info.get("commodityId"));
        try{
            Comment newComment = new Comment(baloot.getUserById(baloot.getLoginUsername()).getEmail(), commodity_id,
                    comment_info.get("text"), new Date());
            baloot.addComment(newComment);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @ResponseStatus(value = HttpStatus.OK,reason = "امتیاز با موفقیت داده شد.")
    @RequestMapping(value = "/commodity/rate",method = RequestMethod.POST)
    public void rateCommodity(@RequestBody Map<String, String> body){
        int commodity_id = Integer.parseInt(body.get("commodityId"));
        String user_name = body.get("username");
        float score = Float.parseFloat(body.get("score"));
        try {
            Rate newRate = new Rate(user_name, commodity_id, score);
            baloot.rateCommodity(newRate);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @ResponseStatus(value = HttpStatus.OK,reason = "کالا با موفقیت به لیست خرید اضافه شد.")
    @RequestMapping(value = "/commodity/add_to_watchlist",method = RequestMethod.POST)
    public void addToBuyList(@RequestBody Map<String, String> commodityId){
        int commodity_id = Integer.parseInt(commodityId.get("commodityId"));
        try {
            baloot.addToBuyList(baloot.getLoginUsername(), commodity_id);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

}