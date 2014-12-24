package com.mrane.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Board extends ExtendedSugarRecord<Board> {

    @SerializedName("id")
    long dbId;

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public long getCornerId() {
        return cornerId;
    }

    public void setCornerId(long cornerId) {
        this.cornerId = cornerId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    String title;
    String header;
    @SerializedName("user_id")
    long userId;
    Date created;
    @SerializedName("corner_id")
    long cornerId;
    String tag;

    public Board() {}
}
