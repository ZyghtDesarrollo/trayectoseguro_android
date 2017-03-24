package com.zyght.trayectoseguro.handler;


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

public class AddTravelAPIHandler extends APIResourceHandler {
    private String answers;
    private String travel_logs;


    public AddTravelAPIHandler(String answers, String travel_logs) {
        this.answers = answers;
        this.travel_logs = travel_logs;

    }




    @Override
    public List<NameValuePair> getValueParams() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("answers", answers));
        nameValuePairs.add(new BasicNameValuePair("travel_logs", travel_logs));

        return nameValuePairs;
    }

    @Override
    public void handlerAPIResponse(APIResponse apiResponse) {

        if (apiResponse.getStatus().isSuccess()) {

            getResponseActionDelegate().didSuccessfully(apiResponse.getRawResponse());
        } else {
            getResponseActionDelegate().didNotSuccessfully(apiResponse.getRawResponse());
        }

    }




    @Override
    public String getServiceURL() {
        //return ResourcesConstants.BASE_URL + "/token";

        return "http://trayectoseguro.azurewebsites.net/index.php/api/rtravel/add";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
