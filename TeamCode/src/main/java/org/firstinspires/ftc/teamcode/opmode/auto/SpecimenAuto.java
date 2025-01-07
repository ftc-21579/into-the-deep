package org.firstinspires.ftc.teamcode.opmode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.common.pedroPathing.constants.LConstants;

@Autonomous(name="Specimen Auto")
//@Autonomous(name="Specimen Auto", preselectTeleOp="TeleOp")
public class SpecimenAuto extends LinearOpMode {

    public static Pose startingPose = new Pose(9, 55, 0);

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

        f.setPose(startingPose);
        f.setMaxPower(0.75);

        SequentialCommandGroup auto = new SequentialCommandGroup(
                new WaitCommand(500)
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
