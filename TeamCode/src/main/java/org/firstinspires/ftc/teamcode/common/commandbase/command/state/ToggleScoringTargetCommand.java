package org.firstinspires.ftc.teamcode.common.commandbase.command.state;

import static org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension.highBasketTarget;
import static org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension.lowBasketTarget;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;
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
                    if (bot.getState() == BotState.DEPOSIT) {
                        bot.getExtension().setSetpointCM(highBasketTarget);
                    }
                    break;
                case HIGH_BASKET:
                    bot.setTargetMode(TargetMode.LOW_BASKET);
                    if (bot.getState() == BotState.DEPOSIT) {
                        bot.getExtension().setSetpointCM(lowBasketTarget);
                    }
                    break;
            }
        } else {
            switch (bot.getTargetMode()) {
                case LOW_BASKET:
                case HIGH_BASKET:
                case SPEC_DEPOSIT:
                    bot.setTargetMode(TargetMode.SPEC_INTAKE);
                    if (bot.getState() == BotState.INTAKE) {
                        bot.getWrist().setAngle(225);
                        bot.getPivot().setSetpointDEG(10);
                    }
                    break;
                case SPEC_INTAKE:
                    bot.setTargetMode(TargetMode.SPEC_DEPOSIT);
                    if (bot.getState() == BotState.INTAKE) {
                        bot.getWrist().setAngle(210);
                        bot.getPivot().setSetpointDEG(15);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
