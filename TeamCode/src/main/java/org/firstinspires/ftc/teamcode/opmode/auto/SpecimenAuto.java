package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.fasterxml.jackson.databind.ext.SqlBlobSerializer;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.ClawIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.ClawOuttakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.FollowPathCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.ManualPivotCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.ManualWristTwistCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Wrist;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;
import org.firstinspires.ftc.teamcode.common.intothedeep.Color;
import org.firstinspires.ftc.teamcode.common.intothedeep.Direction;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;
import org.firstinspires.ftc.teamcode.common.intothedeep.TargetMode;
import org.firstinspires.ftc.teamcode.common.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.common.pedroPathing.constants.LConstants;

@Autonomous(name="Specimen Auto")
//@Autonomous(name="Specimen Auto", preselectTeleOp="TeleOp")
public class SpecimenAuto extends LinearOpMode {

    public static Pose startingPose = new Pose(9, 55, 0);
    public static Pose parkPose = new Pose(18, 26, -135);

    public static Pose score1 = new Pose(38, 67, 0);
    public static Pose score2 = new Pose(38, 79, Math.toRadians(180));
    public static Pose score3 = new Pose(38, 77, Math.toRadians(180));
    public static Pose score4 = new Pose(38, 75, Math.toRadians(180));
    public static Pose score5 = new Pose(38, 73, Math.toRadians(180));

    public static Pose specIntake = new Pose(17, 26, Math.toRadians(180));

    public static Pose scoreControl = new Pose(24, 60, Math.toRadians(180));
    public static Pose scoreControlBack = new Pose(32, 26, Math.toRadians(180));

    public static Pose intake1 = new Pose(32, 44, Math.toRadians(-58));
    public static Pose intake1Control = new Pose(9, 55);
    public static Pose intake1Shuttle = new Pose(30, 40, Math.toRadians(-140));

    public static Pose intake2 = new Pose(33, 33, Math.toRadians(-60));
    public static Pose intake2Shuttle = new Pose(30, 40, Math.toRadians(-140));

    public static Pose intake3 = new Pose(31.5, 25, Math.toRadians(-54));
    public static Pose intake3Shuttle = new Pose(19, 26, Math.toRadians(180));
    public static Pose intake3ShuttleControl = new Pose(30, 24);


    @Override
    public void runOpMode() {
        CommandScheduler.getInstance().reset();

        Telemetry telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Bot bot = new Bot(telem, hardwareMap, gamepad1, false);

        Gamepad currentController = new Gamepad();
        Gamepad previousController = new Gamepad();

        VoltageSensor vs = hardwareMap.voltageSensor.iterator().next();

        CommandScheduler.getInstance().registerSubsystem(bot.getPivot());
        CommandScheduler.getInstance().registerSubsystem(bot.getExtension());
        CommandScheduler.getInstance().registerSubsystem(bot.getWrist());
        CommandScheduler.getInstance().registerSubsystem(bot.getClaw());

        Constants.setConstants(FConstants.class, LConstants.class);
        Follower f = new Follower(hardwareMap);

        f.setPose(startingPose);
        f.setMaxPower(1.0);

        Color alliance = Color.RED;
        GameElement element = GameElement.SPECIMEN;

        // Allow changing of preload for conditional (coming soon)
        while (opModeInInit()) {
            previousController.copy(currentController);
            currentController.copy(gamepad1);

            if (currentController.cross && !previousController.cross) {
                if (alliance == Color.RED) {
                    alliance = Color.BLUE;
                } else {
                    alliance = Color.RED;
                }
            }

            if (currentController.circle && !previousController.circle) {
                if (element == GameElement.SPECIMEN) {
                    element = GameElement.SAMPLE;
                } else {
                    element = GameElement.SPECIMEN;
                }
            }

            if (alliance == Color.RED) {
                bot.getBlinkin().setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
                gamepad1.setLedColor(255, 0, 0, Gamepad.LED_DURATION_CONTINUOUS);
            } else {
                bot.getBlinkin().setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
                gamepad1.setLedColor(0, 0, 255, Gamepad.LED_DURATION_CONTINUOUS);
            }

            telem.addLine("Change alliance by pressing cross");
            telem.addLine("Change element by pressing circle");
            telem.addData("Alliance", alliance);
            telem.addData("Element", element);
            telem.addLine("Current Auto : 5+0");
            telem.update();

        }

        bot.setAllianceColor(alliance);
        GameElement finalElement = element;
        bot.setTargetElement(GameElement.SPECIMEN);
        bot.setTargetMode(TargetMode.SPEC_INTAKE);
        bot.setState(BotState.INTAKE);

        ParallelCommandGroup ScorePositionCommand = new ParallelCommandGroup(
                new SetPivotAngleCommand(bot.getPivot(), 36),
                new SetExtensionCommand(bot.getExtension(), 40),
                new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, 55))
        );

        SequentialCommandGroup ScoreForwardsCommand = new SequentialCommandGroup(
                new ClawOuttakeCommand(bot.getClaw()),
                new SetExtensionCommand(bot.getExtension(), 0),
                new SetWristPositionCommand(bot.getWrist(), new Vector2d(58, Wrist.wristDown)),
                new SetPivotAngleCommand(bot.getPivot(), 10),
                new WaitCommand(500),
                new SetExtensionCommand(bot.getExtension(), 36)
        );

        SequentialCommandGroup IntakeSpecimenCommand = new SequentialCommandGroup(
                new WaitCommand(250),
                new ClawIntakeCommand(bot.getClaw()),
                new WaitCommand(250),
                new SetWristPositionCommand(bot.getWrist(), new Vector2d(-180, Wrist.wristUp))
        );

        SequentialCommandGroup auto = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(startingPose),
                                                new Point(score1)
                                        )
                                )
                                .setConstantHeadingInterpolation(score1.getHeading())
                                .build()
                        ),
                        new ClawIntakeCommand(bot.getClaw()),
                        ScorePositionCommand
                ),
                new ParallelCommandGroup(
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
                        ),
                        ScoreForwardsCommand
                ),
                new WaitCommand(250),
                new SetPivotAngleCommand(bot.getPivot(), 0),
                new ClawIntakeCommand(bot.getClaw()),
                new WaitCommand(250),
                new ManualPivotCommand(bot.getPivot(), Direction.UP),
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierLine(
                                        new Point(intake1),
                                        new Point(intake1Shuttle)
                                )
                        )
                        .setLinearHeadingInterpolation(intake1.getHeading(), intake1Shuttle.getHeading())
                        .build()
                ),
                new WaitCommand(150),
                new ClawOuttakeCommand(bot.getClaw()),
                new WaitCommand(150),
                new SetWristPositionCommand(bot.getWrist(), new Vector2d(60, Wrist.wristDown)),
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierLine(
                                        new Point(intake1Shuttle),
                                        new Point(intake2)
                                )
                        )
                        .setLinearHeadingInterpolation(intake1Shuttle.getHeading(), intake2.getHeading())
                        .build()
                ),
                new WaitCommand(250),
                new SetPivotAngleCommand(bot.getPivot(), 0),
                new ClawIntakeCommand(bot.getClaw()),
                new WaitCommand(250),
                new ManualPivotCommand(bot.getPivot(), Direction.UP),
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierLine(
                                        new Point(intake2),
                                        new Point(intake2Shuttle)
                                )
                        )
                        .setLinearHeadingInterpolation(intake2.getHeading(), intake2Shuttle.getHeading())
                        .build()
                ),
                new WaitCommand(150),
                new ClawOuttakeCommand(bot.getClaw()),
                new WaitCommand(150),
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(intake2Shuttle),
                                                new Point(intake3)
                                        )
                                )
                                .setLinearHeadingInterpolation(intake2Shuttle.getHeading(), intake3.getHeading(), 0.5)
                                .build()
                        ),
                        new SetWristPositionCommand(bot.getWrist(), new Vector2d(54, Wrist.wristDown)),
                        new SetExtensionCommand(bot.getExtension(), 40)
                ),
                new WaitCommand(250),
                new SetPivotAngleCommand(bot.getPivot(), 0),
                new ClawIntakeCommand(bot.getClaw()),
                new WaitCommand(250),
                new ManualPivotCommand(bot.getPivot(), Direction.UP),
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierCurve(
                                                new Point(intake3),
                                                new Point(intake3ShuttleControl),
                                                new Point(specIntake)
                                        )
                                )
                                .setLinearHeadingInterpolation(intake3.getHeading(), specIntake.getHeading(), 0.6)
                                .build()
                        ),
                        new SetExtensionCommand(bot.getExtension(), 16),
                        new SetPivotAngleCommand(bot.getPivot(), 20),
                        new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, Wrist.wristDown))
                ),
                new ClawOuttakeCommand(bot.getClaw()),
                new WaitCommand(150),
                new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, 105)),
                IntakeSpecimenCommand,
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierCurve(
                                                new Point(specIntake),
                                                new Point(scoreControl),
                                                new Point(score2)
                                        )
                                )
                                .setConstantHeadingInterpolation(score2.getHeading())
                                .build()
                        ),
                        new SequentialCommandGroup(
                                new SetExtensionCommand(bot.getExtension(), 0),
                                new SetPivotAngleCommand(bot.getPivot(), 95),
                                new SetExtensionCommand(bot.getExtension(), Extension.highChamberTarget)
                        )
                ),
                new SetExtensionCommand(bot.getExtension(), 0),
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierCurve(
                                                new Point(score2),
                                                new Point(scoreControlBack),
                                                new Point(specIntake)
                                        )
                                )
                                .setConstantHeadingInterpolation(specIntake.getHeading())
                                .build()
                        ),
                        new ClawOuttakeCommand(bot.getClaw()),
                        new SetPivotAngleCommand(bot.getPivot(), 20),
                        new SetExtensionCommand(bot.getExtension(), 16),
                        new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, 105))
                ),
                IntakeSpecimenCommand,
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierCurve(
                                                new Point(specIntake),
                                                new Point(scoreControl),
                                                new Point(score3)
                                        )
                                )
                                .setConstantHeadingInterpolation(score3.getHeading())
                                .build()
                        ),
                        new SequentialCommandGroup(
                                new SetExtensionCommand(bot.getExtension(), 0),
                                new SetPivotAngleCommand(bot.getPivot(), 95),
                                new SetExtensionCommand(bot.getExtension(), Extension.highChamberTarget)
                        )
                ),
                new SetExtensionCommand(bot.getExtension(), 0),
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierCurve(
                                                new Point(score3),
                                                new Point(scoreControlBack),
                                                new Point(specIntake)
                                        )
                                )
                                .setConstantHeadingInterpolation(specIntake.getHeading())
                                .build()
                        ),
                        new ClawOuttakeCommand(bot.getClaw()),
                        new SetPivotAngleCommand(bot.getPivot(), 20),
                        new SetExtensionCommand(bot.getExtension(), 16),
                        new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, 105))
                ),
                IntakeSpecimenCommand,
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierCurve(
                                                new Point(specIntake),
                                                new Point(scoreControl),
                                                new Point(score4)
                                        )
                                )
                                .setConstantHeadingInterpolation(score4.getHeading())
                                .build()
                        ),
                        new SequentialCommandGroup(
                                new SetExtensionCommand(bot.getExtension(), 0),
                                new SetPivotAngleCommand(bot.getPivot(), 95),
                                new SetExtensionCommand(bot.getExtension(), Extension.highChamberTarget)
                        )
                ),
                new SetExtensionCommand(bot.getExtension(), 0),
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierCurve(
                                                new Point(score4),
                                                new Point(scoreControlBack),
                                                new Point(specIntake)
                                        )
                                )
                                .setConstantHeadingInterpolation(specIntake.getHeading())
                                .build()
                        ),
                        new ClawOuttakeCommand(bot.getClaw()),
                        new SetPivotAngleCommand(bot.getPivot(), 20),
                        new SetExtensionCommand(bot.getExtension(), 16),
                        new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, 105))
                ),
                IntakeSpecimenCommand,
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierCurve(
                                                new Point(specIntake),
                                                new Point(scoreControl),
                                                new Point(score5)
                                        )
                                )
                                .setConstantHeadingInterpolation(score5.getHeading())
                                .build()
                        ),
                        new SequentialCommandGroup(
                                new SetExtensionCommand(bot.getExtension(), 0),
                                new SetPivotAngleCommand(bot.getPivot(), 95),
                                new SetExtensionCommand(bot.getExtension(), Extension.highChamberTarget)
                        )
                ),
                new SetExtensionCommand(bot.getExtension(), 0),
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(score5),
                                                new Point(parkPose)
                                        )
                                )
                                .setLinearHeadingInterpolation(score5.getHeading(), parkPose.getHeading())
                                .build()
                        ),
                        new ClawOuttakeCommand(bot.getClaw()),
                        new SetPivotAngleCommand(bot.getPivot(), 10),
                        new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, Wrist.wristDown))
                ),
                new InstantCommand(() -> {
                    bot.setTargetElement(finalElement);
                })
        );

        // Wait for start and schedule auto command group
        waitForStart();
        CommandScheduler.getInstance().schedule(auto);

        // Opmode loop
        while (opModeIsActive()) {
            CommandScheduler.getInstance().run();
            f.update();
            f.telemetryDebug(telem);
        }
    }
}
