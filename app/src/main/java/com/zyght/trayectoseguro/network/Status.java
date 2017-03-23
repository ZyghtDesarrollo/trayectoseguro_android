/*******************************************************************************
 * Copyright (c) 2014. Zyght
 * All rights reserved. 
 *
 ******************************************************************************/

package com.zyght.trayectoseguro.network;


/**
 * Created by Arley Mauricio Duarte
 */
public class Status {

    private int statusCode;
    private String error = "";
    private String errorCode;

    public boolean isSuccess() {
        boolean success = false;
        //Success HTTP Codes
        if (statusCode >= 200 && statusCode < 300) {
            success = true;
        }

        return success;
    }



    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
