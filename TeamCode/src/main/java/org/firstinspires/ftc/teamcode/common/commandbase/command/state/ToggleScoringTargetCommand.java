package org.firstinspires.ftc.teamcode.common.commandbase.command.state;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;
import org.firstinspires.ftc.teamcode.common.intothedeep.TargetMode;

public class ToggleScoringTargetCommand extends CommandBase {
    private final Bot bot;

    public ToggleScoringTargetCommand(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void initialize() {
        if (bot.getTargetElement() == GameElement.SAMPLE) {
            switch (bot.getTargetMode()) {
                case LOW_BASKET:
                case SPEC_DEPOSIT:
                case SPEC_INTAKE:
                    bot.setTargetMode(TargetMode.HIGH_BASKET);
                    break;
                case HIGH_BASKET:
                    bot.setTargetMode(TargetMode.LOW_BASKET);
                    break;
            }
        } else {
            switch (bot.getTargetMode()) {
                case LOW_BASKET:
                case HIGH_BASKET:
                case SPEC_DEPOSIT:
                    bot.setTargetMode(TargetMode.SPEC_INTAKE);
                    break;
                case SPEC_INTAKE:
                    bot.setTargetMode(TargetMode.SPEC_DEPOSIT);
                    break;
            }
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
