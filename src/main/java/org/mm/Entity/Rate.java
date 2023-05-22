package org.mm.Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.persistence.*;

@Entity
@Table(name = "rate")
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String username;
    private Integer commodityId;
    private float score;

     public Rate (String _username, Integer _commodityId, float _score) {
         username = _username;
         commodityId = _commodityId;
         score = _score;
     }

    public boolean isValidScoreRange() throws JsonProcessingException {
        if (score < 1 || score > 10)
            return false;
        else
            return true;
    }

    public boolean isValidScoreType() throws JsonProcessingException {
        if ((int) score != score)
            return false;
        else
            return true;
    }

    public boolean isValidCommand() {
        if (username==null || commodityId==null || score==0.0f)
            return false;
        else
            return true;
    }

    public String getUsername() {
        return username;
    }
    public Integer getCommodityId() {
        return commodityId;
    }
    public float getScore() {
        return score;
    }

    public void setScore(int _score) {
        score = _score;
    }

    public void setCommodityId(int _commodityId) {
        commodityId = _commodityId;
    }

    public void setUsername(String _username) {
        username = _username;
    }
}
