package org.firstinspires.ftc.teamcode.common;

@com.acmerobotics.dashboard.config.Config
public class Config {
    public static double pivot_kP = 0.04, pivot_kI = 0.0, pivot_kD = 0.0025, pivot_tolerance = 5.0;

    public static double pivot_Kgs = 0.2, pivot_Kgd = 0.005, pivot_kS = 0.08, pivot_kV = 0.0042, pivot_kA = 0.0005;

    public static double pivot_Vm = 200, pivot_Ai = 10000, pivot_Af = 500;

    public static double extension_kP = 0.05, extension_kI = 0.0, extension_kD = 0.002, extension_tolerance = 5.0;

    public static double extension_Kgs = 0.1, extension_Kgd = 0.0, extension_Ks = 0.1, extension_Kv = 0.003, extension_Ka = 0.0;

        public static double extension_Vm = 200, extension_Ai = 10000, extension_Af = 5000;

    public static double ascent_kP = 0.01, ascent_kI = 0.0, ascent_kD = 0.0, ascent_kF = 0.0, ascent_tolerance = 10.0;

    public static double ext_increment = 2.0, pivot_increment = 10.0;

    public static double wristAngleIncrement = 45, wristTwistIncrement = 45;
}