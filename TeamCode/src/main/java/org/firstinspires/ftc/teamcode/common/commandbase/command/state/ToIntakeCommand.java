package org.firstinspires.ftc.teamcode.common.commandbase.command.state;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.FunctionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.mineinjava.quail.util.geometry.Vec2d;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;

@Config
public class ToIntakeCommand extends SequentialCommandGroup {
    // Create a SequentialCommandGroup or FunctionalCommand or ParallelCommandGroup to transition to the intake state

    public static double wrist_twist = 135, wrist_angle = 135;

    public ToIntakeCommand(Bot bot) {
        addCommands(
                new SetWristPositionCommand(bot.getWrist(), new Vec2d(wrist_twist, wrist_angle)),
                new SetExtensionCommand(bot.getExtension(), 0.0),
                new InstantCommand(() -> bot.setState(BotState.INTAKE)),
                new SetPivotAngleCommand(bot.getPivot(), 0.0)
        );
    }
}
