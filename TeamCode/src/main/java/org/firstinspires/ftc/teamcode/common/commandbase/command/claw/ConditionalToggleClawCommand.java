package org.firstinspires.ftc.teamcode.common.commandbase.command.claw;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.mineinjava.quail.util.geometry.Vec2d;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.ManualPivotDownCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToggleStateCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Claw;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;

public class ConditionalToggleClawCommand extends CommandBase {

    private final Bot bot;
    private final Command intakeSequence;
    private final Command depositSequence;

    public ConditionalToggleClawCommand(Bot bot) {
        this.bot = bot;

        intakeSequence = new SequentialCommandGroup(
                new ManualPivotDownCommand(bot, bot.getPivot()),
                new ToggleClawCommand(bot.getClaw()),
                new ToggleStateCommand(bot)
        );

        depositSequence = new SequentialCommandGroup(
            new ToggleClawCommand(bot.getClaw()),
            new SetWristPositionCommand(bot.getWrist(), new Vec2d(0, 230)),
            new ToggleStateCommand(bot)
        );

        addRequirements(bot.getClaw(), bot.getPivot());
    }

    @Override
    public void initialize() {
        if (bot.getState() == BotState.INTAKE) {
            intakeSequence.schedule();
        } else {
            depositSequence.schedule();
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
