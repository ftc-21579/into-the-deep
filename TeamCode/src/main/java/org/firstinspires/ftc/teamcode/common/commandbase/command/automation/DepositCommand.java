package org.firstinspires.ftc.teamcode.common.commandbase.command.automation;

import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.BlinkinCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.ClawOuttakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.SetBotStateCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Wrist;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;
import org.firstinspires.ftc.teamcode.common.intothedeep.TargetMode;

public class DepositCommand extends SequentialCommandGroup {

    public DepositCommand(Bot b) {
        addCommands(
                new ConditionalCommand(
                        // Check which element the bot is holding
                        // SAMPLE
                        new SequentialCommandGroup(
                                new BlinkinCommand(b.getBlinkin(), RevBlinkinLedDriver.BlinkinPattern.WHITE),
                                new ClawOuttakeCommand(b.getClaw()),
                                new WaitCommand(250),
                                new SetWristPositionCommand(b.getWrist(), new Vector2d(0, Wrist.wristDown)),
                                new WaitCommand(250),
                                new SetExtensionCommand(b.getExtension(), 0),
                                new SetPivotAngleCommand(b.getPivot(), 12.5)
                        ),
                        // SPECIMEN
                        new SequentialCommandGroup(
                                new ConditionalCommand(
                                        new ParallelCommandGroup(
                                                new ClawOuttakeCommand(b.getClaw()),
                                                new BlinkinCommand(b.getBlinkin(), RevBlinkinLedDriver.BlinkinPattern.WHITE),
                                                new SetWristPositionCommand(b.getWrist(), new Vector2d(0, Wrist.wristDown)),
                                                new SetPivotAngleCommand(b.getPivot(), 10),
                                                new SetExtensionCommand(b.getExtension(), 0)
                                        ),
                                        new SequentialCommandGroup(
                                                new SetExtensionCommand(b.getExtension(), 0),
                                                new ClawOuttakeCommand(b.getClaw()),
                                                new BlinkinCommand(b.getBlinkin(), RevBlinkinLedDriver.BlinkinPattern.WHITE),
                                                new SetWristPositionCommand(b.getWrist(), new Vector2d(0, Wrist.wristForward + 10)),
                                                new SetPivotAngleCommand(b.getPivot(), 20),
                                                new SetExtensionCommand(b.getExtension(), 10)
                                        ),
                                        () -> b.getTargetMode() == TargetMode.SPEC_INTAKE
                                )
                        ),
                        () -> b.getTargetElement() == GameElement.SAMPLE
                ),
                new SetBotStateCommand(b, BotState.INTAKE)
        );
    }
}
