package com.example.qrbarcodescanner.dto;

import java.io.Serializable;

/**
 * Created by ntf-19 on 21/7/18.
 */

public class ScanData implements Serializable

{
    int id;
    String dataFormat,content,date_time;

    public ScanData(int id, String dataFormat, String content, String date_time) {
        this.id = id;
        this.dataFormat = dataFormat;
        this.content = content;
        this.date_time = date_time;
    }

    public ScanData() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
