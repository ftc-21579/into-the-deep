package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.FollowPathCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.common.pedroPathing.constants.LConstants;

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

        Constants.setConstants(FConstants.class, LConstants.class);
        Follower f = new Follower(hardwareMap);

        f.setStartingPose(startingPose);
        f.setMaxPower(0.75);

        SequentialCommandGroup auto = new SequentialCommandGroup(
                new SetExtensionCommand(bot.getExtension(), 10)
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
