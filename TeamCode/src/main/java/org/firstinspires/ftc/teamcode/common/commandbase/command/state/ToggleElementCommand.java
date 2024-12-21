package org.firstinspires.ftc.teamcode.common.commandbase.command.state;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;

public class ToggleElementCommand extends CommandBase {
    private final Bot bot;

    public ToggleElementCommand(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void initialize() {
        if (bot.getTargetElement() == GameElement.SAMPLE) {
            bot.setTargetElement(GameElement.SPECIMEN);
        } else {
            bot.setTargetElement(GameElement.SAMPLE);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
