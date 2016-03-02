package com.upchina.financialnews.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class News implements Serializable{

    @SerializedName("aId")
    private String aId;

    @SerializedName("Id")
    private String id;

    @SerializedName("Subject")
    private String subject;

    @SerializedName("Intro")
    private String intro;

    @SerializedName("Thumbnail")
    private String thumbnail;

    @SerializedName("Comment_Count")
    private String commentCount;

    @SerializedName("Publish_Time")
    private String publishTime;

    @SerializedName("User_Name")
    private String userName;

    @SerializedName("Is_Bull")
    private String isBull;

    @SerializedName("O_Name")
    private String oName;

    @SerializedName("O_Url")
    private String oUrl;

    @SerializedName("Priority")
    private String priority;

    @SerializedName("Module_Id")
    private String moduleId;

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIsBull() {
        return isBull;
    }

    public void setIsBull(String isBull) {
        this.isBull = isBull;
    }

    public String getoName() {
        return oName;
    }

    public void setoName(String oName) {
        this.oName = oName;
    }

    public String getoUrl() {
        return oUrl;
    }

    public void setoUrl(String oUrl) {
        this.oUrl = oUrl;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
}
