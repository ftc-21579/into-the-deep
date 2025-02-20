package org.firstinspires.ftc.teamcode.common.commandbase.command.automation;

import static org.firstinspires.ftc.teamcode.common.Config.xOffset;
import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.score2;
import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.scoreControl;
import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.scoreControlBack;
import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.specIntake;

import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.Point;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.FollowPathCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.intothedeep.Direction;
import org.firstinspires.ftc.teamcode.common.intothedeep.TargetMode;

public class SpecCycleLoop extends SequentialCommandGroup {

    public SpecCycleLoop(Bot bot) {
        addCommands(
                new ConditionalCommand(
                        new InstantCommand(() -> {
                            bot.setPathFinished(true);
                        }),
                        new SequentialCommandGroup(
                                new InstantCommand(() -> {
                                    bot.incrementCurrentSpecCycles(Direction.UP);
                                }),
                                new SequentialCommandGroup(
                                        new FollowPathCommand(bot.getFollower(), bot.getFollower().pathBuilder()
                                                .addPath(
                                                        new BezierCurve(
                                                                new Point(new Pose(score2.getX(), bot.getXOffset() + xOffset, score2.getHeading())),
                                                                new Point(scoreControl),
                                                                new Point(scoreControlBack),
                                                                new Point(new Pose(bot.getXOffset(), specIntake.getY(), specIntake.getHeading()))
                                                        )
                                                )
                                                .setConstantHeadingInterpolation(specIntake.getHeading())
                                                .build()
                                        ),
                                        new WaitCommand(250),
                                        new ParallelCommandGroup(
                                                new IntakeCommand(bot),
                                                new SequentialCommandGroup(
                                                        new WaitCommand(250),
                                                        new FollowPathCommand(bot.getFollower(), bot.getFollower().pathBuilder()
                                                                .addPath(
                                                                        new BezierCurve(
                                                                                new Point(new Pose(bot.getXOffset(), specIntake.getY(), specIntake.getHeading())),
                                                                                new Point(scoreControl),
                                                                                new Point(new Pose(score2.getX(), bot.getYOffset(), score2.getHeading()))
                                                                      )
                                                                )
                                                                .setConstantHeadingInterpolation(score2.getHeading())
                                                                .build()
                                                        )
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
                                        () -> bot.targetSpecCycles.get() == bot.currentSpecCycles.get()
                                )
                        ),
                        () -> bot.targetSpecCycles.get() <= bot.currentSpecCycles.get()
                )
        );
    }
}
