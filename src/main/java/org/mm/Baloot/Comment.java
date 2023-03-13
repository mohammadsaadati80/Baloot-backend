package org.mm.Baloot;

import java.util.*;

public class Comment {
    private String userEmail;
    private Integer commodityId;
    private String text;
    private Date date;
    private Map<String, Integer> usersVote = new HashMap<>();

    public void update(Comment updatedComment) {
        userEmail = updatedComment.getUserEmail();
        date = updatedComment.getDate();
        commodityId = updatedComment.getCommodityId();
        text = updatedComment.getText();
    }

    public boolean isValidCommand() {
        if (userEmail==null || date==null || commodityId==null || text==null)
            return false;
        else
            return true;
    }
    
    public void addVote(String username, Integer vote) {
        if (usersVote.containsKey(username)) {
            usersVote.replace(username, vote);
        } else {
            usersVote.put(username, vote);
        }
    }

    public String getUserEmail() { return userEmail;}

    public Integer getCommodityId() { return commodityId;}

    public String getText() { return text;}

    public Date getDate() { return date;}

}
