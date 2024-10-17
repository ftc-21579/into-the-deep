package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.Bot;

public class Pivot extends SubsystemBase {

    private final Bot bot;

    private final DcMotor pivotMotor;

    public Pivot(Bot bot) {
        this.bot = bot;

        pivotMotor = bot.hMap.get(DcMotor.class, "pivot");
    }

}
