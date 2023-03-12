package org.mm;

public class Response {
    private boolean success;
    private String data;

    public Response(boolean success, String data) {
        this.success = success;
        this.data = data;
    }

    public boolean getSuccess() {
        return success;
    }
    public String getData() {
        return data;
    }
}
