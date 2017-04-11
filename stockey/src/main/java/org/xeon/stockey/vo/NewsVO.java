package org.xeon.stockey.vo;

/**
 * Created by nians on 2016/6/18.
 */
public class NewsVO {
    public String time;
    public String brief;
    public String link;

    public NewsVO() {

    }


    public NewsVO(String time, String brief, String link) {
        this.time = time;
        this.brief = brief;
        this.link = link;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        String result = "";
        result += "time : "+time;
        result += " brief : "+brief;
        result += " link : "+link;
        return result;
    }
}
