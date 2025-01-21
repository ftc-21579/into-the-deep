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
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;
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
    public static Pose basketPose = new Pose(18, 121, Math.toRadians(-45));
    public static Pose samplePreloadBasketControl = new Pose(34, 114);
    public static Pose chamberPose = new Pose(36, 75, Math.toRadians(0));
    public static Pose parkPose = new Pose(60, 92, Math.toRadians(90));
    public static Pose parkControl = new Pose(64, 128);

    // Pickup 1
    public static Pose pickup1Pose = new Pose(36, 117, Math.toRadians(0));
    public static Pose pickup1Intermediate = new Pose(24, 116, Math.toRadians(0));
    public static Pose pickup1Control1 = new Pose(10, 96);
    public static Pose pickup1Control2 = new Pose(10, 116);

    // Pickup 2
    public static Pose pickup2Pose = new Pose(36, 126, Math.toRadians(0));
    public static Pose pickup2Intermidiate = new Pose(30, 126);

    // Pickup 3
    public static Pose pickup3Pose = new Pose(46, 124, Math.toRadians(90));
    //public static Pose pickup3ToBasketControl = new Pose()


    @Override
    public void runOpMode() {
        CommandScheduler.getInstance().reset();

        Telemetry telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Bot bot = new Bot(telem, hardwareMap, gamepad1, false);

        GamepadEx controller = new GamepadEx(gamepad1);
        VoltageSensor vs = hardwareMap.voltageSensor.iterator().next();

        CommandScheduler.getInstance().registerSubsystem(bot.getPivot());
        CommandScheduler.getInstance().registerSubsystem(bot.getExtension());
        CommandScheduler.getInstance().registerSubsystem(bot.getWrist());
        CommandScheduler.getInstance().registerSubsystem(bot.getClaw());

        //Constants.setConstants(FConstants.class, LConstants.class);
        //Follower f = new Follower(hardwareMap);
        Follower f = bot.getDrivetrain().getFollower();

        f.setPose(startingPose);
        f.setMaxPower(0.75);

        GameElement preload = GameElement.SPECIMEN;

        // Allow changing of preload for conditional (coming soon)
        while (opModeInInit()) {
            if (gamepad1.a) {
                if (preload == GameElement.SPECIMEN) {
                    preload = GameElement.SAMPLE;
                } else {
                    preload = GameElement.SPECIMEN;
                }
            }

            telem.addLine("Change preloaded element by pressing X");
            telem.addData("Preloaded Element", preload);
            telem.addData("Current Auto", (preload == GameElement.SPECIMEN ? "1+3" : "0+4"));
            telem.update();

        }

        GameElement finalPreload = preload;
        SequentialCommandGroup auto = new SequentialCommandGroup(
                // start future conditional wrapping
                new ConditionalCommand(
                        new SequentialCommandGroup(
                        // Chamber Setup/Drive
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
                                        new ClawIntakeCommand(bot.getClaw()),
                                        new SetPivotAngleCommand(bot.getPivot(), 38 * (vs.getVoltage() / 13.5)),
                                        new SetExtensionCommand(bot.getExtension(), 40),
                                        new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, 80))
                                )
                        ),
                        new InstantCommand(() -> {
                                bot.setState(BotState.DEPOSIT);
                                bot.setTargetElement(GameElement.SPECIMEN);
                                bot.setTargetMode(TargetMode.SPEC_DEPOSIT);
                        }),
                        //new WaitCommand(500),
                        // Chamber Score
                        new ParallelCommandGroup(
                                new SequentialCommandGroup(
                                        //new SetPivotAngleCommand(bot.getPivot(), 35),
                                        //new WaitCommand(500),
                                        new ClawOuttakeCommand(bot.getClaw()),
                                        new SetExtensionCommand(bot.getExtension(), 0),
                                        new WaitCommand(500),
                                        new SetPivotAngleCommand(bot.getPivot(), 15),
                                        new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, 270))
                                ),
                                new WaitCommand(500)
                        )
                        ),
                        // Sample Drive
                        new SequentialCommandGroup(
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
                                        new SetPivotAngleCommand(bot.getPivot(), 85),
                                        new SetExtensionCommand(bot.getExtension(), 60),
                                        new SetWristPositionCommand(bot.getWrist(), new Vector2d(-180, 90))
                                )
                        ),
                        new InstantCommand(() -> {
                                bot.setState(BotState.DEPOSIT);
                                bot.setTargetElement(GameElement.SAMPLE);
                                bot.setTargetMode(TargetMode.HIGH_BASKET);
                        }),
                        new DepositCommand(bot),
                        new WaitCommand(500)
                        ),
                        () -> finalPreload == GameElement.SPECIMEN
                ),
                // End future conditional wrapping
                new InstantCommand(() -> {
                    bot.setTargetElement(GameElement.SAMPLE);
                    bot.setTargetMode(TargetMode.HIGH_BASKET);
                }),
                // Pickup 1
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierCurve(
                                        new Point(chamberPose),
                                        new Point(pickup1Control1),
                                        new Point(pickup1Control2),
                                        new Point(pickup1Intermediate)
                                )
                        )
                        .setConstantHeadingInterpolation(pickup1Pose.getHeading())
                        .addPath(
                                new BezierLine(
                                        new Point(pickup1Intermediate),
                                        new Point(pickup1Pose)
                                )
                        )
                        .setConstantHeadingInterpolation(pickup1Pose.getHeading())
                        .build()
                ),
                new WaitCommand(100),
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
                new WaitCommand(500),
                // Pickup 2
                new FollowPathCommand(f, f.pathBuilder()
                        .addPath(
                                new BezierLine(
                                        new Point(basketPose),
                                        new Point(pickup2Intermidiate)
                                )
                        )
                        .setLinearHeadingInterpolation(basketPose.getHeading(), pickup2Pose.getHeading(), 0.5)
                        .addPath(
                                new BezierLine(
                                        new Point(pickup2Intermidiate),
                                        new Point(pickup2Pose)
                                )
                        )
                        .setConstantHeadingInterpolation(pickup2Pose.getHeading())
                        .build()
                ),
                new WaitCommand(100),
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
                new WaitCommand(500),
                // Pickup 3
                // potentially do in parallel?
                new ParallelCommandGroup(
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
                        new SequentialCommandGroup(
                                new SetExtensionCommand(bot.getExtension(), 10),
                                new ManualWristTwistCommand(bot.getWrist(), Direction.LEFT),
                                new ManualWristTwistCommand(bot.getWrist(), Direction.LEFT)
                        )
                ),
                new WaitCommand(100),
                new IntakeCommand(bot),
                //new WaitCommand(200),
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
                new DepositCommand(bot),
                new WaitCommand(500),
                // Park
                new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, 135)),
                new SetPivotAngleCommand(bot.getPivot(), 95, true),
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
                )
        );

        // Wait for start and then schedule the auto command sequence
        waitForStart();
        CommandScheduler.getInstance().schedule(auto);

        // Opmode loop
        while (opModeIsActive()) {
            CommandScheduler.getInstance().run();
            f.setMaxPower(0.75 * (13.5 / vs.getVoltage()));
            f.update();

            f.telemetryDebug(telem);
        }
    }
}