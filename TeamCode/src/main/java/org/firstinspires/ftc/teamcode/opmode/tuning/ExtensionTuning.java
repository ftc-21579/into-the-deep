package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.checkerframework.checker.initialization.qual.Initialized;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.RunExtensionPidCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionElevationCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionPositionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

@TeleOp(name = "Extension Tuning", group = "Tuning")
@Config
public class ExtensionTuning extends LinearOpMode {

    public static int evTargetTicks = 0;

    @Override
    public void runOpMode() {
        telemetry = telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Bot bot = new Bot(telemetry, hardwareMap);
        Extension extension = bot.getExtension();

        GamepadEx operator = new GamepadEx(gamepad1);

        waitForStart();

        while (opModeIsActive()) {
            CommandScheduler s = CommandScheduler.getInstance();

            operator.readButtons();

            telemetry.addLine("Extension Tuning - Tune values in Dash");
            telemetry.addLine("192.168.1.43:8080/dash");

            double targetLength = 0.0;

            if (operator.isDown(GamepadKeys.Button.A)) {
                new SetExtensionPositionCommand(extension, 48.0).schedule();
                telemetry.addData("Extension Target Position", 48.0);
                targetLength = 48.0;
            } else if (operator.isDown(GamepadKeys.Button.Y)) {
                new SetExtensionPositionCommand(extension, 12.0).schedule();
                telemetry.addData("Extension Target Position", 12.0);
                targetLength = 12.0;
            }

            telemetry.addData("Extension Current Position", (extension.getExtensionMotorPosition() * 0.2495));
            telemetry.addData("Extension Position Error", targetLength - (extension.getExtensionMotorPosition() * 0.2495));




            new SetExtensionElevationCommand(extension, evTargetTicks).schedule();
            telemetry.addData("Extension Target Elevation", evTargetTicks);
            telemetry.addData("Extension Current Elevation", extension.getElevationMotorPosition());
            telemetry.addData("Extension Elevation Error", evTargetTicks - extension.getElevationMotorPosition());

            s.schedule(new RunExtensionPidCommand(extension));

            telemetry.update();
            s.run();
        }
    }

}