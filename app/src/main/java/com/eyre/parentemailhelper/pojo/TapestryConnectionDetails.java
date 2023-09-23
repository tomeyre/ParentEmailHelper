package com.eyre.parentemailhelper.pojo;

import java.util.List;

public class TapestryConnectionDetails {

    private static TapestryConnectionDetails INSTANCE;
    private TapestryConnectionDetails() {
    }

    public static TapestryConnectionDetails getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new TapestryConnectionDetails();
        }

        return INSTANCE;
    }

    private String cookie;
    private String referer;
    private List<Child> children;

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }
}
