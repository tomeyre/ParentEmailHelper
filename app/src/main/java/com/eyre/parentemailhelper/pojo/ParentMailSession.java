package com.eyre.parentemailhelper.pojo;

import java.util.ArrayList;
import java.util.List;

public class ParentMailSession {

    private static ParentMailSession INSTANCE;
    private ParentMailSession() {
    }

    public static ParentMailSession getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ParentMailSession();
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
