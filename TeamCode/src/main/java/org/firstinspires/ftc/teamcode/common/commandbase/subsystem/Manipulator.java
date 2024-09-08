package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.Bot;

@Config
public class Manipulator extends SubsystemBase {

    private final Bot bot;
    private final Servo wrist, gripper;

    public static double grabPos = 0.0, releasePos = 0.0;
    public static double wristDownPos = 0.0, wristUpPos = 0.0;

    public Manipulator(Bot bot) {
        this.bot = bot;
        wrist = bot.hMap.get(Servo.class, "wrist");
        gripper = bot.hMap.get(Servo.class, "gripper");
    }


    public void grab() {
        gripper.setPosition(grabPos);
    }

    public void release() {
        gripper.setPosition(releasePos);
    }

    public void wristDown() {
        wrist.setPosition(wristDownPos);
    }

    public void wristUp() {
        wrist.setPosition(wristUpPos);
    }

}
