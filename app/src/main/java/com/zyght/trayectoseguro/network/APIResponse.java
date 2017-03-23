package com.zyght.trayectoseguro.network;


public class APIResponse {
    private Status status = new Status();
    private String rawResponse ="";

    public Status getStatus() {
        return status;
    }


    public String getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }
}
