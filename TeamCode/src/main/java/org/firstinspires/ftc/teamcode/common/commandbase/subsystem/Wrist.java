package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.Bot;

public class Wrist extends SubsystemBase {

    private final Bot bot;

    private final Servo left, right;

    public Wrist(Bot bot) {
        this.bot = bot;

        left = bot.hMap.get(Servo.class, "leftWrist");
        right = bot.hMap.get(Servo.class, "rightWrist");
    }

}
