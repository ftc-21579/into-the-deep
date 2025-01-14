package org.firstinspires.ftc.teamcode.common.commandbase.command.automation;

import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ClawIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ClawOuttakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.ManualPivotCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.SetBotStateCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;
import org.firstinspires.ftc.teamcode.common.intothedeep.Direction;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;
import org.firstinspires.ftc.teamcode.common.intothedeep.TargetMode;

public class IntakeCommand extends SequentialCommandGroup {

    public IntakeCommand(Bot b) {
        boolean facingChamber;
        if (b.getDrivetrain() != null) {
            double h = b.getDrivetrain().getHeadingDEG();
            facingChamber = (h >= -45 && h <= 45) || (h >= 135 && h <= 225);
        } else {
            facingChamber = false;
        }

        addCommands(
                new ManualPivotCommand(b.getPivot(), Direction.DOWN),
                new WaitCommand(250),
                new ClawIntakeCommand(b.getClaw()),
                new WaitCommand(250),
                new ConditionalCommand(
                        // Check if the bot is actually grabbing the element
                        // GRABBING
                        new ConditionalCommand(
                                // Sequence depending on the element (sample or specimen)
                                // SAMPLE
                                new SequentialCommandGroup(
                                        new ConditionalCommand(
                                                // Checks if the bot is facing the chamber
                                                // special case for when the bot is facing the chamber to prevent funny stuff
                                                new SequentialCommandGroup(
                                                        new ManualPivotCommand(b.getPivot(), Direction.UP),
                                                        new WaitCommand(250),
                                                        new SetWristPositionCommand(b.getWrist(), new Vector2d(0, 90)),
                                                        new SetExtensionCommand(b.getExtension(), 0),
                                                        new WaitCommand(250)
                                                ),
                                                // just continue if the bot isn't facing the chamber
                                                new InstantCommand(() -> {}),
                                                () -> facingChamber
                                        ),
                                        new SetExtensionCommand(b.getExtension(), 0),
                                        new SetPivotAngleCommand(b.getPivot(), 85),
                                        new SetWristPositionCommand(b.getWrist(), new Vector2d(-180, 90)),
                                        new ConditionalCommand(
                                                // Go to the correct height based on the target mode
                                                // low basket
                                                new SetExtensionCommand(b.getExtension(), 20),
                                                // high basket
                                                new SetExtensionCommand(b.getExtension(), 60),
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
                                                        new ManualPivotCommand(b.getPivot(), Direction.UP),
                                                        new WaitCommand(250),
                                                        new SetWristPositionCommand(b.getWrist(), new Vector2d(0, 90)),
                                                        new SetExtensionCommand(b.getExtension(), 0)
                                                ),
                                                // Deposit Shuttling
                                                new SequentialCommandGroup(
                                                        new SetExtensionCommand(b.getExtension(), 0),
                                                        new SetPivotAngleCommand(b.getPivot(), 95),
                                                        new SetWristPositionCommand(b.getWrist(), new Vector2d(-180, 60)),
                                                        new WaitCommand(500),
                                                        new SetExtensionCommand(b.getExtension(), 16)
                                                ),
                                                () -> b.getTargetMode() == TargetMode.SPEC_INTAKE
                                        ),
                                        new SetBotStateCommand(b, BotState.DEPOSIT)
                                ),
                                () -> b.getTargetElement() == GameElement.SAMPLE
                        ),
                        // NOT GRABBING
                        new SequentialCommandGroup(
                                new ManualPivotCommand(b.getPivot(), Direction.UP),
                                new ClawOuttakeCommand(b.getClaw()),
                                new SetBotStateCommand(b, BotState.INTAKE)
                        ),
                        b.getClaw()::isGrabbing
                )
        );
    }
}