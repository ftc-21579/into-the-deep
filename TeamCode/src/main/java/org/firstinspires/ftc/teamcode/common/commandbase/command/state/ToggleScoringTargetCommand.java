package org.firstinspires.ftc.teamcode.common.commandbase.command.state;

import static org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension.highBasketTarget;
import static org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension.lowBasketTarget;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Wrist;
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
                        new SetExtensionCommand(bot.getExtension(), highBasketTarget).schedule();
                    }
                    break;
                case HIGH_BASKET:
                    bot.setTargetMode(TargetMode.LOW_BASKET);
                    if (bot.getState() == BotState.DEPOSIT) {
                        new SetExtensionCommand(bot.getExtension(), lowBasketTarget).schedule();
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
                        new ParallelCommandGroup(
                                new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, Wrist.wristDown)),
                                new SetPivotAngleCommand(bot.getPivot(), 10)
                        ).schedule();
                    }
                    break;
                case SPEC_INTAKE:
                    bot.setTargetMode(TargetMode.SPEC_DEPOSIT);
                    if (bot.getState() == BotState.INTAKE) {
                        new ParallelCommandGroup(
                                new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, Wrist.wristDown - 15)),
                                new SetPivotAngleCommand(bot.getPivot(), 15)
                        ).schedule();
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
