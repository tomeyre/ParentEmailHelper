package com.eyre.parentemailhelper.pojo;

import java.util.Map;

public class ParentMailSession {

    private static ParentMailSession parentMailSession;
    public static ParentMailSession getSession(){
        if(parentMailSession == null){
            parentMailSession = new ParentMailSession();
        }
        return parentMailSession;
    }

    private Map<String, String> currentHeaders;

    private String cookies;

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public Map<String, String> getCurrentHeaders() {
        return currentHeaders;
    }

    public void setCurrentHeaders(Map<String, String> currentHeaders) {
        this.currentHeaders = currentHeaders;
    }
}
