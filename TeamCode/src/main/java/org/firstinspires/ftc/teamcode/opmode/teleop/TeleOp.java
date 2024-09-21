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
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.RunExtensionPidCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionElevationCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionElevatorPowerCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionPowerCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.manipulator.DepositCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.manipulator.IdleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.manipulator.IntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.manipulator.WristDownCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.manipulator.WristUpCommand;
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
                    new Vec2d(driver.getRightX(), -driver.getLeftY()),
                    driver.getLeftX(), 1.0));

            s.schedule(new SetExtensionPowerCommand(extension,
                    driver.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) -
                            driver.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)));
            telemetry.addData("Extension Power",
                    driver.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) -
                    driver.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER));


            if (driver.wasJustPressed(GamepadKeys.Button.B)) {
                new SetExtensionElevationCommand(extension, (int) (90 * 6.2732)).schedule();
            }
            if (driver.wasJustPressed(GamepadKeys.Button.X)) {
                new SetExtensionElevationCommand(extension, (int) (20 * 6.2732)).schedule();
            }

            if (driver.wasJustPressed(GamepadKeys.Button.RIGHT_BUMPER)) {
                new SetExtensionElevationCommand(extension, (extension.getElevationMotorPosition() + 100)).schedule();
            } else if (driver.wasJustPressed(GamepadKeys.Button.LEFT_BUMPER)) {
                new SetExtensionElevationCommand(extension, (extension.getElevationMotorPosition() - 100)).schedule();
            }


            if (driver.isDown(GamepadKeys.Button.A)) {
                new IntakeCommand(manipulator).schedule();
                telemetry.addLine("a");
                telemetry.addData("Left Servo Power: ", manipulator.getLeftServoPower());
                telemetry.addData("Right Servo Power: ", manipulator.getRightServoPower());
            } else if (driver.isDown(GamepadKeys.Button.Y)) {
                new DepositCommand(manipulator).schedule();
                telemetry.addLine("y");
                telemetry.addData("Left Servo Power: ", manipulator.getLeftServoPower());
                telemetry.addData("Right Servo Power: ", manipulator.getRightServoPower());
            } else {
                new IdleCommand(manipulator).schedule();
                telemetry.addLine("nothing");
                telemetry.addData("Left Servo Power: ", manipulator.getLeftServoPower());
                telemetry.addData("Right Servo Power: ", manipulator.getRightServoPower());
            }


            if (driver.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
                new WristDownCommand(manipulator).schedule();
            }
            if (driver.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                new WristUpCommand(manipulator).schedule();
            }

            new RunExtensionPidCommand(extension).schedule();

            telemetry.update();
            s.run();
        }
    }
}
