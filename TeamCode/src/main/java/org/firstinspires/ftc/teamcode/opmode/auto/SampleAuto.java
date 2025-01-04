package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

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

@Config
@Autonomous(name="Sample Auto")
public class SampleAuto extends LinearOpMode {

    public static Pose startingPose = new Pose(9, 87, Math.toRadians(180));
    public static Pose basketPose = new Pose(20, 124, Math.toRadians(-45));
    public static Pose chamberPose = new Pose(30, 74, Math.toRadians(180));

    public static Pose pickup1Pose = new Pose(35, 116, Math.toRadians(0));
    public static Pose pickup2Pose = new Pose(35, 131, Math.toRadians(6));
    public static Pose pickup3Pose = new Pose(48, 128, Math.toRadians(90));

    private final Pose parkPose = new Pose(64, 97, Math.toRadians(90));

    @Override
    public void runOpMode() {
        CommandScheduler.getInstance().reset();

        ElapsedTime timer = new ElapsedTime();
        double lastLoop = timer.milliseconds();

        Telemetry telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Bot bot = new Bot(telem, hardwareMap, gamepad1, false);

        CommandScheduler.getInstance().registerSubsystem(bot.getPivot());
        CommandScheduler.getInstance().registerSubsystem(bot.getExtension());
        CommandScheduler.getInstance().registerSubsystem(bot.getWrist());
        CommandScheduler.getInstance().registerSubsystem(bot.getClaw());

        Follower f = bot.getFollower();

        f.setPose(startingPose);
        f.setMaxPower(0.75);


        SequentialCommandGroup auto = new SequentialCommandGroup(
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
                        new SequentialCommandGroup(
                                new WaitCommand(500)
                                //new SetPivotAngleCommand(bot.getPivot(), 95),
                                //new SetExtensionCommand(bot.getExtension(), 16)
                        )
                ),
                new InstantCommand(() -> {
                    bot.setState(BotState.DEPOSIT);
                    bot.setTargetElement(GameElement.SPECIMEN);
                    bot.setTargetMode(TargetMode.SPEC_DEPOSIT);
                }),
                new ClawIntakeCommand(bot.getClaw()),
                //new WaitCommand(500),
                new DepositCommand(bot),
                new InstantCommand(() -> {
                    bot.setTargetElement(GameElement.SAMPLE);
                    bot.setTargetMode(TargetMode.HIGH_BASKET);
                }),
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierCurve(
                                        new Point(chamberPose),
                                        new Point(10, 96, Point.CARTESIAN),
                                        new Point(10, 116),
                                        new Point(24, 116)
                                )
                        )
                        .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(0))
                        .addPath(
                                new BezierLine(
                                        new Point(24, 121),
                                        new Point(pickup1Pose)
                                )
                        )
                        .setConstantHeadingInterpolation(0)
                        .build()
                ),
                //new WaitCommand(500),
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
                //new WaitCommand(500),
                new DepositCommand(bot),
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierLine(
                                        new Point(basketPose),
                                        new Point(basketPose)
                                )
                        )
                        .setLinearHeadingInterpolation(basketPose.getHeading(), Math.toRadians(6))
                        .addPath(
                                new BezierLine(
                                        new Point(basketPose),
                                        new Point(pickup2Pose)
                                )
                        )
                        .setConstantHeadingInterpolation(pickup2Pose.getHeading())
                        .build()
                ),
                new WaitCommand(500),
                //new IntakeCommand(bot),
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
                new WaitCommand(500),
                //new DepositCommand(bot),
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



        /**
        SequentialCommandGroup auto = new SequentialCommandGroup(
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                        new BezierLine(
                                new Point(0, 0),
                                new Point(48, 0)
                        )
                        )
                        .build()
                )
        );
         */

        // dashboard pose stuff
        //DashboardPoseTracker tracker = new DashboardPoseTracker(f.poseUpdater);
        //Drawing.drawRobot(f.poseUpdater.getPose(), "#4CBB17");
        //Drawing.sendPacket();

        waitForStart();

        CommandScheduler.getInstance().schedule(auto);

        while (opModeIsActive()) {

            //f.poseUpdater.update();
            //tracker.update();
            //Drawing.drawPoseHistory(tracker, "#4Cbb17");
            //Drawing.drawRobot(f.poseUpdater.getPose(), "#4CBB17");
            //Drawing.sendPacket();

            CommandScheduler.getInstance().run();
            bot.getFollower().update();

            bot.getFollower().telemetryDebug(telem);

            //telem.addData("loop", timer.milliseconds() - lastLoop);
            lastLoop = timer.milliseconds();
            //telem.addData("Status", "Running");
            //telem.update();
        }
    }
}