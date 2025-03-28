package org.firstinspires.ftc.teamcode.common.util;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.ServoControllerEx;

import java.lang.reflect.Field;

public class BlinkinUtil {

    private static final double PULSE_WIDTH_INCREMENTOR = 0.0005;
    private static final double BASE_SERVO_POSITION = 505 * PULSE_WIDTH_INCREMENTOR;
    private static final int PATTERN_OFFSET = 10;

    private static RevBlinkinLedDriver blinkinDriver;

    public static void setBlinkinDriver(RevBlinkinLedDriver driver) {
        blinkinDriver = driver;
    }

    public static RevBlinkinLedDriver.BlinkinPattern getPattern() {
        if (blinkinDriver == null) {
            throw new IllegalStateException("BlinkinDriver is not set");
        }
        try {
            Field controllerField = RevBlinkinLedDriver.class.getDeclaredField("controller");
            controllerField.setAccessible(true);
            ServoControllerEx controller = (ServoControllerEx) controllerField.get(blinkinDriver);
            int port = Integer.parseInt(blinkinDriver.getConnectionInfo().split("port ")[1]);
            double servoPosition = controller.getServoPosition(port);
            double patternIndex = ((servoPosition - BASE_SERVO_POSITION) / (PATTERN_OFFSET * PULSE_WIDTH_INCREMENTOR));
            int intPatternIndex = (int) Math.round(patternIndex);
            return RevBlinkinLedDriver.BlinkinPattern.fromNumber(intPatternIndex);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to access controller field", e);
        }
    }
}