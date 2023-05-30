package org.mm.Exceptions;

public class CommentNotFoundError extends Exception {
    public String getMessage() {
        return "Comment not found";
    }
}

