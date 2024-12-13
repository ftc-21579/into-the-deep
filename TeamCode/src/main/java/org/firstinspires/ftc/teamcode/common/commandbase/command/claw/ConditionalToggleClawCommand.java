package org.firstinspires.ftc.teamcode.common.commandbase.command.claw;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.mineinjava.quail.util.geometry.Vec2d;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.ManualExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.ManualPivotDownCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.ManualPivotUpCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToggleStateCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Claw;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;
import org.firstinspires.ftc.teamcode.common.intothedeep.SpecimenMode;

public class ConditionalToggleClawCommand extends CommandBase {

    private final Bot bot;
    private final Command chamberIntakeSequence;
    private final Command rungIntakeSequence;
    private final Command depositSequence;
    private final Command chamberIntakeSuccessSequence;
    private final Command rungIntakeSuccessSequence;
    private final Command intakeFailureSequence;
    private final Command specimenIntakeSequence;
    private final Command specimenIntakeSuccessSequence;
    private final Command specimenSubIntakeSequence;
    private final Command specimenSubDepositSequence;
    private final Command specimenDepositSequence;

    public ConditionalToggleClawCommand(Bot bot) {
        this.bot = bot;

        chamberIntakeSuccessSequence = new SequentialCommandGroup(
                new SetWristPositionCommand(bot.getWrist(), new Vec2d(0, 90)),
                new SetExtensionCommand(bot.getExtension(), 0.0),
                new WaitCommand(250),
                new ToggleStateCommand(bot)
        );

        rungIntakeSuccessSequence = new SequentialCommandGroup(
                new ToggleStateCommand(bot)
        );

        specimenIntakeSuccessSequence = new SequentialCommandGroup(
                new ToggleStateCommand(bot)
        );

        specimenSubIntakeSequence = new SequentialCommandGroup(
                new SetWristPositionCommand(bot.getWrist(), new Vec2d(0, 90)),
                new SetExtensionCommand(bot.getExtension(), 0.0),
                new InstantCommand(() -> bot.setState(BotState.DEPOSIT))
        );

        intakeFailureSequence = new SequentialCommandGroup(
                new ToggleClawCommand(bot.getClaw()),
                new ManualPivotUpCommand(bot, bot.getPivot())
        );

        chamberIntakeSequence = new SequentialCommandGroup(
                new ManualPivotDownCommand(bot, bot.getPivot()),
                new WaitCommand(250),
                new ToggleClawCommand(bot.getClaw()),
                new WaitCommand(250),
                new InstantCommand(() -> {
                    if (bot.getTargetElement() == GameElement.SPECIMEN && bot.getSpecimenMode() == SpecimenMode.INTAKE) {
                        if (bot.getClaw().isGrabbing()) {
                            specimenSubIntakeSequence.schedule();
                        } else {
                            intakeFailureSequence.schedule();
                        }
                    } else {
                        if (bot.getClaw().isGrabbing()) {
                            chamberIntakeSuccessSequence.schedule();
                        } else {
                            intakeFailureSequence.schedule();
                        }
                    }
                })
        );

        rungIntakeSequence = new SequentialCommandGroup(
                new ManualPivotDownCommand(bot, bot.getPivot()),
                new WaitCommand(250),
                new ToggleClawCommand(bot.getClaw()),
                new WaitCommand(250),
                new InstantCommand(() -> {
                    if (bot.getTargetElement() == GameElement.SPECIMEN && bot.getSpecimenMode() == SpecimenMode.INTAKE) {
                        if (bot.getClaw().isGrabbing()) {
                            specimenSubIntakeSequence.schedule();
                        } else {
                            intakeFailureSequence.schedule();
                        }
                    } else {
                        if (bot.getClaw().isGrabbing()) {
                            rungIntakeSuccessSequence.schedule();
                        } else {
                            intakeFailureSequence.schedule();
                        }
                    }
                })

        );

        depositSequence = new SequentialCommandGroup(
            new ToggleClawCommand(bot.getClaw()),
            new WaitCommand(250),
            new SetWristPositionCommand(bot.getWrist(), new Vec2d(0, 230)),
            new WaitCommand(250),
            new ToggleStateCommand(bot)
        );

        specimenIntakeSequence = new SequentialCommandGroup(
                new ManualPivotDownCommand(bot, bot.getPivot()),
                new WaitCommand(250),
                new ToggleClawCommand(bot.getClaw()),
                new WaitCommand(250),
                new InstantCommand(() -> {
                    if (bot.getClaw().isGrabbing()) {
                        specimenIntakeSuccessSequence.schedule();
                    } else {
                        intakeFailureSequence.schedule();
                    }
                })
        );

        specimenSubDepositSequence = new SequentialCommandGroup(
                new ClawIntakeCommand(bot.getClaw()),
                new WaitCommand(250),
                new SetWristPositionCommand(bot.getWrist(), new Vec2d(0,230)),
                new SetExtensionCommand(bot.getExtension(), 0.0),
                new InstantCommand(() -> bot.setState(BotState.INTAKE)),
                new SetPivotAngleCommand(bot.getPivot(), 15.0)
        );

        specimenDepositSequence = new SequentialCommandGroup(
                new SetExtensionCommand(bot.getExtension(), 0.0),
                new WaitCommand(500),
                new ToggleClawCommand(bot.getClaw()),
                new WaitCommand(250),
                new ToggleStateCommand(bot)
        );

        addRequirements(bot.getClaw(), bot.getPivot());
    }

    @Override
    public void initialize() {
        if (bot.getTargetElement() == GameElement.SAMPLE) {
            if (bot.getState() == BotState.INTAKE) {
                double normalizedHeading = bot.getDrivetrain().getHeadingDEG();

                if ((normalizedHeading >= -45 && normalizedHeading <= 45) || (normalizedHeading >= 135 && normalizedHeading <= 225)) {
                    chamberIntakeSequence.schedule();
                } else {
                    rungIntakeSequence.schedule();
                }
            } else {
                depositSequence.schedule();
            }
        } else {
            if (bot.getSpecimenMode() == SpecimenMode.DEPOSIT) {
                if (bot.getState() == BotState.INTAKE) {
                    specimenIntakeSequence.schedule();
                } else {
                    specimenDepositSequence.schedule();
                }
            } else {
                if (bot.getState() == BotState.INTAKE) {
                    double normalizedHeading = bot.getDrivetrain().getHeadingDEG();

                    if ((normalizedHeading >= -45 && normalizedHeading <= 45) || (normalizedHeading >= 135 && normalizedHeading <= 225)) {
                        chamberIntakeSequence.schedule();
                    } else {
                        rungIntakeSequence.schedule();
                    }
                } else {
                    specimenSubDepositSequence.schedule();
                }
            }
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
