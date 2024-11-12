package org.firstinspires.ftc.teamcode.common;

@com.acmerobotics.dashboard.config.Config
public class Config {
    public static double pivot_kP = 0.03, pivot_kI = 0.0, pivot_kD = 0.001, pivot_kF = 0.0025, pivot_tolerance = 10.0;

    public static double extension_kP = 0.025, extension_kI = 0.0, extension_kD = 0.0, extension_kF = 0.0, extension_tolerance = 10.0;

    public static double ext_increment = 2.0, pivot_increment = 2.0;

    public static double wristAngleIncrement = 45, wristTwistIncrement = 45;
}