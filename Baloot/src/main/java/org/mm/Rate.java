package org.mm;

import com.fasterxml.jackson.core.JsonProcessingException;

public class Rate {
    private String username;
    private Integer commodityId;
    private float score;

    public boolean isValidScore() throws JsonProcessingException {
        if (((int) score != score) || (score < 1 || score > 10)) {
            return false;
        }
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
