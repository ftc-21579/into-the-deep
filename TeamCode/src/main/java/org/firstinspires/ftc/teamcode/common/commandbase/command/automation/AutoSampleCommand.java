package org.firstinspires.ftc.teamcode.common.commandbase.command.automation;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.mineinjava.quail.util.geometry.Vec2d;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ClawOuttakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ToggleClawCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;

@Config
public class AutoSampleCommand extends SequentialCommandGroup {

    public static double wristTwistAngle = 0, pivotAngle = 90.0, extensionHeight = 62.0;

    public AutoSampleCommand(Bot bot) {
        addCommands(
                new SetWristPositionCommand(bot.getWrist(), new Vec2d(wristTwistAngle, 0)),
                new SetExtensionCommand(bot.getExtension(), 0.0),
                new SetPivotAngleCommand(bot.getPivot(), pivotAngle),
                new WaitCommand(1000),
                new SetExtensionCommand(bot.getExtension(), extensionHeight),
                new WaitCommand(2000),
                new ToggleClawCommand(bot.getClaw()),
                new WaitCommand(1000),
                new SetExtensionCommand(bot.getExtension(), 0.0),
                new WaitCommand(1000),
                new ToIntakeCommand(bot)
        );
    }
}
