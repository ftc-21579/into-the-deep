package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.Bot;

public class Claw extends SubsystemBase {

    private final Bot bot;

    private final Servo claw;

    public Claw(Bot bot) {
        this.bot = bot;

        claw = bot.hMap.get(Servo.class, "claw");
    }

    public void intake() {
        claw.setPosition(1.0);
        bot.telem.addData("position", claw.getPosition());
    }

    public void outtake() {
        claw.setPosition(0.0);
        bot.telem.addData("position", claw.getPosition());
    }
}

