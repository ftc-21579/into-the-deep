package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.Bot;

import java.security.cert.CRLSelector;

@Config
public class Manipulator extends SubsystemBase {

    private final Bot bot;
    private final Servo wrist;
    private final CRServo leftIntake, rightIntake;

    public static double intakePower = 1.0, idlePower = 0.0, depositPower = -1.0;
    public static double wristDownPos = 0.3, wristUpPos = 0.6;

    public Manipulator(Bot bot) {
        this.bot = bot;
        wrist = bot.hMap.get(Servo.class, "wrist");
        leftIntake = bot.hMap.get(CRServo.class, "leftIntake");
        rightIntake = bot.hMap.get(CRServo.class, "rightIntake");
    }


    public void intake() {
        leftIntake.setPower(intakePower);
        rightIntake.setPower(intakePower);
    }

    public void idle() {
        leftIntake.setPower(idlePower);
        rightIntake.setPower(idlePower);
    }

    public void deposit() {
        leftIntake.setPower(depositPower);
        rightIntake.setPower(depositPower);
    }

    public void wristDown() {
        wrist.setPosition(wristDownPos);
    }

    public void wristUp() {
        wrist.setPosition(wristUpPos);
    }

    public double getLeftServoPower() {
        return leftIntake.getPower();
    }

    public double getRightServoPower() {
        return rightIntake.getPower();
    }

}
