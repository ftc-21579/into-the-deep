package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.Bot;

@Config
public class Wrist extends SubsystemBase {

    private final Bot bot;

    private final Servo left, right;

    private double twistTarget = 0.0;
    private double angleTarget = 0.5;

    private final double twistRatio = 18.0 / 52.0;

    private static final double SERVO_RANGE_DEGREES = 270.0;

    public Wrist(Bot bot) {
        this.bot = bot;

        left = bot.hMap.get(Servo.class, "leftWrist");
        right = bot.hMap.get(Servo.class, "rightWrist");
        //right.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        updateServoPositions();
    }

    // Set target twist in degrees (0 to 180)
    public void setTwist(double targetTwistDegrees) {
        this.twistTarget = degreesToServoPosition(targetTwistDegrees);
        updateServoPositions();
    }

    // Set target angle in degrees (0 to 180)
    public void setAngle(double targetAngleDegrees) {
        this.angleTarget = degreesToServoPosition(targetAngleDegrees);
        updateServoPositions();
    }

    // Increment twist by a certain amount in degrees
    public void incrementTwist(double deltaTwistDegrees) {
        double newTwistDegrees = getTwistDegrees() + deltaTwistDegrees;
        setTwist(clampTwistDegrees(newTwistDegrees));
    }

    // Increment angle by a certain amount in degrees
    public void incrementAngle(double deltaAngleDegrees) {
        double newAngleDegrees = getAngleDegrees() + deltaAngleDegrees;
        setAngle(clampAngleDegrees(newAngleDegrees));
    }

    // Get the current twist angle in degrees
    public double getTwistDegrees() {
        return servoPositionToDegrees(twistTarget);
    }

    // Get the current angle in degrees
    public double getAngleDegrees() {
        return servoPositionToDegrees(angleTarget);
    }

    // Convert degrees to servo position (0.0 to 1.0)
    private double degreesToServoPosition(double degrees) {
        //return clampServoPosition(degrees / SERVO_RANGE_DEGREES);
        return degrees / SERVO_RANGE_DEGREES;
    }

    // Convert servo position (0.0 to 1.0) to degrees
    private double servoPositionToDegrees(double position) {
        return position * SERVO_RANGE_DEGREES;
    }

    // Clamp servo position between 0.0 and 1.0
    private double clampServoPosition(double position) {
        return Math.max(0.0, Math.min(1.0, position));
    }

    // Clamp degrees between 0 and the maximum range of motion
    private double clampAngleDegrees(double degrees) {
        return Math.max(0.0, Math.min(SERVO_RANGE_DEGREES, degrees));
    }

    private double clampTwistDegrees(double degrees) {
        return Math.max(-90.0, Math.min(90.0, degrees));
    }

    // Update servo positions based on current twist and angle targets
    private void updateServoPositions() {
        // The servo positions are calculated based on twist and angle targets.
        // Servo1 controls the sum of twist and angle, and Servo2 controls the difference.
        //double leftPosition = clampServoPosition((twistTarget + angleTarget) / 2);
        //double rightPosition = clampServoPosition((twistTarget - angleTarget) / 2);

        double leftPosition = clampServoPosition(angleTarget + (twistTarget * twistRatio));
        double rightPosition = clampServoPosition(angleTarget - (twistTarget * twistRatio));

        //double leftPosition = angleTarget + twistTarget;
        //double rightPosition = angleTarget - twistTarget;

        bot.telem.addData("Left Wrist Position", leftPosition);
        bot.telem.addData("Right Wrist Position", rightPosition);

        bot.telem.addData("Angle Target", angleTarget);
        bot.telem.addData("Twist Target", twistTarget);

        bot.telem.addData("Angle Target Degrees", getAngleDegrees());
        bot.telem.addData("Twist Target Degrees", getTwistDegrees());

        // Set the servo positions
        left.setPosition(leftPosition);
        right.setPosition(rightPosition);
    }

}
