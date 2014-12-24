package com.mrane.database;

import android.provider.BaseColumns;

public final class VnbContract {
    /**
     * Empty constructor to handle accidental instantiation
     */
    public VnbContract() {}

    /**
     * Defining type constants
     */
    private static final String COMMA_SEP = ",";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String DATETIME_TYPE = " DATETIME";

    /**
     * Class to define constants for notices table
     */
    public static abstract class Notice implements BaseColumns {
        public static final String TABLE_NAME = "notices";
        public static final String ID = "_id";
        public static final String COL_DATA = "data";
        public static final String COL_IS_EVENT = "is_event";
        public static final String COL_USER_ID = "user_id";
        public static final String COL_POSITION_ID = "position_id";
        public static final String COL_CREATED = "created";
        public static final String COL_MODIFIED = "modified";
        public static final String COL_PARENT = "parent";
        public static final String COL_VISIBLE = "visible";
        public static final String COL_COMMENT_COUNT = "comment_count";
        public static final String COL_LIKE_COUNT = "like_count";
        public static final String COL_START_TIME = "start_time";
        public static final String COL_END_TIME = "end_time";
        public static final String COL_PUBLIC = "public";
        public static final String COL_CORNERS = "corners";
        public static final String COL_UPDATES_BELOW = "updates_below";
        public static final String COL_UPDATES_ABOVE = "updates_above";
        public static final String COL_VENUE_ID = "venue_id";
    }
}
