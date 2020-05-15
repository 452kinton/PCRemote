package com.kinton.pcremote.enitity;

public class MoveEvent extends Event {
    private double angle;
    private double radio;

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getRadio() {
        return radio;
    }

    public void setRadio(double radio) {
        this.radio = radio;
    }
}
