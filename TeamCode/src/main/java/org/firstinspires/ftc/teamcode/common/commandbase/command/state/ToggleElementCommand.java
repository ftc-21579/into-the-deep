package org.firstinspires.ftc.teamcode.common.commandbase.command.state;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;
import org.firstinspires.ftc.teamcode.common.intothedeep.TargetMode;

public class ToggleElementCommand extends CommandBase {
    private final Bot bot;

    public ToggleElementCommand(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void initialize() {
        if (bot.getTargetElement() == GameElement.SAMPLE) {
            bot.setTargetElement(GameElement.SPECIMEN);
            bot.setTargetMode(TargetMode.SPEC_INTAKE);
        } else {
            bot.setTargetElement(GameElement.SAMPLE);
            bot.setTargetMode(TargetMode.HIGH_BASKET);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
