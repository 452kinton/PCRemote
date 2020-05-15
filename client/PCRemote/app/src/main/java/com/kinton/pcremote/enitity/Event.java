package com.kinton.pcremote.enitity;

public class Event {
    private int type;
    public static final int MOUSE_MOVE_EVENT = 0;
    public static final int KEY_EVENT = 0;
    public static final int CLICK_EVENT = 0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
