package org.firstinspires.ftc.teamcode.common.commandbase.command.drive;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Bot;

public class ToggleRobotCentricCommand extends CommandBase {
    private final Bot bot;

    public ToggleRobotCentricCommand(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void initialize() {
        bot.setRobotCentric(!bot.getRobotCentric());
    }
}
