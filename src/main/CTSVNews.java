package main;

public class CTSVNews {
    private int dateid;
    private String title;
    private Long notifyTime;
    private String url;

    public void setDateid(int dateid) {
        this.dateid = dateid;
    }

    public void setNotifyTime(Long notifyTime) {
        this.notifyTime = notifyTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDateid() {
        return dateid;
    }

    public Long getNotifyTime() {
        return notifyTime;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
