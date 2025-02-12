package org.firstinspires.ftc.teamcode.common.commandbase.command.drive;

import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.intake3Shuttle;
import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.score2;
import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.specIntake;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Point;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.automation.DepositCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.automation.IntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.ClawIntakeCommand;

public class SpecCycleCommand extends SequentialCommandGroup {

    private final double yOffset = 2;

    public SpecCycleCommand(Bot bot, int loopCount) {
        addCommands(
                new SequentialCommandGroup(
                        new ParallelCommandGroup(
                                new IntakeCommand(bot),
                                new SequentialCommandGroup(
                                        new WaitCommand(500),
                                        new FollowPathCommand(bot.getFollower(), bot.getFollower().pathBuilder()
                                                .addPath(
                                                        new BezierLine(
                                                                new Point(intake3Shuttle),
                                                                new Point(new Pose(score2.getX() + 2, score2.getY(), score2.getHeading()))
                                                        )
                                                )
                                                .setConstantHeadingInterpolation(score2.getHeading())
                                                .build()
                                        )
                                )
                        ),
                        new DepositCommand(bot)
                )
        );
        if (loopCount == 1) {
            new InstantCommand(() -> {
                bot.setPathFinished(true);
            });
        } else {
            for (int i = 1; i < loopCount; i++) {
                double currentYOffset = yOffset * i;
                addCommands(
                        new SequentialCommandGroup(
                                new FollowPathCommand(bot.getFollower(), bot.getFollower().pathBuilder()
                                        .addPath(
                                                new BezierLine(
                                                        new Point(new Pose(score2.getX() + 2, score2.getY() - currentYOffset + yOffset, score2.getHeading())),
                                                        new Point(specIntake)
                                                )
                                        )
                                        .setConstantHeadingInterpolation(specIntake.getHeading())
                                        .build()
                                )
                        ),
                        new WaitCommand(500),
                        new ParallelCommandGroup(
                                new IntakeCommand(bot),
                                new SequentialCommandGroup(
                                        new WaitCommand(500),
                                        new FollowPathCommand(bot.getFollower(), bot.getFollower().pathBuilder()
                                                .addPath(
                                                        new BezierLine(
                                                                new Point(specIntake),
                                                                new Point(new Pose(score2.getX() + 2, score2.getY() - currentYOffset, score2.getHeading()))
                                                        )
                                                )
                                                .setConstantHeadingInterpolation(score2.getHeading())
                                                .build()
                                        )
                                )
                        ),
                        new DepositCommand(bot)
                );
            }
            addCommands(
                    new InstantCommand(() -> {
                        bot.setPathFinished(true);
                    })
            );
        }
    }
}