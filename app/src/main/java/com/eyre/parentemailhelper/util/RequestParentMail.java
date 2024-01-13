package com.eyre.parentemailhelper.util;

import static com.eyre.parentemailhelper.pojo.ParentMailSession.getSession;

import com.eyre.parentemailhelper.pojo.ParentMailSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestParentMail {
    String json = "";

    HttpURLConnection restConnection;

    ParentMailSession parentMailSession = getSession();

    public String getJSONFromUrlUsingGet(String url) {

        try {
            // this code block represents/configures a connection to your REST service
            // it also represents an HTTP 'GET' request to get data from the REST service, not POST!
            URL u = new URL(url);
            restConnection = (HttpURLConnection) u.openConnection();
            parentMailSession.getCurrentHeaders()
                    .forEach((key, value) -> restConnection.setRequestProperty(key, value));
            restConnection.setRequestProperty("Cookie", parentMailSession.getCookies());
            restConnection.setUseCaches(false);
            restConnection.setAllowUserInteraction(false);
            restConnection.setConnectTimeout(300000);
            restConnection.setReadTimeout(300000);
            restConnection.connect();
            int status = restConnection.getResponseCode();

            // switch statement to catch HTTP 200 and 201 errors

            switch (status) {
                case 200:
                case 201:
                    // live connection to your REST service is established here using getInputStream() method
                    BufferedReader br = new BufferedReader(new InputStreamReader(restConnection.getInputStream()));

                    // create a new string builder to store json data returned from the REST service
                    StringBuilder sb = new StringBuilder();
                    String line;

                    // loop through returned data line by line and append to stringbuilder 'sb' variable
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    // remember, you are storing the json as a stringy
                    try {
                        json = sb.toString();
                    } catch (Exception e) {
                    }
                    restConnection.disconnect();
                    // return JSON String containing data to Tweet activity (or whatever your activity is called!)
                    return json;
            }
            // HTTP 200 and 201 error handling from switch statement
        } catch (MalformedURLException ex) {
            return "error";
        } catch (IOException ex) {
            return "error";
        }
        return null;
    }
}