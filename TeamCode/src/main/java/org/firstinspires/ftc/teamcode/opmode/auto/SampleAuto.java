package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
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

@Autonomous(name = "SampleAuto", group = "Auto")
public class SampleAuto extends LinearOpMode {

    private Bot bot;
    private GamepadEx gamepad;

    private PinpointDrive drive;

    public static double extension1Length = 20, extension2Length = 20;

    public static double startX = -12, startY = -48, startHeading = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        Telemetry telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        bot = new Bot(telem, hardwareMap, gamepad1, false);

        drive = new PinpointDrive(hardwareMap, new Pose2d(
                startX,
                startY,
                startHeading
        ));

        // make a funny sequential command group for autonomous here
        SequentialCommandGroup mainSequence = new SequentialCommandGroup(
                new DriveTrajectorySequence(drive, builder -> builder
                        .setTangent(Math.toRadians(90))
                        .splineToConstantHeading(new Vector2d(-6, -32), Math.toRadians(90))
                        .build()
                ),
                new AutoSpecimenCommand(bot),
                new DriveTrajectorySequence(drive, builder -> builder
                    .splineToLinearHeading(new Pose2d(-48, -48, Math.toRadians(90)), Math.toRadians(90))
                    .build()
                ),
                new ToIntakeCommand(bot),
                new SetExtensionCommand(bot.getExtension(), extension1Length),
                new SetPivotAngleCommand(bot.getPivot(), 0),
                new ClawIntakeCommand(bot.getClaw()),
                new WaitCommand(500),
                new ToIntakeCommand(bot),
                new ParallelCommandGroup(
                        new DriveTrajectorySequence(drive, builder -> builder
                                .turn(45)
                                .build()
                        ),
                        new ToDepositCommand(bot)
                ),
                new SetExtensionCommand(bot.getExtension(), 60.0),
                new DriveTrajectorySequence(drive, builder -> builder
                        .strafeTo(new Vector2d(-56, -56))
                        .build()
                ),
                new ClawOuttakeCommand(bot.getClaw()),
                new WaitCommand(500),
                new SetWristPositionCommand(bot.getWrist(), new Vec2d(0, 90)),
                new ParallelCommandGroup(
                        new ToIntakeCommand(bot),
                        new DriveTrajectorySequence(drive, builder -> builder
                                .strafeToLinearHeading(new Vector2d(-58, -48), Math.toRadians(90))
                                .build()
                        )
                ),
                new SetExtensionCommand(bot.getExtension(), extension2Length),
                new SetPivotAngleCommand(bot.getPivot(), 0),
                new ClawIntakeCommand(bot.getClaw()),
                new WaitCommand(500),
                new ToIntakeCommand(bot),
                new ParallelCommandGroup(
                        new DriveTrajectorySequence(drive, builder -> builder
                                .strafeToLinearHeading(new Vector2d(-54, -54), Math.toRadians(45))
                                .build()
                        ),
                        new ToDepositCommand(bot)
                ),
                new SetExtensionCommand(bot.getExtension(), 60.0),
                new DriveTrajectorySequence(drive, builder -> builder
                        .strafeTo(new Vector2d(-56, -56))
                        .build()
                ),
                new ClawOuttakeCommand(bot.getClaw()),
                new WaitCommand(500),
                new SetWristPositionCommand(bot.getWrist(), new Vec2d(0, 90)),
                new ParallelCommandGroup(
                        new ToIntakeCommand(bot),
                        new DriveTrajectorySequence(drive, builder -> builder
                                .strafeToLinearHeading(new Vector2d(-58, -48), Math.toRadians(90))
                                .build()
                        )
                )
        );

        waitForStart();

        drive.pose = new Pose2d(
                startX,
                startY,
                startHeading
        );

        mainSequence.schedule();

        while(opModeIsActive() && !isStopRequested()){
            CommandScheduler.getInstance().run();
        }

    }

}
