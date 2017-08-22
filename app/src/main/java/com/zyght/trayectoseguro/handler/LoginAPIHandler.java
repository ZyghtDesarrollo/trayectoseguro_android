package com.zyght.trayectoseguro.handler;


import com.google.gson.Gson;
import com.zyght.trayectoseguro.config.ResourcesConstants;
import com.zyght.trayectoseguro.entity.User;
import com.zyght.trayectoseguro.network.APIResourceHandler;
import com.zyght.trayectoseguro.network.APIResponse;
import com.zyght.trayectoseguro.network.HttpMethod;
import com.zyght.trayectoseguro.session.Session;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arley Mauricio Duarte on 3/24/17.
 */

public class LoginAPIHandler extends APIResourceHandler {
    private String userName;
    private String password;
    private String code;

    public LoginAPIHandler(String userName, String password, String code) {
        this.userName = userName;
        this.password = password;
        this.code = code;
    }


    public boolean isNeedAuthToken() {
        return false;
    }

    @Override
    public List<NameValuePair> getValueParams() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("username", userName));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        nameValuePairs.add(new BasicNameValuePair("code", code));
        return nameValuePairs;
    }

    @Override
    public void handlerAPIResponse(APIResponse apiResponse) {

        if (apiResponse.getStatus().isSuccess()) {
            extractToken(apiResponse.getRawResponse());
            GetQuestionsAPIHandler getQuestionsAPIHandler = new GetQuestionsAPIHandler();
            getQuestionsAPIHandler.setRequestHandle(getResponseActionDelegate(), getContext());
        } else {
            getResponseActionDelegate().didNotSuccessfully(apiResponse.getStatus().getErrorCode());
        }

    }


    public void extractToken(String apiResponse) {

        String token = "";

        try {
            JSONObject object = new JSONObject(apiResponse);
            token = object.getString("access_token");


            Gson gson = new Gson();

            User user = gson.fromJson(object.getString("user"), User.class);
            Session.getInstance().setUser(user);

        } catch (JSONException e) {

        } finally {
            Session.getInstance().setAccessToken(token, getContext());
        }


    }

    @Override
    public String getServiceURL() {
        return ResourcesConstants.BASE_URL + "/ruser/login";

        //return "http://trayectoseguro.azurewebsites.net/index.php/api/ruser/login";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
