package org.firstinspires.ftc.teamcode.common.commandbase.command.automation;

import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.intake3Shuttle;
import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.score2;
import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.scoreControl;
import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.scoreControlBack;
import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.specIntake;

import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.RepeatCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Point;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.automation.DepositCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.automation.IntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.automation.SpecCycleLoop;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.FollowPathCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.intothedeep.Direction;
import org.firstinspires.ftc.teamcode.common.intothedeep.TargetMode;
import org.firstinspires.ftc.teamcode.common.util.SussyRepeatCommand;

public class SpecCycle extends SequentialCommandGroup {

    public SpecCycle(Bot bot) {
        addCommands(
                new ConditionalCommand(
                        new SequentialCommandGroup(
                                new InstantCommand(() -> {
                                    bot.currentSpecCycles.set(1);
                                }),
                                new ParallelCommandGroup(
                                        new IntakeCommand(bot),
                                        new SequentialCommandGroup(
                                                new WaitCommand(250),
                                                new FollowPathCommand(bot.getFollower(), bot.getFollower().pathBuilder()
                                                        .addPath(
                                                                new BezierCurve(
                                                                        new Point(specIntake),
                                                                        new Point(scoreControl),
                                                                        new Point(score2)
                                                                )
                                                        )
                                                        .setConstantHeadingInterpolation(score2.getHeading())
                                                        .build()
                                                )
                                        )
                                ),
                                new ConditionalCommand(
                                        new SequentialCommandGroup(
                                                new InstantCommand(() -> {
                                                    bot.setTargetMode(TargetMode.SPEC_INTAKE);
                                                }),
                                                new SetExtensionCommand(bot.getExtension(), 0),
                                                new DepositCommand(bot),
                                                new InstantCommand(() -> {
                                                    bot.setPathFinished(true);
                                                })
                                        ),
                                        new DepositCommand(bot),
                                        () -> bot.targetSpecCycles.get() == 1
                                )
                        ),
                        new InstantCommand(() -> {
                            bot.setPathFinished(true);
                        }),
                        () -> bot.targetSpecCycles.get() > 0
                ),
                new SpecCycleLoop(bot),
                new SpecCycleLoop(bot),
                new SpecCycleLoop(bot),
                new SpecCycleLoop(bot),
                new SpecCycleLoop(bot),
                new SpecCycleLoop(bot),
                new SpecCycleLoop(bot),
                new SpecCycleLoop(bot),
                new SpecCycleLoop(bot),
                new SpecCycleLoop(bot),
                new SpecCycleLoop(bot),
                new SpecCycleLoop(bot),
                new SpecCycleLoop(bot),
                new SpecCycleLoop(bot),
                new SpecCycleLoop(bot),
                new SpecCycleLoop(bot),
                new SpecCycleLoop(bot),
                new SpecCycleLoop(bot),
                new InstantCommand(() -> {
                    bot.currentSpecCycles.set(0);
                })
        );
    }
}