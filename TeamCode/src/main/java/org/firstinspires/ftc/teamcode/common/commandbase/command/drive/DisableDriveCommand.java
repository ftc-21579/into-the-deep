package org.firstinspires.ftc.teamcode.common.commandbase.command.drive;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.Subsystem;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Ascent;

public class DisableDriveCommand extends CommandBase {
    private final Bot bot;

    public DisableDriveCommand(Bot bot) {
        this.bot = bot;
        //addRequirements((Subsystem) this.bot);
    }

    @Override
    public void initialize() {
        bot.disableDrive();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}