package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.Bot;

@Config
public class Wrist extends SubsystemBase {

    private final Bot bot;

    private final Servo left, right;

    public static double increment = 0.1;

    public Wrist(Bot bot) {
        this.bot = bot;

        left = bot.hMap.get(Servo.class, "leftWrist");
        right = bot.hMap.get(Servo.class, "rightWrist");
    }

    public void incrementTwist() {
        double currentTwist = getTwist();
        setTwist(Math.min(1.0, currentTwist + increment));
    }

    public void decrementTwist() {
        double currentTwist = getTwist();
        setTwist(Math.max(0.0, currentTwist - increment));
    }

    public void incrementAngle() {
        double currentAngle = getAngle();
        setAngle(Math.min(1.0, currentAngle + increment));
    }

    public void decrementAngle() {
        double currentAngle = getAngle();
        setAngle(Math.max(0.0, currentAngle - increment));
    }

    public double getTwist() {
        double servo1Position = left.getPosition();
        double servo2Position = right.getPosition();
        return servo1Position - servo2Position;
    }

    public double getAngle() {
        double servo1Position = left.getPosition();
        double servo2Position = right.getPosition();
        return (servo1Position + servo2Position) / 2;
    }

    public void setTwist(double twist) {
        // Ensure twist is within the valid range
        twist = Math.max(0.0, Math.min(1.0, twist));

        // Calculate individual servo positions based on twist
        double servo1Position = 0.5 + twist / 2;
        double servo2Position = 0.5 - twist / 2;

        // Set servo positions
        left.setPosition(servo1Position);
        right.setPosition(servo2Position);
    }

    public void setAngle(double angle) {
        // Ensure angle is within the valid range
        angle = Math.max(0.0, Math.min(1.0, angle));

        // Set both servos to the same position for angle control
        left.setPosition(angle);
        right.setPosition(angle);
    }

    public void setTwistAndAngle(double twist, double angle) {
        // Ensure twist and angle are within the valid range
        twist = Math.max(0.0, Math.min(1.0, twist));
        angle = Math.max(0.0, Math.min(1.0, angle));

        // Calculate individual servo positions
        double servo1Position = angle + twist / 2;
        double servo2Position = angle - twist / 2;

        // Ensure servo positions are within the valid range
        servo1Position = Math.max(0.0, Math.min(1.0, servo1Position));
        servo2Position = Math.max(0.0, Math.min(1.0, servo2Position));

        // Set servo positions
        left.setPosition(servo1Position);
        right.setPosition(servo2Position);
    }



}
