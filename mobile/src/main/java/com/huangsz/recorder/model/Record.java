package com.huangsz.recorder.model;

import android.provider.BaseColumns;

public class Record {

    private long id;

    private String name;

    private String desc;

    /**
     * the format of the data is {"date": "time"} or other json
     * like text
     */
    private String data;

    private String createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public static class Entry implements BaseColumns {

        public static final String TABLE = "Record";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_DESC = "desc";

        public static final String COLUMN_DATA = "data";

        public static final String COLUMN_CREATE_TIME = "createTime";

        public static String[] projection() {
            return new String[] {_ID, COLUMN_NAME, COLUMN_DESC, COLUMN_DATA,
                    COLUMN_CREATE_TIME};
        }
    }
}
