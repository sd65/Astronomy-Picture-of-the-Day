package com.doignon.sylvain.apodnasa;

import java.util.Date;


class ImageInfo {
    private String title;
    private String url;
    private Date date;
    private String sDate;

    ImageInfo(String title, String url, Date date, String sDate) {
        this.title = title;
        this.url = url;
        this.date = date;
        this.sDate = sDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    Date getDate() {
        return date;
    }

    String getsDate() {
        return sDate;
    }

}
