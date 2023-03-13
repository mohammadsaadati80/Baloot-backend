package org.mm.Baloot.Exceptions;

public class InvalidRateScoreError extends Exception {
    public String getMessage() {
        return "The score must be an integer between 1 and 10";
    }
}
