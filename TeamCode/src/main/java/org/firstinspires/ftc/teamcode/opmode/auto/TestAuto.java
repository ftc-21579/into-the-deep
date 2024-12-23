package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.FollowPathCommand;
import org.firstinspires.ftc.teamcode.common.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.common.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.common.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.common.pedroPathing.pathGeneration.Point;
import org.firstinspires.ftc.teamcode.common.pedroPathing.util.DashboardPoseTracker;
import org.firstinspires.ftc.teamcode.common.pedroPathing.util.Drawing;

@Autonomous(name="TestPPAuto")
public class TestAuto extends LinearOpMode {

    private final Pose startingPose = new Pose(0, 0, 0);

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
                new FollowPathCommand(f, f.pathBuilder() // follow a path
                        .addPath(new BezierLine(new Point(0, 0), new Point(0, 0)))
                        .build()
                ),
                new InstantCommand(() -> {}) // execute something AFTER the path, for parallel actions use a parallel command group
        );

        // dashboard pose stuff
        DashboardPoseTracker tracker = new DashboardPoseTracker(f.poseUpdater);
        Drawing.drawRobot(f.poseUpdater.getPose(), "#4CBB17");
        Drawing.sendPacket();

        waitForStart();

        CommandScheduler.getInstance().schedule(auto);

        while (opModeIsActive()) {

            bot.getFollower().update();

            CommandScheduler.getInstance().run();

            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }
}
