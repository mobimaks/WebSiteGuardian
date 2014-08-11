package com.example.WebSit_eGuardian.net;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class HandleHTTP {

    private String url;
    private HttpResponse response = null;

    public HandleHTTP(String url){
        this.url = url;
    }

    public int getServerStatus(){
        makeRequest();
        if (response != null){
            return response.getStatusLine().getStatusCode();
        } else {
            return 500;
        }
    }

    public HttpResponse getResponse() {
        return response;
    }

    private void makeRequest(){
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            response = httpClient.execute(httpGet);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException  e){
            e.printStackTrace();
        }

    }

}
