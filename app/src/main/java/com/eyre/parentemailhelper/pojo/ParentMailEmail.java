package com.eyre.parentemailhelper.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParentMailEmail implements Serializable {

    @JsonProperty(value = "archived")
    private int archived;
    @JsonProperty(value = "attachments")
    private List<Attachment> attachments = new ArrayList<>();
    private long authorId;
    @JsonProperty(value = "authorImg")
    private String authorImgUrl;
    @JsonProperty(value = "authorName")
    private String authorName;
    @JsonProperty(value = "createdTs")
    private String createdTs;
    @JsonProperty(value = "msg")
    private String msg;
    @JsonProperty(value = "newItem")
    private boolean newItem;
    @JsonProperty(value = "read")
    private int read;
    @JsonProperty(value = "regarding")
    private String regarding;
    @JsonProperty(value = "starred")
    private int starred;
    @JsonProperty(value = "subject")
    private String subject;
    @JsonProperty(value = "summary")
    private String summary;
    @JsonProperty(value = "type")
    private String type;
    @JsonProperty(value = "updatedTs")
    private String updatedTs;
    @JsonProperty(value = "_id")
    private String id;

    public int getArchived() {
        return archived;
    }

    public void setArchived(int archived) {
        this.archived = archived;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorImgUrl() {
        return authorImgUrl;
    }

    public void setAuthorImgUrl(String authorImgUrl) {
        this.authorImgUrl = authorImgUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCreatedTs() {
        return createdTs;
    }

    public void setCreatedTs(String createdTs) {
        this.createdTs = createdTs;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isNewItem() {
        return newItem;
    }

    public void setNewItem(boolean newItem) {
        this.newItem = newItem;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public String getRegarding() {
        return regarding;
    }

    public void setRegarding(String regarding) {
        this.regarding = regarding;
    }

    public int getStarred() {
        return starred;
    }

    public void setStarred(int starred) {
        this.starred = starred;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdatedTs() {
        return updatedTs;
    }

    public void setUpdatedTs(String updatedTs) {
        this.updatedTs = updatedTs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
