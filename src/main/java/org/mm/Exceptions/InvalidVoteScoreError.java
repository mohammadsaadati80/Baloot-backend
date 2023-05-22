package org.mm.Exceptions;

public class InvalidVoteScoreError extends Exception {
    public String getMessage() {
        return "Invalid vote score";
    }
}
