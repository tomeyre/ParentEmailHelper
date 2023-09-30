package com.eyre.parentemailhelper.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CalenderEvent {

    private String url;
    private String content;
    private LocalDate datePlanned;
    private LocalDateTime dateApproved;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDatePlanned() {
        return datePlanned;
    }

    public void setDatePlanned(LocalDate datePlanned) {
        this.datePlanned = datePlanned;
    }

    public LocalDateTime getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(LocalDateTime dateApproved) {
        this.dateApproved = dateApproved;
    }
}
