package org.mm.Baloot.Exceptions;

public class InvalidVoteScoreError extends Exception {
    public String getMessage() {
        return "Invalid vote score";
    }
}
