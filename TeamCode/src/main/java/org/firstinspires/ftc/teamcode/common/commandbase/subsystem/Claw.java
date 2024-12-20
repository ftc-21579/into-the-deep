package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.Bot;

public class Claw extends SubsystemBase {

    private final Bot bot;

    private final Servo claw;

    private final AnalogInput clawFeedback;

    public enum ClawState {
        OPEN,
        CLOSED
    }
    private ClawState state = ClawState.OPEN;

    public Claw(Bot bot) {
        this.bot = bot;
        //this.clawFeedback = clawFeedback;

        claw = bot.hMap.get(Servo.class, "claw");
        clawFeedback = bot.hMap.get(AnalogInput.class, "clawFeedback");

    }

    public void periodic() {
        bot.telem.addData("Grabbed??? ", isGrabbing());
    }

    public boolean isGrabbing() {
        double feedbackVoltage = clawFeedback.getVoltage();
        double positionDegrees = mapVoltageToDegrees(feedbackVoltage);

        return positionDegrees > 265.0;
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
        claw.setPosition(0.5);
        state = ClawState.OPEN;
        bot.telem.addData("Claw State", state);
    }

    public void outtake() {
        claw.setPosition(0.05);
        state = ClawState.CLOSED;
        bot.telem.addData("Claw State", state);
    }

    public void toggle() {
        if (state == ClawState.OPEN) {
            outtake();
        } else {
            intake();
        }
    }
}

