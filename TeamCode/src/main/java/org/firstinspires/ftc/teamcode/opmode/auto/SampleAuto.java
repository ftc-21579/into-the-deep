package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.mineinjava.quail.util.geometry.Vec2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.automation.DepositCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.automation.IntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ClawIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.FollowPathCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.SetBotStateCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;
import org.firstinspires.ftc.teamcode.common.intothedeep.TargetMode;
import org.firstinspires.ftc.teamcode.common.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.common.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.common.pedroPathing.localization.PoseUpdater;
import org.firstinspires.ftc.teamcode.common.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.common.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.common.pedroPathing.pathGeneration.Point;
import org.firstinspires.ftc.teamcode.common.pedroPathing.util.DashboardPoseTracker;
import org.firstinspires.ftc.teamcode.common.pedroPathing.util.Drawing;

@Autonomous(name="Sample Auto")
public class SampleAuto extends LinearOpMode {

    private final Pose startingPose = new Pose(9, 89.5, Math.toRadians(180));
    private final Point basketPosition = new Point(16, 128, Point.CARTESIAN);
    private final double basketHeading = -45;

    @Override
    public void runOpMode() {
        CommandScheduler.getInstance().reset();

        Telemetry telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Bot bot = new Bot(telem, hardwareMap, gamepad1, false);

        CommandScheduler.getInstance().registerSubsystem(bot.getPivot());
        CommandScheduler.getInstance().registerSubsystem(bot.getExtension());
        CommandScheduler.getInstance().registerSubsystem(bot.getWrist());
        CommandScheduler.getInstance().registerSubsystem(bot.getClaw());

        Follower f = bot.getFollower();

        f.setStartingPose(startingPose);
        f.setMaxPower(0.75);

        SequentialCommandGroup auto = new SequentialCommandGroup(
                new InstantCommand(() -> {
                    bot.setState(BotState.DEPOSIT);
                    bot.setTargetElement(GameElement.SPECIMEN);
                    bot.setTargetMode(TargetMode.SPEC_DEPOSIT);
                }),
                new ClawIntakeCommand(bot.getClaw()),
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(9, 89.5, Point.CARTESIAN),
                                                new Point(38, 78, Point.CARTESIAN)
                                        )
                                )
                                .setConstantHeadingInterpolation(Math.toRadians(180))
                                .build()
                        ),
                        new SetPivotAngleCommand(bot.getPivot(), 95),
                        new SetExtensionCommand(bot.getExtension(), 16.0)
                ),
                new DepositCommand(bot),
                new InstantCommand(() -> {
                    bot.setTargetElement(GameElement.SAMPLE);
                    bot.setTargetMode(TargetMode.LOW_BASKET);
                }),
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierCurve(
                                        new Point(38, 78, Point.CARTESIAN),
                                        new Point(0, 121, Point.CARTESIAN),
                                        new Point(46, 121, Point.CARTESIAN)
                                )
                        )
                        .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(0))
                        .build()
                ),
                new IntakeCommand(bot),
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierLine(
                                        new Point(46, 121, Point.CARTESIAN),
                                        basketPosition
                                )
                        )
                        .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(basketHeading))
                        .build()
                ),
                new DepositCommand(bot),
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierLine(
                                        new Point(16, 128, Point.CARTESIAN),
                                        new Point(16, 128, Point.CARTESIAN)
                                )
                        )
                        .setLinearHeadingInterpolation(Math.toRadians(basketHeading), Math.toRadians(6))
                        .build()
                ),
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierLine(
                                        new Point(16, 128, Point.CARTESIAN),
                                        new Point(46, 132, Point.CARTESIAN)
                                )
                        )
                        .build()
                )
        );

        // dashboard pose stuff
        DashboardPoseTracker tracker = new DashboardPoseTracker(f.poseUpdater);
        Drawing.drawRobot(f.poseUpdater.getPose(), "#4CBB17");
        Drawing.sendPacket();

        waitForStart();

        CommandScheduler.getInstance().schedule(auto);

        while (opModeIsActive()) {

            bot.getFollower().update();

            f.poseUpdater.update();
            tracker.update();

            CommandScheduler.getInstance().run();

            f.telemetryDebug(telemetry);

            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }
}
