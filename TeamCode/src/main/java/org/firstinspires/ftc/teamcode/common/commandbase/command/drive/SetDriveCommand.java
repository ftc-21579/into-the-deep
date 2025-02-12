package org.firstinspires.ftc.teamcode.common.commandbase.command.drive;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Bot;

public class SetDriveCommand extends CommandBase {
    private final Bot bot;
    private final boolean enableDrive;

    public SetDriveCommand(Bot bot, boolean enableDrive) {
        this.bot = bot;
        this.enableDrive = enableDrive;
    }

    @Override
    public void initialize() {
        bot.setEnableDrive(enableDrive);
        bot.getAscent().resetEncoders();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
