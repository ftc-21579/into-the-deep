package org.firstinspires.ftc.teamcode.common.commandbase.command.state;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.mineinjava.quail.util.geometry.Vec2d;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;

@Config
public class ToSpecimenDepositCommand extends SequentialCommandGroup {
    // Create a SequentialCommandGroup or FunctionalCommand or ParallelCommandGroup to transition to the deposit state

    public static double wrist_twist = -180.0, wrist_angle = 70;

    public ToSpecimenDepositCommand(Bot bot) {
        addCommands(
                new SetWristPositionCommand(bot.getWrist(), new Vec2d(wrist_twist, wrist_angle)),
                new InstantCommand(() -> bot.setState(BotState.DEPOSIT)),
                new SetExtensionCommand(bot.getExtension(), 0.0),
                new InstantCommand(() -> bot.setState(BotState.DEPOSIT)),
                new SetPivotAngleCommand(bot.getPivot(), 95.0),
                new WaitCommand(500),
                new SetExtensionCommand(bot.getExtension(), 20.0)
        );
    }
}