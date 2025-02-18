package org.firstinspires.ftc.teamcode.common.commandbase.command.automation;

import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.BlinkinCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.RumbleControllerCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.ClawIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.ClawOuttakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.ManualPivotCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.SetBotStateCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Wrist;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;
import org.firstinspires.ftc.teamcode.common.intothedeep.Direction;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;
import org.firstinspires.ftc.teamcode.common.intothedeep.TargetMode;

public class IntakeCommand extends SequentialCommandGroup {

    public IntakeCommand(Bot b) {
                addCommands(
                new ConditionalCommand(
                        new SequentialCommandGroup(
                                new ManualPivotCommand(b.getPivot(), Direction.DOWN),
                                //new SetPivotAngleCommand(b.getPivot(), 0),
                                new WaitCommand(250)
                        ),
                        new InstantCommand(() -> {}),
                        () -> b.getTargetElement() == GameElement.SAMPLE || b.getTargetElement() == GameElement.SPECIMEN && b.getTargetMode() == TargetMode.SPEC_INTAKE
                ),
                new ClawIntakeCommand(b.getClaw()),
                new WaitCommand(250),
                new ConditionalCommand(
                        // Check if the bot is actually grabbing the element
                        // GRABBING
                        new ConditionalCommand(
                                // Sequence depending on the element (sample or specimen)
                                // SAMPLE
                                new SequentialCommandGroup(
                                        new BlinkinCommand(b.getBlinkin(), b.getClaw().getColor()),
                                        new SetWristPositionCommand(b.getWrist(), new Vector2d(-90, Wrist.wristUp)),
                                        new SetExtensionCommand(b.getExtension(), 0),
                                        //new WaitCommand(350),
                                        new SetPivotAngleCommand(b.getPivot(), 95),
                                        new SetWristPositionCommand(b.getWrist(), new Vector2d(-180, 45)),
                                        new ConditionalCommand(
                                                // Go to the correct height based on the target mode
                                                // low basket
                                                new SetExtensionCommand(b.getExtension(), Extension.lowBasketTarget),
                                                // high basket
                                                new SetExtensionCommand(b.getExtension(), Extension.highBasketTarget),
                                                () -> b.getTargetMode() == TargetMode.LOW_BASKET
                                        ),
                                        new SetBotStateCommand(b, BotState.DEPOSIT)
                                ),
                                // SPECIMEN
                                new SequentialCommandGroup(
                                        new ConditionalCommand(
                                                // Correct sequence based on the current specimen target mode
                                                // Intake Shuttling
                                                new SequentialCommandGroup(
                                                        new BlinkinCommand(b.getBlinkin(), b.getClaw().getColor()),
                                                        new SetWristPositionCommand(b.getWrist(), new Vector2d(0, 45)),
                                                        new ManualPivotCommand(b.getPivot(), Direction.UP),
                                                        new SetExtensionCommand(b.getExtension(), 0),
                                                        new WaitCommand(500),
                                                        new SetExtensionCommand(b.getExtension(), 35)
                                                ),
                                                // Deposit Shuttling
                                                new SequentialCommandGroup(
                                                        new BlinkinCommand(b.getBlinkin(), b.getClaw().getColor()),
                                                        new SetWristPositionCommand(b.getWrist(), new Vector2d(-180, Wrist.wristUp)),
                                                        new WaitCommand(100),
                                                        new ParallelCommandGroup(
                                                                new SetExtensionCommand(b.getExtension(), 0),
                                                                new SetPivotAngleCommand(b.getPivot(), 95, true)
                                                        ),
                                                        new SetExtensionCommand(b.getExtension(), Extension.highChamberTarget)
                                                ),
                                                () -> b.getTargetMode() == TargetMode.SPEC_INTAKE
                                        ),
                                        new SetBotStateCommand(b, BotState.DEPOSIT)
                                ),
                                () -> b.getTargetElement() == GameElement.SAMPLE
                        ),
                        // NOT GRABBING
                        new ParallelCommandGroup(
                                new ConditionalCommand(
                                        new ManualPivotCommand(b.getPivot(), Direction.UP),
                                        new InstantCommand(() -> {}),
                                        () -> b.getTargetElement() == GameElement.SAMPLE || b.getTargetElement() == GameElement.SPECIMEN && b.getTargetMode() == TargetMode.SPEC_INTAKE
                                ),
                                new RumbleControllerCommand(b, 500),
                                new ClawOuttakeCommand(b.getClaw()),
                                new SetBotStateCommand(b, BotState.INTAKE),
                                new SequentialCommandGroup(
                                        new BlinkinCommand(b.getBlinkin(), RevBlinkinLedDriver.BlinkinPattern.STROBE_RED),
                                        new WaitCommand(500),
                                        new BlinkinCommand(b.getBlinkin(), RevBlinkinLedDriver.BlinkinPattern.WHITE)
                                )
                        ),
                        () -> b.getClaw().isGrabbing() && b.getClaw().isCorrectColor()
                )
        );
    }
}