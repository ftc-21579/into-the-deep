package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.button.Button;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ClawIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ClawOuttakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ToggleClawCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.TeleOpDriveCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.ManualExtensionInCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.ManualExtensionOutCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.ManualPivotDownCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.ManualPivotUpCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToggleStateCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.ManualWristAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.ManualWristTwistCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Claw;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Wrist;
import org.firstinspires.ftc.teamcode.common.intothedeep.Direction;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "TeleOp")
public class TeleOp extends CommandOpMode {

    private Bot bot;
    private Claw claw;
    private Pivot pivot;
    private Extension extension;
    private Wrist wrist;
    private MecanumDrivetrain drivetrain;

    private GamepadEx driverGamepad;

    private MultipleTelemetry telem;


    // Gamepad layout
    // https://www.padcrafter.com/?templates=Gamepad+1&plat=1&col=%23242424%2C%23606A6E%2C%23FFFFFF&rightStick=Yaw%2FRotation&leftStick=Translation&dpadUp=Wrist+Up&dpadRight=Wrist+Clockwise&dpadLeft=Wrist+Counter-Clockwise&dpadDown=Wrist+Down&aButton=Toggle+State&yButton=Specimen+Auto+Deposit&xButton=Sample+Auto+Deposit&bButton=Toggle+Claw&rightTrigger=Extension+Out&leftTrigger=Extension+In&leftBumper=Pivot+Down&rightBumper=Pivot+Up
    @Override
    public void initialize() {

        telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        driverGamepad = new GamepadEx(gamepad1);

        bot = new Bot(telem, hardwareMap);

        //region Drivetrain
        drivetrain = bot.getDrivetrain();

        TeleOpDriveCommand driveCommand = new TeleOpDriveCommand(
                drivetrain,
                () -> -driverGamepad.getRightX(),
                () -> driverGamepad.getLeftY(),
                () -> -driverGamepad.getLeftX(),
                () -> 0.8
        );

        register(drivetrain);
        drivetrain.setDefaultCommand(driveCommand);
        //endregion

        //region Claw
        claw = bot.getClaw();

        Button clawToggle = (new GamepadButton(driverGamepad, GamepadKeys.Button.B))
                .whenPressed(
                        new ToggleClawCommand(claw)
                );

        register(claw);
        //endregion

        //region Pivot
        pivot = bot.getPivot();

        Button pivotDownButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.LEFT_BUMPER))
                .whenPressed(
                        new ManualPivotDownCommand(pivot)
                );
        Button pivotUpButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.RIGHT_BUMPER))
                .whenPressed(
                        new ManualPivotUpCommand(pivot)
                );

        register(pivot);
        //endregion

        //region Wrist
        wrist = bot.getWrist();

        Button wristUpButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_UP))
                .whenPressed(
                        new ManualWristAngleCommand(wrist, Direction.UP)
                );
        Button wristDownButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_DOWN))
                .whenPressed(
                        new ManualWristAngleCommand(wrist, Direction.DOWN)
                );
        Button wristTwistLeftButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_LEFT))
                .whenPressed(
                        new ManualWristTwistCommand(wrist, Direction.LEFT)
                );
        Button wristTwistRightButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_RIGHT))
                .whenPressed(
                        new ManualWristTwistCommand(wrist, Direction.RIGHT)
                );

        register(wrist);
        //endregion

        //region Extension
        extension = bot.getExtension();
        register(extension);
        //endregion

        // region State

        Button stateButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.A))
                .whenPressed(
                        new ToggleStateCommand(bot)
                );
        //endregion

        //region Automation

        //endregion

    }

    @Override
    public void run() {

        CommandScheduler.getInstance().schedule(
                new ManualExtensionOutCommand(extension, driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER))
        );
        CommandScheduler.getInstance().schedule(
                new ManualExtensionInCommand(extension, driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER))
        );

    }
}