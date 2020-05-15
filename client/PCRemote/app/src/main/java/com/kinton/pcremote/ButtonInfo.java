package com.kinton.pcremote;

public class ButtonInfo {
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRes_id() {
        return res_id;
    }

    public void setRes_id(int res_id) {
        this.res_id = res_id;
    }

    private String content;
    private int type;
    private int res_id;
    public final static int TYPE_IMG = 1;
    public final static int TYPE_TEXT = 0;



}
