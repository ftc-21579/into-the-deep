package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.button.Button;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ClawIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ClawOuttakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.StopClawCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.TeleOpDriveCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Claw;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "TeleOp")
public class TeleOp extends CommandOpMode {

    private Bot bot;
    private Claw claw;
    private MecanumDrivetrain drivetrain;

    private TeleOpDriveCommand driveCommand;

    private ClawIntakeCommand intakeCommand;
    private StopClawCommand stopClawCommand;
    private ClawOuttakeCommand outtakeCommand;

    private GamepadEx driverGamepad, operatorGamepad;

    @Override
    public void initialize() {

        driverGamepad = new GamepadEx(gamepad1);

        bot = new Bot(telemetry, hardwareMap);
        drivetrain = bot.getDrivetrain();

        driveCommand = new TeleOpDriveCommand(
                drivetrain,
                () -> -driverGamepad.getRightX(),
                () -> driverGamepad.getLeftY(),
                () -> -driverGamepad.getLeftX(),
                () -> 0.8
        );

        register(drivetrain);
        drivetrain.setDefaultCommand(driveCommand);

        claw = bot.getClaw();

        intakeCommand = new ClawIntakeCommand(claw);
        stopClawCommand = new StopClawCommand(claw);
        outtakeCommand = new ClawOuttakeCommand(claw);

        Button intakeButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.A))
                .whenPressed(intakeCommand).whenReleased(stopClawCommand);

        Button outtakeButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.B))
                .whenPressed(outtakeCommand.withTimeout(2000));

    }
}