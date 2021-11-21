//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.media.weex.weex_video.ijkplayer.danmaku;

public abstract class BaseDanmakuData {
    private int type;
    private String content;
    private long time;
    private float textSize;
    private int textColor;

    public BaseDanmakuData() {
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getTextSize() {
        return this.textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public String toString() {
        return "BaseDanmakuData{type=" + this.type + ", content='" + this.content + '\'' + ", time=" + this.time + ", textSize=" + this.textSize + ", textColor=" + this.textColor + '}';
    }
}
