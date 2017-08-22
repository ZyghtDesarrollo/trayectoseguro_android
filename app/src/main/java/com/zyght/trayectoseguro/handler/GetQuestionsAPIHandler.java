package com.zyght.trayectoseguro.handler;


import android.util.Log;

import com.google.gson.Gson;
import com.zyght.trayectoseguro.config.ResourcesConstants;
import com.zyght.trayectoseguro.entity.Question;
import com.zyght.trayectoseguro.entity.QuestionBLL;
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

public class GetQuestionsAPIHandler extends APIResourceHandler {


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

        QuestionBLL questionBLL = QuestionBLL.getInstance();
        questionBLL.clear();

        try {
            JSONObject object = new JSONObject(apiResponse);
            token = object.getString("response");


            Gson gson = new Gson();

            Question[] arr = gson.fromJson(token, Question[].class);

            for(int i=0; i< arr.length; i++){
                Question q = arr[i];
                questionBLL.add(q);

            }

            Log.d(TAG, arr.toString());



        } catch (JSONException e) {

        } finally {

        }


    }

    @Override
    public String getServiceURL() {
        return ResourcesConstants.BASE_URL + "/rquestion/list_actives";


    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }
}
