package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
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
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.ManualWristTwistCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;
import org.firstinspires.ftc.teamcode.common.intothedeep.Direction;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;
import org.firstinspires.ftc.teamcode.common.intothedeep.TargetMode;
import org.firstinspires.ftc.teamcode.common.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.common.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.common.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.common.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.common.pedroPathing.pathGeneration.Point;
import org.firstinspires.ftc.teamcode.common.pedroPathing.util.DashboardPoseTracker;
import org.firstinspires.ftc.teamcode.common.pedroPathing.util.Drawing;

@Autonomous(name="Sample Auto")
public class SampleAuto extends LinearOpMode {

    private final Pose startingPose = new Pose(9, 89.5, Math.toRadians(180));
    private final Pose basketPose = new Pose(16, 128, Math.toRadians(-45));
    private final Pose chamberPose = new Pose(38, 78, Math.toRadians(180));

    private final Pose pickup1Pose = new Pose(46, 121, Math.toRadians(0));
    private final Pose pickup2Pose = new Pose(46, 132, Math.toRadians(6));
    private final Pose pickup3Pose = new Pose(46, 132, Math.toRadians(90));

    private final Pose parkPose = new Pose(64, 97, Math.toRadians(90));

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
                                                new Point(startingPose),
                                                new Point(chamberPose)
                                        )
                                )
                                .setConstantHeadingInterpolation(chamberPose.getHeading())
                                .build()
                        ),
                        new SetPivotAngleCommand(bot.getPivot(), 95),
                        new SetExtensionCommand(bot.getExtension(), 16.0)
                ),
                new DepositCommand(bot),
                new InstantCommand(() -> {
                    bot.setTargetElement(GameElement.SAMPLE);
                    bot.setTargetMode(TargetMode.HIGH_BASKET);
                }),
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierCurve(
                                        new Point(chamberPose),
                                        new Point(0, 121, Point.CARTESIAN),
                                        new Point(pickup1Pose)
                                )
                        )
                        .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(0))
                        .build()
                ),
                new IntakeCommand(bot),
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierLine(
                                        new Point(pickup1Pose),
                                        new Point(basketPose)
                                )
                        )
                        .setLinearHeadingInterpolation(Math.toRadians(0), basketPose.getHeading())
                        .build()
                ),
                new DepositCommand(bot),
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierLine(
                                        new Point(basketPose),
                                        new Point(basketPose)
                                )
                        )
                        .setLinearHeadingInterpolation(basketPose.getHeading(), Math.toRadians(6))
                        .build()
                ),
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierLine(
                                        new Point(basketPose),
                                        new Point(pickup2Pose)
                                )
                        )
                        .setConstantHeadingInterpolation(pickup2Pose.getHeading())
                        .build()
                ),
                new IntakeCommand(bot),
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierLine(
                                        new Point(pickup2Pose),
                                        new Point(basketPose)
                                )
                        )
                        .setLinearHeadingInterpolation(pickup2Pose.getHeading(), basketPose.getHeading())
                        .build()
                ),
                new DepositCommand(bot),
                // potentially do in parallel?
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierLine(
                                        new Point(basketPose),
                                        new Point(pickup3Pose)
                                )
                        )
                        .setLinearHeadingInterpolation(basketPose.getHeading(), pickup3Pose.getHeading())
                        .build()
                ),
                new SetExtensionCommand(bot.getExtension(), 20),
                new ManualWristTwistCommand(bot.getWrist(), Direction.LEFT),
                new ManualWristTwistCommand(bot.getWrist(), Direction.LEFT),
                new IntakeCommand(bot),
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierLine(
                                        new Point(pickup3Pose),
                                        new Point(basketPose)
                                )
                        )
                        .setLinearHeadingInterpolation(pickup3Pose.getHeading(), basketPose.getHeading())
                        .build()
                ),
                new DepositCommand(bot),
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierCurve(
                                        new Point(basketPose),
                                        new Point(64, 128, Point.CARTESIAN),
                                        new Point(parkPose)
                                )
                        )
                        .setLinearHeadingInterpolation(basketPose.getHeading(), parkPose.getHeading())
                        .build()
                ),
                new SetPivotAngleCommand(bot.getPivot(), 95)
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