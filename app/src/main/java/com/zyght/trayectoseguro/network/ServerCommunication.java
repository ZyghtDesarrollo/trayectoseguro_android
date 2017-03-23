/*******************************************************************************
 * Copyright (c) 2014. Zyght
 * All rights reserved. 
 *
 ******************************************************************************/
package com.zyght.trayectoseguro.network;

import android.util.Log;


import com.zyght.trayectoseguro.session.Session;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * @author Arley Mauricio Duarte Palomino
 */
public class ServerCommunication {

    private static final String TAG = ServerCommunication.class.getSimpleName();
    private static final int UPLOAD_TIME_OUT =  500000;
    private String URL;
    private boolean isNeedSession = false;


    public ServerCommunication(String url, boolean isNeedSession) {
        this.isNeedSession = isNeedSession;
        this.URL = url;
    }

    protected APIResponse getAPIResponse(List<NameValuePair> nameValuePairs, HttpMethod method) {
        APIResponse apiResponse = new APIResponse();
        String rawResponse = "";
        try {

            Log.d(TAG, "Request: " + URL);
            URI uri = new URI(URL);

            HttpClient client = getNewHttpClient();

            HttpRequestBase httpClient = new HttpPost(uri);
            if (method == HttpMethod.GET) {
                httpClient = new HttpGet(uri);
            } else if (method == HttpMethod.POST) {
                httpClient = new HttpPost(uri);
                ((HttpPost) httpClient).setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            }


            httpClient.setHeader("Accept-Language", "es-es");
            httpClient.setHeader("Pragma", "no-cache");
            httpClient.setHeader("Connection", "keep-alive");
            httpClient.setHeader("Proxy-Connection", "keep-alive");

            //New
            httpClient.addHeader("Accept-Encoding", "gzip");

            if (isNeedSession) {
                Session session = Session.getInstance();
                httpClient.addHeader("Authorization", "Bearer " + session.getAccessToken());
            }


            HttpResponse response = client.execute(httpClient);

            InputStream inputStream = response.getEntity().getContent();
            Header contentEncoding = response.getFirstHeader("Content-Encoding");
            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                inputStream = new GZIPInputStream(inputStream);
            }


            rawResponse = convertStreamToString(inputStream);

            Log.d(TAG, "Raw RESPONSE : " + rawResponse);
            apiResponse.getStatus().setStatusCode(response.getStatusLine().getStatusCode());
            apiResponse.setRawResponse(rawResponse);

        } catch (URISyntaxException e) {
           // Crashlytics.logException(e);
        } catch (UnsupportedEncodingException e) {

        } catch (ClientProtocolException e) {

        } catch (IOException e) {
            Log.d(TAG, "Error IO Exception");

            apiResponse.getStatus().setErrorCode(APIErrors.CONNECTION_ERROR);
        }

        return apiResponse;
    }


    public HttpClient getNewHttpClient() {

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = null;

            sf = SSLSocketFactory.getSocketFactory();
            sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpConnectionParams.setSoTimeout(params, UPLOAD_TIME_OUT);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }

    }


    //-----------------------------------------------------------

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}