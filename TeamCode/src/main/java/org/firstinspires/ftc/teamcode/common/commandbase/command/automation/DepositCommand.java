package org.firstinspires.ftc.teamcode.common.commandbase.command.automation;

import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.mineinjava.quail.util.geometry.Vec2d;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ClawOuttakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.SetBotStateCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;

public class DepositCommand extends SequentialCommandGroup {

    public DepositCommand(Bot b) {
        addCommands(
                new ConditionalCommand(
                        // Check which element the bot is holding
                        // SAMPLE
                        new SequentialCommandGroup(
                                new ClawOuttakeCommand(b.getClaw()),
                                new WaitCommand(250),
                                new SetWristPositionCommand(b.getWrist(), new Vec2d(0, 230)),
                                new WaitCommand(250),
                                new SetExtensionCommand(b.getExtension(), 0),
                                new WaitCommand(750)
                        ),
                        // SPECIMEN
                        new SequentialCommandGroup(
                                new SetExtensionCommand(b.getExtension(), 0),
                                new WaitCommand(500),
                                new ClawOuttakeCommand(b.getClaw()),
                                new WaitCommand(250),
                                new SetWristPositionCommand(b.getWrist(), new Vec2d(0, 210)),
                                new WaitCommand(250)
                        ),
                        () -> b.getTargetElement() == GameElement.SAMPLE
                ),
                new SetPivotAngleCommand(b.getPivot(), 15),
                new SetBotStateCommand(b, BotState.INTAKE)
        );
    }
}
