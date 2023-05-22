package org.mm.Entity;

import javax.persistence.*;

@Entity
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String username;
    private int vote;
    private int commentId;

    public void setUsername(String _username) {username = _username;}
    public void setVote(int score) {vote = score;}
    public int getVote() {return vote;}
    public void setCommentId(int _id){commentId = _id;}
}