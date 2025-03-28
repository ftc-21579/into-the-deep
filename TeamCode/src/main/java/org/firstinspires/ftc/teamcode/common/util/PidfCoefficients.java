package org.firstinspires.ftc.teamcode.common.util;
import androidx.annotation.NonNull;

import java.util.function.ToDoubleFunction;
public class PidfCoefficients {
    public final double kp;
    public final double ki;
    public final double kd;
    public final ToDoubleFunction<Object[]> kf;
    public PidfCoefficients(double kp, double ki, double kd, ToDoubleFunction<Object[]> kf) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.kf = kf;
    }
    public PidfCoefficients(double kp, double ki, double kd) {
        this(kp, ki, kd, x -> 0);
    }

    public PidfCoefficients getPidfCoefficients() {
        return new PidfCoefficients(kp, ki, kd, kf);
    }

    @NonNull
    public String toString() {
        return "kp: " + kp + ", ki: " + ki + ", kd: " + kd;
    }
}