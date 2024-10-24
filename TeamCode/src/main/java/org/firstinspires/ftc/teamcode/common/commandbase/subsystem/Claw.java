package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.common.Bot;

public class Claw extends SubsystemBase {

    private final Bot bot;

    private final CRServo left, right;

    public Claw(Bot bot) {
        this.bot = bot;

        left = bot.hMap.get(CRServo.class, "leftClaw");
        right = bot.hMap.get(CRServo.class, "rightClaw");
        right.setDirection(CRServo.Direction.REVERSE);
    }

    public void intakePowers() {
        left.setPower(1);
        right.setPower(1);
    }

    public void outtakePowers() {
        left.setPower(-1);
        right.setPower(-1);
    }

    public void stop() {
        left.setPower(0);
        right.setPower(0);
    }

}

