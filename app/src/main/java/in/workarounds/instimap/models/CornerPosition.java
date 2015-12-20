package in.workarounds.instimap.models;

import com.google.gson.annotations.SerializedName;

public class CornerPosition extends ExtendedSugarRecord<CornerPosition> {

    @SerializedName("id")
    long dbId;
    @SerializedName("board_id")
    long boardId;
    @SerializedName("corner_id")
    long cornerId;

    public boolean isChild() {
        return isChild;
    }

    public void setChild(boolean isChild) {
        this.isChild = isChild;
    }

    public long getCornerId() {
        return cornerId;
    }

    public void setCornerId(long cornerId) {
        this.cornerId = cornerId;
    }

    public long getBoardId() {
        return boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }

    @SerializedName("is_child")
    boolean isChild;

    public CornerPosition() {}
}
