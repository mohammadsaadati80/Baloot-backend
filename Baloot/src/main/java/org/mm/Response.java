package org.mm;

public class Response {
    private boolean isSuccess;
    private String data;

    public Response(boolean isSuccess, String data) {
        this.isSuccess = isSuccess;
        this.data = data;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }
    public String getData() {
        return data;
    }
}
