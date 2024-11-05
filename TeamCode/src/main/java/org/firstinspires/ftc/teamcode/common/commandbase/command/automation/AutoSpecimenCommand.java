package org.firstinspires.ftc.teamcode.common.commandbase.command.automation;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.mineinjava.quail.util.geometry.Vec2d;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ClawOuttakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;

@Config
public class AutoSpecimenCommand extends SequentialCommandGroup {

    public static double wristTwistAngle = 135, pivotAngle = 95.0, extensionHeight = 5.0;

    public AutoSpecimenCommand(Bot bot) {
        addCommands(
                new SetWristPositionCommand(bot.getWrist(), new Vec2d((double) 270 / 2, 0)),
                new SetExtensionCommand(bot.getExtension(), 0.0),
                new SetPivotAngleCommand(bot.getPivot(), pivotAngle),
                new SetExtensionCommand(bot.getExtension(), extensionHeight),
                new SetWristPositionCommand(bot.getWrist(), new Vec2d(wristTwistAngle, 270)),
                new ClawOuttakeCommand(bot.getClaw()),
                new ToIntakeCommand(bot)
        );
    }
}
