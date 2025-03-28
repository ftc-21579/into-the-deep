package org.firstinspires.ftc.teamcode.common.util;
public abstract class MotionProfile {
    protected double ti;
    protected MotionState i;
    protected double tf;
    public double ti() {
        return ti;
    }
    public double tf() {
        return tf;
    }
    public abstract MotionState state(double t);
}