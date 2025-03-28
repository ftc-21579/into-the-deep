package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.intothedeep.Color;
import org.firstinspires.ftc.teamcode.common.util.BlinkinUtil;

public class Intake extends SubsystemBase {

    private final Bot bot;
    private final Servo claw;
    private final AnalogInput clawFeedback;
    private final DigitalChannel colorSensor0, colorSensor1;
    public static double grabbedAngle = 265.0;

    private final CRServo leftActive, rightActive;

    public enum ClawState { OPEN, CLOSED }
    private ClawState state = ClawState.OPEN;
    private Color color = Color.NONE;

    public Intake(Bot bot) {
        this.bot = bot;
        //this.clawFeedback = clawFeedback;

        claw = bot.hMap.get(Servo.class, "claw");
        clawFeedback = bot.hMap.get(AnalogInput.class, "clawFeedback");

        colorSensor0 = bot.hMap.get(DigitalChannel.class, "colorSensor0");
        colorSensor1 = bot.hMap.get(DigitalChannel.class, "colorSensor1");

        leftActive = bot.hMap.get(CRServo.class, "leftActive");
        rightActive = bot.hMap.get(CRServo.class, "rightActive");
        rightActive.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void periodic() {
        bot.telem.addData("Is Grabbed????????", isGrabbing());
        bot.telem.addData("Analog Voltage", clawFeedback.getVoltage());
        bot.telem.addData("Analog Degrees", getAnalogDegrees());
    }

    public boolean isGrabbing() {
        double feedbackVoltage = clawFeedback.getVoltage();
        double positionDegrees = mapVoltageToDegrees(feedbackVoltage);

        return positionDegrees > grabbedAngle;
    }

    public boolean isCorrectColor() {
        Color allianceColor = bot.getAllianceColor();
        Color currentColor = getColor();
        return currentColor == allianceColor || currentColor == Color.YELLOW;
    }

    public double getAnalogDegrees() {
        double feedbackVoltage = clawFeedback.getVoltage();
        return mapVoltageToDegrees(feedbackVoltage);
    }

    private double mapVoltageToDegrees(double voltage) {
        double maxVoltage = 3.3;
        return (voltage / maxVoltage) * 360;
    }

    public void intake() {
        claw.setPosition(0.025);
        state = ClawState.OPEN;
    }

    public void outtake() {
        claw.setPosition(0.5);
        state = ClawState.CLOSED;
    }

    public void toggle() {
        if (state == ClawState.OPEN) {
            outtake();
        } else {
            intake();
        }
    }

    public Color getColor() {
        if (colorSensor0.getState() && colorSensor1.getState()) {
            bot.setGameElementColor(Color.YELLOW);
            return Color.YELLOW;
        } else if (colorSensor0.getState() && !colorSensor1.getState()) {
            bot.setGameElementColor(Color.BLUE);
            return Color.BLUE;
        } else if (!colorSensor0.getState() && colorSensor1.getState()) {
            bot.setGameElementColor(Color.RED);
            return Color.RED;
        } else {
            bot.setGameElementColor(Color.NONE);
            return Color.NONE;
        }
    }

    public void activeIntake() {
        leftActive.setPower(1.0);
        rightActive.setPower(1.0);
    }

    public void activeOuttake() {
        leftActive.setPower(-1.0);
        rightActive.setPower(-1.0);
    }

    public void deactivateActive() {
        leftActive.setPower(0.0);
        rightActive.setPower(0.0);
    }

    public void toggleActive() {
        if (leftActive.getPower() == 0.0) {
            activeIntake();
        } else {
            deactivateActive();
        }
    }

}

