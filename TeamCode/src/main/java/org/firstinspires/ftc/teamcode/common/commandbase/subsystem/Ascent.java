package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.Bot;

@com.acmerobotics.dashboard.config.Config
public class Ascent extends SubsystemBase {
    private final Bot bot;

    private final Servo leftPTO, rightPTO;

    public static double engaged = 0.0, release = 0.2, locked = 0.28;

    public Ascent(Bot bot) {
        this.bot = bot;

        leftPTO = bot.hMap.get(Servo.class, "leftPTO");
        rightPTO = bot.hMap.get(Servo.class, "rightPTO");

        rightPTO.setDirection(Servo.Direction.REVERSE);
    }

    public void engagePTO() {
        leftPTO.setPosition(engaged);
        rightPTO.setPosition(engaged);
    }

    public void releaseArms() {
        leftPTO.setPosition(release);
        rightPTO.setPosition(release);
    }

    public void lockArms() {
        leftPTO.setPosition(locked);
        rightPTO.setPosition(locked);
    }
}
