package in.workarounds.instimap.models;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Filter extends ExtendedSugarRecord<Filter> {

    @SerializedName("id")
    long dbId;
    @SerializedName("notice_id")
    long NoticeId;
    @SerializedName("corner_id")
    long CornerId;
    @SerializedName("is_pinned")
    boolean isPinned;
    @SerializedName("is_official")
    boolean isOfficial;
    @SerializedName("notice_created")
    Date noticeCreated;
    @SerializedName("notice_modified")
    Date noticeModified;
    @SerializedName("notice_start_time")
    Date noticeStartTime;
    @SerializedName("notice_end_time")
    Date noticeEndTime;
    Date created;
    Date modified;

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    public long getNoticeId() {
        return NoticeId;
    }

    public void setNoticeId(long noticeId) {
        NoticeId = noticeId;
    }

    public long getCornerId() {
        return CornerId;
    }

    public void setCornerId(long cornerId) {
        CornerId = cornerId;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean isPinned) {
        this.isPinned = isPinned;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean isOfficial) {
        this.isOfficial = isOfficial;
    }

    public Date getNoticeCreated() {
        return noticeCreated;
    }

    public void setNoticeCreated(Date noticeCreated) {
        this.noticeCreated = noticeCreated;
    }

    public Date getNoticeModified() {
        return noticeModified;
    }

    public void setNoticeModified(Date noticeModified) {
        this.noticeModified = noticeModified;
    }

    public Date getNoticeStartTime() {
        return noticeStartTime;
    }

    public void setNoticeStartTime(Date noticeStartTime) {
        this.noticeStartTime = noticeStartTime;
    }

    public Date getNoticeEndTime() {
        return noticeEndTime;
    }

    public void setNoticeEndTime(Date noticeEndTime) {
        this.noticeEndTime = noticeEndTime;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Filter() {}
}
