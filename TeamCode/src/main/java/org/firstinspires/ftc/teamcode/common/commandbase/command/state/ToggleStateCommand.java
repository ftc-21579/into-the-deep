package org.firstinspires.ftc.teamcode.common.commandbase.command.state;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;

public class ToggleStateCommand extends InstantCommand {
    // create a command to toggle the state

    public ToggleStateCommand(Bot bot) {
        super(() -> {
            if (bot.getTargetElement() == GameElement.SAMPLE) {
                if (bot.getState() == BotState.INTAKE) {
                    new ToDepositCommand(bot).whenFinished(()-> bot.setState(BotState.DEPOSIT)).schedule();
                } else {
                    new ToIntakeCommand(bot).whenFinished(()-> bot.setState(BotState.INTAKE)).schedule();
                }
            } else {
                if (bot.getState() == BotState.INTAKE) {
                    new ToSpecimenDepositCommand(bot).whenFinished(()-> bot.setState(BotState.DEPOSIT)).schedule();
                } else {
                    new ToSpecimenIntakeCommand(bot).whenFinished(()-> bot.setState(BotState.INTAKE)).schedule();
                }
            }

        });
    }
}
