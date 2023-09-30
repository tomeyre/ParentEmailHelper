package com.eyre.parentemailhelper.util;

import static com.eyre.parentemailhelper.util.CommonConstants.TAPESTRY_BASE_PATH;

import com.eyre.parentemailhelper.pojo.TapestrySession;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Request {
    // the below line is for making debugging easier
    final String TAG = "JsonParser.java";
    // where the returned json data from service will be stored when downloaded
    String json = "";

    static final String COOKIES_HEADER = "Set-Cookie";
    CookieManager msCookieManager = new CookieManager();

    HttpURLConnection restConnection;

    TapestrySession tapestrySession = TapestrySession.getInstance();

    // your android activity will call this method and pass in the url of the REST service
    public String getJSONFromUrlUsingGet(String url) {

        try {
            // this code block represents/configures a connection to your REST service
            // it also represents an HTTP 'GET' request to get data from the REST service, not POST!
            URL u = new URL(url);
            restConnection = (HttpURLConnection) u.openConnection();
            restConnection.setRequestProperty("authority", "tapestryjournal.com");
            restConnection.setRequestMethod("GET");
            restConnection.setRequestProperty("path", url.substring(url.indexOf(TAPESTRY_BASE_PATH)));
            restConnection.setRequestProperty("scheme", "https");
            restConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            restConnection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            restConnection.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
            restConnection.setRequestProperty("Cache-Control", "max-age=0");
            if (tapestrySession.getCookie() != null) {
                restConnection.setRequestProperty("Cookie", tapestrySession.getCookie());
            }
            if (tapestrySession.getReferer() != null) {
                restConnection.setRequestProperty("Referer", tapestrySession.getReferer());
            }
            restConnection.setRequestProperty("Sec-Ch-Ua", "\"Chromium\";v=\"116\", \"Not)A;Brand\";v=\"24\", \"Google Chrome\";v=\"116\"");
            restConnection.setRequestProperty("Sec-Ch-Ua-Mobile", "?0");
            restConnection.setRequestProperty("Sec-Ch-Ua-Platform", "\"Windows\"");
            restConnection.setRequestProperty("Sec-Fetch-Dest", "document");
            restConnection.setRequestProperty("Sec-Fetch-Mode", "navigate");
            restConnection.setRequestProperty("Sec-Fetch-Site", "same-origin");
            restConnection.setRequestProperty("Sec-Fetch-User", "?1");
            restConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            restConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36");
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

                    tapestrySession.setCookie(restConnection.getHeaderFields().get(COOKIES_HEADER).get(0));
                    tapestrySession.setReferer(url);

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

    public String getJSONFromUrlUsingPost(String url, HashMap<String, String> postDataParams) {
        String params = getPostDataString(postDataParams);
        int postDataLength = params.length();
        try {
            // this code block represents/configures a connection to your REST service
            // it also represents an HTTP 'GET' request to get data from the REST service, not POST!
            URL u = new URL(url);
            restConnection = (HttpURLConnection) u.openConnection();
            restConnection.setRequestProperty("authority", "tapestryjournal.com");
            restConnection.setRequestMethod("POST");
            restConnection.setRequestProperty("path", url.substring(url.indexOf(TAPESTRY_BASE_PATH)));
            restConnection.setRequestProperty("scheme", "https");
            restConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            restConnection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            restConnection.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8");
            restConnection.setRequestProperty("Cache-Control", "max-age=0");
            restConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            restConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if (tapestrySession.getCookie() != null) {
                restConnection.setRequestProperty("Cookie", tapestrySession.getCookie());
            }
            restConnection.setRequestProperty("Origin", TAPESTRY_BASE_PATH);
            if (tapestrySession.getReferer() != null) {
                restConnection.setRequestProperty("Referer", tapestrySession.getReferer());
            }
            restConnection.setRequestProperty("Sec-Ch-Ua", "\"Chromium\";v=\"116\", \"Not)A;Brand\";v=\"24\", \"Google Chrome\";v=\"116\"");
            restConnection.setRequestProperty("Sec-Ch-Ua-Mobile", "?0");
            restConnection.setRequestProperty("Sec-Ch-Ua-Platform", "\"Windows\"");
            restConnection.setRequestProperty("Sec-Fetch-Dest", "document");
            restConnection.setRequestProperty("Sec-Fetch-Mode", "navigate");
            restConnection.setRequestProperty("Sec-Fetch-Site", "same-origin");
            restConnection.setRequestProperty("Sec-Fetch-User", "?1");
            restConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            restConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36");
            restConnection.setUseCaches(false);
            restConnection.setAllowUserInteraction(false);
            restConnection.setConnectTimeout(10000);
            restConnection.setReadTimeout(10000);

            OutputStream os = restConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(params);

            writer.flush();
            writer.close();
            os.close();

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

                    tapestrySession.setCookie(restConnection.getHeaderFields().get(COOKIES_HEADER).get(0));
                    tapestrySession.setReferer(url);

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

    private String getPostDataString(HashMap<String, String> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return result.toString();
    }
}