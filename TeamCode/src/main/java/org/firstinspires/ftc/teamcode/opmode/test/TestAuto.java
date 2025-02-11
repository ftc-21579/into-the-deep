package org.firstinspires.ftc.teamcode.opmode.test;

import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.intake1;
import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.intake1Control;
import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.score1;
import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.startingPose;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.FollowPathCommand;
import org.firstinspires.ftc.teamcode.common.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.common.pedroPathing.constants.LConstants;

@Autonomous(name="Test Auto")
public class TestAuto extends LinearOpMode {

    //private final Pose startingPose = new Pose(0, 0, 0);

    @Override
    public void runOpMode() {
        CommandScheduler.getInstance().reset();

        Telemetry telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Bot bot = new Bot(telem, hardwareMap, gamepad1, false);

        //CommandScheduler.getInstance().registerSubsystem(bot.getPivot());
        //CommandScheduler.getInstance().registerSubsystem(bot.getExtension());
        //CommandScheduler.getInstance().registerSubsystem(bot.getWrist());
        //CommandScheduler.getInstance().registerSubsystem(bot.getClaw());

        Constants.setConstants(FConstants.class, LConstants.class);
        Follower f = new Follower(hardwareMap);

        f.setStartingPose(startingPose);
        f.setMaxPower(0.75);

        SequentialCommandGroup auto = new SequentialCommandGroup(
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierLine(
                                        new Point(startingPose),
                                        new Point(new Pose(34, 67, 0))
                                )
                        )
                        .setConstantHeadingInterpolation(score1.getHeading())
                        .build()
                ),
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierCurve(
                                        new Point(score1),
                                        new Point(intake1Control),
                                        new Point(intake1)
                                )
                        )
                        .setLinearHeadingInterpolation(score1.getHeading(), intake1.getHeading())
                        .build()
                )
        );

        // dashboard pose stuff
        //DashboardPoseTracker tracker = new DashboardPoseTracker(f.poseUpdater);
        //Drawing.drawRobot(f.poseUpdater.getPose(), "#4CBB17");
        //Drawing.sendPacket();

        waitForStart();

        CommandScheduler.getInstance().schedule(auto);

        while (opModeIsActive()) {

            f.update();

            CommandScheduler.getInstance().run();

            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }
}
