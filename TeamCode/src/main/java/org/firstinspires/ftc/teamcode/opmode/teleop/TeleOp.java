package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.button.Button;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ClawIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ClawOuttakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.StopClawCommand;
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

    private TeleOpDriveCommand driveCommand;

    private ClawIntakeCommand intakeCommand;
    private StopClawCommand stopClawCommand;
    private ClawOuttakeCommand outtakeCommand;

    private ManualPivotDownCommand pivotDownCommand;
    private ManualPivotUpCommand pivotUpCommand;

    private ManualExtensionOutCommand extensionOutCommand;
    private ManualExtensionInCommand extensionInCommand;

    private ManualWristAngleCommand wristUpCommand;
    private ManualWristAngleCommand wristDownCommand;
    private ManualWristTwistCommand wristTwistLeftCommand;
    private ManualWristTwistCommand wristTwistRightCommand;

    private GamepadEx driverGamepad, operatorGamepad;

    private MultipleTelemetry telem;


    @Override
    public void initialize() {

        telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        driverGamepad = new GamepadEx(gamepad1);
        operatorGamepad = new GamepadEx(gamepad2);

        bot = new Bot(telem, hardwareMap);

        //region Drivetrain
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
        //endregion

        //region Claw
        claw = bot.getClaw();

        intakeCommand = new ClawIntakeCommand(claw);
        stopClawCommand = new StopClawCommand(claw);
        outtakeCommand = new ClawOuttakeCommand(claw);

        Button intakeButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.A))
                .whenPressed(intakeCommand).whenReleased(stopClawCommand);

        Button outtakeButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.B))
                .whenPressed(outtakeCommand.withTimeout(2000));

        register(claw);
        //endregion

        //region Pivot
        pivot = bot.getPivot();

        pivotDownCommand = new ManualPivotDownCommand(pivot);
        pivotUpCommand = new ManualPivotUpCommand(pivot);

        ToggleStateCommand toggleStateCommand = new ToggleStateCommand(bot);
        Button toggleStateButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.START))
                .whenPressed(toggleStateCommand);

        Button pivotDownButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.X))
                .whenPressed(pivotDownCommand);
        Button pivotUpButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.B))
                .whenPressed(pivotUpCommand);

        register(pivot);
        //endregion

        //region Wrist
        wrist = bot.getWrist();

        wristUpCommand = new ManualWristAngleCommand(wrist, Direction.UP);
        wristDownCommand = new ManualWristAngleCommand(wrist, Direction.DOWN);
        wristTwistLeftCommand = new ManualWristTwistCommand(wrist, Direction.LEFT);
        wristTwistRightCommand = new ManualWristTwistCommand(wrist, Direction.RIGHT);

        Button wristUpButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_UP))
                .whenPressed(wristUpCommand);
        Button wristDownButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_DOWN))
                .whenPressed(wristDownCommand);
        Button wristTwistLeftButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_LEFT))
                .whenPressed(wristTwistLeftCommand);
        Button wristTwistRightButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_RIGHT))
                .whenPressed(wristTwistRightCommand);

        register(wrist);
        //endregion

        //region Extension
        extension = bot.getExtension();

        Button extensionOutButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.LEFT_BUMPER))
                .whenPressed(new ManualExtensionOutCommand(extension));
        Button extensionInButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.RIGHT_BUMPER))
                .whenPressed(new ManualExtensionInCommand(extension));
        register(extension);
        //endregion


    }
}