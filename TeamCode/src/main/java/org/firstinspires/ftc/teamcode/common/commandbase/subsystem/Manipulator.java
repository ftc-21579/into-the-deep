package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.common.Bot;

public class Manipulator extends SubsystemBase {

    private final Bot bot;
    private final CRServo wrist, gripper;

    public Manipulator(Bot bot) {
        this.bot = bot;
        wrist = bot.hMap.get(CRServo.class, "wrist");
        gripper = bot.hMap.get(CRServo.class, "gripper");
    }

    public void setWristPower(double power) {
        wrist.setPower(power);
    }

    public void setGripperPower(double power) {
        gripper.setPower(power);
    }
}
