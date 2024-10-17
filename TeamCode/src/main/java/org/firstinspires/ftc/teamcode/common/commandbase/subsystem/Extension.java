package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.Bot;

public class Extension extends SubsystemBase {

    private final Bot bot;

    private final DcMotor extensionMotor;

    public Extension(Bot bot) {
        this.bot = bot;

        extensionMotor = bot.hMap.get(DcMotor.class, "extension");
    }

}
