package org.mm.Controllers;

import org.mm.Baloot.Baloot;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CommentController {
    private Baloot baloot;

    public CommentController(){
        baloot = Baloot.getInstance();
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public void getComments (@RequestBody Map<String, String> body){
        try {
            baloot.getCommentByCommodity(Integer.valueOf(body.get("commodityId")));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @ResponseStatus(value = HttpStatus.OK,reason = "کامنت با موفقیت لایک شد.")
    @RequestMapping(value = "/comment/like",method = RequestMethod.POST)
    public void likeComment (@RequestBody Map<String, String> commentId){
        try {
            baloot.voteComment(baloot.getLoginUsername(), Integer.valueOf(commentId.get("commentId")),1);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @ResponseStatus(value = HttpStatus.OK,reason = "کامنت با موفقیت دیس لایک شد.")
    @RequestMapping(value = "/comment/dislike",method = RequestMethod.POST)
    public void dislikeComment (@RequestBody Map<String, String> commentId){
        try {
            baloot.voteComment(baloot.getLoginUsername(), Integer.valueOf(commentId.get("commentId")),-1);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
