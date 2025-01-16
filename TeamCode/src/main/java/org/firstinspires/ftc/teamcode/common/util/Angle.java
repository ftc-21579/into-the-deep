package org.firstinspires.ftc.teamcode.common.util;

public class Angle {
    private static final double TAU = 2 * Math.PI;

    /**
     * Returns {@code angle} clamped to {@code [0, 2pi]}.
     *
     * @param angle angle measure in radians
     */
    public static double norm(double angle) {
        double modifiedAngle = angle % TAU;

        modifiedAngle = (modifiedAngle + TAU) % TAU;

        return modifiedAngle;
    }

    /**
     * Returns {@code angleDelta} clamped to {@code [-pi, pi]}.
     *
     * @param angleDelta angle delta in radians
     */
    public static double normDelta(double angleDelta) {
        double modifiedAngleDelta = norm(angleDelta + TAU);

        if (modifiedAngleDelta > Math.PI) {
            modifiedAngleDelta -= TAU;
        }

        return modifiedAngleDelta;
    }
}