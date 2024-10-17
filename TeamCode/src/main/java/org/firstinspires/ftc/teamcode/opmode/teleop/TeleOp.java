package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.TeleOpDriveCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "TeleOp")
public class TeleOp extends CommandOpMode {

    private Bot bot;
    private MecanumDrivetrain drivetrain;

    private TeleOpDriveCommand driveCommand;

    private GamepadEx driverGamepad, operatorGamepad;

    @Override
    public void initialize() {

        driverGamepad = new GamepadEx(gamepad1);

        bot = new Bot(telemetry, hardwareMap);
        drivetrain = bot.getDrivetrain();

        driveCommand = new TeleOpDriveCommand(
                drivetrain,
                () ->driverGamepad.getLeftX(),
                ()-> driverGamepad.getLeftY(),
                () -> driverGamepad.getRightX(),
                () -> 0.8
        );

        register(drivetrain);
        drivetrain.setDefaultCommand(driveCommand);
    }
}
