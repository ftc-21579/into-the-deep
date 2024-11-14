package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.mineinjava.quail.util.geometry.Vec2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.automation.AutoSpecimenCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ClawIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ClawOuttakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.DriveTrajectorySequence;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToDepositCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.ManualWristAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.roadrunner.PinpointDrive;

@Autonomous(name = "StraightAuto", group = "Auto")
public class StraightAuto extends LinearOpMode {

    private Bot bot;
    private GamepadEx gamepad;

    private PinpointDrive drive;

    public static double extension1Length = 20, extension2Length = 20;

    public static double startX = -12, startY = -63, startHeading = Math.toRadians(-90);

    //Pose2d beginPose = new Pose2d(0, 0, 0);

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(0, 0, 0);
        //Telemetry telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        //bot = new Bot(telem, hardwareMap, gamepad1);

        PinpointDrive drive = new PinpointDrive(hardwareMap, beginPose);

        // make a funny sequential command group for autonomous here
        //SequentialCommandGroup mainSequence = new SequentialCommandGroup(
                //new DriveTrajectorySequence(drive, builder -> builder
                        //.lineToY(-40)
                        //.build()
                //)
        //);

        waitForStart();

        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        .waitSeconds(1)
                        .lineToX(10)
                        .build()
        );

        //drive.pose = new Pose2d(
                //startX,
                //startY,
                //startHeading
        //);

        //mainSequence.schedule();

        //while(opModeIsActive() && !isStopRequested()){
            //CommandScheduler.getInstance().run();
        //}

    }

}