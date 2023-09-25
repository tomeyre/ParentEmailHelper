package com.eyre.parentemailhelper.pojo;

import java.util.ArrayList;
import java.util.List;

public class TapestrySession {

    private static TapestrySession INSTANCE;
    private TapestrySession() {
    }

    public static TapestrySession getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new TapestrySession();
        }

        return INSTANCE;
    }

    private String cookie;
    private String referer;
    private List<Child> children = new ArrayList<>();

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

    public void addChildren(Child child) {
        children.add(child);
    }
}
