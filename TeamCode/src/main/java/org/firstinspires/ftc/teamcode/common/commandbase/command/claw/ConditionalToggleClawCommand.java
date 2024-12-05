package org.firstinspires.ftc.teamcode.common.commandbase.command.claw;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.mineinjava.quail.util.geometry.Vec2d;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.ManualPivotDownCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToggleStateCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Claw;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;

public class ConditionalToggleClawCommand extends CommandBase {

    private final Bot bot;
    private final Command chamberIntakeSequence;
    private final Command rungIntakeSequence;
    private final Command depositSequence;

    public ConditionalToggleClawCommand(Bot bot) {
        this.bot = bot;

        chamberIntakeSequence = new SequentialCommandGroup(
                new ManualPivotDownCommand(bot, bot.getPivot()),
                new WaitCommand(500),
                new ToggleClawCommand(bot.getClaw()),
                new WaitCommand(500),
                new SetWristPositionCommand(bot.getWrist(), new Vec2d(0, 45)),
                new SetExtensionCommand(bot.getExtension(), 0.0),
                new WaitCommand(500),
                new ToggleStateCommand(bot)
        );

        rungIntakeSequence = new SequentialCommandGroup(
                new ManualPivotDownCommand(bot, bot.getPivot()),
                new WaitCommand(500),
                new ToggleClawCommand(bot.getClaw()),
                new WaitCommand(500),
                new ToggleStateCommand(bot)
        );

        depositSequence = new SequentialCommandGroup(
            new ToggleClawCommand(bot.getClaw()),
            new WaitCommand(500),
            new SetWristPositionCommand(bot.getWrist(), new Vec2d(0, 230)),
            new WaitCommand(500),
            new ToggleStateCommand(bot)
        );

        addRequirements(bot.getClaw(), bot.getPivot());
    }

    @Override
    public void initialize() {
        if (bot.getState() == BotState.INTAKE) {
            double heading = bot.getDrivetrain().getHeadingDEG();

            double normalizedHeading = normalizeHeading(heading);

            if ((normalizedHeading >= -45 && normalizedHeading <= 45) || (normalizedHeading >= 135 && normalizedHeading <= 225)) {
                chamberIntakeSequence.schedule();
            } else {
                rungIntakeSequence.schedule();
            }
        } else {
            depositSequence.schedule();
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    private double normalizeHeading(double heading) {
        while (heading > 180) {
            heading -= 360;
        }
        while (heading < -180) {
            heading += 360;
        }
        return heading;
    }
}
