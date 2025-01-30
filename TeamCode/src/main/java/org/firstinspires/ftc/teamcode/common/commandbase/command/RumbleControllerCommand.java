package org.firstinspires.ftc.teamcode.common.commandbase.command;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Bot;

public class RumbleControllerCommand extends CommandBase {
    private final Bot bot;
    private final int duration;

    public RumbleControllerCommand(Bot bot, int duration) {
        this.bot = bot;
        this.duration = duration;
    }

    @Override
    public void initialize() {
        bot.getGamepad().rumble(duration);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
