package com.zyght.trayectoseguro.handler;


import android.util.Log;

import com.google.gson.Gson;
import com.zyght.trayectoseguro.config.ResourcesConstants;
import com.zyght.trayectoseguro.entity.Question;
import com.zyght.trayectoseguro.entity.QuestionBLL;
import com.zyght.trayectoseguro.entity.TravelItem;
import com.zyght.trayectoseguro.entity.TravelItemBLL;
import com.zyght.trayectoseguro.network.APIResourceHandler;
import com.zyght.trayectoseguro.network.APIResponse;
import com.zyght.trayectoseguro.network.HttpMethod;
import com.zyght.trayectoseguro.session.Session;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Arley Mauricio Duarte on 3/24/17.
 */

public class GetTravelsAPIHandler extends APIResourceHandler {


    private static final String TAG = "GetQuestionsAPIHandler" ;

    @Override
    public void handlerAPIResponse(APIResponse apiResponse) {

        if (apiResponse.getStatus().isSuccess()) {
            extract(apiResponse.getRawResponse());
            getResponseActionDelegate().didSuccessfully("");
        } else {
            getResponseActionDelegate().didNotSuccessfully(apiResponse.getStatus().getErrorCode());
        }

    }


    private void extract(String apiResponse) {

        String token = "";

        TravelItemBLL travelItemBLL = TravelItemBLL.getInstance();
        travelItemBLL.clear();

        try {
            JSONObject object = new JSONObject(apiResponse);
            token = object.getString("response");


            Gson gson = new Gson();

            TravelItem[] arr = gson.fromJson(token, TravelItem[].class);

            for(int i=0; i< arr.length; i++){
                TravelItem q = arr[i];
                travelItemBLL.add(q);

            }

            Log.d(TAG, arr.toString());



        } catch (JSONException e) {

        } finally {

        }


    }

    @Override
    public String getServiceURL() {
        return ResourcesConstants.BASE_URL + "rtravel/list?user_id="+ Session.getInstance().getUser().getId();

       // return "http://trayectoseguro.azurewebsites.net/index.php/api/rtravel/list?user_id="+ Session.getInstance().getUser().getId();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }
}
