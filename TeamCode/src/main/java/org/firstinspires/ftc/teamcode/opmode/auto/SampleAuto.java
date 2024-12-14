package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.mineinjava.quail.util.geometry.Vec2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.automation.AutoSpecimenCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ClawIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ClawOuttakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ToggleClawCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.DriveTrajectorySequence;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.ManualPivotDownCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.AutoToDepositCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.AutoToIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToDepositCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToSpecimenDepositCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToSpecimenIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.common.roadrunner.PinpointDrive;
import org.firstinspires.ftc.teamcode.opmode.auto.command.SpecimenSampleIntakeCommand;

@Autonomous(name = "SampleAuto", group = "Auto")
public class SampleAuto extends LinearOpMode {

    private Bot bot;
    private GamepadEx gamepad;

    private PinpointDrive drive;

    public static double extension1Length = 20, extension2Length = 20;

    public static double startX = -12, startY = -63, startHeading = Math.toRadians(-90);

    @Override
    public void runOpMode() throws InterruptedException {
        Telemetry telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        bot = new Bot(telem, hardwareMap, gamepad1, false);

        CommandScheduler.getInstance().registerSubsystem(bot.getPivot());
        CommandScheduler.getInstance().registerSubsystem(bot.getExtension());
        CommandScheduler.getInstance().registerSubsystem(bot.getWrist());
        CommandScheduler.getInstance().registerSubsystem(bot.getClaw());

        drive = new PinpointDrive(hardwareMap, new Pose2d(
                startX,
                startY,
                startHeading
        ));

        // make a funny sequential command group for autonomous here
        SequentialCommandGroup mainSequence = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new ClawOuttakeCommand(bot.getClaw()),
                        new ToSpecimenDepositCommand(bot),
                        new DriveTrajectorySequence(drive, builder -> builder
                                .setTangent(Math.toRadians(90))
                                .splineToConstantHeading(new Vector2d(-6, -34), Math.toRadians(90))
                                .build()
                        )
                ),
                new SetExtensionCommand(bot.getExtension(), 0.0),
                new WaitCommand(250),
                new ToggleClawCommand(bot.getClaw()),
                new ParallelCommandGroup(
                        new SpecimenSampleIntakeCommand(bot, 10, new Vec2d(0, 230)),
                        new DriveTrajectorySequence(drive, builder -> builder
                                .splineToLinearHeading(new Pose2d(-46, -42, Math.toRadians(90)), Math.toRadians(90))
                                .build()
                        )
                ),
                new ManualPivotDownCommand(bot, bot.getPivot()),
                new WaitCommand(250),
                new ToggleClawCommand(bot.getClaw()),
                new WaitCommand(250),
                new ParallelCommandGroup(
                        new AutoToDepositCommand(bot, 95, 60),
                        new DriveTrajectorySequence(drive, builder -> builder
                                .strafeToLinearHeading(new Vector2d(-50, -56), Math.toRadians(45))
                                .build()
                        )
                ),
                new WaitCommand(1000),
                new ToggleClawCommand(bot.getClaw()),
                new WaitCommand(250),
                new ParallelCommandGroup(
                        new AutoToIntakeCommand(bot, 12.0, new Vec2d(0, 230)),
                        new DriveTrajectorySequence(drive, builder -> builder
                                .splineToLinearHeading(new Pose2d(-57, -41, Math.toRadians(90)), Math.toRadians(90))
                                .build()
                        )
                ),
                new ManualPivotDownCommand(bot, bot.getPivot()),
                new WaitCommand(250),
                new ToggleClawCommand(bot.getClaw()),
                new WaitCommand(250),
                new ParallelCommandGroup(
                        new AutoToDepositCommand(bot, 95, 60),
                        new DriveTrajectorySequence(drive, builder -> builder
                                .strafeToLinearHeading(new Vector2d(-50, -56), Math.toRadians(45))
                                .build()
                        )
                ),
                new WaitCommand(1000),
                new ToggleClawCommand(bot.getClaw()),
                new WaitCommand(250),
                new ParallelCommandGroup(
                        new AutoToIntakeCommand(bot, 20.0, new Vec2d(-30, 230)),
                        new DriveTrajectorySequence(drive, builder -> builder
                                .splineToLinearHeading(new Pose2d(-57, -42, Math.toRadians(120)), Math.toRadians(120))
                                .build()
                        )
                ),
                new ManualPivotDownCommand(bot, bot.getPivot()),
                new WaitCommand(250),
                new ToggleClawCommand(bot.getClaw()),
                new WaitCommand(250),
                new ParallelCommandGroup(
                        new AutoToDepositCommand(bot, 95, 60),
                        new DriveTrajectorySequence(drive, builder -> builder
                                .strafeToLinearHeading(new Vector2d(-50, -56), Math.toRadians(45))
                                .build()
                        )
                ),
                new WaitCommand(1000),
                new ToggleClawCommand(bot.getClaw()),
                new WaitCommand(250),
                new ParallelCommandGroup(
                        new ToIntakeCommand(bot),
                        new DriveTrajectorySequence(drive, builder -> builder
                                .splineToLinearHeading(new Pose2d(-22, -12, Math.toRadians(180)), Math.toRadians(0))
                                .build()
                        )
                ),
                new SetPivotAngleCommand(bot.getPivot(), 95),
                new SetWristPositionCommand(bot.getWrist(), new Vec2d(0, 120))

        );

        waitForStart();

        drive.pose = new Pose2d(
                startX,
                startY,
                startHeading
        );

        new SetPivotAngleCommand(bot.getPivot(), 0).schedule();

        mainSequence.schedule();

        while(opModeIsActive() && !isStopRequested()){
            CommandScheduler.getInstance().run();
        }

        MecanumDrivetrain.pose = new Pose2D(
                DistanceUnit.INCH,
                drive.pose.position.x,
                drive.pose.position.y,
                AngleUnit.RADIANS,
                drive.pose.heading.real
        );

        //bot.getPivot().setSetpointDEG(10);

        CommandScheduler.getInstance().cancelAll();
        CommandScheduler.getInstance().unregisterSubsystem(bot.getClaw());
        CommandScheduler.getInstance().unregisterSubsystem(bot.getExtension());
        CommandScheduler.getInstance().unregisterSubsystem(bot.getPivot());
        CommandScheduler.getInstance().unregisterSubsystem(bot.getWrist());

    }

}