package org.firstinspires.ftc.teamcode.common.commandbase.command.automation;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.mineinjava.quail.util.geometry.Vec2d;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ToggleClawCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotSussyWussy;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;

@Config
public class AutoSpecimenCommand extends SequentialCommandGroup {

    public static double wristTwistAngle = 0, pivotAngle = 100.0, extensionHeight = 3.0;

    public AutoSpecimenCommand(Bot bot) {
        addCommands(
                new SetWristPositionCommand(bot.getWrist(), new Vec2d(wristTwistAngle, 137)),
                new SetExtensionCommand(bot.getExtension(), 0.0),
                new SetPivotSussyWussy(bot.getPivot(), pivotAngle),
                new WaitCommand(1000),
                new SetExtensionCommand(bot.getExtension(), extensionHeight),
                new WaitCommand(1000),
                //new SetWristPositionCommand(bot.getWrist(), new Vec2d(wristTwistAngle, 225)),
                new SetPivotAngleCommand(bot.getPivot(), 60).withTimeout(100),
                new WaitCommand(500),
                new ToggleClawCommand(bot.getClaw()),
                new WaitCommand(500),
                new ToIntakeCommand(bot)
        );
    }
}
