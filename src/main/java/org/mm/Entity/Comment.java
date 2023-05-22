package org.mm.Entity;

import java.util.*;

public class Comment {
    private Integer id;
    private String userEmail;
    private String username;
    private Integer commodityId;
    private String text;
    private Date date;
    private Map<String, Integer> usersVote = new HashMap<>();

    private Integer like=0;
    private Integer dislike=0;

    public Comment(String _userEmail, Integer _commodityId, String _text, Date _date) {
        userEmail = _userEmail;
        commodityId = _commodityId;
        text = _text;
        date = _date;
    }

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

    public void addId(Integer _id) { id = _id;}
    
    public void addVote(String username, Integer vote) {
        if (usersVote == null)
            usersVote = new HashMap<>();
        if (usersVote.containsKey(username)) {
            usersVote.replace(username, vote);
        } else {
            usersVote.put(username, vote);
        }

        like = 0;
        dislike = 0;
        for (Map.Entry<String, Integer> entry : usersVote.entrySet()) {
            if (entry.getValue() > 0)
                like +=1;
            else if (entry.getValue() < 0)
                dislike += 1;
        }
    }

    public void addUsername(String _username) {username = _username;}

    public Integer getId() { return id;}

    public String getUserEmail() { return userEmail;}

    public Integer getCommodityId() { return commodityId;}

    public String getText() { return text;}

    public Date getDate() { return date;}

    public Integer getLike() {
        if(like == null)
            like = 0;
        return like;
    }

    public Integer getDislike() {
        if(dislike == null)
            dislike = 0;
        return dislike;
    }

    public String getUsername() {return username;}

}
