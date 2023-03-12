package org.mm;

import com.fasterxml.jackson.core.JsonProcessingException;

public class Rate {
    private String username;
    private Integer commodityId;
    private float score;

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
}
