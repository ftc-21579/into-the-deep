package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.mineinjava.quail.util.geometry.Vec2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.TeleOpDriveCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionElevatorPowerCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionPowerCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.manipulator.SetGripperPowerCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.manipulator.SetWristPowerCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Manipulator;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;

import java.util.List;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "0")
public class TeleOp extends LinearOpMode {

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Bot bot = new Bot(telemetry, hardwareMap);
        MecanumDrivetrain drivetrain = bot.getDrivetrain();
        Extension extension = bot.getExtension();
        Manipulator manipulator = bot.getManipulator();

        GamepadEx driver = new GamepadEx(gamepad1);
        GamepadEx operator = new GamepadEx(gamepad2);

        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule module : allHubs) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        waitForStart();

        while (opModeIsActive()) {
            CommandScheduler s = CommandScheduler.getInstance();

            driver.readButtons();
            operator.readButtons();

            s.schedule(new TeleOpDriveCommand(drivetrain,
                    new Vec2d(driver.getLeftX(), driver.getRightX()),
                    -driver.getLeftY(), 1.0));

            s.schedule(new SetExtensionPowerCommand(extension,
                    driver.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) -
                            driver.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)));
            telemetry.addData("Extension Power",
                    driver.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) -
                    driver.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER));

            /*s.schedule(new SetExtensionElevatorPowerCommand(extension,
                    operator.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) -
                            operator.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)));
            telemetry.addData("Elevator Power",
                    operator.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) -
                    operator.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER));*/

            if (driver.isDown(GamepadKeys.Button.RIGHT_BUMPER)) {
                s.schedule(new SetExtensionElevatorPowerCommand(extension, 0.5));
            } else if (driver.isDown(GamepadKeys.Button.LEFT_BUMPER)) {
                s.schedule(new SetExtensionElevatorPowerCommand(extension, -0.5));
            } else {
                s.schedule(new SetExtensionElevatorPowerCommand(extension, 0.0));
            }


            if (driver.isDown(GamepadKeys.Button.A)) {
                s.schedule(new SetGripperPowerCommand(manipulator, 0.25));
            } else if (driver.isDown(GamepadKeys.Button.Y)) {
                s.schedule(new SetGripperPowerCommand(manipulator, -0.25));
            } else {
                s.schedule(new SetGripperPowerCommand(manipulator, 0.0));
            }

            if (driver.isDown(GamepadKeys.Button.DPAD_DOWN)) {
                s.schedule(new SetWristPowerCommand(manipulator, 0.25));
            } else if (driver.isDown(GamepadKeys.Button.DPAD_UP)) {
                s.schedule(new SetWristPowerCommand(manipulator, -0.25));
            } else {
                s.schedule(new SetWristPowerCommand(manipulator, 0.0));
            }


            telemetry.update();
            s.run();
        }
    }
}
