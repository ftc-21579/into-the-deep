package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.geometry.Vector2d;
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
import org.firstinspires.ftc.teamcode.common.commandbase.command.automation.DepositCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.automation.IntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.ClawIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.intake.ClawOuttakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.FollowPathCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
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

@Config
@Autonomous(name="Sample Auto")
//@Autonomous(name="Sample Auto", preselectTeleOp="TeleOp")
public class SampleAuto extends LinearOpMode {

    // Scoring Poses
    public static Pose startingPose = new Pose(9, 87, Math.toRadians(0));
    public static Pose basketPose = new Pose(13, 126, Math.toRadians(-45));
    public static Pose samplePreloadBasketControl = new Pose(34, 114);
    public static Pose chamberPose = new Pose(38, 75, Math.toRadians(0));
    public static Pose parkPose = new Pose(60, 93, Math.toRadians(90));
    public static Pose parkControl = new Pose(64, 128);

    // Pickup 1
    public static Pose pickup1Pose = new Pose(28, 118, Math.toRadians(0));
    public static Pose pickup1Intermediate = new Pose(24, 116, Math.toRadians(0));
    public static Pose pickup1Control1 = new Pose(10, 96);
    public static Pose pickup1Control2 = new Pose(10, 116);

    // Pickup 2
    public static Pose pickup2Pose = new Pose(28, 128, Math.toRadians(0));
    public static Pose pickup2Intermediate = new Pose(22, 126);

    // Pickup 3
    public static Pose pickup3Pose = new Pose(30, 125, Math.toRadians(42));
    //public static Pose pickup3ToBasketControl = new Pose()

    // Hamburger Special
    public static Pose hamburgerSpecialPose = new Pose(12, 58, Math.toRadians(-110));
    public static Pose hamburgerSpecialControl = new Pose(16, 72);


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
        TargetMode mode = TargetMode.SPEC_INTAKE;
        GameElement preload = GameElement.SPECIMEN;
        boolean hamburgerSpecial = false;

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
                    mode = TargetMode.HIGH_BASKET;
                } else {
                    element = GameElement.SPECIMEN;
                    mode = TargetMode.SPEC_INTAKE;
                }
            }

            if (currentController.square && !previousController.square) {
                if (preload == GameElement.SPECIMEN) {
                    preload = GameElement.SAMPLE;
                } else {
                    preload = GameElement.SPECIMEN;
                }
            }

            if (currentController.triangle && !previousController.triangle) {
                hamburgerSpecial = !hamburgerSpecial;
            }

            if (alliance == Color.RED) {
                bot.getBlinkin().setPattern(RevBlinkinLedDriver.BlinkinPattern.RED);
                gamepad1.setLedColor(255, 0, 0, Gamepad.LED_DURATION_CONTINUOUS);
            } else {
                bot.getBlinkin().setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
                gamepad1.setLedColor(0, 0, 255, Gamepad.LED_DURATION_CONTINUOUS);
            }

            telem.addLine("Change alliance by pressing cross");
            telem.addLine("Change target element by pressing circle");
            telem.addLine("Change preloaded element by pressing square");
            telem.addData("Alliance", alliance);
            telem.addData("Target Element", element);
            telem.addData("Preloaded Element", preload);
            telem.addData("Current Auto", (preload == GameElement.SPECIMEN ? "1+3" : "0+4"));
            telem.addData("Hamburger Special???", hamburgerSpecial);
            telem.update();

        }

        bot.setAllianceColor(alliance);
        GameElement finalElement = element;
        TargetMode finalMode = mode;
        GameElement finalPreload = preload;
        boolean finalHamburgerSpecial = hamburgerSpecial;
        bot.setState(BotState.DEPOSIT);
        bot.setTargetElement(GameElement.SAMPLE);
        bot.setTargetMode(TargetMode.HIGH_BASKET);

        SequentialCommandGroup intake = new SequentialCommandGroup(
                new WaitCommand(250),
                new SetPivotAngleCommand(bot.getPivot(), 0),
                new ClawIntakeCommand(bot.getClaw()),
                new WaitCommand(250),
                new ParallelCommandGroup(
                        new SetExtensionCommand(bot.getExtension(), 0),
                        new SetPivotAngleCommand(bot.getPivot(), 95),
                        new SetWristPositionCommand(bot.getWrist(), new Vector2d(-180, 45))
                )
        );

        SequentialCommandGroup samplePreload = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierCurve(
                                                new Point(startingPose),
                                                new Point(samplePreloadBasketControl),
                                                new Point(basketPose)
                                        )
                                )
                                .setLinearHeadingInterpolation(startingPose.getHeading(), basketPose.getHeading())
                                .build()
                        ),
                        new SequentialCommandGroup(
                                new ClawIntakeCommand(bot.getClaw()),
                                new SetWristPositionCommand(bot.getWrist(), new Vector2d(-180, 45)),
                                new SetPivotAngleCommand(bot.getPivot(), 95),
                                new SetExtensionCommand(bot.getExtension(), Extension.highBasketTarget)
                        )
                ),
                new WaitCommand(250),
                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new DepositCommand(bot),
                                new SetPivotAngleCommand(bot.getPivot(), 12),
                                new SetExtensionCommand(bot.getExtension(), 22)
                        ),
                        new SequentialCommandGroup(
                                new WaitCommand(250),
                                new FollowPathCommand(f, f.pathBuilder()
                                        .addPath(
                                                new BezierCurve(
                                                        new Point(chamberPose),
                                                        new Point(pickup1Control1),
                                                        new Point(pickup1Control2),
                                                        new Point(pickup1Intermediate),
                                                        new Point(pickup1Pose)
                                                )
                                        )
                                        .setConstantHeadingInterpolation(pickup1Pose.getHeading())
                                        .build()
                                )
                        )
                )
        );

        SequentialCommandGroup specimenPreload = new SequentialCommandGroup(
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
                        new ClawIntakeCommand(bot.getClaw()),
                        new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, 55)),
                        new SetPivotAngleCommand(bot.getPivot(), 35),
                        new SetExtensionCommand(bot.getExtension(), 40)
                ),
                // Chamber Score
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierCurve(
                                                new Point(chamberPose),
                                                new Point(pickup1Control1),
                                                new Point(pickup1Control2),
                                                new Point(pickup1Intermediate),
                                                new Point(pickup1Pose)
                                        )
                                )
                                .setConstantHeadingInterpolation(pickup1Pose.getHeading())
                                .build()
                        ),
                        new SequentialCommandGroup(
                                new ClawOuttakeCommand(bot.getClaw()),
                                new SetExtensionCommand(bot.getExtension(), 0),
                                new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, Wrist.wristDown)),
                                new SetPivotAngleCommand(bot.getPivot(), 15),
                                new WaitCommand(500),
                                new SetExtensionCommand(bot.getExtension(), 15)
                        )
                )
        );

        SequentialCommandGroup hamburgerSpecialAuto = new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierCurve(
                                                new Point(startingPose),
                                                new Point(samplePreloadBasketControl),
                                                new Point(basketPose)
                                        )
                                )
                                .setLinearHeadingInterpolation(startingPose.getHeading(), basketPose.getHeading())
                                .build()
                        ),
                        new SequentialCommandGroup(
                                new ClawIntakeCommand(bot.getClaw()),
                                new SetWristPositionCommand(bot.getWrist(), new Vector2d(-180, 45)),
                                new SetPivotAngleCommand(bot.getPivot(), 95),
                                new SetExtensionCommand(bot.getExtension(), Extension.highBasketTarget)
                        )
                ),
                new WaitCommand(250),
                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new DepositCommand(bot),
                                new SetWristPositionCommand(bot.getWrist(), new Vector2d(180 + Math.toDegrees(hamburgerSpecialPose.getHeading()), Wrist.wristDown)),
                                new SetPivotAngleCommand(bot.getPivot(), 12),
                                new SetExtensionCommand(bot.getExtension(), 45)
                        ),
                        new SequentialCommandGroup(
                                new WaitCommand(250),
                                new FollowPathCommand(f, f.pathBuilder()
                                        .addPath(
                                                new BezierCurve(
                                                        new Point(basketPose),
                                                        new Point(hamburgerSpecialControl),
                                                        new Point(hamburgerSpecialPose)
                                                )
                                        )
                                        .setLinearHeadingInterpolation(basketPose.getHeading(), hamburgerSpecialPose.getHeading())
                                        .build()
                                )
                        )
                ),
                new SequentialCommandGroup(
                        new WaitCommand(250),
                        new SetPivotAngleCommand(bot.getPivot(), 0),
                        new ClawIntakeCommand(bot.getClaw()),
                        new WaitCommand(250),
                        new ParallelCommandGroup(
                                new SetExtensionCommand(bot.getExtension(), 0),
                                new SetPivotAngleCommand(bot.getPivot(), 95),
                                new SetWristPositionCommand(bot.getWrist(), new Vector2d(-180, 45))
                        )
                ),
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierCurve(
                                                new Point(hamburgerSpecialPose),
                                                new Point(hamburgerSpecialControl),
                                                new Point(basketPose)
                                        )
                                )
                                .setLinearHeadingInterpolation(hamburgerSpecialPose.getHeading(), basketPose.getHeading())
                                .build()
                        ),
                        new SetExtensionCommand(bot.getExtension(), Extension.highBasketTarget)
                ),
                new WaitCommand(500),
                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new DepositCommand(bot),
                                new SetPivotAngleCommand(bot.getPivot(), 12),
                                new SetExtensionCommand(bot.getExtension(), 22)
                        ),
                        new SequentialCommandGroup(
                                new WaitCommand(250),
                                new FollowPathCommand(f, f.pathBuilder()
                                        .addPath(
                                                new BezierCurve(
                                                        new Point(chamberPose),
                                                        new Point(pickup1Control1),
                                                        new Point(pickup1Control2),
                                                        new Point(pickup1Intermediate),
                                                        new Point(pickup1Pose)
                                                )
                                        )
                                        .setConstantHeadingInterpolation(pickup1Pose.getHeading())
                                        .build()
                                )
                        )
                )
        );

        SequentialCommandGroup auto = new SequentialCommandGroup(
                // start future conditional wrapping
                new ConditionalCommand(
                        hamburgerSpecialAuto,
                        new ConditionalCommand(
                                specimenPreload,
                                samplePreload,
                                () -> finalPreload == GameElement.SPECIMEN
                        ),
                        () -> finalHamburgerSpecial
                ),
                // Pickup 1
                intake,
                new ParallelCommandGroup(
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
                        new SetExtensionCommand(bot.getExtension(), Extension.highBasketTarget)
                ),
                new WaitCommand(500),
                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new DepositCommand(bot),
                                new SetExtensionCommand(bot.getExtension(), 22)
                        ),
                        new SequentialCommandGroup(
                                new WaitCommand(250),
                                new FollowPathCommand(f, f.pathBuilder()
                                        .addPath(
                                                new BezierLine(
                                                        new Point(basketPose),
                                                        new Point(pickup2Intermediate)
                                                )
                                        )
                                        .setLinearHeadingInterpolation(basketPose.getHeading(), pickup2Pose.getHeading())
                                        .addPath(
                                                new BezierLine(
                                                        new Point(pickup2Intermediate),
                                                        new Point(pickup2Pose)
                                                )
                                        )
                                        .setConstantHeadingInterpolation(pickup2Pose.getHeading())
                                        .build()
                                )
                        )
                ),
                intake,
                new ParallelCommandGroup(
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
                        new SetExtensionCommand(bot.getExtension(), Extension.highBasketTarget)
                ),
                new WaitCommand(500),
                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new DepositCommand(bot),
                                new SetWristPositionCommand(bot.getWrist(), new Vector2d(Math.toDegrees(-pickup3Pose.getHeading()), Wrist.wristDown)),
                                new SetExtensionCommand(bot.getExtension(), 30)
                        ),
                        new SequentialCommandGroup(
                                new WaitCommand(250),
                                new FollowPathCommand(f, f.pathBuilder()
                                        .addPath(
                                                new BezierLine(
                                                        new Point(basketPose),
                                                        new Point(pickup3Pose)
                                                )
                                        )
                                        .setLinearHeadingInterpolation(basketPose.getHeading(), pickup3Pose.getHeading())
                                        .build()
                                )
                        )
                ),
                intake,
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierLine(
                                                new Point(pickup3Pose),
                                                new Point(basketPose)
                                        )
                                )
                                .setLinearHeadingInterpolation(pickup3Pose.getHeading(), basketPose.getHeading(), 0.3)
                                .build()
                        ),
                        new SetExtensionCommand(bot.getExtension(), Extension.highBasketTarget)
                ),
                new WaitCommand(500),
                new ClawOuttakeCommand(bot.getClaw()),
                new WaitCommand(250),
                new SetWristPositionCommand(bot.getWrist(), new Vector2d(-180, Wrist.wristForward)),
                new WaitCommand(250),
                new ParallelCommandGroup(
                        new FollowPathCommand(f, f.pathBuilder()
                                .addPath(
                                        new BezierCurve(
                                                new Point(basketPose),
                                                new Point(parkControl),
                                                new Point(parkPose)
                                        )
                                )
                                .setLinearHeadingInterpolation(basketPose.getHeading(), parkPose.getHeading())
                                .build()
                        ),
                        new SequentialCommandGroup(
                                new SetExtensionCommand(bot.getExtension(), 0),
                                new SetPivotAngleCommand(bot.getPivot(), 97, true),
                                new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, 45))
                        )
                ),
                new InstantCommand(() -> {
                    bot.setTargetElement(finalElement);
                    bot.setTargetMode(finalMode);
                })
        );

        // Wait for start and then schedule the auto command sequence
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